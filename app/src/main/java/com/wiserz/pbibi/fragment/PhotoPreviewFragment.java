package com.wiserz.pbibi.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.ShowPicView;

/**
 * Created by gigabud on 16-6-21.
 */
public class PhotoPreviewFragment extends BaseFragment {

    private Bitmap mScreenBmp, mBmp;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_photo_preview;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            goBack();
            return;
        }
    }


    @Override
    protected void initView(View view) {
        getView().findViewById(R.id.tvCancel).setOnClickListener(this);
        getView().findViewById(R.id.tvOk).setOnClickListener(this);
        mBmp = (Bitmap) DataManager.getInstance().getObject();
        if (mBmp == null) {
            goBack();
            return;
        }
        DataManager.getInstance().setObject(null);
        final ShowPicView showPicView = (ShowPicView) view.findViewById(R.id.ivShowPic);
        view.findViewById(R.id.cutPhotoView).setVisibility(View.VISIBLE);
        showPicView.setImageBitmap(mBmp, true);

    }

    public void onStart() {
        super.onStart();
        ((BaseActivity) getActivity()).setScreenFull(true);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tvCancel) {
            DataManager.getInstance().setObject(null);
            getActivity().finish();
        } else if (id == R.id.tvOk) {
                View rl = getView().findViewById(R.id.rl);
                mScreenBmp = Bitmap.createBitmap(rl.getWidth(), rl.getHeight(), Bitmap.Config.ARGB_8888);
                rl.draw(new Canvas(mScreenBmp));
                final ShowPicView showPicView = (ShowPicView) getView().findViewById(R.id.ivShowPic);
                showPicView.setImageBitmap(mScreenBmp, false);
                mBmp.recycle();
                mBmp = null;

                int x = 0;
                int y = (mScreenBmp.getHeight() - mScreenBmp.getWidth()) / 2;
                Bitmap newBmp = Bitmap.createBitmap(mScreenBmp, x, y, mScreenBmp.getWidth(), mScreenBmp.getWidth());
                DataManager.getInstance().setObject(CommonUtil.saveJpeg(newBmp, getActivity()));
                newBmp.recycle();
                newBmp = null;
            getActivity().finish();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mScreenBmp != null && !mScreenBmp.isRecycled()) {
            mScreenBmp.recycle();
        }
        mScreenBmp = null;
    }

}
