package com.wiserz.pbibi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wiserz.pbibi.R;
import com.wiserz.pbibi.fragment.AlbumFragment;

import java.util.ArrayList;

/**
 * Created by gigabud on 16-2-19.
 */
public class MediaFileAdapter extends BaseAdapter {
    private ArrayList<AlbumFragment.MediaFileInfo> mMediaFileInfos;
    private Context context;

    public MediaFileAdapter(Context context) {
        this.context = context;
    }

    public void resetMediaFiles(ArrayList<AlbumFragment.MediaFileInfo> mediaFileInfos) {
        mMediaFileInfos = mediaFileInfos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mMediaFileInfos == null ? 0 : mMediaFileInfos.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mMediaFileInfos == null ? null : mMediaFileInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if ( convertView == null ) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.image_file_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvFileName = (TextView) convertView.findViewById(R.id.tvFileName);
            viewHolder.tvImageNum = (TextView) convertView.findViewById(R.id.tvImageNum);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AlbumFragment.MediaFileInfo mediaFileInfo = mMediaFileInfos.get(position);
        viewHolder.tvFileName.setText(mediaFileInfo.mediaLastFileName);
        viewHolder.tvImageNum.setText("(" + mediaFileInfo.mediaInfoList.size() + ")");
        return convertView;
    }

    class ViewHolder {
        public TextView tvFileName;
        public TextView tvImageNum;
    }
}
