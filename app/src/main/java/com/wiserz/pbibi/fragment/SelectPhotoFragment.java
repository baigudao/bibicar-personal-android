package com.wiserz.pbibi.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.CustomViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by huangzhifeng on 2017/11/14.
 */

public class SelectPhotoFragment extends BaseFragment {
    private ArrayList<BaseFragment> mFragmentList;

    private int mCurrentPhotoNum;  //现有多少张照片，当为-1时表示VIN照片
    private Map<Integer,File> mSelectedPhotos;

    private String from_class;
    private boolean is_post_new_car;
    private boolean select_album;

    public Map<Integer,File> getSelectedPhotos() {
        if (mSelectedPhotos == null) {
            mSelectedPhotos = new LinkedHashMap<Integer, File>();
        }
        return mSelectedPhotos;
    }

    public String getFromClass(){
        return from_class;
    }

    public boolean isPostNewCar(){
        return is_post_new_car;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_photo;
    }

    @Override
    protected void initView(View view) {
        from_class=getArguments().getString("from_class","");
        is_post_new_car=getArguments().getBoolean("is_post_new_car",false);
        select_album = getArguments().getBoolean("select_album",false);
        view.findViewById(R.id.tvRight).setOnClickListener(this);
        view.findViewById(R.id.tvLeft).setOnClickListener(this);
        mCurrentPhotoNum = 0;
        if (DataManager.getInstance().getObject() != null) {
            mCurrentPhotoNum = (int) DataManager.getInstance().getObject();
        }
        if(DataManager.getInstance().getData1()!=null){
            getSelectedPhotos().putAll((Map<Integer,File>) DataManager.getInstance().getData1());
        }
        DataManager.getInstance().setObject(null);
        initViewPager();
    }

//    private void initPicData(){
//        ArrayList<>
//    }


    public int getCurrentPhotoNum() {
        return mCurrentPhotoNum;
    }

    public int getCurrentItem() {
        return ((CustomViewPager) getView().findViewById(R.id.viewpager)).getCurrentItem();
    }

    private void initViewPager() {
        final ViewPager viewPager = (ViewPager) getView().findViewById(R.id.viewpager);
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
        }
        if (mFragmentList.isEmpty()) {
            mFragmentList.add(new CameraFragment().setParentFragment(this));//相机
            mFragmentList.add(new AlbumFragment().setParentFragment(this));//相册
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
                    ((TextView) getView().findViewById(R.id.tvCenter)).setText(getString(R.string.camera));
                } else {
                    getView().findViewById(R.id.tvLeft).setVisibility(View.VISIBLE);
                    getView().findViewById(R.id.tvRight).setVisibility(View.INVISIBLE);
                    ((TextView) getView().findViewById(R.id.tvCenter)).setText(getString(R.string.album));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if(select_album){
            viewPager.setCurrentItem(1);
        }else{
            viewPager.setCurrentItem(0);
        }

    }


    //设置是否能切换相机和相册
    public void setViewPagerCanScroll(boolean isCanScroll) {
        if (getView() == null) {
            return;
        }
        ((CustomViewPager) getView().findViewById(R.id.viewpager)).setCanScroll(isCanScroll);
        getView().findViewById(R.id.llBottomView).setVisibility(isCanScroll ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
