package com.wiserz.pbibi.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.RegisterAndLoginActivity;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.adapter.HeaderAndFooterWrapper;
import com.wiserz.pbibi.bean.CarConfiguration;
import com.wiserz.pbibi.bean.CarInfoBean;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.MorePopupWindow;
import com.wiserz.pbibi.view.RangeSeekBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/9 18:00.
 * QQ : 971060378
 * Used as : 汽车中心页面
 */
public class CarCenterFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener, OnLoadmoreListener, OnRefreshListener {

//    public LocationClient mLocationClient;
//    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口，原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明
//    public BDAbstractLocationListener myListener = new MyLocationListener();

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new GaoDeLocationListener();
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private TextView tvLocation;
    private LinearLayout ll_select;
    private LinearLayout ll_sort;
    private LinearLayout ll_price;
    private LinearLayout ll_filter;
    private LinearLayout ll_brand;

    private LinearLayout llTop;


    private RecyclerView recyclerView;
    private int mPage;//请求车列表的第几页信息

    private String order_id;

    private int price_id, temp_price_id;
    private String min_price = "0", temp_min_price;
    private String max_price, temp_max_price;


    private String min_mileage = "0";
    private String max_mileage;
    private String min_board_time = "0";
    private String max_board_time;
    private String min_pailiang = "0";
    private String max_pailiang;

    private ArrayList<CarConfiguration> mCarConfigurationList;
    private SmartRefreshLayout smartRefreshLayout;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private BaseRecyclerViewAdapter baseRecyclerViewAdapter;
    //   private int refresh_or_load;//0或1

    private int flag;
    private String new_or_old = "";
    private String car_source = "";
    private ArrayList<String> car_levels = new ArrayList<>();
    private ArrayList<String> seat_nums = new ArrayList<>();
    private ArrayList<String> car_colors = new ArrayList<>();
    private ArrayList<String> car_fueltypes = new ArrayList<>();
    private ArrayList<String> envirstandards = new ArrayList<>();

    private TreeMap<Integer, ArrayList<String>> extra_infos = new TreeMap<>();

    private String board_add = "";
    private String forward = "";

    private int car_vr;
    private int brand_id;
    private int series_id;

    private String brand_name;
    private String series_name;

    private TextView tvTotalCar;

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
//        mLocationClient = new LocationClient(BaseApplication.getAppContext());//声明LocationClient类
//        mLocationClient.registerLocationListener(myListener);//注册监听函数
//        initLocation();
//        mLocationClient.start();//开始定位

        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        initLocation();
        //启动定位
        mLocationClient.startLocation();

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


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mPage = 0;

        order_id = "0";
        price_id = 0;
    }

