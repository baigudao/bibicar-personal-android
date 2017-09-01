package com.wiserz.pbibi.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SearchView;

import com.blankj.utilcode.util.ToastUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.DataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackie on 2017/8/15 15:14.
 * QQ : 971060378
 * Used as : 搜索的页面
 */
public class SearchFragment extends BaseFragment {

    private List<BaseFragment> mBaseFragment;
    private int position;
    private Fragment fromFragment;

    private String keyword;

    private SearchView search_view;
    private RadioGroup mRg_main;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        search_view = (SearchView) view.findViewById(R.id.search_view);
        search_view.setSubmitButtonEnabled(true);
        search_view.setIconifiedByDefault(false);
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                ToastUtils.showShort("开始搜索" + query);
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mRg_main = (RadioGroup) view.findViewById(R.id.rg_main);
        initFragment();
        mRg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //设置默认选中常用框架
        mRg_main.check(R.id.rb_car);
    }

    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new CarSearchFragment());//车辆搜索
        mBaseFragment.add(new ArticleSearchFragment());//文章搜索
        mBaseFragment.add(new UserSearchFragment());//用户搜索
        mBaseFragment.add(new TopicSearchFragment());//话题搜索
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_car://车辆
                    position = 0;
                    DataManager.getInstance().setData1(keyword);
                    break;
                case R.id.rb_article://文章
                    position = 1;
                    DataManager.getInstance().setData1(keyword);
                    break;
                case R.id.rb_user://用户
                    position = 2;
                    DataManager.getInstance().setData1(keyword);
                    break;
                case R.id.rb_topic://话题
                    position = 3;
                    DataManager.getInstance().setData1(keyword);
                    break;
                default:
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
            //判断有没有被添加
            if (!to.isAdded()) {
                //to没有被添加
                //from隐藏
                if (from != null) {
                    ft.remove(from);
                }
                //添加to
                ft.add(R.id.fl_search_content, to).commit();
            } else {
                //to已经被添加
                // from隐藏
                if (from != null) {
                    ft.remove(from);
                }
                //显示to
                ft.show(to).commit();
            }
        }
    }

    private BaseFragment getFragment() {
        return mBaseFragment.get(position);
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

    //    private void setCheck(RadioButton radioButton) {
    //        if (getView() != null) {
    //            ((RadioButton) getView().findViewById(R.id.rb_car)).setChecked(false);
    //            ((RadioButton) getView().findViewById(R.id.rb_article)).setChecked(false);
    //            ((RadioButton) getView().findViewById(R.id.rb_user)).setChecked(false);
    //            ((RadioButton) getView().findViewById(R.id.rb_topic)).setChecked(false);
    //        }
    //        radioButton.setChecked(true);
    //    }
}
