package com.wiserz.pbibi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.CarInfoBeanForCarDetail;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/17 9:30.
 * QQ : 971060378
 * Used as : 汽车详情的页面
 */
public class CarDetailFragment extends BaseFragment {

    private String car_id;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_car_detail;
    }

    @Override
    protected void initView(View view) {
        Bundle bundle = getArguments();
        car_id = bundle.getString(Constant.CAR_ID);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("车辆详情");
        view.findViewById(R.id.btn_watch_vr).setOnClickListener(this);
        view.findViewById(R.id.btn_line_contact).setOnClickListener(this);
        view.findViewById(R.id.btn_phone_contact).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_watch_vr:
                ToastUtils.showShort("VR");
                break;
            case R.id.btn_line_contact:
                ToastUtils.showShort("btn_line_contact");
                break;
            case R.id.btn_phone_contact:
                ToastUtils.showShort("btn_phone_contact");
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (EmptyUtils.isNotEmpty(car_id)) {
            getDataFromNet();
        }
    }

    private void getDataFromNet() {
        OkHttpUtils.post()
                .url(Constant.getCarDetailUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.CAR_ID, car_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                handlerCarInfoData(jsonObjectData.optJSONObject("car_info"));//处理此车辆的数据

                            } else {
                                String code = jsonObject.optString("code");
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void handlerCarInfoData(JSONObject car_info) {
        Gson gson = new Gson();
        CarInfoBeanForCarDetail carInfoBeanForCarDetail = gson.fromJson(car_info.toString(), CarInfoBeanForCarDetail.class);

        if (EmptyUtils.isNotEmpty(carInfoBeanForCarDetail) && getView() != null) {
            ((TextView) getView().findViewById(R.id.tv_title)).setText(carInfoBeanForCarDetail.getBrand_info().getBrand_name() + " " + carInfoBeanForCarDetail.getSeries_info().getSeries_name());

            ConvenientBanner convenientBanner = (ConvenientBanner) getView().findViewById(R.id.convenientBanner);
            ArrayList<CarInfoBeanForCarDetail.FilesBean> filesBeanArrayList = (ArrayList<CarInfoBeanForCarDetail.FilesBean>) carInfoBeanForCarDetail.getFiles();
            //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
            //            convenientBanner.startTurning(5000);
            convenientBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
                @Override
                public LocalImageHolderView createHolder() {
                    return new LocalImageHolderView();
                }
            }, filesBeanArrayList)
                    //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                    //                    .setPageIndicator(new int[]{R.drawable.point_normal1, R.drawable.point_checked})
                    //设置指示器的方向
                    //                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                    .setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            ToastUtils.showShort(position + "");
                        }
                    });
            //设置翻页的效果，不需要翻页效果可用不设
            //.setPageTransformer(Transformer.DefaultTransformer);    集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
            // convenientBanner.setManualPageable(false);//设置不能手动影响

            if (EmptyUtils.isNotEmpty(carInfoBeanForCarDetail.getCar_name())) {
                ((TextView) getView().findViewById(R.id.tv_car_name)).setText(carInfoBeanForCarDetail.getCar_name());
            }
            if (EmptyUtils.isNotEmpty(carInfoBeanForCarDetail.getPrice())) {
                ((TextView) getView().findViewById(R.id.tv_car_price)).setText(carInfoBeanForCarDetail.getPrice() + "万");
            }
            ((TextView) getView().findViewById(R.id.tv_car_location)).setText(carInfoBeanForCarDetail.getCity_info().getCity_name());
            ((TextView) getView().findViewById(R.id.tv_car_time)).setText(carInfoBeanForCarDetail.getCar_time());

            getView().findViewById(R.id.tv_check_all_car_configure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoPager(ConcreteParameterFragment.class, null);
                }
            });

            if (EmptyUtils.isNotEmpty(carInfoBeanForCarDetail.getUser_info()) && EmptyUtils.isNotEmpty(carInfoBeanForCarDetail.getUser_info().getProfile()) && EmptyUtils.isNotEmpty(carInfoBeanForCarDetail.getUser_info().getProfile().getAvatar())) {
                Glide.with(mContext)
                        .load(carInfoBeanForCarDetail.getUser_info().getProfile().getAvatar())
                        .placeholder(R.drawable.user_photo)
                        .into((ImageView) getView().findViewById(R.id.image_circle));
            }
            if (EmptyUtils.isNotEmpty(carInfoBeanForCarDetail.getUser_info()) && EmptyUtils.isNotEmpty(carInfoBeanForCarDetail.getUser_info().getProfile())) {
                ((TextView) getView().findViewById(R.id.tv_car_owner_nickname)).setText(carInfoBeanForCarDetail.getUser_info().getProfile().getNickname());
            }
            getView().findViewById(R.id.tv_contact_car_owner).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showShort("联系车主");
                }
            });
            if (EmptyUtils.isNotEmpty(carInfoBeanForCarDetail.getUser_info()) && EmptyUtils.isNotEmpty(carInfoBeanForCarDetail.getUser_info().getProfile())) {
                ((TextView) getView().findViewById(R.id.tv_car_owner_detail)).setText(carInfoBeanForCarDetail.getUser_info().getProfile().getSignature());
            }
        }
    }

    private class LocalImageHolderView implements Holder<CarInfoBeanForCarDetail.FilesBean> {

        private ImageView iv_image;
        private RelativeLayout rl_topic;
        private TextView tv_title;

        @Override
        public View createView(Context context) {
            View view = View.inflate(context, R.layout.item_banner, null);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            rl_topic = (RelativeLayout) view.findViewById(R.id.rl_topic);
            rl_topic.setVisibility(View.GONE);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_title.setVisibility(View.GONE);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, CarInfoBeanForCarDetail.FilesBean data) {
            if (data != null) {
                Glide.with(context)
                        .load(data.getFile_url())
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .error(R.drawable.default_bg_ratio_1)
                        .into(iv_image);
            }
        }
    }
}
