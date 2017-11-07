package com.wiserz.pbibi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.wiserz.pbibi.activity.BaseActivity;

/**
 * Created by gigabud on 17-9-27.
 */

public class ShowPicView extends View {

    public float MIN_SCALE_SIZE = 0.3f;
    private float[] mOriginPoints;
    private float[] mPoints;
    private RectF mOriginContentRect;
    private RectF mContentRect;
    private RectF mViewRect;

    private float mLastPointX, mLastPointY;

    private int mCutLeft, mCutTop, mCutRight, mCutBottom;

    private Bitmap mBitmap;
    private Matrix mMatrix;

    private float mScaleSize = 1.0f;


    private boolean mIsDownInStricker;
    private boolean mIsCutBmp;


    /**
     * 模式 NONE：无 DRAG：拖拽. ZOOM:缩放
     */
    private enum MODE {
        NONE, DRAG, ZOOM
    }

    private MODE mode = MODE.NONE;// 默认模式

    public ShowPicView(Context context) {
        this(context, null);
    }

    public ShowPicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowPicView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mIsCutBmp = false;
    }

    public void setImageBitmap(Bitmap bitmap, boolean isCutBmp) {
        mIsCutBmp = isCutBmp;
        if (isCutBmp) {
            DisplayMetrics dis = ((BaseActivity) getContext()).getDisplaymetrics();
            mCutLeft = 0;
            mCutTop = (dis.heightPixels - dis.widthPixels) / 2;
            mCutRight = dis.widthPixels;
            mCutBottom = (dis.heightPixels + dis.widthPixels) / 2;
        }
        setImageBitmap(bitmap);
        MIN_SCALE_SIZE = mScaleSize;
    }

    public void setImageBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        if (mBitmap == null) {
            invalidate();
            return;
        }
        try {
            float px = mBitmap.getWidth();
            float py = mBitmap.getHeight();
            mPoints = new float[10];
            if (getWidth() == 0 || getHeight() == 0) {
                DisplayMetrics dis = ((BaseActivity) getContext()).getDisplaymetrics();
                if (mIsCutBmp) {
                    mScaleSize = Math.max(dis.widthPixels / px, dis.widthPixels / py);
                } else {
                    mScaleSize = Math.min(dis.widthPixels / px, dis.heightPixels / py);
                }
                mPoints[8] = dis.widthPixels * 0.5f;
                mPoints[9] = dis.heightPixels * 0.5f;
            } else {
                if (mIsCutBmp) {
                    mScaleSize = Math.max(getWidth() / px, getWidth() / py);
                } else {
                    mScaleSize = Math.min(getWidth() / px, getHeight() / py);
                }
                mPoints[8] = getWidth() * 0.5f;
                mPoints[9] = getHeight() * 0.5f;
            }
            mOriginPoints = new float[]{0, 0, px, 0, px, py, 0, py, px / 2, py / 2};
            mOriginContentRect = new RectF(0, 0, px, py);

            mContentRect = new RectF();
            mMatrix = new Matrix();
            mMatrix.postTranslate(mPoints[8] - px / 2, mPoints[9] - py / 2);
            mMatrix.postScale(mScaleSize, mScaleSize, mPoints[8], mPoints[9]);
            RectF rectF = new RectF();
            mMatrix.mapRect(rectF, mOriginContentRect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        invalidate();

    }


    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap == null || mMatrix == null || mContentRect == null) {
            return;
        }
        mMatrix.mapPoints(mPoints, mOriginPoints);
        mMatrix.mapRect(mContentRect, mOriginContentRect);
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (!mIsCutBmp) {
//            return super.dispatchTouchEvent(event);
//        }
        if (mContentRect == null) {
            return super.dispatchTouchEvent(event);
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN && mContentRect != null) {
            mIsDownInStricker = mContentRect.contains(event.getX(), event.getY());
        }
        if (!mIsDownInStricker) {
            return super.dispatchTouchEvent(event);
        }
        if (mViewRect == null) {
            mViewRect = new RectF(0f, 0f, getMeasuredWidth(), getMeasuredHeight());
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (mContentRect.contains(event.getX(), event.getY())) {
                    mLastPointX = event.getX();
                    mLastPointY = event.getY();
                    mode = MODE.DRAG;
                }
                break;
            // 多点触摸
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 2) {
                    mode = MODE.ZOOM;
                    mLastPointX = event.getX(1);
                    mLastPointY = event.getY(1);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mode == MODE.DRAG) {
                    performClick();
                }
                if (mIsCutBmp) {
                    float cX = event.getX() - mLastPointX;
                    float cY = event.getY() - mLastPointY;
                    if (mContentRect.left + cX > mCutLeft) {
                        cX = mCutLeft - mContentRect.left;
                    }
                    if (mContentRect.top + cY > mCutTop) {
                        cY = mCutTop - mContentRect.top;
                    }
                    if (mContentRect.right + cX < mCutRight) {
                        cX = mCutRight - mContentRect.right;
                    }
                    if (mContentRect.bottom + cY < mCutBottom) {
                        cY = mCutBottom - mContentRect.bottom;
                    }
                    mMatrix.postTranslate(cX, cY);
                    postInvalidate();
                }
                mLastPointX = 0;
                mLastPointY = 0;
                mode = MODE.NONE;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = MODE.NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == MODE.ZOOM) {
                    if (event.getPointerCount() == 2) {
                        float nowLenght = calculateLength(mLastPointX, mLastPointY);
                        float touchLenght = calculateLength(event.getX(1), event.getY(1));
                        if (Math.sqrt((nowLenght - touchLenght) * (nowLenght - touchLenght)) > 0.0f) {
                            float scale = touchLenght / nowLenght;
                            float nowsc = mScaleSize * scale;
                            if (nowsc >= MIN_SCALE_SIZE) {
                                mMatrix.postScale(scale, scale, mPoints[8], mPoints[9]);
                                mScaleSize = nowsc;
                            }
                        }
                        invalidate();
                        mLastPointX = event.getX(1);
                        mLastPointY = event.getY(1);
                    }
                } else if (mode == MODE.DRAG) { //拖动的操作
                    if (mIsCutBmp) {
                        float cX = event.getX() - mLastPointX;
                        float cY = event.getY() - mLastPointY;
                        if (mContentRect.left + cX > mCutLeft) {
                            cX = mCutLeft - mContentRect.left;
                        }
                        if (mContentRect.top + cY > mCutTop) {
                            cY = mCutTop - mContentRect.top;
                        }
                        if (mContentRect.right + cX < mCutRight) {
                            cX = mCutRight - mContentRect.right;
                        }
                        if (mContentRect.bottom + cY < mCutBottom) {
                            cY = mCutBottom - mContentRect.bottom;
                        }
                        mMatrix.postTranslate(cX, cY);
                        postInvalidate();
                        mLastPointX = event.getX();
                        mLastPointY = event.getY();
                    }
                }
                break;
        }
        return true;
    }


    private boolean canStickerMove(float cx, float cy) {
        float px = cx + mPoints[8];
        float py = cy + mPoints[9];
        return mViewRect.contains(px, py);
    }


    private float calculateLength(float x, float y) {
        float ex = x - mPoints[8];
        float ey = y - mPoints[9];
        return (float) Math.sqrt(ex * ex + ey * ey);
    }


}
