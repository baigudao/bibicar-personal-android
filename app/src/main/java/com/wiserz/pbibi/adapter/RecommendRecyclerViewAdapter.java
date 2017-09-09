package com.wiserz.pbibi.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
import com.wiserz.pbibi.activity.MainActivity;
import com.wiserz.pbibi.activity.VRWatchCarActivity;
import com.wiserz.pbibi.bean.ArticleBean;
import com.wiserz.pbibi.bean.BannerBean;
import com.wiserz.pbibi.bean.CarInfoBean;
import com.wiserz.pbibi.bean.CheHangHomeBean;
import com.wiserz.pbibi.bean.LampBean;
import com.wiserz.pbibi.bean.TopLineBean;
import com.wiserz.pbibi.bean.VideoBean;
import com.wiserz.pbibi.fragment.ArticleDetailFragment;
import com.wiserz.pbibi.fragment.ArticleListFragment;
import com.wiserz.pbibi.fragment.BannerFragment;
import com.wiserz.pbibi.fragment.CarCheckServiceFragment;
import com.wiserz.pbibi.fragment.CarDetailFragment;
import com.wiserz.pbibi.fragment.CarRentFragment;
import com.wiserz.pbibi.fragment.CheHangListFragment;
import com.wiserz.pbibi.fragment.VideoDetailFragment;
import com.wiserz.pbibi.fragment.VideoListFragment;
import com.wiserz.pbibi.util.Constant;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.BaseAutoScrollView;
import com.wiserz.pbibi.view.VerticalLampView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by jackie on 2017/8/10 10:03.
 * QQ : 971060378
 * Used as : 推荐页面的适配器
 */
public class RecommendRecyclerViewAdapter extends RecyclerView.Adapter implements BaseRecyclerViewAdapter.OnItemClickListener {

    private Context mContext;
    private JSONObject jsonObjectData;

    private int currentType = BANNER;

    private static final int BANNER = 0;
    private static final int TWO_BIG_BUTTON = 1;
    private static final int TOP_LINE = 2;
    private static final int NEW_CAR = 3;
    private static final int CAR_VIDEO = 4;
    private static final int CAR_ARTICLE = 5;
    private static final int CHE_HANG = 6;

    private static final int NEW_CAR_DATA_TYPE = 55;
    private static final int CAR_VIDEO_DATA_TYPE = 65;
    private static final int CHE_HANG_HOME_DATA_TYPE = 75;

