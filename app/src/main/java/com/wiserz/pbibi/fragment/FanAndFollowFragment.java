package com.wiserz.pbibi.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackie on 2017/10/12 9:46.
 * QQ : 971060378
 * Used as : 粉丝和关注的页面
 */
public class FanAndFollowFragment extends BaseFragment {

    private List<BaseFragment> mBaseFragment;
    private int position;
    private Fragment fromFragment;
    private int user_id;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fan_follow;
    }

    @Override
    protected void initView(View view) {
        user_id = getArguments().getInt(Constant.USER_ID);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("关注和粉丝");

        RadioGroup mRg_main = (RadioGroup) view.findViewById(R.id.rg_main);

        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new FollowListFragment());
        mBaseFragment.add(new FanListFragment());

        mRg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //设置默认选中常用框架
        mRg_main.check(R.id.rb_follow);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            default:
                break;
        }
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_follow://关注
                    position = 0;
                    DataManager.getInstance().setData1(user_id);
                    break;
                case R.id.rb_fan://粉丝
                    position = 1;
                    DataManager.getInstance().setData1(user_id);
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

    private void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            fromFragment = to;
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            //才切换
            //判断有没有被添加
            if (!to.isAdded()) {
                //to没有被添加
                //from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //添加to
                ft.add(R.id.fl_content_for_fan_and_follow, to).commit();
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

    private BaseFragment getFragment() {
        return mBaseFragment.get(position);
    }
}
