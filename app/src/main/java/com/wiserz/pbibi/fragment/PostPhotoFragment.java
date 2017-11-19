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

    private MyRecyclerViewAdapter0 myRecyclerViewAdapter0;
    private MyRecyclerViewAdapter1 myRecyclerViewAdapter1;
    private MyRecyclerViewAdapter2 myRecyclerViewAdapter2;
    private MyRecyclerViewAdapter3 myRecyclerViewAdapter3;

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
        //        LogUtils.e(myRecyclerViewAdapter0.getmSelectedData0().size() + "个0");
        //        LogUtils.e(myRecyclerViewAdapter1.getmSelectedData1().size() + "个1");
        //        LogUtils.e(myRecyclerViewAdapter2.getmSelectedData2().size() + "个2");
        //        LogUtils.e(myRecyclerViewAdapter3.getmSelectedData3().size() + "个3");
        ArrayList<Uri> uris0 = myRecyclerViewAdapter0.getmSelectedData0();
        int size0 = uris0.size();
        if (EmptyUtils.isNotEmpty(uris0) && size0 != 0) {
            for (int i = 0; i < size0; i++) {
                UploadCarPhotoInfo uploadCarPhotoInfo = new UploadCarPhotoInfo();
                uploadCarPhotoInfo.setFile_type(1);
                uploadCarPhotoInfo.setFile(new File(CommonUtil.getRealFilePath(mContext, uris0.get(i))));
                uploadCarPhotoInfoArrayList.add(uploadCarPhotoInfo);
            }
        }
        ArrayList<Uri> uris1 = myRecyclerViewAdapter1.getmSelectedData1();
        int size1 = uris1.size();
        if (EmptyUtils.isNotEmpty(uris1) && size1 != 0) {
            for (int i = 0; i < size1; i++) {
                UploadCarPhotoInfo uploadCarPhotoInfo = new UploadCarPhotoInfo();
                uploadCarPhotoInfo.setFile_type(2);
                uploadCarPhotoInfo.setFile(new File(CommonUtil.getRealFilePath(mContext, uris1.get(i))));
                uploadCarPhotoInfoArrayList.add(uploadCarPhotoInfo);
            }
        }
        ArrayList<Uri> uris2 = myRecyclerViewAdapter2.getmSelectedData2();
        int size2 = uris2.size();
        if (EmptyUtils.isNotEmpty(uris2) && size2 != 0) {
            for (int i = 0; i < size2; i++) {
                UploadCarPhotoInfo uploadCarPhotoInfo = new UploadCarPhotoInfo();
                uploadCarPhotoInfo.setFile_type(3);
                uploadCarPhotoInfo.setFile(new File(CommonUtil.getRealFilePath(mContext, uris2.get(i))));
                uploadCarPhotoInfoArrayList.add(uploadCarPhotoInfo);
            }
        }
        ArrayList<Uri> uris3 = myRecyclerViewAdapter3.getmSelectedData3();
        int size3 = uris3.size();
        if (EmptyUtils.isNotEmpty(uris3) && size3 != 0) {
            for (int i = 0; i < size3; i++) {
                UploadCarPhotoInfo uploadCarPhotoInfo = new UploadCarPhotoInfo();
                uploadCarPhotoInfo.setFile_type(4);
                uploadCarPhotoInfo.setFile(new File(CommonUtil.getRealFilePath(mContext, uris3.get(i))));
                uploadCarPhotoInfoArrayList.add(uploadCarPhotoInfo);
            }
        }

        if (EmptyUtils.isNotEmpty(upload_token) && EmptyUtils.isNotEmpty(uploadCarPhotoInfoArrayList) && uploadCarPhotoInfoArrayList.size() != 0) {
            DataManager.getInstance().setData8(uploadCarPhotoInfoArrayList);
            DataManager.getInstance().setData9(upload_token);
            goBack();
        } else {
            ToastUtils.showShort("请添加车辆照片");
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
                                ToastUtils.showShort("" + msg);
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

    ArrayList<Uri> mSelected;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {//车辆照片类型 (1:外观 2:中控内饰 3:发动机及结构 4:更多细节)
            mSelected = (ArrayList<Uri>) Matisse.obtainResult(data);

            if (EmptyUtils.isNotEmpty(mSelected) && mSelected.size() != 0) {
                int mSize = mSelected.size();
                switch (flag) {
                    case 0:
                        if (EmptyUtils.isNotEmpty(mSelected) && mSize != 0) {
                            recyclerView_surface.setVisibility(View.VISIBLE);
                            iv_add_photo_surface.setVisibility(View.GONE);

                            myRecyclerViewAdapter0 = new MyRecyclerViewAdapter0(mSelected);
                            recyclerView_surface.setAdapter(myRecyclerViewAdapter0);
                            recyclerView_surface.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));
                        }
                        break;
                    case 1:
                        if (EmptyUtils.isNotEmpty(mSelected) && mSize != 0) {
                            recyclerView_inner.setVisibility(View.VISIBLE);
                            iv_add_photo_inner.setVisibility(View.GONE);

                            myRecyclerViewAdapter1 = new MyRecyclerViewAdapter1(mSelected);
                            recyclerView_inner.setAdapter(myRecyclerViewAdapter1);
                            recyclerView_inner.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));
                        }
                        break;
                    case 2:
                        if (EmptyUtils.isNotEmpty(mSelected) && mSize != 0) {
                            recyclerView_structure.setVisibility(View.VISIBLE);
                            iv_add_photo_structure.setVisibility(View.GONE);

                            myRecyclerViewAdapter2 = new MyRecyclerViewAdapter2(mSelected);
                            recyclerView_structure.setAdapter(myRecyclerViewAdapter2);
                            recyclerView_structure.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));
                        }
                        break;
                    case 3:
                        if (EmptyUtils.isNotEmpty(mSelected) && mSize != 0) {
                            recyclerView_more.setVisibility(View.VISIBLE);
                            iv_add_photo_more.setVisibility(View.GONE);

                            myRecyclerViewAdapter3 = new MyRecyclerViewAdapter3(mSelected);
                            recyclerView_more.setAdapter(myRecyclerViewAdapter3);
                            recyclerView_more.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));
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

    private class MyRecyclerViewAdapter0 extends RecyclerView.Adapter<MyRecyclerViewAdapter0.ViewHolder> {

        private ArrayList<Uri> mSelectedData0;

        public ArrayList<Uri> getmSelectedData0() {
            return mSelectedData0;
        }

        MyRecyclerViewAdapter0(ArrayList<Uri> mSelectedData) {
            this.mSelectedData0 = mSelectedData;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = View.inflate(mContext, R.layout.item_all_image, null);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Glide.with(mContext)
                    .load(mSelectedData0.get(position))
                    .placeholder(R.drawable.default_bg_ratio_1)
                    .error(R.drawable.default_bg_ratio_1)
                    .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                    .into(holder.iv_image_all);

            holder.iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedData0.remove(position);
                    notifyDataSetChanged();
                    if (mSelectedData0.size() == 0) {
                        iv_add_photo_surface.setVisibility(View.VISIBLE);
                        LogUtils.e("删除为零了==0");
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mSelectedData0.size();
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

    private class MyRecyclerViewAdapter1 extends RecyclerView.Adapter<MyRecyclerViewAdapter1.ViewHolder> {

        private ArrayList<Uri> mSelectedData1;

        public ArrayList<Uri> getmSelectedData1() {
            return mSelectedData1;
        }

        MyRecyclerViewAdapter1(ArrayList<Uri> mSelectedData1) {
            this.mSelectedData1 = mSelectedData1;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = View.inflate(mContext, R.layout.item_all_image, null);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Glide.with(mContext)
                    .load(mSelectedData1.get(position))
                    .placeholder(R.drawable.default_bg_ratio_1)
                    .error(R.drawable.default_bg_ratio_1)
                    .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                    .into(holder.iv_image_all);

            holder.iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedData1.remove(position);
                    notifyDataSetChanged();
                    if (mSelectedData1.size() == 0) {
                        iv_add_photo_inner.setVisibility(View.VISIBLE);
                        LogUtils.e("删除为零了===1");
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mSelectedData1.size();
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

    private class MyRecyclerViewAdapter2 extends RecyclerView.Adapter<MyRecyclerViewAdapter2.ViewHolder> {

        private ArrayList<Uri> mSelectedData2;

        public ArrayList<Uri> getmSelectedData2() {
            return mSelectedData2;
        }

        MyRecyclerViewAdapter2(ArrayList<Uri> mSelectedData2) {
            this.mSelectedData2 = mSelectedData2;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = View.inflate(mContext, R.layout.item_all_image, null);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Glide.with(mContext)
                    .load(mSelectedData2.get(position))
                    .placeholder(R.drawable.default_bg_ratio_1)
                    .error(R.drawable.default_bg_ratio_1)
                    .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                    .into(holder.iv_image_all);

            holder.iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedData2.remove(position);
                    notifyDataSetChanged();
                    if (mSelectedData2.size() == 0) {
                        iv_add_photo_structure.setVisibility(View.VISIBLE);
                        LogUtils.e("删除为零了==2");
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mSelectedData2.size();
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

    private class MyRecyclerViewAdapter3 extends RecyclerView.Adapter<MyRecyclerViewAdapter3.ViewHolder> {

        private ArrayList<Uri> mSelectedData3;

        public ArrayList<Uri> getmSelectedData3() {
            return mSelectedData3;
        }

        MyRecyclerViewAdapter3(ArrayList<Uri> mSelectedData3) {
            this.mSelectedData3 = mSelectedData3;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = View.inflate(mContext, R.layout.item_all_image, null);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Glide.with(mContext)
                    .load(mSelectedData3.get(position))
                    .placeholder(R.drawable.default_bg_ratio_1)
                    .error(R.drawable.default_bg_ratio_1)
                    .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                    .into(holder.iv_image_all);

            holder.iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedData3.remove(position);
                    notifyDataSetChanged();
                    if (mSelectedData3.size() == 0) {
                        iv_add_photo_more.setVisibility(View.VISIBLE);
                        LogUtils.e("删除为零了===3");
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mSelectedData3.size();
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
