package com.wiserz.pbibi.adapter;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.ContactsBean;
import com.wiserz.pbibi.util.PinYinComparator;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by jackie on 2017/10/13 14:12.
 * QQ : 971060378
 * Used as : xxx
 */
public class ContactsAdapter extends BaseAdapter {

    private static final String TAG = ContactsAdapter.class.getSimpleName();
    private FragmentActivity act;
    private List<ContactsBean> datas;

    public ContactsAdapter(FragmentActivity act, List<ContactsBean> datas) {
        this.act = act;
        this.datas = datas;
        for (ContactsBean c1 : datas) {
            Log.i("ContactsC1", c1.getName() + c1.getPhone());
        }
        Collections.sort(datas, new PinYinComparator());
        for (ContactsBean c2 : datas) {
            Log.i("ContactsC2", c2.getName() + c2.getPhone());
        }
    }

    @Override
    public int getCount() {
        if (datas == null || datas.size() == 0 || datas.equals("")) {
            return 0;
        }
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = act.getLayoutInflater().inflate(R.layout.contacts_listview_item, null);
            vh = new ViewHolder();
            vh.ivInvite = (ImageView) convertView.findViewById(R.id.ivInvite);
           /* vh.ivInvite.setTag(position);
            vh.ivInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    Utils.showToast(act,pos + "");
                    Utils.showToast(act,((ContactsBean)(ContactsAdapter.this.getItem(pos))).getName());
                }
            });*/
            vh.tvName = (TextView) convertView.findViewById(R.id.tvName);
            vh.mCharCategoryText = (TextView) convertView.findViewById(R.id.tv_header_char);
            vh.mDivider = convertView.findViewById(R.id.divider);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        ContactsBean contactBean = datas.get(position);
        if (contactBean.getUserinfo() == null || contactBean.getUserinfo().equals("")) {
            vh.ivInvite.setImageResource(R.drawable.invite);
        } else {
            int is_friend = contactBean.getUserinfo().getIs_friend();
            if (is_friend == 1) {
                //已经是朋友了
                vh.ivInvite.setImageResource(R.drawable.other_followed);//已关注
            } else if (is_friend == 2) {
                //不是朋友
                vh.ivInvite.setImageResource(R.drawable.other_follow);//关注
            }
        }

        // 获取当前联系人的首字母
        char headerChar = getHeaderCharByPosition(position);
        // 获取当前联系人首字母第一次出现的位置
        int index = getFirstPositionByHeaderChar(headerChar);

        // 如果当前联系人首字母第一次出现的位置等于当前的位置，则表示该联系人是该首字母下出现的第一个联系人
        if (position == index) {
            vh.mCharCategoryText.setVisibility(View.VISIBLE);
            vh.mCharCategoryText.setText(datas.get(position).getLetter());
            vh.mDivider.setVisibility(View.GONE);
        } else {
            // 默认设置字母栏不显示
            vh.mCharCategoryText.setVisibility(View.GONE);
            vh.mDivider.setVisibility(View.VISIBLE);
        }

        vh.tvName.setText(datas.get(position).getName());
        return convertView;
    }

    /**
     * 通过联系人的位置获取该联系人的名称的首字母
     *
     * @param position
     * @return 首字母
     */
    private char getHeaderCharByPosition(int position) {
        return datas.get(position).getLetter().toUpperCase().charAt(0);
    }

    /**
     * 通过首字母获取显示该首字母的第一个联系人的位置：比如C，陈奕迅
     *
     * @param c
     * @return 位置，如果返回-1表示未查找到该字母
     */
    public int getFirstPositionByHeaderChar(char c) {
        for (int i = 0; i < getCount(); i++) {
            String headerChar = datas.get(i).getLetter();
            char firstChar = headerChar.toUpperCase(Locale.CHINA).charAt(0);
            if (firstChar == c) {
                return i;
            }
        }
        return -1;
    }

    static class ViewHolder {
        ImageView ivInvite;
        TextView tvName;
        TextView mCharCategoryText;
        View mDivider;
    }
}
