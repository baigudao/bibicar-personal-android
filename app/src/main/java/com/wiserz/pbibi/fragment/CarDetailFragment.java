package com.wiserz.pbibi.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.VRWatchCarActivity;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.CarInfoBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.SharePlatformPopupWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Call;

/**
 * Created by jackie on 2017/8/17 9:30.
 * QQ : 971060378
 * Used as : 汽车详情的页面
 */
public class CarDetailFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private String car_id;
    private String share_img;
    private String share_title;
    private String share_txt;
    private String share_url;
    private String vr_url;
    private String contact_phone;
    private CarInfoBean carInfoBean;

    private static final int SAME_STYLE_CAR_DATA_TYPE = 28;

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
        ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
        iv_image.setVisibility(View.VISIBLE);
        iv_image.setImageResource(R.drawable.share_selector);
        iv_image.setOnClickListener(this);
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
            case R.id.iv_image:
                showSharePlatformPopWindow();
                break;
            case R.id.btn_watch_vr:
                if (EmptyUtils.isEmpty(vr_url)) {
                    ToastUtils.showShort("该车暂未拍摄");
                    return;
                } else {
                    gotoPager(VRWatchCarActivity.class, null);
                }
                break;
            case R.id.btn_line_contact:
                if (EmptyUtils.isNotEmpty(carInfoBean)) {
                    CarInfoBean.UserInfoBean userInfoBean = carInfoBean.getUser_info();
                    if (EmptyUtils.isNotEmpty(userInfoBean)) {
                        CarInfoBean.UserInfoBean.ProfileBean profileBean = userInfoBean.getProfile();
                        final int user_id = userInfoBean.getUser_id();
                        if (EmptyUtils.isNotEmpty(profileBean)) {
                            final String avatar = profileBean.getAvatar();
                            final String nick_name = profileBean.getNickname();

                            if (EmptyUtils.isNotEmpty(user_id) && EmptyUtils.isNotEmpty(avatar) && EmptyUtils.isNotEmpty(nick_name)) {
                                RongIM.getInstance().startConversation(mContext, Conversation.ConversationType.PRIVATE, String.valueOf(user_id), nick_name);
                                RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                                    @Override
                                    public UserInfo getUserInfo(String s) {
                                        return new UserInfo(String.valueOf(user_id), nick_name, Uri.parse(avatar));//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                                    }
                                }, true);
                            } else {
                                ToastUtils.showShort("该车的相关信息为空");
                            }
                        }
                    }
                }
                break;
            case R.id.btn_phone_contact:
                if (EmptyUtils.isEmpty(contact_phone)) {
                    ToastUtils.showShort("该车主没有联系方式");
                    return;
                } else {
                    showCallPhoneDialog(contact_phone);
                }
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
                                share_img = jsonObjectData.optString("share_img");
                                share_title = jsonObjectData.optString("share_title");
                                share_txt = jsonObjectData.optString("share_txt");
                                share_url = jsonObjectData.optString("share_url");
                                handlerCarInfoData(jsonObjectData.optJSONObject("car_info"));//处理此车辆的数据
                                handlerSameStyleCarData(jsonObjectData.optJSONArray("related_style_car_list"));//处理同款车辆的数据
                                handlerSamePriceCarData(jsonObjectData.optJSONArray("related_price_car_list"));//处理同价车辆的数据
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

    private void handlerSamePriceCarData(JSONArray related_price_car_list) {
        if (getView() != null) {
            RecyclerView recycler_view_same_style = (RecyclerView) getView().findViewById(R.id.recycler_view_same_price);
            TextView tv_same_style_car = (TextView) getView().findViewById(R.id.tv_same_price_car);
            if (EmptyUtils.isNotEmpty(related_price_car_list)) {
                if (related_price_car_list.length() == 0) {
                    tv_same_style_car.setVisibility(View.GONE);
                    recycler_view_same_style.setVisibility(View.GONE);
                    return;
                }
                Gson gson = new Gson();
                ArrayList<CarInfoBean> carInfoBeanArrayList = gson.fromJson(related_price_car_list.toString(), new TypeToken<ArrayList<CarInfoBean>>() {
                }.getType());
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, carInfoBeanArrayList, SAME_STYLE_CAR_DATA_TYPE);
                recycler_view_same_style.setAdapter(baseRecyclerViewAdapter);
                recycler_view_same_style.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false));
                baseRecyclerViewAdapter.setOnItemClickListener(this);
            } else {
                tv_same_style_car.setVisibility(View.GONE);
                recycler_view_same_style.setVisibility(View.GONE);
            }
        }
    }

    private void handlerSameStyleCarData(JSONArray related_style_car_list) {
        if (getView() != null) {
            RecyclerView recycler_view_same_style = (RecyclerView) getView().findViewById(R.id.recycler_view_same_style);
            TextView tv_same_style_car = (TextView) getView().findViewById(R.id.tv_same_style_car);
            if (EmptyUtils.isNotEmpty(related_style_car_list)) {
                if (related_style_car_list.length() == 0) {
                    tv_same_style_car.setVisibility(View.GONE);
                    recycler_view_same_style.setVisibility(View.GONE);
                    return;
                }
                Gson gson = new Gson();
                ArrayList<CarInfoBean> carInfoBeanArrayList = gson.fromJson(related_style_car_list.toString(), new TypeToken<ArrayList<CarInfoBean>>() {
                }.getType());
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, carInfoBeanArrayList, SAME_STYLE_CAR_DATA_TYPE);
                recycler_view_same_style.setAdapter(baseRecyclerViewAdapter);
                recycler_view_same_style.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false));
                baseRecyclerViewAdapter.setOnItemClickListener(this);
            } else {
                tv_same_style_car.setVisibility(View.GONE);
                recycler_view_same_style.setVisibility(View.GONE);
            }
        }
    }

    private void handlerCarInfoData(JSONObject car_info) {
        Gson gson = new Gson();
        carInfoBean = gson.fromJson(car_info.toString(), CarInfoBean.class);

        if (EmptyUtils.isNotEmpty(carInfoBean) && getView() != null) {
            vr_url = carInfoBean.getVr_url();
            contact_phone = carInfoBean.getContact_phone();
            ((TextView) getView().findViewById(R.id.tv_title)).setText(carInfoBean.getBrand_info().getBrand_name() + " " + carInfoBean.getSeries_info().getSeries_name());

            final CarInfoBean.FilesBean filesBean = carInfoBean.getFiles();
            if (EmptyUtils.isNotEmpty(filesBean)) {

                ArrayList<CarInfoBean.FilesBean.Type1Bean> type1BeanArrayList = (ArrayList<CarInfoBean.FilesBean.Type1Bean>) carInfoBean.getFiles().getType1();
                ArrayList<CarInfoBean.FilesBean.Type2Bean> type2BeanArrayList = (ArrayList<CarInfoBean.FilesBean.Type2Bean>) carInfoBean.getFiles().getType2();
                ArrayList<CarInfoBean.FilesBean.Type3Bean> type3BeanArrayList = (ArrayList<CarInfoBean.FilesBean.Type3Bean>) carInfoBean.getFiles().getType3();
                ArrayList<CarInfoBean.FilesBean.Type4Bean> type4BeanArrayList = (ArrayList<CarInfoBean.FilesBean.Type4Bean>) carInfoBean.getFiles().getType4();

                ArrayList<String> stringImageUrl = new ArrayList<>();

                //车辆照片类型 (1:外观 2:中控内饰 3:发动机及结构 4:更多细节)
                if (EmptyUtils.isNotEmpty(type1BeanArrayList) && type1BeanArrayList.size() != 0) {
                    //外观
                    ImageView iv_car_surface = (ImageView) getView().findViewById(R.id.iv_car_surface);
                    Glide.with(mContext)
                            .load(type1BeanArrayList.get(0).getFile_url())
                            .placeholder(R.drawable.default_bg_ratio_1)
                            .error(R.drawable.default_bg_ratio_1)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                            .into(iv_car_surface);
                    iv_car_surface.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DataManager.getInstance().setData1(filesBean);
                            gotoPager(AllImageFragment.class, null);
                        }
                    });
                    int size = type1BeanArrayList.size();
                    ((TextView) getView().findViewById(R.id.tv_car_surface_num)).setText("(" + size + ")");

                    for (int i = 0; i < type1BeanArrayList.size(); i++) {
                        stringImageUrl.add(type1BeanArrayList.get(i).getFile_url());
                    }
                }
                if (EmptyUtils.isNotEmpty(type2BeanArrayList) && type2BeanArrayList.size() != 0) {
                    //中控内饰
                    ImageView iv_car_inside = (ImageView) getView().findViewById(R.id.iv_car_inside);
                    Glide.with(mContext)
                            .load(type2BeanArrayList.get(0).getFile_url())
                            .placeholder(R.drawable.default_bg_ratio_1)
                            .error(R.drawable.default_bg_ratio_1)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                            .into(iv_car_inside);
                    iv_car_inside.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DataManager.getInstance().setData1(filesBean);
                            gotoPager(AllImageFragment.class, null);
                        }
                    });
                    int size = type2BeanArrayList.size();
                    ((TextView) getView().findViewById(R.id.tv_car_inside)).setText("(" + size + ")");

                    for (int i = 0; i < type2BeanArrayList.size(); i++) {
                        stringImageUrl.add(type2BeanArrayList.get(i).getFile_url());
                    }
                }
                if (EmptyUtils.isNotEmpty(type3BeanArrayList) && type3BeanArrayList.size() != 0) {
                    //发动机及结构
                    ImageView iv_car_structure = (ImageView) getView().findViewById(R.id.iv_car_structure);
                    Glide.with(mContext)
                            .load(type3BeanArrayList.get(0).getFile_url())
                            .placeholder(R.drawable.default_bg_ratio_1)
                            .error(R.drawable.default_bg_ratio_1)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                            .into(iv_car_structure);
                    iv_car_structure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DataManager.getInstance().setData1(filesBean);
                            gotoPager(AllImageFragment.class, null);
                        }
                    });
                    int size = type3BeanArrayList.size();
                    ((TextView) getView().findViewById(R.id.tv_car_structure)).setText("(" + size + ")");

                    for (int i = 0; i < type3BeanArrayList.size(); i++) {
                        stringImageUrl.add(type3BeanArrayList.get(i).getFile_url());
                    }
                }
                if (EmptyUtils.isNotEmpty(type4BeanArrayList) && type4BeanArrayList.size() != 0) {
                    //更多细节
                    ImageView iv_car_more_detail = (ImageView) getView().findViewById(R.id.iv_car_more_detail);
                    Glide.with(mContext)
                            .load(type4BeanArrayList.get(0).getFile_url())
                            .placeholder(R.drawable.default_bg_ratio_1)
                            .error(R.drawable.default_bg_ratio_1)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                            .into(iv_car_more_detail);
                    iv_car_more_detail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DataManager.getInstance().setData1(filesBean);
                            gotoPager(AllImageFragment.class, null);
                        }
                    });
                    int size = type4BeanArrayList.size();
                    ((TextView) getView().findViewById(R.id.tv_car_more_detail)).setText("(" + size + ")");

                    for (int i = 0; i < type4BeanArrayList.size(); i++) {
                        stringImageUrl.add(type4BeanArrayList.get(i).getFile_url());
                    }
                }

                ConvenientBanner convenientBanner = (ConvenientBanner) getView().findViewById(R.id.convenientBanner);
                if (EmptyUtils.isNotEmpty(stringImageUrl) && stringImageUrl.size() != 0) {
                    //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
                    //            convenientBanner.startTurning(5000);
                    convenientBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
                        @Override
                        public LocalImageHolderView createHolder() {
                            return new LocalImageHolderView();
                        }
                    }, stringImageUrl)
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
                }
            }

            if (EmptyUtils.isNotEmpty(carInfoBean.getCar_name())) {
                ((TextView) getView().findViewById(R.id.tv_car_name)).setText(carInfoBean.getCar_name());
            }
            if (EmptyUtils.isNotEmpty(carInfoBean.getPrice())) {
                ((TextView) getView().findViewById(R.id.tv_car_price)).setText(carInfoBean.getPrice() + "万");
            }
            ((TextView) getView().findViewById(R.id.tv_car_location)).setText(EmptyUtils.isEmpty(carInfoBean.getCity_name()) ? "深圳" : carInfoBean.getCity_name());
            ((TextView) getView().findViewById(R.id.tv_car_time)).setText(TimeUtils.date2String(new Date(Long.valueOf(carInfoBean.getCreated()) * 1000), new SimpleDateFormat("yyyy-MM-dd")));

            getView().findViewById(R.id.tv_check_all_car_configure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoPager(ConcreteParameterFragment.class, null);
                }
            });

            if (EmptyUtils.isNotEmpty(carInfoBean.getUser_info()) && EmptyUtils.isNotEmpty(carInfoBean.getUser_info().getProfile()) && EmptyUtils.isNotEmpty(carInfoBean.getUser_info().getProfile().getAvatar())) {
                Glide.with(mContext)
                        .load(carInfoBean.getUser_info().getProfile().getAvatar())
                        .placeholder(R.drawable.user_photo)
                        .into((ImageView) getView().findViewById(R.id.image_circle));
            }
            if (EmptyUtils.isNotEmpty(carInfoBean.getUser_info()) && EmptyUtils.isNotEmpty(carInfoBean.getUser_info().getProfile())) {
                ((TextView) getView().findViewById(R.id.tv_car_owner_nickname)).setText(carInfoBean.getUser_info().getProfile().getNickname());
            }
            getView().findViewById(R.id.tv_contact_car_owner).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (EmptyUtils.isNotEmpty(carInfoBean.getContact_phone())) {
                        showCallPhoneDialog(carInfoBean.getContact_phone());
                    }
                }
            });
            if (EmptyUtils.isNotEmpty(carInfoBean.getUser_info()) && EmptyUtils.isNotEmpty(carInfoBean.getUser_info().getProfile())) {
                ((TextView) getView().findViewById(R.id.tv_car_owner_detail)).setText(carInfoBean.getUser_info().getProfile().getSignature());
            }

            getView().findViewById(R.id.tv_check_all_car_service).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoPager(CarServiceFragment.class, null);
                }
            });
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("CarInfoBean")) {
            CarInfoBean carInfoBean = (CarInfoBean) data;
            if (EmptyUtils.isNotEmpty(carInfoBean)) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.CAR_ID, carInfoBean.getCar_id());
                gotoPager(CarDetailFragment.class, bundle);
            }
        }
    }

    private void showCallPhoneDialog(final String contact_phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.connect_to_buy));
        builder.setMessage(getString(R.string.connect_phone_number));
        builder.setPositiveButton(getString(R.string.call_phone), new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + contact_phone));
                startActivity(intent);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private class LocalImageHolderView implements Holder<String> {

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
        public void UpdateUI(Context context, int position, String data) {
            if (data != null) {
                Glide.with(context)
                        .load(data)
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .error(R.drawable.default_bg_ratio_1)
                        .into(iv_image);
            }
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
}
