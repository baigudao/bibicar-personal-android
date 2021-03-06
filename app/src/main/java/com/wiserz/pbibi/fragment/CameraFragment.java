package com.wiserz.pbibi.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.SelectedPhotoAdapter;
import com.wiserz.pbibi.hardwrare.CameraManager;
import com.wiserz.pbibi.hardwrare.OnCameraListener;
import com.wiserz.pbibi.hardwrare.SensorControler;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.SquareCameraContainer;

import java.io.File;

/** 拍照
 * Created by gigabud on 15-12-23.
 */
public class CameraFragment extends BaseFragment implements View.OnTouchListener {


    private final static String TAG = "CameraFragment";

    private CameraManager mCameraManager;
    private SquareCameraContainer mCameraContainer;

    private boolean mUsingCamera;

    private SelectedPhotoAdapter mSelectedPhotoAdapter;
    private SelectPhotoFragment mSelectPhotoFragment;
    private RecyclerView recyclerView;
    private LinearLayoutManager llManager;
    private TextView tvPos;

    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private static final int EXTERNAL_STORAGE_REQ_CODE = 10;//权限请求码

    public BaseFragment setParentFragment(SelectPhotoFragment selectPhotoFragment) {
        mSelectPhotoFragment = selectPhotoFragment;
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_camera;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(DataManager.getInstance().getObject()!=null){
//            mSelectedPhotos=(ArrayList<File>) DataManager.getInstance().getObject();
//        }
        DataManager.getInstance().setObject(null);
    }

    @Override
    public void initView(final View view) {
        view.findViewById(R.id.btnBack).setOnClickListener(this);
        view.findViewById(R.id.btnTakePhoto).setOnClickListener(this);
        view.findViewById(R.id.btnFlashlight).setOnClickListener(this);
        view.findViewById(R.id.btnSwitchCamera).setOnClickListener(this);
        view.findViewById(R.id.tvNext).setOnClickListener(this);

        tvPos = (TextView) view.findViewById(R.id.tvPos);
        recyclerView = (RecyclerView) view.findViewById(R.id.selectPhotoView);
        llManager = new LinearLayoutManager(getActivity());
        llManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llManager);
        recyclerView.setAdapter(getSelectedPhotoAdapter());
        getSelectedPhotoAdapter().setDataList(mSelectPhotoFragment.getSelectedPhotos());

        requestPermission();
    }

    private SelectedPhotoAdapter getSelectedPhotoAdapter() {
        if (mSelectedPhotoAdapter == null) {
            mSelectedPhotoAdapter = new SelectedPhotoAdapter(getActivity());
            mSelectedPhotoAdapter.setOnPhotoDelete(new SelectedPhotoAdapter.OnPhotoOperator() {
                @Override
                public void onDelete(int position) {
                    mSelectPhotoFragment.getSelectedPhotos().remove(position+1);
                    showView();
                }

                @Override
                public void onItemClick(int position) {
                    tvPos.setText(getResources().getStringArray(R.array.car_img_type)[position]);
                }

            });
        }
        return mSelectedPhotoAdapter;
    }


