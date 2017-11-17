package com.wiserz.pbibi.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wiserz.pbibi.BaseApplication;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.RegisterAndLoginActivity;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.CarInfoBean;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.MorePopupWindow;
import com.wiserz.pbibi.view.RangeSeekBar;
import com.wiserz.pbibi.view.SelectStartEndView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/9 18:00.
 * QQ : 971060378
 * Used as : 汽车中心页面
 */
public class CarCenterFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener, OnLoadmoreListener, OnRefreshListener {

    public LocationClient mLocationClient;
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口，原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明
    public BDAbstractLocationListener myListener = new MyLocationListener();

    private TextView tvLocation;
    private LinearLayout ll_select;
    private LinearLayout ll_sort;
    private LinearLayout ll_price;
    private LinearLayout ll_filter;
    private LinearLayout ll_brand;


    private RecyclerView recyclerView;
    private int mPage;

    private String order_id;
    private String min_price;
    private String max_price;
    private String min_mileage;
    private String max_mileage;
    private String min_board_time;
    private String max_board_time;


    private SmartRefreshLayout smartRefreshLayout;
    private BaseRecyclerViewAdapter baseRecyclerViewAdapter;
    private int refresh_or_load;//0或1

    private int flag;
    private int price_id;
    private int new_or_old;
    private int car_source;
    private int car_vr;
    private int brand_id;
    private int series_id;

    private String brand_name;
    private String series_name;

