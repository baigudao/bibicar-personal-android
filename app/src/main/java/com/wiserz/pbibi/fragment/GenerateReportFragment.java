package com.wiserz.pbibi.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.wiserz.pbibi.BaseApplication;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.CarConfiguration;
import com.wiserz.pbibi.bean.CarInfoBean;
import com.wiserz.pbibi.bean.LoginBean;
import com.wiserz.pbibi.bean.ReportBean;
import com.wiserz.pbibi.bean.TypeBean;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.MorePopupWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import okhttp3.Call;

/**
 * Created by skylar on 2017/12/13 17:34.
 * Used as : 生成报价单
 */
public class GenerateReportFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener{

    private CarInfoBean mCarInfoBean;
    private ReportBean mReportBean;

    private ImageView iv_brand;
    private ImageView iv_promise;
    private RelativeLayout rl_choose_report_date;
    private RelativeLayout rl_choose_car_type;
    private RelativeLayout rl_choose_color;
    private RelativeLayout rl_choose_detail;
    private RelativeLayout rl_date;
    private LinearLayout llCarPhotos;
    private LinearLayout llTop;
    private LinearLayout llPromise;
    private TextView tv_car_name;
    private TextView tvReportDate;
    private TextView tvDetail;
    private TextView tvCarType;
    private TextView tvEdit;
    private TextView tvColor;
    private EditText editPromise;
    private EditText editInsurance;
    private EditText editGuidePrice;
    private EditText editPurchaseTax;
    private EditText editPremium;
    private EditText editOtherCost;
    private EditText editOtherCostExplain;
    private EditText editComment;
    private EditText editTotalPrice;
    private EditText editConsultantName;
    private EditText editConsultantTel;
    private EditText editReceiveAccount;
    private EditText editAccountName;
    private EditText editAccountBank;

    private CharSequence temp;
    private String mCarId;
    private String upload_token;
    private int mYear,mMonth,mDay;
    private int mColor;
//    private int mBrandId,mSeriesId,mModelId;
    private int mSaveStatus = 1;//1保存,2不保存
    private int flag;
    private boolean mHasPromise = true;//点击承诺按钮
    private boolean mFinishEdit = false;//点击编辑按钮
    private ArrayList<CarConfiguration> mCarConfigurationList;//从网络获取的配置详情数据
    private TreeMap<Integer, ArrayList<String>> extra_infos = new TreeMap<>();//存放选择的配置详情字符串（key表示类型，ArrayList表示某类型下的字符串）
    private Map<Integer,File> fileMap;
    private ArrayList<TypeBean> mAllImageUrls = new ArrayList<>();//存放该车辆的网络图片

    private JSONArray mPhotoTypes;
    private JSONArray mPhotoFile;

    private static final int SELECT_CAR_COLOR_DATA_TYPE = 33;
    private PopupWindow popupWindow;
    private String dstDirFile ;
    private int i;
    private static final int DOWN_SUCCESS = 1;

