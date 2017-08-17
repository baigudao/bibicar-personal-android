package com.wiserz.pbibi.fragment;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.VideoBean;
import com.wiserz.pbibi.util.DataManager;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by jackie on 2017/8/17 14:43.
 * QQ : 971060378
 * Used as : 视频详情的页面
 */
public class VideoDetailFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video_detail;
    }

    @Override
    protected void initView(View view) {
        VideoBean videoBean = (VideoBean) DataManager.getInstance().getData1();
        if (EmptyUtils.isNotEmpty(videoBean)) {
            JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) view.findViewById(R.id.videoPlayer);
            jcVideoPlayerStandard.setUp(videoBean.getHtml_url()
                    , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, videoBean.getPost_content());
            jcVideoPlayerStandard.thumbImageView.setImageURI(Uri.parse(videoBean.getImage_url()));
        }

        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("视频详情");
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
    public void onPause() {
        super.onPause();
        //释放资源
        JCVideoPlayer.releaseAllVideos();
    }
}
