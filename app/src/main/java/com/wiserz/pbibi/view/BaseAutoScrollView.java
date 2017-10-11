package com.wiserz.pbibi.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wiserz.pbibi.R;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by jackie on 2017/8/10 13:26.
 * QQ : 971060378
 * Used as : xxx
 */
public abstract class BaseAutoScrollView<T> extends ListView implements AutoScrollData<T> {

    private ArrayList<T> mDataList = new ArrayList<>();
    private int scroll_Y;
    private int mScrollY;
    private Context mContext;
    private long mTimer = 1000;
    private int position = -1;
    private float mSize;
    private int mMax;
    private AutoScrollAdapter mAutoScrollAdapter = new AutoScrollAdapter();

    private Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // 开启轮播
            switchItem();
            handler.postDelayed(this, mTimer);//转速
        }
    };


    public BaseAutoScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        mScrollY = dip2px(getAdvertisementHeight());
        init();
    }

    public BaseAutoScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseAutoScrollView(Context context) {
        this(context, null);
    }


    @Override
    public String getImgUrl(T data) {
        return null;
    }

    @Override
    public String getTextTitle(T data) {
        return null;
    }

    @Override
    public String getTextInfo(T data) {
        return null;
    }

    private int dip2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    protected abstract int getAdvertisementHeight();

    private void init() {
        this.setDivider(null);
        this.setFastScrollEnabled(false);
        this.setDividerHeight(0);
        this.setEnabled(false);
    }

    private void switchItem() {
        if (position == -1) {
            scroll_Y = 0;
        } else {
            scroll_Y = mScrollY;
        }
        smoothScrollBy(scroll_Y, 500);
        setSelection(position);
        position++;
    }

    private class AutoScrollAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            final int count = mDataList == null ? 0 : mDataList.size();
            return count > 1 ? Integer.MAX_VALUE : count;
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position % mMax);
        }

        @Override
        public long getItemId(int position) {
            return position % mMax;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_lamp_layout, null);
                viewHolder.iv_user_photo = (ImageView) convertView.findViewById(R.id.iv_user_photo);
                viewHolder.tv_user = (TextView) convertView.findViewById(R.id.tv_user);
                viewHolder.tv_info = (TextView) convertView.findViewById(R.id.tv_info);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            T data = mDataList.get(position % mMax);
            initItemData(position, convertView, viewHolder, data);
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView iv_user_photo;
        TextView tv_user;
        TextView tv_info;
    }

    private void initItemData(final int position, View convertView, ViewHolder viewHolder, T data) {
        Glide.with(mContext)
                .load(getImgUrl(data))
                .placeholder(R.drawable.user_photo)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(viewHolder.iv_user_photo);
        viewHolder.tv_user.setText(getTextTitle(data));
        viewHolder.tv_info.setText(getTextInfo(data));

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(position % mMax);
            }
        });
    }

    public void setData(ArrayList<T> _datas) {
        mDataList.clear();
        mDataList.addAll(_datas);
        mMax = mDataList == null ? 0 : mDataList.size();
        this.setAdapter(mAutoScrollAdapter);
        mAutoScrollAdapter.notifyDataSetChanged();
    }

    public void setTextSize(float _size) {
        this.mSize = _size;
    }

    public void setTimer(long _time) {
        this.mTimer = _time;
    }

    public void start() {
        handler.postDelayed(runnable, 500);
    }

    public void stop() {
        handler.removeCallbacks(runnable);
    }

    /**
     * item的点击接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener _listener) {
        this.mOnItemClickListener = _listener;
    }
}