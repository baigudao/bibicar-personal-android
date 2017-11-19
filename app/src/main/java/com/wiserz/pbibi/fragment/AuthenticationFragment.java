package com.wiserz.pbibi.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jackie on 2017/8/18 23:04.
 * QQ : 971060378
 * Used as : 身份验证的页面
 */

public class AuthenticationFragment extends BaseFragment {

    private EditText et_input_name;
    private EditText et_input_num;

    private ImageView iv_card_cur;
    private ImageView iv_card_opp;
    private ImageView iv_drive_cur;
    private ImageView iv_drive_opp;
    private Button btn_commit;
    private String upload_token;
    private UploadManager uploadManager;
    private int REQUEST_CODE_CHOOSE = 55;

    private int position;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_authentication;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("认证身份信息");

        et_input_name = (EditText) view.findViewById(R.id.et_input_name);
        et_input_num = (EditText) view.findViewById(R.id.et_input_num);

        iv_card_cur = (ImageView) view.findViewById(R.id.iv_card_cur);
        iv_card_cur.setOnClickListener(this);
        iv_card_opp = (ImageView) view.findViewById(R.id.iv_card_opp);
        iv_card_opp.setOnClickListener(this);
        iv_drive_cur = (ImageView) view.findViewById(R.id.iv_drive_cur);
        iv_drive_cur.setOnClickListener(this);
        iv_drive_opp = (ImageView) view.findViewById(R.id.iv_drive_opp);
        iv_drive_opp.setOnClickListener(this);
        btn_commit = (Button) view.findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);

        uploadManager = new UploadManager();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.iv_card_cur:
                position = 0;
                selectImage();
                break;
            case R.id.iv_card_opp:
                position = 1;
                selectImage();
                break;
            case R.id.iv_drive_cur:
                position = 2;
                selectImage();
                break;
            case R.id.iv_drive_opp:
                position = 3;
                selectImage();
                break;
            case R.id.btn_commit:
                ToastUtils.showShort("提交");
                commitData();
                break;
            default:
                break;
        }
    }

    private void commitData() {
        //进行用户姓名和身份证的判断

        if (EmptyUtils.isNotEmpty(uriArrayList) && uriArrayList.size() == 4 && EmptyUtils.isNotEmpty(upload_token)) {
            final HashMap<String, String> stringHashMap = new HashMap<>();
            for (int i = 0; i < uriArrayList.size(); i++) {
                File file = new File(CommonUtil.getRealFilePath(mContext, uriArrayList.get(i)));
                uploadManager.put(file, "证件资料图片" + i, upload_token, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (info.isOK()) {
                            //上传成功
                            int status = response.optInt("status");
                            JSONObject jsonObjectData = response.optJSONObject("data");
                            if (status == 1) {
                                String hash = jsonObjectData.optString("hash");
                                LogUtils.e(hash);
                                stringHashMap.put(key, hash);
                                if (stringHashMap.size() == 4) {
                                    commitDataToServer(stringHashMap);
                                }
                            } else {
                                String code = response.optString("code");
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("" + msg);
                            }
                        } else {
                            ToastUtils.showShort("证件资料上传失败，请重新上传");
                        }
                    }
                }, null);
            }
        } else {
            ToastUtils.showShort("请添加完整的证件资料");
            return;
        }


        ToastUtils.showShort("heh");
    }

    private void commitDataToServer(HashMap<String, String> stringHashMap) {
        for (String key : stringHashMap.keySet()) {
            LogUtils.e("key= " + key + " and value= " + stringHashMap.get(key));
        }
    }

    private void selectImage() {
        Matisse.from(AuthenticationFragment.this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .theme(R.style.Matisse_Zhihu)
                .countable(false)
                .maxSelectable(1)
//                        .capture(true)
//                        .captureStrategy(new CaptureStrategy(true,"李"))
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    private String getCardNumber() {
        return et_input_name.getText().toString().trim();
    }

    private String getUserName() {
        return et_input_num.getText().toString().trim();
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
                                ToastUtils.showShort("" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    List<Uri> mSelected;
    ArrayList<Uri> uriArrayList = new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            switch (position) {
                case 0:
                    iv_card_cur.setImageURI(mSelected.get(0));
                    uriArrayList.add(mSelected.get(0));
                    break;
                case 1:
                    iv_card_opp.setImageURI(mSelected.get(0));
                    uriArrayList.add(mSelected.get(0));
                    break;
                case 2:
                    iv_drive_cur.setImageURI(mSelected.get(0));
                    uriArrayList.add(mSelected.get(0));
                    break;
                case 3:
                    iv_drive_opp.setImageURI(mSelected.get(0));
                    uriArrayList.add(mSelected.get(0));
                    break;
                default:
                    break;
            }
        }
    }
}
