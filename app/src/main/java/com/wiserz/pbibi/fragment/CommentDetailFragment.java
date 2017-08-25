package com.wiserz.pbibi.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.BaseRecyclerViewAdapter;
import com.wiserz.pbibi.bean.ArticleCommentBean;
import com.wiserz.pbibi.util.DataManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jackie on 2017/8/25 14:18.
 * QQ : 971060378
 * Used as : 评论详情的页面
 */
public class CommentDetailFragment extends BaseFragment {

    private ArticleCommentBean articleCommentBean;

    private static final int COMMENT_DETAIL_DATA_TYPE = 20;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_comment_detail;
    }

    @Override
    protected void initView(View view) {
        articleCommentBean = (ArticleCommentBean) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("评论详情");

        showTopView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.ll_zan_or_no:
                zan();
                break;
            default:
                break;
        }
    }

    private void showTopView() {
        if (EmptyUtils.isNotEmpty(articleCommentBean) && getView() != null) {
            ImageView iv_circle_image = (ImageView) getView().findViewById(R.id.iv_circle_image);
            TextView tv_comment_name = (TextView) getView().findViewById(R.id.tv_comment_name);
            TextView tv_comment_time = (TextView) getView().findViewById(R.id.tv_comment_time);
            TextView tv_comment_content = (TextView) getView().findViewById(R.id.tv_comment_content);

            Glide.with(mContext)
                    .load(articleCommentBean.getFrom_user().getAvatar())
                    .placeholder(R.drawable.user_photo)
                    .error(R.drawable.user_photo)
                    .into(iv_circle_image);
            tv_comment_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showShort(articleCommentBean.getFrom_user().getUser_id() + "");
                }
            });
            tv_comment_name.setText(articleCommentBean.getFrom_user().getNickname());
            tv_comment_time.setText(TimeUtils.date2String(new Date(Long.valueOf(articleCommentBean.getComment_created()) * 1000), new SimpleDateFormat("yyyy-MM-dd")));

            getView().findViewById(R.id.ll_zan_or_no).setOnClickListener(this);

            tv_comment_content.setText(articleCommentBean.getComment_content());//评论的内容

            RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
            ArrayList<ArticleCommentBean.HotListBean.ListBean> listBeanList = (ArrayList<ArticleCommentBean.HotListBean.ListBean>) articleCommentBean.getHot_list().getList();
            if (EmptyUtils.isNotEmpty(listBeanList)) {
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, listBeanList, COMMENT_DETAIL_DATA_TYPE);
                recyclerView.setAdapter(baseRecyclerViewAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            }
        }
    }

    private void cancelZan() {
        if (getView() != null) {
            ImageView iv_zan = (ImageView) getView().findViewById(R.id.iv_zan);
            ImageView iv_zan_no = (ImageView) getView().findViewById(R.id.iv_zan_no);

            iv_zan.setVisibility(View.GONE);
            iv_zan_no.setVisibility(View.VISIBLE);
        }
    }

    private void zan() {
        if (getView() != null) {
            ImageView iv_zan = (ImageView) getView().findViewById(R.id.iv_zan);
            ImageView iv_zan_no = (ImageView) getView().findViewById(R.id.iv_zan_no);

            iv_zan.setVisibility(View.VISIBLE);
            iv_zan_no.setVisibility(View.GONE);
        }
    }
}
