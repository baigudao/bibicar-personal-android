package com.wiserz.pbibi.fragment;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.hardwrare.CameraManager;
import com.wiserz.pbibi.hardwrare.OnCameraListener;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.SquareCameraContainer;

/**
 * Created by gigabud on 15-12-23.
 */
public class CameraFragment extends BaseFragment implements View.OnTouchListener {


    private final static String TAG = "CameraFragment";

    private CameraManager mCameraManager;
    private SquareCameraContainer mCameraContainer;

    private boolean mUsingCamera;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_camera;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            goBack();
            return;
        }
        DataManager.getInstance().setObject(null);
        ((BaseActivity) getActivity()).setScreenFull(true);
    }

    @Override
    public void initView(final View view) {
        view.findViewById(R.id.btnTakePhoto).setOnTouchListener(this);
        view.findViewById(R.id.btnFlashlight).setOnClickListener(this);
        view.findViewById(R.id.btnSwitchCamera).setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (DataManager.getInstance().getObject() != null) {
            getActivity().finish();
            return;
        }
        mCameraManager = CameraManager.getInstance(getActivity());
        mCameraManager.setCameraDirection(CameraManager.CameraDirection.CAMERA_BACK);
        initCameraLayout();
        showOrHideAllBtn(true);
        mUsingCamera = false;
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


    private void initCameraLayout() {
        RelativeLayout topLayout = (RelativeLayout) getView().findViewById(R.id.recorder_surface_parent);
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

    private void showOrHideAllBtn(final boolean isShow) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isShow) {
                    getView().findViewById(R.id.recorder_flashlight_parent1).setVisibility(View.VISIBLE);
                } else {
                    getView().findViewById(R.id.recorder_flashlight_parent1).setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mUsingCamera || mCameraContainer == null) {
            return true;
        }
        if (v.getId() == R.id.focusView) {
            mCameraContainer.onTouchEvent(event);
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                mUsingCamera = true;
                boolean isSuccessful = mCameraContainer.takePicture(new OnCameraListener() {
                    @Override
                    public void takePicture(final Bitmap bmp) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                if (bmp != null) {
//                                    mCameraContainer.stopPreview();
//                                    DataManager.getInstance().setObject(bmp);
//                                    Bundle bundle = new Bundle();
//                                    bundle.putInt(CameraFragment.USE_CAMERA_TYPE, mCameraUseType);
//                                    gotoPager(PhotoPreviewFragment.class, bundle);
//                                } else {
//                                    SensorControler.getInstance().unlockFocus();
//                                    mUsingCamera = false;
//                                    mCameraContainer.startPreview();
//                                    showOrHideAllBtn(true);
//                                }
                            }
                        });
                    }

                });
                if (!isSuccessful) {
                    mUsingCamera = false;
                    mCameraContainer.startPreview();
                    showOrHideAllBtn(true);
                }
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    public void onClick(final View v) {
        int id = v.getId();
        if (id == R.id.btnFlashlight) {
            if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                return;
            }
            mCameraManager.setLightStatus(mCameraManager.getLightStatus().next());
            showFlashIcon();
        } else if (id == R.id.btnSwitchCamera) {
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
            ((ImageButton) getView().findViewById(R.id.btnFlashlight)).setImageResource(R.drawable.camera_flashlight_on);
        } else {
            ((ImageButton) getView().findViewById(R.id.btnFlashlight)).setImageResource(R.drawable.camera_flashlight_off);
        }
    }
}
