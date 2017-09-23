package com.wiserz.pbibi.fragment;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.DataManager;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by jackie on 2017/8/31 9:49.
 * QQ : 971060378
 * Used as : 展示所有图片的页面
 */
public class ShowAllImageFragment extends BaseFragment {

    private ViewPager view_pager;

    private ArrayList<String> stringImageUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_show_all_image;
    }

    @Override
    protected void initView(View view) {
        stringImageUrl = (ArrayList<String>) DataManager.getInstance().getData1();
        DataManager.getInstance().setData1(null);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        view_pager = (ViewPager) view.findViewById(R.id.view_pager);

        if (EmptyUtils.isNotEmpty(stringImageUrl) && stringImageUrl.size() != 0) {
            ((TextView) view.findViewById(R.id.tv_title)).setText(stringImageUrl.size() + "张");
            view_pager.setAdapter(new ShowAllImagePagerAdapter());
        } else {
            ((TextView) view.findViewById(R.id.tv_title)).setText("没有图片");
        }
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

    private class ShowAllImagePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return stringImageUrl.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final PhotoView photoView = new PhotoView(mContext);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            Glide.with(mContext)
                    .load(stringImageUrl.get(position))
                    .placeholder(R.drawable.default_bg_ratio_1)
                    .error(R.drawable.default_bg_ratio_1)
                    .into(photoView);

            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // 判断是否存在SD卡
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                        final Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                if (msg.what == 55) {
                                    Bitmap bitmap = (Bitmap) msg.obj;
                                    CommonUtil.savePhotoToAppAlbum(bitmap, mContext);
                                    ToastUtils.showShort("保存成功");
                                }
                            }
                        };

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Bitmap bitmap = Glide.with(mContext)
                                            .load(stringImageUrl.get(position))
                                            .asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                            .get();
                                    handler.obtainMessage(55, bitmap);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        //                        new Thread(new Runnable() {
                        //                            @Override
                        //                            public void run() {
                        //                                try {
                        //                                    Bitmap bitmap = Glide.with(mContext)
                        //                                            .load(stringImageUrl.get(position))
                        //                                            .asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        //                                            .get();
                        //                                    // 获取SD卡的目录
                        //                                    File sdDire = Environment.getExternalStorageDirectory();
                        //                                    File real_save_dir = new File(sdDire.getAbsolutePath() + "/AAA/");
                        //                                    ImageUtils.save(bitmap, real_save_dir, Bitmap.CompressFormat.PNG);
                        //                                    ToastUtils.showShort("成功保存图片至：" + real_save_dir.getAbsolutePath());
                        //                                } catch (InterruptedException e) {
                        //                                    e.printStackTrace();
                        //                                } catch (ExecutionException e) {
                        //                                    e.printStackTrace();
                        //                                }
                        //                            }
                        //                        }).start();
                    }
                    return true;
                }
            });

            return photoView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
