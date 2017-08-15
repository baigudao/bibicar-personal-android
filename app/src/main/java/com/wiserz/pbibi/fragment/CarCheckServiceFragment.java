package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.TextView;

import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/8/10 18:04.
 * QQ : 971060378
 * Used as : 车辆检测服务
 */
public class CarCheckServiceFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_car_check_service;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("车辆检测服务");
        view.findViewById(R.id.btn_register).setVisibility(View.GONE);

        view.findViewById(R.id.btn_check_peccancy).setOnClickListener(this);
        view.findViewById(R.id.btn_check_guarantee).setOnClickListener(this);
        view.findViewById(R.id.btn_check_insurance).setOnClickListener(this);
        view.findViewById(R.id.btn_check_sum).setOnClickListener(this);
        view.findViewById(R.id.btn_check_history).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_check_peccancy:
                gotoPager(CheckPeccancyFragment.class, null);//查违章
                break;
            case R.id.btn_check_insurance:
                gotoPager(CheckInsuranceFragment.class, null);//查保险
                break;
            case R.id.btn_check_guarantee:
                gotoPager(CheckGuaranteeFragment.class, null);//查维保
                break;
            case R.id.btn_check_sum:
                gotoPager(CheckSumFragment.class, null);//查出险
                break;
            case R.id.btn_check_history:
                gotoPager(CheckHistoryFragment.class, null);//查询历史
                break;
            default:
                break;
        }
    }
}
