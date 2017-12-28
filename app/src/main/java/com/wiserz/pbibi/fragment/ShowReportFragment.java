package com.wiserz.pbibi.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.RegisterAndLoginActivity;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.ReportBean;
import com.wiserz.pbibi.bean.TypeBean;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.ShareReportPopupWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import okhttp3.Call;

/**
 * Created by skylar on 2017/12/16 17:15.
 * Used as : 展示报价单
 */
public class ShowReportFragment extends BaseFragment {

    private ReportBean mReportBean;

    private ImageView iv_brand;
    private TextView  tv_car_name;
    private TextView  tvPromise;
    private TextView  showReportDate;
    private TextView  showCarType;
    private TextView  showGuidePrice;
    private TextView  showCarColor;
    private TextView  showPurchaseTax;
    private TextView  showInsurance;
    private TextView  showPremium;
    private TextView  showOtherCost;
    private TextView  showDetail;
    private TextView  showOther;
    private TextView  showTotalPrice;
    private TextView  showConsultTel;
    private TextView  showConsultName;
    private TextView  showReceiveAccount;
    private TextView  showAccountName;
    private TextView  showAccountBank;
    private LinearLayout ll_photo;
    private LinearLayout  ll_root;
    private LinearLayout llParent;
    private LinearLayout ll_receive;
    private LinearLayout  ll_account_name;
    private LinearLayout ll_account_bank;
    private LinearLayout llCarPhotos;
    private ScrollView  scrollView;
    private RelativeLayout rl_qr;
//    private RecyclerView  recyclerView;
    private BaseRecyclerViewAdapter baseRecyclerViewAdapter;
    private static final int REPORT_CAR_PHOTO_ITEM = 102;
    private String report_id = null;
    private Bitmap bitmap = null;
    private String path = null;
    private boolean showShare = false;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_show_report;
    }

    @Override
    protected void initView(View view) {
        report_id = getArguments().getString(Constant.REPORT_ID);
        mReportBean = (ReportBean) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText(getString(R.string.show_report));
        ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
        iv_image.setVisibility(View.VISIBLE);
        iv_image.setImageResource(R.drawable.share_selector);
        iv_image.setOnClickListener(this);

        ll_receive = (LinearLayout) view.findViewById(R.id.ll_receive);
        ll_account_name = (LinearLayout) view.findViewById(R.id.ll_account_name);
        ll_account_bank = (LinearLayout) view.findViewById(R.id.ll_account_bank);
        llParent = (LinearLayout) view.findViewById(R.id.llParent);
        ll_root = (LinearLayout) view.findViewById(R.id.ll_root);
        scrollView = (ScrollView) view.findViewById(R.id.scrollBasicMsg);

        iv_brand = (ImageView) view.findViewById(R.id.iv_brand);
        tv_car_name = (TextView) view.findViewById(R.id.tv_car_name);
        tvPromise = (TextView) view.findViewById(R.id.tvPromise);
        showReportDate = (TextView) view.findViewById(R.id.showReportDate);
        showCarType = (TextView) view.findViewById(R.id.showCarType);
        showGuidePrice = (TextView) view.findViewById(R.id.showGuidePrice);
        showCarColor = (TextView) view.findViewById(R.id.showCarColor);
        showPurchaseTax = (TextView) view.findViewById(R.id.showPurchaseTax);
        showInsurance = (TextView) view.findViewById(R.id.showInsurance);
        showPremium = (TextView) view.findViewById(R.id.showPremium);
        showOtherCost = (TextView) view.findViewById(R.id.showOtherCost);
        showDetail = (TextView) view.findViewById(R.id.showDetail);
        showOther = (TextView) view.findViewById(R.id.showOther);
        showTotalPrice = (TextView) view.findViewById(R.id.showTotalPrice);
        showConsultTel = (TextView) view.findViewById(R.id.showConsultTel);
        showConsultName = (TextView) view.findViewById(R.id.showConsultName);
        showReceiveAccount = (TextView) view.findViewById(R.id.showReceiveAccount);
        showAccountName = (TextView) view.findViewById(R.id.showAccountName);
        showAccountBank = (TextView) view.findViewById(R.id.showAccountBank);
        llCarPhotos = (LinearLayout) view.findViewById(R.id.llCarPhotos);
        ll_photo = (LinearLayout) view.findViewById(R.id.ll_photo);
        rl_qr = (RelativeLayout) view.findViewById(R.id.rl_qr);
    }


    @Override
    protected void initData() {
        super.initData();

//        if(mReportBean == null && EmptyUtils.isNotEmpty(report_id)){ //从历史报价单页面进入
//            getReportFromNet(report_id);
//        }else{ //生成报价单后进入
//            init();
//        }


    }

    @Override
    public void onStart() {
        super.onStart();

        if(mReportBean == null && EmptyUtils.isNotEmpty(report_id)){ //从历史报价单页面进入
            getReportFromNet(report_id);
        }else{ //生成报价单后进入
            init();
        }
    }

    private void init(){
        Glide.with(this).load(mReportBean.getInfo().getBrand_info().getBrand_url()).error(R.drawable.default_bg_ratio_1).into(iv_brand);
        tv_car_name.setText(mReportBean.getInfo().getBrand_name());
        if(EmptyUtils.isNotEmpty(mReportBean.getInfo().getPromise())){
            tvPromise.setText(mReportBean.getInfo().getPromise());
            tvPromise.setVisibility(View.VISIBLE);
        }

        showReportDate.setText(CommonUtil.timeToDate(String.valueOf(mReportBean.getInfo().getReport_time())));
        showCarType.setText(mReportBean.getInfo().getCar_name());

        int guidePrice = mReportBean.getInfo().getGuide_price();
        if(guidePrice != 0){
            showGuidePrice.setText(guidePrice+"万元");
        }else{
            showGuidePrice.setText("-");
        }

        int color = mReportBean.getInfo().getCar_color();
        if (color >= 0) {
            Resources res = getResources();
            String text = res.getString(res.getIdentifier("car_color_" + color, "string", getActivity().getPackageName()));
            showCarColor.setText(text);
        }

        int purchaseTax = mReportBean.getInfo().getPurch_fee();
        if(purchaseTax != 0){
            showPurchaseTax.setText(purchaseTax+"万元");
        }else{
            showPurchaseTax.setText("-");
        }

        int insurance = mReportBean.getInfo().getInsurance_fee();
        if(insurance != 0){
            showInsurance.setText(insurance +"万元");
        }else{
            showInsurance.setText("-");
        }

        int premium = mReportBean.getInfo().getBoard_fee();
        if(premium != 0){
            showPremium.setText( premium + "万元");
        }else{
            showPremium.setText("-");
        }

        int otherCost = mReportBean.getInfo().getOther_fee();
        String otherCostExplain = mReportBean.getInfo().getOther_fee_intro();
        if(otherCost == 0){
            if(EmptyUtils.isNotEmpty(otherCostExplain)){
                showOtherCost.setText(otherCostExplain);
            }else{
                showOtherCost.setText("-");
            }
        }else{
            if(EmptyUtils.isNotEmpty(otherCostExplain)){
                showOtherCost.setText(otherCostExplain + " " + otherCost + "万元");
            }else{
                showOtherCost.setText(otherCost + "万元");
            }
        }

        //配置详情
        List<ReportBean.Extra_info> extra_infos = mReportBean.getInfo().getExtra_info();
        if(EmptyUtils.isNotEmpty(extra_infos)){
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<extra_infos.size();i++){
                sb.append(extra_infos.get(i).getName()).append(" ");
            }
            showDetail.setText(sb.toString());
        }else{
            showDetail.setText("-");
        }

        showOther.setText(mReportBean.getInfo().getCar_intro());
        showTotalPrice.setText(mReportBean.getInfo().getTotal_price() +"万元");
        showConsultTel.setText(mReportBean.getInfo().getContact_phone());
        showConsultName.setText(mReportBean.getInfo().getContact_name());

        if(EmptyUtils.isNotEmpty(mReportBean.getInfo().getBank_no())){
            showReceiveAccount.setText(mReportBean.getInfo().getBank_no());
        }else{
            ll_receive.setVisibility(View.GONE);
        }

        if(EmptyUtils.isNotEmpty(mReportBean.getInfo().getBank_name())){
            showAccountBank.setText(mReportBean.getInfo().getBank_name());
        }else{
            ll_account_bank.setVisibility(View.GONE);
        }

        if(EmptyUtils.isNotEmpty(mReportBean.getInfo().getBank_account())){
            showAccountName.setText(mReportBean.getInfo().getBank_account());
        }else{
            ll_account_name.setVisibility(View.GONE);
        }

        ArrayList<TypeBean> mTypeList = mReportBean.getInfo().getFiles();

        if(EmptyUtils.isNotEmpty(mTypeList)){
            //有图片
            resetCarPhotosView(mTypeList);
        }else{
            ll_photo.setVisibility(View.GONE);
        }


    }

    private void resetCarPhotosView(List<TypeBean> mTypeList) {
        llCarPhotos.removeAllViews();
        int size;
        size = mTypeList.size();

        int row = (size % 2 == 0) ? size / 2 : size / 2 + 1;
        LinearLayout itemView;
        for (int i = 0; i < row; ++i) {
            itemView = (LinearLayout) View.inflate(mContext,R.layout.item_report_car_photo, null);
            llCarPhotos.addView(itemView);
            int index = i * 2;
            setPhoto((ViewGroup) itemView.getChildAt(0), index < size ? mTypeList.get(index).getFile_url():null);
            setPhoto((ViewGroup) itemView.getChildAt(1), index + 1 < size ? mTypeList.get(index+1).getFile_url() : null);

        }
    }

    private void setPhoto(ViewGroup itemView, String url) {
        ImageView ivPhoto = (ImageView) itemView.getChildAt(0);
        ImageView ivClose = (ImageView) itemView.getChildAt(1);
        if (url != null) {
            Glide.with(mContext).load(url).error(R.drawable.default_bg_ratio_1).into(ivPhoto);
            ivClose.setVisibility(View.GONE);
        } else {
            itemView.setVisibility(View.INVISIBLE);
        }
    }

    //获取历史报价列表
    private void getReportFromNet(String report_id){
        OkHttpUtils.post()
                .url(Constant.getReportViewUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.REPORT_ID, report_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("TESTLOG","获取报价单详情 onError===");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("TESTLOG","获取报价单详情 respnse===");
//                        CommonUtil.showLargeLog(response,3900);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                handlerDataToShow(jsonObjectData);
                            } else {
                                String code = jsonObject.optString("code");
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private void handlerDataToShow(JSONObject jsonObjectData) {
        Gson gson = new Gson();
        mReportBean = gson.fromJson(jsonObjectData.toString(), ReportBean.class);
        if(EmptyUtils.isNotEmpty(mReportBean)){
            init();
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.iv_image:
                if(mReportBean !=null){
                    showSharePlatformPopWindow();
                }
                break;
            default:
                break;
        }
    }


    private void showSharePlatformPopWindow() {
        ShareReportPopupWindow shareReportPopupWindow = new ShareReportPopupWindow(getActivity(), new ShareReportPopupWindow.ShareReportListener() {
            @Override
            public void onSinaWeiboClicked() {
                if (!CommonUtil.isHadLogin()) {
                    gotoPager(RegisterAndLoginActivity.class, null);
                    return;
                }
                showShare(mContext, "SinaWeibo", true);
            }

            @Override
            public void onWeChatClicked() {
                if (!CommonUtil.isHadLogin()) {
                    gotoPager(RegisterAndLoginActivity.class, null);
                    return;
                }
                showShare(mContext, "Wechat", true);
            }

            @Override
            public void onWechatMomentsClicked() {
                if (!CommonUtil.isHadLogin()) {
                    gotoPager(RegisterAndLoginActivity.class, null);
                    return;
                }
                showShare(mContext, "WechatMoments", true);
            }

            @Override
            public void onQQClicked() {
                if (!CommonUtil.isHadLogin()) {
                    gotoPager(RegisterAndLoginActivity.class, null);
                    return;
                }
                showShare(mContext, "QQ", true);
            }

            @Override
            public void onQQZoneClicked() {
                if (!CommonUtil.isHadLogin()) {
                      gotoPager(RegisterAndLoginActivity.class, null);
                      return;
                 }
                   showShare(mContext, "QZone", true);

            }

            @Override
            public void onCopyLinkClicke() {
                if (!CommonUtil.isHadLogin()) {
                    gotoPager(RegisterAndLoginActivity.class, null);
                    return;
                }
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                //创建ClipData对象
                ClipData clipData = ClipData.newPlainText("simple text copy", mReportBean.getShare_url());
                //添加ClipData对象到剪切板中
                clipboardManager.setPrimaryClip(clipData);
                ToastUtils.showShort(getString(R.string.copy_link_success));
            }

            @Override
            public void onCancelBtnClicked() {

            }

            @Override
            public void onSavePicClicked() {
                //保存快照
                if(EmptyUtils.isEmpty(bitmap) || EmptyUtils.isEmpty(path)){
                    bitmap = getScrollViewBitmap(scrollView);
//                    bitmap = saveBitmap();
                    if(EmptyUtils.isEmpty(bitmap)){
                        ToastUtils.showShort("保存快照失败");
                    }else{
                        path = CommonUtil.saveReportPicToAppAlbum(bitmap,getActivity(),String.valueOf(mReportBean.getInfo().getReport_time()));
                        Log.i("LOGTEST","path==="+path);
                        ToastUtils.showShort("成功保存快照到"+path);
                    }
                }else{
                    ToastUtils.showShort("成功保存快照到"+path);
                }
            }

            @Override
            public void onSharePicClicked() {
                //快照分享
//                showReportPicPopupWindow();
                if(EmptyUtils.isEmpty(path)){//先保存好图片，直接把图片的路径传过去
                    bitmap = getScrollViewBitmap(scrollView);
                    if(EmptyUtils.isNotEmpty(bitmap)){
                        path = CommonUtil.saveReportPicToAppAlbum(bitmap,getActivity(),String.valueOf(mReportBean.getInfo().getReport_time()));
                    }
                }
                DataManager.getInstance().setData1(mReportBean);
                Bundle bundle = new Bundle();
                bundle.putString("PATH",path);
                gotoPager(ShareReportFragment.class,bundle);

            }
        });
        shareReportPopupWindow.initView();
        shareReportPopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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

        oks.setTitle(mReportBean.getShare_title());
        if (platformToShare.equalsIgnoreCase("SinaWeibo")) {
            oks.setText(mReportBean.getShare_txt() + "\n" + mReportBean.getShare_url());
        }else if(platformToShare.equalsIgnoreCase("QQ")){
            oks.setImageUrl(mReportBean.getShare_img());
            oks.setTitleUrl(mReportBean.getShare_url());
            oks.setText(mReportBean.getShare_txt());
        } else if(platformToShare.equalsIgnoreCase("QZone")){
            oks.setImageUrl(mReportBean.getShare_img());
            oks.setTitleUrl(mReportBean.getShare_url());
            oks.setText(mReportBean.getShare_txt());
            oks.setSite("BibiCar");
            oks.setSiteUrl("www.bibicar.cn");
        }else {
            oks.setText(mReportBean.getShare_txt());
            oks.setImageUrl(mReportBean.getShare_img());
            oks.setUrl(mReportBean.getShare_url());
        }

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.i("LOGTEST","onComplete==="+i );

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.i("LOGTEST","onError==="+i + "异常信息：=="+throwable.getMessage());

            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.i("LOGTEST","onCancel==="+i);

            }
        });

        // 启动分享
        oks.show(context);


    }


//    private Bitmap saveBitmap(){
//        Bitmap bitmap1 = getScrollViewBitmap(scrollView);
//        Bitmap bitmap2 = getViewBitmap();
//        Log.i("TESTLOG","bitmap2======="+bitmap2);
//        Bitmap result = add2Bitmap(bitmap1,bitmap2);
//        return result;
//    }


    /**
     * 截取scrollview的屏幕
     **/
    public  Bitmap getScrollViewBitmap(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }

        // 创建对应大小的bitmap
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        bitmap = Bitmap.createBitmap(dm.widthPixels, h,
                Bitmap.Config.ARGB_4444);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor("#ffffff"));
        scrollView.draw(canvas);
        return bitmap;
    }


    /**
     *拼接图片
     * @param first 分享的长图
     * @param second  公司logo图
     *@author gengqiquan
     *@date 2017/3/25 下午4:56
     */
    public static Bitmap add2Bitmap(Bitmap first, Bitmap second) {
//        float scale = ((float) first.getWidth()) / second.getWidth();
//        second = ImageUtil.scaleImg(second, scale);

        int width = first.getWidth();
        int height = first.getHeight() + second.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, 0, first.getHeight(), null);
        return result;
    }

}
