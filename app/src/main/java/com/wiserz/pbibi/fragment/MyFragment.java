package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.TextView;

import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/8/9 18:02.
 * QQ : 971060378
 * Used as : 个人中心页面
 */
public class MyFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.tv_title)).setText("我的");
        view.findViewById(R.id.rl_user).setOnClickListener(this);
        view.findViewById(R.id.rl_setting).setOnClickListener(this);
        view.findViewById(R.id.rl_my_wallet).setOnClickListener(this);
        view.findViewById(R.id.rl_total_property).setOnClickListener(this);
        view.findViewById(R.id.rl_my_car_repertory).setOnClickListener(this);
        view.findViewById(R.id.rl_like).setOnClickListener(this);
        view.findViewById(R.id.rl_my_order).setOnClickListener(this);
        view.findViewById(R.id.rl_car_service).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_user:
                gotoPager(MyHomePageFragment.class, null);
                break;
            case R.id.rl_setting:
                gotoPager(SettingFragment.class, null);
                break;
            case R.id.rl_my_wallet:
                gotoPager(MyWalletFragment.class, null);
                break;
            case R.id.rl_total_property:
                gotoPager(TotalPropertyFragment.class, null);
                break;
            case R.id.rl_my_car_repertory:
                gotoPager(MyCarRepertory.class, null);
                break;
            case R.id.rl_like:
                gotoPager(MyLikeFragment.class, null);
                break;
            case R.id.rl_my_order:
                gotoPager(MyOrderFragment.class, null);
                break;
            case R.id.rl_car_service:
                gotoPager(CarServiceFragment.class, null);
                break;
            default:
                break;
        }
    }
}
