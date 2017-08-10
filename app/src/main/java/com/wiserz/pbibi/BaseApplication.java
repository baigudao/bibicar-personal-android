package com.wiserz.pbibi;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.wiserz.pbibi.fragment.BaseFragment;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import io.rong.imkit.RongIM;
import okhttp3.OkHttpClient;

/**
 * Created by jackie on 2017/8/9 9:55.
 * QQ : 971060378
 * Used as : 应用的基类
 */
public class BaseApplication extends Application {

    private static BaseFragment curFragment;

    @Override
    public void onCreate() {
        super.onCreate();
        //AndroidUtilCode库的初始化
        Utils.init(this);

        //配置OKHttp
        configureOkHttp();

        //动态设置内存缓存size
        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);

        //融云初始化
        RongIM.init(this);
    }

    private void configureOkHttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static void setCurFragment(BaseFragment fragment) {
        curFragment = fragment;
    }

    public static BaseFragment getCurFragment() {
        return curFragment;
    }
}
