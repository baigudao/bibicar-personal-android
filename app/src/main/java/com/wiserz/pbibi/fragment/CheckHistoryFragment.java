package com.wiserz.pbibi.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wiserz.pbibi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackie on 2017/8/15 10:50.
 * QQ : 971060378
 * Used as : 查询历史的页面
 */
public class CheckHistoryFragment extends BaseFragment {

    private List<BaseFragment> mBaseFragment;
    private int position;
    private Fragment fromFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_check_history;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("查询历史");

        RadioGroup mRg_main = (RadioGroup) view.findViewById(R.id.rg_main);

        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new PeccancyHistoryFragment());//违章
        mBaseFragment.add(new SumHistoryFragment());//出险
        mBaseFragment.add(new GuaranteeHistoryFragment());//维保

        mRg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //设置默认选中常用框架
        mRg_main.check(R.id.rb_peccancy);
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
                case R.id.rb_peccancy://查违章
                    position = 0;
                    break;
                case R.id.rb_sum://查出险
                    position = 1;
                    break;
                case R.id.rb_guarantee://查维保
                    position = 2;
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
                ft.add(R.id.fl_content_check_history, to).commit();
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

    public void setHistoryCheck(RadioButton radioButton) {
        if (getView() != null) {
            ((RadioButton) getView().findViewById(R.id.rb_peccancy)).setChecked(false);
            ((RadioButton) getView().findViewById(R.id.rb_sum)).setChecked(false);
            ((RadioButton) getView().findViewById(R.id.rb_guarantee)).setChecked(false);
            radioButton.setChecked(true);
        }
    }
}
