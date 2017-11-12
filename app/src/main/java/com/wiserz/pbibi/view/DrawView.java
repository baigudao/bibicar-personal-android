package com.wiserz.pbibi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tencent.mm.opensdk.utils.Log;
import com.wiserz.pbibi.fragment.EditPhotoFragment;
import com.wiserz.pbibi.util.CommonUtil;

import java.util.ArrayList;

/**
 * Created by gigabud on 17-9-27.
 */

public class DrawView extends View {

    private PointF mPreviousPoint;
    private PointF mStartPoint;
    private PointF mCurrentPoint;

    private Paint mPaint;
    private Path mPath;

    private ArrayList<DrawLineInfo> mDrawPathsList;     //当前正在绘制的评论
    private DrawLineInfo mDrawLineInfo;
    public Canvas mCanvas;

    private int mViewWidth, mViewHeight;

    private boolean mIsEnable = false;

    private int mPaintColor = Color.RED;

    private Bitmap mDrawBmp;

    private EditPhotoFragment.OnDrawBmp mOnDrawBmp;

    public static class DrawLineInfo {   //手指一次按下抬起的绘画信息
        int color;
        ArrayList<DrawPath> drawPaths;
        boolean isDrawInBmp = false;
    }


    public static class DrawPath {
        Path path;
        boolean isDrawPoint;
        Point point;
    }

    public void setOnDrawBmp(EditPhotoFragment.OnDrawBmp onDrawBmp){
        mOnDrawBmp=onDrawBmp;
    }


    public DrawView(Context context) {
        this(context, null);
    }

