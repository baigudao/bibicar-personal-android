package com.wiserz.pbibi.activity;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by jackie on 2017/8/9 13:25.
 * QQ : 971060378
 * Used as : Activity的基类
 */
public class BaseActivity extends AppCompatActivity {

    //    /**
    //     * 页面跳转，如果返回true,则基类已经处理，否则没有处理
    //     *
    //     * @param pagerClass
    //     * @param bundle
    //     * @return
    //     */
    //    public boolean gotoPager(Class<?> pagerClass, Bundle bundle) {
    //        if (Activity.class.isAssignableFrom(pagerClass)) { //Activity的情况
    //            Intent intent = new Intent(this, pagerClass);
    //            if (bundle != null) {
    //                intent.putExtras(bundle);
    //            }
    //            startActivity(intent);
    //            return true;
    //        } else {
    //            if (BaseFragment.class.isAssignableFrom(pagerClass)) { //Fragment的情况
    //                String name = pagerClass.getName();
    //                Intent intent = new Intent(this, EmptyActivity.class);
    //                if (bundle != null) {
    //                    intent.putExtras(bundle);
    //                }
    //                intent.putExtra(Constant.FRAGMENT_NAME, name);
    //                startActivity(intent);
    //                return true;
    //            }
    //            return false;
    //        }
    //    }
    //
    //    /**
    //     * 返回，如果stack中还有Fragment的话，则返回stack中的fragment，否则 finish当前的Activity
    //     */
    //    public void goBack() {
    //        getSupportFragmentManager().executePendingTransactions();
    //        int nSize = getSupportFragmentManager().getBackStackEntryCount();
    //        if (nSize > 1) {
    //            getSupportFragmentManager().popBackStack();
    //        } else {
    //            finish();
    //        }
    //    }

}
