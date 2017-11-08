package com.wiserz.pbibi.fragment;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wiserz.pbibi.R;
import com.wiserz.pbibi.activity.BaseActivity;
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

/**
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

    public class MediaInfo {
        public File mediaFile;
        public long mediaAddTime;
        public boolean isSelected;
        public int index;
    }

    private ArrayList<MediaFileInfo> mMediaFileInfos = new ArrayList<>();
    private Cursor mCursor;
    private ShowMediasAdapter mMediasAdapter;
    private MediaFileAdapter mMediaFileAdapter;

    private SelectedPhotoAdapter mSelectedPhotoAdapter;
    private ArrayList<File> mSelectedPhotos;
    private ArrayList<MediaInfo> mSelectedMediaInfos;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            goBack();
            return;
        }
        DataManager.getInstance().setObject(null);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_album;
    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.btnBack).setOnClickListener(this);
        view.findViewById(R.id.tvLeft).setOnClickListener(this);
        view.findViewById(R.id.tvAlbum).setOnClickListener(this);
        view.findViewById(R.id.alphaView).setOnClickListener(this);
        view.findViewById(R.id.bottomView).setOnClickListener(this);
        view.findViewById(R.id.alphaView).setVisibility(View.GONE);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.selectPhotoView);
        LinearLayoutManager linearLayoutManagerHorizontal = new LinearLayoutManager(getActivity());
        linearLayoutManagerHorizontal.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManagerHorizontal);
        recyclerView.setAdapter(getSelectedPhotoAdapter());
    }

    private SelectedPhotoAdapter getSelectedPhotoAdapter() {
        if (mSelectedPhotoAdapter == null) {
            mSelectedPhotoAdapter = new SelectedPhotoAdapter(getActivity());
            mSelectedPhotoAdapter.setOnPhotoDelete(new SelectedPhotoAdapter.OnPhotoOperator() {
                @Override
                public void onDelete(int position) {
                    MediaInfo mediaInfo = getSelectedMediaInfos().remove(position);
                    mediaInfo.isSelected = false;
                    int index = 0;
                    for (MediaInfo info : getSelectedMediaInfos()) {
                        info.index = (index++);
                    }
                    mMediasAdapter.notifyDataSetChanged();
                    showView();
                }
            });
        }
        return mSelectedPhotoAdapter;
    }

    private ArrayList<File> getSelectedPhotos() {
        if (mSelectedPhotos == null) {
            mSelectedPhotos = new ArrayList<>();
        }
        return mSelectedPhotos;
    }

    private ArrayList<MediaInfo> getSelectedMediaInfos() {
        if (mSelectedMediaInfos == null) {
            mSelectedMediaInfos = new ArrayList<>();
        }
        return mSelectedMediaInfos;
    }

    private void showView() {
        if (getSelectedPhotos().size() > 0) {
            getView().findViewById(R.id.selectPhotoView).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.pointView).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.llBottom).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.tvOk).setVisibility(View.VISIBLE);
            ((TextView) getView().findViewById(R.id.tvOk)).setText(getString(R.string.make_sure) + " (" + getSelectedPhotos().size() + ")");
        } else {
            getView().findViewById(R.id.selectPhotoView).setVisibility(View.GONE);
            getView().findViewById(R.id.pointView).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.llBottom).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.tvOk).setVisibility(View.INVISIBLE);
        }
    }

    public void onResume() {
        super.onResume();
        showView();
        ((BaseActivity) getActivity()).setScreenFull(false);
        if (mMediaFileAdapter == null || mMediaFileInfos.isEmpty()) {
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
                if (getSelectedPhotos().size() == 6) {
                    return;
                }
                ImageView iv = (ImageView) view.findViewById(R.id.iv);
                final MediaInfo info = (MediaInfo) iv.getTag(R.id.tag);
                if (info != null && iv.getDrawable() != null) {
                    DisplayMetrics dm = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                    Bitmap bmp = CommonUtil.getBitmapFromFile(info.mediaFile, dm.widthPixels, dm.heightPixels);
                    if (bmp == null) {
                        return;
                    }
                    String path = CommonUtil.saveJpeg(bmp, getActivity());
                    bmp.recycle();
                    getView().findViewById(R.id.selectPhotoView).setVisibility(View.VISIBLE);
                    getSelectedPhotos().add(new File(path));
                    getSelectedPhotoAdapter().setDataList(getSelectedPhotos());
                    info.isSelected = true;
                    info.index = getSelectedPhotos().size() - 1;
                    getSelectedMediaInfos().add(info);
                    mMediasAdapter.notifyDataSetChanged();
                    showView();
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
                goBack();
                break;
            case R.id.tvLeft:
                gotoPager(CameraFragment.class, null, true);
                break;
            case R.id.bottomView:
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
