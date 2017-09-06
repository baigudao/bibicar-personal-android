package com.wiserz.pbibi.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blankj.utilcode.util.LogUtils;
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

    private EditText et_search;
    private RadioGroup mRg_main;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        et_search = (EditText) view.findViewById(R.id.et_search);
        view.findViewById(R.id.btn_search).setOnClickListener(this);

        initFragment();
        mRg_main = (RadioGroup) view.findViewById(R.id.rg_main);
        mRg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //设置默认选中常用框架
        mRg_main.check(R.id.rb_car);
    }

    private String getKeyword() {
        return et_search.getText().toString().trim();
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
                    DataManager.getInstance().setData1(getKeyword());
                    break;
                case R.id.rb_article://文章
                    position = 1;
                    DataManager.getInstance().setData1(getKeyword());
                    break;
                case R.id.rb_user://用户
                    position = 2;
                    DataManager.getInstance().setData1(getKeyword());
                    break;
                case R.id.rb_topic://话题
                    position = 3;
                    DataManager.getInstance().setData1(getKeyword());
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
            case R.id.btn_search:
                ToastUtils.showShort("搜索");
                checkRadioButton();
                break;
            default:
                break;
        }
    }

    private void checkRadioButton() {
        switch (position) {
            case 0:
                mRg_main.check(R.id.rb_car);
                LogUtils.e("hehe0");
                break;
            case 1:
                mRg_main.check(R.id.rb_article);
                LogUtils.e("hehe1");
                break;
            case 2:
                mRg_main.check(R.id.rb_user);
                LogUtils.e("hehe2");
                break;
            case 3:
                mRg_main.check(R.id.rb_topic);
                LogUtils.e("hehe3");
                break;
            default:
                break;
        }
    }

    private void setCheck(RadioButton radioButton) {
        if (getView() != null) {
            ((RadioButton) getView().findViewById(R.id.rb_car)).setChecked(false);
            ((RadioButton) getView().findViewById(R.id.rb_article)).setChecked(false);
            ((RadioButton) getView().findViewById(R.id.rb_user)).setChecked(false);
            ((RadioButton) getView().findViewById(R.id.rb_topic)).setChecked(false);
        }
        radioButton.setChecked(true);
    }
}