//    private void addTopLayout(){
//        smartRefreshLayout.addView();
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivRight: //上传
                if (!CommonUtil.isHadLogin()) {
                    gotoPager(RegisterAndLoginActivity.class, null);
                    return;
                }
                showPostCarWindow();
                break;
            case R.id.ll_sort:  //默认排序
                flag = 0;
                resetHeaderView();
                View car_sort_view = View.inflate(mContext, R.layout.item_car_sort, null);
                carSelectPopupWindow(car_sort_view);
                break;
            case R.id.ll_price:    //价格
                flag = 1;
                resetHeaderView();
                View car_price_view = View.inflate(mContext, R.layout.item_car_price, null);
                carSelectPopupWindow(car_price_view);
                break;
            case R.id.ll_filter:  //筛选
                flag = 2;
                if (mCarConfigurationList == null || mCarConfigurationList.isEmpty()) {
                    getCarExtraInfo();
                }
                resetHeaderView();
                View car_filter_view = View.inflate(mContext, R.layout.item_filter_view, null);
                tvTotalCar = (TextView) car_filter_view.findViewById(R.id.tvTotalCar);
                carSelectPopupWindow(car_filter_view);
                break;
            case R.id.ll_brand:  //品牌
                Bundle bundle = new Bundle();
                bundle.putString("fromClass", this.getClass().getName());
                gotoPager(SelectCarBrandFragment.class, bundle);
                break;
            case R.id.tvSearch:  //搜索
                gotoPager(SearchFragment.class, null);
                break;
            default:
                break;
        }
    }

    public void onResume() {
        super.onResume();
        //获取车的基本配置信息
        if (mCarConfigurationList == null || mCarConfigurationList.isEmpty()) {
            getCarExtraInfo();
        }


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
            mPage = 0;
            getCarListDataFromNet(2);
            DataManager.getInstance().setData1(null);
            DataManager.getInstance().setData2(null);
            DataManager.getInstance().setData3(null);
            DataManager.getInstance().setData4(null);
        }
    }

    //重置排序头部
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

    //顶部筛选
    private void carSelectPopupWindow(final View view) {
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //外部是否可以点击
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        if (flag < 2) {
            popupWindow.showAsDropDown(ll_select);
            popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        } else {
            popupWindow.showAsDropDown(getView().findViewById(R.id.topView));
            popupWindow.setAnimationStyle(R.style.PopupWindowAnimation2);
        }

        //对返回按键的捕获并处理
        popupWindow.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setFocusable(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    popupWindow.dismiss();
                    new_or_old = ""; //车况
                    car_source = ""; //车源
                    car_levels.clear(); //车辆级别
                    seat_nums.clear(); //座位数
                    car_colors.clear(); //颜色
                    car_fueltypes.clear(); //燃油类型
                    envirstandards.clear(); //环保标准
                    board_add = "";
                    forward = "";
                    min_mileage = "0";
                    max_mileage = "";
                    min_board_time = "0";
                    max_board_time = "";
                    min_pailiang = "0";
                    max_pailiang = "";
                }
                return false;
            }
        });

        //点击阴影部分退出
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                new_or_old = "";
                car_source = "";
                car_levels.clear();
                seat_nums.clear();
                car_colors.clear();
                car_fueltypes.clear();
                envirstandards.clear();
                board_add = "";
                forward = "";
                min_mileage = "0";
                max_mileage = "";
                min_board_time = "0";
                max_board_time = "";
                min_pailiang = "0";
                max_pailiang = "";
            }
        });

        switch (flag) {
            case 0:
                final TextView tv_sort_default = (TextView) view.findViewById(R.id.tv_sort_default);
                final TextView tv_sort_most_low = (TextView) view.findViewById(R.id.tv_sort_most_low);
                final TextView tv_sort_most_top = (TextView) view.findViewById(R.id.tv_sort_most_top);
                final TextView tv_sort_newest_publish = (TextView) view.findViewById(R.id.tv_sort_newest_publish);
                final TextView tv_sort = (TextView) getView().findViewById(R.id.tv_sort);
                resetTextColor(tv_sort_default, tv_sort_most_low, tv_sort_most_top, tv_sort_newest_publish);
                tv_sort_default.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPage = 0;
                        order_id = "0";
                        resetTextColor(tv_sort_default, tv_sort_most_low, tv_sort_most_top, tv_sort_newest_publish);
                        getCarListDataFromNet(2);
                        tv_sort.setText("默认排序");
                        popupWindow.dismiss();
                    }
                });
                tv_sort_most_low.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPage = 0;
                        order_id = "2";
                        resetTextColor(tv_sort_default, tv_sort_most_low, tv_sort_most_top, tv_sort_newest_publish);
                        getCarListDataFromNet(2);
                        tv_sort.setText("价格最低");
                        popupWindow.dismiss();
                    }
                });
                tv_sort_most_top.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPage = 0;
                        order_id = "3";
                        resetTextColor(tv_sort_default, tv_sort_most_low, tv_sort_most_top, tv_sort_newest_publish);
                        getCarListDataFromNet(2);
                        tv_sort.setText("价格最高");
                        popupWindow.dismiss();
                    }
                });
                tv_sort_newest_publish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPage = 0;
                        order_id = "1";
                        resetTextColor(tv_sort_default, tv_sort_most_low, tv_sort_most_top, tv_sort_newest_publish);
                        getCarListDataFromNet(2);
                        tv_sort.setText("最新发布");
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
                final TextView tvPrice = (TextView) view.findViewById(R.id.tvPrice);
                temp_price_id = price_id;
                temp_min_price = min_price;
                temp_max_price = max_price;
                resetTextView(price_id, tv0, tv1, tv2, tv3, tv4, tv5, tv6);
                resetPrice(tvPrice);
                final RangeSeekBar<Integer> priceBar = (RangeSeekBar<Integer>) view.findViewById(R.id.priceBar);
                priceBar.setMaxLastText(200, getResources().getString(R.string.no_limit));
                priceBar.setMaxMinSpace(1);
                ArrayList<String> items = new ArrayList<>();
                items.add("0");
                items.add("50");
                items.add("100");
                items.add("150");
                items.add("200");
                items.add(getResources().getString(R.string.no_limit));
                priceBar.setNumberItemStr(items);
                priceBar.setSelectedMinValue(TextUtils.isEmpty(temp_min_price) ? 0 : Integer.parseInt(temp_min_price));
                priceBar.setSelectedMaxValue(TextUtils.isEmpty(temp_max_price) ? 250 : Integer.parseInt(temp_max_price));
                priceBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
                    @Override
                    public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                        temp_price_id = -1;
                        resetTextView(temp_price_id, tv0, tv1, tv2, tv3, tv4, tv5, tv6);
                        temp_min_price = String.valueOf(minValue);
                        temp_max_price = maxValue > 200 ? "" : String.valueOf(maxValue);
                        resetPrice(tvPrice);
                    }
                });
                tv0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        temp_price_id = 0;
                        resetTextView(temp_price_id, tv0, tv1, tv2, tv3, tv4, tv5, tv6);
                        temp_min_price = "0";
                        temp_max_price = "";
                        priceBar.setSelectedMinValue(0);
                        priceBar.setSelectedMaxValue(250);
                        resetPrice(tvPrice);
                    }
                });
                tv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        temp_price_id = 1;
                        resetTextView(temp_price_id, tv0, tv1, tv2, tv3, tv4, tv5, tv6);
                        temp_min_price = "0";
                        temp_max_price = "15";
                        priceBar.setSelectedMinValue(0);
                        priceBar.setSelectedMaxValue(15);
                        resetPrice(tvPrice);
                    }
                });
                tv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        temp_price_id = 2;
                        resetTextView(temp_price_id, tv0, tv1, tv2, tv3, tv4, tv5, tv6);
                        temp_min_price = "15";
                        temp_max_price = "30";
                        priceBar.setSelectedMinValue(15);
                        priceBar.setSelectedMaxValue(30);
                        resetPrice(tvPrice);
                    }
                });
                tv3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        temp_price_id = 3;
                        resetTextView(temp_price_id, tv0, tv1, tv2, tv3, tv4, tv5, tv6);
                        temp_min_price = "30";
                        temp_max_price = "50";
                        priceBar.setSelectedMinValue(30);
                        priceBar.setSelectedMaxValue(50);
                        resetPrice(tvPrice);
                    }
                });
                tv4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        temp_price_id = 4;
                        resetTextView(temp_price_id, tv0, tv1, tv2, tv3, tv4, tv5, tv6);
                        temp_min_price = "50";
                        temp_max_price = "100";
                        priceBar.setSelectedMinValue(50);
                        priceBar.setSelectedMaxValue(100);
                        resetPrice(tvPrice);
                    }
                });
                tv5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        temp_price_id = 5;
                        resetTextView(temp_price_id, tv0, tv1, tv2, tv3, tv4, tv5, tv6);
                        temp_min_price = "100";
                        temp_max_price = "200";
                        priceBar.setSelectedMinValue(100);
                        priceBar.setSelectedMaxValue(200);
                        resetPrice(tvPrice);
                    }
                });

                tv6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        temp_price_id = 6;
                        resetTextView(temp_price_id, tv0, tv1, tv2, tv3, tv4, tv5, tv6);
                        temp_min_price = "200";
                        temp_max_price = "";
                        priceBar.setSelectedMinValue(200);
                        priceBar.setSelectedMaxValue(250);
                        resetPrice(tvPrice);
                    }
                });
                btn_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        price_id = temp_price_id;
                        min_price = temp_min_price;
                        max_price = temp_max_price;
                        mPage = 0;
                        getCarListDataFromNet(2);
                        popupWindow.dismiss();
                    }
                });
                break;
            case 2: //筛选
                final ArrayList<View> viewList = new ArrayList<>();
                final ViewPager viewPager = (ViewPager) view.findViewById(R.id.customViewPager);
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                viewList.add(inflater.inflate(R.layout.layout_car_filter_1, null)); //基本信息界面
                viewList.add(inflater.inflate(R.layout.item_post_car_detail_msg, null)); //详细配置页面
                resetBasicMsg(viewList.get(0));
                resetCarConfigurations(viewList.get(1), mCarConfigurationList);
                view.findViewById(R.id.rl).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                view.findViewById(R.id.tvReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getCarListDataFromNet(1);
                        resetBasicMsg(viewList.get(0));
                        resetCarConfigurations(viewList.get(1), mCarConfigurationList);
                    }
                });
                view.findViewById(R.id.tvTotalCar).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPage = 0;
                        getCarListDataFromNet(2);
                        popupWindow.dismiss();
                    }
                });


                view.findViewById(R.id.tvBasicMsg).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(0);
                        ((TextView) view.findViewById(R.id.tvBasicMsg)).setTextColor(getResources().getColor(R.color.main_color));
                        view.findViewById(R.id.line1).setVisibility(View.VISIBLE);
                        ((TextView) view.findViewById(R.id.tvDetailMsg)).setTextColor(getResources().getColor(R.color.main_text_color));
                        view.findViewById(R.id.line2).setVisibility(View.GONE);
                    }
                });

                view.findViewById(R.id.tvDetailMsg).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(1);
                        ((TextView) view.findViewById(R.id.tvBasicMsg)).setTextColor(getResources().getColor(R.color.main_text_color));
                        view.findViewById(R.id.line1).setVisibility(View.GONE);
                        ((TextView) view.findViewById(R.id.tvDetailMsg)).setTextColor(getResources().getColor(R.color.main_color));
                        view.findViewById(R.id.line2).setVisibility(View.VISIBLE);
                    }
                });

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (position == 0) {
                            view.findViewById(R.id.tvBasicMsg).performClick();
                        } else {
                            view.findViewById(R.id.tvDetailMsg).performClick();
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                viewPager.setAdapter(new PagerAdapter() {

                    @Override
                    public int getCount() {
                        // TODO Auto-generated method stub
                        return viewList.size();
                    }

                    @Override
                    public boolean isViewFromObject(View arg0, Object arg1) {
                        // TODO Auto-generated method stub
                        return arg0 == arg1;
                    }

                    @Override
                    public Object instantiateItem(ViewGroup container, int position) {
                        container.addView(viewList.get(position));
                        return viewList.get(position);
                    }

                    @Override
                    public void destroyItem(ViewGroup container, int position, Object object) {
                        // 删除
                        container.removeView(viewList.get(position));
                    }
                });
                break;
            default:
                break;
        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tvTotalCar = null;
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

    private void resetPrice(TextView tvPrice) {
        if (!temp_min_price.equals("0") || !TextUtils.isEmpty(temp_max_price)) {
            if (TextUtils.isEmpty(temp_max_price)) {
                tvPrice.setText(temp_min_price + "万以上");
            } else {
                tvPrice.setText(temp_min_price + "-" + temp_max_price + "万");
            }
        } else {
            tvPrice.setText("不限");
        }
    }

    //基本信息
    private void resetBasicMsg(View view) {
        new_or_old = "";
        car_source = "";
        car_levels.clear();
        seat_nums.clear();
        car_colors.clear();
        car_fueltypes.clear();
        envirstandards.clear();
        board_add = "";
        forward = "";
        min_mileage = "0";
        max_mileage = "";
        min_board_time = "0";
        max_board_time = "";
        min_pailiang = "0";
        max_pailiang = "";

        getCarListDataFromNet(1);

        final LinearLayout llRoot = (LinearLayout) view.findViewById(R.id.llRoot);
        int childCount = llRoot.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            if (i == 6) {//里程
                final TextView tvDistanceNum = (TextView) view.findViewById(R.id.tvDistanceNum);
                tvDistanceNum.setText(getResources().getString(R.string.no_limit));
                RangeSeekBar<Integer> distanceBar = (RangeSeekBar<Integer>) view.findViewById(R.id.distanceBar);
                distanceBar.setMaxLastText(12, getResources().getString(R.string.no_limit));
                distanceBar.setMaxMinSpace(1);
                ArrayList<String> items = new ArrayList<>();
                items.add("0");
                items.add("3");
                items.add("6");
                items.add("9");
                items.add("12");
                items.add(getResources().getString(R.string.no_limit));
                distanceBar.setNumberItemStr(items);
                distanceBar.reset();
                distanceBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
                    @Override
                    public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                        min_mileage = String.valueOf(minValue);
                        max_mileage = maxValue > 12 ? "" : String.valueOf(maxValue);
                        if (!min_mileage.equals("0") || !TextUtils.isEmpty(max_mileage)) {
                            if (TextUtils.isEmpty(max_mileage)) {
                                tvDistanceNum.setText(min_mileage + "万公里以上");
                            } else {
                                tvDistanceNum.setText(min_mileage + "-" + max_mileage + "万公里");
                            }
                        } else {
                            tvDistanceNum.setText(getResources().getString(R.string.no_limit));
                        }
                        getCarListDataFromNet(1);
                    }
                });
                continue;
            } else if (i == 7) {//车龄
                final TextView tvAgeNum = (TextView) view.findViewById(R.id.tvAgeNum);
                tvAgeNum.setText(getResources().getString(R.string.no_limit));
                RangeSeekBar<Integer> ageBar = (RangeSeekBar<Integer>) view.findViewById(R.id.ageBar);
                ageBar.setMaxLastText(8, getResources().getString(R.string.no_limit));
                ageBar.setMaxMinSpace(1);
                ageBar.reset();
                ArrayList<String> items = new ArrayList<>();
                items.add("0");
                items.add("2");
                items.add("4");
                items.add("6");
                items.add("8");
                items.add(getResources().getString(R.string.no_limit));
                ageBar.setNumberItemStr(items);
                ageBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
                    @Override
                    public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                        min_board_time = String.valueOf(minValue);
                        max_board_time = maxValue > 8 ? "" : String.valueOf(maxValue);
                        if (!min_board_time.equals("0") || !TextUtils.isEmpty(max_board_time)) {
                            if (TextUtils.isEmpty(max_board_time)) {
                                tvAgeNum.setText(min_board_time + "年以上");
                            } else {
                                tvAgeNum.setText(min_board_time + "-" + max_board_time + "年");
                            }
                        } else {
                            tvAgeNum.setText(getResources().getString(R.string.no_limit));
                        }
                        getCarListDataFromNet(1);
                    }
                });
                continue;
            } else if (i == 8) {//排量
                final TextView tvPailiangNum = (TextView) view.findViewById(R.id.tvPailiangNum);
                tvPailiangNum.setText(getResources().getString(R.string.no_limit));
                RangeSeekBar<Integer> pailiangBar = (RangeSeekBar<Integer>) view.findViewById(R.id.pailiangBar);
                pailiangBar.setMaxLastText(4, getResources().getString(R.string.no_limit));
                pailiangBar.setMaxMinSpace(1);
                pailiangBar.reset();
                ArrayList<String> items = new ArrayList<>();
                items.add("0");
                items.add("1");
                items.add("2");
                items.add("3");
                items.add("4");
                items.add(getResources().getString(R.string.no_limit));
                pailiangBar.setNumberItemStr(items);
                pailiangBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
                    @Override
                    public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                        min_pailiang = String.valueOf(minValue);
                        max_pailiang = maxValue > 4 ? "" : String.valueOf(maxValue);
                        if (!min_pailiang.equals("0") || !TextUtils.isEmpty(max_pailiang)) {
                            if (TextUtils.isEmpty(max_pailiang)) {
                                tvPailiangNum.setText(min_pailiang + "L以上");
                            } else {
                                tvPailiangNum.setText(min_pailiang + "-" + max_pailiang + "L");
                            }
                        } else {
                            tvPailiangNum.setText(getResources().getString(R.string.no_limit));
                        }
                        getCarListDataFromNet(1);
                    }
                });
                continue;
            }
            ViewGroup itemView = (ViewGroup) llRoot.getChildAt(i);
            int count = itemView.getChildCount();
            if (i == 2 || i == 3 || i == 5 || i == 9 || i == 11) {//这几个是可以展开全部显示的
                ViewGroup viewGroup = (ViewGroup) itemView.getChildAt(0);
                ((ImageView) viewGroup.getChildAt(1)).setImageResource(R.drawable.v2_expand3x);
                ((TextView) viewGroup.getChildAt(2)).setText("全部");
                ((TextView) viewGroup.getChildAt(2)).setTextColor(getResources().getColor(R.color.second_text_color));
                if (i == 2) {
                    viewGroup.setTag(1);
                    ((ImageView) viewGroup.getChildAt(1)).setImageResource(R.drawable.v2_ewer3x);
                    ((TextView) viewGroup.getChildAt(2)).setTextColor(getResources().getColor(R.color.main_color));
                } else {
                    viewGroup.setTag(0);
                }
                viewGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tag = (int) v.getTag();
                        ViewGroup viewGroup = (ViewGroup) v;
                        ViewGroup parent = (ViewGroup) v.getParent();
                        int childCount = parent.getChildCount();
                        if (tag == 1) {
                            v.setTag(0);
                            ((ImageView) viewGroup.getChildAt(1)).setImageResource(R.drawable.v2_expand3x);
                            ((TextView) viewGroup.getChildAt(2)).setTextColor(getResources().getColor(R.color.second_text_color));
                            for (int i = 2; i < childCount; ++i) {
                                parent.getChildAt(i).setVisibility(View.GONE);
                            }
                        } else {
                            v.setTag(1);
                            ((ImageView) viewGroup.getChildAt(1)).setImageResource(R.drawable.v2_ewer3x);
                            ((TextView) viewGroup.getChildAt(2)).setTextColor(getResources().getColor(R.color.main_color));
                            for (int i = 2; i < childCount; ++i) {
                                parent.getChildAt(i).setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

            }
            for (int j = 1; j < count; ++j) {
                ViewGroup itemView2 = (ViewGroup) itemView.getChildAt(j);
                if (i == 2) {
                    itemView2.setVisibility(View.VISIBLE);
                } else {
                    if (j > 1) {
                        itemView2.setVisibility(View.GONE);
                    }
                }
                int count2 = itemView2.getChildCount();
                for (int k = 0; k < count2; ++k) {
                    ViewGroup itemView3 = (ViewGroup) itemView2.getChildAt(k);
                    itemView3.setTag(R.id.tag, i);
                    switch (i) {
                        case 0:
                        case 1:
                        case 3:
                        case 4:
                        case 5:
                        case 9:
                        case 10:
                        case 11:
                            itemView3.setBackgroundResource(R.drawable.back_filter_car_btn_1);
                            if (i != 5) {
                                itemView3.getChildAt(0).setVisibility(View.GONE);
                            }
                            ((TextView) itemView3.getChildAt(1)).setTextColor(getResources().getColor(R.color.main_text_color));
                            break;
                        case 2:
                            ImageView ivCarLevel = (ImageView) itemView3.getChildAt(0);
                            String tag = (String) itemView3.getTag();
                            if (tag.equals("6")) {
                                ivCarLevel.setImageResource(R.drawable.jiaoche_hei);
                            } else if (tag.equals("9")) {
                                ivCarLevel.setImageResource(R.drawable.paoche_hei);
                            } else if (tag.equals("13")) {
                                ivCarLevel.setImageResource(R.drawable.changpeng_hei);
                            } else if (tag.equals("8")) {
                                ivCarLevel.setImageResource(R.drawable.suv_hei);
                            } else if (tag.equals("11")) {
                                ivCarLevel.setImageResource(R.drawable.pika_hei);
                            } else if (tag.equals("7")) {
                                ivCarLevel.setImageResource(R.drawable.mpv_hei);
                            }
                            ((TextView) itemView3.getChildAt(1)).setTextColor(getResources().getColor(R.color.main_text_color));
                            break;
                    }
                    itemView3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String tag = (String) view.getTag();
                            int index = (int) view.getTag(R.id.tag);
                            ViewGroup parent;
                            switch (index) {
                                case 0:
                                    if (new_or_old.equals(tag)) {
                                        new_or_old = "";
                                        view.setBackgroundResource(R.drawable.back_filter_car_btn_1);
                                        ((ViewGroup) view).getChildAt(0).setVisibility(View.GONE);
                                        ((TextView) ((ViewGroup) view).getChildAt(1)).setTextColor(getResources().getColor(R.color.main_text_color));
                                    } else {
                                        new_or_old = tag;
                                        parent = (ViewGroup) view.getParent();
                                        for (int i = 0; i < parent.getChildCount(); ++i) {
                                            parent.getChildAt(i).setBackgroundResource(R.drawable.back_filter_car_btn_1);
                                            ((ViewGroup) parent.getChildAt(i)).getChildAt(0).setVisibility(View.GONE);
                                            ((TextView) ((ViewGroup) parent.getChildAt(i)).getChildAt(1)).setTextColor(getResources().getColor(R.color.main_text_color));
                                        }
                                        view.setBackgroundResource(R.drawable.back_filter_car_btn_2);
                                        ((ViewGroup) view).getChildAt(0).setVisibility(View.VISIBLE);
                                        ((TextView) ((ViewGroup) view).getChildAt(1)).setTextColor(getResources().getColor(R.color.main_color));
                                    }
                                    if (new_or_old.equals("1")) {
                                        llRoot.getChildAt(4).setVisibility(View.GONE);
                                        llRoot.getChildAt(6).setVisibility(View.GONE);
                                        llRoot.getChildAt(7).setVisibility(View.GONE);
                                    } else {
                                        llRoot.getChildAt(4).setVisibility(View.VISIBLE);
                                        llRoot.getChildAt(6).setVisibility(View.VISIBLE);
                                        llRoot.getChildAt(7).setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case 1:
                                    if (car_source.equals(tag)) {
                                        car_source = "";
                                        view.setBackgroundResource(R.drawable.back_filter_car_btn_1);
                                        ((ViewGroup) view).getChildAt(0).setVisibility(View.GONE);
                                        ((TextView) ((ViewGroup) view).getChildAt(1)).setTextColor(getResources().getColor(R.color.main_text_color));
                                    } else {
                                        car_source = tag;
                                        parent = (ViewGroup) view.getParent();
                                        for (int i = 0; i < parent.getChildCount(); ++i) {
                                            parent.getChildAt(i).setBackgroundResource(R.drawable.back_filter_car_btn_1);
                                            ((ViewGroup) parent.getChildAt(i)).getChildAt(0).setVisibility(View.GONE);
                                            ((TextView) ((ViewGroup) parent.getChildAt(i)).getChildAt(1)).setTextColor(getResources().getColor(R.color.main_text_color));
                                        }
                                        view.setBackgroundResource(R.drawable.back_filter_car_btn_2);
                                        ((ViewGroup) view).getChildAt(0).setVisibility(View.VISIBLE);
                                        ((TextView) ((ViewGroup) view).getChildAt(1)).setTextColor(getResources().getColor(R.color.main_color));
                                    }
                                    break;
                                case 2:
                                    ImageView ivCarLevel = (ImageView) ((ViewGroup) view).getChildAt(0);
                                    TextView tvName = (TextView) ((ViewGroup) view).getChildAt(1);
                                    String text = tag + "_" + tvName.getText().toString();
                                    if (car_levels.contains(text)) {
                                        car_levels.remove(text);
                                        if (tag.equals("6")) {
                                            ivCarLevel.setImageResource(R.drawable.jiaoche_hei);
                                        } else if (tag.equals("9")) {
                                            ivCarLevel.setImageResource(R.drawable.paoche_hei);
                                        } else if (tag.equals("13")) {
                                            ivCarLevel.setImageResource(R.drawable.changpeng_hei);
                                        } else if (tag.equals("8")) {
                                            ivCarLevel.setImageResource(R.drawable.suv_hei);
                                        } else if (tag.equals("11")) {
                                            ivCarLevel.setImageResource(R.drawable.pika_hei);
                                        } else if (tag.equals("7")) {
                                            ivCarLevel.setImageResource(R.drawable.mpv_hei);
                                        }
                                        tvName.setTextColor(getResources().getColor(R.color.main_text_color));
                                    } else {
                                        car_levels.add(text);
                                        if (tag.equals("6")) {
                                            ivCarLevel.setImageResource(R.drawable.jiaoche_huang);
                                        } else if (tag.equals("9")) {
                                            ivCarLevel.setImageResource(R.drawable.paoche_huang);
                                        } else if (tag.equals("13")) {
                                            ivCarLevel.setImageResource(R.drawable.changpeng_huang);
                                        } else if (tag.equals("8")) {
                                            ivCarLevel.setImageResource(R.drawable.suv_huang);
                                        } else if (tag.equals("11")) {
                                            ivCarLevel.setImageResource(R.drawable.pika_huang);
                                        } else if (tag.equals("7")) {
                                            ivCarLevel.setImageResource(R.drawable.mpv_huang);
                                        }
                                        tvName.setTextColor(getResources().getColor(R.color.main_color));
                                    }
                                    TextView tv = (TextView) ((ViewGroup) ((ViewGroup) llRoot.getChildAt(index)).getChildAt(0)).getChildAt(2);
                                    int size = car_levels.size();
                                    if (size == 0 || size == 6) {
                                        tv.setText("全部");
                                    } else {
                                        tv.setText(getNamesByList(car_levels));
                                    }
                                    break;
                                case 3:
                                    View ivCheck = ((ViewGroup) view).getChildAt(0);
                                    tvName = (TextView) ((ViewGroup) view).getChildAt(1);
                                    text = tag + "_" + tvName.getText().toString();
                                    if (seat_nums.contains(text)) {
                                        seat_nums.remove(text);
                                        view.setBackgroundResource(R.drawable.back_filter_car_btn_1);
                                        tvName.setTextColor(getResources().getColor(R.color.main_text_color));
                                        ivCheck.setVisibility(View.GONE);
                                    } else {
                                        seat_nums.add(text);
                                        view.setBackgroundResource(R.drawable.back_filter_car_btn_2);
                                        tvName.setTextColor(getResources().getColor(R.color.main_color));
                                        ivCheck.setVisibility(View.VISIBLE);
                                    }
                                    tv = (TextView) ((ViewGroup) ((ViewGroup) llRoot.getChildAt(index)).getChildAt(0)).getChildAt(2);
                                    size = seat_nums.size();
                                    if (size == 0 || size == 5) {
                                        tv.setText("全部");
                                    } else {
                                        tv.setText(getNamesByList(seat_nums));
                                    }
                                    break;
                                case 4:
                                    if (board_add.equals(tag)) {
                                        board_add = "";
                                        view.setBackgroundResource(R.drawable.back_filter_car_btn_1);
                                        ((ViewGroup) view).getChildAt(0).setVisibility(View.GONE);
                                        ((TextView) ((ViewGroup) view).getChildAt(1)).setTextColor(getResources().getColor(R.color.main_text_color));
                                    } else {
                                        board_add = tag;
                                        parent = (ViewGroup) view.getParent();
                                        for (int i = 0; i < parent.getChildCount(); ++i) {
                                            parent.getChildAt(i).setBackgroundResource(R.drawable.back_filter_car_btn_1);
                                            ((ViewGroup) parent.getChildAt(i)).getChildAt(0).setVisibility(View.GONE);
                                            ((TextView) ((ViewGroup) parent.getChildAt(i)).getChildAt(1)).setTextColor(getResources().getColor(R.color.main_text_color));
                                        }
                                        view.setBackgroundResource(R.drawable.back_filter_car_btn_2);
                                        ((ViewGroup) view).getChildAt(0).setVisibility(View.VISIBLE);
                                        ((TextView) ((ViewGroup) view).getChildAt(1)).setTextColor(getResources().getColor(R.color.main_color));
                                    }
                                    break;
                                case 5:
                                    tvName = (TextView) ((ViewGroup) view).getChildAt(1);
                                    text = tag + "_" + tvName.getText().toString();
                                    if (car_colors.contains(text)) {
                                        car_colors.remove(text);
                                        view.setBackgroundResource(R.drawable.back_filter_car_btn_1);
                                        tvName.setTextColor(getResources().getColor(R.color.main_text_color));
                                    } else {
                                        car_colors.add(text);
                                        view.setBackgroundResource(R.drawable.back_filter_car_btn_2);
                                        tvName.setTextColor(getResources().getColor(R.color.main_color));
                                    }
                                    tv = (TextView) ((ViewGroup) ((ViewGroup) llRoot.getChildAt(index)).getChildAt(0)).getChildAt(2);
                                    size = car_colors.size();
                                    if (size == 0 || size == 14) {
                                        tv.setText("全部");
                                    } else {
                                        tv.setText(getNamesByList(car_colors));
                                    }
                                    break;
                                case 9:
                                    ivCheck = ((ViewGroup) view).getChildAt(0);
                                    tvName = (TextView) ((ViewGroup) view).getChildAt(1);
                                    text = tag + "_" + tvName.getText().toString();
                                    if (car_fueltypes.contains(text)) {
                                        car_fueltypes.remove(text);
                                        view.setBackgroundResource(R.drawable.back_filter_car_btn_1);
                                        tvName.setTextColor(getResources().getColor(R.color.main_text_color));
                                        ivCheck.setVisibility(View.GONE);
                                    } else {
                                        car_fueltypes.add(text);
                                        view.setBackgroundResource(R.drawable.back_filter_car_btn_2);
                                        tvName.setTextColor(getResources().getColor(R.color.main_color));
                                        ivCheck.setVisibility(View.VISIBLE);
                                    }
                                    tv = (TextView) ((ViewGroup) ((ViewGroup) llRoot.getChildAt(index)).getChildAt(0)).getChildAt(2);
                                    size = car_fueltypes.size();
                                    if (size == 0 || size == 4) {
                                        tv.setText("全部");
                                    } else {
                                        tv.setText(getNamesByList(car_fueltypes));
                                    }
                                    break;
                                case 10:
                                    if (forward.equals(tag)) {
                                        forward = "";
                                        view.setBackgroundResource(R.drawable.back_filter_car_btn_1);
                                        ((ViewGroup) view).getChildAt(0).setVisibility(View.GONE);
                                        ((TextView) ((ViewGroup) view).getChildAt(1)).setTextColor(getResources().getColor(R.color.main_text_color));
                                    } else {
                                        forward = tag;
                                        parent = (ViewGroup) view.getParent();
                                        for (int i = 0; i < parent.getChildCount(); ++i) {
                                            parent.getChildAt(i).setBackgroundResource(R.drawable.back_filter_car_btn_1);
                                            ((ViewGroup) parent.getChildAt(i)).getChildAt(0).setVisibility(View.GONE);
                                            ((TextView) ((ViewGroup) parent.getChildAt(i)).getChildAt(1)).setTextColor(getResources().getColor(R.color.main_text_color));
                                        }
                                        view.setBackgroundResource(R.drawable.back_filter_car_btn_2);
                                        ((ViewGroup) view).getChildAt(0).setVisibility(View.VISIBLE);
                                        ((TextView) ((ViewGroup) view).getChildAt(1)).setTextColor(getResources().getColor(R.color.main_color));
                                    }
                                    break;
                                case 11:
                                    ivCheck = ((ViewGroup) view).getChildAt(0);
                                    tvName = (TextView) ((ViewGroup) view).getChildAt(1);
                                    text = tag + "_" + tvName.getText().toString();
                                    if (envirstandards.contains(text)) {
                                        envirstandards.remove(text);
                                        view.setBackgroundResource(R.drawable.back_filter_car_btn_1);
                                        tvName.setTextColor(getResources().getColor(R.color.main_text_color));
                                        ivCheck.setVisibility(View.GONE);
                                    } else {
                                        envirstandards.add(text);
                                        view.setBackgroundResource(R.drawable.back_filter_car_btn_2);
                                        tvName.setTextColor(getResources().getColor(R.color.main_color));
                                        ivCheck.setVisibility(View.VISIBLE);
                                    }
                                    tv = (TextView) ((ViewGroup) ((ViewGroup) llRoot.getChildAt(index)).getChildAt(0)).getChildAt(2);
                                    size = envirstandards.size();
                                    if (size == 0 || size == 4) {
                                        tv.setText("全部");
                                    } else {
                                        tv.setText(getNamesByList(envirstandards));
                                    }
                                    break;
                            }
                            getCarListDataFromNet(1);
                        }
                    });
                }
            }
        }
    }


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
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONArray jsonObjectData = jsonObject.optJSONArray("data");
                            if (status == 1) {
                                mCarConfigurationList = getCarConfigurations(jsonObjectData);
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

    private ArrayList<CarConfiguration> getCarConfigurations(JSONArray array) {
        if (array == null) {
            return new ArrayList<>();
        } else {
            return new Gson().fromJson(array.toString(), new TypeToken<ArrayList<CarConfiguration>>() {
            }.getType());
        }
    }


    //详细配置
    private void resetCarConfigurations(View view, ArrayList<CarConfiguration> list) {
        extra_infos.clear();
        LinearLayout llDetailMsg = (LinearLayout) view.findViewById(R.id.llDetailMsg);
        llDetailMsg.removeAllViews();

        if (list == null || list.isEmpty()) {
            return;
        }
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        LinearLayout layout;
        ArrayList<CarConfiguration.Configuration> itemList;
        for (CarConfiguration config : list) {
            layout = (LinearLayout) layoutInflater.inflate(R.layout.item_car_configuration, null);
            layout.findViewById(R.id.line).setVisibility(View.GONE);
            llDetailMsg.addView(layout);
            final ViewGroup itemView1 = (ViewGroup) layout.getChildAt(0);
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
                if (index < itemCount) {
                    charCount += itemList.get(index).getName().length();
                    ((TextView) llName1.getChildAt(1)).setText(itemList.get(index).getName());
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
        if (list.contains(text)) {
            list.remove(text);
            view.setBackgroundResource(R.drawable.back_filter_car_btn_1);
            tv.setTextColor(getResources().getColor(R.color.main_text_color));
            ivCheck.setVisibility(View.GONE);
        } else {
            list.add(text);
            view.setBackgroundResource(R.drawable.back_filter_car_btn_2);
            tv.setTextColor(getResources().getColor(R.color.main_color));
            ivCheck.setVisibility(View.VISIBLE);
        }
        ViewGroup parent = (ViewGroup) view.getParent().getParent().getParent();
        ViewGroup itemView1 = (ViewGroup) parent.getChildAt(0);
        int size = (int) view.getTag();
        if (list.isEmpty() || list.size() == size) {
            ((TextView) itemView1.getChildAt(2)).setText("全部");
        } else {
            ((TextView) itemView1.getChildAt(2)).setText(getNamesByList(list));
        }
        getCarListDataFromNet(1);
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

    private void resetTextColor(TextView tv_sort_default, TextView tv_sort_most_low, TextView tv_sort_most_top, TextView tv_sort_newest_publish) {
        tv_sort_default.setTextColor(getResources().getColor(R.color.main_text_color));
        tv_sort_most_low.setTextColor(getResources().getColor(R.color.main_text_color));
        tv_sort_most_top.setTextColor(getResources().getColor(R.color.main_text_color));
        tv_sort_newest_publish.setTextColor(getResources().getColor(R.color.main_text_color));
        if (order_id.equals("0")) {
            tv_sort_default.setTextColor(getResources().getColor(R.color.main_color));
        } else if (order_id.equals("1")) {
            tv_sort_newest_publish.setTextColor(getResources().getColor(R.color.main_color));
        } else if (order_id.equals("2")) {
            tv_sort_most_low.setTextColor(getResources().getColor(R.color.main_color));
        } else if (order_id.equals("3")) {
            tv_sort_most_top.setTextColor(getResources().getColor(R.color.main_color));
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mPage = 0;
        getCarListDataFromNet(2);
    }


    //获取车信息列表
    private void getCarListDataFromNet(final int searchType) {
        OkHttpUtils.post()
                .url(Constant.getCarListUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                //     .addParams(Constant.KEY_WORD, "")//关键字
                .addParams(Constant.ORDER_ID, order_id == null ? "" : order_id)//排序id
                .addParams(Constant.BRAND_ID, brand_id == 0 ? "" : String.valueOf(brand_id))//车品牌id
                .addParams(Constant.SERIES_ID, series_id == 0 ? "" : String.valueOf(series_id))//车系列id
                .addParams(Constant.PAGE, String.valueOf(mPage))
                .addParams(Constant.MIN_PRICE, min_price == null ? "" : min_price)//最低价格
                .addParams(Constant.MAX_PRICE, max_price == null ? "" : max_price)//最高价格
//                .addParams(Constant.MIN_PRICE, min_price == null ? "" : min_price)//最低价格
//                .addParams(Constant.MAX_PRICE, max_price == null ? "" : max_price)//最高价格
                .addParams(Constant.MIN_MILEAGE, min_mileage == null ? "" : min_mileage)//最低里程
                .addParams(Constant.MAX_MILEAGE, max_mileage == null ? "" : max_mileage)//最高里程
                .addParams(Constant.MIN_BOARD_TIME, min_board_time == null ? "" : min_board_time)//最短上牌时间
                .addParams(Constant.MAX_BOARD_TIME, max_board_time == null ? "" : max_board_time)//最长上牌时间
                .addParams(Constant.MIN_FORFLOAT, min_pailiang == null ? "" : min_pailiang)//最短上牌时间
                .addParams(Constant.MAX_FORFLOAT, max_pailiang == null ? "" : max_pailiang)//最长上牌时间
                //               .addParams(Constant.HAS_VR, car_vr == 0 ? "" : String.valueOf(car_vr))//是否有VR看车功能  1:是
                .addParams(Constant.SEARCH_TYPE, String.valueOf(searchType))
                .addParams(Constant.CAR_TYPE, new_or_old == null ? "" : new_or_old)//是否新车二手车 1:新车 2 二手车
                .addParams(Constant.CAR_SOURCE, car_source)//车辆来源 1:个人 2 商家
                .addParams(Constant.CAR_LEVEL, getIdsByList(car_levels))
                .addParams(Constant.SEAT_NUM, getIdsByList(seat_nums))
                .addParams(Constant.FUELTYPE, getIdsByList(car_fueltypes))
                .addParams(Constant.BOARD_ADD, board_add)
                .addParams(Constant.CAR_COLOR, getIdsByList(car_colors))
                .addParams(Constant.FORWARD, forward)
                .addParams(Constant.ENVIRSTANDARD, getIdsByList(envirstandards))
                .addParams(Constant.EXTRA_INFO, getIdsByList(extra_infos))
                .addParams(Constant.CITY_CODE, SPUtils.getInstance().getString(Constant.CITY_CODE, ""))
                .addParams(Constant.CITY_LAT, SPUtils.getInstance().getString(Constant.CITY_LAT, ""))
                .addParams(Constant.CITY_LNG, SPUtils.getInstance().getString(Constant.CITY_LNG, ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("TESTLOG","getcarlist =="+response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                if (searchType == 2) {
                                    if (mPage == 0) {
                                        smartRefreshLayout.finishRefresh();
                                        handlerCarListData(jsonObjectData);
                                    } else {
                                        smartRefreshLayout.finishLoadmore();
                                        handlerCarListMoreData(jsonObjectData);
                                    }
                                } else { //筛选中找到多少辆符合条件的车
                                    setTotalCar(jsonObjectData.optInt("total"));
                                }
                            } else {
                                if (mPage > 0) {
                                    --mPage;
                                }
                                String code = jsonObject.optString("code");
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("" + msg);
                            }
                        } catch (JSONException e) {
                            if (mPage > 0) {
                                --mPage;
                            }
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void setTotalCar(int total) {
        if (getView() != null && tvTotalCar != null) {
            if (total > 0) {
                tvTotalCar.setText("共" + total + "辆车符合条件");
            } else {
                tvTotalCar.setText("找到0辆车,开启车辆定制");
            }
        }
    }

    private String getIdsByList(ArrayList<String> list) {
        String ids = "";
        int size = list.size();
        for (int i = 0; i < size; ++i) {
            if (i < size - 1) {
                ids += list.get(i).split("_")[0] + ",";
            } else {
                ids += list.get(i).split("_")[0];
            }
        }
        return ids;
    }

    private String getIdsByList(TreeMap<Integer, ArrayList<String>> map) {
        String ids = "";
        Iterator<TreeMap.Entry<Integer, ArrayList<String>>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            TreeMap.Entry<Integer, ArrayList<String>> entry = entries.next();
            ArrayList<String> list = entry.getValue();
            if (list != null && !list.isEmpty()) {
                int size = list.size();
                for (int i = 0; i < size; ++i) {
                    ids += list.get(i).split("_")[0] + ",";
                }
            }
        }
        if (ids.length() > 0) {
            ids = ids.substring(0, ids.length() - 1);
        }
        return ids;
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

    private void handlerCarListMoreData(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            ArrayList<CarInfoBean> carInfoBeanArrayList = getCarListData(jsonObjectData);
            if (EmptyUtils.isNotEmpty(carInfoBeanArrayList) && carInfoBeanArrayList.size() != 0) {
                baseRecyclerViewAdapter.addDatas(carInfoBeanArrayList);
                mHeaderAndFooterWrapper.notifyDataSetChanged();
            } else {
                ToastUtils.showShort("没有更多了");
            }
        }
    }

    private void handlerCarListData(JSONObject jsonObjectData) {
        ArrayList<CarInfoBean> carInfoBeanArrayList = getCarListData(jsonObjectData);
        if (baseRecyclerViewAdapter == null) {
            baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, carInfoBeanArrayList, CAR_LIST_FOR_CAR_CENTER);
            mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(baseRecyclerViewAdapter);
            llTop = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.car_center_topview, null);//作为头布局存放过滤条件
            mHeaderAndFooterWrapper.addHeaderView(llTop);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(mHeaderAndFooterWrapper);
        } else {
            baseRecyclerViewAdapter.setDatas(carInfoBeanArrayList);
            mHeaderAndFooterWrapper.notifyDataSetChanged();
        }
        baseRecyclerViewAdapter.setOnItemClickListener(this);
        resetFilterView(llTop, carInfoBeanArrayList == null || carInfoBeanArrayList.isEmpty(), jsonObjectData.optString("custom_url"));
    }

    //过滤条件
    public class FilterCondition {
        int type;
        String text;
        String tag;
        int index;
    }

    private ArrayList<FilterCondition> getFilterConditions() {
        ArrayList<FilterCondition> list = new ArrayList<>();
        FilterCondition condition;
        if (!min_price.equals("0") || !TextUtils.isEmpty(max_price)) {
            condition = new FilterCondition();
            condition.type = 1;
            if (TextUtils.isEmpty(max_price)) {
                condition.text = min_price + "万以上";
            } else {
                condition.text = min_price + "-" + max_price + "万";
            }
            list.add(condition);
        }
        if (!TextUtils.isEmpty(new_or_old)) {
            condition = new FilterCondition();
            condition.type = 2;
            condition.text = new_or_old.equals("1") ? "新车" : "二手车";
            list.add(condition);
        }

        if (!TextUtils.isEmpty(car_source)) {
            condition = new FilterCondition();
            condition.type = 3;
            condition.text = car_source.equals("1") ? "个人" : "商家";
            list.add(condition);
        }

        if (!car_levels.isEmpty()) {
            for (String str : car_levels) {
                condition = new FilterCondition();
                condition.type = 4;
                String[] strs = str.split("_");
                condition.tag = strs[0];
                condition.text = strs[1];
                list.add(condition);
            }
        }

        if (!seat_nums.isEmpty()) {
            for (String str : seat_nums) {
                condition = new FilterCondition();
                condition.type = 5;
                String[] strs = str.split("_");
                condition.tag = strs[0];
                condition.text = strs[1];
                list.add(condition);
            }
        }

        if (!car_colors.isEmpty()) {
            for (String str : car_colors) {
                condition = new FilterCondition();
                condition.type = 7;
                String[] strs = str.split("_");
                condition.tag = strs[0];
                condition.text = strs[1];
                list.add(condition);
            }
        }
        if (!new_or_old.equals("1")) {
            if (!TextUtils.isEmpty(board_add)) {
                condition = new FilterCondition();
                condition.type = 6;
                condition.text = board_add.equals("1") ? "本地牌照" : "外地牌照";
                list.add(condition);
            }
            if (!min_mileage.equals("0") || !TextUtils.isEmpty(max_mileage)) {
                condition = new FilterCondition();
                condition.type = 8;
                if (TextUtils.isEmpty(max_mileage)) {
                    condition.text = min_mileage + "万公里以上";
                } else {
                    condition.text = min_mileage + "-" + max_mileage + "万公里";
                }
                list.add(condition);
            }

            if (!min_board_time.equals("0") || !TextUtils.isEmpty(max_board_time)) {
                condition = new FilterCondition();
                condition.type = 9;
                if (TextUtils.isEmpty(max_board_time)) {
                    condition.text = min_board_time + "年以上";
                } else {
                    condition.text = min_board_time + "-" + max_board_time + "年";
                }
                list.add(condition);
            }
        }


        if (!min_pailiang.equals("0") || !TextUtils.isEmpty(max_pailiang)) {
            condition = new FilterCondition();
            condition.type = 10;
            if (TextUtils.isEmpty(max_pailiang)) {
                condition.text = min_pailiang + "L以上";
            } else {
                condition.text = min_pailiang + "-" + max_pailiang + "L";
            }
            list.add(condition);
        }

        if (!car_fueltypes.isEmpty()) {
            for (String str : car_fueltypes) {
                condition = new FilterCondition();
                condition.type = 11;
                String[] strs = str.split("_");
                condition.tag = strs[0];
                condition.text = strs[1];
                list.add(condition);
            }
        }

        if (!TextUtils.isEmpty(forward)) {
            condition = new FilterCondition();
            condition.type = 12;
            condition.text = board_add.equals("1") ? "手动" : "自动";
            list.add(condition);
        }

        if (!envirstandards.isEmpty()) {
            for (String str : envirstandards) {
                condition = new FilterCondition();
                condition.type = 13;
                String[] strs = str.split("_");
                condition.tag = strs[0];
                condition.text = strs[1];
                list.add(condition);
            }
        }

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

    private void onFilterCondition(View view) {
        FilterCondition condition = (FilterCondition) view.getTag(R.id.tag);
        switch (condition.type) {//通过type区分点击了不同的过滤条件（删除该过滤条件）
            case -1:
                price_id = -1;
                min_price = "0";
                max_price = "";
                new_or_old = "";
                car_source = "";
                car_levels.clear();
                seat_nums.clear();
                car_colors.clear();
                car_fueltypes.clear();
                envirstandards.clear();
                extra_infos.clear();
                board_add = "";
                forward = "";
                min_mileage = "0";
                max_mileage = "";
                min_board_time = "0";
                max_board_time = "";
                min_pailiang = "0";
                max_pailiang = "";
                break;
            case 1:
                price_id = -1;
                min_price = "0";
                max_price = "";
                break;
            case 2:
                new_or_old = "";
                break;
            case 3:
                car_source = "";
                break;
            case 4:
                car_levels.remove(condition.tag + "_" + condition.text);
                break;
            case 5:
                seat_nums.remove(condition.tag + "_" + condition.text);
                break;
            case 6:
                board_add = "";
                break;
            case 7:
                car_colors.remove(condition.tag + "_" + condition.text);
                break;
            case 8:
                min_mileage = "0";
                max_mileage = "";
                break;
            case 9:
                min_board_time = "0";
                max_board_time = "";
                break;
            case 10:
                min_pailiang = "0";
                max_pailiang = "";
                break;
            case 11:
                car_fueltypes.remove(condition.tag + "_" + condition.text);
                break;
            case 12:
                forward = "";
                break;
            case 13:
                envirstandards.remove(condition.tag + "_" + condition.text);
                break;
            case 14:
                ArrayList<String> list = extra_infos.get(condition.index);
                list.remove(condition.tag + "_" + condition.text);
                break;

        }
        mPage = 0;
        getCarListDataFromNet(2);
    }

    //重置过滤条件头部
    private void resetFilterView(LinearLayout linearLayout, boolean isNoData, String dingzhiUrl) {
        if (mHeaderAndFooterWrapper == null) {
            return;
        }
        linearLayout.removeAllViews();

        ArrayList<FilterCondition> list = getFilterConditions();//获取过滤条件对象集合
        int itemCount = list.size();//过滤条件的个数
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
                if (charCount + list.get(index).text.length() > 16) {//如果前面两个item的长度加上第三个item的长度超过16个字，则不显示第三个，换行
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

        if (isNoData) {//没有符合条件的车辆，显示定制按钮
            View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_car_dingzhi, null);
            linearLayout.addView(noDataView);
            noDataView.findViewById(R.id.tvDingzhi).setTag(dingzhiUrl);
            noDataView.findViewById(R.id.tvDingzhi).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dingzhiUrl = (String) v.getTag();
                    Bundle bundle = new Bundle();
                    bundle.putString("dingzhi_url", dingzhiUrl);
                    gotoPager(CarDingzhiFragment.class, bundle);
                }
            });
        }
    }


    //将jsonobject用gson解析成CarInfoBean类
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
                Bundle bundle = new Bundle();
                bundle.putString("from_class", getClass().getName());
                bundle.putBoolean("is_post_new_car", true);
                DataManager.getInstance().setObject(null);
                gotoPager(SelectPhotoFragment.class, bundle, true);
            }

            @Override
            public void onSecondBtnClicked() {
                //上传二手车
                Bundle bundle = new Bundle();
                bundle.putString("from_class", getClass().getName());
                bundle.putBoolean("is_post_new_car", false);
                DataManager.getInstance().setObject(null);
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
        }, MorePopupWindow.MORE_POPUP_WINDOW_TYPE.TYPE_SALE_CAR);
        morePopupWindow.initView();
        morePopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        ++mPage;
        getCarListDataFromNet(2);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage = 0;
        getCarListDataFromNet(2);
    }

    private void initLocation(){
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2000);

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);

        //设置定位请求超时时间.单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(30000);

        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(true);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }


    private class GaoDeLocationListener implements AMapLocationListener {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {

            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。

                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();//获取纬度
                    amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    amapLocation.getCountry();//国家信息
                    amapLocation.getProvince();//省信息
                    amapLocation.getCity();//城市信息
                    amapLocation.getDistrict();//城区信息
                    amapLocation.getStreet();//街道信息
                    amapLocation.getStreetNum();//街道门牌号信息
                    amapLocation.getCityCode();//城市编码
                    amapLocation.getAdCode();//地区编码
                    amapLocation.getAoiName();//获取当前定位点的AOI信息
                    amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                    amapLocation.getFloor();//获取当前室内定位的楼层
                    //                    amapLocation.getGpsStatus();//获取GPS的当前状态
                    //获取定位时间
                    //                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //                    Date date = new Date(amapLocation.getTime());
                    //                    df.format(date);

                    if (!TextUtils.isEmpty(amapLocation.getCity())) {
                        if (!TextUtils.isEmpty(amapLocation.getCityCode())) {
                            SPUtils.getInstance().put(Constant.CITY_CODE, String.valueOf(amapLocation.getCityCode()));
                        }

                        SPUtils.getInstance().put(Constant.CITY_LAT, String.valueOf(amapLocation.getLatitude()));
                        SPUtils.getInstance().put(Constant.CITY_LNG, String.valueOf(amapLocation.getLongitude()));
                    }


                    if (amapLocation.getLocationType() != 0) {

                        Message msg = Message.obtain();
                        msg.what = 0x0001;
                        msg.obj = amapLocation.getCity();
                        handler.sendMessage(msg);
                        return ;
                    }

                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());


                }
            }

            //定位失败
            handler.sendEmptyMessage(0x0002);
        }
    }



    /**
     * 配置定位SDK参数
     */
