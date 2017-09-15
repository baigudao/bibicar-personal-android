package com.wiserz.pbibi.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;

import com.wiserz.pbibi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackie on 2017/8/9 17:59.
 * QQ : 971060378
 * Used as : 圈子页面
 */
public class CommunityFragment extends BaseFragment {

    private List<BaseFragment> mBaseFragment;
    private int position;
    private Fragment fromFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_community;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.tv_create_topic).setOnClickListener(this);
        RadioGroup mRg_main = (RadioGroup) view.findViewById(R.id.rg_main);

        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new HallFragment());
        mBaseFragment.add(new FollowFragment());

        mRg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //设置默认选中常用框架
        mRg_main.check(R.id.rb_hall);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_create_topic:
                gotoPager(CreateTopicFragment.class, null);

                //                gotoPager(PublishStateFragment.class, null);//发布动态

                //                gotoPager(StateDetailFragment.class, null);//动态详情
                break;
            default:
                break;
        }
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_hall://大厅
                    position = 0;
                    break;
                case R.id.rb_follow://话题
                    position = 1;
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
                ft.add(R.id.fl_content_community, to).commit();
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
        BaseFragment fragment = mBaseFragment.get(position);
        return fragment;
    }
}
