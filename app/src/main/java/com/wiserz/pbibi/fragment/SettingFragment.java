package com.wiserz.pbibi.fragment;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.activity.EmptyActivity;
import com.wiserz.pbibi.activity.RegisterAndLoginActivity;
import com.wiserz.pbibi.bean.LoginBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.util.GBExecutionPool;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/21 16:47.
 * QQ : 971060378
 * Used as : 系统设置页面
 */
public class SettingFragment extends BaseFragment {

    private TextView settings_cache_size;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView(View view) {
        DataManager.getInstance().setData1(null);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("系统设置");

        settings_cache_size = (TextView) view.findViewById(R.id.settings_cache_size);

//        view.findViewById(R.id.rl_binding_phone).setOnClickListener(this);
//        view.findViewById(R.id.rl_modify_pwd).setOnClickListener(this);
        view.findViewById(R.id.rl_clear_cache).setOnClickListener(this);
        view.findViewById(R.id.rl_about_us).setOnClickListener(this);
        view.findViewById(R.id.rl_give_grade).setOnClickListener(this);
        view.findViewById(R.id.tvLogout).setOnClickListener(this);

        LoginBean.UserInfoBean userInfoBean = DataManager.getInstance().getUserInfo();
        if(userInfoBean!=null){
            ((TextView)getView().findViewById(R.id.tv_binding_phone)).setText(TextUtils.isEmpty(userInfoBean.getMobile())?"":userInfoBean.getMobile());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.rl_binding_phone:
                gotoPager(BindingPhoneFragment.class, null);
                break;
//            case R.id.rl_modify_pwd:
//                gotoPager(ForgetPasswordFragment.class, null);
//                break;
            case R.id.rl_clear_cache:
                showDeleteCacheDialog();
                break;
            case R.id.rl_about_us:
                gotoPager(AboutUsFragment.class, null);
                break;
            case R.id.rl_give_grade:
                try {
                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    ToastUtils.showShort(getString(R.string.load_store_failed));
                }
                break;
            case R.id.tvLogout:
                showLogoutDialog();
                break;
            default:
                break;
        }
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        AlertDialog alertDialog = builder.setMessage("确定要退出BiBiCar吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logout();
                        SPUtils.getInstance().put(Constant.SESSION_ID,"");
                        SPUtils.getInstance().put(Constant.ACCOUNT, "");
                        SPUtils.getInstance().put(Constant.PASSWORD, "");
                        SPUtils.getInstance().put(Constant.CHAT_TOKEN, "");
                        SPUtils.getInstance().put(Constant.USER_ID, -1);
                        SPUtils.getInstance().put(Constant.IS_USER_LOGIN, false);
                        DataManager.getInstance().setUserInfo(null);
                        gotoPager(RegisterAndLoginActivity.class, null);
                        ((EmptyActivity) mContext).finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create();
        alertDialog.show();
    }

    private void logout() {
        OkHttpUtils.post()
                .url(Constant.getUserHomeUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.USER_ID, String.valueOf(SPUtils.getInstance().getInt(Constant.USER_ID)))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e(response);
                    }
                });
    }

    private void showDeleteCacheDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        AlertDialog alertDialog = builder.setMessage("清除手机缓存?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Glide.get(mContext).clearMemory();
                        GBExecutionPool.getExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                Glide.get(mContext).clearDiskCache();//清理磁盘缓存 需要在子线程中执行
                                ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        settings_cache_size.setText(getCacheSize(mContext) + "MB");
                                    }
                                });
                            }
                        });
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create();
        alertDialog.show();
    }
}
