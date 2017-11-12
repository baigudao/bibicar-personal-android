package com.wiserz.pbibi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.tencent.mm.opensdk.utils.Log;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.fragment.EditPhotoFragment;
import com.wiserz.pbibi.util.CommonUtil;

import java.util.ArrayList;

public class MosaicView extends View {

    private Bitmap mOrignBitmap, mTempBmp;
    private static final String ERROR_INFO = "bad bitmap to add mosaic";
    private final int BLOCK_SIZE;//马赛克的大小：BLOCK_SIZE*BLOCK_SIZE
    private int[] mSampleColors;
    private float mLastX, mLastY;
    private int mBitmapWidth, mBitmapHeight;
    private int[] mSrcBitmapPixs;//保留原图的像素数组
    private int[] mTempBitmapPixs;//用于马赛克的临时像素数组
    private int mRowCount;
    private int mColumnCount;
    private final int VALID_DISTANCE; //滑动的有效距离

    private int mViewWidth, mViewHeight;

    private boolean mIsEnable = false;

    private ArrayList<ArrayList<MosicPointInfo>> mMosicPointInfoList = new ArrayList<>();

    private ArrayList<MosicPointInfo> mMosicPointInfos;

    private EditPhotoFragment.OnDrawBmp mOnDrawBmp;

    public class MosicPointInfo {
        public MosaicPoint startPoint;
        public MosaicPoint endPoint;
    }

    public void setOnDrawBmp(EditPhotoFragment.OnDrawBmp onDrawBmp){
        mOnDrawBmp=onDrawBmp;
    }


    public MosaicView(Context context) {
        this(context, null);
    }

