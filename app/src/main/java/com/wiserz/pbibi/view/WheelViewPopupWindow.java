package com.wiserz.pbibi.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.CityBean;
import com.wiserz.pbibi.bean.ProvinceBean;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import okhttp3.Call;

/**
 * Created by jackie on 2017/9/1 13:55.
 * QQ : 971060378
 * Used as : 滚动的PopupWindow
 */
public class WheelViewPopupWindow extends PopupWindow implements View.OnClickListener {

    public interface OnSelectItemListener {
        void onSelect(int index1, Object value1, int indx2, Object value2, int index3, Object value3);
    }

    public enum WHEEL_VIEW_WINDOW_TYPE {
        TYPE_GENDER,
        TYPE_DATA,
        TYPE_NUMBER,
        TYPE_YES_NO,
        TYPE_PROVINCE_CITY
    }

    protected Activity context;
    protected WHEEL_VIEW_WINDOW_TYPE type;
    private OnSelectItemListener mOnSelectItemListener;
    private ArrayList<ProvinceBean> mProvinceList;
    private ArrayList<CityBean> mCityList;

    private static final int[] GENDERS = {R.string.man, R.string.woman};
    private static final int[] YES_NO = {R.string.yes, R.string.no};

    public WheelViewPopupWindow(Activity context, OnSelectItemListener onSelectItemListener, WHEEL_VIEW_WINDOW_TYPE type) {
        super(context);
        setContentView(LayoutInflater.from(context).inflate(R.layout.select_by_wheel_view, null));
        this.context = context;
        this.type = type;
        this.mOnSelectItemListener = onSelectItemListener;
        setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //设置弹出窗体需要软键盘，
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        //再设置模式，和Activity的一样，覆盖，调整大小。
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // //设置SelectPicPopupWindow弹出窗体可点击
        // this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // 实例化一个ColorDrawable颜色为半透明
        // ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        setAnimationStyle(R.style.PopupWindowAnimation);
        this.setBackgroundDrawable(null);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        getContentView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = getMoreMenuView().getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        getContentView().setFocusableInTouchMode(true); // 设置view能够接听事件 标注2
        getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
                if (arg1 == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                }
                return false;
            }
        });
        getContentView().findViewById(R.id.tvCancel).setOnClickListener(this);
        getContentView().findViewById(R.id.tvSure).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                dismiss();
                break;
            case R.id.tvSure:
                dismiss();
                if (mOnSelectItemListener != null) {
                    WheelView wheelVie1 = (WheelView) getContentView().findViewById(R.id.wheelVie1);
                    int index1 = wheelVie1.getCurrentItem();
                    WheelView wheelVie2 = (WheelView) getContentView().findViewById(R.id.wheelVie2);
                    int index2 = wheelVie2.getCurrentItem();
                    WheelView wheelVie3 = (WheelView) getContentView().findViewById(R.id.wheelVie3);
                    int index3 = wheelVie3.getCurrentItem();
                    if (type != WHEEL_VIEW_WINDOW_TYPE.TYPE_PROVINCE_CITY) {
                        AbstractWheelTextAdapter adapter1 = (AbstractWheelTextAdapter) wheelVie1.getViewAdapter();
                        String value1 = adapter1 == null ? "" : adapter1.getItemText(index1).toString();
                        AbstractWheelTextAdapter adapter2 = (AbstractWheelTextAdapter) wheelVie2.getViewAdapter();
                        String value2 = adapter2 == null ? "" : adapter2.getItemText(index2).toString();
                        AbstractWheelTextAdapter adapter3 = (AbstractWheelTextAdapter) wheelVie3.getViewAdapter();
                        String value3 = adapter3 == null ? "" : adapter3.getItemText(index3).toString();
                        mOnSelectItemListener.onSelect(index1, value1, index2, value2, index3, value3);
                    } else {
                        if (CommonUtil.isListNullOrEmpty(mProvinceList) || CommonUtil.isListNullOrEmpty(mCityList)) {
                            return;
                        }
                        mOnSelectItemListener.onSelect(index1, mProvinceList.get(index1), index2, mCityList.get(index2), index3, null);
                    }
                }
                break;
        }
    }

    public View getViewG() {
        return getContentView().findViewById(R.id.view_bg);
    }

    public void initView() {
        if (type == null) {
            return;
        }

        final WheelView wheelVie1 = (WheelView) getContentView().findViewById(R.id.wheelVie1);
        final WheelView wheelVie2 = (WheelView) getContentView().findViewById(R.id.wheelVie2);
        final WheelView wheelVie3 = (WheelView) getContentView().findViewById(R.id.wheelVie3);
        if (type == WHEEL_VIEW_WINDOW_TYPE.TYPE_GENDER || type == WHEEL_VIEW_WINDOW_TYPE.TYPE_YES_NO) {
            wheelVie2.setVisibility(View.GONE);
            wheelVie3.setVisibility(View.GONE);
            YesNoAdapter yesNoAdapter = new YesNoAdapter(context);
            wheelVie1.setViewAdapter(yesNoAdapter);
        } else if (type == WHEEL_VIEW_WINDOW_TYPE.TYPE_DATA) {
            Calendar calendar = Calendar.getInstance();
            OnWheelChangedListener listener = new OnWheelChangedListener() {
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    updateDays(wheelVie1, wheelVie2, wheelVie3);
                }
            };

            // month
            int curMonth = calendar.get(Calendar.MONTH);
            String months[] = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
            wheelVie2.setViewAdapter(new DateArrayAdapter(context, months, curMonth));
            wheelVie2.setCurrentItem(curMonth);
            wheelVie2.addChangingListener(listener);

            // year
            int curYear = calendar.get(Calendar.YEAR);
            wheelVie1.setViewAdapter(new DateNumericAdapter(context, curYear - 50, curYear + 50, 50));
            wheelVie1.setCurrentItem(50);
            wheelVie1.addChangingListener(listener);

            //day
            updateDays(wheelVie1, wheelVie2, wheelVie3);
            wheelVie3.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        } else if (type == WHEEL_VIEW_WINDOW_TYPE.TYPE_NUMBER) {
            wheelVie2.setVisibility(View.GONE);
            wheelVie3.setVisibility(View.GONE);
            NumericWheelAdapter numberAdapter = new NumericWheelAdapter(context, 0, 10);
            numberAdapter.setItemResource(R.layout.wheel_text_item);
            numberAdapter.setItemTextResource(R.id.text);
            wheelVie1.setViewAdapter(numberAdapter);
        } else if (type == WHEEL_VIEW_WINDOW_TYPE.TYPE_PROVINCE_CITY) {
            wheelVie3.setVisibility(View.GONE);
            ProvinceAdapter provinceAdapter = new ProvinceAdapter(context);
            wheelVie1.setViewAdapter(provinceAdapter);
            OnWheelChangedListener listener = new OnWheelChangedListener() {
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    updateCitys(wheelVie1);
                }
            };
            wheelVie1.addChangingListener(listener);
            updateCitys(wheelVie1);

            CityAdapter cityAdapter = new CityAdapter(context);
            wheelVie2.setViewAdapter(cityAdapter);
        }

    }

    public void setProvinceList(ArrayList<ProvinceBean> provinceList) {
        mProvinceList = provinceList;
        final WheelView wheelVie1 = (WheelView) getContentView().findViewById(R.id.wheelVie1);
        ProvinceAdapter provinceAdapter = new ProvinceAdapter(context);
        wheelVie1.setViewAdapter(provinceAdapter);
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateCitys(wheelVie1);
            }
        };
        wheelVie1.addChangingListener(listener);
        updateCitys(wheelVie1);
    }


    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        viewShowLocation();
    }

    protected void viewShowLocation() {
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(getViewG(), "alpha", 0.5f).setDuration(250);
        fadeAnim.setStartDelay(250);
        fadeAnim.start();
    }

    @Override
    public void update() {
        super.update();
    }

    private LinearLayout getMoreMenuView() {
        return (LinearLayout) getContentView().findViewById(R.id.pop_layout);
    }


    @Override
    public void dismiss() {
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(getViewG(), "alpha", 0f).setDuration(250);
        fadeAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            public void onAnimationEnd(Animator animation) {
                WheelViewPopupWindow.super.dismiss();
            }
        });
        fadeAnim.start();
    }


    /**
     * YesNo Adapter
     */
    private class YesNoAdapter extends AbstractWheelTextAdapter {

        /**
         * Constructor
         */
        protected YesNoAdapter(Context context) {
            super(context, R.layout.wheel_text_item, NO_RESOURCE);
            setItemTextResource(R.id.text);
        }

        @Override
        public int getItemsCount() {
            return GENDERS.length;
        }

        @Override
        public CharSequence getItemText(int index) {
            return type == WHEEL_VIEW_WINDOW_TYPE.TYPE_YES_NO ?
                    context.getString(YES_NO[index]) : context.getString(GENDERS[index]);
        }
    }


    /**
     * Province Adapter
     */
    private class ProvinceAdapter extends AbstractWheelTextAdapter {

        /**
         * Constructor
         */
        protected ProvinceAdapter(Context context) {
            super(context, R.layout.wheel_text_item, NO_RESOURCE);
            setItemTextResource(R.id.text);
        }

        @Override
        public int getItemsCount() {
            return mProvinceList == null ? 1 : mProvinceList.size();
        }

        @Override
        public CharSequence getItemText(int index) {
            return mProvinceList == null ? "" : mProvinceList.get(index).getProvince();
        }
    }


    /**
     * City Adapter
     */
    private class CityAdapter extends AbstractWheelTextAdapter {

        /**
         * Constructor
         */
        protected CityAdapter(Context context) {
            super(context, R.layout.wheel_text_item, NO_RESOURCE);
            setItemTextResource(R.id.text);
        }

        @Override
        public int getItemsCount() {
            return mCityList == null ? 1 : mCityList.size();
        }

        @Override
        public CharSequence getItemText(int index) {
            return mCityList == null ? "" : mCityList.get(index).getCity_name();
        }
    }

    /**
     * Updates day wheel. Sets max days according to selected month and year
     */
    void updateDays(WheelView year, WheelView month, WheelView day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(context, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }

    void updateCitys(WheelView wheelView1) {
        if (CommonUtil.isListNullOrEmpty(mProvinceList)) {
            return;
        }
        int index = wheelView1.getCurrentItem();
        getCityList(mProvinceList.get(index).getProvince_id());
    }

    private void getCityList(final int provinceId) {
        OkHttpUtils.post()
                .url(Constant.getCityListUrl() + provinceId)
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
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                JSONArray jsonArray = jsonObjectData.optJSONArray("city_list");
                                if (EmptyUtils.isNotEmpty(jsonArray) && jsonArray.length() != 0) {
                                    Gson gson = new Gson();
                                    mCityList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                        CityBean cityBean = gson.fromJson(jsonObject1.toString(), CityBean.class);
                                        mCityList.add(cityBean);
                                    }
                                    final WheelView wheelVie2 = (WheelView) getContentView().findViewById(R.id.wheelVie2);
                                    CityAdapter cityAdapter = new CityAdapter(context);
                                    wheelVie2.setViewAdapter(cityAdapter);
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

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
}
