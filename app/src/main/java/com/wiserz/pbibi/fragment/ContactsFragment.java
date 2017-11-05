package com.wiserz.pbibi.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.activity.RegisterAndLoginActivity;
import com.wiserz.pbibi.adapter.ContactsAdapter;
import com.wiserz.pbibi.bean.ContactsBean;
import com.wiserz.pbibi.bean.UserInfoBean;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.util.Trans2PinYin;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by jackie on 2017/10/13 13:55.
 * QQ : 971060378
 * Used as : 通讯录好友
 */
public class ContactsFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private String SHARE_MESSAGE;

    private List<ContactsBean> datas;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("通讯录好友");

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        List<ContactsBean> list = getSystemContactInfos();
        Gson gson = new Gson();
        String jsonStr = gson.toJson(list);

        SHARE_MESSAGE = (String) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);

        OkHttpUtils.post()
                .url(Constant.getMobileFriend())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.MOBILE_LIST, jsonStr)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShort("请求数据出错");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jo = new JSONObject(response);
                            int status = jo.getInt("status");
                            int code = jo.getInt("code");
                            if (status == 1 && code == 0) {
                                JSONObject jo1 = jo.getJSONObject("data");
                                JSONArray ja = jo1.getJSONArray("list");
                                Gson gson = new Gson();
                                List<ContactsBean> list = gson.fromJson(ja.toString(), new TypeToken<List<ContactsBean>>() {
                                }.getType());
                                List<ContactsBean> list1 = new ArrayList<>();
                                datas = new ArrayList<>();
                                for (int i = 0; i < list.size(); i++) {
                                    ContactsBean contactsBean = new ContactsBean();
                                    UserInfoBean userInfoBean = list.get(i).getUserinfo();
                                    contactsBean.setName(list.get(i).getName());
                                    contactsBean.setPhone(list.get(i).getPhone());
                                    contactsBean.setUserinfo(userInfoBean);
                                    String pinyin = Trans2PinYin.trans2PinYin(list.get(i).getName());
                                    String letter = pinyin.substring(0, 1).toUpperCase();
                                    contactsBean.setLetter(letter);
                                    list1.add(contactsBean);
                                }
                                datas = getNewList(list1);
                                listView.setAdapter(new ContactsAdapter(getActivity(), datas));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ContactsFragmentJson", "解析出错" + e.getMessage());
                            ToastUtils.showShort("解析出错");
                        }
                    }
                });
    }

    /**
     * 获取系统联系人信息
     *
     * @return
     */
    public List<ContactsBean> getSystemContactInfos() {
        String[] mContactsProjection = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID
        };
        List<ContactsBean> infos = new ArrayList<ContactsBean>();
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, mContactsProjection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {

                ContactsBean info = new ContactsBean();
                String contactName = cursor.getString(2);
                String phoneNumber = cursor.getString(1);
                info.setName(contactName);
                info.setPhone(phoneNumber);
                infos.add(info);
                info = null;
            }
            cursor.close();
        }
        return infos;
    }

    /**
     * 去除数据重复
     *
     * @param li 数据集合
     * @return
     */
    public static List<ContactsBean> getNewList(List<ContactsBean> li) {
        List<ContactsBean> list = new ArrayList<ContactsBean>();
        //遍历数据集合
        for (int i = 0; i < li.size(); i++) {
            //得到数据
            ContactsBean b = li.get(i);
            //            String str = li.get(i).getPhone();
            //不重复
            //查看新集合中是否有指定的元素，如果没有则加入
            if (!list.contains(b)) {
                //添加数据
                list.add(b);
            }
        }
        return list;  //返回集合
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        ContactsBean bean = datas.get(position);
        if (bean.getUserinfo() == null || bean.getUserinfo().equals("") || bean.getUserinfo().getUser_id() == 0) {
            //没有注册
            sendSmsWithBody(getActivity(), datas.get(position).getPhone(), SHARE_MESSAGE);
        } else {
            if(!CommonUtil.isHadLogin()) {
                gotoPager(RegisterAndLoginActivity.class, null);
                return;
            }
            int is_friend = bean.getUserinfo().getIs_friend();
            if (is_friend == 1) {
                //关注了
                Bundle bundle = new Bundle();
                int userid = bean.getUserinfo().getUser_id();
                bundle.putInt(Constant.USER_ID, userid);
                ((BaseActivity) getActivity()).gotoPager(OtherHomePageFragment.class, bundle);
            } else if (is_friend == 2) {
                //没有关注
                final UserInfoBean mMyUserInfo = bean.getUserinfo();
                OkHttpUtils.post()
                        .url(Constant.getCreateFollowUrl())
                        .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                        .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                        .addParams(Constant.USER_ID, String.valueOf(mMyUserInfo.getUser_id()))
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
                                        ((ImageView) (view.findViewById(R.id.ivInvite))).setImageResource(R.drawable.other_followed);
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

    /**
     * 调用系统界面，给指定的号码发送短信，并附带短信内容
     *
     * @param context
     * @param number
     * @param body
     */
    public void sendSmsWithBody(Context context, String number, String body) {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(Uri.parse("smsto:" + number));
        sendIntent.putExtra("sms_body", body);
        context.startActivity(sendIntent);
    }
}
