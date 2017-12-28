package com.wiserz.pbibi.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.CropLayout;
import com.wiserz.pbibi.view.DrawView;
import com.wiserz.pbibi.view.MosaicView;
import com.wiserz.pbibi.view.SelectColorView;

import java.io.File;

/**
 * Created by huangzhifeng on 2017/11/8.
 */

public class EditPhotoFragment extends BaseFragment {
    private String mPhotoPath, mCropPath;
    private int mEditIndex;
    private int key;

    private OperatorType mCurrentOperatorType;


    enum OperatorType {
        TYPE_NONE,
        TYPE_COMMON_DRAW,
        TYPE_MOSAIC_DRAW,
        TYPE_CUT;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_photo;
    }

    @Override
    protected void initView(View view) {
        mPhotoPath = (String) DataManager.getInstance().getObject();
        Log.i("TESTLOG","EDITPHOTO mPhotoPath="+mPhotoPath);
        if (TextUtils.isEmpty(mPhotoPath)) {
            return;
        }
        mEditIndex = (int) DataManager.getInstance().getData1();
        int size = (int) DataManager.getInstance().getData2();
        Object data3 = DataManager.getInstance().getData3();
        if(data3 != null){
            key = (int) data3;
        }
        ((TextView) view.findViewById(R.id.tvTitle)).setText((mEditIndex + 1) + "/" + size);
        DataManager.getInstance().setObject(null);
        DataManager.getInstance().setData1(null);
        DataManager.getInstance().setData2(null);
        DataManager.getInstance().setData3(null);
        view.findViewById(R.id.btnCancel).setOnClickListener(this);
        view.findViewById(R.id.btnDone).setOnClickListener(this);
        view.findViewById(R.id.ivDelete).setOnClickListener(this);
        view.findViewById(R.id.ivCrop).setOnClickListener(this);
        view.findViewById(R.id.ivMasaike).setOnClickListener(this);
        view.findViewById(R.id.ivPen).setOnClickListener(this);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap bmp = CommonUtil.getBitmapFromFile(new File(mPhotoPath), dm.widthPixels, dm.heightPixels);

        if(bmp == null)
            getActivity().finish();

        mCurrentOperatorType = OperatorType.TYPE_NONE;
        MosaicView mosaicView = (MosaicView) view.findViewById(R.id.mosaicView);
        DrawView drawView = (DrawView) view.findViewById(R.id.drawView);
        int[] wh = mosaicView.setImageBitmap(bmp);
        mosaicView.setOnDrawBmp(new OnDrawBmp() {
            @Override
            public void startDraw() {
                showOrHideOtherView(false);
            }

            @Override
            public void endDraw() {
                showOrHideOtherView(true);
            }
        });
        drawView.setViewWH(wh[0], wh[1]);
        drawView.setOnDrawBmp(new OnDrawBmp() {
            @Override
            public void startDraw() {
                showOrHideOtherView(false);
            }

            @Override
            public void endDraw() {
                showOrHideOtherView(true);
            }
        });
        initSelectColorView();
        resetOperatorView();
        ((BaseActivity) getActivity()).setScreenFull(true);
    }

    private void resetOperatorView() {
        MosaicView mosaicView = (MosaicView) getView().findViewById(R.id.mosaicView);
        DrawView drawView = (DrawView) getView().findViewById(R.id.drawView);
        if (mCurrentOperatorType == OperatorType.TYPE_NONE || mCurrentOperatorType == OperatorType.TYPE_CUT) {
            mosaicView.setEnable(false);
            drawView.setEnable(false);
            getView().findViewById(R.id.selectView).setVisibility(View.GONE);
        } else if (mCurrentOperatorType == OperatorType.TYPE_COMMON_DRAW) {
            mosaicView.setEnable(false);
            drawView.setEnable(true);
            getView().findViewById(R.id.selectView).setVisibility(View.VISIBLE);
            LinearLayout ll = (LinearLayout) getView().findViewById(R.id.llSelectColor);
            int count = ll.getChildCount();
            for (int i = 0; i < count; ++i) {
                ll.getChildAt(i).setVisibility(View.VISIBLE);
            }
        } else {
            mosaicView.setEnable(true);
            drawView.setEnable(false);
            getView().findViewById(R.id.selectView).setVisibility(View.VISIBLE);
            LinearLayout ll = (LinearLayout) getView().findViewById(R.id.llSelectColor);
            int count = ll.getChildCount();
            for (int i = 0; i < count; ++i) {
                ll.getChildAt(i).setVisibility(i < count - 1 ? View.INVISIBLE : View.VISIBLE);
            }
        }
    }

