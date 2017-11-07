package com.wiserz.pbibi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;
import com.wiserz.pbibi.BaseApplication;
import com.wiserz.pbibi.fragment.BaseFragment;
import com.wiserz.pbibi.util.Constant;

import java.util.List;

/**
 * Created by jackie on 2017/8/9 13:25.
 * QQ : 971060378
 * Used as : Activity的基类
 */
public class BaseActivity extends AppCompatActivity {

    private DisplayMetrics mDisplaymetrics;

    private boolean mIsFullScreen;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public DisplayMetrics getDisplaymetrics() {
        if (mDisplaymetrics == null) {
            mDisplaymetrics = new DisplayMetrics();
            ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(mDisplaymetrics);
        }
        return mDisplaymetrics;
    }

    public void setScreenFull(boolean isFull) {
        if (mIsFullScreen == isFull) {
            return;
        }

        if (isFull) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(params);
            mIsFullScreen = true;
        } else {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(params);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            mIsFullScreen = false;
        }

    }

    /**
     * 页面跳转，如果返回true,则基类已经处理，否则没有处理
     *
     * @param pagerClass
     * @param bundle
     * @return
     */
    public boolean gotoPager(Class<?> pagerClass, Bundle bundle) {
        return gotoPager(pagerClass, bundle, false);
    }

    /**
     * 页面跳转，如果返回true,则基类已经处理，否则没有处理
     *
     * @param pagerClass
     * @param bundle
     * @return
     */
    public boolean gotoPager(Class<?> pagerClass, Bundle bundle, boolean isGoTwo) {
        if (Activity.class.isAssignableFrom(pagerClass)) { //Activity的情况
            Intent intent = new Intent(this, pagerClass);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            startActivity(intent);
            return true;
        } else {
            if (BaseFragment.class.isAssignableFrom(pagerClass)) {
                String name = pagerClass.getName();
                if (!isGoTwo) // 使用EmptyActiviy
                {
                    Intent intent = new Intent(this, EmptyActivity.class);
                    if (bundle != null) {
                        intent.putExtras(bundle);
                    }
                    intent.putExtra(Constant.FRAGMENT_NAME, name);
                    startActivity(intent);
                    return true;
                } else {
                    Intent intent = new Intent(this, EmptyTwoActivity.class);
                    if (bundle != null) {
                        intent.putExtras(bundle);
                    }
                    intent.putExtra(Constant.FRAGMENT_NAME, name);
                    startActivity(intent);
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 返回，如果stack中还有Fragment的话，则返回stack中的fragment，否则 finish当前的Activity
     */
    public void goBack() {
        getSupportFragmentManager().executePendingTransactions();
        int nSize = getSupportFragmentManager().getBackStackEntryCount();
        if (nSize > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    /**
     * 获取当前可见Fragment
     *
     * @return
     */
    public BaseFragment getVisibleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments == null) {
            return null;
        }
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return (BaseFragment) fragment;
        }
        return BaseApplication.getCurFragment();
    }

    public void onResume() {
        super.onResume();
        //友盟
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