    public DrawView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mIsEnable=true;
        mPath = new Path();
        mDrawPathsList = new ArrayList<>();
        setBackgroundColor(Color.TRANSPARENT);
        resetDrawPaint(mPaintColor);
    }


    public void setViewWH(int viewWidth,int viewHeight) {
        mViewWidth = viewWidth;
        mViewHeight = viewHeight;
    }

    public void clearPath(){
        mDrawPathsList.clear();
        if (mDrawBmp != null && !mDrawBmp.isRecycled()) {
            mDrawBmp.recycle();
            mDrawBmp = null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    public void resetDrawPaint(int paintColor) {
        int strokeWidth = CommonUtil.dip2px(getContext(),4);
        CornerPathEffect cornerPathEffect = new CornerPathEffect(CommonUtil.dip2px(getContext(), 5));
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
        }
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setPathEffect(cornerPathEffect);
        mPaint.setColor(paintColor);
    }

    public void setEnable(boolean isEnable) {
        mIsEnable = isEnable;
    }

    public boolean isEnable() {
        return mIsEnable;
    }

    public void resetPaintColor(int color) {
        mPaintColor = color;
    }

    public ArrayList<DrawLineInfo> getDrawPathsList() {
        return mDrawPathsList;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsEnable) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(mOnDrawBmp!=null){
                    mOnDrawBmp.startDraw();
                }
                mDrawLineInfo = new DrawLineInfo();
                mDrawLineInfo.color = mPaintColor;
                resetDrawPaint(mPaintColor);
                mDrawLineInfo.drawPaths = new ArrayList<>();
                mCurrentPoint = new PointF(event.getX(), event.getY());
                mPreviousPoint = mCurrentPoint;
                mStartPoint = mPreviousPoint;
                drawLine(mPreviousPoint, mStartPoint, mCurrentPoint, true);
                break;
            case MotionEvent.ACTION_MOVE:
                mStartPoint = mPreviousPoint;
                mPreviousPoint = mCurrentPoint;
                mCurrentPoint = new PointF(event.getX(), event.getY());
                drawLine(mPreviousPoint, mStartPoint, mCurrentPoint, false);
                break;
            case MotionEvent.ACTION_UP:
                if(mOnDrawBmp!=null){
                    mOnDrawBmp.endDraw();
                }
                mStartPoint = mPreviousPoint;
                mPreviousPoint = mCurrentPoint;
                mCurrentPoint = new PointF(event.getX(), event.getY());
                drawLine(mPreviousPoint, mStartPoint, mCurrentPoint, false);
                savePaintOperator();
                break;
            default:
                break;
        }
        return true;
    }


    public synchronized void unDoDraw() {
        if (isDrawPathsListEmpty()) {
            return;
        }
        mDrawPathsList.remove(mDrawPathsList.size() - 1);
        resetDrawView();
    }

    public void resetDrawView() {
        if (mDrawBmp != null && !mDrawBmp.isRecycled()) {
            mDrawBmp.recycle();
            mDrawBmp = null;
        }
        drawPathListInBitmap(mDrawPathsList);
        invalidate();
    }

    private void drawPathInBitmap(DrawPath dp) {
        if (dp == null) {
            return;
        }
        if (mDrawBmp == null || mDrawBmp.isRecycled()) {
            mDrawBmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mDrawBmp);
            mCanvas.drawBitmap(mDrawBmp, 0, 0, null);
        }

        if (dp.isDrawPoint) {
            mCanvas.drawPoint(dp.point.x, dp.point.y,
                    mPaint);
        } else {
            mCanvas.drawPath(dp.path, mPaint);
        }
    }

    public void drawPathListInBitmap(ArrayList<DrawLineInfo> drawPathList) {
        if (drawPathList != null && !drawPathList.isEmpty()) {
            if (mDrawBmp == null || mDrawBmp.isRecycled()) {
                mDrawBmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                mCanvas = new Canvas(mDrawBmp);
                mCanvas.drawBitmap(mDrawBmp, 0, 0, null);
                for (DrawLineInfo drawLineInfo : drawPathList) {
                    drawLineInfo.isDrawInBmp = false;
                }
            }

            for (DrawLineInfo drawLineInfo : drawPathList) {
                if (isDrawLineInfoInvalid(drawLineInfo) || drawLineInfo.isDrawInBmp) {
                    continue;
                }
                resetDrawPaint(drawLineInfo.color);
                drawLineInfo.isDrawInBmp = true;
                for (DrawPath dp : drawLineInfo.drawPaths) {
                    if (dp.isDrawPoint) {
                        mCanvas.drawPoint(dp.point.x, dp.point.y,
                                mPaint);
                    } else {
                        mCanvas.drawPath(dp.path, mPaint);
                    }
                }
            }
            resetDrawPaint(mPaintColor);
        }
    }

    public boolean isDrawPathsListEmpty() {
        if (mDrawPathsList == null || mDrawPathsList.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDrawed() {
        if ((mDrawPathsList == null || mDrawPathsList.isEmpty()) && isDrawLineInfoInvalid(mDrawLineInfo)) {
            return false;
        } else {
            return true;
        }
    }


    /*
     * 保存住Operator方式，以便撤销
     */
    private void savePaintOperator() {
        if (!isDrawLineInfoInvalid(mDrawLineInfo)) {
            mDrawPathsList.add(mDrawLineInfo);
            mDrawLineInfo.isDrawInBmp = true;
            //drawPathListInBitmap(mDrawPathsList);
        }
        mDrawLineInfo = null;
    }

    public boolean isDrawLineInfoInvalid(DrawLineInfo drawLineInfo) {   //判断DrawLineInfo是否无效
        return drawLineInfo == null || drawLineInfo.drawPaths == null || drawLineInfo.drawPaths.isEmpty();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawBmp != null && !mDrawBmp.isRecycled()) {
            canvas.drawBitmap(mDrawBmp, 0, 0, null);
        }
    }


    private void drawLine(PointF previous, PointF start, PointF end, boolean isDrawPoint) {
        DrawPath dp = new DrawPath();
        dp.point = new Point((int) (end.x + 0.5), (int) (end.y + 0.5));
        if (isDrawPoint) {
            dp.isDrawPoint = true;
        } else {
            PointF mid1 = midPoint(previous, start);
            PointF mid2 = midPoint(end, previous);
            mPath = new Path();
            mPath.reset();
            mPath.moveTo(mid1.x, mid1.y);
            mPath.quadTo(previous.x, previous.y, mid2.x, mid2.y);
            dp.path = mPath;
            dp.isDrawPoint = false;
        }
        mDrawLineInfo.drawPaths.add(dp);
        drawPathInBitmap(dp);
        invalidate();
    }

    private PointF midPoint(PointF p1, PointF p2) {
        return new PointF((p1.x + p2.x) * 0.5f, (p1.y + p2.y) * 0.5f);
    }

}
