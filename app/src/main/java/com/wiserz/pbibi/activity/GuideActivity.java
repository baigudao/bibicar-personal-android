package com.wiserz.pbibi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.Constant;

import java.util.ArrayList;

/**
 * Created by jackie on 2017/8/9 10:41.
 * QQ : 971060378
 * Used as : 引导界面
 */
public class GuideActivity extends Activity implements View.OnClickListener {

    private Context mContext;
    private Button btn_start_main;
    private LinearLayout ll_point_group;
    private ImageView iv_red_point;
    private ArrayList<ImageView> imageViews;
    private int leftMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("TESTLOG","GuideActivity");
        mContext = GuideActivity.this;
        initView();
    }

    private void initView() {
        BarUtils.setStatusBarColor(GuideActivity.this, getResources().getColor(R.color.background_color));
        //        BarUtils.hideStatusBar((Activity) mContext);//隐藏状态栏
        setContentView(R.layout.activity_guide);

        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
        btn_start_main = (Button) findViewById(R.id.btn_start_main);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
        iv_red_point = (ImageView) findViewById(R.id.iv_red_point);

        //准备资源
        int[] ids = new int[]{
                R.drawable.guide_1,
                R.drawable.guide_2,
                R.drawable.guide_3,
                R.drawable.guide_4
        };

        /**
         * 单位是像数
         * 把单位当成dp转成对应的像数
         */
        int width_dpi = SizeUtils.dp2px(10);

        imageViews = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(mContext);
            //设置背景
            imageView.setBackgroundResource(ids[i]);

            //添加到集合中
            imageViews.add(imageView);

            //创建点
            ImageView point = new ImageView(mContext);
            point.setBackgroundColor(getResources().getColor(R.color.pure_white_color));
            point.setBackgroundResource(R.drawable.point_normal);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width_dpi, width_dpi);
            if (i != 0) {
                //不包括第0个，所有的点距离左边有10个像数
                params.leftMargin = width_dpi;
            }
            point.setLayoutParams(params);
            //添加到线性布局里面
            ll_point_group.addView(point);
        }

        //设置ViewPager的适配器
        viewpager.setAdapter(new MyPagerAdapter());

        //根据View的生命周期，当视图执行到onLayout或者onDraw的时候，视图的高和宽，边距都有了
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());

        //得到屏幕滑动的百分比
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        btn_start_main.setOnClickListener(this);
    }

    //屏幕滑动的百分比
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当页面回调了会回调这个方法
         *
         * @param position             当前滑动页面的位置
         * @param positionOffset       页面滑动的百分比
         * @param positionOffsetPixels 滑动的像数
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            //两点间滑动距离对应的坐标 = 原来的起始位置 +  两点间移动的距离
            int leftMargin = (int) (position * leftMax + (positionOffset * leftMax));

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
            params.leftMargin = leftMargin;
            iv_red_point.setLayoutParams(params);
        }

        /**
         * 当页面被选中的时候，回调这个方法
         *
         * @param position 被选中页面的对应的位置
         */
        @Override
        public void onPageSelected(int position) {
            if (position == imageViews.size() - 1) {
                //最后一个页面
                btn_start_main.setVisibility(View.VISIBLE);
            } else {
                //其他页面
                btn_start_main.setVisibility(View.INVISIBLE);
            }
        }

        /**
         * 当ViewPager页面滑动状态发生变化的时候
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //全局布局的监听
    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {

            //执行不只一次，让它只执行一次
            iv_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(MyOnGlobalLayoutListener.this);

            //间距  = 第1个点距离左边的距离 - 第0个点距离左边的距离
            leftMax = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();
        }
    }

    //ViewPager的适配器类
    class MyPagerAdapter extends PagerAdapter {
        /**
         * 返回数据的总个数
         *
         * @return
         */
        @Override
        public int getCount() {
            return imageViews == null ? 0 : imageViews.size();
        }

        /**
         * 作用，getView
         *
         * @param container ViewPager
         * @param position  要创建页面的位置
         * @return 返回和创建与当前页面有关系的值
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            //一定要添加到容器中
            container.addView(imageView);
            return imageView;
        }

        /**
         * 判断
         *
         * @param view   当前创建的视图
         * @param object 上面instantiateItem返回的结果值
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 销毁页面
         *
         * @param container ViewPager
         * @param position  要销毁页面的位置
         * @param object    要销毁的页面
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //这个必须去掉，不去掉程序就会崩溃   super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_start_main) {
            SPUtils.getInstance().put(Constant.IS_ENTER_GUIDE_VIEW, true);
            startActivity(new Intent(mContext, MainActivity.class));
            finish();
        }
    }
}
