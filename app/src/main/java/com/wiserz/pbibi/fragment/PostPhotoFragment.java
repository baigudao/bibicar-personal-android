package com.wiserz.pbibi.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.UploadCarPhotoInfo;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.util.GifSizeFilter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jackie on 2017/9/7 17:13.
 * QQ : 971060378
 * Used as : 上传照片的页面
 */
public class PostPhotoFragment extends BaseFragment {

    private static final int REQUEST_CODE_CHOOSE = 55;

    private ImageView iv_add_photo_surface;
    private RecyclerView recyclerView_surface;

    private ImageView iv_add_photo_inner;
    private RecyclerView recyclerView_inner;

    private ImageView iv_add_photo_structure;
    private RecyclerView recyclerView_structure;

    private ImageView iv_add_photo_more;
    private RecyclerView recyclerView_more;

    private int flag;
    private String upload_token;
    private ArrayList<UploadCarPhotoInfo> uploadCarPhotoInfoArrayList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_post_photo;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("上传照片");

        uploadCarPhotoInfoArrayList = new ArrayList<>();

        iv_add_photo_surface = (ImageView) view.findViewById(R.id.iv_add_photo_surface);
        iv_add_photo_surface.setOnClickListener(this);
        recyclerView_surface = (RecyclerView) view.findViewById(R.id.recyclerView_surface);

        iv_add_photo_inner = (ImageView) view.findViewById(R.id.iv_add_photo_inner);
        iv_add_photo_inner.setOnClickListener(this);
        recyclerView_inner = (RecyclerView) view.findViewById(R.id.recyclerView_inner);

        iv_add_photo_structure = (ImageView) view.findViewById(R.id.iv_add_photo_structure);
        iv_add_photo_structure.setOnClickListener(this);
        recyclerView_structure = (RecyclerView) view.findViewById(R.id.recyclerView_structure);

        iv_add_photo_more = (ImageView) view.findViewById(R.id.iv_add_photo_more);
        iv_add_photo_more.setOnClickListener(this);
        recyclerView_more = (RecyclerView) view.findViewById(R.id.recyclerView_more);

