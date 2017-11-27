package com.wiserz.pbibi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiserz.pbibi.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.rong.common.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imkit.widget.ProviderContainerView;
import io.rong.imkit.widget.adapter.BaseAdapter;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Conversation;

/**
 * Created by jackie on 2017/8/9 23:49.
 * QQ : 971060378
 * Used as : 自定义会话列表的adapter
 */

public class CustomConversationListAdapter extends BaseAdapter<UIConversation> {

    private LayoutInflater mInflater;
    private Context mContext;
    private CustomConversationListAdapter.OnPortraitItemClick mOnPortraitItemClick;

    public long getItemId(int position) {
        UIConversation conversation = (UIConversation) this.getItem(position);
        return conversation == null ? 0L : (long) conversation.hashCode();
    }

    public CustomConversationListAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(this.mContext);
    }

    public int findGatheredItem(Conversation.ConversationType type) {
        int index = this.getCount();
        int position = -1;

        while (index-- > 0) {
            UIConversation uiConversation = (UIConversation) this.getItem(index);
            if (uiConversation.getConversationType().equals(type)) {
                position = index;
                break;
            }
        }

        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        reSort();
        super.notifyDataSetChanged();
    }

    private void reSort() {
        UIConversation uiConversation;
        ArrayList<UIConversation> topList = new ArrayList<>();
        for (int i = 0; i < this.getCount(); ) {
            uiConversation = getItem(i);
            if (uiConversation.getConversationTargetId().equals("1")
                    || uiConversation.getConversationTargetId().equals("2")
                    || uiConversation.getConversationTargetId().equals("3")
                    || uiConversation.getConversationTargetId().equals("4")) {
                remove(i);
                topList.add(uiConversation);
            } else {
                ++i;
            }
        }
        Collections.sort(topList, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                return ((UIConversation) o1).getConversationTargetId().compareTo(((UIConversation) o2).getConversationTargetId());
            }
        });
        for (int i = 0; i < topList.size(); ++i) {
            add(topList.get(i), i);
        }
    }

    public int findPosition(Conversation.ConversationType type, String targetId) {
        int index = this.getCount();
        int position = -1;

        while (index-- > 0) {
            if (((UIConversation) this.getItem(index)).getConversationType().equals(type) && ((UIConversation) this.getItem(index)).getConversationTargetId().equals(targetId)) {
                position = index;
                break;
            }
        }

        return position;
    }

    protected View newView(Context context, int position, ViewGroup group) {
        View result = this.mInflater.inflate(io.rong.imkit.R.layout.rc_item_conversation, (ViewGroup) null);
        CustomConversationListAdapter.ViewHolder holder = new CustomConversationListAdapter.ViewHolder();
        holder.layout = this.findViewById(result, io.rong.imkit.R.id.rc_item_conversation);
        holder.leftImageLayout = this.findViewById(result, io.rong.imkit.R.id.rc_item1);
        holder.rightImageLayout = this.findViewById(result, io.rong.imkit.R.id.rc_item2);
        holder.leftImageView = (AsyncImageView) this.findViewById(result, io.rong.imkit.R.id.rc_left);
        holder.rightImageView = (AsyncImageView) this.findViewById(result, io.rong.imkit.R.id.rc_right);
        holder.contentView = (ProviderContainerView) this.findViewById(result, io.rong.imkit.R.id.rc_content);
        holder.unReadMsgCount = (TextView) this.findViewById(result, io.rong.imkit.R.id.rc_unread_message);
        holder.unReadMsgCountRight = (TextView) this.findViewById(result, io.rong.imkit.R.id.rc_unread_message_right);
        holder.unReadMsgCountIcon = (ImageView) this.findViewById(result, io.rong.imkit.R.id.rc_unread_message_icon);
        holder.unReadMsgCountRightIcon = (ImageView) this.findViewById(result, io.rong.imkit.R.id.rc_unread_message_icon_right);
        result.setTag(holder);
        return result;
    }

    protected void bindView(View v, int position, final UIConversation data) {
        CustomConversationListAdapter.ViewHolder holder = (CustomConversationListAdapter.ViewHolder) v.getTag();
        if (data != null) {
            IContainerItemProvider.ConversationProvider provider = RongContext.getInstance().getConversationTemplate(data.getConversationType().getName());
            if (provider == null) {
                RLog.e("ConversationListAdapter", "provider is null");
            } else {

                View view = holder.contentView.inflate(provider);
                if (data.getConversationTargetId().equals("1")) {
                    data.setUIConversationTitle(mContext.getString(R.string.system_message));
                    data.setUIConversationTime(0);
                } else if (data.getConversationTargetId().equals("2")) {
                    data.setUIConversationTitle(mContext.getString(R.string.comment_me));
                    data.setUIConversationTime(0);
                } else if (data.getConversationTargetId().equals("3")) {
                    data.setUIConversationTitle(mContext.getString(R.string.like_me));
                    data.setUIConversationTime(0);
                }else if (data.getConversationTargetId().equals("4")) {
                    data.setUIConversationTitle("BIBI小管家");
                    data.setUIConversationTime(0);
                }
                provider.bindView(view, position, data);
                if (data.isTop()) {
                    holder.layout.setBackgroundDrawable(this.mContext.getResources().getDrawable(R.drawable.rc_item_top_list_selector));//设置item的布局背景
                } else {
                    holder.layout.setBackgroundDrawable(this.mContext.getResources().getDrawable(R.drawable.rc_item_list_selector));
                }


                //ConversationProviderTag为一个接口
                ConversationProviderTag tag = RongContext.getInstance().getConversationProviderTag(data.getConversationType().getName());
                //                boolean defaultId = false;
                int defaultId1;
                if (tag.portraitPosition() == 1) { //肖像位置为1，来的消息
                    holder.leftImageLayout.setVisibility(View.VISIBLE);
                    if (data.getConversationType().equals(Conversation.ConversationType.GROUP)) {//群组聊天
                        defaultId1 = R.drawable.rc_default_group_portrait;
                    } else if (data.getConversationType().equals(Conversation.ConversationType.DISCUSSION)) {//讨论组
                        defaultId1 = R.drawable.rc_default_discussion_portrait;
                    } else {
                        if (data.getConversationTargetId().equals("1")) {
                            defaultId1 = R.drawable.xitong;
                        } else if (data.getConversationTargetId().equals("2")) {
                            defaultId1 = R.drawable.pinglun_wo;
                        } else if (data.getConversationTargetId().equals("3")) {
                            defaultId1 = R.drawable.zan_wo;
                        } else if (data.getConversationTargetId().equals("4")) {
                            defaultId1 = R.drawable.bibi_xiaoguanjia;
                        }
                        else {
                            defaultId1 = R.drawable.rc_default_portrait;
                        }
                    }

                    holder.leftImageLayout.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (CustomConversationListAdapter.this.mOnPortraitItemClick != null) {
                                CustomConversationListAdapter.this.mOnPortraitItemClick.onPortraitItemClick(v, data);
                            }

                        }
                    });
                    holder.leftImageLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        public boolean onLongClick(View v) {
                            if (CustomConversationListAdapter.this.mOnPortraitItemClick != null) {
                                CustomConversationListAdapter.this.mOnPortraitItemClick.onPortraitItemLongClick(v, data);
                            }
                            return true;
                        }
                    });

                    if (data.getConversationGatherState()) {//得到会话的聚合状态
                        holder.leftImageView.setAvatar((String) null, defaultId1);
                    } else if (data.getIconUrl() != null) {
                        holder.leftImageView.setAvatar(data.getIconUrl().toString(), defaultId1);
                    } else {
                        holder.leftImageView.setAvatar((String) null, defaultId1);
                    }

                    if (data.getUnReadMessageCount() > 0) {
                        holder.unReadMsgCountIcon.setVisibility(View.VISIBLE);
                        if (data.getUnReadType().equals(UIConversation.UnreadRemindType.REMIND_WITH_COUNTING)) {
                            if (data.getUnReadMessageCount() > 99) {
                                holder.unReadMsgCount.setText(this.mContext.getResources().getString(io.rong.imkit.R.string.rc_message_unread_count));
                            } else {
                                holder.unReadMsgCount.setText(Integer.toString(data.getUnReadMessageCount()));
                            }

                            holder.unReadMsgCount.setVisibility(View.VISIBLE);
                            holder.unReadMsgCountIcon.setImageResource(io.rong.imkit.R.drawable.rc_unread_count_bg);
                        } else {
                            holder.unReadMsgCount.setVisibility(View.GONE);
                            holder.unReadMsgCountIcon.setImageResource(io.rong.imkit.R.drawable.rc_unread_remind_list_count);
                        }
                    } else {
                        holder.unReadMsgCountIcon.setVisibility(View.GONE);
                        holder.unReadMsgCount.setVisibility(View.GONE);
                    }

                    holder.rightImageLayout.setVisibility(View.GONE);
                } else if (tag.portraitPosition() == 2) { //肖像位置为2，发送的消息
                    holder.rightImageLayout.setVisibility(View.VISIBLE);
                    holder.rightImageLayout.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (CustomConversationListAdapter.this.mOnPortraitItemClick != null) {
                                CustomConversationListAdapter.this.mOnPortraitItemClick.onPortraitItemClick(v, data);
                            }

                        }
                    });
                    holder.rightImageLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        public boolean onLongClick(View v) {
                            if (CustomConversationListAdapter.this.mOnPortraitItemClick != null) {
                                CustomConversationListAdapter.this.mOnPortraitItemClick.onPortraitItemLongClick(v, data);
                            }

                            return true;
                        }
                    });
                    if (data.getConversationType().equals(Conversation.ConversationType.GROUP)) {
                        defaultId1 = io.rong.imkit.R.drawable.rc_default_group_portrait;
                    } else if (data.getConversationType().equals(Conversation.ConversationType.DISCUSSION)) {
                        defaultId1 = io.rong.imkit.R.drawable.rc_default_discussion_portrait;
                    } else {
                        if (data.getConversationTargetId().equals("1")) {
                            defaultId1 = R.drawable.xitong;
                        } else if (data.getConversationTargetId().equals("2")) {
                            defaultId1 = R.drawable.pinglun_wo;
                        } else if (data.getConversationTargetId().equals("3")) {
                            defaultId1 = R.drawable.zan_wo;
                        } else {
                            defaultId1 = io.rong.imkit.R.drawable.rc_default_portrait;
                        }
                    }

                    if (data.getConversationGatherState()) {
                        holder.rightImageView.setAvatar((String) null, defaultId1);
                    } else if (data.getIconUrl() != null) {
                        holder.rightImageView.setAvatar(data.getIconUrl().toString(), defaultId1);
                    } else {
                        holder.rightImageView.setAvatar((String) null, defaultId1);
                    }

                    if (data.getUnReadMessageCount() > 0) {
                        holder.unReadMsgCountRightIcon.setVisibility(View.VISIBLE);
                        if (data.getUnReadType().equals(UIConversation.UnreadRemindType.REMIND_WITH_COUNTING)) {
                            holder.unReadMsgCount.setVisibility(View.VISIBLE);
                            if (data.getUnReadMessageCount() > 99) {
                                holder.unReadMsgCountRight.setText(this.mContext.getResources().getString(io.rong.imkit.R.string.rc_message_unread_count));
                            } else {
                                holder.unReadMsgCountRight.setText(Integer.toString(data.getUnReadMessageCount()));
                            }
                            holder.unReadMsgCountRightIcon.setImageResource(R.drawable.rc_unread_count_bg);
                        } else {
                            holder.unReadMsgCount.setVisibility(View.GONE);
                            holder.unReadMsgCountRightIcon.setImageResource(R.drawable.rc_unread_remind_without_count);
                        }
                    } else {
                        holder.unReadMsgCountIcon.setVisibility(View.GONE);
                        holder.unReadMsgCount.setVisibility(View.GONE);
                    }

                    holder.leftImageLayout.setVisibility(View.GONE);
                } else { //肖像位置为其他，除开位置1和2，即是[3，正无穷)
                    if (tag.portraitPosition() != 3) {//[4，正无穷)都走这里
                        throw new IllegalArgumentException("the portrait position is wrong!");
                    }
                    holder.rightImageLayout.setVisibility(View.GONE);
                    holder.leftImageLayout.setVisibility(View.GONE);
                }


            }
        }
    }

    public void setOnPortraitItemClick(CustomConversationListAdapter.OnPortraitItemClick onPortraitItemClick) {
        this.mOnPortraitItemClick = onPortraitItemClick;
    }

    public interface OnPortraitItemClick {
        void onPortraitItemClick(View var1, UIConversation var2);

        boolean onPortraitItemLongClick(View var1, UIConversation var2);
    }

    class ViewHolder {
        View layout;
        View leftImageLayout;
        View rightImageLayout;
        AsyncImageView leftImageView;
        TextView unReadMsgCount;
        ImageView unReadMsgCountIcon;
        AsyncImageView rightImageView;
        TextView unReadMsgCountRight;
        ImageView unReadMsgCountRightIcon;
        ProviderContainerView contentView;

        ViewHolder() {
        }
    }
}
