package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.CarRentDetailInfoBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.view.SharePlatformPopupWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Call;

import static com.wiserz.pbibi.R.id.iv_image;

/**
 * Created by jackie on 2017/8/15 15:43.
 * QQ : 971060378
 * Used as : 汽车租赁详情的页面
 */
public class CarRentDetailFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private TextView tv_title;

    private String car_id;

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
                ToastUtils.showShort("联系客服");
                showShare();
                break;
            case R.id.btn_rent_car:
                Bundle bundle = new Bundle();
                bundle.putString(Constant.CAR_ID,car_id);
                gotoPager(CreateCarRentOrderFragment.class, bundle);
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
                                handlerDataForCarRentDetail(jsonObjectData);
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

    private void handlerDataForCarRentDetail(JSONObject jsonObjectData) {
        JSONObject jsonObject = jsonObjectData.optJSONObject("car_info");
        Gson gson = new Gson();
        CarRentDetailInfoBean carRentDetailInfoBean = gson.fromJson(jsonObject.toString(), CarRentDetailInfoBean.class);

        if (EmptyUtils.isNotEmpty(carRentDetailInfoBean) && getView() != null) {
            tv_title.setText(carRentDetailInfoBean.getBrand_info().getBrand_name());


            ((TextView) getView().findViewById(R.id.tv_car_name)).setText(carRentDetailInfoBean.getCar_name());
            ((TextView) getView().findViewById(R.id.tv_car_price)).setText("¥" + String.valueOf(carRentDetailInfoBean.getRental_info().getOne()) + "/日");
            ((TextView) getView().findViewById(R.id.tv_one)).setText("¥" + String.valueOf(carRentDetailInfoBean.getRental_info().getOne()) + "/日");
            ((TextView) getView().findViewById(R.id.tv_two)).setText("¥" + String.valueOf(carRentDetailInfoBean.getRental_info().getTwo()) + "/日");
            ((TextView) getView().findViewById(R.id.tv_three)).setText("¥" + String.valueOf(carRentDetailInfoBean.getRental_info().getThree()) + "/日");
            ((TextView) getView().findViewById(R.id.tv_four)).setText("¥" + String.valueOf(carRentDetailInfoBean.getRental_info().getFour()) + "/日");

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
                //                showShare(getActivity(), "SinaWeibo", true);
            }

            @Override
            public void onWeChatClicked() {
                //                showShare(getActivity(), "Wechat", true);
            }

            @Override
            public void onWechatMomentsClicked() {
                //                showShare(getActivity(), "WechatMoments", true);
            }

            @Override
            public void onCancelBtnClicked() {

            }
        });
        sharePlatformPopWindow.initView();
        sharePlatformPopWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(mContext);
    }

    //    /**
    //     * 演示调用ShareSDK执行分享
    //     *
    //     * @param context
    //     * @param platformToShare 指定直接分享平台名称（一旦设置了平台名称，则九宫格将不会显示）
    //     * @param showContentEdit 是否显示编辑页
    //     */
    //    private void showShare(Context context, String platformToShare, boolean showContentEdit) {
    //        OnekeyShare oks = new OnekeyShare();
    //        oks.setSilent(!showContentEdit);
    //        if (platformToShare != null) {
    //            oks.setPlatform(platformToShare);
    //        }
    //        //ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC  第二个是SKYBLUE
    //        oks.setTheme(OnekeyShareTheme.CLASSIC);
    //        // 令编辑页面显示为Dialog模式
    //        oks.setDialogMode();
    //        // 在自动授权时可以禁用SSO方式
    //        oks.disableSSOWhenAuthorize();
    //
    //        oks.setTitle(mShareTitle);
    //        if (platformToShare.equalsIgnoreCase("SinaWeibo")) {
    //            oks.setText(mShareText + "\n" + mShareUrl);
    //        } else {
    //            oks.setText(mShareImg);
    //            oks.setImageUrl(mShareImg);
    //            oks.setUrl(mShareUrl);
    //        }
    //
    //        // 启动分享
    //        oks.show(context);
    //    }
}
