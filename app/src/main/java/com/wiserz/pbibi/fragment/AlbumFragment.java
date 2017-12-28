package com.wiserz.pbibi.fragment;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.MediaFileAdapter;
import com.wiserz.pbibi.adapter.SelectedPhotoAdapter;
import com.wiserz.pbibi.adapter.ShowMediasAdapter;
import com.wiserz.pbibi.util.CommonUtil;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.util.GBExecutionPool;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/** 相册选择
 * Created by gigabud on 16-6-21.
 */
public class AlbumFragment extends BaseFragment {
    private MediaMetadataRetriever mMediaMetadataRetriever;

    public class MediaFileInfo {
        public String mediaLastFileAbsoluteName;
        public ArrayList<MediaInfo> mediaInfoList;
        public String mediaLastFileName;
    }

    private static final String CAMERA_PATH = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM;

    public class MediaInfo {//选中的item对象
        public File mediaFile;
        public long mediaAddTime;
        public boolean isSelected;
        public int index;
    }

    private ArrayList<MediaFileInfo> mMediaFileInfos;
    private Cursor mCursor;
    private ShowMediasAdapter mMediasAdapter;
    private MediaFileAdapter mMediaFileAdapter;
    private SelectedPhotoAdapter mSelectedPhotoAdapter;
    private Map<Integer,MediaInfo> mSelectedMediaInfos;//存放选择的相片对象
    private SelectPhotoFragment mSelectPhotoFragment;
    private LinearLayoutManager llManager;

