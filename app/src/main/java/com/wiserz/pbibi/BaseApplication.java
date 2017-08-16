package com.wiserz.pbibi;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.mob.MobSDK;
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
    private static Context appContext ;

    public static Context getAppContext() {
        return appContext;
    }

    public static void setAppContext(Context appContext) {
        BaseApplication.appContext = appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BaseApplication.setAppContext(getApplicationContext());

        //AndroidUtilCode库的初始化
        Utils.init(this);

        //配置OKHttp
        configureOkHttp();

        //动态设置内存缓存size
        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);

        //融云初始化
        RongIM.init(this);

        //对LogUtils的配置
        configLogUtils();

        //初始社会化组件
        MobSDK.init(this,"174e89a173460","");
    }

    private void configLogUtils() {
        LogUtils.Builder lBuilder = new LogUtils.Builder()
                .setLogSwitch(BuildConfig.DEBUG)// 设置log总开关，默认开
                .setGlobalTag("数据>>>")// 设置log全局标签，默认为空
                // 当全局标签不为空时，我们输出的log全部为该tag，
                // 为空时，如果传入的tag为空那就显示类名，否则显示tag
                .setLog2FileSwitch(false)// 打印log时是否存到文件的开关，默认关
                .setBorderSwitch(false)// 输出日志是否带边框开关，默认开
                .setFileFilter(LogUtils.E);// log过滤器，和logcat过滤器同理，默认Verbose
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