    private int editNum =0;
    private boolean loadData = true;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_generate_report;
    }

    @Override
    protected void initView(View view) {
        mCarId = getArguments().getString(Constant.CAR_ID);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText(getString(R.string.generate_offering_list));

        iv_brand = (ImageView) view.findViewById(R.id.iv_brand);
        iv_promise = (ImageView) view.findViewById(R.id.iv_promise);

        rl_choose_report_date = (RelativeLayout) view.findViewById(R.id.rl_choose_report_date);
        rl_choose_car_type = (RelativeLayout) view.findViewById(R.id.rl_choose_car_type);
        rl_choose_color = (RelativeLayout) view.findViewById(R.id.rl_choose_color);
        rl_choose_detail = (RelativeLayout) view.findViewById(R.id.rl_choose_detail);
        rl_date = (RelativeLayout) view.findViewById(R.id.rl_date);
        llTop = (LinearLayout) view.findViewById(R.id.llTop);
        llCarPhotos = (LinearLayout) view.findViewById(R.id.llCarPhotos);
        llPromise = (LinearLayout) view.findViewById(R.id.llPromise);

        tv_car_name = (TextView) view.findViewById(R.id.tv_car_name);
        tvReportDate = (TextView) view.findViewById(R.id.tvReportDate);
        tvDetail = (TextView) view.findViewById(R.id.tvDetail);
        tvCarType = (TextView) view.findViewById(R.id.tvCarType);
        tvColor = (TextView) view.findViewById(R.id.tvColor);
        tvEdit = (TextView) view.findViewById(R.id.tvEdit);

        editPromise = (EditText) view.findViewById(R.id.editPromise);
        editInsurance = (EditText) view.findViewById(R.id.editInsurance);
        editGuidePrice = (EditText) view.findViewById(R.id.editGuidePrice);
        editPurchaseTax = (EditText) view.findViewById(R.id.editPurchaseTax);
        editPremium = (EditText) view.findViewById(R.id.editPremium);
        editOtherCost = (EditText) view.findViewById(R.id.editOtherCost);
        editOtherCostExplain = (EditText) view.findViewById(R.id.editOtherCostExplain);
        editComment = (EditText) view.findViewById(R.id.editComment);
        editTotalPrice = (EditText) view.findViewById(R.id.editTotalPrice);
        editConsultantName = (EditText) view.findViewById(R.id.editConsultantName);
        editConsultantTel = (EditText) view.findViewById(R.id.editConsultantTel);
        editReceiveAccount = (EditText) view.findViewById(R.id.editReceiveAccount);
        editAccountName = (EditText) view.findViewById(R.id.editAccountName);
        editAccountBank = (EditText) view.findViewById(R.id.editAccountBank);

        rl_choose_report_date.setOnClickListener(this);
        rl_choose_car_type.setOnClickListener(this);
        rl_choose_color.setOnClickListener(this);
        rl_choose_detail.setOnClickListener(this);
//        iv_promise.setOnClickListener(this);
        rl_date.setOnClickListener(this);
        tvEdit.setOnClickListener(this);
        llPromise.setOnClickListener(this);

        //选择报价时间
        mYear= Calendar.getInstance().get(Calendar.YEAR);
        mMonth= Calendar.getInstance().get(Calendar.MONTH);
        mDay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        ((DatePicker) view.findViewById(R.id.datePicker)).init(mYear,
                mMonth,
                mDay, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                        mYear=year;
                        mMonth=month;
                        mDay=day;
                    }
                });

        view.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_date.setVisibility(View.GONE);
            }
        });
        view.findViewById(R.id.tvSure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int month=mMonth+1;
                String choose_time = mYear + "-" + (month < 10 ? (0 + "" + String.valueOf(month)) : String.valueOf(month)) + "-" + (mDay < 10 ? (0 + "" + String.valueOf(mDay)) : String.valueOf(mDay));
                tvReportDate.setText(choose_time);
                tvReportDate.setTextColor(getResources().getColor(R.color.main_text_color));
                rl_date.setVisibility(View.GONE);
            }
        });

        setEdit(false);
        getTokenFromNet();

        //填写联系人信息
        LoginBean.UserInfoBean userInfoBean = DataManager.getInstance().getUserInfo();
        if (userInfoBean != null) {
            editConsultantName.setText(TextUtils.isEmpty(userInfoBean.getProfile().getNickname()) ? "" : userInfoBean.getProfile().getNickname());
            editConsultantTel.setText(TextUtils.isEmpty(userInfoBean.getMobile()) ? "" : userInfoBean.getMobile());
        }


        dstDirFile = Environment.getExternalStorageDirectory() + "/Android/data/" + getActivity().getPackageName() + "/download";

        editReceiveAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //1234 6789 1234 6789 123     23   4
            //1234 6789 1234 678         19   3
            //1234 6789 1234 67         17  3

            @Override
            public void afterTextChanged(Editable s) {
//                int editStart = editConsultantName.getSelectionStart();
//                int editEnd = editConsultantName.getSelectionEnd();

                if(temp.length() < 5 * editNum ){
                    editNum--;
                }else{
                    if(temp.length() == 4 || ( temp.length() > 4 && (temp.length()-editNum) % 4 == 0 )){
                        s.append(' ');
                        editReceiveAccount.setText(s);
                        editReceiveAccount.setSelection(s.length());
                        editNum++;
                    }
                }

            }
        });

        ScrollView scrollBasicMsg = (ScrollView) view.findViewById(R.id.scrollBasicMsg);
        scrollBasicMsg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float y = 0;
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        y = motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float scrollY = motionEvent.getRawY();
                        if(Math.abs(scrollY-y)>20){
                            //如果键盘弹出则隐藏键盘
                            InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//                            boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
//                            if(isOpen){
//                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                            }
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        break;
                }
                return false;
            }
        });

    }



    @Override
    protected void initData() {
        super.initData();
//        if(EmptyUtils.isNotEmpty(mCarId)){
//            getCarDetailData();
//        }else{
//            Log.i("TESTLOG","mCarId is null ===");
//        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if(isAdded() && loadData){
            loadData = false;
            if(EmptyUtils.isNotEmpty(mCarId)){
                getCarDetailData();
            }else{
                Log.i("TESTLOG","mCarId is null ===");
            }
        }
    }

    private void setData() {
        Log.i("TESTLOG","setData===");
        //顶部车品牌标志 和个性单中的车型
        resetCarModelView(mCarInfoBean.getBrand_info().getBrand_name(),mCarInfoBean.getSeries_info().getSeries_name(),mCarInfoBean.getModel_info().getModel_name(),mCarInfoBean.getBrand_info().getBrand_url());
        //颜色
        resetColorView(mCarInfoBean.getCar_color());
        //报价日期
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new java.util.Date());
        tvReportDate.setText(date);
        tvReportDate.setTextColor(getResources().getColor(R.color.main_text_color));
        //承诺
        editPromise.setText(String.format(getString(R.string.promise_text),mCarInfoBean.getBrand_info().getBrand_name(),mCarInfoBean.getSeries_info().getSeries_name()+" "+mCarInfoBean.getModel_info().getModel_name()));
        //详情配置
        ArrayList<CarConfiguration.Configuration> configList = mCarInfoBean.getCar_extra_info();
        if(EmptyUtils.isNotEmpty(configList)){
            //将详情配置数据存入extra_info
            for(CarConfiguration.Configuration configuration : configList){
                ArrayList<String> list = extra_infos.get(configuration.getType());
                if (list == null) {
                    list = new ArrayList<>();
                    extra_infos.put(configuration.getType(), list);
                }

                list.add(configuration.getId() + "_" + configuration.getName());
            }
            resetFilterView(llTop);
        }
    }


    private void getCarDetailData(){
            OkHttpUtils.post()
                    .url(Constant.getCarDetailUrl())
                    .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                    .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                    .addParams(Constant.CAR_ID, mCarId)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            CommonUtil.showLargeLog("getcardetail response==="+response,3900);
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
                                    ToastUtils.showShort("" + msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
    }

    private void handlerCarInfoData(JSONObject car_info) {
        Gson gson = new Gson();
        mCarInfoBean = gson.fromJson(car_info.toString(), CarInfoBean.class);

        if (EmptyUtils.isNotEmpty(mCarInfoBean)){
            setData();
            //获取图片,不能超过9张
            //获取图片,不能超过12张
            mAllImageUrls.clear();
            mAllImageUrls = mCarInfoBean.getFiles();
            //设置图片
            if(EmptyUtils.isNotEmpty(mAllImageUrls)){
                if(EmptyUtils.isNotEmpty(getUploadPhotos())){
                    getUploadPhotos().clear();
                }
                downLoadPic();
            }else{
                resetCarPhotosView();
            }

        }else{
            Log.i("TESTLOG","carbean is  null ===");
        }


    }

    private void downLoadPic(){
            String fileName = UUID.randomUUID().toString() + ".jpg";
            Log.i("TESTLOG","downLoadPic === url == " + mAllImageUrls.get(i).getFile_url() );
            Log.i("TESTLOG","downLoadPic === fileName ==" + fileName );
            OkHttpUtils.get()
                    .url(mAllImageUrls.get(i).getFile_url())
                    .build()
                    .execute(new FileCallBack(dstDirFile,fileName) {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(File response, int id) {
                            getUploadPhotos().put(mAllImageUrls.get(i).getFile_type(),response);
                            mHandler.sendEmptyMessage(DOWN_SUCCESS);
                        }
                    });

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DOWN_SUCCESS:
                    if(i == mAllImageUrls.size()-1){
                        resetCarPhotosView();
                    }else{
                        //下载下一个
                        i++;
                        downLoadPic();
                    }

                    break;
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        if (DataManager.getInstance().getObject() != null && DataManager.getInstance().getData1() != null) {
            String newPath = (String) DataManager.getInstance().getObject();
            int index = (int) DataManager.getInstance().getData1();
            getUploadPhotos().put(index, new File(newPath));
            DataManager.getInstance().setObject(null);
            DataManager.getInstance().setData1(null);
            resetCarPhotosView();
        } else if (DataManager.getInstance().getData8() != null) {
            int index = (int) DataManager.getInstance().getData8();
            getUploadPhotos().remove(index);
            DataManager.getInstance().setData8(null);
            resetCarPhotosView();
        } else if (DataManager.getInstance().getObject() != null) {
            getUploadPhotos().putAll( (HashMap<Integer,File>)DataManager.getInstance().getObject());
            DataManager.getInstance().setObject(null);
            resetCarPhotosView();
        }
    }



    //Fragment的显示状态发生改变时调用
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i("TESTLOG","onHiddenChanged ===");
        if(!isHidden()){//Fragment显示了

            Object data1 = DataManager.getInstance().getData1();
            Object data2 = DataManager.getInstance().getData2();
            Object data3 = DataManager.getInstance().getData3();
            Object data4 = DataManager.getInstance().getData4();
            Object data5 = DataManager.getInstance().getData5();
            Object data6 = DataManager.getInstance().getData6();
            Object data7 = DataManager.getInstance().getData7();
            if (EmptyUtils.isNotEmpty(data1)
                    && EmptyUtils.isNotEmpty(data2)
                    && EmptyUtils.isNotEmpty(data3)
                    && EmptyUtils.isNotEmpty(data4)
                    && EmptyUtils.isNotEmpty(data5)
                    && EmptyUtils.isNotEmpty(data6)
                    && EmptyUtils.isNotEmpty(data7)) {
                if (data1 instanceof Integer && data2 instanceof String
                        && data3 instanceof Integer && data4 instanceof String
                        && data5 instanceof Integer && data6 instanceof String
                        && data7 instanceof String) {
                    mCarInfoBean.getBrand_info().setBrand_id((int) data1);
                    String brand_name = (String) data2;
                    mCarInfoBean.getSeries_info().setSeries_id((int) data3);
                    String series_name = (String) data4;
                    mCarInfoBean.getModel_info().setModel_id((int) data5);
                    String model_name = (String) data6;
                    String brand_url = (String) data7;
                    resetCarModelView(brand_name, series_name, model_name,brand_url);
                }
                DataManager.getInstance().setData1(null);
                DataManager.getInstance().setData2(null);
                DataManager.getInstance().setData3(null);
                DataManager.getInstance().setData4(null);
                DataManager.getInstance().setData5(null);
                DataManager.getInstance().setData6(null);
                DataManager.getInstance().setData7(null);
            }
        }
    }

    //设置车型
    private void resetCarModelView(String brand_name, String series_name, String model_name ,String brand_url) {
        //设置个性单的车型、顶部的车型、顶部的品牌图标
        tvCarType.setTextColor(getResources().getColor(R.color.main_text_color));
        tvCarType.setText(brand_name + " " + series_name + " " + model_name );
        tv_car_name.setText(brand_name);
        Glide.with(this).load(brand_url).error(R.drawable.default_bg_ratio_1).into(iv_brand);
        //承诺
        editPromise.setText(String.format(getString(R.string.promise_text),brand_name,series_name+" "+model_name));

    }

    //设置车辆颜色
    private void resetColorView(int color) {
        Log.i("TESTLOG","select color==="+color);
        if (color >= 0) {
            mColor = color;
            Resources res = getResources();
            String text = res.getString(res.getIdentifier("car_color_" + color, "string", getActivity().getPackageName()));
            tvColor.setText(text);
            tvColor.setTextColor(getResources().getColor(R.color.main_text_color));
        }
    }

    private void resetCarPhotosView() {
        llCarPhotos.removeAllViews();
        int size;
        if (getUploadPhotos().size() == Constant.MAX_UPLOAD_PHOTO_NUM) {
            size = Constant.MAX_UPLOAD_PHOTO_NUM;
        } else {
            size = getUploadPhotos().size() + 1;
        }

        Iterator<Map.Entry<Integer, File>> iterator = getUploadPhotos().entrySet().iterator();

        int row = (size % 2 == 0) ? size / 2 : size / 2 + 1;
        LinearLayout itemView;
        for (int i = 0; i < row; ++i) {
            itemView = (LinearLayout) View.inflate(mContext,R.layout.item_report_car_photo, null);
            llCarPhotos.addView(itemView);

            int index = i * 2;
            File photo = null;
            int key = -1;
            if(iterator.hasNext()) {
                Map.Entry<Integer, File> entry = iterator.next();
                key = entry.getKey();
                photo = entry.getValue();
            }

            setPhoto((ViewGroup) itemView.getChildAt(0),photo,key,index, index == size - 1);

            key = -1;
            photo = null;
            if(iterator.hasNext()) {
                Map.Entry<Integer, File> entry = iterator.next();
                key = entry.getKey();
                photo = entry.getValue();
            }

            setPhoto((ViewGroup) itemView.getChildAt(1),photo,key,index + 1, index + 1 == size - 1);

        }
    }

    public void setPhoto(ViewGroup itemView, final File photo, final int key, int index, boolean isLast) {
        ImageView ivPhoto = (ImageView) itemView.getChildAt(0);
        ImageView ivClose = (ImageView) itemView.getChildAt(1);

        if (photo != null) {
            CommonUtil.loadImage(BaseApplication.getAppContext(), 0, Uri.fromFile(photo), ivPhoto);
            ivClose.setVisibility(View.VISIBLE);
            ivPhoto.setTag(R.id.tag, index);
            ivClose.setTag(R.id.tag, index);
            ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag(R.id.tag);
                    if (pos < getUploadPhotos().size()) {
                        DataManager.getInstance().setObject(photo.getAbsolutePath());
                        DataManager.getInstance().setData1(pos);
                        DataManager.getInstance().setData2(getUploadPhotos().size());
                        DataManager.getInstance().setData3(key);
                        ((BaseActivity) mContext).gotoPager(EditPhotoFragment.class, null, true);
                    }

                }
            });
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag(R.id.tag);
                    File file = getUploadPhotos().remove(key);
                    if (file != null) {
                        file.delete();
                    }
                    resetCarPhotosView();

                }
            });

        }else {
            if (isLast) {
                ivPhoto.setImageResource(R.drawable.add_car_photo);
                ivPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //选择进入相机或者相册
                        showSelectPhotoWindow();
                    }
                });
                ivClose.setVisibility(View.GONE);
            } else {
                itemView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void showSelectPhotoWindow() {
        MorePopupWindow morePopupWindow = new MorePopupWindow(getActivity(), new MorePopupWindow.MorePopupWindowClickListener() {
            @Override
            public void onFirstBtnClicked() {
                //拍照
                Bundle bundle = new Bundle();
                bundle.putBoolean("select_album", false);
                DataManager.getInstance().setObject(getUploadPhotos().size());
                DataManager.getInstance().setData1(getUploadPhotos());
                gotoPager(SelectPhotoFragment.class, bundle, true);
            }

            @Override
            public void onSecondBtnClicked() {
                //相册
                Bundle bundle = new Bundle();
                bundle.putBoolean("select_album", true);
                DataManager.getInstance().setObject(getUploadPhotos().size());
                DataManager.getInstance().setData1(getUploadPhotos());
                gotoPager(SelectPhotoFragment.class, bundle, true);
            }

            @Override
            public void onThirdBtnClicked() {

            }

            @Override
            public void onFourthBtnClicked() {

            }

            @Override
            public void onCancelBtnClicked() {

            }
        }, MorePopupWindow.MORE_POPUP_WINDOW_TYPE.TYPE_ADD_PICTURE_MODEL);
        morePopupWindow.initView();
        morePopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.llPromise:
                mHasPromise = !mHasPromise;
                if(!mHasPromise){//未选中承诺
                    editPromise.clearFocus();
                    editPromise.setFocusableInTouchMode(false);
                    editPromise.setTextColor(getResources().getColor(R.color.second_text_color));
                }else{
                    editPromise.setFocusableInTouchMode(true);
                    editPromise.setTextColor(getResources().getColor(R.color.main_text_color));
                }
                iv_promise.setImageResource(mHasPromise?R.drawable.shape_circle_press:R.drawable.shape_circle);
                break;
            case R.id.tvEdit:
                 if(!mFinishEdit){//进入编辑状态，底部按钮显示为完成
                     setEdit(true);
                     mFinishEdit = !mFinishEdit;
                     tvEdit.setText(getString(R.string.done));
                     editPromise.requestFocus();
                     editPromise.setTextColor(getResources().getColor(R.color.main_text_color));
                     editPromise.setSelection(editPromise.getText().length());
                     InputMethodManager imm=(InputMethodManager)editPromise.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                     imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
                 }else{//完成编辑
                     checkInput();
                 }

                break;
            case R.id.rl_choose_report_date://选择报价日期
                rl_date.bringToFront();
                rl_date.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_date:
                rl_date.setVisibility(View.GONE);
                break;
            case R.id.rl_choose_car_type://选择车辆型号
                gotoPager(SelectCarBrandFragment.class, null);
                break;
            case R.id.rl_choose_color://选择车辆颜色
//                gotoPager(SelectCarColorFragment.class, null);

                flag = 1;
                View colorView = View.inflate(mContext, R.layout.layout_popwindow, null);
                carSelectPopupWindow(colorView);

                break;
            case R.id.rl_choose_detail://选择配置详情
                flag = 2;
                if (mCarConfigurationList == null || mCarConfigurationList.isEmpty()) {
                    getCarExtraInfo();
                }else{
                    View car_filter_view = View.inflate(mContext, R.layout.layout_popwindow, null);
                    carSelectPopupWindow(car_filter_view);
                }

                break;
            default:
                break;
        }
    }

    private void setEdit(boolean flag){
        iv_promise.setClickable(flag);
        editPromise.setFocusableInTouchMode(flag);
        rl_choose_report_date.setClickable(flag);
        rl_choose_car_type.setClickable(flag);
        rl_choose_color.setClickable(flag);
        editInsurance.setFocusableInTouchMode(flag);
        editGuidePrice.setFocusableInTouchMode(flag);
        editPurchaseTax.setFocusableInTouchMode(flag);
        editPremium.setFocusableInTouchMode(flag);
        editOtherCost.setFocusableInTouchMode(flag);
        editOtherCostExplain.setFocusableInTouchMode(flag);
        rl_choose_detail.setClickable(flag);
        editComment.setFocusableInTouchMode(flag);
        editTotalPrice.setFocusableInTouchMode(flag);
        editConsultantName.setFocusableInTouchMode(flag);
        editConsultantTel.setFocusableInTouchMode(flag);
        editReceiveAccount.setFocusableInTouchMode(flag);
        editAccountName.setFocusableInTouchMode(flag);
        editAccountBank.setFocusableInTouchMode(flag);
    }


    private void showSaveDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        AlertDialog alertDialog = builder.setMessage("是否保存到历史报价单?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        gotoPager(RegisterAndLoginActivity.class, null);
//                        ((EmptyActivity) mContext).finish();
                        //提交报价单
                        mSaveStatus = 1;
                        postReport();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        mSaveStatus = 2;
                        postReport();
                    }
                }).create();
        alertDialog.show();
    }

    private void checkInput(){
        //检查必填项是否都输入了

        if(mHasPromise){
            String promise = editPromise.getText().toString().trim();
            if(EmptyUtils.isEmpty(promise)){
                ToastUtils.showShort(R.string.input_promise);
                return;
            }
        }

        String report_time = tvReportDate.getText().toString();
        if(EmptyUtils.isEmpty(report_time)){
            ToastUtils.showShort(R.string.set_report_time);
            return;
        }

        String cartype = tvCarType.getText().toString();
        if(EmptyUtils.isEmpty(cartype)){
            ToastUtils.showShort(R.string.input_car_type);
            return;
        }

        String carColor = tvColor.getText().toString();
        if(EmptyUtils.isEmpty(carColor)){
            ToastUtils.showShort(R.string.input_car_color);
            return;
        }

        String insurance = editInsurance.getText().toString().trim();
        if(EmptyUtils.isEmpty(insurance)){
            ToastUtils.showShort(R.string.input_insurance);
            return;
        }

        String guidePrice = editGuidePrice.getText().toString().trim();
        if(EmptyUtils.isEmpty(guidePrice)){
            ToastUtils.showShort(R.string.input_guide_price);
            return;
        }

        String purch_fee = editPurchaseTax.getText().toString().trim();
        if(EmptyUtils.isEmpty(purch_fee)){
            ToastUtils.showShort(R.string.input_purchase_tax);
            return;
        }

        String premium = editPremium.getText().toString().trim();
        if(EmptyUtils.isEmpty(premium)){
            ToastUtils.showShort(R.string.input_premium);
            return;
        }

        String otherCost = editOtherCost.getText().toString().trim();
        if(EmptyUtils.isEmpty(otherCost)){
            ToastUtils.showShort(R.string.input_other);
            return;
        }

        String totalPrice = editTotalPrice.getText().toString().trim();
        if(EmptyUtils.isEmpty(totalPrice)){
            ToastUtils.showShort(R.string.input_total_price);
            return;
        }

        String consultName = editConsultantName.getText().toString().trim();
        if(EmptyUtils.isEmpty(consultName)){
            ToastUtils.showShort(R.string.input_consultant_name);
            return;
        }
        String consultTel = editConsultantTel.getText().toString().trim();
        if(EmptyUtils.isEmpty(consultTel)){
            ToastUtils.showShort(R.string.input_consultant_tel);
            return;
        }

//        String receiveAccount = editReceiveAccount.getText().toString().trim();
//        if(EmptyUtils.isEmpty(receiveAccount)){
//            ToastUtils.showShort(R.string.input_receive_account);
//            return;
//        }
//        String accountName = editAccountName.getText().toString().trim();
//        if(EmptyUtils.isEmpty(accountName)){
//            ToastUtils.showShort(R.string.input_account_name);
//            return;
//        }
//        String accountBank = editAccountBank.getText().toString().trim();
//        if(EmptyUtils.isEmpty(accountBank)){
//            ToastUtils.showShort(R.string.input_account_bank);
//            return;
//        }

        showSaveDialog();
    }

    //生成报价单
    private void postReport(){
        mPhotoTypes = new JSONArray();
        mPhotoFile = new JSONArray();
//        if(EmptyUtils.isNotEmpty(mAllImageUrls)){
//            for(int i=0;i<mAllImageUrls.size();i++){ //将选取的网络图片组成数组包
//                mPhotoTypes.put(mAllImageUrls.get(i).getFile_type());
//                mPhotoFile.put(mAllImageUrls.get(i).getFile_id());
//            }
//        }

        if(EmptyUtils.isNotEmpty(getUploadPhotos())){//需要上传本地图片到七牛
            uploadImage(getUploadPhotos(),upload_token);
        }else{
            commitDataToServer(mPhotoTypes, mPhotoFile);//直接提交数据
        }

    }

    private void uploadImage(Map<Integer,File> uploadPhotos, String upload_token) {
        final int mSize = uploadPhotos.size();

        UploadManager uploadManager = new UploadManager();

        Iterator<Map.Entry<Integer, File>> iterator = uploadPhotos.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, File> entry = iterator.next();
            int file_type = entry.getKey() + 1;//从1开始
            File photoPath = entry.getValue();

            if(EmptyUtils.isNotEmpty(photoPath)){
                uploadManager.put(photoPath, UUID.randomUUID().toString() + "_" + String.valueOf(file_type), upload_token, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (info.isOK()) {
                            //上传成功
                            int status = response.optInt("status");
                            JSONObject jsonObjectData = response.optJSONObject("data");
                            if (status == 1) {
                                String file_type = key.substring(key.lastIndexOf("_") + 1);
                                mPhotoTypes.put(file_type);
                                String hash = jsonObjectData.optString("hash");
                                mPhotoFile.put(hash);
                                if (mPhotoTypes.length() == mSize&& mPhotoFile.length() == mSize ) {
                                    commitDataToServer(mPhotoTypes, mPhotoFile);
                                }
                            } else {
                                String code = response.optString("code");
                                final String msg = jsonObjectData.optString("msg");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showShort("" + msg);
                                    }
                                });
                            }
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showShort("车辆照片上传失败，请重新上传");
                                }
                            });
                        }
                    }
                }, null);
            }

        }

    }

    private void commitDataToServer(JSONArray mPhotoTypes, JSONArray mPhotoFile) {
        OkHttpUtils.post()
                .url(Constant.getReportCreateUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.FILES_TYPE, mPhotoTypes.toString())
                .addParams(Constant.FILES_ID, mPhotoFile.toString())
                .addParams(Constant.CAR_ID, String.valueOf(mCarInfoBean.getId()))//id (int)
                .addParams(Constant.HASH, mCarInfoBean.getCar_id())//car_id
                .addParams(Constant.CAR_COLOR, String.valueOf(mColor))
                .addParams(Constant.BRAND_ID, String.valueOf(mCarInfoBean.getBrand_info().getBrand_id()))
                .addParams(Constant.SERIES_ID, String.valueOf(mCarInfoBean.getSeries_info().getSeries_id()))
                .addParams(Constant.MODEL_ID, String.valueOf(mCarInfoBean.getModel_info().getModel_id()))//int
                .addParams(Constant.CONTACT_PHONE, editConsultantTel.getText().toString().trim())//string
                .addParams(Constant.CONTACT_NAME, editConsultantName.getText().toString().trim())
                .addParams(Constant.CONTACT_ADDRESS, "")
                .addParams(Constant.GUIDE_PRICE, editGuidePrice.getText().toString().trim())
                .addParams(Constant.BOARD_FEE, editPremium.getText().toString().trim())
                .addParams(Constant.INSURANCE_FEE, editInsurance.getText().toString().trim())
                .addParams(Constant.OTHER_FEE, editOtherCost.getText().toString().trim())
                .addParams(Constant.OTHER_FEE_INTRO, editOtherCostExplain.getText().toString().trim())
                .addParams(Constant.BANK_NO, editReceiveAccount.getText().toString().trim())
                .addParams(Constant.BANK_NAME, editAccountBank.getText().toString().trim())
                .addParams(Constant.BANK_ACCOUNT, editAccountName.getText().toString().trim())
                .addParams(Constant.EXTRA_INFO, getExtraInfo())//基本配置选项(id与逗号拼接字符串 2,3,4,5)
                .addParams(Constant.PROMISE, mHasPromise?editPromise.getText().toString().trim():"")
                .addParams(Constant.TOTAL_PRICE,editTotalPrice.getText().toString().trim() )
                .addParams(Constant.PURCH_FEE, editPurchaseTax.getText().toString().trim())
                .addParams(Constant.REPORT_TIME, CommonUtil.dateToStamp(tvReportDate.getText().toString()))
                .addParams(Constant.CAR_INTRO, editComment.getText().toString().trim())//String
                .addParams(Constant.STATUS,String.valueOf(mSaveStatus) )//int
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
        DataManager.getInstance().setData1(mReportBean);
        gotoPager(ShowReportFragment.class,null);
    }


    //获取上传图片的token
    private void getTokenFromNet() {
        OkHttpUtils.post()
                .url(Constant.getUploadTokenUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("TESTLOG", "getTokenFromNet response: " + response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                upload_token = jsonObjectData.optString("upload_token");
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


    private String getExtraInfo(){
        StringBuilder sb = new StringBuilder();
        Iterator<TreeMap.Entry<Integer, ArrayList<String>>> entries = extra_infos.entrySet().iterator();
        while (entries.hasNext()) {
            TreeMap.Entry<Integer, ArrayList<String>> entry = entries.next();
            int key = entry.getKey();
            ArrayList<String> infos = entry.getValue();
            if (infos != null && !infos.isEmpty()) {
                for (String str : infos) {
                    String[] strs = str.split("_");
                    sb.append(strs[0]).append(",");

                }
            }
        }
        if(sb.length()>1){
            sb.deleteCharAt(sb.length()-1);
        }
        Log.i("TESTLOG","getExtraInfo sb =="+sb.toString());
        return sb.toString();
    }


    private Map<Integer,File> getUploadPhotos() {
        if (fileMap == null) {
            fileMap = new HashMap<>();
        }
        return fileMap;
    }






    //从网络获取配置详情数据
    private void getCarExtraInfo() {
        OkHttpUtils.post()
                .url(Constant.getCarExtraInfoUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("TESTLOG","get mCarConfigurationList response" + response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONArray jsonObjectData = jsonObject.optJSONArray("data");
                            if (status == 1) {
                                mCarConfigurationList = getCarConfigurations(jsonObjectData);
                                View car_filter_view = View.inflate(mContext, R.layout.layout_popwindow, null);
                                carSelectPopupWindow(car_filter_view);
                            } else {
                                String code = jsonObject.optString("code");
                                String msg = jsonObject.optString("msg");
                                ToastUtils.showShort("" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    //将配置详情数据解析成集合对象
    private ArrayList<CarConfiguration> getCarConfigurations(JSONArray array) {
        if (array == null) {
            return new ArrayList<>();
        } else {
            return new Gson().fromJson(array.toString(), new TypeToken<ArrayList<CarConfiguration>>() {
            }.getType());
        }
    }

    //弹出侧边栏
    private void carSelectPopupWindow(final View view) {
        if(popupWindow != null){
            popupWindow.dismiss();
            popupWindow = null;
        }
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //外部是否可以点击
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(getView().findViewById(R.id.topView));
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation2);

        //对返回按键的捕获并处理
        popupWindow.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setFocusable(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    if(popupWindow!= null){
                        popupWindow.dismiss();
                        popupWindow = null;
                    }
                }
                return false;
            }
        });

        //点击阴影部分退出
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow!= null){
                    popupWindow.dismiss();
                    popupWindow = null;
                }
//                if(flag == 1){
//                    resetColorView(mColor);
//                }else if(flag == 2){
//                    resetFilterView(llTop);
//                }
            }
        });

        //点击返回箭头退出，加入选择的配置详情item到报价单
        view.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow!= null){
                    popupWindow.dismiss();
                    popupWindow = null;
                }
