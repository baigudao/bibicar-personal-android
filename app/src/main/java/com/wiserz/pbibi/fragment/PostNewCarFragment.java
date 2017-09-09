package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.UploadCarPhotoInfo;
import com.wiserz.pbibi.util.DataManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by jackie on 2017/9/7 16:04.
 * QQ : 971060378
 * Used as : 上传新车的页面
 */
public class PostNewCarFragment extends BaseFragment {

    private UploadManager uploadManager;
    private HashMap<Integer, String> stringHashMap;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_post_new_car;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("上传新车");
        view.findViewById(R.id.iv_add_car_photo).setOnClickListener(this);
        view.findViewById(R.id.btn_post_new_car).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.iv_add_car_photo:
                gotoPager(PostPhotoFragment.class, null);
                break;
            case R.id.btn_post_new_car:
                publishNewCar();
                break;
            default:
                break;
        }
    }

    private void publishNewCar() {
        ArrayList<UploadCarPhotoInfo> uploadCarPhotoInfoArrayList = (ArrayList<UploadCarPhotoInfo>) DataManager.getInstance().getData1();
        String upload_token = (String) DataManager.getInstance().getData2();
        DataManager.getInstance().setData1(null);
        DataManager.getInstance().setData2(null);

        if (EmptyUtils.isNotEmpty(upload_token) && EmptyUtils.isNotEmpty(uploadCarPhotoInfoArrayList) && uploadCarPhotoInfoArrayList.size() != 0) {
            uploadImage(uploadCarPhotoInfoArrayList, upload_token);
        }
    }

    private void uploadImage(ArrayList<UploadCarPhotoInfo> uploadCarPhotoInfoArrayList, String upload_token) {
        int mSize = uploadCarPhotoInfoArrayList.size();
        uploadManager = new UploadManager();
        for (int i = 0; i < mSize; i++) {
            UploadCarPhotoInfo uploadCarPhotoInfo = uploadCarPhotoInfoArrayList.get(i);

            if (EmptyUtils.isNotEmpty(uploadCarPhotoInfo)) {
                stringHashMap = new HashMap<>();
                uploadManager.put(uploadCarPhotoInfo.getFile(), UUID.randomUUID().toString() + "_" + String.valueOf(uploadCarPhotoInfo.getFile_type()), upload_token, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        LogUtils.e(response);
                        if (info.isOK()) {
                            //上传成功
                            int status = response.optInt("status");
                            JSONObject jsonObjectData = response.optJSONObject("data");
                            if (status == 1) {
                                String hash = jsonObjectData.optString("hash");
                                LogUtils.e(key + "和" + hash);//例如： ee38b98b-721a-4181-bbbc-a4f444ad0fba_1和ee38b98b-721a-4181-bbbc-a4f444ad0fba_1
                                //                            stringHashMap.put(key, hash);
                                //                            if (stringHashMap.size() == 4) {
                                //                                commitDataToServer(stringHashMap);
                                //                            }
                            } else {
                                String code = response.optString("code");
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
                            }
                        } else {
                            ToastUtils.showShort("车辆照片上传失败，请重新上传");
                        }
                    }
                }, null);
            }
        }
    }
}
