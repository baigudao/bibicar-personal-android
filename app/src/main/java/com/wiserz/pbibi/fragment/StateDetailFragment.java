package com.wiserz.pbibi.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.wiserz.pbibi.R;


/**
 * Created by jackie on 2017/9/13 11:47.
 * QQ : 971060378
 * Used as : 动态详情的页面
 */
public class StateDetailFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_state_detail;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("动态详情");
        ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
        iv_image.setVisibility(View.VISIBLE);
        iv_image.setScaleType(ImageView.ScaleType.CENTER);
        ViewGroup.LayoutParams layoutParams = iv_image.getLayoutParams();
        layoutParams.width = 60;
        layoutParams.height = 20;
        iv_image.setLayoutParams(layoutParams);
        iv_image.setImageResource(R.drawable.report_selector);
        iv_image.setOnClickListener(this);

        view.findViewById(R.id.rl_share).setOnClickListener(this);
        view.findViewById(R.id.rl_comment).setOnClickListener(this);
        view.findViewById(R.id.rl_like).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.iv_image:
                ToastUtils.showShort("举报or删除");
                break;
            case R.id.rl_share:
                ToastUtils.showShort("分享");
                break;
            case R.id.rl_comment:
                ToastUtils.showShort("评论");
                break;
            case R.id.rl_like:
                ToastUtils.showShort("喜欢");
                break;
            default:
                break;
        }
    }
}
