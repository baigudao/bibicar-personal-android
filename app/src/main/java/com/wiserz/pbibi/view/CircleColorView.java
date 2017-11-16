package com.wiserz.pbibi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.wiserz.pbibi.util.CommonUtil;

/**
 * Created by gigabud on 17-11-16.
 */

public class CircleColorView extends View {
    private Paint mPaint;
    private int mRadius;

    public CircleColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mRadius = CommonUtil.dip2px(context, 6);
    }

    public void setColor(int color) {
        getPaint().setColor(color);
        invalidate();
    }

    public Paint getPaint() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
        }
        return mPaint;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mRadius, mRadius, mRadius, getPaint());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mRadius * 2, mRadius * 2);
    }

}