    public MosaicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MosaicView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        VALID_DISTANCE = CommonUtil.dip2px(getContext(), 2);
        BLOCK_SIZE = CommonUtil.dip2px(getContext(), 8);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(mTempBmp, 0, 0, null);
    }

    public void init(Bitmap bitmap) {
        if (bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0 || bitmap.isRecycled()) {
            throw new RuntimeException(ERROR_INFO);
        }
        mBitmapWidth = bitmap.getWidth();
        mBitmapHeight = bitmap.getHeight();
        mRowCount = (int) Math.ceil((float) mBitmapHeight / BLOCK_SIZE);
        mColumnCount = (int) Math.ceil((float) mBitmapWidth / BLOCK_SIZE);
        mSampleColors = new int[mRowCount * mColumnCount];

        int maxX = mBitmapWidth - 1;
        int maxY = mBitmapHeight - 1;
        mSrcBitmapPixs = new int[mBitmapWidth * mBitmapHeight];
        mTempBitmapPixs = new int[mBitmapWidth * mBitmapHeight];
        bitmap.getPixels(mSrcBitmapPixs, 0, mBitmapWidth, 0, 0, mBitmapWidth, mBitmapHeight);
        mTempBitmapPixs = mSrcBitmapPixs.clone();
//        bitmap.getPixels(mTempBitmapPixs, 0, mBitmapWidth, 0, 0,mBitmapWidth, mBitmapHeight);
        for (int row = 0; row < mRowCount; row++) {
            for (int column = 0; column < mColumnCount; column++) {
                int startX = column * BLOCK_SIZE;
                int startY = row * BLOCK_SIZE;
                mSampleColors[row * mColumnCount + column] = sampleBlock(mSrcBitmapPixs, startX, startY, BLOCK_SIZE, maxX, maxY);
            }
        }
        bitmap.setPixels(mSrcBitmapPixs, 0, mBitmapWidth, 0, 0, mBitmapWidth, mBitmapHeight);
    }

    public int[] setImageBitmap(Bitmap bitmap) {
        DisplayMetrics dis = ((BaseActivity) getContext()).getDisplaymetrics();
        float scaleX = dis.widthPixels * 1.0f / bitmap.getWidth();
        float scaleY = dis.heightPixels * 1.0f / bitmap.getHeight();
        Matrix matrix = new Matrix();
        if (scaleX > scaleY) {
            matrix.postScale(scaleY, scaleY);
        } else {
            matrix.postScale(scaleX, scaleX);
        }
        if(mOrignBitmap!=null && !mOrignBitmap.isRecycled()){
            mOrignBitmap.recycle();
        }
        mOrignBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
        mViewWidth=mOrignBitmap.getWidth();
        mViewHeight=mOrignBitmap.getHeight();
        if(mOrignBitmap!=bitmap) {
            bitmap.recycle();
        }
        mTempBmp = mOrignBitmap.copy(Bitmap.Config.ARGB_8888, true);
        init(mTempBmp);
        return new int[]{mViewWidth, mViewHeight};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    private int sampleBlock(int[] pxs, int startX, int startY, int blockSize, int maxX, int maxY) {
        int stopX = startX + blockSize - 1;
        int stopY = startY + blockSize - 1;
        stopX = Math.min(stopX, maxX);
        stopY = Math.min(stopY, maxY);
        int sampleColor = 0;
        int red = 0;
        int green = 0;
        int blue = 0;
        //将该块的所有点的颜色求平均值
        for (int y = startY; y <= stopY; y++) {
            int p = y * mBitmapWidth;
            for (int x = startX; x <= stopX; x++) {
                int color = pxs[p + x];
                red += Color.red(color);
                green += Color.green(color);
                blue += Color.blue(color);
            }
        }
        int sampleCount = (stopY - startY + 1) * (stopX - startX + 1);
        red /= sampleCount;
        green /= sampleCount;
        blue /= sampleCount;
        sampleColor = Color.rgb(red, green, blue);
        return sampleColor;
    }

    private void touchStart(float x, float y) {
        mLastX = x;
        mLastY = y;
    }

    private void touchMove(float x, float y) {
        if (Math.abs(x - mLastX) >= VALID_DISTANCE || Math.abs(y - mLastY) >= VALID_DISTANCE) {
            MosaicPoint startPoint = new MosaicPoint(mLastX, mLastY);
            MosaicPoint endPoint = new MosaicPoint(x, y);
            mosaic(startPoint, endPoint);
            MosicPointInfo mosicPointInfo = new MosicPointInfo();
            mosicPointInfo.startPoint = startPoint.clone();
            mosicPointInfo.endPoint = endPoint.clone();
            mMosicPointInfos.add(mosicPointInfo);
        }
        mLastX = x;
        mLastY = y;
    }

    public void clearPath(){
        mMosicPointInfoList.clear();
    }

    public synchronized void unDoDraw() {
        if (mMosicPointInfoList.isEmpty()) {
            return;
        }
        mMosicPointInfoList.remove(mMosicPointInfoList.size() - 1);
        long start = System.currentTimeMillis();
        if (mTempBmp != null && !mTempBmp.isRecycled()) {
            mTempBmp.recycle();
            mTempBmp = null;
        }
        mTempBmp = mOrignBitmap.copy(Bitmap.Config.ARGB_8888, true);
        mTempBitmapPixs = mSrcBitmapPixs.clone();
        for (ArrayList<MosicPointInfo> list : mMosicPointInfoList) {
            for (MosicPointInfo info : list) {
                mosaic(info.startPoint, info.endPoint);
            }
        }
        invalidate();
    }

    private void mosaic(MosaicPoint startPoint, MosaicPoint endPoint) {
        float startTouchX = startPoint.x;
        float startTouchY = startPoint.y;

        float endTouchX = endPoint.x;
        float endTouchY = endPoint.y;

        float minX = Math.min(startTouchX, endTouchX);
        float maxX = Math.max(startTouchX, endTouchX);

        int startIndexX = (int) minX / BLOCK_SIZE;
        int endIndexX = (int) maxX / BLOCK_SIZE;

        float minY = Math.min(startTouchY, endTouchY);
        float maxY = Math.max(startTouchY, endTouchY);

        int startIndexY = (int) minY / BLOCK_SIZE;
        int endIndexY = (int) maxY / BLOCK_SIZE;//确定矩形的判断范围
        for (int row = startIndexY; row <= endIndexY; row++) {
            for (int colunm = startIndexX; colunm <= endIndexX; colunm++) {
                Rect rect = new Rect(colunm * BLOCK_SIZE, row * BLOCK_SIZE, (colunm + 1) * BLOCK_SIZE, (row + 1) * BLOCK_SIZE);
                boolean intersectRect = isLineIntersectRect(startPoint.clone(), endPoint.clone(), rect);
                if (intersectRect) {//线段与直线相交
                    int rowMax = Math.min((row + 1) * BLOCK_SIZE, mBitmapHeight);
                    int colunmMax = Math.min((colunm + 1) * BLOCK_SIZE, mBitmapWidth);
                    for (int i = row * BLOCK_SIZE; i < rowMax; i++) {
                        for (int j = colunm * BLOCK_SIZE; j < colunmMax; j++) {
                            mTempBitmapPixs[i * mBitmapWidth + j] = mSampleColors[row * mColumnCount + colunm];
                        }
                    }
                }
            }
        }
        mTempBmp.setPixels(mTempBitmapPixs, 0, mBitmapWidth, 0, 0, mBitmapWidth, mBitmapHeight);
    }
    public void setEnable(boolean isEnable) {
        mIsEnable = isEnable;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsEnable) {
            return super.onTouchEvent(event);
        }
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(mOnDrawBmp!=null){
                    mOnDrawBmp.startDraw();
                }
                touchStart(Math.abs(x), Math.abs(y));
                mMosicPointInfos = new ArrayList<>();
                mMosicPointInfoList.add(mMosicPointInfos);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(Math.abs(x), Math.abs(y));
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if(mOnDrawBmp!=null){
                    mOnDrawBmp.endDraw();
                }
                invalidate();
                break;
        }
        return true;
    }

    //判断点在直线的左侧或上或右侧
    //返回值 ：-1：点在线段左侧 0：点在线段上 1：点在线段右侧
    public int pointAtLineLeftRight(MosaicPoint start, MosaicPoint end, MosaicPoint test) {
        start.x -= test.x;
        start.y -= test.y;
        end.x -= test.x;
        end.y -= test.y;

        float ret = start.x * end.y - start.y * end.x;
        if (ret == 0) {
            return 0;
        } else if (ret > 0) {
            return 1;
        } else if (ret < 0) {
            return -1;
        }
        return 0;
    }

    //判断两条线段是否相交
    public boolean isTwoLineIntersect(MosaicPoint start1, MosaicPoint end1, MosaicPoint start2, MosaicPoint end2) {
        int nLine1Start = pointAtLineLeftRight(start2.clone(), end2.clone(), start1.clone());
        int nLine1End = pointAtLineLeftRight(start2.clone(), end2.clone(), end1.clone());
        if (nLine1Start * nLine1End > 0)
            return false;
        int nLine2Start = pointAtLineLeftRight(start1.clone(), end1.clone(), start2.clone());
        int nLine2End = pointAtLineLeftRight(start1.clone(), end1.clone(), end2.clone());
        if (nLine2Start * nLine2End > 0)
            return false;
        return true;
    }

    //判断线段与矩形是否相交
    public boolean isLineIntersectRect(MosaicPoint start, MosaicPoint end, Rect rect) {
        // 直线的一端在矩形内 直线的两端都在矩形内
        if (isPointInRect(rect, start) || isPointInRect(rect, end)) {
            return true;
        }

        // 直线的两端都不在矩形内，判断直线是否与矩形的四条边相交
        if (isTwoLineIntersect(start, end, new MosaicPoint(rect.left, rect.top), new MosaicPoint(rect.left, rect.bottom))) {
            return true;
        }
        if (isTwoLineIntersect(start, end, new MosaicPoint(rect.left, rect.bottom), new MosaicPoint(rect.right, rect.bottom))) {
            return true;
        }

        if (isTwoLineIntersect(start, end, new MosaicPoint(rect.right, rect.bottom), new MosaicPoint(rect.right, rect.top))) {
            return true;
        }
        if (isTwoLineIntersect(start, end, new MosaicPoint(rect.left, rect.top), new MosaicPoint(rect.right, rect.top))) {
            return true;
        }
        return false;
    }

    private boolean isPointInRect(Rect rect, MosaicPoint test) {
        if (test.x >= (float) rect.left && test.x <= (float) rect.right && test.y >= (float) rect.top && test.y <= (float) rect.bottom) {
            return true;
        }
        return false;
    }

    public class MosaicPoint {

        public MosaicPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float x;
        public float y;

        public MosaicPoint clone() {
            return new MosaicPoint(x, y);
        }
    }
}
