package com.wiserz.pbibi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wiserz.pbibi.R;


/**
 * Created by 97106 on 2017/9/24.
 */

public class SelectStartEndView extends RelativeLayout implements View.OnTouchListener {

    private int mStartIndex;
    private int mEndIndex;
    private float mStartX;
    private int mStartLeft;
    private int mStart;
    private int mEnd;

    public SelectStartEndView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.select_start_end_view, this);
        findViewById(R.id.startView).setOnTouchListener(this);
        findViewById(R.id.endView).setOnTouchListener(this);
        mStart = mEnd = 0;
        resetView();
    }

    public void setStartEnd(int start, int end) {
        mStartIndex = start;
        mEndIndex = end;
    }

    public int getStart() {
        return mStart;
    }

    public int getEnd() {
        return mEnd;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mStartX = event.getRawX();
            mStartLeft = v.getLeft();
        } else {
            int detalX = (int) (event.getRawX() - mStartX);
            int left = mStartLeft + detalX;
            if (id == R.id.endView) {
                if (left < findViewById(R.id.startView).getRight()) {
                    left = findViewById(R.id.startView).getRight();
                } else if (left + v.getWidth() > getWidth()) {
                    left = getWidth() - v.getWidth();
                }
            } else if (id == R.id.startView) {
                if (left < 0) {
                    left = 0;
                } else if (left + v.getWidth() > findViewById(R.id.endView).getLeft()) {
                    left = findViewById(R.id.endView).getLeft() - v.getWidth();
                }
            }
            v.layout(left, v.getTop(), left + v.getWidth(), v.getBottom());
            resetView();
        }
        return true;
    }


    private void resetView() {
        View startView = findViewById(R.id.startView);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) startView.getLayoutParams();
        lp.leftMargin = startView.getLeft();
        View endView = findViewById(R.id.endView);
        lp = (RelativeLayout.LayoutParams) endView.getLayoutParams();
        lp.leftMargin = endView.getLeft() - startView.getRight();
        View progressLine = findViewById(R.id.progressView);
        progressLine.layout(startView.getRight(), progressLine.getTop(), endView.getLeft(), progressLine.getBottom());
        lp = (RelativeLayout.LayoutParams) progressLine.getLayoutParams();
        lp.width = endView.getRight() - startView.getLeft();
        lp.leftMargin = startView.getLeft();
        if (getWidth() > 0) {
            int totalLength = getWidth() - startView.getWidth() - endView.getWidth();
            if (totalLength == 0) {
                return;
            }
            mStart = mStartIndex + (int) (startView.getLeft() * (mEndIndex - mStartIndex) * 1.0 / totalLength + 0.5);
            mEnd = (int) ((endView.getLeft() - startView.getWidth()) * (mEndIndex - mStartIndex) * 1.0 / totalLength + 0.5);
            if (mEnd > mEndIndex) {
                mEnd = mEndIndex;
            }
            if (mStart < mStartIndex) {
                mStart = mStartIndex;
            }
            TextView tvPriceStart = (TextView) findViewById(R.id.tvStart);
            TextView tvPriceEnd = (TextView) findViewById(R.id.tvEnd);
            if (mStart == 0 && mEnd == mEndIndex) {
                tvPriceStart.setText("0");
                tvPriceEnd.setText(getResources().getString(R.string.no_limit));
            } else {
                tvPriceStart.setText(String.valueOf(mStart));
                tvPriceEnd.setText(String.valueOf(mEnd));
            }
        }
    }
}
