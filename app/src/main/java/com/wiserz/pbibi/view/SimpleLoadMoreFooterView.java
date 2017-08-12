package com.wiserz.pbibi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/8/12 11:48.
 * QQ : 971060378
 * Used as : 加载更多的视图
 */
public class SimpleLoadMoreFooterView extends FrameLayout implements RefreshAndLoadMoreLayout.ILoadMoreView {

    private TextView tvHitText;
    private SpinKitView spinKitView;
    private View view;

    public SimpleLoadMoreFooterView(Context context) {
        this(context, null);
    }

    public SimpleLoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.default_load_more_footer, this);
        tvHitText = (TextView) view.findViewById(R.id.tv_hit_content);
        spinKitView = (SpinKitView) view.findViewById(R.id.spin_kit);
    }

    @Override
    public void reset() {
        spinKitView.setVisibility(INVISIBLE);
        tvHitText.setVisibility(INVISIBLE);
        tvHitText.setText("正在加载...");
    }

    @Override
    public void loading() {
        spinKitView.setVisibility(VISIBLE);
        tvHitText.setVisibility(VISIBLE);
        tvHitText.setText("正在加载...");
    }

    @Override
    public void loadComplete() {
        spinKitView.setVisibility(INVISIBLE);
        tvHitText.setVisibility(VISIBLE);
        tvHitText.setText("加载完成");
    }

    @Override
    public void loadFail() {
        spinKitView.setVisibility(INVISIBLE);
        tvHitText.setVisibility(VISIBLE);
        tvHitText.setText("加载失败,点击重新加载");
    }

    @Override
    public void loadNothing() {
        spinKitView.setVisibility(INVISIBLE);
        tvHitText.setVisibility(VISIBLE);
        tvHitText.setText("没有更多可以加载");
    }

    @Override
    public View getCanClickFailView() {
        return view;
    }
}