    private void initSelectColorView() {
        LinearLayout ll = (LinearLayout) getView().findViewById(R.id.llSelectColor);
        int count = ll.getChildCount();
        View view;
        for (int i = 0; i < count; ++i) {
            view = ll.getChildAt(i);
            if (i < count - 1) {
                view.setTag(i);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setColorSelected((int) view.getTag());
                    }
                });
                if (i == 2) {
                    setColorSelected(i);
                }
            } else {
                view.setOnClickListener(this);
            }
        }

    }

    private void setColorSelected(int selectedIndex) {
        LinearLayout ll = (LinearLayout) getView().findViewById(R.id.llSelectColor);
        int count = ll.getChildCount();
        View view;
        for (int i = 0; i < count; ++i) {
            view = ll.getChildAt(i);
            if (i == selectedIndex) {
                SelectColorView selectColorView = ((SelectColorView) view);
                selectColorView.setColorSelected(true);
                ((DrawView) getView().findViewById(R.id.drawView)).resetPaintColor(selectColorView.getPaintColor());
            } else if (i < count - 1) {
                ((SelectColorView) view).setColorSelected(false);
            }
        }
    }

    public void showOrHideOtherView(boolean isShow) {
        if (isShow) {
            getView().findViewById(R.id.topView).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.bottomView).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.selectView).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.topView).setVisibility(View.GONE);
            getView().findViewById(R.id.bottomView).setVisibility(View.GONE);
            getView().findViewById(R.id.selectView).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnCancel:
                goBack();
                break;
            case R.id.btnDone:
                Bitmap bitmap = getCropBmp();
                String newPath = CommonUtil.saveJpeg(bitmap, getActivity());
                bitmap.recycle();
                DataManager.getInstance().setObject(newPath);
                DataManager.getInstance().setData1(key);
                View rlOperator = getView().findViewById(R.id.rlOperator);
                rlOperator.requestLayout();
                new File(mPhotoPath).delete();
                goBack();
                break;
            case R.id.ivDelete:
                DataManager.getInstance().setData8(key);
                new File(mPhotoPath).delete();
                goBack();
                break;
            case R.id.ivCrop:
                mCurrentOperatorType = OperatorType.TYPE_CUT;
                resetOperatorView();
                final CropLayout layout = new CropLayout(getActivity());
                final Uri fileUri = Uri.fromFile(new File(getCropBmpFile()));
                layout.setImageData(fileUri, fileUri);
                ((RelativeLayout) getView().findViewById(R.id.rootView)).addView(layout);
                layout.setOnOperatorBmp(new OnOperatorBmp() {
                    @Override
                    public void onFinish() {
                        mCurrentOperatorType = OperatorType.TYPE_NONE;
                        ((RelativeLayout) getView().findViewById(R.id.rootView)).removeView(layout);
                        resetAfterCropBmp();
                    }

                    @Override
                    public void onCancel() {
                        mCurrentOperatorType = OperatorType.TYPE_NONE;
                        ((RelativeLayout) getView().findViewById(R.id.rootView)).removeView(layout);
                    }

                    @Override
                    public void onUndo() {
                        layout.setImageData(fileUri, fileUri);
                    }
                });
                break;
            case R.id.ivMasaike:
                if (mCurrentOperatorType == OperatorType.TYPE_MOSAIC_DRAW) {
                    mCurrentOperatorType = OperatorType.TYPE_NONE;
                } else {
                    mCurrentOperatorType = OperatorType.TYPE_MOSAIC_DRAW;
                }
                resetOperatorView();
                break;
            case R.id.ivPen:
                if (mCurrentOperatorType == OperatorType.TYPE_COMMON_DRAW) {
                    mCurrentOperatorType = OperatorType.TYPE_NONE;
                } else {
                    mCurrentOperatorType = OperatorType.TYPE_COMMON_DRAW;
                }
                resetOperatorView();
                break;
            case R.id.ivUndo:
                if (mCurrentOperatorType == OperatorType.TYPE_COMMON_DRAW) {
                    ((DrawView) getView().findViewById(R.id.drawView)).unDoDraw();
                } else if (mCurrentOperatorType == OperatorType.TYPE_MOSAIC_DRAW) {
                    ((MosaicView) getView().findViewById(R.id.mosaicView)).unDoDraw();
                }
                break;
        }
    }

    public void resetAfterCropBmp() {
        if (TextUtils.isEmpty(mCropPath)) {
            return;
        }
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap bmp = CommonUtil.getBitmapFromFile(new File(mCropPath), dm.widthPixels, dm.heightPixels);
        MosaicView mosaicView = ((MosaicView) getView().findViewById(R.id.mosaicView));
        int[] wh = mosaicView.setImageBitmap(bmp);
        mosaicView.requestLayout();
        DrawView drawView = ((DrawView) getView().findViewById(R.id.drawView));
        drawView.setViewWH(wh[0], wh[1]);
        drawView.requestLayout();
    }

    public interface OnOperatorBmp {
        public void onFinish();

        public void onCancel();

        public void onUndo();
    }

    public interface OnDrawBmp {
        public void startDraw();

        public void endDraw();
    }

    private Bitmap getCropBmp() {
        View view = getView().findViewById(R.id.rlOperator);
        int w = view.getWidth();
        int h = view.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        view.layout(0, 0, view.getWidth(), view.getHeight());
        view.draw(c);
        return bmp;
    }

    private String getCropBmpFile() {
        Bitmap bmp = getCropBmp();
        ((MosaicView) getView().findViewById(R.id.mosaicView)).clearPath();
        ((DrawView) getView().findViewById(R.id.drawView)).clearPath();
        mCropPath = CommonUtil.saveJpegByFileName(bmp, "temp.jpg", getActivity());
        bmp.recycle();
        return mCropPath;
    }

}
