package com.wiserz.pbibi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import com.wiserz.pbibi.R;
import com.wiserz.pbibi.fragment.EditPhotoFragment;
import com.yalantis.ucrop.UCropActivity;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.view.CropImageView;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView;
import com.yalantis.ucrop.view.UCropView;

/**
 * Created by huangzhifeng on 2017/11/8.
 */

public class CropLayout extends RelativeLayout implements View.OnClickListener{

    public static final int DEFAULT_COMPRESS_QUALITY = 90;
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;

    public EditPhotoFragment.OnOperatorBmp mOnOperatorBmp;


    private UCropView mUCropView;
    private GestureCropImageView mGestureCropImageView;
    private OverlayView mOverlayView;

    private Bitmap.CompressFormat mCompressFormat = DEFAULT_COMPRESS_FORMAT;
    private int mCompressQuality = DEFAULT_COMPRESS_QUALITY;

    public CropLayout(Context context) {
        this(context,null);
    }
    public CropLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.crop_layout,this);
        initiateRootViews();
    }

    public void setOnOperatorBmp(EditPhotoFragment.OnOperatorBmp onOperatorBmp){
        mOnOperatorBmp=onOperatorBmp;
    }

    /**
     * This method extracts all data from the incoming intent and setups views properly.
     */
    public void setImageData(Uri inputUri,Uri outputUri) {
        processOptions();

        if (inputUri != null && outputUri != null) {
            try {
                mGestureCropImageView.setImageUri(inputUri, outputUri);
            } catch (Exception e) {
                setResultError(e);
   //             finish();
            }
        } else {
//            finish();
        }
    }

    /**
     * This method extracts {@link com.yalantis.ucrop.UCrop.Options #optionsBundle} from incoming intent
     * and setups Activity, {@link OverlayView} and {@link CropImageView} properly.
     */
    @SuppressWarnings("deprecation")
    private void processOptions() {
        mCompressFormat = DEFAULT_COMPRESS_FORMAT;

        mCompressQuality = UCropActivity.DEFAULT_COMPRESS_QUALITY;

        // Crop image view options
        mGestureCropImageView.setMaxBitmapSize(CropImageView.DEFAULT_MAX_BITMAP_SIZE);
        mGestureCropImageView.setMaxScaleMultiplier(CropImageView.DEFAULT_MAX_SCALE_MULTIPLIER);
        mGestureCropImageView.setImageToWrapCropBoundsAnimDuration(CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION);

        // Overlay view options
        mOverlayView.setFreestyleCropEnabled(true);

        mOverlayView.setDimmedColor(getResources().getColor(R.color.ucrop_color_default_dimmed));
        mOverlayView.setCircleDimmedLayer(OverlayView.DEFAULT_CIRCLE_DIMMED_LAYER);

        mOverlayView.setShowCropFrame(OverlayView.DEFAULT_SHOW_CROP_FRAME);
        mOverlayView.setCropFrameColor(getResources().getColor(R.color.ucrop_color_default_crop_frame));
        mOverlayView.setCropFrameStrokeWidth(getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_frame_stoke_width));

        mOverlayView.setShowCropGrid(OverlayView.DEFAULT_SHOW_CROP_GRID);
        mOverlayView.setCropGridRowCount(OverlayView.DEFAULT_CROP_GRID_ROW_COUNT);
        mOverlayView.setCropGridColumnCount(OverlayView.DEFAULT_CROP_GRID_COLUMN_COUNT);
        mOverlayView.setCropGridColor(getResources().getColor(R.color.ucrop_color_default_crop_grid));
        mOverlayView.setCropGridStrokeWidth(getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_grid_stoke_width));
        mGestureCropImageView.setTargetAspectRatio(CropImageView.SOURCE_IMAGE_ASPECT_RATIO);
    }

    private void initiateRootViews() {
        mUCropView = (UCropView) findViewById(R.id.ucrop);
        mGestureCropImageView = mUCropView.getCropImageView();
        mOverlayView = mUCropView.getOverlayView();

        mGestureCropImageView.setTransformImageListener(mImageListener);

        findViewById(R.id.tvCancel).setOnClickListener(this);
        findViewById(R.id.tvOk).setOnClickListener(this);
        findViewById(R.id.tvUndo).setOnClickListener(this);
    }

    private TransformImageView.TransformImageListener mImageListener = new TransformImageView.TransformImageListener() {
        @Override
        public void onRotate(float currentAngle) {

        }

        @Override
        public void onScale(float currentScale) {

        }

        @Override
        public void onLoadComplete() {
            mUCropView.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator());
        }

        @Override
        public void onLoadFailure(@NonNull Exception e) {
            setResultError(e);
   //         finish();
        }

    };


    private void resetRotation() {
        mGestureCropImageView.postRotate(-mGestureCropImageView.getCurrentAngle());
        mGestureCropImageView.setImageToWrapCropBounds();
    }

    private void rotateByAngle(int angle) {
        mGestureCropImageView.postRotate(angle);
        mGestureCropImageView.setImageToWrapCropBounds();
    }


    protected void cropAndSaveImage() {
        mGestureCropImageView.cropAndSaveImage(mCompressFormat, mCompressQuality, new BitmapCropCallback() {

            @Override
            public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {
                setResultUri(resultUri, mGestureCropImageView.getTargetAspectRatio(), offsetX, offsetY, imageWidth, imageHeight);
   //             finish();
            }

            @Override
            public void onCropFailure(@NonNull Throwable t) {
                setResultError(t);
  //              finish();
            }
        });
    }

    protected void setResultUri(Uri uri, float resultAspectRatio, int offsetX, int offsetY, int imageWidth, int imageHeight) {
//        setResult(RESULT_OK, new Intent()
//                .putExtra(UCrop.EXTRA_OUTPUT_URI, uri)
//                .putExtra(UCrop.EXTRA_OUTPUT_CROP_ASPECT_RATIO, resultAspectRatio)
//                .putExtra(UCrop.EXTRA_OUTPUT_IMAGE_WIDTH, imageWidth)
//                .putExtra(UCrop.EXTRA_OUTPUT_IMAGE_HEIGHT, imageHeight)
//                .putExtra(UCrop.EXTRA_OUTPUT_OFFSET_X, offsetX)
//                .putExtra(UCrop.EXTRA_OUTPUT_OFFSET_Y, offsetY)
//        );
    }

    protected void setResultError(Throwable throwable) {
 //       setResult(UCrop.RESULT_ERROR, new Intent().putExtra(UCrop.EXTRA_ERROR, throwable));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvCancel:
                if(mOnOperatorBmp!=null){
                    mOnOperatorBmp.onCancel();
                }
                break;
            case R.id.tvUndo:
                if(mOnOperatorBmp!=null){
                    mOnOperatorBmp.onUndo();
                }
                break;
            case R.id.tvOk:
                if(mOnOperatorBmp!=null){
                    mOnOperatorBmp.onFinish();
                }
                break;
        }
    }
}
