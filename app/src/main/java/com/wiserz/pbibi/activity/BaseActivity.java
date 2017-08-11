package com.wiserz.pbibi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

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

    private static BaseActivity baseActivity;

    public static BaseActivity getBaseActivity() {
        return baseActivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = this;
    }

    /**
     * 页面跳转，如果返回true,则基类已经处理，否则没有处理
     *
     * @param pagerClass
     * @param bundle
     * @return
     */
    public boolean gotoPager(Class<?> pagerClass, Bundle bundle) {
        if (Activity.class.isAssignableFrom(pagerClass)) { //Activity的情况
            Intent intent = new Intent(this, pagerClass);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            startActivity(intent);
            return true;
        } else {
            if (BaseFragment.class.isAssignableFrom(pagerClass)) { //Fragment的情况
                String name = pagerClass.getName();
                Intent intent = new Intent(this, EmptyActivity.class);
                if (bundle != null) {
                    intent.putExtras(bundle);
                }
                intent.putExtra(Constant.FRAGMENT_NAME, name);
                startActivity(intent);
                return true;
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

}
