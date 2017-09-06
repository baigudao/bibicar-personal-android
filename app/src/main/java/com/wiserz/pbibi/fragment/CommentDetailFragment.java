package com.wiserz.pbibi.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.AllSecondCommentBean;
import com.wiserz.pbibi.bean.FatherInfoBean;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;

/**
 * Created by jackie on 2017/8/25 14:18.
 * QQ : 971060378
 * Used as : 评论详情的页面
 */
public class CommentDetailFragment extends BaseFragment {

    private static final int COMMENT_DETAIL_DATA_TYPE = 20;
    private int mPage;
    private int feed_id;
    private int comment_id;

    private TextView tv_zan;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_comment_detail;
    }

    @Override
    protected void initView(View view) {
        feed_id = (int) DataManager.getInstance().getData1();
        comment_id = (int) DataManager.getInstance().getData2();
        DataManager.getInstance().setData1(null);
        DataManager.getInstance().setData2(null);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("评论详情");

        tv_zan = (TextView) view.findViewById(R.id.tv_zan);

        mPage = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.ll_zan_or_no:
                zanOrNo();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (EmptyUtils.isNotEmpty(feed_id) && EmptyUtils.isNotEmpty(comment_id)) {
            getDataFromNet();
        }
    }

    private void getDataFromNet() {
        OkHttpUtils.post()
                .url(Constant.getAllSecondComment())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.COMMENT_ID, String.valueOf(comment_id))
                .addParams(Constant.FEED_ID, String.valueOf(feed_id))
                .addParams(Constant.PAGE, String.valueOf(mPage))
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
                                handlerDataForSecondCommentTop(jsonObjectData);
                                handlerDataForSecondCommentList(jsonObjectData);
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

    private void handlerDataForSecondCommentList(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONArray jsonArray = jsonObjectData.optJSONArray("comment_list");
            if (EmptyUtils.isNotEmpty(jsonArray) && jsonArray.length() != 0) {
                Gson gson = new Gson();
                ArrayList<AllSecondCommentBean> allSecondCommentBeanArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<AllSecondCommentBean>>() {
                }.getType());

                if (EmptyUtils.isNotEmpty(allSecondCommentBeanArrayList) && getView() != null) {
                    RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
                    BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, allSecondCommentBeanArrayList, COMMENT_DETAIL_DATA_TYPE);
                    recyclerView.setAdapter(baseRecyclerViewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                }
            }
        }
    }

    private void handlerDataForSecondCommentTop(JSONObject jsonObjectData) {
        if (EmptyUtils.isNotEmpty(jsonObjectData)) {
            JSONObject jsonObjectForFatherInfo = jsonObjectData.optJSONObject("father_info");
            if (EmptyUtils.isNotEmpty(jsonObjectForFatherInfo)) {
                Gson gson = new Gson();
                FatherInfoBean fatherInfoBean = gson.fromJson(jsonObjectForFatherInfo.toString(), FatherInfoBean.class);

                if (EmptyUtils.isNotEmpty(fatherInfoBean)) {
                    showTopView(fatherInfoBean);
                }
            }
        }
    }

    private void showTopView(final FatherInfoBean fatherInfoBean) {
        if (getView() != null) {
            ImageView iv_circle_image = (ImageView) getView().findViewById(R.id.iv_circle_image);
            TextView tv_comment_name = (TextView) getView().findViewById(R.id.tv_comment_name);
            TextView tv_comment_time = (TextView) getView().findViewById(R.id.tv_comment_time);
            TextView tv_comment_content = (TextView) getView().findViewById(R.id.tv_comment_content);

            Glide.with(mContext)
                    .load(fatherInfoBean.getFrom_user().getAvatar())
                    .placeholder(R.drawable.user_photo)
                    .error(R.drawable.user_photo)
                    .into(iv_circle_image);
            iv_circle_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showShort(fatherInfoBean.getFrom_user().getUser_id() + "");
                }
            });

            tv_comment_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showShort(fatherInfoBean.getFrom_user().getUser_id() + "");
                }
            });
            tv_comment_name.setText(fatherInfoBean.getFrom_user().getNickname());

            tv_comment_time.setText(TimeUtils.date2String(new Date(Long.valueOf(fatherInfoBean.getComment_created()) * 1000), new SimpleDateFormat("yyyy-MM-dd")));

            int is_like = fatherInfoBean.getIs_like();
            if (EmptyUtils.isNotEmpty(is_like)) {
                switch (is_like) {
                    case 1:
                        zanView();
                        break;
                    case 2:
                        //没赞
                        cancelZanView();
                        break;
                    default:
                        break;
                }
            }
            getView().findViewById(R.id.ll_zan_or_no).setOnClickListener(this);
            tv_zan.setText(String.valueOf(fatherInfoBean.getLike_num()));

            tv_comment_content.setText(fatherInfoBean.getComment_content());//评论的内容
        }
    }

    private void zanOrNo() {
        //点赞
        OkHttpUtils.post()
                .url(Constant.getCommentLikeCreate())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.FEED_ID, String.valueOf(feed_id))
                .addParams(Constant.COMMENT_ID, String.valueOf(comment_id))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShort("点赞失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                zanView();
                                int like_num = Integer.valueOf(tv_zan.getText().toString());
                                ++like_num;
                                tv_zan.setText(String.valueOf(like_num));
                                ToastUtils.showShort("点赞成功");
                            } else {
                                String code = jsonObject.optString("code");
                                if (code.equals("67000")) {
                                    cancelZan();
                                    return;
                                }
                                String msg = jsonObjectData.optString("msg");
                                ToastUtils.showShort("请求数据失败,请检查网络:" + code + " - " + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void cancelZan() {
        //取消点赞
        OkHttpUtils.post()
                .url(Constant.getCommentLikeDelete())
                .addParams(Constant.DEVICE_IDENTIFIER, SPUtils.getInstance().getString(Constant.DEVICE_IDENTIFIER))
                .addParams(Constant.SESSION_ID, SPUtils.getInstance().getString(Constant.SESSION_ID))
                .addParams(Constant.FEED_ID, String.valueOf(feed_id))
                .addParams(Constant.COMMENT_ID, String.valueOf(comment_id))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShort("取消点赞失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                            if (status == 1) {
                                cancelZanView();
                                int like_num = Integer.valueOf(tv_zan.getText().toString());
                                --like_num;
                                tv_zan.setText(String.valueOf(like_num));
                                ToastUtils.showShort("取消点赞成功");
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

    private void cancelZanView() {
        if (getView() != null) {
            ImageView iv_zan = (ImageView) getView().findViewById(R.id.iv_zan);
            ImageView iv_zan_no = (ImageView) getView().findViewById(R.id.iv_zan_no);

            iv_zan.setVisibility(View.GONE);
            iv_zan_no.setVisibility(View.VISIBLE);
        }
    }

    private void zanView() {
        if (getView() != null) {
            ImageView iv_zan = (ImageView) getView().findViewById(R.id.iv_zan);
            ImageView iv_zan_no = (ImageView) getView().findViewById(R.id.iv_zan_no);

            iv_zan.setVisibility(View.VISIBLE);
            iv_zan_no.setVisibility(View.GONE);
        }
    }
}
