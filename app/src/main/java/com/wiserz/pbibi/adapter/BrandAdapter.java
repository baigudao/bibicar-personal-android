package com.wiserz.pbibi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.BrandInfoBean;

import java.util.ArrayList;
import java.util.List;

import cc.solart.wave.BaseTurboAdapter;
import cc.solart.wave.BaseViewHolder;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by jackie on 2017/9/9 15:36.
 * QQ : 971060378
 * Used as : BrandAdapter
 */
public class BrandAdapter extends BaseTurboAdapter<BrandInfoBean, BaseViewHolder> {

    private ArrayList<BrandInfoBean> brandInfoBeanArrayList;
    private Context mContext;

    public BrandAdapter(Context context, List<BrandInfoBean> data) {
        super(context, data);
        this.mContext = context;
        this.brandInfoBeanArrayList = (ArrayList<BrandInfoBean>) data;
    }

    @Override
    protected int getDefItemViewType(int position) {
        BrandInfoBean brandInfoBean = getItem(position);
        return brandInfoBean.getType();
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new BrandHolder(inflateItemView(R.layout.item_brand, parent));
        } else {
            return new PinnedHolder(inflateItemView(R.layout.item_pinned_header, parent));
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, BrandInfoBean item) {
        if (holder instanceof BrandHolder) {
            Glide.with(mContext)
                    .load(item.getBrand_url())
                    .placeholder(R.drawable.default_bg_ratio_1)
                    .error(R.drawable.default_bg_ratio_1)
                    .bitmapTransform(new RoundedCornersTransformation(mContext, SizeUtils.dp2px(8), 0, RoundedCornersTransformation.CornerType.ALL))
                    .into(((BrandHolder) holder).iv_image_brand_item);
            ((BrandHolder) holder).brand_name.setText(item.getBrand_name());
        } else {
            ((PinnedHolder) holder).brand_tip.setText(item.getAbbre().toUpperCase());
        }
    }

    public int getLetterPosition(String letter) {
        for (int i = 0; i < getData().size(); i++) {
            if (getData().get(i).getType() == 1 && getData().get(i).getAbbre().equals(letter)) {
                return i;
            }
        }
        return -1;
    }

    private class BrandHolder extends BaseViewHolder {

        private TextView brand_name;
        private ImageView iv_image_brand_item;
        private RelativeLayout rl_item_brand;

        BrandHolder(View view) {
            super(view);
            brand_name = (TextView) view.findViewById(R.id.brand_name);
            iv_image_brand_item = (ImageView) view.findViewById(R.id.iv_image_brand_item);
            rl_item_brand = (RelativeLayout) view.findViewById(R.id.rl_item_brand);

            rl_item_brand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        int position = getLayoutPosition();
                        mOnItemClickListener.onItemClick(brandInfoBeanArrayList.get(position), position);
                    }
                }
            });
        }
    }

    private class PinnedHolder extends BaseViewHolder {

        private TextView brand_tip;

        PinnedHolder(View view) {
            super(view);
            brand_tip = findViewById(R.id.brand_tip);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Object data, int position);
    }

    public BaseRecyclerViewAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(BaseRecyclerViewAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
