package com.wiserz.pbibi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/8/23 21:46.
 * QQ : 971060378
 * Used as : 右侧导航栏字母列表
 */

public class SideBar extends View {
    /*
     * 默认的导航栏中字母的大小
     */
    private static final int DEFAULT_ALPHA_SIZE = 13;

    /*
     * 字母导航栏选择时，在中部显示所选择字母的TextView
     */
    private TextView mDialog;

    /*
     * 字母导航栏选择事件回调接口
     */
    private OnLetterSelectedListener mOnLetterSelectedListener;

    /*
     * 字母列表内容
     */
    private static final String[] ALPHA_LIST = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /*
     * 选中的元素
     */
    private int mChoiceIndex;

    /*
     * 画笔
     */
    private Paint mPaint;

    /*
     * 默认字母颜色
     */
    private static final int DEFAULT_CHARACTER = Color.rgb(33, 66, 99);
    /*
     * 选中字母颜色
     */
    private static final int CHIOCED_CHARACTER = Color.parseColor("#FF0000");

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true); // 抗锯齿，字体圆滑
        mPaint.setColor(DEFAULT_CHARACTER); // 设置字体颜色
        mPaint.setTypeface(Typeface.DEFAULT_BOLD); // 设置字体为粗体
        mPaint.setTextSize(sp2px(DEFAULT_ALPHA_SIZE));
    }

    /**
     * 字母导航栏选中事件回调接口
     *
     * @author Administrator
     */
    public interface OnLetterSelectedListener {
        void onLetterSelected(String letter);
    }

    /**
     * 设置字母导航栏字母被选中时的回调接口
     *
     * @param listener 接口
     */
    public void setOnLetterSelectedListener(OnLetterSelectedListener listener) {
        mOnLetterSelectedListener = listener;
    }

    public int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取整个SideBar的宽和高
        int width = getWidth();
        int height = getHeight();
        // 平均每行所分配到的高度值
        int singleHeight = height / ALPHA_LIST.length;
        for (int i = 0; i < ALPHA_LIST.length; i++) {
            // 计算每个字母的x坐标
            float xPos = (width - mPaint.measureText(ALPHA_LIST[i])) / 2;
            // 计算每个字母的y坐标（顶部预留一个singleHeight的高度）
            float yPos = singleHeight * (i + 1);
            // 如果当前字母是点击的字母，则改变画笔颜色
            if (i == mChoiceIndex) {
                mPaint.setColor(CHIOCED_CHARACTER);
            }
            // 绘制文本
            canvas.drawText(ALPHA_LIST[i], xPos, yPos, mPaint);
            // 绘制完成后，将画笔颜色修改为默认的颜色
            mPaint.setColor(DEFAULT_CHARACTER);
        }
    }

    /**
     * 触摸事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // 记录当前的触摸动作
        int action = event.getAction();
        // 获取当前的y坐标
        float yPos = event.getY();
        int oldChoice = mChoiceIndex;
        // 计算出当前的位置在哪个字母上面
        int c = (int) (yPos / getHeight() * ALPHA_LIST.length);
        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.TRANSPARENT);
                // 手指弹起，没有选中任何字母
                mChoiceIndex = -1;
                invalidate();
                if (mDialog != null) {
                    // 将中部显示字母的TextView隐藏
                    mDialog.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                // 设置字母导航栏被触摸时的背景
                setBackgroundResource(R.drawable.shape_sidebar_background);
                // 如果当前的选择不是之前的选择，触发字母选择事件的回调接口方法，改变中部TextView中的字母
                if (oldChoice != c) {
                    if (c >= 0 && c < ALPHA_LIST.length) {
                        // 将选中的字母传递给注册事件的监听者
                        if (mOnLetterSelectedListener != null) {
                            mOnLetterSelectedListener.onLetterSelected(ALPHA_LIST[c]);
                        }

                        if (mDialog != null) {
                            mDialog.setVisibility(View.VISIBLE);
                            mDialog.setText(ALPHA_LIST[c]);
                        }
                    }
                    mChoiceIndex = c;
                    invalidate();
                }
                break;
        }
        return true;
    }

    /**
     * 绑定中部显示字母的TextView控件
     *
     * @param tvDialog
     */
    public void setDialog(TextView tvDialog) {
        if (tvDialog == null) {
            throw new NullPointerException("tvDialog can not be null...");
        }
        this.mDialog = tvDialog;
    }
}
