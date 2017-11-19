package com.wiserz.pbibi.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.adapter.BrandAdapter;
import com.wiserz.pbibi.bean.BrandInfoBean;
import com.wiserz.pbibi.bean.CarSeriesBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import cc.solart.wave.LetterComparator;
import cc.solart.wave.PinnedHeaderDecoration;
import cc.solart.wave.WaveSideBarView;
import okhttp3.Call;

/**
 * Created by jackie on 2017/9/9 11:06.
 * QQ : 971060378
 * Used as : 选择车辆品牌的页面
 */
public class SelectCarBrandFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener {

    private RecyclerView brand_recyclerView;
    private WaveSideBarView wave_side_bar_view;

    private ArrayList<BrandInfoBean> brandInfoBeanArrayList;
    private String[] strings;

    private RelativeLayout rlSelectSeries;
    private RecyclerView recyclerViewCarSeries;

    private static final int CAR_SERIES_DATA_TYPE = 40;

    private String from_class;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_car_brand;
    }

    @Override
    protected void initView(View view) {
        from_class = getArguments().getString("fromClass", "");

        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("选择车系");

        brand_recyclerView = (RecyclerView) view.findViewById(R.id.brand_recyclerView);
        wave_side_bar_view = (WaveSideBarView) view.findViewById(R.id.wave_side_bar_view);

        strings = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        brandInfoBeanArrayList = new ArrayList<>();

        rlSelectSeries = (RelativeLayout) view.findViewById(R.id.rlSelectSeries);
        recyclerViewCarSeries = (RecyclerView) view.findViewById(R.id.recyclerViewCarSeries);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        getBrandDataFromNet();
    }

    private void getBrandDataFromNet() {
        OkHttpUtils.post()
                .url(Constant.getCarBrandUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
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
                                handlerCarBrandData(jsonObjectData);
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

    private void handlerCarBrandData(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONObject jsonObject = jsonObjectData.optJSONObject("brand_list");

            if (EmptyUtils.isNotEmpty(jsonObject)) {
                Gson gson = new Gson();

                for (String string : strings) {
                    JSONArray jsonArray = jsonObject.optJSONArray(string);
                    if (EmptyUtils.isNotEmpty(jsonArray) && jsonArray.length() != 0) {
                        BrandInfoBean brandInfoBean = new BrandInfoBean();
                        brandInfoBean.setType(1);
                        brandInfoBean.setAbbre(string);
                        brandInfoBeanArrayList.add(brandInfoBean);
                    }
                }

                for (String string : strings) {
                    JSONArray jsonArray = jsonObject.optJSONArray(string);
                    if (EmptyUtils.isNotEmpty(jsonArray) && jsonArray.length() != 0) {

                        ArrayList<BrandInfoBean> brandInfoArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<BrandInfoBean>>() {
                        }.getType());

                        if (EmptyUtils.isNotEmpty(brandInfoArrayList) && brandInfoArrayList.size() != 0) {
                            for (BrandInfoBean brandInfoBean : brandInfoArrayList) {
                                brandInfoBean.setAbbre(string);
                                brandInfoBean.setType(0);
                                brandInfoBeanArrayList.add(brandInfoBean);
                            }
                        }
                    }
                }
                Collections.sort(brandInfoBeanArrayList, new LetterComparator());
                if (!TextUtils.isEmpty(from_class) && from_class.equals(CarCenterFragment.class.getName())) {
                    BrandInfoBean brandInfoBean = new BrandInfoBean();
                    brandInfoBean.setBrand_id(0);
                    brandInfoBean.setBrand_name("不限");
                    brandInfoBeanArrayList.add(0, brandInfoBean);
                    brandInfoBean.setAbbre("#");
                }

                showDataForCarBrand(brandInfoBeanArrayList);
            }
        }
    }

    private void showDataForCarBrand(ArrayList<BrandInfoBean> brandInfoBeanArrayList) {
        if (EmptyUtils.isNotEmpty(brandInfoBeanArrayList) && brandInfoBeanArrayList.size() != 0) {

            final BrandAdapter brandAdapter = new BrandAdapter(mContext, brandInfoBeanArrayList);
            brand_recyclerView.setAdapter(brandAdapter);
            brand_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            brandAdapter.setOnItemClickListener(this);

            final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
            decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
                @Override
                public boolean create(RecyclerView parent, int adapterPosition) {
                    return true;
                }
            });
            brand_recyclerView.addItemDecoration(decoration);

            wave_side_bar_view.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
                @Override
                public void onLetterChange(String letter) {
                    int pos = brandAdapter.getLetterPosition(letter);

                    if (pos != -1) {
                        brand_recyclerView.scrollToPosition(pos);
                        LinearLayoutManager mLayoutManager = (LinearLayoutManager) brand_recyclerView.getLayoutManager();
                        mLayoutManager.scrollToPositionWithOffset(pos, 0);
                    }
                }
            });
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goBack();
                }
            }, 300);//此时FragmentManager is already executing transactions，所以要延迟
        }
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("BrandInfoBean")) {
            BrandInfoBean brandInfoBean = (BrandInfoBean) data;
            if (EmptyUtils.isNotEmpty(brandInfoBean)) {
                //设置brand信息
                DataManager.getInstance().setData1(brandInfoBean.getBrand_id());
                DataManager.getInstance().setData2(brandInfoBean.getBrand_name());
                if (brandInfoBean.getBrand_id() == 0) {
                    DataManager.getInstance().setData3(0);
                    DataManager.getInstance().setData4("不限");
                    goBack();
                    return;
                }
                showSelectSeriesView(brandInfoBean);
            }
        } else if (data.getClass().getSimpleName().equals("CarSeriesBean")) {
            CarSeriesBean carSeriesBean = (CarSeriesBean) data;
            if (EmptyUtils.isNotEmpty(carSeriesBean)) {
                rlSelectSeries.setVisibility(View.GONE);
                int series_id = carSeriesBean.getSeries_id();
                //设置series信息
                DataManager.getInstance().setData3(carSeriesBean.getSeries_id());
                DataManager.getInstance().setData4(carSeriesBean.getSeries_name());
                if (!TextUtils.isEmpty(from_class) && from_class.equals(CarCenterFragment.class.getName())) {
                    goBack();
                    return;
                }
                if (EmptyUtils.isNotEmpty(series_id)) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.SERIES_ID, series_id);
                    gotoPager(SelectCarModelFragment.class, bundle);
                }
            }
        }
    }

    private void showSelectSeriesView(BrandInfoBean brandInfoBean) {
        if (EmptyUtils.isNotEmpty(brandInfoBean)) {
            rlSelectSeries.setVisibility(View.VISIBLE);
            int brand_id = brandInfoBean.getBrand_id();
            if (EmptyUtils.isNotEmpty(brand_id)) {
                getSeriesListByBrandId(brand_id);
            }
        }
    }

    private void getSeriesListByBrandId(int brand_id) {
        OkHttpUtils.post()
                .url(Constant.getCarBrandSeriesUrl() + brand_id)
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
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
                                handlerCarSeriesData(jsonObjectData);
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

    private void handlerCarSeriesData(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONArray jsonArray = jsonObjectData.optJSONArray("series_list");
            if (EmptyUtils.isNotEmpty(jsonArray)) {
                Gson gson = new Gson();
                ArrayList<CarSeriesBean> carSeriesBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CarSeriesBean>>() {
                }.getType());

                if (!TextUtils.isEmpty(from_class) && from_class.equals(CarCenterFragment.class.getName())) {
                    CarSeriesBean carSeriesBean = new CarSeriesBean();
                    carSeriesBean.setSeries_id(0);
                    carSeriesBean.setSeries_name("不限");
                    carSeriesBeanArrayList.add(0, carSeriesBean);
                }
                if (EmptyUtils.isNotEmpty(carSeriesBeanArrayList) && carSeriesBeanArrayList.size() != 0) {
                    BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, carSeriesBeanArrayList, CAR_SERIES_DATA_TYPE);
                    recyclerViewCarSeries.setAdapter(baseRecyclerViewAdapter);
                    recyclerViewCarSeries.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                    baseRecyclerViewAdapter.setOnItemClickListener(this);
                }
            }
        }
    }
}
