package com.wiserz.pbibi.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.CarRentInfoBean;
import com.wiserz.pbibi.util.DataManager;

/**
 * Created by jackie on 2017/8/15 15:43.
 * QQ : 971060378
 * Used as : 汽车租赁详情的页面
 */
public class CarRentDetailFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private CarRentInfoBean carRentInfoBean;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_car_rent_detail;
    }

    @Override
    protected void initView(View view) {
        carRentInfoBean = (CarRentInfoBean) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        if (EmptyUtils.isNotEmpty(carRentInfoBean)) {
            ((TextView) view.findViewById(R.id.tv_title)).setText(carRentInfoBean.getBrand_info().getBrand_name());
        }
        ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
        iv_image.setVisibility(View.VISIBLE);
        iv_image.setImageResource(R.drawable.share_selector);
        iv_image.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);//用这个写租车详情，也可以不用这个写
        view.findViewById(R.id.btn_custom_service).setOnClickListener(this);
        view.findViewById(R.id.btn_rent_car).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.iv_image:
                ToastUtils.showShort("分享");
                break;
            case R.id.btn_custom_service:
                ToastUtils.showShort("联系客服");
                break;
            case R.id.btn_rent_car:
                DataManager.getInstance().setData1(carRentInfoBean);
                gotoPager(CreateCarRentOrderFragment.class, null);
                break;
            default:
                break;
        }
    }
}