//
            }
        });

        switch (flag) {
            case 1://颜色选择
                resetColorConfiguration(view);
                break;
            case 2: //初始化详细配置
                resetCarConfigurations(view, mCarConfigurationList);
                break;
            default:
                break;
        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (getView() != null) {
                    switch (flag) {
                        case 1:
                            resetColorView(mColor);
                            break;
                        case 2:
                            resetFilterView(llTop);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    private void resetColorConfiguration(View view){
        TextView tvBack = (TextView) view.findViewById(R.id.tvBack);
        tvBack.setText("选择颜色");

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);

        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            strings.add("color" + i);
        }
        BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, strings, SELECT_CAR_COLOR_DATA_TYPE);
        recyclerView.setAdapter(baseRecyclerViewAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false));
        baseRecyclerViewAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("String")) {
            Log.i("TESTLOG","onItemClick ===");
            mColor = position;

            if(popupWindow!= null){
                popupWindow.dismiss();
                popupWindow = null;
            }
        }
    }

    //获取选中的字符串
    private ArrayList<String> getExtraString (int type){
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> infos = extra_infos.get(type);
        if(EmptyUtils.isNotEmpty(infos)){
            for (String str : infos) {
                String[] strs = str.split("_");
                list.add(strs[1]);

            }
        }

        return list;
    }


    //详细配置,加入一个一个的字符串方框
    private void resetCarConfigurations(View view, ArrayList<CarConfiguration> list) {
//        extra_infos.clear();
        LinearLayout llDetailMsg = (LinearLayout) view.findViewById(R.id.llDetailMsg);
        llDetailMsg.removeAllViews();

        if (list == null || list.isEmpty()) {
            return;
        }

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        LinearLayout layout;
        ArrayList<CarConfiguration.Configuration> itemList;
        for (CarConfiguration config : list) {//一个类型一个类型的加入
            layout = (LinearLayout) layoutInflater.inflate(R.layout.item_car_configuration, null);
            layout.findViewById(R.id.line).setVisibility(View.GONE);
            llDetailMsg.addView(layout);
            final ViewGroup itemView1 = (ViewGroup) layout.getChildAt(0);

            ArrayList<String> typeName = null;
            if(EmptyUtils.isNotEmpty(extra_infos)){
                int conType = config.getType_id();
                typeName = getExtraString(conType);
            }

            ((TextView) itemView1.getChildAt(0)).setText(config.getType_name());
            itemView1.getChildAt(1).setVisibility(View.VISIBLE);
            itemView1.getChildAt(2).setVisibility(View.VISIBLE);
            itemView1.setTag(0);
            itemView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) itemView1.getTag();
                    ViewGroup viewGroup = (ViewGroup) (((ViewGroup) v.getParent())).getChildAt(1);
                    int childCount = viewGroup.getChildCount();
                    if (tag == 1) {
                        itemView1.setTag(0);
                        for (int i = 1; i < childCount; ++i) {
                            viewGroup.getChildAt(i).setVisibility(View.GONE);
                        }
                        ((ImageView) ((ViewGroup) v).getChildAt(1)).setImageResource(R.drawable.v2_expand3x);
                        ((TextView) ((ViewGroup) v).getChildAt(2)).setTextColor(getResources().getColor(R.color.second_text_color));
                    } else {
                        itemView1.setTag(1);
                        for (int i = 1; i < childCount; ++i) {
                            viewGroup.getChildAt(i).setVisibility(View.VISIBLE);
                        }
                        ((ImageView) ((ViewGroup) v).getChildAt(1)).setImageResource(R.drawable.v2_ewer3x);
                        ((TextView) ((ViewGroup) v).getChildAt(2)).setTextColor(getResources().getColor(R.color.main_color));
                    }
                }
            });
            itemList = config.getList();
            int itemCount = itemList.size();
            LinearLayout itemLayout;
            LinearLayout llName1, llName2, llName3;
            int index = 0;
            int charCount;
            for (int j = 0; j < Integer.MAX_VALUE; ++j) {
                charCount = 0;
                itemLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_filter_configuration, null);
                if (j > 0) {
                    itemLayout.setVisibility(View.GONE);
                }
                ((LinearLayout) layout.findViewById(R.id.llDetails)).addView(itemLayout);
                llName1 = (LinearLayout) itemLayout.getChildAt(0);
                llName2 = (LinearLayout) itemLayout.getChildAt(1);
                llName3 = (LinearLayout) itemLayout.getChildAt(2);
                if (index < itemCount) { //第一个item
                    charCount += itemList.get(index).getName().length();
                    ((TextView) llName1.getChildAt(1)).setText(itemList.get(index).getName());
                    if(EmptyUtils.isNotEmpty(typeName)){
                        if(typeName.contains(itemList.get(index).getName())){ ///选中状态
                            View ivCheck = ((ViewGroup) llName1).getChildAt(0);
                            TextView tv = (TextView) ((ViewGroup) llName1).getChildAt(1);
                            llName1.setBackgroundResource(R.drawable.back_filter_car_btn_2);
                            tv.setTextColor(getResources().getColor(R.color.main_color));
                            ivCheck.setVisibility(View.VISIBLE);
                        }
                    }

                    llName1.setTag(itemList.size());
                    llName1.setTag(R.id.tag, itemList.get(index));
                    llName1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onConfigClick(view);
                        }
                    });
                } else {
                    llName1.setVisibility(View.GONE);
                    llName2.setVisibility(View.GONE);
                    llName3.setVisibility(View.GONE);
                    break;
                }
                ++index;
                if (index < itemCount) {
                    charCount += itemList.get(index).getName().length();
                    ((TextView) llName2.getChildAt(1)).setText(itemList.get(index).getName());

                    if(EmptyUtils.isNotEmpty(typeName)){
                        if(typeName.contains(itemList.get(index).getName())){ ///选中状态
                            View ivCheck = ((ViewGroup) llName2).getChildAt(0);
                            TextView tv = (TextView) ((ViewGroup) llName2).getChildAt(1);
                            llName2.setBackgroundResource(R.drawable.back_filter_car_btn_2);
                            tv.setTextColor(getResources().getColor(R.color.main_color));
                            ivCheck.setVisibility(View.VISIBLE);
                        }
                    }

                    llName2.setTag(itemList.size());
                    llName2.setTag(R.id.tag, itemList.get(index));
                    llName2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onConfigClick(view);
                        }
                    });
                } else {
                    llName2.setVisibility(View.GONE);
                    llName3.setVisibility(View.GONE);
                    break;
                }
                ++index;
                if (index < itemCount) {
                    if (charCount + itemList.get(index).getName().length() > 14) {
                        llName3.setVisibility(View.GONE);
                        continue;
                    }
                    ((TextView) llName3.getChildAt(1)).setText(itemList.get(index).getName());

                    if(EmptyUtils.isNotEmpty(typeName)) {
                        if(typeName.contains(itemList.get(index).getName())){ ///选中状态
                            View ivCheck = ((ViewGroup) llName3).getChildAt(0);
                            TextView tv = (TextView) ((ViewGroup) llName3).getChildAt(1);
                            llName3.setBackgroundResource(R.drawable.back_filter_car_btn_2);
                            tv.setTextColor(getResources().getColor(R.color.main_color));
                            ivCheck.setVisibility(View.VISIBLE);
                        }
                    }

                    llName3.setTag(itemList.size());
                    llName3.setTag(R.id.tag, itemList.get(index));
                    llName3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onConfigClick(view);
                        }
                    });
                } else {
                    llName3.setVisibility(View.GONE);
                    break;
                }
                ++index;
            }
        }

    }

    //点击一个item
    private void onConfigClick(View view) {
        CarConfiguration.Configuration con = (CarConfiguration.Configuration) view.getTag(R.id.tag);
        String text = con.getId() + "_" + con.getName();
        View ivCheck = ((ViewGroup) view).getChildAt(0);
        TextView tv = (TextView) ((ViewGroup) view).getChildAt(1);
        ArrayList<String> list = extra_infos.get(con.getType());
        if (list == null) {
            list = new ArrayList<>();
            extra_infos.put(con.getType(), list);
        }
        if (list.contains(text)) {//已存在则移除
            list.remove(text);
            view.setBackgroundResource(R.drawable.back_filter_car_btn_1);
            tv.setTextColor(getResources().getColor(R.color.main_text_color));
            ivCheck.setVisibility(View.GONE);
        } else {//否则加入
            list.add(text);
            view.setBackgroundResource(R.drawable.back_filter_car_btn_2);
            tv.setTextColor(getResources().getColor(R.color.main_color));
            ivCheck.setVisibility(View.VISIBLE);
        }
        ViewGroup parent = (ViewGroup) view.getParent().getParent().getParent();
        ViewGroup itemView1 = (ViewGroup) parent.getChildAt(0);
        int size = (int) view.getTag();
        if (list.isEmpty() || list.size() == size) {//右上角显示选择的item的字符串
            ((TextView) itemView1.getChildAt(2)).setText("全部");
        } else {
            ((TextView) itemView1.getChildAt(2)).setText(getNamesByList(list));
        }
    }

    private String getNamesByList(ArrayList<String> list) {
        String ids = "";
        int size = list.size();
        for (int i = 0; i < size; ++i) {
            if (i < size - 1) {
                ids += list.get(i).split("_")[1] + ",";
            } else {
                ids += list.get(i).split("_")[1];
            }
        }
        return ids;
    }

    //过滤条件
    public class FilterCondition {
        int type;
        String text;
        String tag;
        int index;
    }

    //在LinearLayout中加入配置详情item
    private void resetFilterView(LinearLayout linearLayout) {
        linearLayout.removeAllViews();

        ArrayList<FilterCondition> list = getFilterConditions();//获取过滤条件对象集合
        int itemCount = list.size();//过滤条件的个数

        if(itemCount>0){
            tvDetail.setText(">");
        }

        LinearLayout itemLayout;
        LinearLayout llName1, llName2, llName3;
        int index = 0;//记录已经加入到头部的过滤条件个数
        int charCount;//记录过滤条件的总长度
        for (int j = 0; j < Integer.MAX_VALUE; ++j) {
            charCount = 0;
            itemLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_filter_conditicion, null);
            linearLayout.addView(itemLayout);//先添加一行（3个过滤条件）
            llName1 = (LinearLayout) itemLayout.getChildAt(0);
            llName2 = (LinearLayout) itemLayout.getChildAt(1);
            llName3 = (LinearLayout) itemLayout.getChildAt(2);
            if (index < itemCount) {//已经加入的个数小于总个数，为第一个item赋值
                charCount += list.get(index).text.length();
                ((TextView) llName1.getChildAt(0)).setText(list.get(index).text);
                llName1.setTag(R.id.tag, list.get(index));//设置tag,将item布局和过滤条件FilterCondition绑定
                llName1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onFilterCondition(view);
                    }
                });
                if (index == itemCount - 1) {//如果是最后一个，则是重置
                    ((ImageView) llName1.getChildAt(1)).setImageResource(R.drawable.chongzhi);
                }
            } else {
                llName1.setVisibility(View.GONE);
                llName2.setVisibility(View.GONE);
                llName3.setVisibility(View.GONE);
                break;
            }
            ++index;
            if (index < itemCount) {//已经加入的个数小于总个数，为第二个item赋值
                charCount += list.get(index).text.length();
                ((TextView) llName2.getChildAt(0)).setText(list.get(index).text);
                llName2.setTag(R.id.tag, list.get(index));
                llName2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onFilterCondition(view);
                    }
                });
                if (index == itemCount - 1) {
                    ((ImageView) llName2.getChildAt(1)).setImageResource(R.drawable.chongzhi);
                }
            } else {
                llName2.setVisibility(View.GONE);
                llName3.setVisibility(View.GONE);
                break;
            }
            ++index;
            if (index < itemCount) {//已经加入的个数小于总个数，为第三个item赋值
                if (charCount + list.get(index).text.length() > 9) {//如果前面两个item的长度加上第三个item的长度超过9个字，则不显示第三个，换行
                    llName3.setVisibility(View.GONE);
                    continue;
                }
                ((TextView) llName3.getChildAt(0)).setText(list.get(index).text);
                if (index == itemCount - 1) {
                    ((ImageView) llName3.getChildAt(1)).setImageResource(R.drawable.chongzhi);
                }
                llName3.setTag(R.id.tag, list.get(index));
                llName3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onFilterCondition(view);
                    }
                });
            } else {
                llName3.setVisibility(View.GONE);
                break;
            }
            ++index;
        }
    }

    private void onFilterCondition(View view) {
        if(mFinishEdit){
            FilterCondition condition = (FilterCondition) view.getTag(R.id.tag);
            switch (condition.type) {//通过type区分点击了不同的过滤条件（删除该过滤条件）
                case -1://重置
                    extra_infos.clear();
                    break;
                case 14:
                    ArrayList<String> list = extra_infos.get(condition.index);
                    list.remove(condition.tag + "_" + condition.text);
                    break;

            }
            resetFilterView(llTop);
        }

    }


    //将extra_infos字符串转换为FilterCondition对象
    private ArrayList<FilterCondition> getFilterConditions() {
        ArrayList<FilterCondition> list = new ArrayList<>();
        FilterCondition condition;

        Iterator<TreeMap.Entry<Integer, ArrayList<String>>> entries = extra_infos.entrySet().iterator();
        while (entries.hasNext()) {
            TreeMap.Entry<Integer, ArrayList<String>> entry = entries.next();
            int key = entry.getKey();
            ArrayList<String> infos = entry.getValue();
            if (infos != null && !infos.isEmpty()) {
                for (String str : infos) {
                    condition = new FilterCondition();
                    condition.type = 14;
                    String[] strs = str.split("_");
                    condition.tag = strs[0];
                    condition.text = strs[1];
                    condition.index = key;
                    list.add(condition);
                }
            }
        }

        if (list.size() > 0) {
            condition = new FilterCondition();
            condition.type = -1;
            condition.text = "重置";
            list.add(condition);
        }
        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(popupWindow != null){
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
}
