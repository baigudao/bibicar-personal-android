package com.wiserz.pbibi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.wiserz.pbibi.R;

/**
 * 按比例的ImageView，以宽为基数
 */
public class RatioImageView extends AppCompatImageView {

    private float mRatio;

    private RectF mRoundRect;

    public RatioImageView(Context context) {
        super(context);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ImageViewRatio);
            mRatio = array.getFloat(R.styleable.ImageViewRatio_ratio, 1.0f);
            array.recycle();
        }
        if (mRatio <= 0.0f) {
            mRatio = 1.0f;
        }
    }


    public void setRatio(float ratio) {
        mRatio = ratio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childWidthSize * mRatio + 0.5f), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

//    @Override
//    public void setImageBitmap(Bitmap bmp) {
//        if (bmp != null) {
//            mRatio = bmp.getHeight() * 1.0f / bmp.getWidth();
//            requestLayout();
//            super.setImageBitmap(bmp);
//        }
//    }
}
