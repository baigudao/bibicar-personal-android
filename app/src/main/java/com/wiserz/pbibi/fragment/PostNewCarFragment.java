package com.wiserz.pbibi.fragment;

import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.tencent.mm.opensdk.utils.Log;
import com.wiserz.pbibi.BaseApplication;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.CarConfiguration;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import okhttp3.Call;

/**
 * Created by jackie on 2017/9/7 16:04.
 * QQ : 971060378
 * Used as : 上传新车的页面
 */
public class PostNewCarFragment extends BaseFragment {

    private JSONArray mPhotoTypes;
    private JSONArray mPhotoFile;

    private TextView tv_car_type;
    private TextView tvCarColor;

    private int brand_id;
    private int series_id;
    private int model_id;

    private int mColor;

    private ArrayList<File> mUploadPhotos;
    private String upload_token;
    private ArrayList<CarConfiguration> mCarConfigurationList;
    private ArrayList<String> mSelectedConfig=new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_post_new_car;
    }

    @Override
    protected void initView(View view) {
        DataManager.getInstance().setObject(null);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("上传新车");
        view.findViewById(R.id.topLine).setVisibility(View.GONE);
        view.findViewById(R.id.btn_post_new_car).setOnClickListener(this);
        view.findViewById(R.id.rl_choose_car_color).setOnClickListener(this);
        view.findViewById(R.id.rl_choose_car_type).setOnClickListener(this);
        view.findViewById(R.id.tvCloseWarning).setOnClickListener(this);
        view.findViewById(R.id.tvDetailMsg).setOnClickListener(this);
        view.findViewById(R.id.tvBasicMsg).setOnClickListener(this);

        tv_car_type = (TextView) view.findViewById(R.id.tv_car_type);
        tvCarColor = (TextView) view.findViewById(R.id.tvCarColor);

        getTokenFromNet();
        getCarExtraInfo();
        gotoPager(CameraFragment.class,null,true);
    }

    private ArrayList<File> getUploadPhotos() {
        if (mUploadPhotos == null) {
            mUploadPhotos = new ArrayList<>();
        }
        return mUploadPhotos;
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

    private void getCarExtraInfo() {
        OkHttpUtils.post()
                .url(Constant.getCarExtraInfoUrl())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
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
                            JSONArray jsonObjectData = jsonObject.optJSONArray("data");
                            if (status == 1) {
                                mCarConfigurationList=getCarConfigurations(jsonObjectData);
                                resetCarConfigurations(mCarConfigurationList);
                            } else {
                                String code = jsonObject.optString("code");
                                String msg = jsonObject.optString("msg");
                                ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void resetCarConfigurations(ArrayList<CarConfiguration> list){
        LinearLayout llDetailMsg=(LinearLayout)getView().findViewById(R.id.llDetailMsg);
        llDetailMsg.removeAllViews();
        mSelectedConfig.clear();
        if(list==null || list.isEmpty()){
            return;
        }
        LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
        LinearLayout layout;
        ArrayList<CarConfiguration.Configuration> itemList;
        for(CarConfiguration config:list){
            layout=(LinearLayout) layoutInflater.inflate(R.layout.item_car_configuration,null);
            llDetailMsg.addView(layout);
            ((TextView) layout.findViewById(R.id.tvConfigName)).setText(config.getType_name());
            itemList=config.getList();
            int itemCount=itemList.size();
            int rowCount=3;
            int typeId=config.getType_id();
            if(typeId==3 || typeId==4 || typeId==5){
                rowCount=2;
            }
            int row=itemCount%rowCount==0?itemCount/rowCount:itemCount/rowCount+1;
            LinearLayout itemLayout;
            TextView tvName1,tvName2,tvName3;
            for(int j=0;j<row;++j){
                itemLayout=(LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_configuration_detail,null);
                ((LinearLayout) layout.findViewById(R.id.llDetails)).addView(itemLayout);
                tvName1=((TextView) itemLayout.findViewById(R.id.tvName1));
                tvName2=((TextView) itemLayout.findViewById(R.id.tvName2));
                tvName3=((TextView) itemLayout.findViewById(R.id.tvName3));
                if(j*rowCount<itemCount){
                    tvName1.setText(itemList.get(j*rowCount).getName());
                    tvName1.setTag(R.id.tag,itemList.get(j*rowCount).getId());
                    tvName1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String id=String.valueOf(view.getTag(R.id.tag));
                            if(mSelectedConfig.contains(id)){
                                mSelectedConfig.remove(id);
                                view.setBackgroundResource(R.drawable.back_config_not_selected);
                                ((TextView)view).setTextColor(getResources().getColor(R.color.main_text_color));
                            }else{
                                mSelectedConfig.add(id);
                                view.setBackgroundResource(R.drawable.back_config_selected);
                                ((TextView)view).setTextColor(getResources().getColor(R.color.main_color));
                            }
                        }
                    });
                }else{
                    tvName1.setVisibility(View.GONE);
                }
                if(j*rowCount+1<itemCount){
                    tvName2.setText(itemList.get(j*rowCount+1).getName());
                    tvName2.setTag(R.id.tag,itemList.get(j*rowCount).getId());
                    tvName2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String id=String.valueOf(view.getTag(R.id.tag));
                            if(mSelectedConfig.contains(id)){
                                mSelectedConfig.remove(id);
                                view.setBackgroundResource(R.drawable.back_config_not_selected);
                                ((TextView)view).setTextColor(getResources().getColor(R.color.main_text_color));
                            }else{
                                mSelectedConfig.add(id);
                                view.setBackgroundResource(R.drawable.back_config_selected);
                                ((TextView)view).setTextColor(getResources().getColor(R.color.main_color));
                            }
                        }
                    });
                }else{
                    tvName2.setVisibility(View.GONE);
                }
                if(rowCount==2){
                    tvName3.setVisibility(View.GONE);
                }else {
                    if (j * rowCount + 2 < itemCount) {
                        tvName3.setText(itemList.get(j * rowCount + 2).getName());
                        tvName3.setTag(R.id.tag,itemList.get(j * rowCount + 2).getId());
                        tvName3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String id=String.valueOf(view.getTag(R.id.tag));
                                if(mSelectedConfig.contains(id)){
                                    mSelectedConfig.remove(id);
                                    view.setBackgroundResource(R.drawable.back_config_not_selected);
                                    ((TextView)view).setTextColor(getResources().getColor(R.color.main_text_color));
                                }else{
                                    mSelectedConfig.add(id);
                                    view.setBackgroundResource(R.drawable.back_config_selected);
                                    ((TextView)view).setTextColor(getResources().getColor(R.color.main_color));
                                }
                            }
                        });
                    } else {
                        tvName3.setVisibility(View.GONE);
                    }
                }
            }
        }

    }

    private ArrayList<CarConfiguration> getCarConfigurations(JSONArray array) {
        if (array == null) {
            return new ArrayList<>();
        } else {
            return  new Gson().fromJson(array.toString(), new TypeToken<ArrayList<CarConfiguration>>() {}.getType());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.tvBasicMsg:
                ((TextView)getView().findViewById(R.id.tvBasicMsg)).setTextColor(getResources().getColor(R.color.main_text_color));
                ((TextView)getView().findViewById(R.id.tvDetailMsg)).setTextColor(getResources().getColor(R.color.second_text_color));
                getView().findViewById(R.id.line1).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.line2).setVisibility(View.GONE);
                getView().findViewById(R.id.llBasicMsg).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.llDetailMsg).setVisibility(View.GONE);
                break;
            case R.id.tvDetailMsg:
                ((TextView)getView().findViewById(R.id.tvBasicMsg)).setTextColor(getResources().getColor(R.color.second_text_color));
                ((TextView)getView().findViewById(R.id.tvDetailMsg)).setTextColor(getResources().getColor(R.color.main_text_color));
                getView().findViewById(R.id.line1).setVisibility(View.GONE);
                getView().findViewById(R.id.line2).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.llBasicMsg).setVisibility(View.GONE);
                getView().findViewById(R.id.llDetailMsg).setVisibility(View.VISIBLE);
                break;
            case R.id.btn_post_new_car:
                publishNewCar();
                break;
            case R.id.rl_choose_car_type:
                gotoPager(SelectCarBrandFragment.class, null);
                break;
            case R.id.tvCloseWarning:
                getView().findViewById(R.id.rlWarning).setVisibility(View.GONE);
                break;
            case R.id.rl_choose_car_color:
                gotoPager(SelectCarColorFragment.class, null);
                break;
            default:
                break;
        }
    }

    public void onResume() {
        super.onResume();
        mUploadPhotos = (ArrayList<File>) DataManager.getInstance().getObject();
        if(mUploadPhotos==null){
            mUploadPhotos=new ArrayList<>();
        }
        resetCarPhotosView();

    }

    private void resetCarPhotosView(){
        LinearLayout llCarPhotos = (LinearLayout) getView().findViewById(R.id.llCarPhotos);
        llCarPhotos.removeAllViews();
        int size;
        if(mUploadPhotos.size()==6){
            size=6;
        }else{
            size=mUploadPhotos.size()+1;
        }
        int row=(size%3==0)?size/3:size/3+1;
        LinearLayout itemView;
        for(int i=0;i<row;++i){
            itemView= (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_car_photo,null);
            llCarPhotos.addView(itemView);
            int index=i*3;
            setPhoto((ViewGroup) itemView.getChildAt(0),index<mUploadPhotos.size()?mUploadPhotos.get(index):null,index,index==size-1);
            setPhoto((ViewGroup) itemView.getChildAt(1),index+1<mUploadPhotos.size()?mUploadPhotos.get(index+1):null,index+1,index+1==size-1);
            setPhoto((ViewGroup) itemView.getChildAt(2),index+2<mUploadPhotos.size()?mUploadPhotos.get(index+2):null,index+2,index+2==size-1);
        }
    }

    private void setPhoto(ViewGroup itemView,File photo,int index,boolean isLast){
        ImageView ivPhoto=(ImageView) itemView.getChildAt(0);
        ImageView ivClose=(ImageView) itemView.getChildAt(1);
        ivPhoto.setTag(R.id.tag, index);
        ivClose.setTag(R.id.tag, index);
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPager(CameraFragment.class,null,true);
            }
        });
        if(photo!=null){
            CommonUtil.loadImage(BaseApplication.getAppContext(), 0, Uri.fromFile(photo), ivPhoto);
            ivClose.setVisibility(View.VISIBLE);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag(R.id.tag);
                    File file = mUploadPhotos.remove(pos);
                    file.delete();
                    resetCarPhotosView();
                }
            });
        }else{
            if(isLast) {
                ivPhoto.setImageResource(R.drawable.add_car_photo);
                ivClose.setVisibility(View.GONE);
            }else {
                itemView.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isHidden()) {
            Object data = DataManager.getInstance().getData7();
            if (EmptyUtils.isNotEmpty(data)) {
                if (data instanceof Integer) {
                    //颜色回调
                    mColor = (int) data;
                    resetColorView(mColor);
                }
                DataManager.getInstance().setData7(null);
            }

            Object data1 = DataManager.getInstance().getData1();
            Object data2 = DataManager.getInstance().getData2();
            Object data3 = DataManager.getInstance().getData3();
            Object data4 = DataManager.getInstance().getData4();
            Object data5 = DataManager.getInstance().getData5();
            Object data6 = DataManager.getInstance().getData6();
            if (EmptyUtils.isNotEmpty(data1)
                    && EmptyUtils.isNotEmpty(data2)
                    && EmptyUtils.isNotEmpty(data3)
                    && EmptyUtils.isNotEmpty(data4)
                    && EmptyUtils.isNotEmpty(data5)
                    && EmptyUtils.isNotEmpty(data6)) {
                if (data1 instanceof Integer && data2 instanceof String
                        && data3 instanceof Integer && data4 instanceof String
                        && data5 instanceof Integer && data6 instanceof String) {
                    brand_id = (int) data1;
                    String brand_name = (String) data2;
                    series_id = (int) data3;
                    String series_name = (String) data4;
                    model_id = (int) data5;
                    String model_name = (String) data6;
                    resetCarModelView(brand_name, series_name, model_name);
                }
                DataManager.getInstance().setData1(null);
                DataManager.getInstance().setData2(null);
                DataManager.getInstance().setData3(null);
                DataManager.getInstance().setData4(null);
                DataManager.getInstance().setData5(null);
                DataManager.getInstance().setData6(null);
            }
        }
    }

    private void resetCarModelView(String brand_name, String series_name, String model_name) {
        tv_car_type.setText(brand_name + " " + series_name + " " + model_name);
    }

    private void resetColorView(int color) {
        Resources res = getResources();
        String text = res.getString(res.getIdentifier("car_color_" + (color + 1), "string", getActivity().getPackageName()));
        tvCarColor.setText(text);
    }

    private String getInputProfile() {
        if (getView() != null)
            return ((EditText) getView().findViewById(R.id.et_input_profile)).getText().toString().trim();
        else
            return null;
    }

    private String getInputPhoneNum() {
        if (getView() != null)
            return ((EditText) getView().findViewById(R.id.et_input_phone_num)).getText().toString().trim();
        else
            return null;
    }

