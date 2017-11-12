package com.wiserz.pbibi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.CommonUtil;

/**
 * Created by huangzhifeng on 2017/11/12.
 */

public class SelectColorView extends View {

    private Paint mPaint;
    private boolean mIsSelected;
    private int mColor=Color.RED;

    public SelectColorView(Context context) {
        this(context, null);
    }

    public SelectColorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectColorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(attrs!=null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SelectColorView);
            mColor = ta.getColor(R.styleable.SelectColorView_circle_color, Color.RED);
        }
    }

    public void setColorSelected(boolean isSelected){
        mIsSelected=isSelected;
        invalidate();
    }

    public int getPaintColor(){
        return mColor;
    }

    private Paint getPaint(){
        if(mPaint==null){
            mPaint = new Paint();
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
        }
        return mPaint;
    }

    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        float radius;
        if(mIsSelected){
            radius=getWidth()*0.5f;
        }else{
            radius=getWidth()*0.3333f;
        }
        float centerX=getWidth()*0.5f;
        float centerY=getHeight()*0.5f;
        getPaint().setColor(Color.WHITE);
        canvas.drawCircle(centerX,centerY,radius,getPaint());
        getPaint().setColor(mColor);
        canvas.drawCircle(centerX,centerY,radius*0.75f,getPaint());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(CommonUtil.dip2px(getContext(),30), CommonUtil.dip2px(getContext(),30));
    }
}
