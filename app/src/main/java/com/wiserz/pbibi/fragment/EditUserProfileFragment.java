package com.wiserz.pbibi.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.LoginBean;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.util.GifSizeFilter;
import com.wiserz.pbibi.view.CircleImageView;
import com.wiserz.pbibi.view.WheelViewPopupWindow;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
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
 * Created by jackie on 2017/8/21 14:50.
 * QQ : 971060378
 * Used as : 编辑个人资料的页面
 */
public class EditUserProfileFragment extends BaseFragment {

    private static final int REQUEST_CODE_CHOOSE = 55;
    private CircleImageView iv_circle_image;
    private EditText et_edit_nickname;
    private TextView tv_edit_gender;
    private EditText et_edit_profile;

    private int mGender;
    private String file_string;
    private String upload_token;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_user_profile;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("编辑资料");
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setVisibility(View.VISIBLE);
        btn_register.setText("完成");
        btn_register.setOnClickListener(this);

        LoginBean.UserInfoBean userInfoBean = DataManager.getInstance().getUserInfo();
        et_edit_nickname = (EditText) view.findViewById(R.id.et_edit_nickname);
        view.findViewById(R.id.rl_choose_gender).setOnClickListener(this);
        tv_edit_gender = (TextView) view.findViewById(R.id.tv_edit_gender);
        et_edit_profile = (EditText) view.findViewById(R.id.et_edit_profile);
        iv_circle_image = (CircleImageView) view.findViewById(R.id.iv_circle_image);
        iv_circle_image.setOnClickListener(this);
        if (EmptyUtils.isNotEmpty(userInfoBean)) {
            Glide.with(mContext)
                    .load(userInfoBean.getProfile().getAvatar())
                    .placeholder(R.drawable.user_photo)
                    .error(R.drawable.user_photo)
                    .into(iv_circle_image);
            et_edit_nickname.setText(userInfoBean.getProfile().getNickname());
            tv_edit_gender.setText(String.valueOf(userInfoBean.getProfile().getGender() == 1 ? getString(R.string.man) : getString(R.string.woman)));
            et_edit_profile.setText(userInfoBean.getProfile().getSignature());
        }
    }

    private String getNickName() {
        return et_edit_nickname.getText().toString().trim();
    }

    private String getSignature() {
        return et_edit_profile.getText().toString().trim();
    }

    private String getGender() {
        return tv_edit_gender.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_register:
                commitDataToServer();
                break;
            case R.id.iv_circle_image:
                editImage();
                break;
            case R.id.rl_choose_gender:
                editGender();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
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

    private void commitDataToServer() {
        if (EmptyUtils.isNotEmpty(file_string) && EmptyUtils.isNotEmpty(upload_token)) {
            UploadManager uploadManager = new UploadManager();
            File file = new File(file_string);

            uploadManager.put(file, UUID.randomUUID().toString(), upload_token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject response) {
                    if (info.isOK()) {
                        LogUtils.e(file_string, "和" + key, "和" + upload_token);
                        //上传成功
                        int status = response.optInt("status");
                        JSONObject jsonObjectData = response.optJSONObject("data");
                        LogUtils.e(response);
                        if (status == 1) {
                            String hash = jsonObjectData.optString("hash");
                            if (EmptyUtils.isNotEmpty(hash)) {
                                commitDataToRealServer(hash);
                            }
                        } else {
                            String code = response.optString("code");
                            String msg = jsonObjectData.optString("msg");
                            ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
                        }
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShort("车辆照片上传失败，请重新上传");
                            }
                        });
                    }
                }
            }, null);
        }
    }

    private void commitDataToRealServer(String hash) {
        OkHttpUtils.post()
                .url(Constant.getUserInfoUpdateUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.NICKNAME, getNickName())
                .addParams(Constant.AVATAR, hash)
                .addParams("gender", getGender())
                .addParams("signature", getSignature())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e(response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                ToastUtils.showShort("更新成功");
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

    private void editGender() {
        WheelViewPopupWindow wheelViewPopupWindow = new WheelViewPopupWindow(getActivity(), new WheelViewPopupWindow.OnSelectItemListener() {
            @Override
            public void onSelect(int index1, Object value1, int index2, Object value2, int index3, Object value3) {
                mGender = index1 + 1;
                tv_edit_gender.setText(mGender == 1 ? getString(R.string.man) : getString(R.string.woman));
            }
        }, WheelViewPopupWindow.WHEEL_VIEW_WINDOW_TYPE.TYPE_GENDER);
        wheelViewPopupWindow.initView();
        wheelViewPopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void editImage() {
        Matisse.from(EditUserProfileFragment.this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))//MimeType.ofAll()
                .countable(false)
                .maxSelectable(1)
                .theme(R.style.Matisse_Zhihu)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                //                .capture(true)
                //                .captureStrategy(new CaptureStrategy(true,"com.bibicar.capture"))
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    List<Uri> mSelected;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);

            if (EmptyUtils.isNotEmpty(mSelected)) {
                Glide.with(mContext)
                        .load(mSelected.get(0))
                        .placeholder(R.drawable.user_photo)
                        .error(R.drawable.user_photo)
                        .into(iv_circle_image);

                file_string = CommonUtil.getRealFilePath(mContext, mSelected.get(0));
            }
        }
    }
}
