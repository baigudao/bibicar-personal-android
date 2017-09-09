package com.wiserz.pbibi.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.GifSizeFilter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jackie on 2017/8/15 10:35.
 * QQ : 971060378
 * Used as : 查维保的页面
 */
public class CheckGuaranteeFragment extends BaseFragment {

    private static final int REQUEST_CODE_CHOOSE = 55;
    private EditText et_input_vin;
    private ImageView iv_image_vin_container;
    private ImageView iv_add_vin;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_check_guarantee;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("查维保");
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setVisibility(View.VISIBLE);
        btn_register.setText("查询历史");
        btn_register.setTextSize(15);
        btn_register.setTextColor(getResources().getColor(R.color.second_text_color));
        btn_register.setOnClickListener(this);

        et_input_vin = (EditText) view.findViewById(R.id.et_input_vin);
        iv_add_vin = (ImageView) view.findViewById(R.id.iv_add_vin);
        iv_add_vin.setOnClickListener(this);
        iv_image_vin_container = (ImageView) view.findViewById(R.id.iv_image_vin_container);
        iv_image_vin_container.setOnClickListener(this);
        view.findViewById(R.id.btn_check).setOnClickListener(this);

        view.findViewById(R.id.rl_check_brand).setOnClickListener(this);
    }

    private String getInputVin() {
        return et_input_vin.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_register:
                ToastUtils.showShort("查询历史");
                break;
            case R.id.btn_check:
                ToastUtils.showShort("立即查询");
                break;
            case R.id.iv_add_vin:
            case R.id.iv_image_vin_container:
                chooseImage();
                break;
            case R.id.rl_check_brand:
                gotoPager(SelectCarBrandFragment.class,null);
                break;
            default:
                break;
        }
    }

    private void chooseImage() {
        Matisse.from(CheckGuaranteeFragment.this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))//MimeType.ofAll()
                //                .countable(true)
                .maxSelectable(1)
                .theme(R.style.Matisse_Zhihu)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                //                .capture(true)
                //                .captureStrategy(new CaptureStrategy(true,"com.bibicar.capture"))
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    List<Uri> mSelected;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);

            if (EmptyUtils.isNotEmpty(mSelected) && mSelected.size() != 0) {
                iv_add_vin.setVisibility(View.GONE);
                iv_image_vin_container.setImageURI(mSelected.get(0));
            }
        }
    }
}
