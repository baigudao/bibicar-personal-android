package com.wiserz.pbibi.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blankj.utilcode.util.KeyboardUtils;
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
    private Button btn_search;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        et_search = (EditText) view.findViewById(R.id.et_search);
        et_search.addTextChangedListener(new MyTextWatcher());
        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    KeyboardUtils.hideSoftInput(getActivity());
                    //                    search();
                }
                return false;
            }
        });
        btn_search = (Button) view.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);

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
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        if (from != to) {
            LogUtils.e("switchFragment");
            fromFragment = to;
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
        } else {
            LogUtils.e("to与from相等");
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
                KeyboardUtils.hideSoftInput(getActivity());
                checkRadioButton();
                break;
            default:
                break;
        }
    }

    private void checkRadioButton() {
        if (getView() != null) {
            switch (position) {
                case 0:
                    setCheck((RadioButton) getView().findViewById(R.id.rb_car));
                    break;
                case 1:
                    mRg_main.check(R.id.rb_article);
                    setCheck((RadioButton) getView().findViewById(R.id.rb_article));
                    break;
                case 2:
                    mRg_main.check(R.id.rb_user);
                    setCheck((RadioButton) getView().findViewById(R.id.rb_user));
                    break;
                case 3:
                    mRg_main.check(R.id.rb_topic);
                    setCheck((RadioButton) getView().findViewById(R.id.rb_topic));
                    break;
                default:
                    break;
            }
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

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean search = et_search.getText().length() > 0;
            if (search) {
                btn_search.setEnabled(true);
            } else {
                btn_search.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
