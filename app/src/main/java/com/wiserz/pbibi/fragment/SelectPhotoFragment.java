package com.wiserz.pbibi.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.CustomViewPager;

import java.util.ArrayList;

/**
 * Created by huangzhifeng on 2017/11/14.
 */

public class SelectPhotoFragment extends BaseFragment {
    private ArrayList<BaseFragment> mFragmentList;

    private int mCurrentPhotoNum;  //现有多少张照片，当为-1时表示VIN照片

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_photo;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.tvRight).setOnClickListener(this);
        view.findViewById(R.id.tvLeft).setOnClickListener(this);
        mCurrentPhotoNum = 0;
        if (DataManager.getInstance().getObject() != null) {
            mCurrentPhotoNum = (int) DataManager.getInstance().getObject();
        }
        DataManager.getInstance().setObject(null);
        initViewPager();
    }

    public int getCurrentPhotoNum() {
        return mCurrentPhotoNum;
    }

    private void initViewPager() {
        final ViewPager viewPager = (ViewPager) getView().findViewById(R.id.viewpager);
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
        }
        if (mFragmentList.isEmpty()) {
            mFragmentList.add(new CameraFragment().setParentFragment(this));
            mFragmentList.add(new AlbumFragment().setParentFragment(this));
        }
        viewPager.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    getView().findViewById(R.id.tvLeft).setVisibility(View.INVISIBLE);
                    getView().findViewById(R.id.tvRight).setVisibility(View.VISIBLE);
                } else {
                    getView().findViewById(R.id.tvLeft).setVisibility(View.VISIBLE);
                    getView().findViewById(R.id.tvRight).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);
    }


    public void setViewPagerCanScroll(boolean isCanScroll) {
        if (getView() == null) {
            return;
        }
        ((CustomViewPager) getView().findViewById(R.id.viewpager)).setCanScroll(isCanScroll);
        getView().findViewById(R.id.llBottomView).setVisibility(isCanScroll ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvLeft:
                ((CustomViewPager) getView().findViewById(R.id.viewpager)).setCurrentItem(0);
                break;
            case R.id.tvRight:
                ((CustomViewPager) getView().findViewById(R.id.viewpager)).setCurrentItem(1);
                break;
            default:
                break;
        }
    }
}