//    private void initLocation() {
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//
//        option.setCoorType("bd09ll");
//        //可选，默认gcj02，设置返回的定位结果坐标系
//
//        int span = 1000;
//        option.setScanSpan(span);
//        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//
//        option.setIsNeedAddress(true);
//        //可选，设置是否需要地址信息，默认不需要
//
//        option.setOpenGps(true);
//        //可选，默认false,设置是否使用gps
//
//        option.setLocationNotify(true);
//        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
//
//        option.setIsNeedLocationDescribe(true);
//        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//
//        option.setIsNeedLocationPoiList(true);
//        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//
//        option.setIgnoreKillProcess(false);
//        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//
//        option.SetIgnoreCacheException(false);
//        //可选，默认false，设置是否收集CRASH信息，默认收集
//
//        option.setEnableSimulateGps(false);
//        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
//
//        option.setWifiCacheTimeOut(5 * 60 * 1000);
//        //可选，7.2版本新增能力，如果您设置了这个接口，首次启动定位时，会先判断当前WiFi是否超出有效期，超出有效期的话，会先重新扫描WiFi，然后再定位
//
//        mLocationClient.setLocOption(option);
//    }
//
//    private class MyLocationListener extends BDAbstractLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            //获取定位结果
//            location.getTime();    //获取定位时间
//            location.getLocationID();    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
//            location.getLocType();    //获取定位类型
//            location.getLatitude();    //获取纬度信息
//            location.getLongitude();    //获取经度信息
//            location.getRadius();    //获取定位精准度
//            location.getAddrStr();    //获取地址信息
//            location.getCountry();    //获取国家信息
//            location.getCountryCode();    //获取国家码
//            location.getCity();    //获取城市信息
//            location.getCityCode();    //获取城市码
//            location.getDistrict();    //获取区县信息
//            location.getStreet();    //获取街道信息
//            location.getStreetNumber();    //获取街道码
//            location.getLocationDescribe();    //获取当前位置描述信息
//            location.getPoiList();    //获取当前位置周边POI信息
//
//            location.getBuildingID();    //室内精准定位下，获取楼宇ID
//            location.getBuildingName();    //室内精准定位下，获取楼宇名称
//            location.getFloor();    //室内精准定位下，获取当前位置所处的楼层信息
//
//            if (!TextUtils.isEmpty(location.getCity())) {
//                if (!TextUtils.isEmpty(location.getCityCode())) {
//                    SPUtils.getInstance().put(Constant.CITY_CODE, String.valueOf(location.getCityCode()));
//                }
//
//                SPUtils.getInstance().put(Constant.CITY_LAT, String.valueOf(location.getLatitude()));
//                SPUtils.getInstance().put(Constant.CITY_LNG, String.valueOf(location.getLongitude()));
//            }
//
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {
//
//                //当前为GPS定位结果，可获取以下信息
//                location.getSpeed();    //获取当前速度，单位：公里每小时
//                location.getSatelliteNumber();    //获取当前卫星数
//                location.getAltitude();    //获取海拔高度信息，单位米
//                location.getDirection();    //获取方向信息，单位度
//
//                Message msg = Message.obtain();
//                msg.what = 0x0001;
//                msg.obj = location.getCity();
//                handler.sendMessage(msg);
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//
//                //当前为网络定位结果，可获取以下信息
//                location.getOperators();    //获取运营商信息
//
//                Message msg = Message.obtain();
//                msg.what = 0x0001;
//                msg.obj = location.getAddress().city;
//                handler.sendMessage(msg);
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
//
//                //当前为网络定位结果
//
//                Message msg = Message.obtain();
//                msg.what = 0x0001;
//                msg.obj = location.getAddress().city;
//                handler.sendMessage(msg);
//            } else if (location.getLocType() == BDLocation.TypeServerError) {
//
//                //当前网络定位失败
//                //可将定位唯一ID、IMEI、定位失败时间反馈至loc-bugs@baidu.com
//                handler.sendEmptyMessage(0x0002);
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//
//                //当前网络不通
//                handler.sendEmptyMessage(0x0002);
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//
//                //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
//                //可进一步参考onLocDiagnosticMessage中的错误返回码
//                handler.sendEmptyMessage(0x0002);
//            }
//        }
//
//        /**
//         * 回调定位诊断信息，开发者可以根据相关信息解决定位遇到的一些问题
//         * 自动回调，相同的diagnosticType只会回调一次
//         *
//         * @param locType           当前定位类型
//         * @param diagnosticType    诊断类型（1~9）
//         * @param diagnosticMessage 具体的诊断信息释义
//         */
//        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {
//
//            if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_GPS) {
//
//                //建议打开GPS
//
//            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_WIFI) {
//
//                //建议打开wifi，不必连接，这样有助于提高网络定位精度！
//
//            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_LOC_PERMISSION) {
//
//                //定位权限受限，建议提示用户授予APP定位权限！
//
//            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_NET) {
//
//                //网络异常造成定位失败，建议用户确认网络状态是否异常！
//
//            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CLOSE_FLYMODE) {
//
//                //手机飞行模式造成定位失败，建议用户关闭飞行模式后再重试定位！
//
//            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_INSERT_SIMCARD_OR_OPEN_WIFI) {
//
//                //无法获取任何定位依据，建议用户打开wifi或者插入sim卡重试！
//
//            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_OPEN_PHONE_LOC_SWITCH) {
//
//                //无法获取有效定位依据，建议用户打开手机设置里的定位开关后重试！
//
//            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_SERVER_FAIL) {
//
//                //百度定位服务端定位失败
//                //建议反馈location.getLocationID()和大体定位时间到loc-bugs@baidu.com
//
//            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_FAIL_UNKNOWN) {
//
//                //无法获取有效定位依据，但无法确定具体原因
//                //建议检查是否有安全软件屏蔽相关定位权限
//                //或调用LocationClient.restart()重新启动后重试！
//            }
//        }
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }
}