    public RecommendRecyclerViewAdapter(Context mContext, JSONObject jsonObjectData) {
        this.mContext = mContext;
        this.jsonObjectData = jsonObjectData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == BANNER) {
            viewHolder = new BannerViewHolder(View.inflate(mContext, R.layout.item_recommend_banner, null));
        } else if (viewType == TWO_BIG_BUTTON) {
            viewHolder = new TwoBigButtonViewHolder(View.inflate(mContext, R.layout.item_recommend_two_big_button, null));
        } else if (viewType == TOP_LINE) {
            viewHolder = new TopLineViewHolder(View.inflate(mContext, R.layout.item_recommend_top_line, null));
        } else if (viewType == NEW_CAR) {
            viewHolder = new NewCarViewHolder(View.inflate(mContext, R.layout.item_recommend_new_car, null));
        } else if (viewType == CAR_VIDEO) {
            viewHolder = new CarVideoViewHolder(View.inflate(mContext, R.layout.item_recommend_car_video, null));
        } else if (viewType == CAR_ARTICLE) {
            viewHolder = new CarArticleViewHolder(View.inflate(mContext, R.layout.item_recommend_car_article, null));
        } else if (viewType == CHE_HANG) {
            viewHolder = new CheHangViewHolder(View.inflate(mContext, R.layout.item_recommend_che_hang, null));
        } else {
            viewHolder = null;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (currentType == BANNER) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;

            final ArrayList<BannerBean> bannerBeanArrayList = getBannerData(jsonObjectData);

            //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
            bannerViewHolder.convenientBanner.startTurning(5000);
            bannerViewHolder.convenientBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
                @Override
                public LocalImageHolderView createHolder() {
                    return new LocalImageHolderView();
                }
            }, bannerBeanArrayList)
                    //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                    .setPageIndicator(new int[]{R.drawable.point_normal1, R.drawable.point_checked})
                    //设置指示器的方向
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                    .setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            BannerBean bannerBean = bannerBeanArrayList.get(position);
                            if (EmptyUtils.isNotEmpty(bannerBean)) {
                                String type = bannerBean.getType();
                                if (type.equals("0")) {
                                    DataManager.getInstance().setData1(bannerBean);
                                    ((MainActivity) mContext).gotoPager(VRWatchCarActivity.class, null);
                                } else {
                                    DataManager.getInstance().setData1(bannerBean);
                                    ((BaseActivity) mContext).gotoPager(BannerFragment.class, null);
                                }
                            }
                        }
                    });
            //设置翻页的效果，不需要翻页效果可用不设
            //.setPageTransformer(Transformer.DefaultTransformer);    集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
            // convenientBanner.setManualPageable(false);//设置不能手动影响
        } else if (currentType == TWO_BIG_BUTTON) {
            TwoBigButtonViewHolder twoBigButtonViewHolder = (TwoBigButtonViewHolder) holder;
            twoBigButtonViewHolder.iv_car_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BaseActivity) mContext).gotoPager(CarCheckServiceFragment.class, null);
                    //这样也可以
                    //                    Intent intent = new Intent(mContext, EmptyActivity.class);
                    //                    intent.putExtra(Constant.FRAGMENT_NAME, CarCheckServiceFragment.class.getName());
                    //                    mContext.startActivity(intent);
                }
            });
            twoBigButtonViewHolder.iv_rent_car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BaseActivity) mContext).gotoPager(CarRentFragment.class, null);
                }
            });
        } else if (currentType == TOP_LINE) {
            TopLineViewHolder topLineViewHolder = (TopLineViewHolder) holder;

            ArrayList<TopLineBean> topLineBeanArrayList = getTopLineData(jsonObjectData);
            ArrayList<LampBean> list = new ArrayList<>();
            for (int i = 0; i < topLineBeanArrayList.size(); i++) {
                LampBean bean = new LampBean();
                bean.imgUrl = topLineBeanArrayList.get(i).getImg_url();
                bean.info = topLineBeanArrayList.get(i).getContent();
                list.add(bean);
            }

            topLineViewHolder.vertical_lamp_view.setData(list);
            topLineViewHolder.vertical_lamp_view.setTextSize(15);
            topLineViewHolder.vertical_lamp_view.setTimer(4000);
            topLineViewHolder.vertical_lamp_view.start();

            topLineViewHolder.vertical_lamp_view.setOnItemClickListener(new BaseAutoScrollView.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    //                ToastUtils.showShort("" + position);
                }
            });
        } else if (currentType == NEW_CAR) {
            NewCarViewHolder newCarViewHolder = (NewCarViewHolder) holder;
            newCarViewHolder.tv_more_new_car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) mContext).setCheck((RadioButton) ((MainActivity) mContext).findViewById(R.id.rb_car_center));
                }
            });

            ArrayList<CarInfoBean> carInfoBeanArrayList = getNewCarData(jsonObjectData);
            BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, carInfoBeanArrayList, NEW_CAR_DATA_TYPE);
            newCarViewHolder.most_new_car_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            newCarViewHolder.most_new_car_recyclerView.setAdapter(baseRecyclerViewAdapter);
            baseRecyclerViewAdapter.setOnItemClickListener(this);
        } else if (currentType == CAR_VIDEO) {
            CarVideoViewHolder carVideoViewHolder = (CarVideoViewHolder) holder;
            carVideoViewHolder.tv_more_car_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BaseActivity) mContext).gotoPager(VideoListFragment.class, null);
                }
            });

            ArrayList<VideoBean> videoBeanArrayList = getVideoData(jsonObjectData);
            if (EmptyUtils.isNotEmpty(videoBeanArrayList)) {
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, videoBeanArrayList, CAR_VIDEO_DATA_TYPE);
                carVideoViewHolder.car_video_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                carVideoViewHolder.car_video_recyclerView.setAdapter(baseRecyclerViewAdapter);
                baseRecyclerViewAdapter.setOnItemClickListener(this);
            }
        } else if (currentType == CAR_ARTICLE) {
            CarArticleViewHolder carArticleViewHolder = (CarArticleViewHolder) holder;
            carArticleViewHolder.tv_more_article_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BaseActivity) mContext).gotoPager(ArticleListFragment.class, null);
                }
            });

            ArrayList<ArticleBean> articleBeanArrayList = getArticleData(jsonObjectData);
            if (EmptyUtils.isNotEmpty(articleBeanArrayList)) {
                //第一篇文章
                final ArticleBean articleBean1 = articleBeanArrayList.get(0);
                Glide.with(mContext)
                        .load(articleBean1.getImage_url().get(0))
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(carArticleViewHolder.iv_article_img);
                carArticleViewHolder.tv_article_title.setText(articleBean1.getPost_content());
                carArticleViewHolder.tv_article_info.setText(articleBean1.getTitle());

                carArticleViewHolder.ll_article_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constant.FEED_ID, articleBean1.getFeed_id());
                        ((BaseActivity) mContext).gotoPager(ArticleDetailFragment.class, bundle);
                    }
                });
                //第二篇文章
                final ArticleBean articleBean2 = articleBeanArrayList.get(1);
                Glide.with(mContext)
                        .load(articleBean2.getImage_url().get(0))
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(carArticleViewHolder.iv_article_information);
                carArticleViewHolder.tv_title.setText(articleBean2.getPost_content());
                carArticleViewHolder.tv_user_name.setText(articleBean2.getPost_user_info().getProfile().getNickname());

                carArticleViewHolder.rl_article_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constant.FEED_ID, articleBean2.getFeed_id());
                        ((BaseActivity) mContext).gotoPager(ArticleDetailFragment.class, bundle);
                    }
                });
                //第三篇文章
                final ArticleBean articleBean3 = articleBeanArrayList.get(2);
                Glide.with(mContext)
                        .load(articleBean3.getImage_url().get(0))
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(carArticleViewHolder.iv_article_information1);
                carArticleViewHolder.tv_title1.setText(articleBean3.getPost_content());
                carArticleViewHolder.tv_user_name1.setText(articleBean3.getPost_user_info().getProfile().getNickname());

                carArticleViewHolder.rl_article_item3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constant.FEED_ID, articleBean3.getFeed_id());
                        ((BaseActivity) mContext).gotoPager(ArticleDetailFragment.class, bundle);
                    }
                });
            }
        } else if (currentType == CHE_HANG) {
            CheHangViewHolder cheHangViewHolder = (CheHangViewHolder) holder;
            cheHangViewHolder.tv_more_car_company.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BaseActivity) mContext).gotoPager(CheHangListFragment.class, null);
                }
            });

            ArrayList<CheHangHomeBean> cheHangHomeBeanArrayList = getCheHangData(jsonObjectData);
            if (EmptyUtils.isNotEmpty(cheHangHomeBeanArrayList)) {
                BaseRecyclerViewAdapter baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(mContext, cheHangHomeBeanArrayList, CHE_HANG_HOME_DATA_TYPE);
                cheHangViewHolder.car_company_recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                cheHangViewHolder.car_company_recyclerView.setAdapter(baseRecyclerViewAdapter);
                baseRecyclerViewAdapter.setOnItemClickListener(this);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case BANNER:
                currentType = BANNER;
                break;
            case TWO_BIG_BUTTON:
                currentType = TWO_BIG_BUTTON;
                break;
            case TOP_LINE:
                currentType = TOP_LINE;
                break;
            case NEW_CAR:
                currentType = NEW_CAR;
                break;
            case CAR_VIDEO:
                currentType = CAR_VIDEO;
                break;
            case CAR_ARTICLE:
                currentType = CAR_ARTICLE;
                break;
            case CHE_HANG:
                currentType = CHE_HANG;
                break;
            default:
                currentType = BANNER;
                break;
        }
        return currentType;
    }

    @Override
    public void onItemClick(Object data, int position) {
        if (data.getClass().getSimpleName().equals("CarInfoBean")) {
            CarInfoBean carInfoBean = (CarInfoBean) data;
            if (EmptyUtils.isNotEmpty(carInfoBean)) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.CAR_ID, carInfoBean.getCar_id());
                ((BaseActivity) mContext).gotoPager(CarDetailFragment.class, bundle);
            }
        } else if (data.getClass().getSimpleName().equals("VideoBean")) {
            VideoBean videoBean = (VideoBean) data;
            if (EmptyUtils.isNotEmpty(videoBean)) {
                int feed_id = videoBean.getFeed_id();
                DataManager.getInstance().setData1(feed_id);
                ((BaseActivity) mContext).gotoPager(VideoDetailFragment.class, null);
            }
        } else if (data.getClass().getSimpleName().equals("CheHangHomeBean")) {
            CheHangHomeBean cheHangHomeBean = (CheHangHomeBean) data;
            ToastUtils.showShort(cheHangHomeBean.getNickname());
        }
    }

    private class BannerViewHolder extends RecyclerView.ViewHolder {

        private ConvenientBanner convenientBanner;

        BannerViewHolder(View itemView) {
            super(itemView);
            convenientBanner = (ConvenientBanner) itemView.findViewById(R.id.convenientBanner);
        }
    }

    private class TwoBigButtonViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_car_check;
        private ImageView iv_rent_car;

        TwoBigButtonViewHolder(View itemView) {
            super(itemView);
            iv_car_check = (ImageView) itemView.findViewById(R.id.iv_car_check);
            iv_rent_car = (ImageView) itemView.findViewById(R.id.iv_rent_car);
        }
    }

    private class TopLineViewHolder extends RecyclerView.ViewHolder {

        private VerticalLampView vertical_lamp_view;

        TopLineViewHolder(View itemView) {
            super(itemView);
            vertical_lamp_view = (VerticalLampView) itemView.findViewById(R.id.vertical_lamp_view);
        }
    }

    private class NewCarViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_more_new_car;
        private RecyclerView most_new_car_recyclerView;

        NewCarViewHolder(View itemView) {
            super(itemView);
            tv_more_new_car = (TextView) itemView.findViewById(R.id.tv_more_new_car);
            most_new_car_recyclerView = (RecyclerView) itemView.findViewById(R.id.most_new_car_recyclerView);
        }
    }

    private class CarVideoViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_more_car_video;
        private RecyclerView car_video_recyclerView;

        CarVideoViewHolder(View itemView) {
            super(itemView);
            tv_more_car_video = (TextView) itemView.findViewById(R.id.tv_more_car_video);
            car_video_recyclerView = (RecyclerView) itemView.findViewById(R.id.car_video_recyclerView);
        }
    }

    private class CarArticleViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_more_article_video;
        private LinearLayout ll_article_item1;
        private RelativeLayout rl_article_item2;
        private RelativeLayout rl_article_item3;

        private ImageView iv_article_img;
        private TextView tv_article_title;
        private TextView tv_article_info;

        private ImageView iv_article_information;
        private TextView tv_title;
        private TextView tv_user_name;

        private ImageView iv_article_information1;
        private TextView tv_title1;
        private TextView tv_user_name1;

        CarArticleViewHolder(View itemView) {
            super(itemView);
            tv_more_article_video = (TextView) itemView.findViewById(R.id.tv_more_article_video);
            ll_article_item1 = (LinearLayout) itemView.findViewById(R.id.ll_article_item1);
            rl_article_item2 = (RelativeLayout) itemView.findViewById(R.id.rl_article_item2);
            rl_article_item3 = (RelativeLayout) itemView.findViewById(R.id.rl_article_item3);

            iv_article_img = (ImageView) itemView.findViewById(R.id.iv_article_img);
            tv_article_title = (TextView) itemView.findViewById(R.id.tv_article_title);
            tv_article_info = (TextView) itemView.findViewById(R.id.tv_article_info);

            iv_article_information = (ImageView) itemView.findViewById(R.id.iv_article_information);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);

            iv_article_information1 = (ImageView) itemView.findViewById(R.id.iv_article_information1);
            tv_title1 = (TextView) itemView.findViewById(R.id.tv_title1);
            tv_user_name1 = (TextView) itemView.findViewById(R.id.tv_user_name1);
        }
    }

    private class CheHangViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_more_car_company;
        private RecyclerView car_company_recyclerView;

        CheHangViewHolder(View itemView) {
            super(itemView);
            tv_more_car_company = (TextView) itemView.findViewById(R.id.tv_more_car_company);
            car_company_recyclerView = (RecyclerView) itemView.findViewById(R.id.car_company_recyclerView);
        }
    }

    private class LocalImageHolderView implements Holder<BannerBean> {

        private ImageView iv_image;
        private TextView tv_title;

        @Override
        public View createView(Context context) {
            View view = View.inflate(context, R.layout.item_banner, null);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, BannerBean data) {
            if (data != null) {
                Glide.with(context)
                        .load(data.getImgUrl())
                        .placeholder(R.drawable.default_bg_ratio_1)
                        .into(iv_image);
                tv_title.setText(data.getTitle());
            }
        }
    }

    private ArrayList<BannerBean> getBannerData(JSONObject jsonObjectData) {
        ArrayList<BannerBean> list = null;
        if (jsonObjectData == null) {
            return new ArrayList<>();
        } else {
            JSONArray jsonArray = jsonObjectData.optJSONArray("banners");
            Gson gson = new Gson();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<BannerBean>>() {
            }.getType());
        }
        return list;
    }

    private ArrayList<TopLineBean> getTopLineData(JSONObject jsonObjectData) {
        ArrayList<TopLineBean> list = null;
        if (jsonObjectData == null) {
            return new ArrayList<>();
        } else {
            JSONArray jsonArray = jsonObjectData.optJSONArray("news");
            Gson gson = new Gson();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<TopLineBean>>() {
            }.getType());
        }
        return list;
    }

    private ArrayList<CarInfoBean> getNewCarData(JSONObject jsonObjectData) {
        ArrayList<CarInfoBean> list = null;
        if (jsonObjectData == null) {
            return new ArrayList<>();
        } else {
            list = new ArrayList<>();
            JSONArray jsonArray = jsonObjectData.optJSONObject("car_list").optJSONArray("car_list");
            int size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i).optJSONObject("car_info");
                Gson gson = new Gson();
                CarInfoBean carInfoBean = gson.fromJson(jsonObject.toString(), CarInfoBean.class);
                list.add(carInfoBean);
            }
        }
        return list;
    }

    private ArrayList<VideoBean> getVideoData(JSONObject jsonObjectData) {
        ArrayList<VideoBean> list = null;
        if (jsonObjectData == null) {
            return new ArrayList<>();
        } else {
            JSONArray ja = jsonObjectData.optJSONObject("videos").optJSONArray("list");
            Gson gson = new Gson();
            list = gson.fromJson(ja.toString(), new TypeToken<ArrayList<VideoBean>>() {
            }.getType());
        }
        return list;
    }

    private ArrayList<ArticleBean> getArticleData(JSONObject jsonObjectData) {
        ArrayList<ArticleBean> list = null;
        if (jsonObjectData == null) {
            return new ArrayList<>();
        } else {
            JSONArray jsonArray = jsonObjectData.optJSONObject("article").optJSONArray("list");
            Gson gson = new Gson();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<ArticleBean>>() {
            }.getType());
        }
        return list;
    }

    private ArrayList<CheHangHomeBean> getCheHangData(JSONObject jsonObjectData) {
        ArrayList<CheHangHomeBean> list = null;
        if (jsonObjectData == null) {
            return new ArrayList<>();
        } else {
            JSONArray jsonArray = jsonObjectData.optJSONObject("company_list").optJSONArray("user_list");
            Gson gson = new Gson();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CheHangHomeBean>>() {
            }.getType());
        }
        return list;
    }
}
