package com.wiserz.pbibi.fragment;

import android.view.View;

import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/8/9 18:00.
 * QQ : 971060378
 * Used as : 汽车中心页面
 */
public class CarCenterFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_car_center;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ivRight).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivRight:
                //                gotoPager(LoanPlanFragment.class,null);//贷款方案

                gotoPager(ConcreteParameterFragment.class, null);//具体参数
                break;
            default:
                break;
        }
    }
}