//    private String getInputPlace() {
//        if (getView() != null)
//            return ((EditText) getView().findViewById(R.id.et_input_place)).getText().toString().trim();
//        else
//            return null;
//    }

    private String getInputName() {
        if (getView() != null)
            return ((EditText) getView().findViewById(R.id.et_input_name)).getText().toString().trim();
        else
            return null;
    }

    private String getInputPrice() {
        if (getView() != null)
            return ((EditText) getView().findViewById(R.id.et_input_price)).getText().toString().trim();
        else
            return null;
    }

    private void publishNewCar() {
        String profile = getInputProfile();
        String phone_num = getInputPhoneNum();
 //       String place = getInputPlace();
        String name = getInputName();
        String car_price = getInputPrice();

        if (EmptyUtils.isEmpty(model_id) || model_id == 0) {
            ToastUtils.showShort("请选择车型");
            return;
        }

        if (EmptyUtils.isEmpty(car_price)) {
            ToastUtils.showShort("请填写车辆价格");
            return;
        }

        if (EmptyUtils.isEmpty(name)) {
            ToastUtils.showShort("请填写联系人");
            return;
        }

        if (EmptyUtils.isEmpty(phone_num)) {
            ToastUtils.showShort("请填写你的联系电话");
            return;
        }

        if (EmptyUtils.isEmpty(profile)) {
            ToastUtils.showShort("请填写车主说的内容");
            return;
        }

        if (EmptyUtils.isEmpty(upload_token)) {
            getTokenFromNet();
            return;
        }

        if (!getUploadPhotos().isEmpty()) {
            uploadImage(getUploadPhotos(), upload_token);
        } else {
            ToastUtils.showShort("请添加车辆照片！");
        }
    }

    private void uploadImage(ArrayList<File> uploadPhotos, String upload_token) {
        final int mSize = uploadPhotos.size();

        mPhotoTypes = new JSONArray();
        mPhotoFile = new JSONArray();

        UploadManager uploadManager = new UploadManager();

        for (int i = 0; i < mSize; i++) {
            File photoPath = uploadPhotos.get(i);

            if (EmptyUtils.isNotEmpty(photoPath)) {
                int file_type = 1;
                if (EmptyUtils.isNotEmpty(file_type)) {
                    uploadManager.put(photoPath, UUID.randomUUID().toString() + "_" + String.valueOf(file_type), upload_token, new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            Log.e("aaaaaaaaa1","response: "+response.toString());
                            if (info.isOK()) {
                                //上传成功
                                int status = response.optInt("status");
                                JSONObject jsonObjectData = response.optJSONObject("data");
                                if (status == 1) {
                                    String file_type = key.substring(key.lastIndexOf("_") + 1);
                                    mPhotoTypes.put(file_type);
                                    String hash = jsonObjectData.optString("hash");
                                    mPhotoFile.put(hash);
                                    if (mPhotoTypes.length() == mSize && mPhotoFile.length() == mSize) {
                                        commitDataToServer(mPhotoTypes, mPhotoFile);
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

    private void commitDataToServer(JSONArray mPhotoTypes, JSONArray mPhotoFile) {
        if (EmptyUtils.isNotEmpty(mPhotoTypes) && EmptyUtils.isNotEmpty(mPhotoFile)) {

            String profile = getInputProfile();
            String phone_num = getInputPhoneNum();
            String name = getInputName();
            String car_price = getInputPrice();
            String carInfoIds="";
            int size=mSelectedConfig.size();
            for(int i=0;i<size;++i){
                carInfoIds+=mSelectedConfig.get(i);
                if(i<size-1){
                    carInfoIds+=",";
                }
            }
            OkHttpUtils.post()
                    .url(Constant.getPublishNewCarUrl())
                    .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                    .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                    .addParams(Constant.FILES_TYPE, mPhotoTypes.toString())
                    .addParams(Constant.FILES_ID, mPhotoFile.toString())
                    .addParams(Constant.CAR_COLOR, String.valueOf(mColor))
                    .addParams(Constant.CAR_INTRO, profile)
                    .addParams(Constant.PRICE, car_price)
                    .addParams(Constant.CONTACT_NAME, name)
                    .addParams(Constant.CONTACT_PHONE, phone_num)
                    .addParams(Constant.CONTACT_ADDRESS, "")
                    .addParams(Constant.CITY_ID, "")
                    .addParams(Constant.BRAND_ID, String.valueOf(brand_id))//车品牌id                    ooo(必填)
                    .addParams(Constant.SERIES_ID, String.valueOf(series_id))//车系列id                  ooo(必填)
                    .addParams(Constant.MODEL_ID, String.valueOf(model_id))//车型id                      ooo(必填)
                    .addParams(Constant.CAR_TYPE, String.valueOf(1))
                    .addParams(Constant.CAR_NO, "")//车牌号码
                    .addParams(Constant.ACTION, String.valueOf(1))//上传车类型
                    .addParams(Constant.IS_TRANSFER, String.valueOf(0))//是否过户
                    .addParams(Constant.CAR_STATUS, String.valueOf(0))//车辆状态
                    .addParams(Constant.VIN_NO, "")//vin码
                    .addParams(Constant.VIN_FILE, "")//vin文件
                    .addParams(Constant.EXCHANGE_TIME, String.valueOf(0))//交易时间
                    .addParams(Constant.ENGINE_NO, "")//发动机号
                    .addParams(Constant.MAINTAIN, String.valueOf(0))
                    .addParams(Constant.BOARD_TIME, "")
                    .addParams(Constant.MILEAGE, String.valueOf(0.000000))
                    .addParams(Constant.BOARD_ADDRESS,"")
                    .addParams(Constant.CAR_INFO_IDS,carInfoIds)
                    .addParams(Constant.CAR_ID, "")
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
                                    ToastUtils.showShort("上传新车成功！");
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
}
