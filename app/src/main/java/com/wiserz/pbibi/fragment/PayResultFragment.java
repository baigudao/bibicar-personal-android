package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiserz.pbibi.R;

/**
 * Created by huangzhifeng on 2017/11/27.
 */

public class PayResultFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pay_result;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        int payResult=getArguments().getInt("pay_result",0);
        if(payResult==0){
            ((TextView) view.findViewById(R.id.tv_title)).setText("支付失败");
            ((ImageView) view.findViewById(R.id.ivResult)).setImageResource(R.drawable.pay_failed);
            ((TextView) view.findViewById(R.id.tvResult)).setText("支付失败");
            ((TextView) view.findViewById(R.id.tvResultDetail)).setText("支付失败，请稍后再试");
        }else{
            ((TextView) view.findViewById(R.id.tv_title)).setText("支付成功");
        }
        view.findViewById(R.id.tvSeeWallet).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                goBack();
                break;
            case R.id.tvSeeWallet:
                goBack();
                goBack();
                gotoPager(MyWalletFragment.class, null);
                break;
        }
    }
}