    @Override
    public void onResume() {
        super.onResume();
        requestPermission();
        if (mSelectPhotoFragment.getCurrentItem() == 0) {
            if (DataManager.getInstance().getObject() != null && DataManager.getInstance().getData1() != null) {//编辑照片后返回
                String newPath = (String) DataManager.getInstance().getObject();
                int index = (int) DataManager.getInstance().getData1();
                mSelectPhotoFragment.getSelectedPhotos().put(index, new File(newPath));
                getSelectedPhotoAdapter().notifyDataSetChanged();
            } else if (DataManager.getInstance().getData8() != null) {//删除照片后返回
                int index = (int) DataManager.getInstance().getData8();
                mSelectPhotoFragment.getSelectedPhotos().remove(index);
                getSelectedPhotoAdapter().notifyDataSetChanged();
            }
            DataManager.getInstance().setObject(null);
            DataManager.getInstance().setData1(null);
            DataManager.getInstance().setData8(null);
        }
        showView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCameraContainer != null) {
            if (mCameraContainer.getParent() != null) {
                ((ViewGroup) mCameraContainer.getParent()).removeAllViews();
            }
            mCameraContainer.onStop();
        } else {
            if (mCameraManager != null) {
                mCameraManager.releaseActivityCamera();
            }
        }
        mUsingCamera = false;
        mCameraContainer = null;

    }

    private void requestPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(), CAMERA_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{CAMERA_PERMISSION}, EXTERNAL_STORAGE_REQ_CODE);
        } else {
            startCamera();
        }
    }

    private void startCamera() {
        mCameraManager = CameraManager.getInstance(getActivity());
        mCameraManager.setCameraDirection(CameraManager.CameraDirection.CAMERA_BACK);
        initCameraLayout();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQ_CODE: {
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result == PackageManager.PERMISSION_GRANTED) {
                            startCamera();
                            return;
                        }
                    }
                }
            }
            default:
                break;
        }
    }


    private void initCameraLayout() {
        RelativeLayout topLayout = (RelativeLayout) getView().findViewById(R.id.camera_surface_parent);
        topLayout.setVisibility(View.VISIBLE);

        getView().findViewById(R.id.focusView).setOnTouchListener(this);

        if (topLayout.getChildCount() > 0)
            topLayout.removeAllViews();

        if (mCameraContainer == null) {
            if (topLayout.getChildCount() > 0)
                topLayout.removeAllViews();
            mCameraContainer = new SquareCameraContainer(getActivity());
        }
        mCameraContainer.onStart();
        mCameraContainer.bindActivity(getActivity());
        if (mCameraContainer.getParent() == null) {
            RelativeLayout.LayoutParams layoutParam1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParam1.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            topLayout.addView(mCameraContainer, layoutParam1);
        }

        showSwitchCameraIcon();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mUsingCamera || mCameraContainer == null) {
            return true;
        }
        if (v.getId() == R.id.focusView) {
            mCameraContainer.onTouchEvent(event);
        }
        return true;
    }


    @Override
    public void onClick(final View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnFlashlight:
                if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    return;
                }
                mCameraManager.setLightStatus(mCameraManager.getLightStatus().next());
                showFlashIcon();
                break;
            case R.id.btnSwitchCamera:
                mCameraManager.setCameraDirection(mCameraManager.getCameraDirection().next());
                v.setClickable(false);
                mCameraContainer.switchCamera();
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setClickable(true);
                    }
                }, 500);
                showSwitchCameraIcon();
                break;
            case R.id.btnBack:
                DataManager.getInstance().setObject(mSelectPhotoFragment.getSelectedPhotos());
                getActivity().finish();
                break;
            case R.id.btnTakePhoto:
                if (mUsingCamera){
                    return;
                }

                if(mSelectPhotoFragment.getSelectedPhotos().get(getSelectedPhotoAdapter().getSelectIndex()+1)!=null){
                    ToastUtils.showShort(getString(R.string.only_a_photo));
                    return;
                }

                mUsingCamera = true;
                boolean isSuccessful = mCameraContainer.takePicture(new OnCameraListener() {
                    @Override
                    public void takePicture(final Bitmap bmp) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (bmp != null) {
                                    String path = CommonUtil.saveJpeg(bmp, getActivity());
                                    bmp.recycle();
                                    if (mSelectPhotoFragment.getCurrentPhotoNum() == -1) {
                                        DataManager.getInstance().setData9(path);
                                        getActivity().finish();
                                        return;
                                    }
                                    //将拍下来的照片保存到指定位置
                                    int  index = getSelectedPhotoAdapter().getSelectIndex();
                                    mSelectPhotoFragment.getSelectedPhotos().put(index+1,new File(path));
                                    if(index < 11){//自动跳到下一个,最大是11
                                        ++ index;
                                        getSelectedPhotoAdapter().setSelectIndex(index);
                                        llManager.scrollToPositionWithOffset(index,0);
                                        tvPos.setText(getResources().getStringArray(R.array.car_img_type)[index]);
                                    }
                                    getSelectedPhotoAdapter().setDataList(mSelectPhotoFragment.getSelectedPhotos());
                                    showView();
                                }
                                SensorControler.getInstance().unlockFocus();
                                mUsingCamera = false;
                                mCameraContainer.startPreview();
                            }
                        });
                    }

                });
                if (!isSuccessful) {
                    mUsingCamera = false;
                    mCameraContainer.startPreview();
                }
                break;
            case R.id.tvNext:
                DataManager.getInstance().setObject(mSelectPhotoFragment.getSelectedPhotos());
                if(!TextUtils.isEmpty(mSelectPhotoFragment.getFromClass())){
                    gotoPager(mSelectPhotoFragment.isPostNewCar()?PostNewCarFragment.class:PostSecondHandCarFragment.class,null);
                }
                getActivity().finish();
                break;

        }
    }

    private void showView() {
        if (mSelectPhotoFragment.getSelectedPhotos().size() > 0) {
            getView().findViewById(R.id.tvPhotoNum).setVisibility(View.VISIBLE);
            ((TextView) getView().findViewById(R.id.tvPhotoNum)).setText(String.valueOf(mSelectPhotoFragment.getSelectedPhotos().size()));
            getView().findViewById(R.id.tvNext).setVisibility(View.VISIBLE);
            mSelectPhotoFragment.setViewPagerCanScroll(false);
        } else {
            getView().findViewById(R.id.tvPhotoNum).setVisibility(View.GONE);
            getView().findViewById(R.id.tvNext).setVisibility(View.GONE);
            mSelectPhotoFragment.setViewPagerCanScroll(true);
        }
    }

    private void showSwitchCameraIcon() {
        if (mCameraManager.getCameraDirection() == CameraManager.CameraDirection.CAMERA_FRONT) {
            getView().findViewById(R.id.btnFlashlight).setVisibility(View.INVISIBLE);
        } else {
            getView().findViewById(R.id.btnFlashlight).setVisibility(View.VISIBLE);
            showFlashIcon();
        }
        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            getView().findViewById(R.id.btnSwitchCamera).setVisibility(View.VISIBLE);
        }
    }


    private void showFlashIcon() {
        if (mCameraManager.getLightStatus() == CameraManager.FlashLigthStatus.LIGHT_ON) {
            ((TextView) getView().findViewById(R.id.btnFlashlight)).setText(getString(R.string.open));
        } else {
            ((TextView) getView().findViewById(R.id.btnFlashlight)).setText(getString(R.string.close));
        }
    }
}
