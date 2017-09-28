package com.wiserz.pbibi.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.view.CountDownView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import okhttp3.Call;

public class SplashActivity extends AppCompatActivity {

    private static final String[] APP_NEED_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,//定位
            Manifest.permission.RECORD_AUDIO, //录音
            Manifest.permission.CALL_PHONE,//打电话
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //读写
            Manifest.permission.CAMERA};//照相

    private static final int EXTERNAL_STORAGE_REQ_CODE = 10;//权限请求码

    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAdvertisementImageFromNet();//额外增加线程外任务，用于请求启动页面的数据并保存广告图片到SD卡
        flag = 0;
        initView();
    }

    private void initView() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort("当前无网络");
        }
        BarUtils.hideStatusBar(SplashActivity.this);
        setContentView(R.layout.activity_splash);
        ImageView iv_advertisement = (ImageView) findViewById(R.id.iv_advertisement);
        final CountDownView countDownView = (CountDownView) findViewById(R.id.countDownView);

        countDownView.setCountDownTimerListener(new CountDownView.CountDownTimerListener() {
            @Override
            public void onStartCount() {
                //               Toast.makeText(getApplicationContext(), "开始计时", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinishCount() {
                if (flag == 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //SDK版本大于等于23，也就是Android 6.0
                        requestPermission();//请求权限 Xiaomi6.0.1
                    } else {
                        //SDK版本小于23的走这
                        afterRequestPermission();//请求权限之后 Meizu5.1
                    }
                }
            }
        });
        countDownView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //SDK版本大于等于23，也就是Android 6.0
                    requestPermission();//请求权限 Xiaomi6.0.1
                } else {
                    //SDK版本小于23的走这
                    afterRequestPermission();//请求权限之后 Meizu5.1
                }
            }
        });

        String stringUrl = getAdvertisementImage();
        Glide.with(this)
                .load(stringUrl)
                .placeholder(R.drawable.start_pic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new GlideDrawableImageViewTarget(iv_advertisement) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        countDownView.setVisibility(View.VISIBLE);
                        countDownView.start();
                    }
                });
    }

    private String getAdvertisementImage() {
        final ArrayList<String> strings = new ArrayList<>();
        boolean is_have_image = SPUtils.getInstance().getBoolean(Constant.ADVERTISEMENT_IMAGE_SUCCESS);
        if (is_have_image) {
            int image_size = SPUtils.getInstance().getInt(Constant.ADVERTISEMENT_IMAGE_NUM);
            for (int i = 0; i < image_size; i++) {
                strings.add(SPUtils.getInstance().getString(Constant.ADVERTISEMENT_IMAGE + i));
            }
        } else {
            //默认的三张图片
            strings.add("http://img.bibicar.cn/chezhuzhaoweijia.jpeg");
            strings.add("http://img.bibicar.cn/chezhustory002.jpeg");
            strings.add("http://img.bibicar.cn/chezhustory003.jpeg");
        }
        Random random = new Random();
        int size = random.nextInt(strings.size());
        return strings.get(size);
    }

    private void requestPermission() {
        ArrayList<String> unCheckPermissions = null;
        String[] appNeedPermission = APP_NEED_PERMISSIONS;
        for (String permission : appNeedPermission) {
            if (ContextCompat.checkSelfPermission(SplashActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                if (unCheckPermissions == null) {
                    unCheckPermissions = new ArrayList<>();
                }
                unCheckPermissions.add(permission);
            }
        }
        if (unCheckPermissions != null && !unCheckPermissions.isEmpty()) {
            String[] array = new String[unCheckPermissions.size()];
            ActivityCompat.requestPermissions(this, unCheckPermissions.toArray(array), EXTERNAL_STORAGE_REQ_CODE);
        } else {
            afterRequestPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQ_CODE: {
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            finish();
                            return;
                        }
                    }
                    afterRequestPermission();
                } else {
                    afterRequestPermission();
                }
            }
            default:
                break;
        }
    }

    private void afterRequestPermission() {
        String device_identifier = SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER);
        if (EmptyUtils.isEmpty(device_identifier)) {
            String device_id = CommonUtil.getDeviceId(this);//设备id
            String device_resolution = ScreenUtils.getScreenWidth() + "*" + ScreenUtils.getScreenHeight();//设备分辨率
            String device_sys_version = Constant.DEVICE_ANDROID + Build.VERSION.SDK_INT;//版本号
            String device_type = String.valueOf(Constant.DEVICE_TYPE_ANDROID);//设备类型Apple/Android

            OkHttpUtils.post()
                    .url(Constant.getRegisterApp())
                    .addParams(Constant.DEVICE_ID, device_id)
                    .addParams(Constant.DEVICE_RESOLUTION, device_resolution)
                    .addParams(Constant.DEVICE_SYS_VERSION, device_sys_version)
                    .addParams(Constant.DEVICE_TYPE, device_type)
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
                                    //app注册，返回设备唯一标识，并保存
                                    String device_identifier = jsonObjectData.optString(Constant.DEVICE_IDENTIFIER);
                                    SPUtils.getInstance().put(Constant.DEVICE_IDENTIFIER, device_identifier);
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

        Intent intent;
        boolean isEnterGuideView = SPUtils.getInstance().getBoolean(Constant.IS_ENTER_GUIDE_VIEW);
        boolean is_user_login = SPUtils.getInstance().getBoolean(Constant.IS_USER_LOGIN);
        if (isEnterGuideView) {
            //如果进入了引导页面
            if (is_user_login) {
                //如果用户登录过，就直接进入主页面
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                //如果用户没有登录过，就进入登录注册页面
                intent = new Intent(SplashActivity.this, RegisterAndLoginActivity.class);
            }
        } else {
            //如果没有进入引导页面，就进入引导页面
            intent = new Intent(SplashActivity.this, GuideActivity.class);
        }
        startActivity(intent);
        finish();
    }

    /**
     * 用于请求启动页面的数据并保存广告图片到SD卡
     */
    private void getAdvertisementImageFromNet() {
        OkHttpUtils.get()
                .url(Constant.getSplashUrl())
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
                                JSONArray jsonArray = jsonObjectData.optJSONArray("url");
                                if (EmptyUtils.isNotEmpty(jsonArray)) {
                                    //保存广告图片的数量
                                    SPUtils.getInstance().put(Constant.ADVERTISEMENT_IMAGE_NUM, jsonArray.length());
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        //保存广告图片的地址
                                        SPUtils.getInstance().put(Constant.ADVERTISEMENT_IMAGE + i, jsonArray.optString(i));
                                    }
                                    //保存广告图片地址成功
                                    SPUtils.getInstance().put(Constant.ADVERTISEMENT_IMAGE_SUCCESS, true);
                                }

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
}