        view.findViewById(R.id.btn_sure).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.iv_add_photo_surface:
                addPhotoForCar();
                flag = 0;
                break;
            case R.id.iv_add_photo_inner:
                addPhotoForCar();
                flag = 1;
                break;
            case R.id.iv_add_photo_structure:
                addPhotoForCar();
                flag = 2;
                break;
            case R.id.iv_add_photo_more:
                addPhotoForCar();
                flag = 3;
                break;
            case R.id.btn_sure:
                postPhoto();
                break;
            default:
                break;
        }
    }

    private void postPhoto() {
        if (EmptyUtils.isNotEmpty(upload_token) && EmptyUtils.isNotEmpty(uploadCarPhotoInfoArrayList) && uploadCarPhotoInfoArrayList.size() != 0) {
            DataManager.getInstance().setData8(uploadCarPhotoInfoArrayList);
            DataManager.getInstance().setData9(upload_token);
            goBack();
        }
    }

    @Override
    protected void initData() {
        super.initData();
        getTokenFromNet();
    }

    private void getTokenFromNet() {
        OkHttpUtils.post()
                .url(Constant.getUploadTokenUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                upload_token = jsonObjectData.optString("upload_token");
                            } else {
                                String code = jsonObject.optString("code");
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void addPhotoForCar() {
        Matisse.from(PostPhotoFragment.this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))//MimeType.ofAll()
                .countable(true)
                .maxSelectable(9)
                .theme(R.style.Matisse_Zhihu)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                //                .capture(true)
                //                .captureStrategy(new CaptureStrategy(true,"com.bibicar.capture"))
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    List<Uri> mSelected;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {//车辆照片类型 (1:外观 2:中控内饰 3:发动机及结构 4:更多细节)
            mSelected = Matisse.obtainResult(data);

            if (EmptyUtils.isNotEmpty(mSelected) && mSelected.size() != 0) {
                int mSize = mSelected.size();
                MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter(mSelected);
                switch (flag) {
                    case 0:
                        recyclerView_surface.setVisibility(View.VISIBLE);
                        iv_add_photo_surface.setVisibility(View.GONE);

                        recyclerView_surface.setAdapter(myRecyclerViewAdapter);
                        recyclerView_surface.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));

                        for (int i = 0; i < mSize; i++) {
                            UploadCarPhotoInfo uploadCarPhotoInfo = new UploadCarPhotoInfo();
                            uploadCarPhotoInfo.setFile_type(1);
                            uploadCarPhotoInfo.setFile(new File(CommonUtil.getRealFilePath(mContext, mSelected.get(i))));
                            uploadCarPhotoInfoArrayList.add(uploadCarPhotoInfo);
                        }
                        break;
                    case 1:
                        recyclerView_inner.setVisibility(View.VISIBLE);
                        iv_add_photo_inner.setVisibility(View.GONE);

                        recyclerView_inner.setAdapter(myRecyclerViewAdapter);
                        recyclerView_inner.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));

                        for (int i = 0; i < mSize; i++) {
                            UploadCarPhotoInfo uploadCarPhotoInfo = new UploadCarPhotoInfo();
                            uploadCarPhotoInfo.setFile_type(2);
                            uploadCarPhotoInfo.setFile(new File(CommonUtil.getRealFilePath(mContext, mSelected.get(i))));
                            uploadCarPhotoInfoArrayList.add(uploadCarPhotoInfo);
                        }
                        break;
                    case 2:
                        recyclerView_structure.setVisibility(View.VISIBLE);
                        iv_add_photo_structure.setVisibility(View.GONE);

                        recyclerView_structure.setAdapter(myRecyclerViewAdapter);
                        recyclerView_structure.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));

                        for (int i = 0; i < mSize; i++) {
                            UploadCarPhotoInfo uploadCarPhotoInfo = new UploadCarPhotoInfo();
                            uploadCarPhotoInfo.setFile_type(3);
                            uploadCarPhotoInfo.setFile(new File(CommonUtil.getRealFilePath(mContext, mSelected.get(i))));
                            uploadCarPhotoInfoArrayList.add(uploadCarPhotoInfo);
                        }
                        break;
                    case 3:
                        recyclerView_more.setVisibility(View.VISIBLE);
                        iv_add_photo_more.setVisibility(View.GONE);

                        recyclerView_more.setAdapter(myRecyclerViewAdapter);
                        recyclerView_more.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));

                        for (int i = 0; i < mSize; i++) {
                            UploadCarPhotoInfo uploadCarPhotoInfo = new UploadCarPhotoInfo();
                            uploadCarPhotoInfo.setFile_type(4);
                            uploadCarPhotoInfo.setFile(new File(CommonUtil.getRealFilePath(mContext, mSelected.get(i))));
                            uploadCarPhotoInfoArrayList.add(uploadCarPhotoInfo);
                        }
                        break;
                    default:
                        break;
                }
            } else {
                ToastUtils.showShort("选择图片失败");
            }
        }
    }

    private class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

        private List<Uri> mSelectedData;

        MyRecyclerViewAdapter(List<Uri> mSelected) {
            this.mSelectedData = mSelected;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = View.inflate(mContext, R.layout.item_all_image, null);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Glide.with(mContext)
                    .load(mSelectedData.get(position))
                    .placeholder(R.drawable.default_bg_ratio_1)
                    .error(R.drawable.default_bg_ratio_1)
                    .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                    .into(holder.iv_image_all);
            holder.iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelected.remove(position);
                    notifyDataSetChanged();
                    if (mSelected.size() == 0) {
                        LogUtils.e("删除为零了");
                        switch (flag) {
                            //                            case 0:
                            //                                iv_add_photo_surface.setVisibility(View.VISIBLE);
                            //                                break;
                            //                            case 1:
                            //                                iv_add_photo_inner.setVisibility(View.VISIBLE);
                            //                                break;
                            //                            case 2:
                            //                                iv_add_photo_structure.setVisibility(View.VISIBLE);
                            //                                break;
                            //                            case 3:
                            //                                iv_add_photo_more.setVisibility(View.VISIBLE);
                            //                                break;
                            //                            default:
                            //                                break;
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mSelectedData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView iv_image_all;
            private ImageView iv_close;

            ViewHolder(View itemView) {
                super(itemView);
                iv_image_all = (ImageView) itemView.findViewById(R.id.iv_image_all);
                iv_close = (ImageView) itemView.findViewById(R.id.iv_close);
            }
        }
    }
}
