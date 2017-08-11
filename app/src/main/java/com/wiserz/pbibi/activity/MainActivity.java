package com.wiserz.pbibi.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.wiserz.pbibi.R;
import com.wiserz.pbibi.fragment.BaseFragment;
import com.wiserz.pbibi.fragment.CarCenterFragment;
import com.wiserz.pbibi.fragment.CommunityFragment;
import com.wiserz.pbibi.fragment.MessageFragment;
import com.wiserz.pbibi.fragment.MyFragment;
import com.wiserz.pbibi.fragment.RecommendFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private RadioGroup mRg_main;
    /**
     * 装Fragment的集合
     */
    private List<BaseFragment> mBaseFragment;

    /**
     * 选中Fragment对应的位置
     */
    private int position;

    /**
     * 上次的Fragment
     */
    private Fragment fromFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化View
        initView();
        //初始化Fragment
        initFragment();
        //设置RadioGroup的监听
        setListener();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        mRg_main = (RadioGroup) findViewById(R.id.rg_main);
    }

    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new RecommendFragment());//推荐
        mBaseFragment.add(new CommunityFragment());//圈子
        mBaseFragment.add(new CarCenterFragment());//汽车中心
        mBaseFragment.add(new MessageFragment());//消息
        mBaseFragment.add(new MyFragment());//我的
    }

    private void setListener() {
        mRg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //设置默认选中常用框架
        mRg_main.check(R.id.rb_recommend);
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_recommend://推荐
                    position = 0;
                    break;
                case R.id.rb_community://圈子
                    position = 1;
                    break;
                case R.id.rb_car_center://汽车中心
                    position = 2;
                    break;
                case R.id.rb_message://消息
                    position = 3;
                    break;
                case R.id.rb_mine://我的
                    position = 4;
                    break;
                default:
                    position = 0;
                    break;
            }
            //根据位置得到对应的Fragment
            BaseFragment toFragment = getFragment();
            //切换Fragment
            switchFragment(fromFragment, toFragment);
        }
    }


    /**
     * @param from 刚显示的Fragment,马上就要被隐藏了
     * @param to   马上要切换到的Fragment，一会要显示
     */
    private void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            fromFragment = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //才切换
            //判断有没有被添加
            if (!to.isAdded()) {
                //to没有被添加
                //from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //添加to
                ft.add(R.id.fl_content, to).commit();
            } else {
                //to已经被添加
                // from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //显示to
                ft.show(to).commit();
            }
        }
    }

    /**
     * 根据位置得到对应的Fragment
     *
     * @return
     */
    private BaseFragment getFragment() {
        BaseFragment fragment = mBaseFragment.get(position);
        return fragment;
    }

    //    private long startTime = 0;
    //    private Toast toast = null;
    //
    //    @Override
    //    public boolean onKeyDown(int keyCode, KeyEvent event) {
    //        if (keyCode == KeyEvent.KEYCODE_BACK) {
    //            long currentTime = System.currentTimeMillis();
    //            if ((currentTime - startTime) > 2000) {
    //                toast = Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT);
    //                toast.show();
    //                startTime = currentTime;
    //            } else {
    //                finish();
    //                if (toast != null) {
    //                    toast.cancel();
    //                }
    //            }
    //        }
    //        return super.onKeyDown(keyCode, event);
    //    }
}
