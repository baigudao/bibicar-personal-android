package com.wiserz.pbibi.fragment;

import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextPaint;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/8/14 18:17.
 * QQ : 971060378
 * Used as : 贷款方案的页面
 */
public class LoanPlanFragment extends BaseFragment {

    private EditText et_input_phone;
    private EditText et_input_name;

    private TextView tvFirstPay20;
    private TextView tvFirstPay40;
    private TextView tvFirstPay60;
    private TextView tvFirstPay80;
    private TextView tvPeriod12;
    private TextView tvPeriod24;
    private TextView tvPeriod36;
    private TextView tvPeriod60;

    private int mFirstPayType;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_loan_plan;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("贷款方案");
        view.findViewById(R.id.btn_commit).setOnClickListener(this);

        et_input_phone = (EditText) view.findViewById(R.id.et_input_phone);
        et_input_name = (EditText) view.findViewById(R.id.et_input_name);

        tvFirstPay20 = (TextView) view.findViewById(R.id.tvFirstPay20);
        tvFirstPay20.setOnClickListener(this);
        tvFirstPay40 = (TextView) view.findViewById(R.id.tvFirstPay40);
        tvFirstPay40.setOnClickListener(this);
        tvFirstPay60 = (TextView) view.findViewById(R.id.tvFirstPay60);
        tvFirstPay60.setOnClickListener(this);
        tvFirstPay80 = (TextView) view.findViewById(R.id.tvFirstPay80);
        tvFirstPay80.setOnClickListener(this);
        tvPeriod12 = (TextView) view.findViewById(R.id.tvPeriod12);
        tvPeriod12.setOnClickListener(this);
        tvPeriod24 = (TextView) view.findViewById(R.id.tvPeriod24);
        tvPeriod24.setOnClickListener(this);
        tvPeriod36 = (TextView) view.findViewById(R.id.tvPeriod36);
        tvPeriod36.setOnClickListener(this);
        tvPeriod60 = (TextView) view.findViewById(R.id.tvPeriod60);
        tvPeriod60.setOnClickListener(this);
    }

    private String getInputPhone() {
        return et_input_phone.getText().toString().trim();
    }

    private String getInputName() {
        return et_input_name.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_commit:
                commitApply();
                break;
            case R.id.tvFirstPay20:
                LogUtils.e("tvFirstPay20");
                mFirstPayType = 1;
                break;
            case R.id.tvFirstPay40:
                LogUtils.e("tvFirstPay40");
                mFirstPayType = 2;
                break;
            case R.id.tvFirstPay60:
                LogUtils.e("tvFirstPay60");
                mFirstPayType = 3;
                break;
            case R.id.tvFirstPay80:
                LogUtils.e("tvFirstPay80");
                mFirstPayType = 4;
                break;
            case R.id.tvPeriod12:
                LogUtils.e("tvPeriod12");
                break;
            case R.id.tvPeriod24:
                LogUtils.e("tvPeriod24");
                break;
            case R.id.tvPeriod36:
                LogUtils.e("tvPeriod36");
                break;
            case R.id.tvPeriod60:
                LogUtils.e("tvPeriod60");
                break;
            default:
                break;
        }
    }

    private void resetFirstPayViewByType() {
        int color = getResources().getColor(R.color.main_color);
        changeText(tvFirstPay20, color);
        changeText(tvFirstPay40, color);
        changeText(tvFirstPay60, color);
        changeText(tvFirstPay80, color);
        color = getResources().getColor(R.color.background_color);
        if (mFirstPayType == 1) {
            changeText(tvFirstPay20, color);
            TextPaint tp = tvFirstPay20.getPaint();
            tp.setFakeBoldText(true);
        } else if (mFirstPayType == 2) {
            changeText(tvFirstPay40, color);
            TextPaint tp = tvFirstPay40.getPaint();
            tp.setFakeBoldText(true);
        } else if (mFirstPayType == 3) {
            changeText(tvFirstPay60, color);
            TextPaint tp = tvFirstPay60.getPaint();
            tp.setFakeBoldText(true);
        } else if (mFirstPayType == 4) {
            changeText(tvFirstPay80, color);
            TextPaint tp = tvFirstPay80.getPaint();
            tp.setFakeBoldText(true);
        }
        resetLineFirstPay();
    }

    private void changeText(TextView textView, int color) {
        textView.setTextColor(color);
        TextPaint textPaint = textView.getPaint();
        textPaint.setFakeBoldText(false);
        LayerDrawable background = (LayerDrawable) textView.getBackground();
        GradientDrawable item1 = (GradientDrawable) background.findDrawableByLayerId(R.id.item1);
        item1.setColor(color);
    }

    private void resetLineFirstPay() {
        View line1 = getView().findViewById(R.id.lineFirstPay1);
        View line2 = getView().findViewById(R.id.lineFirstPay2);
        View line3 = getView().findViewById(R.id.lineFirstPay3);
        View line4 = getView().findViewById(R.id.lineFirstPay4);
        View line5 = getView().findViewById(R.id.lineFirstPay5);
        int grayColor = getResources().getColor(R.color.background_color);
        line1.setBackgroundColor(grayColor);
        line2.setBackgroundColor(grayColor);
        line3.setBackgroundColor(grayColor);
        line4.setBackgroundColor(grayColor);
        line5.setBackgroundColor(grayColor);
        int color = getResources().getColor(R.color.main_color);
        if (mFirstPayType == 1) {
            line1.setBackgroundColor(color);
            line2.setBackgroundColor(color);
        } else if (mFirstPayType == 2) {
            line2.setBackgroundColor(color);
            line3.setBackgroundColor(color);
        } else if (mFirstPayType == 3) {
            line3.setBackgroundColor(color);
            line4.setBackgroundColor(color);
        } else if (mFirstPayType == 4) {
            line4.setBackgroundColor(color);
            line5.setBackgroundColor(color);
        }
    }

    /**
     * 提交申请
     */
    private void commitApply() {

    }
}
