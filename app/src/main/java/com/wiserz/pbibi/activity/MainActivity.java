package com.wiserz.pbibi.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.fragment.BaseFragment;
import com.wiserz.pbibi.fragment.CarCenterFragment;
import com.wiserz.pbibi.fragment.CommunityFragment;
import com.wiserz.pbibi.fragment.MessageFragment;
import com.wiserz.pbibi.fragment.MyFragment;
import com.wiserz.pbibi.fragment.MyFragmentForCompany;
import com.wiserz.pbibi.fragment.RecommendFragment;
import com.wiserz.pbibi.util.DataManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private RadioGroup mRg_main;
    private List<BaseFragment> mBaseFragment;
    private int position;
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
        mBaseFragment.add(new MyFragmentForCompany());//企业中心
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
                    int type = DataManager.getInstance().getUserInfo().getProfile().getType();//1,表示个人用户；2，表示企业用户
                    LogUtils.e("用户类型===" + type);
                    if (type == 1) {
                        position = 4;
                    } else if (type == 2) {
                        position = 5;
                    }
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

    public void setCheck(RadioButton radioButton) {
        ((RadioButton) findViewById(R.id.rb_recommend)).setChecked(false);
        ((RadioButton) findViewById(R.id.rb_community)).setChecked(false);
        ((RadioButton) findViewById(R.id.rb_car_center)).setChecked(false);
        ((RadioButton) findViewById(R.id.rb_message)).setChecked(false);
        ((RadioButton) findViewById(R.id.rb_mine)).setChecked(false);
        radioButton.setChecked(true);
    }

    long startTime = 0;

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - startTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            startTime = currentTime;
        } else {
            finish();
        }
    }
}
