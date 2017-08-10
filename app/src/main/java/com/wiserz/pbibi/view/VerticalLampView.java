package com.wiserz.pbibi.view;

import android.content.Context;
import android.util.AttributeSet;

import com.wiserz.pbibi.bean.LampBean;

/**
 * Created by jackie on 2017/8/10 13:24.
 * QQ : 971060378
 * Used as : 竖直跑马灯
 */
public class VerticalLampView extends BaseAutoScrollView<LampBean> {

    public VerticalLampView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VerticalLampView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalLampView(Context context) {
        super(context);
    }

    @Override
    public String getImgUrl(LampBean data) {
        return data.imgUrl;
    }

    @Override
    public String getTextInfo(LampBean data) {
        return data.info;
    }

    @Override
    public String getTextTitle(LampBean data) {
        return data.title;
    }

    @Override
    protected int getAdvertisementHeight() {
        return 50;
    }
}