    public BaseFragment setParentFragment(SelectPhotoFragment selectPhotoFragment) {
        mSelectPhotoFragment = selectPhotoFragment;
        return this;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(DataManager.getInstance().getObject()!=null){
//            mSelectedPhotos=(ArrayList<File>) DataManager.getInstance().getObject();
//        }
        DataManager.getInstance().setObject(null);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_album;
    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.btnBack).setOnClickListener(this);
        view.findViewById(R.id.tvAlbum).setOnClickListener(this);
        view.findViewById(R.id.alphaView).setOnClickListener(this);
        view.findViewById(R.id.tvOk).setOnClickListener(this);
        view.findViewById(R.id.alphaView).setVisibility(View.GONE);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.selectPhotoView);
        llManager = new LinearLayoutManager(getActivity());
        llManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llManager);
        recyclerView.setAdapter(getSelectedPhotoAdapter());
        getSelectedPhotoAdapter().setDataList(mSelectPhotoFragment.getSelectedPhotos());
    }

    private SelectedPhotoAdapter getSelectedPhotoAdapter() {
        if (mSelectedPhotoAdapter == null) {
            mSelectedPhotoAdapter = new SelectedPhotoAdapter(getActivity());
            mSelectedPhotoAdapter.setOnPhotoDelete(new SelectedPhotoAdapter.OnPhotoOperator() {
                @Override
                public void onDelete(int position) {
                    mSelectPhotoFragment.getSelectedPhotos().remove(position+1);
                    MediaInfo mediaInfo = getSelectedMediaInfos().remove(position+1);
                    if(mediaInfo!=null){//删除的照片是选中状态，如果是重新进来的话，照片不是选择状态
                        mediaInfo.isSelected = false;
                        //                    int index = 0;
                        //                    for (MediaInfo info : getSelectedMediaInfos()) {
                        //                        info.index = (index++);
                        //                    }
                        mMediasAdapter.notifyDataSetChanged();
                    }

                    showView();
                }

                @Override
                public void onItemClick(int position) {

                }

            });
        }
        return mSelectedPhotoAdapter;
    }

    private Map<Integer,MediaInfo> getSelectedMediaInfos() {
        if (mSelectedMediaInfos == null) {
            mSelectedMediaInfos = new HashMap<Integer,MediaInfo>();
        }
        return mSelectedMediaInfos;
    }

    private void showView() {
        if (mSelectPhotoFragment.getSelectedPhotos().size() > 0) {
            getView().findViewById(R.id.selectPhotoView).setVisibility(View.VISIBLE);
            mSelectPhotoFragment.setViewPagerCanScroll(false);
            getView().findViewById(R.id.tvOk).setVisibility(View.VISIBLE);
            ((TextView) getView().findViewById(R.id.tvOk)).setText(getString(R.string.make_sure) + " (" + mSelectPhotoFragment.getSelectedPhotos().size() + ")");
        } else {
//            getView().findViewById(R.id.selectPhotoView).setVisibility(View.GONE);
            mSelectPhotoFragment.setViewPagerCanScroll(true);
            getView().findViewById(R.id.tvOk).setVisibility(View.INVISIBLE);
        }
    }

    public void onResume() {
        super.onResume();
        if (mMediaFileInfos == null) {
            mMediaFileInfos = new ArrayList<>();
            GBExecutionPool.getExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    getAlbumMedias();
                    if (mMediaFileInfos != null && mMediaFileInfos.size() > 1) {
                        Collections.sort(mMediaFileInfos, new SortByFileName());
                    }
                    for (MediaFileInfo mediaFileInfo : mMediaFileInfos) {
                        if (mediaFileInfo.mediaInfoList != null && mediaFileInfo.mediaInfoList.size() > 1) {
                            Collections.sort(mediaFileInfo.mediaInfoList, new SortByMediaAddTime());
                        }
                    }
                    if (getView() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int initOpenIndex = -1;
                                MediaFileInfo mediaFileInfo;
                                for (int i = 0; i < mMediaFileInfos.size(); ++i) {
                                    mediaFileInfo = mMediaFileInfos.get(i);
                                    if (mediaFileInfo.mediaLastFileAbsoluteName.contains(CAMERA_PATH + "/Camera") && mediaFileInfo.mediaLastFileName.equalsIgnoreCase("Camera")) {
                                        initOpenIndex = i;
                                        break;
                                    }
                                }
                                if (initOpenIndex == -1) {
                                    for (int i = 0; i < mMediaFileInfos.size(); ++i) {
                                        mediaFileInfo = mMediaFileInfos.get(i);
                                        if (mediaFileInfo.mediaLastFileAbsoluteName.contains(CAMERA_PATH)) {
                                            initOpenIndex = i;
                                            break;
                                        }
                                    }
                                }
                                initOpenIndex = Math.max(initOpenIndex, 0);
                                initGridView(initOpenIndex);
                                initListView(initOpenIndex);
                            }
                        });
                    }
                }
            });
        }
        if (mSelectPhotoFragment.getCurrentItem() == 1) {
            if (DataManager.getInstance().getObject() != null && DataManager.getInstance().getData1() != null) {
                String newPath = (String) DataManager.getInstance().getObject();
                int index = (int) DataManager.getInstance().getData1();
                mSelectPhotoFragment.getSelectedPhotos().put(index, new File(newPath));
                getSelectedPhotoAdapter().notifyDataSetChanged();
            } else if (DataManager.getInstance().getData8() != null) {
                int index = (int) DataManager.getInstance().getData8();
                mSelectPhotoFragment.getSelectedPhotos().remove(index);
                getSelectedPhotoAdapter().notifyDataSetChanged();
                MediaInfo mediaInfo = getSelectedMediaInfos().remove(index);
                mediaInfo.isSelected = false;
//                for (MediaInfo info : getSelectedMediaInfos()) {
//                    info.index = (index++);
//                }
                mMediasAdapter.notifyDataSetChanged();
            }
            DataManager.getInstance().setObject(null);
            DataManager.getInstance().setData1(null);
            DataManager.getInstance().setData8(null);
        }
        showView();

    }

    private void getAlbumMedias() {
        // 执行查询，返回一个cursor
        mCursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.DATE_TAKEN}, null,
                null, null);
        int filePathColumn = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        int bucketNameColumn = mCursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        int fileContentType = mCursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE);
        int dateAdded = mCursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);

        mCursor.moveToLast();
        String mediaFilePath, bunketName, mediaLastPath;
        String fileType;
        long mediaAddTime;
        while (mCursor != null && mCursor.getCount() > 0) {
            fileType = mCursor.getString(fileContentType);
            mediaFilePath = mCursor.getString(filePathColumn);
            if (fileType != null) {
                if (fileType.equalsIgnoreCase("image/gif")) {
                    if (mCursor.isFirst()) {
                        break;
                    }
                    mCursor.moveToPrevious();
                    continue;
                }
            } else {
                if (mCursor.isFirst()) {
                    break;
                }
                mCursor.moveToPrevious();
                continue;
            }
            bunketName = mCursor.getString(bucketNameColumn);
            mediaLastPath = mediaFilePath.split("/" + mCursor.getString(bucketNameColumn) + "/")[0] + ("/" + bunketName);
            try {
                mediaAddTime = mCursor.getLong(dateAdded);
            } catch (Exception e) {
                mediaAddTime = 0l;
            }
            addMediaFile(mediaLastPath, mediaFilePath, mediaAddTime);
            try {
                if (mCursor.isFirst()) {
                    break;
                }
                mCursor.moveToPrevious();
            } catch (Exception e) {
                break;
            }
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
            mCursor = null;
        }

        if (mMediaMetadataRetriever != null) {
            mMediaMetadataRetriever.release();
            mMediaMetadataRetriever = null;
        }
    }

    class SortByFileName implements Comparator {
        public int compare(Object o1, Object o2) {
            String name1 = ((MediaFileInfo) o1).mediaLastFileName.toLowerCase();
            String name2 = ((MediaFileInfo) o2).mediaLastFileName.toLowerCase();
            return name1.compareTo(name2);
        }
    }

    class SortByMediaAddTime implements Comparator {
        public int compare(Object o1, Object o2) {
            long mediaAddTime1 = ((MediaInfo) o1).mediaAddTime;
            long mediaAddTime2 = ((MediaInfo) o2).mediaAddTime;
            if (mediaAddTime1 >= mediaAddTime2) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    private void initListView(int openIndex) {
        final ListView listView = (ListView) getView().findViewById(R.id.list);
        mMediaFileAdapter = new MediaFileAdapter(getActivity());
        listView.setAdapter(mMediaFileAdapter);
        mMediaFileAdapter.resetMediaFiles(mMediaFileInfos);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaFileInfo mediaFileInfo = mMediaFileInfos.get(position);
                ((TextView) getView().findViewById(R.id.tvAlbum)).setText(mediaFileInfo.mediaLastFileName);
                mMediasAdapter.resetMediaFileInfo(mediaFileInfo);
                listView.setVisibility(View.GONE);
                getView().findViewById(R.id.alphaView).setVisibility(View.GONE);

            }
        });
        ((TextView) getView().findViewById(R.id.tvAlbum)).setText(mMediaFileInfos.isEmpty() ? "" : mMediaFileInfos.get(openIndex).mediaLastFileName);
    }

    private void initGridView(int openIndex) {
        final GridView gridView = (GridView) getView().findViewById(R.id.gridView);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mMediasAdapter = new ShowMediasAdapter(this);
        gridView.setAdapter(mMediasAdapter);
        mMediasAdapter.resetMediaFileInfo(mMediaFileInfos.isEmpty() ? null : mMediaFileInfos.get(openIndex));
        mMediasAdapter.setItemWidth(dm.widthPixels / 3 - CommonUtil.dip2px(getActivity(), 3));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView iv = (ImageView) view.findViewById(R.id.iv);
                final MediaInfo info = (MediaInfo) iv.getTag(R.id.tag);
//                if (info != null && info.isSelected) {//取消选中
//
//                    if(info.index != getSelectedPhotoAdapter().getSelectIndex()){//只能操作选中的角度index对应的图片
//                        return ;
//                    }
//
//                    info.isSelected = false;
//                    mSelectPhotoFragment.getSelectedPhotos().remove(info.index);
//                    getSelectedPhotoAdapter().notifyDataSetChanged();
//                    getSelectedMediaInfos().remove(info);
//                    int index = 0;
//                    for (MediaInfo mediaInfo : getSelectedMediaInfos()) {
//                        mediaInfo.index = (index++);
//                    }
//                    mMediasAdapter.notifyDataSetChanged();
//                    showView();
//                    return;
//                }
//                int currentNum=mSelectPhotoFragment.getSelectedPhotos().size() + mSelectPhotoFragment.getCurrentPhotoNum();
//                if (currentNum == Constant.MAX_UPLOAD_PHOTO_NUM) {
//                    Toast.makeText(getActivity(),"最多可以上传"+(Constant.MAX_UPLOAD_PHOTO_NUM-mSelectPhotoFragment.getCurrentPhotoNum())+"张照片",Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if(mSelectPhotoFragment.getSelectedPhotos().get(getSelectedPhotoAdapter().getSelectIndex()+1)!=null){
                    ToastUtils.showShort(getString(R.string.only_a_photo));
                    return;
                }

                if (info != null && !info.isSelected && iv.getDrawable() != null) {//选中
                    DisplayMetrics dm = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                    Bitmap bmp = CommonUtil.getBitmapFromFile(info.mediaFile, dm.widthPixels, dm.heightPixels);
                    if (bmp == null) {
                        return;
                    }
                    String path = CommonUtil.saveJpeg(bmp, getActivity());
                    bmp.recycle();
                    if (mSelectPhotoFragment.getCurrentPhotoNum() == -1) {//vin
                        DataManager.getInstance().setData9(path);
                        getActivity().finish();
                        return;
                    }

                    //当前选中的角度index
                    int  index = getSelectedPhotoAdapter().getSelectIndex();

                    //更新中间照片的选中状态
                    info.isSelected = true;
                    getSelectedMediaInfos().put(index+1,info);
                    mMediasAdapter.notifyDataSetChanged();

                    //更新底部recyclerview
                    mSelectPhotoFragment.getSelectedPhotos().put(index+1,new File(path));
                    if(index < 11){//自动跳到下一个,最大是11
                        ++ index;
                        getSelectedPhotoAdapter().setSelectIndex(index);
                        llManager.scrollToPositionWithOffset(index,0);
                    }
                    getSelectedPhotoAdapter().setDataList(mSelectPhotoFragment.getSelectedPhotos());

                    showView();
//                    if (mSelectPhotoFragment.getSelectedPhotos().size() + mSelectPhotoFragment.getCurrentPhotoNum() == Constant.MAX_UPLOAD_PHOTO_NUM) {
//                        Toast.makeText(getActivity(),"最多可以上传"+(Constant.MAX_UPLOAD_PHOTO_NUM-mSelectPhotoFragment.getCurrentPhotoNum())+"张照片",Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
            mCursor = null;
        }
        if (mMediaMetadataRetriever != null) {
            mMediaMetadataRetriever.release();
            mMediaMetadataRetriever = null;
        }
    }

    private void addMediaFile(String mediaLastPath, String mediaPath, long mediaAddTime) {
        for (MediaFileInfo imageFileInfo : mMediaFileInfos) {
            if (imageFileInfo.mediaLastFileAbsoluteName.equals(mediaLastPath)) {
                File mediaFile = new File(mediaPath);
                if (mediaFile.exists()) {
                    MediaInfo mediaInfo = new MediaInfo();
                    mediaInfo.mediaFile = mediaFile;
//                    mediaInfo.mediaType = mediaType;
                    mediaInfo.mediaAddTime = mediaAddTime;
                    if (mMediaMetadataRetriever == null) {
                        mMediaMetadataRetriever = new MediaMetadataRetriever();
                    }
                    imageFileInfo.mediaInfoList.add(mediaInfo);
                }
                return;
            }
        }
        File mediaFile = new File(mediaPath);
        if (mediaFile.exists()) {
            ArrayList<MediaInfo> list = new ArrayList<>();
            MediaInfo mediaInfo = new MediaInfo();
            mediaInfo.mediaFile = new File(mediaPath);
//            mediaInfo.mediaType = mediaType;
            mediaInfo.mediaAddTime = mediaAddTime;
            if (mMediaMetadataRetriever == null) {
                mMediaMetadataRetriever = new MediaMetadataRetriever();
            }
            list.add(mediaInfo);
            MediaFileInfo mediaFileInfo = new MediaFileInfo();
            mediaFileInfo.mediaLastFileAbsoluteName = mediaLastPath;
            mediaFileInfo.mediaInfoList = list;
            String[] fileNames = mediaLastPath.split("/");
            mediaFileInfo.mediaLastFileName = fileNames[fileNames.length - 1];
            mMediaFileInfos.add(mediaFileInfo);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tvAlbum:
                if (mMediaFileAdapter == null) {
                    return;
                }
                ListView listView = (ListView) getView().findViewById(R.id.list);
                if (listView.getVisibility() == View.VISIBLE) {
                    listView.setVisibility(View.GONE);
                    getView().findViewById(R.id.alphaView).setVisibility(View.GONE);
                    return;
                }
                listView.setVisibility(View.VISIBLE);
                if (mMediaFileInfos.size() < 4) {
                    int height = CommonUtil.dip2px(getActivity(), 36) * mMediaFileInfos.size();
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) listView.getLayoutParams();
                    lp.height = height;
                    listView.setLayoutParams(lp);
                }
                getView().findViewById(R.id.alphaView).setVisibility(View.VISIBLE);
                mMediaFileAdapter.notifyDataSetChanged();
                break;
            case R.id.alphaView:
                v.setVisibility(View.GONE);
                getView().findViewById(R.id.list).setVisibility(View.GONE);
                break;
            case R.id.btnBack:
                DataManager.getInstance().setObject(mSelectPhotoFragment.getSelectedPhotos());
                getActivity().finish();
                break;
            case R.id.tvOk:
                DataManager.getInstance().setObject(mSelectPhotoFragment.getSelectedPhotos());
                if(!TextUtils.isEmpty(mSelectPhotoFragment.getFromClass())){
                    gotoPager(mSelectPhotoFragment.isPostNewCar()?PostNewCarFragment.class:PostSecondHandCarFragment.class,null);
                }
                getActivity().finish();
                break;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
            mCursor = null;
        }
        if (mMediaMetadataRetriever != null) {
            mMediaMetadataRetriever.release();
            mMediaMetadataRetriever = null;
        }
    }

}
