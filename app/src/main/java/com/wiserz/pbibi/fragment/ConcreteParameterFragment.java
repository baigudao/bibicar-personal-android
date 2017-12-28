package com.wiserz.pbibi.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.CarConfiguration;
import com.wiserz.pbibi.bean.CarInfoBean;
import com.wiserz.pbibi.util.DataManager;

import java.util.ArrayList;

/**
 * Created by jackie on 2017/8/14 22:55.
 * QQ : 971060378
 * Used as : 具体参数的页面
 */

public class ConcreteParameterFragment extends BaseFragment {

    private CarInfoBean carInfoBean;

    private TextView tv_price;
    private TextView tv_guarantee;
    private TextView tv_output;
    private TextView tv_auto;
    private TextView tv_oil;
    private TextView tv_100_accelerate_time;
    private TextView tv_car_top_speed;
    private TextView tv_car_take_num;
    private TextView tv_car_drive_style;
    private TextView tv_car_engine_type;
    private TextView tv_car_engine_position;
    private TextView tv_car_enter_gas_type;
    private TextView tv_car_most_engine;
    private TextView tv_car_most_distance;
    private TextView tv_car_environment_standard;
    private TextView tv_car_vol;
    private TextView tv_car_ground_distance;
    private TextView tv_car_distance;
    private TextView tv_car_box_vol;
    private TextView tv_car_gas_style;
    private TextView tv_car_oil_style;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_concrete_parameter;
    }

    @Override
    protected void initView(View view) {
        carInfoBean = (CarInfoBean) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("具体参数");

        tv_price = (TextView) view.findViewById(R.id.tv_price);
        tv_guarantee = (TextView) view.findViewById(R.id.tv_guarantee);
        tv_output = (TextView) view.findViewById(R.id.tv_output);
        tv_auto = (TextView) view.findViewById(R.id.tv_auto);
        tv_oil = (TextView) view.findViewById(R.id.tv_oil);
        tv_100_accelerate_time = (TextView) view.findViewById(R.id.tv_100_accelerate_time);
        tv_car_top_speed = (TextView) view.findViewById(R.id.tv_car_top_speed);
        tv_car_take_num = (TextView) view.findViewById(R.id.tv_car_take_num);
        tv_car_drive_style = (TextView) view.findViewById(R.id.tv_car_drive_style);
        tv_car_engine_type = (TextView) view.findViewById(R.id.tv_car_engine_type);
        tv_car_engine_position = (TextView) view.findViewById(R.id.tv_car_engine_position);
        tv_car_enter_gas_type = (TextView) view.findViewById(R.id.tv_car_enter_gas_type);
        tv_car_most_engine = (TextView) view.findViewById(R.id.tv_car_most_engine);
        tv_car_most_distance = (TextView) view.findViewById(R.id.tv_car_most_distance);
        tv_car_environment_standard = (TextView) view.findViewById(R.id.tv_car_environment_standard);
        tv_car_vol = (TextView) view.findViewById(R.id.tv_car_vol);
        tv_car_ground_distance = (TextView) view.findViewById(R.id.tv_car_ground_distance);
        tv_car_distance = (TextView) view.findViewById(R.id.tv_car_distance);
        tv_car_box_vol = (TextView) view.findViewById(R.id.tv_car_box_vol);
        tv_car_gas_style = (TextView) view.findViewById(R.id.tv_car_gas_style);
        tv_car_oil_style = (TextView) view.findViewById(R.id.tv_car_oil_style);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (EmptyUtils.isNotEmpty(carInfoBean)) {
            showData();
        }
    }

    private void showData() {
        CarInfoBean.ModelDetailBean modelDetailBean = carInfoBean.getModel_detail();
        ArrayList<CarConfiguration.Configuration> carExtraInfo = carInfoBean.getCar_extra_info();
        LinearLayout llExtra = (LinearLayout) getView().findViewById(R.id.llExtra);
        llExtra.removeAllViews();
        if (carExtraInfo != null && !carExtraInfo.isEmpty()) {
            getView().findViewById(R.id.rlExtraName).setVisibility(View.VISIBLE);
            llExtra.setVisibility(View.VISIBLE);
            int size = carExtraInfo.size();
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            RelativeLayout view;
            for (int i = 0; i < size; ++i) {
                view = (RelativeLayout) inflater.inflate(R.layout.item_extra_info, null);
                view.setBackgroundColor(getResources().getColor(i % 2 == 0 ? R.color.background_color : R.color.pure_white_color));
                ((TextView) view.getChildAt(0)).setText(carExtraInfo.get(i).getName());
                llExtra.addView(view);
            }
            if (size % 2 == 0) {
                getView().findViewById(R.id.rlExtraBasicName).setBackgroundColor(getResources().getColor(R.color.background_color));
                LinearLayout llBasic = (LinearLayout) getView().findViewById(R.id.llBasic);
                size = llBasic.getChildCount();
                for (int i = 0; i < size; ++i) {
                    llBasic.getChildAt(i).setBackgroundColor(getResources().getColor(i % 2 == 1 ? R.color.background_color : R.color.pure_white_color));
                }
            }
        } else {
            getView().findViewById(R.id.rlExtraName).setVisibility(View.GONE);
            llExtra.setVisibility(View.GONE);
        }

        if (EmptyUtils.isNotEmpty(modelDetailBean)) {
            tv_price.setText(EmptyUtils.isEmpty(modelDetailBean.getCarReferPrice()) ? "****" : modelDetailBean.getCarReferPrice());
            tv_guarantee.setText(EmptyUtils.isEmpty(modelDetailBean.getCar_RepairPolicy()) ? "***" : modelDetailBean.getCar_RepairPolicy());
            tv_output.setText(EmptyUtils.isEmpty(modelDetailBean.getEngine_ExhaustForFloat()) ? "****" : modelDetailBean.getEngine_ExhaustForFloat());
            tv_oil.setText(EmptyUtils.isEmpty(modelDetailBean.getPerf_ZongHeYouHao()) ? "****" : modelDetailBean.getPerf_ZongHeYouHao());
            tv_100_accelerate_time.setText(EmptyUtils.isEmpty(modelDetailBean.getPerf_AccelerateTime()) ? "****" : modelDetailBean.getPerf_AccelerateTime());
            tv_car_top_speed.setText(EmptyUtils.isEmpty(modelDetailBean.getPerf_MaxSpeed()) ? "****" : modelDetailBean.getPerf_MaxSpeed());
            tv_car_take_num.setText(EmptyUtils.isEmpty(modelDetailBean.getPerf_SeatNum()) ? "****" : modelDetailBean.getPerf_SeatNum());
            tv_car_drive_style.setText(EmptyUtils.isEmpty(modelDetailBean.getPerf_DriveType()) ? "****" : modelDetailBean.getPerf_DriveType());
            tv_car_engine_type.setText(EmptyUtils.isEmpty(modelDetailBean.getEngine_Type()) ? "****" : modelDetailBean.getEngine_Type());
            tv_car_engine_position.setText(EmptyUtils.isEmpty(modelDetailBean.getEngine_Location()) ? "****" : modelDetailBean.getEngine_Location());
            tv_car_enter_gas_type.setText(EmptyUtils.isEmpty(modelDetailBean.getEngine_InhaleType()) ? "****" : modelDetailBean.getEngine_InhaleType());
            tv_car_most_engine.setText(EmptyUtils.isEmpty(modelDetailBean.getEngine_horsepower()) ? "****" : modelDetailBean.getEngine_horsepower());
            tv_car_most_distance.setText(EmptyUtils.isEmpty(modelDetailBean.getEngine_MaxNJ()) ? "****" : modelDetailBean.getEngine_MaxNJ());
            tv_car_environment_standard.setText(EmptyUtils.isEmpty(modelDetailBean.getEngine_EnvirStandard()) ? "****" : modelDetailBean.getEngine_EnvirStandard());

            tv_car_vol.setText((EmptyUtils.isEmpty(modelDetailBean.getOutSet_Length()) ? "****" : modelDetailBean.getOutSet_Length()) + "x" +
                    (EmptyUtils.isEmpty(modelDetailBean.getOutSet_Width()) ? "****" : modelDetailBean.getOutSet_Width()) + "x" +
                    (EmptyUtils.isEmpty(modelDetailBean.getOutSet_Width()) ? "****" : modelDetailBean.getOutSet_Width()));//长x宽x高

            tv_car_ground_distance.setText(EmptyUtils.isEmpty(modelDetailBean.getOutSet_MinGapFromEarth()) ? "****" : modelDetailBean.getOutSet_MinGapFromEarth());
            tv_car_distance.setText(EmptyUtils.isEmpty(modelDetailBean.getOutSet_WheelBase()) ? "****" : modelDetailBean.getOutSet_WheelBase());
            tv_car_box_vol.setText(EmptyUtils.isEmpty(modelDetailBean.getOil_FuelCapacity()) ? "****" : modelDetailBean.getOil_FuelCapacity());
            tv_car_gas_style.setText(EmptyUtils.isEmpty(modelDetailBean.getOil_FuelType()) ? "****" : modelDetailBean.getOil_FuelType());
            tv_car_oil_style.setText(EmptyUtils.isEmpty(modelDetailBean.getOil_SupplyType()) ? "****" : modelDetailBean.getOil_SupplyType());
        }
        tv_auto.setText(EmptyUtils.isEmpty(carInfoBean.getGearbox()) ? "****" : carInfoBean.getGearbox());
    }
}
