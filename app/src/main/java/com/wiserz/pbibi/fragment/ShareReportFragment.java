package com.wiserz.pbibi.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.ReportBean;
import com.wiserz.pbibi.bean.TypeBean;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.ShareReportPicPopupWindow;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

/**
 * Created by jackie on 2017/12/20 9:05.
 * QQ : 971060378
 * Used as : xxx
 */
public class ShareReportFragment extends BaseFragment {

    private ReportBean mReportBean;

    private ImageView ivClose;
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
    private LinearLayout ll_receive;
    private LinearLayout  ll_account_name;
    private LinearLayout ll_account_bank;
    private LinearLayout llCarPhotos;
    private ScrollView  scrollView;
    private RelativeLayout rl_qr;
//    private RecyclerView  recyclerView;
    private BaseRecyclerViewAdapter baseRecyclerViewAdapter;
    private static final int REPORT_CAR_PHOTO_ITEM = 102;
//    private String report_id = null;
    private Bitmap bitmap = null;
    private String path = null;

    private boolean mShowShare = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_share_report;
    }

    @Override
    protected void initView(View view) {
        path = getArguments().getString("PATH");
        mReportBean = (ReportBean) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);

        ll_receive = (LinearLayout) view.findViewById(R.id.ll_receive);
        ll_account_name = (LinearLayout) view.findViewById(R.id.ll_account_name);
        ll_account_bank = (LinearLayout) view.findViewById(R.id.ll_account_bank);
        ll_root = (LinearLayout) view.findViewById(R.id.ll_root);
        scrollView = (ScrollView) view.findViewById(R.id.scrollBasicMsg);

        ivClose = (ImageView) view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(this);
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
//        recyclerView = (RecyclerView) view.findViewById(R.id.recycerView);
        ll_photo = (LinearLayout) view.findViewById(R.id.ll_photo);
        llCarPhotos = (LinearLayout) view.findViewById(R.id.llCarPhotos);
        rl_qr = (RelativeLayout) view.findViewById(R.id.rl_qr);


        ll_root.setOnClickListener(this);

    }


    @Override
    protected void initData() {
        super.initData();

//        init();

    }

    @Override
    public void onStart() {
        super.onStart();

        init();
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

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ivClose:
                goBack();
                break;
            case R.id.ll_root:
//                if(mShowShare){
                //                    mShowShare = false;
                //                }
                showSharePic();
                break;
            default:
                break;
        }
    }

    private void showSharePic() {
        ShareReportPicPopupWindow shareReportPicPopupWindow = new ShareReportPicPopupWindow(getActivity(), new ShareReportPicPopupWindow.SharePicReportListener() {
            @Override
            public void onWeChatClicked() {
                showSharePic(mContext, "Wechat", true);
            }

            @Override
            public void onWechatMomentsClicked() {
                showSharePic(mContext, "WechatMoments", true);
            }

            @Override
            public void onCancelBtnClicked() {

            }

            @Override
            public void onSavePicClicked() {
                if(EmptyUtils.isNotEmpty(path)){
                    ToastUtils.showShort("成功保存快照到"+path);
                }else{
                    ToastUtils.showShort("保存快照失败");
                }
            }
        });
        shareReportPicPopupWindow.initView();
        shareReportPicPopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

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
     * 演示调用ShareSDK执行分享
     *
     * @param context
     * @param platformToShare 指定直接分享平台名称（一旦设置了平台名称，则九宫格将不会显示）
     * @param showContentEdit 是否显示编辑页
     */
    private void showSharePic(Context context, String platformToShare, boolean showContentEdit) {

        if(path==null){
                ToastUtils.showShort("生成图片出现错误了");
                return;
        }

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

        oks.setImagePath(path);

        // 启动分享
        oks.show(context);
    }

}
