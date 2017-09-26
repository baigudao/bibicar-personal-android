package com.wiserz.pbibi.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jackie on 2017/8/14 17:39.
 * QQ : 971060378
 * Used as : 发布动态的页面
 */
public class PublishStateFragment extends BaseFragment {

    private static final int REQUEST_CODE_CHOOSE = 55;
    private RecyclerView recyclerView;
    private ImageView iv_add;
    private EditText et_input_content;

    private ArrayList<UploadCarPhotoInfo> uploadCarPhotoInfoArrayList;
    private String upload_token;

    private TextView tv_choose_topic;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_publish_state;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("发布动态");
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setVisibility(View.VISIBLE);
        btn_register.setText("发布");
        btn_register.setOnClickListener(this);

        et_input_content = (EditText) view.findViewById(R.id.et_input_content);
        et_input_content.setFocusable(true);

        iv_add = (ImageView) view.findViewById(R.id.iv_add);
        iv_add.setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        view.findViewById(R.id.rl_choose_topic).setOnClickListener(this);

        tv_choose_topic = (TextView) view.findViewById(R.id.tv_choose_topic);

        uploadCarPhotoInfoArrayList = new ArrayList<>();
    }

    private String getInputContent() {
        return et_input_content.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_register:
                publishState();
                break;
            case R.id.iv_add:
                addImage();
                break;
            case R.id.rl_choose_topic:
                gotoPager(SelectTopicFragment.class, null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Object object = DataManager.getInstance().getData4();
            if (EmptyUtils.isNotEmpty(object)) {
                ArrayList<String> stringArrayList = (ArrayList<String>) object;
                if (EmptyUtils.isNotEmpty(stringArrayList) && stringArrayList.size() != 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 0; i < stringArrayList.size(); i++) {
                        stringBuffer.append(stringArrayList.get(i));
                    }
                    tv_choose_topic.setText(stringBuffer);
                }
                DataManager.getInstance().setData4(null);
            }
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

    private void publishState() {
        String content = getInputContent();
        if (EmptyUtils.isEmpty(content)) {
            ToastUtils.showShort("亲，请输入动态内容哦！");
            return;
        }
        if (EmptyUtils.isEmpty(uploadCarPhotoInfoArrayList)) {
            ToastUtils.showShort("亲，上传图片更容易吸引关注哦！");
            return;
        }
        if (EmptyUtils.isNotEmpty(upload_token) && EmptyUtils.isNotEmpty(uploadCarPhotoInfoArrayList) && uploadCarPhotoInfoArrayList.size() != 0) {
            UploadManager uploadManager = new UploadManager();
            final JSONArray mPhotoFile = new JSONArray();

            final int mSize = uploadCarPhotoInfoArrayList.size();
            for (int i = 0; i < mSize; i++) {
                UploadCarPhotoInfo uploadCarPhotoInfo = uploadCarPhotoInfoArrayList.get(i);

                if (EmptyUtils.isNotEmpty(uploadCarPhotoInfo)) {
                    uploadManager.put(uploadCarPhotoInfo.getFile(), UUID.randomUUID().toString(), upload_token, new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            if (info.isOK()) {
                                //上传成功
                                int status = response.optInt("status");
                                JSONObject jsonObjectData = response.optJSONObject("data");
                                if (status == 1) {
                                    String hash = jsonObjectData.optString("hash");
                                    mPhotoFile.put(hash);
                                    if (mPhotoFile.length() == mSize) {
                                        commitDataToServer(mPhotoFile);
                                    }
                                } else {
                                    String code = response.optString("code");
                                    String msg = jsonObjectData.optString("msg");
                                    ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
                                }
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showShort("车辆照片上传失败，请重新上传");
                                    }
                                });
                            }
                        }
                    }, null);

                }
            }
        }
    }

    private void commitDataToServer(JSONArray mPhotoFile) {
        if (EmptyUtils.isNotEmpty(mPhotoFile)) {
            String string = tv_choose_topic.getText().toString().trim();
            if (string.equals("#选择话题")) {
                string = "";
            }
            LogUtils.e(string + getInputContent());
            OkHttpUtils.post()
                    .url(Constant.getCreatePostUrl())
                    .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                    .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                    .addParams(Constant.FILES_ID, mPhotoFile.toString())
                    .addParams(Constant.POST_CONTENT, string + getInputContent())
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
                                    ToastUtils.showShort("发布动态成功！");
                                    goBack();
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
    }

    private void addImage() {
        Matisse.from(PublishStateFragment.this)
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
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);

            if (EmptyUtils.isNotEmpty(mSelected) && mSelected.size() != 0) {
                recyclerView.setVisibility(View.VISIBLE);
                iv_add.setVisibility(View.GONE);

                MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter(mSelected);
                recyclerView.setAdapter(myRecyclerViewAdapter);
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));

                int mSize = mSelected.size();
                for (int i = 0; i < mSize; i++) {
                    UploadCarPhotoInfo uploadCarPhotoInfo = new UploadCarPhotoInfo();
                    uploadCarPhotoInfo.setFile_type(1);
                    uploadCarPhotoInfo.setFile(new File(CommonUtil.getRealFilePath(mContext, mSelected.get(i))));
                    uploadCarPhotoInfoArrayList.add(uploadCarPhotoInfo);
                }
            }
        }
    }

    private class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

        private List<Uri> mSelectedData;

        MyRecyclerViewAdapter(List<Uri> mSelected) {
            this.mSelectedData = mSelected;
        }

        @Override
        public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = View.inflate(mContext, R.layout.item_state_image, null);
            return new MyRecyclerViewAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyRecyclerViewAdapter.ViewHolder holder, int position) {
            Glide.with(mContext)
                    .load(mSelectedData.get(position))
                    .placeholder(R.drawable.default_bg_ratio_1)
                    .error(R.drawable.default_bg_ratio_1)
                    .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                    .into(holder.iv_image_state);
        }

        @Override
        public int getItemCount() {
            return mSelectedData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView iv_image_state;
            private ImageView iv_close;

            ViewHolder(View itemView) {
                super(itemView);
                iv_image_state = (ImageView) itemView.findViewById(R.id.iv_image_state);
                iv_close = (ImageView) itemView.findViewById(R.id.iv_close);
            }
        }
    }
}
