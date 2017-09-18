package com.wiserz.pbibi.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jackie on 2017/8/14 16:02.
 * QQ : 971060378
 * Used as : 创建话题页面
 */
public class CreateTopicFragment extends BaseFragment {


    private String upload_token;
    private EditText et_input_name;
    private EditText et_input_info;

    private int REQUEST_CODE_CHOOSE = 55;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_create_topic;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("创建话题");
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setVisibility(View.VISIBLE);
        btn_register.setText("创建");
        btn_register.setOnClickListener(this);
        view.findViewById(R.id.ll_setting_background).setOnClickListener(this);

        et_input_name = (EditText) view.findViewById(R.id.et_input_name);
        et_input_info = (EditText) view.findViewById(R.id.et_input_info);
    }

    private String getTopicTitle() {
        return et_input_name.getText().toString().trim();
    }

    private String getTopicContent() {
        return et_input_info.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_register:
                postDataToQiNiu();
                break;
            case R.id.ll_setting_background:
                selectImage();
                break;
            default:
                break;
        }
    }

    private void selectImage() {
        Matisse.from(CreateTopicFragment.this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .theme(R.style.Matisse_Zhihu)
                .countable(false)
                .maxSelectable(1)
                //                        .capture(true)
                //                        .captureStrategy(new CaptureStrategy(true,"李"))
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void initData() {
        super.initData();
        getTokenFromNet();
    }

    private void getTokenFromNet() {
        OkHttpUtils.post()
                .url(Constant.getUploadTokenUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                upload_token = jsonObjectData.optString("upload_token");
                            } else {
                                String code = jsonObject.optString("code");
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void postDataToQiNiu() {
        if (EmptyUtils.isEmpty(getTopicTitle()) && EmptyUtils.isEmpty(getTopicContent())) {
            ToastUtils.showShort("请填写完整的信息");
            return;
        }
        if (EmptyUtils.isEmpty(mSelected)) {
            ToastUtils.showShort("请上传话题图片");
            return;
        }
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        UploadManager uploadManager = new UploadManager();
        String filePath = CommonUtil.getRealFilePath(mContext, mSelected.get(0));
        File file = new File(filePath);
        if (EmptyUtils.isNotEmpty(filePath) && file.exists() && EmptyUtils.isNotEmpty(upload_token)) {
            uploadManager.put(file, UUID.randomUUID().toString(), upload_token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject response) {//hash
                    if (info.isOK()) {
                        //上传成功
                        int status = response.optInt("status");
                        JSONObject jsonObjectData = response.optJSONObject("data");
                        if (status == 1) {
                            String hash = jsonObjectData.optString("hash");
                            putDataToServer(hash);
                        } else {
                            String code = response.optString("code");
                            String msg = jsonObjectData.optString("msg");
                            ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
                        }
                    } else {
                        ToastUtils.showShort("上传图片失败，请重新上传");
                    }
                }
            }, null);//如果需要进度通知、crc 校验、中途取消、指定 mimeType，则需要填写相应字段，详见下面的 UploadOptions 参数说明
        } else {
            ToastUtils.showShort("请设置话题背景图！");
        }
    }

    private void putDataToServer(String hash) {
        OkHttpUtils.post()
                .url(Constant.getCreateTopicUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.THEME, "#" + getTopicTitle() + "#")
                .addParams(Constant.POST_FILE, hash)
                .addParams(Constant.TITLE, getTopicContent())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                ToastUtils.showShort("创建话题成功");
                                goBack();
                            } else {
                                String code = jsonObject.optString("code");
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    List<Uri> mSelected;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            if (getView() != null) {
                getView().findViewById(R.id.ll_setting_background).setVisibility(View.GONE);
                ((ImageView) getView().findViewById(R.id.iv_image_topic)).setImageURI(mSelected.get(0));
            }
        }
    }
}
