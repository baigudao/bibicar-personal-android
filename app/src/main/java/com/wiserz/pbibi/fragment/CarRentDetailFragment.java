package com.wiserz.pbibi.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
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
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.CarRentDetailInfoBean;
import com.wiserz.pbibi.bean.CarRentRecommendCarBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.SharePlatformPopupWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import okhttp3.Call;

import static com.wiserz.pbibi.R.id.iv_image;


/**
 * Created by jackie on 2017/8/15 15:43.
 * QQ : 971060378
 * Used as : 汽车租赁详情的页面
 */
public class CarRentDetailFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private ConvenientBanner convenientBanner;
    private RecyclerView recyclerView;
    private TextView tv_title;

    private String car_id;
    private String is_auth;

    private static final int CAR_RENT_RECOMMEND_DATA_TYPE = 16;
    private String share_img;
    private String share_title;
    private String share_txt;
    private String share_url;
    private CarRentDetailInfoBean carRentDetailInfoBean;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_car_rent_detail;
    }

    @Override
    protected void initView(View view) {
        car_id = getArguments().getString(Constant.CAR_ID);

        view.findViewById(R.id.iv_back).setOnClickListener(this);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("租车详情");

        convenientBanner = (ConvenientBanner) view.findViewById(R.id.convenientBanner);

        ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
        iv_image.setVisibility(View.VISIBLE);
        iv_image.setImageResource(R.drawable.share_selector);
        iv_image.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);//推荐租赁

        view.findViewById(R.id.btn_custom_service).setOnClickListener(this);
        view.findViewById(R.id.btn_rent_car).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case iv_image://分享
                showSharePlatformPopWindow();
                break;
            case R.id.btn_custom_service:
                contactCustomService();
                break;
            case R.id.btn_rent_car:
                //是否身份验证 1:是 2:否
                if (is_auth.equals("2")) {
                    gotoPager(AuthenticationFragment.class, null);
                } else {
                    if (EmptyUtils.isNotEmpty(carRentDetailInfoBean)) {
                        DataManager.getInstance().setData1(carRentDetailInfoBean);
                        gotoPager(CreateCarRentOrderFragment.class, null);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void contactCustomService() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("联系客服")
                .setMessage("\n电话号码：075522233323")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Intent.ACTION_CALL= "android.intent.action.CALL";
                        //需要权限:<uses-permission android:name="android.permission.CALL_PHONE"/>
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:075522233323"));
                        startActivity(intent);

                        //也可以直接调用拨打电话的界面
                        //                        startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:075522233323")));
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
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
                .url(Constant.getRentCarDetailUrl())
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
                                is_auth = jsonObjectData.optString(Constant.IS_AUTH);
                                share_img = jsonObjectData.optString("share_img");
                                share_title = jsonObjectData.optString("share_title");
                                share_txt = jsonObjectData.optString("share_txt");
                                share_url = jsonObjectData.optString("share_url");
                                handlerDataForCarRentDetail(jsonObjectData);
                                handlerDataForRecommendCar(jsonObjectData);
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

    private void handlerDataForRecommendCar(JSONObject jsonObjectData) {
        JSONArray jsonArray = jsonObjectData.optJSONArray("recommon_cars");
        Gson gson = new Gson();
        ArrayList<CarRentRecommendCarBean> carRentRecommendCarBeanArrayList = new ArrayList<>();
        if (EmptyUtils.isNotEmpty(jsonArray)) {
            carRentRecommendCarBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CarRentRecommendCarBean>>() {
            }.getType());

            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, carRentRecommendCarBeanArrayList, CAR_RENT_RECOMMEND_DATA_TYPE);
            recyclerView.setAdapter(baseRecyclerViewAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false));
            baseRecyclerViewAdapter.setOnItemClickListener(this);
        }
    }

    private void handlerDataForCarRentDetail(JSONObject jsonObjectData) {
        JSONObject jsonObject = jsonObjectData.optJSONObject("car_info");
        Gson gson = new Gson();
        carRentDetailInfoBean = gson.fromJson(jsonObject.toString(), CarRentDetailInfoBean.class);

        if (EmptyUtils.isNotEmpty(carRentDetailInfoBean) && getView() != null) {
            tv_title.setText(carRentDetailInfoBean.getBrand_info().getBrand_name());

            CarRentDetailInfoBean.FilesBean filesBean = carRentDetailInfoBean.getFiles();
            if (EmptyUtils.isNotEmpty(filesBean)) {
                final ArrayList<String> strings = new ArrayList<>();
                ArrayList<CarRentDetailInfoBean.FilesBean.Type1Bean> type1BeanArrayList = (ArrayList<CarRentDetailInfoBean.FilesBean.Type1Bean>) filesBean.getType1();
                ArrayList<CarRentDetailInfoBean.FilesBean.Type2Bean> type2BeanArrayList = (ArrayList<CarRentDetailInfoBean.FilesBean.Type2Bean>) filesBean.getType2();
                ArrayList<CarRentDetailInfoBean.FilesBean.Type3Bean> type3BeanArrayList = (ArrayList<CarRentDetailInfoBean.FilesBean.Type3Bean>) filesBean.getType3();
                ArrayList<CarRentDetailInfoBean.FilesBean.Type4Bean> type4BeanArrayList = (ArrayList<CarRentDetailInfoBean.FilesBean.Type4Bean>) filesBean.getType4();
                for (int i = 0; i < type1BeanArrayList.size(); i++) {
                    strings.add(type1BeanArrayList.get(i).getFile_url());
                }
                for (int i = 0; i < type2BeanArrayList.size(); i++) {
                    strings.add(type2BeanArrayList.get(i).getFile_url());
                }
                for (int i = 0; i < type3BeanArrayList.size(); i++) {
                    strings.add(type3BeanArrayList.get(i).getFile_url());
                }
                for (int i = 0; i < type4BeanArrayList.size(); i++) {
                    strings.add(type4BeanArrayList.get(i).getFile_url());
                }

                //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
                //                convenientBanner.startTurning(5000);
                convenientBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, strings)
                        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                        //                        .setPageIndicator(new int[]{R.drawable.point_normal1, R.drawable.point_checked})
                        //设置指示器的方向
                        //                        .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                DataManager.getInstance().setData1(strings);
                                gotoPager(ShowAllImageFragment.class, null);
                            }
                        });
                //设置翻页的效果，不需要翻页效果可用不设
                //.setPageTransformer(Transformer.DefaultTransformer);    集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
                // convenientBanner.setManualPageable(false);//设置不能手动影响
            }

            ((TextView) getView().findViewById(R.id.tv_car_name)).setText(carRentDetailInfoBean.getCar_name());
            ((TextView) getView().findViewById(R.id.tv_car_price)).setText("¥" + String.valueOf(carRentDetailInfoBean.getRental_info().getOne()) + "/日");
            ((TextView) getView().findViewById(R.id.tv_car_location)).setText(carRentDetailInfoBean.getCity_info().getCity_name());
            ((TextView) getView().findViewById(R.id.tv_car_time)).setText(carRentDetailInfoBean.getCar_time());
            ((TextView) getView().findViewById(R.id.tv_one)).setText("¥" + String.valueOf(carRentDetailInfoBean.getRental_info().getOne()) + "/日");
            ((TextView) getView().findViewById(R.id.tv_two)).setText("¥" + String.valueOf(carRentDetailInfoBean.getRental_info().getTwo()) + "/日");
            ((TextView) getView().findViewById(R.id.tv_three)).setText("¥" + String.valueOf(carRentDetailInfoBean.getRental_info().getThree()) + "/日");
            ((TextView) getView().findViewById(R.id.tv_four)).setText("¥" + String.valueOf(carRentDetailInfoBean.getRental_info().getFour()) + "/日");

            ((TextView) getView().findViewById(R.id.tv_volume)).setText(carRentDetailInfoBean.getModel_detail().getEngine_ExhaustForFloat());
            getView().findViewById(R.id.tv_check_all_parameter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoPager(ConcreteParameterFragment.class, null);
                }
            });

            Glide.with(mContext)
                    .load(carRentDetailInfoBean.getBrand_info().getBrand_url())
                    .placeholder(R.drawable.user_photo)
                    .error(R.drawable.user_photo)
                    .into((ImageView) getView().findViewById(R.id.iv_image_brand));
        }
    }

    private void showSharePlatformPopWindow() {
        SharePlatformPopupWindow sharePlatformPopWindow = new SharePlatformPopupWindow(getActivity(), new SharePlatformPopupWindow.SharePlatformListener() {
            @Override
            public void onSinaWeiboClicked() {
                showShare(mContext, "SinaWeibo", true);
            }

            @Override
            public void onWeChatClicked() {
                showShare(mContext, "Wechat", true);
            }

            @Override
            public void onWechatMomentsClicked() {
                showShare(mContext, "WechatMoments", true);
            }

            @Override
            public void onCancelBtnClicked() {

            }
        });
        sharePlatformPopWindow.initView();
        sharePlatformPopWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 演示调用ShareSDK执行分享
     *
     * @param context
     * @param platformToShare 指定直接分享平台名称（一旦设置了平台名称，则九宫格将不会显示）
     * @param showContentEdit 是否显示编辑页
     */
    private void showShare(Context context, String platformToShare, boolean showContentEdit) {

        OnekeyShare oks = new OnekeyShare();
        oks.setSilent(!showContentEdit);
        if (platformToShare != null) {
            oks.setPlatform(platformToShare);
        }
        //ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC  第二个是SKYBLUE
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        // 令编辑页面显示为Dialog模式
        //        oks.setDialogMode();
        // 在自动授权时可以禁用SSO方式
        oks.disableSSOWhenAuthorize();

        oks.setTitle(share_title);
        if (platformToShare.equalsIgnoreCase("SinaWeibo")) {
            oks.setText(share_txt + "\n" + share_url);
        } else {
            oks.setText(share_img);
            oks.setImageUrl(share_img);
            oks.setUrl(share_url);
        }

        // 启动分享
        oks.show(context);
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("CarRentRecommendCarBean")) {
            CarRentRecommendCarBean rentRecommendCarBean = (CarRentRecommendCarBean) data;
            String car_id = rentRecommendCarBean.getCar_id();
            if (EmptyUtils.isNotEmpty(car_id)) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.CAR_ID, car_id);
                gotoPager(CarRentDetailFragment.class, bundle);//汽车租赁详情
            }
        }
    }

    private class LocalImageHolderView implements Holder<String> {

        private ImageView iv_image;
        //        private RelativeLayout rl_topic;
        //        private TextView tv_title;

        @Override
        public View createView(Context context) {
            View view = View.inflate(context, R.layout.item_banner, null);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            //            rl_topic = (RelativeLayout) view.findViewById(R.id.rl_topic);
            //            rl_topic.setVisibility(View.GONE);
            //            tv_title = (TextView) view.findViewById(R.id.tv_title);
            //            tv_title.setVisibility(View.GONE);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            if (data != null) {
                Glide.with(context)
                        .load(data)
                        .error(R.drawable.default_bg_ratio_1)
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .error(R.drawable.default_bg_ratio_1)
                        .into(iv_image);
            }
        }
    }
}
