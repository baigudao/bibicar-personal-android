package com.wiserz.pbibi.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.view.CropLayout;

import java.io.File;

/**
 * Created by huangzhifeng on 2017/11/8.
 */

public class EditPhotoFragment extends BaseFragment {
    private String mPhotoPath;
    private Bitmap mOrignBmp;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_photo;
    }

    @Override
    protected void initView(View view) {
        mPhotoPath=getArguments().getString("photoPath","");
        if(TextUtils.isEmpty(mPhotoPath)){
            return;
        }
        ((TextView) view.findViewById(R.id.tvTitle)).setText(getArguments().getString("photoIndex",""));
        view.findViewById(R.id.btnCancel).setOnClickListener(this);
        view.findViewById(R.id.btnDone).setOnClickListener(this);
        view.findViewById(R.id.ivDelete).setOnClickListener(this);
        view.findViewById(R.id.ivCrop).setOnClickListener(this);
        view.findViewById(R.id.ivMasaike).setOnClickListener(this);
        view.findViewById(R.id.ivPen).setOnClickListener(this);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mOrignBmp = CommonUtil.getBitmapFromFile(new File(mPhotoPath), dm.widthPixels, dm.heightPixels);
        ((ImageView) view.findViewById(R.id.ivPhoto)).setImageBitmap(mOrignBmp);
        ((BaseActivity) getActivity()).setScreenFull(true);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.tvCancel:
                goBack();
                break;
            case R.id.btnDone:
                break;
            case R.id.ivDelete:
                break;
            case R.id.ivCrop:
                final CropLayout layout=new CropLayout(getActivity());
                Uri fileUri=Uri.fromFile(new File(mPhotoPath));
                layout.setImageData(fileUri,fileUri);
                ((RelativeLayout) getView().findViewById(R.id.rootView)).addView(layout);
                layout.setOnOperatorBmp(new OnOperatorBmp() {
                    @Override
                    public void onFinish() {
                        ((RelativeLayout) getView().findViewById(R.id.rootView)).removeView(layout);
                    }

                    @Override
                    public void onCancel() {
                        ((RelativeLayout) getView().findViewById(R.id.rootView)).removeView(layout);
                    }

                    @Override
                    public void onUndo() {
                        ((RelativeLayout) getView().findViewById(R.id.rootView)).removeView(layout);
                    }
                });
                break;
            case R.id.ivMasaike:
                break;
            case R.id.ivPen:
                break;
        }
    }

    public interface OnOperatorBmp{
        public void onFinish();
        public void onCancel();
        public void onUndo();
    }
}