    private static final int CAR_LIST_FOR_CAR_CENTER = 100;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x0001) {
                String city = (String) msg.obj;
                tvLocation.setText(city);
            } else if (msg.what == 0x0002) {
                tvLocation.setText("深圳");
                ToastUtils.showShort("定位失败，请检查网络是否通畅");
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_car_center;
    }

    @Override
    protected void initView(View view) {
        tvLocation = (TextView) view.findViewById(R.id.tvLocation);
        mLocationClient = new LocationClient(BaseApplication.getAppContext());//声明LocationClient类
        mLocationClient.registerLocationListener(myListener);//注册监听函数
        initLocation();
        mLocationClient.start();//开始定位

        view.findViewById(R.id.ivRight).setOnClickListener(this);
        ll_select = (LinearLayout) view.findViewById(R.id.ll_select);
        ll_sort = (LinearLayout) view.findViewById(R.id.ll_sort);
        ll_sort.setOnClickListener(this);
        ll_price = (LinearLayout) view.findViewById(R.id.ll_price);
        ll_price.setOnClickListener(this);
        ll_filter = (LinearLayout) view.findViewById(R.id.ll_filter);
        ll_filter.setOnClickListener(this);
        ll_brand = (LinearLayout) view.findViewById(R.id.ll_brand);
        ll_brand.setOnClickListener(this);

        view.findViewById(R.id.tvSearch).setOnClickListener(this);

        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadmoreListener(this);
        refresh_or_load = 0;

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mPage = 0;

        order_id = "0";
        price_id = 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivRight:
                //                gotoPager(LoanPlanFragment.class,null);//贷款方案

                //                gotoPager(ConcreteParameterFragment.class, null);//具体参数
                if (!CommonUtil.isHadLogin()) {
                    gotoPager(RegisterAndLoginActivity.class, null);
                    return;
                }
                showPostCarWindow();
                break;
            case R.id.ll_sort:
                flag = 0;
                resetHeaderView();
                View car_sort_view = View.inflate(mContext, R.layout.item_car_sort, null);
                carSelectPopupWindow(car_sort_view);
                break;
            case R.id.ll_price:
                flag = 1;
                resetHeaderView();
                View car_price_view = View.inflate(mContext, R.layout.item_car_price, null);
                carSelectPopupWindow(car_price_view);
                break;
            case R.id.ll_filter:
                flag = 2;
                resetHeaderView();
                View car_filter_view = View.inflate(mContext, R.layout.item_filter_view, null);
                carSelectPopupWindow(car_filter_view);
                break;
            case R.id.ll_brand:
                Bundle bundle = new Bundle();
                bundle.putString("fromClass", this.getClass().getName());
                gotoPager(SelectCarBrandFragment.class, bundle);
                break;
            case R.id.tvSearch:
                gotoPager(SearchFragment.class, null);
                break;
            default:
                break;
        }
    }

    public void onResume() {
        super.onResume();
        Object data1 = DataManager.getInstance().getData1();
        Object data2 = DataManager.getInstance().getData2();
        Object data3 = DataManager.getInstance().getData3();
        Object data4 = DataManager.getInstance().getData4();
        if (EmptyUtils.isNotEmpty(data1)
                && EmptyUtils.isNotEmpty(data2)
                && EmptyUtils.isNotEmpty(data3)
                && EmptyUtils.isNotEmpty(data4)) {
            if (data1 instanceof Integer && data2 instanceof String
                    && data3 instanceof Integer && data4 instanceof String) {
                brand_id = (int) data1;
                brand_name = (String) data2;
                series_id = (int) data3;
                series_name = (String) data4;

                TextView tvBrand = (TextView) getView().findViewById(R.id.tvBrand);
                if (brand_id == 0) {
                    tvBrand.setText("全部品牌");
                } else {
                    String text = brand_name;
                    if (series_id > 0 && !TextUtils.isEmpty(series_name)) {
                        text = text.substring(0, 2) + "..." + series_name.substring(series_name.length() - 2);
                    }
                    tvBrand.setText(text);
                }
            }
            getCarListDataFromNet();
            DataManager.getInstance().setData1(null);
            DataManager.getInstance().setData2(null);
            DataManager.getInstance().setData3(null);
            DataManager.getInstance().setData4(null);
        }
    }

    private void resetHeaderView() {
        if (getView() != null) {
            switch (flag) {
                case 0:
                    ((TextView) getView().findViewById(R.id.tv_sort)).setTextColor(getResources().getColor(R.color.main_color));
                    ((ImageView) getView().findViewById(R.id.iv_arrow_sort)).setImageResource(R.drawable.v2_ewer3x);
                    break;
                case 1:
                    ((TextView) getView().findViewById(R.id.tv_price)).setTextColor(getResources().getColor(R.color.main_color));
                    ((ImageView) getView().findViewById(R.id.iv_arrow_price)).setImageResource(R.drawable.v2_ewer3x);
                    break;
                case 2:
                    ((TextView) getView().findViewById(R.id.tv_filter)).setTextColor(getResources().getColor(R.color.main_color));
                    ((ImageView) getView().findViewById(R.id.iv_arrow_filter)).setImageResource(R.drawable.v2_ewer3x);
                    break;
                default:
                    break;
            }
        }
    }

    private void carSelectPopupWindow(final View view) {
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //外部是否可以点击
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(ll_select);

        //对返回按键的捕获并处理
        popupWindow.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setFocusable(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });

        //点击阴影部分退出
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        switch (flag) {
            case 0:
                final TextView tv_sort_default = (TextView) view.findViewById(R.id.tv_sort_default);
                final TextView tv_sort_most_low = (TextView) view.findViewById(R.id.tv_sort_most_low);
                final TextView tv_sort_most_top = (TextView) view.findViewById(R.id.tv_sort_most_top);
                final TextView tv_sort_most_short = (TextView) view.findViewById(R.id.tv_sort_most_short);
                resetTextColor(tv_sort_default, tv_sort_most_low, tv_sort_most_top, tv_sort_most_short);
                tv_sort_default.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPage = 0;
                        order_id = "0";
                        resetTextColor(tv_sort_default, tv_sort_most_low, tv_sort_most_top, tv_sort_most_short);
                        getCarListDataFromNet();
                        popupWindow.dismiss();
                    }
                });
                tv_sort_most_low.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPage = 0;
                        order_id = "1";
                        resetTextColor(tv_sort_default, tv_sort_most_low, tv_sort_most_top, tv_sort_most_short);
                        getCarListDataFromNet();
                        popupWindow.dismiss();
                    }
                });
                tv_sort_most_top.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPage = 0;
                        order_id = "2";
                        resetTextColor(tv_sort_default, tv_sort_most_low, tv_sort_most_top, tv_sort_most_short);
                        getCarListDataFromNet();
                        popupWindow.dismiss();
                    }
                });
                tv_sort_most_short.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPage = 0;
                        order_id = "3";
                        resetTextColor(tv_sort_default, tv_sort_most_low, tv_sort_most_top, tv_sort_most_short);
                        getCarListDataFromNet();
                        popupWindow.dismiss();
                    }
                });
                break;
            case 1:
                Button btn_sure = (Button) view.findViewById(R.id.btn_sure);
                final TextView tv0 = (TextView) view.findViewById(R.id.tv0);
                final TextView tv1 = (TextView) view.findViewById(R.id.tv1);
                final TextView tv2 = (TextView) view.findViewById(R.id.tv2);
                final TextView tv3 = (TextView) view.findViewById(R.id.tv3);
                final TextView tv4 = (TextView) view.findViewById(R.id.tv4);
                final TextView tv5 = (TextView) view.findViewById(R.id.tv5);
                final TextView tv6 = (TextView) view.findViewById(R.id.tv6);
                resetTextView(price_id, tv0, tv1, tv2, tv3, tv4, tv5, tv6);
                tv0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        price_id = 0;
                        resetTextView(price_id, tv0, tv1, tv2, tv3, tv4, tv5, tv6);
                        min_price = "0";
                        max_price = "";
                    }
                });
                tv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        price_id = 1;
                        resetTextView(price_id, tv0, tv1, tv2, tv3, tv4, tv5, tv6);
                        min_price = "0";
                        max_price = "15";
                    }
                });
                tv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        price_id = 2;
                        resetTextView(price_id, tv0, tv1, tv2, tv3, tv4, tv5, tv6);
                        min_price = "15";
                        max_price = "30";
                    }
                });
                tv3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        price_id = 3;
                        resetTextView(price_id, tv0, tv1, tv2, tv3, tv4, tv5, tv6);
                        min_price = "30";
                        max_price = "50";
                    }
                });
                tv4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        price_id = 4;
                        resetTextView(price_id, tv0, tv1, tv2, tv3, tv4, tv5, tv6);
                        min_price = "50";
                        max_price = "100";
                    }
                });
                tv5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        price_id = 5;
                        resetTextView(price_id, tv0, tv1, tv2, tv3, tv4, tv5, tv6);
                        min_price = "100";
                        max_price = "200";
                    }
                });
                RangeSeekBar<Integer> priceBar = (RangeSeekBar<Integer>) view.findViewById(R.id.priceBar);
                priceBar.setMaxLastText(200, getResources().getString(R.string.no_limit));
                priceBar.setMaxMinSpace(1);
                priceBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
                    @Override
                    public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                        price_id = -1;
                        resetTextView(price_id, tv0, tv1, tv2, tv3, tv4, tv5, tv6);
                        min_price = String.valueOf(minValue);
                        max_price = maxValue > 200 ? "" : String.valueOf(maxValue);
                    }
                });
                tv6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        price_id = 6;
                        resetTextView(price_id, tv0, tv1, tv2, tv3, tv4, tv5, tv6);
                        min_price = "200";
                        max_price = "";
                    }
                });
                btn_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPage = 0;
                        getCarListDataFromNet();
                        popupWindow.dismiss();
                    }
                });
                break;
            case 2:
                final TextView tvOldOrNewNoLimit = (TextView) view.findViewById(R.id.tvOldOrNewNoLimit);
                final TextView tvOnlyNew = (TextView) view.findViewById(R.id.tvOnlyNew);
                final TextView tvOnlySecondHand = (TextView) view.findViewById(R.id.tvOnlySecondHand);
                tvOldOrNewNoLimit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new_or_old = 0;
                        resetTextView(new_or_old, tvOldOrNewNoLimit, tvOnlyNew, tvOnlySecondHand);
                        showViewByNewOld(view, true);
                    }
                });
                tvOnlyNew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new_or_old = 1;
                        resetTextView(new_or_old, tvOldOrNewNoLimit, tvOnlyNew, tvOnlySecondHand);
                        showViewByNewOld(view, false);
                        ((ScrollView) view).scrollTo(0, 0);
                    }
                });

                tvOnlySecondHand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new_or_old = 2;
                        resetTextView(new_or_old, tvOldOrNewNoLimit, tvOnlyNew, tvOnlySecondHand);
                        showViewByNewOld(view, true);
                    }
                });
                resetTextView(new_or_old, tvOldOrNewNoLimit, tvOnlyNew, tvOnlySecondHand);
                showViewByNewOld(view, new_or_old != 1);

                final TextView tvSourceNoLimit = (TextView) view.findViewById(R.id.tvSourceNoLimit);
                final TextView tvProfileSource = (TextView) view.findViewById(R.id.tvProfileSource);
                final TextView tvShopSource = (TextView) view.findViewById(R.id.tvShopSource);
                tvSourceNoLimit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        car_source = 0;
                        resetTextView(car_source, tvSourceNoLimit, tvProfileSource, tvShopSource);
                    }
                });

                tvProfileSource.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        car_source = 1;
                        resetTextView(car_source, tvSourceNoLimit, tvProfileSource, tvShopSource);
                    }
                });

                tvShopSource.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        car_source = 2;
                        resetTextView(car_source, tvSourceNoLimit, tvProfileSource, tvShopSource);
                    }
                });
                resetTextView(car_source, tvSourceNoLimit, tvProfileSource, tvShopSource);

                final TextView tvVRNoLimit = (TextView) view.findViewById(R.id.tvVRNoLimit);
                final TextView tvOnlyVR = (TextView) view.findViewById(R.id.tvOnlyVR);
                tvVRNoLimit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        car_vr = 0;
                        resetTextView(car_vr, tvVRNoLimit, tvOnlyVR);
                    }
                });

                tvOnlyVR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        car_vr = 1;
                        resetTextView(car_vr, tvVRNoLimit, tvOnlyVR);
                    }
                });
                resetTextView(car_vr, tvVRNoLimit, tvOnlyVR);

                RangeSeekBar<Integer> distanceBar = (RangeSeekBar<Integer>) view.findViewById(R.id.distanceBar);
                distanceBar.setMaxLastText(12, getResources().getString(R.string.no_limit));
                distanceBar.setMaxMinSpace(1);
                distanceBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
                    @Override
                    public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                        min_mileage = String.valueOf(minValue);
                        max_mileage = maxValue > 12 ? "" : String.valueOf(maxValue);
                    }
                });

                RangeSeekBar<Integer> ageBar = (RangeSeekBar<Integer>) view.findViewById(R.id.ageBar);
                ageBar.setMaxLastText(8, getResources().getString(R.string.no_limit));
                ageBar.setMaxMinSpace(1);
                ageBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
                    @Override
                    public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                        min_board_time = String.valueOf(minValue);
                        max_board_time = maxValue > 8 ? "" : String.valueOf(maxValue);
                    }
                });

                view.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPage = 0;
                        getCarListDataFromNet();
                        popupWindow.dismiss();
                    }
                });
                break;
            default:
                break;
        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (getView() != null) {
                    switch (flag) {
                        case 0:
                            ((TextView) getView().findViewById(R.id.tv_sort)).setTextColor(getResources().getColor(R.color.main_text_color));
                            ((ImageView) getView().findViewById(R.id.iv_arrow_sort)).setImageResource(R.drawable.v2_expand3x);
                            break;
                        case 1:
                            ((TextView) getView().findViewById(R.id.tv_price)).setTextColor(getResources().getColor(R.color.main_text_color));
                            ((ImageView) getView().findViewById(R.id.iv_arrow_price)).setImageResource(R.drawable.v2_expand3x);
                            break;
                        case 2:
                            ((TextView) getView().findViewById(R.id.tv_filter)).setTextColor(getResources().getColor(R.color.main_text_color));
                            ((ImageView) getView().findViewById(R.id.iv_arrow_filter)).setImageResource(R.drawable.v2_expand3x);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    private void showViewByNewOld(View view, boolean isShow) {
        if (isShow) {
            view.findViewById(R.id.tvSource).setVisibility(View.VISIBLE);
            view.findViewById(R.id.llSource).setVisibility(View.VISIBLE);
            view.findViewById(R.id.llDistance).setVisibility(View.VISIBLE);
            view.findViewById(R.id.distanceBar).setVisibility(View.VISIBLE);
            view.findViewById(R.id.llAge).setVisibility(View.VISIBLE);
            view.findViewById(R.id.ageBar).setVisibility(View.VISIBLE);
            view.findViewById(R.id.fillView).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.tvSource).setVisibility(View.GONE);
            view.findViewById(R.id.llSource).setVisibility(View.GONE);
            view.findViewById(R.id.llDistance).setVisibility(View.GONE);
            view.findViewById(R.id.distanceBar).setVisibility(View.GONE);
            view.findViewById(R.id.llAge).setVisibility(View.GONE);
            view.findViewById(R.id.ageBar).setVisibility(View.GONE);
            view.findViewById(R.id.fillView).setVisibility(View.VISIBLE);
        }
    }

    private void resetTextView(int selectId, TextView... textViews) {
        int fromColor = getResources().getColor(R.color.third_text_color);
        for (TextView tv : textViews) {
            GradientDrawable background = (GradientDrawable) tv.getBackground();
            background.setStroke(SizeUtils.dp2px(1), fromColor);
            tv.setTextColor(getResources().getColor(R.color.main_text_color));
        }
        if (selectId >= 0) {
            int color = getResources().getColor(R.color.main_color);
            textViews[selectId].setTextColor(color);
            GradientDrawable background = (GradientDrawable) textViews[selectId].getBackground();
            background.setStroke(SizeUtils.dp2px(1), color);
        }
    }

    private void resetTextColor(TextView tv_sort_default, TextView tv_sort_most_low, TextView tv_sort_most_top, TextView tv_sort_most_short) {
        tv_sort_default.setTextColor(getResources().getColor(R.color.main_text_color));
        tv_sort_most_low.setTextColor(getResources().getColor(R.color.main_text_color));
        tv_sort_most_top.setTextColor(getResources().getColor(R.color.main_text_color));
        tv_sort_most_short.setTextColor(getResources().getColor(R.color.main_text_color));
        if (order_id.equals("0")) {
            tv_sort_default.setTextColor(getResources().getColor(R.color.main_color));
        } else if (order_id.equals("1")) {
            tv_sort_most_low.setTextColor(getResources().getColor(R.color.main_color));
        } else if (order_id.equals("2")) {
            tv_sort_most_top.setTextColor(getResources().getColor(R.color.main_color));
        } else if (order_id.equals("3")) {
            tv_sort_most_short.setTextColor(getResources().getColor(R.color.main_color));
        }
    }

    @Override
    protected void initData() {
        super.initData();
        getCarListDataFromNet();
    }

    private void getCarListDataFromNet() {
        OkHttpUtils.post()
                .url(Constant.getCarListUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.KEY_WORD, "")//关键字
                .addParams(Constant.ORDER_ID, order_id == null ? "" : order_id)//排序id
                .addParams(Constant.BRAND_ID, brand_id == 0 ? "" : String.valueOf(brand_id))//车品牌id
                .addParams(Constant.SERIES_ID, series_id == 0 ? "" : String.valueOf(series_id))//车系列id
                .addParams(Constant.PAGE, String.valueOf(mPage))
                .addParams(Constant.MIN_PRICE, min_price == null ? "" : min_price)//最低价格
                .addParams(Constant.MAX_PRICE, max_price == null ? "" : max_price)//最高价格
                .addParams(Constant.MIN_PRICE, min_price == null ? "" : min_price)//最低价格
                .addParams(Constant.MAX_PRICE, max_price == null ? "" : max_price)//最高价格
                .addParams(Constant.MIN_MILEAGE, min_mileage == null ? "" : min_mileage)//最低里程
                .addParams(Constant.MAX_MILEAGE, max_mileage == null ? "" : max_mileage)//最高里程
                .addParams(Constant.MIN_BOARD_TIME, min_board_time == null ? "" : min_board_time)//最短上牌时间
                .addParams(Constant.MAX_BOARD_TIME, max_board_time == null ? "" : max_board_time)//最长上牌时间
                .addParams(Constant.HAS_VR, car_vr == 0 ? "" : String.valueOf(car_vr))//是否有VR看车功能  1:是
                .addParams(Constant.OLD, new_or_old == 0 ? "" : String.valueOf(new_or_old))//是否新车二手车 1:新车 2 二手车
                .addParams(Constant.SOURCE, car_source == 0 ? "" : String.valueOf(car_source))//车辆来源 1:个人 2 商家
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
                                switch (refresh_or_load) {
                                    case 0:
                                        smartRefreshLayout.finishRefresh();
                                        handlerCarListData(jsonObjectData);
                                        break;
                                    case 1:
                                        smartRefreshLayout.finishLoadmore();
                                        handlerCarListMoreData(jsonObjectData);
                                        break;
                                    default:
                                        break;
                                }
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

    private void handlerCarListMoreData(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            ArrayList<CarInfoBean> carInfoBeanArrayList = getCarListData(jsonObjectData);
            if (EmptyUtils.isNotEmpty(carInfoBeanArrayList) && carInfoBeanArrayList.size() != 0) {
                baseRecyclerViewAdapter.addDatas(carInfoBeanArrayList);
            } else {
                ToastUtils.showShort("没有更多了");
            }
        }
    }

    private void handlerCarListData(JSONObject jsonObjectData) {
        ArrayList<CarInfoBean> carInfoBeanArrayList = getCarListData(jsonObjectData);
        if (EmptyUtils.isNotEmpty(carInfoBeanArrayList) && carInfoBeanArrayList.size() != 0) {
            baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, carInfoBeanArrayList, CAR_LIST_FOR_CAR_CENTER);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(baseRecyclerViewAdapter);
            baseRecyclerViewAdapter.setOnItemClickListener(this);
        }
    }

    private ArrayList<CarInfoBean> getCarListData(JSONObject jsonObjectData) {
        ArrayList<CarInfoBean> list = null;
        if (jsonObjectData == null) {
            return new ArrayList<>();
        } else {
            list = new ArrayList<>();
            JSONArray jsonArray = jsonObjectData.optJSONArray("car_list");
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectCarInfo = jsonArray.optJSONObject(i).optJSONObject("car_info");
                CarInfoBean carInfoBean = gson.fromJson(jsonObjectCarInfo.toString(), CarInfoBean.class);
                list.add(carInfoBean);
            }
        }
        return list;
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

    private void showPostCarWindow() {
        MorePopupWindow morePopupWindow = new MorePopupWindow(getActivity(), new MorePopupWindow.MorePopupWindowClickListener() {
            @Override
            public void onFirstBtnClicked() {
                //上传新车
                DataManager.getInstance().setObject(null);
                gotoPager(PostNewCarFragment.class, null);
            }

            @Override
            public void onSecondBtnClicked() {
                //上传二手车
                DataManager.getInstance().setObject(null);
                gotoPager(PostSecondHandCarFragment.class, null);
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
        }, MorePopupWindow.MORE_POPUP_WINDOW_TYPE.TYPE_SALE_CAR);
        morePopupWindow.initView();
        morePopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        ++mPage;
        refresh_or_load = 1;
        getCarListDataFromNet();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage = 0;
        refresh_or_load = 0;
        getCarListDataFromNet();
    }

    /**
     * 配置定位SDK参数
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //可选，7.2版本新增能力，如果您设置了这个接口，首次启动定位时，会先判断当前WiFi是否超出有效期，超出有效期的话，会先重新扫描WiFi，然后再定位

        mLocationClient.setLocOption(option);
    }

    private class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            //获取定位结果
            location.getTime();    //获取定位时间
            location.getLocationID();    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
            location.getLocType();    //获取定位类型
            location.getLatitude();    //获取纬度信息
            location.getLongitude();    //获取经度信息
            location.getRadius();    //获取定位精准度
            location.getAddrStr();    //获取地址信息
            location.getCountry();    //获取国家信息
            location.getCountryCode();    //获取国家码
            location.getCity();    //获取城市信息
            location.getCityCode();    //获取城市码
            location.getDistrict();    //获取区县信息
            location.getStreet();    //获取街道信息
            location.getStreetNumber();    //获取街道码
            location.getLocationDescribe();    //获取当前位置描述信息
            location.getPoiList();    //获取当前位置周边POI信息

            location.getBuildingID();    //室内精准定位下，获取楼宇ID
            location.getBuildingName();    //室内精准定位下，获取楼宇名称
            location.getFloor();    //室内精准定位下，获取当前位置所处的楼层信息

            if (location.getLocType() == BDLocation.TypeGpsLocation) {

                //当前为GPS定位结果，可获取以下信息
                location.getSpeed();    //获取当前速度，单位：公里每小时
                location.getSatelliteNumber();    //获取当前卫星数
                location.getAltitude();    //获取海拔高度信息，单位米
                location.getDirection();    //获取方向信息，单位度

                Message msg = Message.obtain();
                msg.what = 0x0001;
                msg.obj = location.getCity();
                handler.sendMessage(msg);
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                //当前为网络定位结果，可获取以下信息
                location.getOperators();    //获取运营商信息

                Message msg = Message.obtain();
                msg.what = 0x0001;
                msg.obj = location.getAddress().city;
                handler.sendMessage(msg);
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                //当前为网络定位结果

                Message msg = Message.obtain();
                msg.what = 0x0001;
                msg.obj = location.getAddress().city;
                handler.sendMessage(msg);
            } else if (location.getLocType() == BDLocation.TypeServerError) {

                //当前网络定位失败
                //可将定位唯一ID、IMEI、定位失败时间反馈至loc-bugs@baidu.com
                handler.sendEmptyMessage(0x0002);
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                //当前网络不通
                handler.sendEmptyMessage(0x0002);
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
                //可进一步参考onLocDiagnosticMessage中的错误返回码
                handler.sendEmptyMessage(0x0002);
            }
        }

        /**
         * 回调定位诊断信息，开发者可以根据相关信息解决定位遇到的一些问题
         * 自动回调，相同的diagnosticType只会回调一次
         *
         * @param locType           当前定位类型
         * @param diagnosticType    诊断类型（1~9）
         * @param diagnosticMessage 具体的诊断信息释义
         */
        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {

            if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_GPS) {

                //建议打开GPS

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_WIFI) {

                //建议打开wifi，不必连接，这样有助于提高网络定位精度！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_LOC_PERMISSION) {

                //定位权限受限，建议提示用户授予APP定位权限！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_NET) {

                //网络异常造成定位失败，建议用户确认网络状态是否异常！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CLOSE_FLYMODE) {

                //手机飞行模式造成定位失败，建议用户关闭飞行模式后再重试定位！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_INSERT_SIMCARD_OR_OPEN_WIFI) {

                //无法获取任何定位依据，建议用户打开wifi或者插入sim卡重试！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_OPEN_PHONE_LOC_SWITCH) {

                //无法获取有效定位依据，建议用户打开手机设置里的定位开关后重试！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_SERVER_FAIL) {

                //百度定位服务端定位失败
                //建议反馈location.getLocationID()和大体定位时间到loc-bugs@baidu.com

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_FAIL_UNKNOWN) {

                //无法获取有效定位依据，但无法确定具体原因
                //建议检查是否有安全软件屏蔽相关定位权限
                //或调用LocationClient.restart()重新启动后重试！
            }
        }
    }
}
