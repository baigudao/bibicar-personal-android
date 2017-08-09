package com.wiserz.pbibi.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wiserz.pbibi.R;
import com.wiserz.pbibi.adapter.CustomConversationListAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.rong.eventbus.EventBus;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.IHistoryDataResultCallback;
import io.rong.imkit.fragment.UriFragment;
import io.rong.imkit.manager.InternalModuleManager;
import io.rong.imkit.model.Event;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.utilities.OptionsPopupDialog;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ReadReceiptMessage;
import io.rong.push.RongPushClient;

/**
 * Created by jackie on 2017/8/9 23:44.
 * QQ : 971060378
 * Used as : 自定义会话列表
 */

public class CustomConversationListFragment extends UriFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, CustomConversationListAdapter.OnPortraitItemClick {

    private List<ConversationConfig> mConversationsConfig;
    private CustomConversationListFragment mThis;
    private CustomConversationListAdapter mAdapter;
    private ListView mList;
    private LinearLayout mNotificationBar;
    private ImageView mNotificationBarImage;
    private TextView mNotificationBarText;
    private boolean isShowWithoutConnected = false;

    private RongIMClient.ResultCallback mCallback = new RongIMClient.ResultCallback() { //会话列表数据回调。

        public void onSuccess(Object object) {
            List<Conversation> conversations;
            if (object == null) {
                conversations = new ArrayList<>();
            } else {
                conversations = (List<Conversation>) object;
            }
            initConversationList(conversations);
            if (CustomConversationListFragment.this.mAdapter != null && CustomConversationListFragment.this.mAdapter.getCount() != 0) {
                CustomConversationListFragment.this.mAdapter.clear();
            }

            if (conversations != null && conversations.size() != 0) {
                if (CustomConversationListFragment.this.mAdapter == null) {
                    CustomConversationListFragment.this.mAdapter = new CustomConversationListAdapter(RongContext.getInstance());
                }

                CustomConversationListFragment.this.makeUiConversationList(conversations);
                if (CustomConversationListFragment.this.mList != null && CustomConversationListFragment.this.mList.getAdapter() != null) {
                    CustomConversationListFragment.this.mAdapter.notifyDataSetChanged();
                }

            } else {
                if (CustomConversationListFragment.this.mAdapter != null) {
                    CustomConversationListFragment.this.mAdapter.notifyDataSetChanged();
                }
            }
        }

        public void onError(RongIMClient.ErrorCode e) {
            if (e.equals(RongIMClient.ErrorCode.IPC_DISCONNECT)) {
                CustomConversationListFragment.this.isShowWithoutConnected = true;
            }

        }
    };

    private void initConversationList(List<Conversation> conversations) {
        String targetId;
        for (int i = 3; i > 0; --i) {
            boolean isHad = false;
            targetId = String.valueOf(i);
            for (Conversation conversation : conversations) {
                if (conversation.getTargetId().equals(targetId)) {
                    isHad = true;
                    break;
                }
            }
            if (!isHad) {
                Conversation conversation = new Conversation();
                conversation.setTargetId(targetId);
                conversation.setConversationType(Conversation.ConversationType.PRIVATE);
                conversation.setNotificationStatus(Conversation.ConversationNotificationStatus.NOTIFY);//自己添加。8月7日
                conversations.add(0, conversation);
            }
        }
    }

    public CustomConversationListFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mThis = this;
        this.mConversationsConfig = new ArrayList();
        EventBus.getDefault().register(this);
        InternalModuleManager.getInstance().onLoaded();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_rc_fr_conversationlist, container, false);

        this.mNotificationBar = (LinearLayout) this.findViewById(view, R.id.rc_status_bar);//网络连接不可用等状态
        this.mNotificationBar.setVisibility(View.GONE);
        this.mNotificationBarImage = (ImageView) this.findViewById(view, R.id.rc_status_bar_image);
        this.mNotificationBarText = (TextView) this.findViewById(view, R.id.rc_status_bar_text);

        this.mList = (ListView) this.findViewById(view, R.id.rc_list);

        View emptyView = this.findViewById(view, R.id.rc_conversation_list_empty_layout);
        TextView emptyText = (TextView) this.findViewById(view, R.id.rc_empty_tv);

        RongIMClient.ConnectionStatusListener.ConnectionStatus status = RongIM.getInstance().getCurrentConnectionStatus();
        if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            emptyText.setText(R.string.rc_conversation_list_not_connected);
        } else {
            emptyText.setText(R.string.rc_conversation_list_empty_prompt);
        }
        this.setNotificationBarVisibility(status);
        this.mList.setEmptyView(emptyView);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (this.mAdapter == null) {
            this.mAdapter = new CustomConversationListAdapter(RongContext.getInstance());
        }
        this.mList.setAdapter(this.mAdapter);
        this.mList.setOnItemClickListener(this);
        this.mList.setOnItemLongClickListener(this);
        this.mAdapter.setOnPortraitItemClick(this);
        super.onViewCreated(view, savedInstanceState);
    }

    protected void initFragment(Uri uri) {
        //        LogUtils.e(getUri().toString());//rong://com.wiserz.pbibi/conversationlist?private=false&group=false&discussion=false&system=false
        Conversation.ConversationType[] defConversationType = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP, Conversation.ConversationType.DISCUSSION, Conversation.ConversationType.SYSTEM, Conversation.ConversationType.CUSTOMER_SERVICE, Conversation.ConversationType.CHATROOM, Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE};
        if (uri == null) {
            RongIM.getInstance().getConversationList(this.mCallback);//回调方式获取会话列表。
        } else {
            int arr$ = defConversationType.length;//8个

            int len$;
            for (len$ = 0; len$ < arr$; ++len$) {
                Conversation.ConversationType i$ = defConversationType[len$];//得到8个会话类型中的每一个
                if (uri.getQueryParameter(i$.getName()) != null) {//筛选出4个会话类型。也就是MessageFragment中设置的4中会话类型。LogUtils.e(i$.getName());//private，GROUP，DISCUSSION，SYSTEM
                    CustomConversationListFragment.ConversationConfig conversationConfig = new CustomConversationListFragment.ConversationConfig();
                    conversationConfig.conversationType = i$;
                    conversationConfig.isGathered = uri.getQueryParameter(i$.getName()).equals("true");
                    this.mConversationsConfig.add(conversationConfig);//向集合中添加会话配置
                }
            }


            if (this.mConversationsConfig.size() == 0) {
                String var9 = uri.getQueryParameter("type");
                Conversation.ConversationType[] var10 = defConversationType;
                len$ = defConversationType.length;

                for (int var11 = 0; var11 < len$; ++var11) {
                    Conversation.ConversationType var12 = var10[var11];
                    if (var12.getName().equals(var9)) {
                        CustomConversationListFragment.ConversationConfig config = new CustomConversationListFragment.ConversationConfig();
                        config.conversationType = var12;
                        config.isGathered = false;
                        this.mConversationsConfig.add(config);
                        break;
                    }
                }

                this.mAdapter.clear();
                if (RongIMClient.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                    this.isShowWithoutConnected = true;
                } else {
                    this.getConversationList(this.getConfigConversationTypes());
                }
            } else {
                RongIM.getInstance().getConversationList(this.mCallback, this.getConfigConversationTypes());
            }
        }
    }

    public void onResume() {
        super.onResume();
        RongPushClient.clearAllPushNotifications(this.getActivity());
        this.setNotificationBarVisibility(RongIM.getInstance().getCurrentConnectionStatus());
    }

    private void getConversationList(Conversation.ConversationType[] conversationTypes) {
        this.getConversationList(conversationTypes, new IHistoryDataResultCallback() {
            @Override
            public void onResult(Object object) {
                List<Conversation> data;
                if (object == null) {
                    data = new ArrayList<>();
                } else {
                    data = (List<Conversation>) object;
                }
                if (data != null && data.size() > 0) {
                    CustomConversationListFragment.this.makeUiConversationList(data);
                    CustomConversationListFragment.this.mAdapter.notifyDataSetChanged();
                } else {
                    CustomConversationListFragment.this.isShowWithoutConnected = true;
                }

            }

            public void onError() {
            }
        });
    }

    public void getConversationList(Conversation.ConversationType[] conversationTypes, final IHistoryDataResultCallback<List<Conversation>> callback) {
        RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback() {

            @Override
            public void onSuccess(Object object) {
                List<Conversation> data;
                if (object == null) {
                    data = new ArrayList<>();
                } else {
                    data = (List<Conversation>) object;
                }
                if (callback != null) {
                    callback.onResult(data);
                }
            }

            public void onError(RongIMClient.ErrorCode e) {
                if (callback != null) {
                    callback.onError();
                }

            }
        }, conversationTypes);
    }


    public void focusUnreadItem() {
        int first = this.mList.getFirstVisiblePosition();
        int last = this.mList.getLastVisiblePosition();
        int visibleCount = last - first + 1;
        int count = this.mList.getCount();
        if (visibleCount < count) {
            int index;
            if (last < count - 1) {
                index = first + 1;
            } else {
                index = 0;
            }

            if (!this.selectNextUnReadItem(index, count)) {
                this.selectNextUnReadItem(0, count);
            }
        }

    }

    private boolean selectNextUnReadItem(int startIndex, int totalCount) {
        int index = -1;

        for (int i = startIndex; i < totalCount; ++i) {
            UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
            if (uiConversation.getUnReadMessageCount() > 0) {
                index = i;
                break;
            }
        }

        if (index >= 0 && index < totalCount) {
            this.mList.setSelection(index);
            return true;
        } else {
            return false;
        }
    }

    private void setNotificationBarVisibility(RongIMClient.ConnectionStatusListener.ConnectionStatus status) {
        if (!this.getResources().getBoolean(R.bool.rc_is_show_warning_notification)) {
        } else {
            String content = null;
            if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE)) {
                content = this.getResources().getString(R.string.rc_notice_network_unavailable);
            } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
                content = this.getResources().getString(R.string.rc_notice_tick);
            } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                this.mNotificationBar.setVisibility(View.GONE);
            } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                content = this.getResources().getString(R.string.rc_notice_disconnect);
            } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING)) {
                content = this.getResources().getString(R.string.rc_notice_connecting);
            }

            if (content != null) {
                if (this.mNotificationBar.getVisibility() == View.GONE) {
                    final String finalContent = content;
                    this.getHandler().postDelayed(new Runnable() {
                        public void run() {
                            if (!RongIMClient.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                                CustomConversationListFragment.this.mNotificationBar.setVisibility(View.VISIBLE);
                                CustomConversationListFragment.this.mNotificationBarText.setText(finalContent);
                                if (RongIMClient.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING)) {
                                    CustomConversationListFragment.this.mNotificationBarImage.setImageResource(R.drawable.rc_notification_connecting_animated);
                                } else {
                                    CustomConversationListFragment.this.mNotificationBarImage.setImageResource(R.drawable.rc_notification_network_available);
                                }
                            }

                        }
                    }, 4000L);
                } else {
                    this.mNotificationBarText.setText(content);
                    if (RongIMClient.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING)) {
                        this.mNotificationBarImage.setImageResource(R.drawable.rc_notification_connecting_animated);
                    } else {
                        this.mNotificationBarImage.setImageResource(R.drawable.rc_notification_network_available);
                    }
                }
            }

        }
    }

    public boolean onBackPressed() {
        return false;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setAdapter(CustomConversationListAdapter adapter) {
        this.mAdapter = adapter;
        if (this.mList != null) {
            this.mList.setAdapter(adapter);
        }

    }

    public CustomConversationListAdapter onResolveAdapter(Context context) {
        this.mAdapter = new CustomConversationListAdapter(context);
        return this.mAdapter;
    }

    public void onEventMainThread(Event.SyncReadStatusEvent event) {
        Conversation.ConversationType conversationType = event.getConversationType();
        String targetId = event.getTargetId();
        int first = this.mList.getFirstVisiblePosition();
        int last = this.mList.getLastVisiblePosition();
        int position;
        if (this.getGatherState(conversationType)) {
            position = this.mAdapter.findGatheredItem(conversationType);
        } else {
            position = this.mAdapter.findPosition(conversationType, targetId);
        }

        if (position >= 0) {
            UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(position);
            uiConversation.clearUnRead(conversationType, targetId);
            if (position >= first && position <= last) {
                this.mAdapter.getView(position, this.mList.getChildAt(position - this.mList.getFirstVisiblePosition()), this.mList);
            }
        }

    }

    public void onEventMainThread(Event.ReadReceiptEvent event) {
        Conversation.ConversationType conversationType = event.getMessage().getConversationType();
        String targetId = event.getMessage().getTargetId();
        int originalIndex = this.mAdapter.findPosition(conversationType, targetId);
        boolean gatherState = this.getGatherState(conversationType);
        if (!gatherState && originalIndex >= 0) {
            UIConversation conversation = (UIConversation) this.mAdapter.getItem(originalIndex);
            ReadReceiptMessage content = (ReadReceiptMessage) event.getMessage().getContent();
            if (content.getLastMessageSendTime() >= conversation.getUIConversationTime() && conversation.getConversationSenderId().equals(RongIMClient.getInstance().getCurrentUserId())) {
                conversation.setSentStatus(Message.SentStatus.READ);
                this.mAdapter.getView(originalIndex, this.mList.getChildAt(originalIndex - this.mList.getFirstVisiblePosition()), this.mList);
            }
        }

    }

    public void onEventMainThread(Event.AudioListenedEvent event) {
        Message message = event.getMessage();
        Conversation.ConversationType conversationType = message.getConversationType();
        String targetId = message.getTargetId();
        if (this.isConfigured(conversationType)) {
            boolean gathered = this.getGatherState(conversationType);
            int position = gathered ? this.mAdapter.findGatheredItem(conversationType) : this.mAdapter.findPosition(conversationType, targetId);
            if (position >= 0) {
                UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(position);
                if (message.getMessageId() == uiConversation.getLatestMessageId()) {
                    uiConversation.updateConversation(message, gathered);
                    this.mAdapter.getView(position, this.mList.getChildAt(position - this.mList.getFirstVisiblePosition()), this.mList);
                }
            }
        }

    }

    public boolean shouldUpdateConversation(Message message, int left) {
        return true;
    }

    public void onEventMainThread(Event.OnReceiveMessageEvent event) {
        Message message = event.getMessage();
        String targetId = message.getTargetId();
        Conversation.ConversationType conversationType = message.getConversationType();
        int first = this.mList.getFirstVisiblePosition();
        int last = this.mList.getLastVisiblePosition();
        if (this.isConfigured(message.getConversationType()) && this.shouldUpdateConversation(event.getMessage(), event.getLeft())) {
            if (message.getMessageId() > 0) {
                boolean gathered = this.getGatherState(conversationType);
                int position;
                if (gathered) {
                    position = this.mAdapter.findGatheredItem(conversationType);
                } else {
                    position = this.mAdapter.findPosition(conversationType, targetId);
                }

                UIConversation uiConversation;
                int index;
                if (position < 0) {
                    uiConversation = UIConversation.obtain(message, gathered);
                    index = this.getPosition(uiConversation);
                    this.mAdapter.add(uiConversation, index);
                    this.mAdapter.notifyDataSetChanged();
                } else {
                    uiConversation = (UIConversation) this.mAdapter.getItem(position);
                    if (event.getMessage().getSentTime() > uiConversation.getUIConversationTime()) {
                        uiConversation.updateConversation(message, gathered);
                        this.mAdapter.remove(position);
                        index = this.getPosition(uiConversation);
                        if (index == position) {
                            this.mAdapter.add(uiConversation, index);
                            if (index >= first && index <= last) {
                                this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition()), this.mList);
                            }
                        } else {
                            this.mAdapter.add(uiConversation, index);
                            if (index >= first && index <= last) {
                                this.mAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                    }
                }

            }

            if (event.getLeft() == 0) {
                this.syncUnreadCount();
            }

        }

    }

    public void onEventMainThread(Event.MessageLeftEvent event) {
        if (event.left == 0) {
            this.syncUnreadCount();
        }

    }

    private void syncUnreadCount() {
        if (this.mAdapter.getCount() > 0) {
            final int first = this.mList.getFirstVisiblePosition();
            final int last = this.mList.getLastVisiblePosition();

            for (int i = 0; i < this.mAdapter.getCount(); ++i) {
                final UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
                Conversation.ConversationType conversationType = uiConversation.getConversationType();
                String targetId = uiConversation.getConversationTargetId();
                final int position;
                if (this.getGatherState(conversationType)) {
                    position = this.mAdapter.findGatheredItem(conversationType);
                    RongIMClient.getInstance().getUnreadCount(new RongIMClient.ResultCallback() {

                        @Override
                        public void onSuccess(Object object) {
                            Integer data;
                            if (object == null) {
                                data = 0;
                            } else {
                                data = (Integer) object;
                            }
                            uiConversation.setUnReadMessageCount(data.intValue());
                            if (position >= first && position <= last) {
                                CustomConversationListFragment.this.mAdapter.getView(position, CustomConversationListFragment.this.mList.getChildAt(position - CustomConversationListFragment.this.mList.getFirstVisiblePosition()), CustomConversationListFragment.this.mList);
                            }
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    }, new Conversation.ConversationType[]{conversationType});
                } else {
                    position = this.mAdapter.findPosition(conversationType, targetId);
                    RongIMClient.getInstance().getUnreadCount(conversationType, targetId, new RongIMClient.ResultCallback() {

                        @Override
                        public void onSuccess(Object object) {
                            Integer data;
                            if (object == null) {
                                data = 0;
                            } else {
                                data = (Integer) object;
                            }
                            uiConversation.setUnReadMessageCount(data.intValue());
                            if (position >= first && position <= last) {
                                CustomConversationListFragment.this.mAdapter.getView(position, CustomConversationListFragment.this.mList.getChildAt(position - CustomConversationListFragment.this.mList.getFirstVisiblePosition()), CustomConversationListFragment.this.mList);
                            }
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    });
                }
            }
        }

    }

    public void onEventMainThread(Event.MessageRecallEvent event) {
        int count = this.mAdapter.getCount();

        for (int i = 0; i < count; ++i) {
            UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
            if (event.getMessageId() == uiConversation.getLatestMessageId()) {
                boolean gatherState = ((UIConversation) this.mAdapter.getItem(i)).getConversationGatherState();
                if (gatherState) {
                    RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback() {

                        @Override
                        public void onSuccess(Object object) {
                            List<Conversation> data;
                            if (object == null) {
                                data = new ArrayList<>();
                            } else {
                                data = (List<Conversation>) object;
                            }
                            if (data != null && data.size() > 0) {
                                UIConversation uiConversation = CustomConversationListFragment.this.makeUIConversation(data);
                                int oldPos = CustomConversationListFragment.this.mAdapter.findPosition(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
                                if (oldPos >= 0) {
                                    CustomConversationListFragment.this.mAdapter.remove(oldPos);
                                }

                                int newIndex = CustomConversationListFragment.this.getPosition(uiConversation);
                                CustomConversationListFragment.this.mAdapter.add(uiConversation, newIndex);
                                CustomConversationListFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    }, new Conversation.ConversationType[]{uiConversation.getConversationType()});
                } else {
                    RongIM.getInstance().getConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), new RongIMClient.ResultCallback() {

                        @Override
                        public void onSuccess(Object object) {
                            Conversation data;
                            if (object == null) {
                                data = new Conversation();
                            } else {
                                data = (Conversation) object;
                            }
                            if (data != null) {
                                UIConversation temp = UIConversation.obtain(data, false);
                                int pos = CustomConversationListFragment.this.mAdapter.findPosition(data.getConversationType(), data.getTargetId());
                                if (pos >= 0) {
                                    CustomConversationListFragment.this.mAdapter.remove(pos);
                                }

                                int newPosition = CustomConversationListFragment.this.getPosition(temp);
                                CustomConversationListFragment.this.mAdapter.add(temp, newPosition);
                                CustomConversationListFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    });
                }
                break;
            }
        }

    }

    public void onEventMainThread(Event.RemoteMessageRecallEvent event) {
        int count = this.mAdapter.getCount();

        for (int i = 0; i < count; ++i) {
            UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
            if (event.getMessageId() == uiConversation.getLatestMessageId()) {
                boolean gatherState = uiConversation.getConversationGatherState();
                if (gatherState) {
                    RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback() {

                        @Override
                        public void onSuccess(Object object) {
                            List<Conversation> data;
                            if (object == null) {
                                data = new ArrayList<>();
                            } else {
                                data = (List<Conversation>) object;
                            }
                            if (data != null && data.size() > 0) {
                                UIConversation uiConversation = CustomConversationListFragment.this.makeUIConversation(data);
                                int oldPos = CustomConversationListFragment.this.mAdapter.findPosition(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
                                if (oldPos >= 0) {
                                    CustomConversationListFragment.this.mAdapter.remove(oldPos);
                                }

                                int newIndex = CustomConversationListFragment.this.getPosition(uiConversation);
                                CustomConversationListFragment.this.mAdapter.add(uiConversation, newIndex);
                                CustomConversationListFragment.this.mAdapter.notifyDataSetChanged();
                            }

                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    }, new Conversation.ConversationType[]{((UIConversation) this.mAdapter.getItem(i)).getConversationType()});
                } else {
                    RongIM.getInstance().getConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), new RongIMClient.ResultCallback() {

                        @Override
                        public void onSuccess(Object object) {
                            Conversation data;
                            if (object == null) {
                                data = new Conversation();
                            } else {
                                data = (Conversation) object;
                            }
                            if (data != null) {
                                UIConversation temp = UIConversation.obtain(data, false);
                                int pos = CustomConversationListFragment.this.mAdapter.findPosition(data.getConversationType(), data.getTargetId());
                                if (pos >= 0) {
                                    CustomConversationListFragment.this.mAdapter.remove(pos);
                                }

                                int newPosition = CustomConversationListFragment.this.getPosition(temp);
                                CustomConversationListFragment.this.mAdapter.add(temp, newPosition);
                                CustomConversationListFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    });
                }
                break;
            }
        }

    }

    public void onEventMainThread(Message message) {
        Conversation.ConversationType conversationType = message.getConversationType();
        String targetId = message.getTargetId();
        boolean gathered = this.getGatherState(conversationType);
        if (this.isConfigured(conversationType) && message.getMessageId() > 0) {
            int position = gathered ? this.mAdapter.findGatheredItem(conversationType) : this.mAdapter.findPosition(conversationType, targetId);
            UIConversation uiConversation;
            int index;
            if (position < 0) {
                uiConversation = UIConversation.obtain(message, gathered);
                index = this.getPosition(uiConversation);
                this.mAdapter.add(uiConversation, index);
                this.mAdapter.notifyDataSetChanged();
            } else {
                uiConversation = (UIConversation) this.mAdapter.getItem(position);
                this.mAdapter.remove(position);
                uiConversation.updateConversation(message, gathered);
                index = this.getPosition(uiConversation);
                this.mAdapter.add(uiConversation, index);
                if (position == index) {
                    this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition()), this.mList);
                } else {
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    public void onEventMainThread(RongIMClient.ConnectionStatusListener.ConnectionStatus status) {
        this.setNotificationBarVisibility(status);
    }

    public void onEventMainThread(Event.ConnectEvent event) {
        if (this.isShowWithoutConnected) {
            this.getConversationList(this.getConfigConversationTypes());
            this.isShowWithoutConnected = false;
        }

    }

    public void onEventMainThread(final Event.CreateDiscussionEvent createDiscussionEvent) {
        final String targetId = createDiscussionEvent.getDiscussionId();
        if (this.isConfigured(Conversation.ConversationType.DISCUSSION)) {
            RongIMClient.getInstance().getConversation(Conversation.ConversationType.DISCUSSION, targetId, new RongIMClient.ResultCallback() {

                @Override
                public void onSuccess(Object object) {
                    Conversation data;
                    if (object == null) {
                        data = new Conversation();
                    } else {
                        data = (Conversation) object;
                    }
                    if (data != null) {
                        int position;
                        if (CustomConversationListFragment.this.getGatherState(Conversation.ConversationType.DISCUSSION)) {
                            position = CustomConversationListFragment.this.mAdapter.findGatheredItem(Conversation.ConversationType.DISCUSSION);
                        } else {
                            position = CustomConversationListFragment.this.mAdapter.findPosition(Conversation.ConversationType.DISCUSSION, targetId);
                        }

                        data.setConversationTitle(createDiscussionEvent.getDiscussionName());
                        UIConversation uiConversation;
                        if (position < 0) {
                            uiConversation = UIConversation.obtain(data, CustomConversationListFragment.this.getGatherState(Conversation.ConversationType.DISCUSSION));
                            int index = CustomConversationListFragment.this.getPosition(uiConversation);
                            CustomConversationListFragment.this.mAdapter.add(uiConversation, index);
                            CustomConversationListFragment.this.mAdapter.notifyDataSetChanged();
                        } else {
                            uiConversation = (UIConversation) CustomConversationListFragment.this.mAdapter.getItem(position);
                            uiConversation.updateConversation(data, CustomConversationListFragment.this.getGatherState(Conversation.ConversationType.DISCUSSION));
                            CustomConversationListFragment.this.mAdapter.getView(position, CustomConversationListFragment.this.mList.getChildAt(position - CustomConversationListFragment.this.mList.getFirstVisiblePosition()), CustomConversationListFragment.this.mList);
                        }
                    }
                }

                public void onError(RongIMClient.ErrorCode e) {
                }
            });
        }

    }

    public void onEventMainThread(final Event.DraftEvent draft) {
        Conversation.ConversationType conversationType = draft.getConversationType();
        String targetId = draft.getTargetId();
        if (this.isConfigured(conversationType)) {
            final boolean gathered = this.getGatherState(conversationType);
            final int position = gathered ? this.mAdapter.findGatheredItem(conversationType) : this.mAdapter.findPosition(conversationType, targetId);
            RongIMClient.getInstance().getConversation(conversationType, targetId, new RongIMClient.ResultCallback() {

                @Override
                public void onSuccess(Object object) {
                    Conversation data;
                    if (object == null) {
                        data = new Conversation();
                    } else {
                        data = (Conversation) object;
                    }
                    if (data != null) {
                        UIConversation uiConversation;
                        if (position < 0) {
                            if (!TextUtils.isEmpty(draft.getContent())) {
                                uiConversation = UIConversation.obtain(data, gathered);
                                int index = CustomConversationListFragment.this.getPosition(uiConversation);
                                CustomConversationListFragment.this.mAdapter.add(uiConversation, index);
                                CustomConversationListFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        } else {
                            uiConversation = (UIConversation) CustomConversationListFragment.this.mAdapter.getItem(position);
                            if (TextUtils.isEmpty(draft.getContent()) && !TextUtils.isEmpty(uiConversation.getDraft()) || !TextUtils.isEmpty(draft.getContent()) && TextUtils.isEmpty(uiConversation.getDraft()) || !TextUtils.isEmpty(draft.getContent()) && !TextUtils.isEmpty(uiConversation.getDraft()) && !draft.getContent().equals(uiConversation.getDraft())) {
                                uiConversation.updateConversation(data, gathered);
                                CustomConversationListFragment.this.mAdapter.getView(position, CustomConversationListFragment.this.mList.getChildAt(position - CustomConversationListFragment.this.mList.getFirstVisiblePosition()), CustomConversationListFragment.this.mList);
                            }
                        }
                    }
                }

                public void onError(RongIMClient.ErrorCode e) {
                }
            });
        }

    }

    public void onEventMainThread(Group groupInfo) {
        int count = this.mAdapter.getCount();
        if (groupInfo.getName() != null) {
            int last = this.mList.getLastVisiblePosition();
            int first = this.mList.getFirstVisiblePosition();

            for (int i = 0; i < count; ++i) {
                UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
                uiConversation.updateConversation(groupInfo);
                if (i >= first && i <= last) {
                    this.mAdapter.getView(i, this.mList.getChildAt(i - first), this.mList);
                }
            }

        }
    }

    public void onEventMainThread(Discussion discussion) {
        if (this.isConfigured(Conversation.ConversationType.DISCUSSION)) {
            int last = this.mList.getLastVisiblePosition();
            int first = this.mList.getFirstVisiblePosition();
            int position;
            if (this.getGatherState(Conversation.ConversationType.DISCUSSION)) {
                position = this.mAdapter.findGatheredItem(Conversation.ConversationType.DISCUSSION);
            } else {
                position = this.mAdapter.findPosition(Conversation.ConversationType.DISCUSSION, discussion.getId());
            }

            if (position >= 0) {
                for (int i = 0; i == position; ++i) {
                    UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
                    uiConversation.updateConversation(discussion);
                    if (i >= first && i <= last) {
                        this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                    }
                }
            }
        }

    }

    public void onEventMainThread(GroupUserInfo groupUserInfo) {
        if (groupUserInfo.getNickname() != null && groupUserInfo.getGroupId() != null) {
            int count = this.mAdapter.getCount();
            int last = this.mList.getLastVisiblePosition();
            int first = this.mList.getFirstVisiblePosition();

            for (int i = 0; i < count; ++i) {
                UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
                if (!this.getGatherState(Conversation.ConversationType.GROUP) && uiConversation.getConversationTargetId().equals(groupUserInfo.getGroupId()) && uiConversation.getConversationSenderId().equals(groupUserInfo.getUserId())) {
                    uiConversation.updateConversation(groupUserInfo);
                    if (i >= first && i <= last) {
                        this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                    }
                }
            }

        }
    }

    public void onEventMainThread(UserInfo userInfo) {
        int count = this.mAdapter.getCount();
        int last = this.mList.getLastVisiblePosition();
        int first = this.mList.getFirstVisiblePosition();

        for (int i = 0; i < count && userInfo.getName() != null; ++i) {
            UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
            if (uiConversation.hasNickname(userInfo.getUserId())) {
            } else {
                uiConversation.updateConversation(userInfo);
                if (i >= first && i <= last) {
                    this.mAdapter.getView(i, this.mList.getChildAt(i - first), this.mList);
                }
            }
        }

    }

    public void onEventMainThread(PublicServiceProfile profile) {
        int count = this.mAdapter.getCount();
        boolean gatherState = this.getGatherState(profile.getConversationType());

        for (int i = 0; i < count; ++i) {
            UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
            if (uiConversation.getConversationType().equals(profile.getConversationType()) && uiConversation.getConversationTargetId().equals(profile.getTargetId()) && !gatherState) {
                uiConversation.setUIConversationTitle(profile.getName());
                uiConversation.setIconUrl(profile.getPortraitUri());
                this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                break;
            }
        }

    }

    public void onEventMainThread(Event.PublicServiceFollowableEvent event) {
        if (!event.isFollow()) {
            int originalIndex = this.mAdapter.findPosition(event.getConversationType(), event.getTargetId());
            if (originalIndex >= 0) {
                this.mAdapter.remove(originalIndex);
                this.mAdapter.notifyDataSetChanged();
            }
        }

    }

    public void onEventMainThread(Event.ConversationUnreadEvent unreadEvent) {
        Conversation.ConversationType conversationType = unreadEvent.getType();
        String targetId = unreadEvent.getTargetId();
        int position = this.getGatherState(conversationType) ? this.mAdapter.findGatheredItem(conversationType) : this.mAdapter.findPosition(conversationType, targetId);
        if (position >= 0) {
            int first = this.mList.getFirstVisiblePosition();
            int last = this.mList.getLastVisiblePosition();
            UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(position);
            uiConversation.clearUnRead(conversationType, targetId);
            if (position >= first && position <= last) {
                this.mAdapter.getView(position, this.mList.getChildAt(position - this.mList.getFirstVisiblePosition()), this.mList);
            }
        }

    }

    public void onEventMainThread(Event.ConversationTopEvent setTopEvent) {
        Conversation.ConversationType conversationType = setTopEvent.getConversationType();
        String targetId = setTopEvent.getTargetId();
        int position = this.mAdapter.findPosition(conversationType, targetId);
        if (position >= 0 && !this.getGatherState(conversationType)) {
            UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(position);
            if (uiConversation.isTop() != setTopEvent.isTop()) {
                uiConversation.setTop(!uiConversation.isTop());
                this.mAdapter.remove(position);
                int index = this.getPosition(uiConversation);
                this.mAdapter.add(uiConversation, index);
                if (index == position) {
                    this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition()), this.mList);
                } else {
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    public void onEventMainThread(Event.ConversationRemoveEvent removeEvent) {
        Conversation.ConversationType conversationType = removeEvent.getType();
        this.removeConversation(conversationType, removeEvent.getTargetId());
    }

    public void onEventMainThread(Event.ClearConversationEvent clearConversationEvent) {
        List typeList = clearConversationEvent.getTypes();

        for (int i = this.mAdapter.getCount() - 1; i >= 0; --i) {
            if (typeList.indexOf(((UIConversation) this.mAdapter.getItem(i)).getConversationType()) >= 0) {
                this.mAdapter.remove(i);
            }
        }

        this.mAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(Event.MessageDeleteEvent event) {
        int count = this.mAdapter.getCount();

        for (int i = 0; i < count; ++i) {
            if (event.getMessageIds().contains(Integer.valueOf(((UIConversation) this.mAdapter.getItem(i)).getLatestMessageId()))) {
                boolean gatherState = ((UIConversation) this.mAdapter.getItem(i)).getConversationGatherState();
                if (gatherState) {
                    RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback() {

                        @Override
                        public void onSuccess(Object object) {
                            List<Conversation> data;
                            if (object == null) {
                                data = new ArrayList<>();
                            } else {
                                data = (List<Conversation>) object;
                            }
                            if (data != null && data.size() != 0) {
                                UIConversation uiConversation = CustomConversationListFragment.this.makeUIConversation(data);
                                int oldPos = CustomConversationListFragment.this.mAdapter.findPosition(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
                                if (oldPos >= 0) {
                                    CustomConversationListFragment.this.mAdapter.remove(oldPos);
                                }

                                int newIndex = CustomConversationListFragment.this.getPosition(uiConversation);
                                CustomConversationListFragment.this.mAdapter.add(uiConversation, newIndex);
                                CustomConversationListFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    }, new Conversation.ConversationType[]{((UIConversation) this.mAdapter.getItem(i)).getConversationType()});
                } else {
                    RongIM.getInstance().getConversation(((UIConversation) this.mAdapter.getItem(i)).getConversationType(), ((UIConversation) this.mAdapter.getItem(i)).getConversationTargetId(), new RongIMClient.ResultCallback() {

                        @Override
                        public void onSuccess(Object object) {
                            Conversation data;
                            if (object == null) {
                                data = new Conversation();
                            } else {
                                data = (Conversation) object;
                            }
                            if (data == null) {
                            } else {
                                UIConversation temp = UIConversation.obtain(data, false);
                                int pos = CustomConversationListFragment.this.mAdapter.findPosition(data.getConversationType(), data.getTargetId());
                                if (pos >= 0) {
                                    CustomConversationListFragment.this.mAdapter.remove(pos);
                                }

                                int newIndex = CustomConversationListFragment.this.getPosition(temp);
                                CustomConversationListFragment.this.mAdapter.add(temp, newIndex);
                                CustomConversationListFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    });
                }
                break;
            }
        }

    }

    public void onEventMainThread(Event.ConversationNotificationEvent notificationEvent) {
        int originalIndex = this.mAdapter.findPosition(notificationEvent.getConversationType(), notificationEvent.getTargetId());
        if (originalIndex >= 0) {
            this.mAdapter.getView(originalIndex, this.mList.getChildAt(originalIndex - this.mList.getFirstVisiblePosition()), this.mList);
        }

    }

    public void onEventMainThread(Event.MessagesClearEvent clearMessagesEvent) {
        Conversation.ConversationType conversationType = clearMessagesEvent.getType();
        String targetId = clearMessagesEvent.getTargetId();
        int position = this.getGatherState(conversationType) ? this.mAdapter.findGatheredItem(conversationType) : this.mAdapter.findPosition(conversationType, targetId);
        if (position >= 0) {
            UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(position);
            uiConversation.clearLastMessage();
            this.mAdapter.getView(position, this.mList.getChildAt(position - this.mList.getFirstVisiblePosition()), this.mList);
        }

    }

    public void onEventMainThread(Event.OnMessageSendErrorEvent sendErrorEvent) {
        Message message = sendErrorEvent.getMessage();
        Conversation.ConversationType conversationType = message.getConversationType();
        String targetId = message.getTargetId();
        if (this.isConfigured(conversationType)) {
            int first = this.mList.getFirstVisiblePosition();
            int last = this.mList.getLastVisiblePosition();
            boolean gathered = this.getGatherState(conversationType);
            int index = gathered ? this.mAdapter.findGatheredItem(conversationType) : this.mAdapter.findPosition(conversationType, targetId);
            if (index >= 0) {
                UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(index);
                message.setSentStatus(Message.SentStatus.FAILED);
                uiConversation.updateConversation(message, gathered);
                if (index >= first && index <= last) {
                    this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition()), this.mList);
                }
            }
        }

    }

    public void onEventMainThread(Event.QuitDiscussionEvent event) {
        this.removeConversation(Conversation.ConversationType.DISCUSSION, event.getDiscussionId());
    }

    public void onEventMainThread(Event.QuitGroupEvent event) {
        this.removeConversation(Conversation.ConversationType.GROUP, event.getGroupId());
    }

    private void removeConversation(final Conversation.ConversationType conversationType, String targetId) {
        boolean gathered = this.getGatherState(conversationType);
        int index;
        if (gathered) {
            index = this.mAdapter.findGatheredItem(conversationType);
            if (index >= 0) {
                RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback() {

                    @Override
                    public void onSuccess(Object object) {
                        List<Conversation> data;
                        if (object == null) {
                            data = new ArrayList<>();
                        } else {
                            data = (List<Conversation>) object;
                        }
                        int oldPos = CustomConversationListFragment.this.mAdapter.findGatheredItem(conversationType);
                        if (oldPos >= 0) {
                            CustomConversationListFragment.this.mAdapter.remove(oldPos);
                            if (data != null && data.size() > 0) {
                                UIConversation uiConversation = CustomConversationListFragment.this.makeUIConversation(data);
                                int newIndex = CustomConversationListFragment.this.getPosition(uiConversation);
                                CustomConversationListFragment.this.mAdapter.add(uiConversation, newIndex);
                            }

                            CustomConversationListFragment.this.mAdapter.notifyDataSetChanged();
                        }

                    }

                    public void onError(RongIMClient.ErrorCode e) {
                    }
                }, new Conversation.ConversationType[]{conversationType});
            }
        } else {
            index = this.mAdapter.findPosition(conversationType, targetId);
            if (index >= 0) {
                this.mAdapter.remove(index);
                this.mAdapter.notifyDataSetChanged();
            }
        }

    }

    public void onPortraitItemClick(View v, UIConversation data) {
        Conversation.ConversationType type = data.getConversationType();
        if (this.getGatherState(type)) {
            RongIM.getInstance().startSubConversationList(this.getActivity(), type);
        } else {
            if (RongContext.getInstance().getConversationListBehaviorListener() != null) {
                boolean isDefault = RongContext.getInstance().getConversationListBehaviorListener().onConversationPortraitClick(this.getActivity(), type, data.getConversationTargetId());
                if (isDefault) {
                    return;
                }
            }

            data.setUnReadMessageCount(0);
            RongIM.getInstance().startConversation(this.getActivity(), type, data.getConversationTargetId(), data.getUIConversationTitle());
        }

    }

    public boolean onPortraitItemLongClick(View v, UIConversation data) {
        Conversation.ConversationType type = data.getConversationType();
        if (RongContext.getInstance().getConversationListBehaviorListener() != null) {
            boolean isDealt = RongContext.getInstance().getConversationListBehaviorListener().onConversationPortraitLongClick(this.getActivity(), type, data.getConversationTargetId());
            if (isDealt) {
                return true;
            }
        }

        if (!this.getGatherState(type)) {
            this.buildMultiDialog(data);
            return true;
        } else {
            this.buildSingleDialog(data);
            return true;
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(position);
        Conversation.ConversationType conversationType = uiConversation.getConversationType();
        if (this.getGatherState(conversationType)) {
            RongIM.getInstance().startSubConversationList(this.getActivity(), conversationType);
        } else {
            if (RongContext.getInstance().getConversationListBehaviorListener() != null && RongContext.getInstance().getConversationListBehaviorListener().onConversationClick(this.getActivity(), view, uiConversation)) {
                return;
            }

            uiConversation.setUnReadMessageCount(0);
            RongIM.getInstance().startConversation(this.getActivity(), conversationType, uiConversation.getConversationTargetId(), uiConversation.getUIConversationTitle());
        }

    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(position);
        if (RongContext.getInstance().getConversationListBehaviorListener() != null) {
            boolean isDealt = RongContext.getInstance().getConversationListBehaviorListener().onConversationLongClick(this.getActivity(), view, uiConversation);
            if (isDealt) {
                return true;
            }
        }

        if (!this.getGatherState(uiConversation.getConversationType())) {
            this.buildMultiDialog(uiConversation);
            return true;
        } else {
            this.buildSingleDialog(uiConversation);
            return true;
        }
    }

    private void buildMultiDialog(final UIConversation uiConversation) {
        String[] items = new String[2];
        if (uiConversation.isTop()) {
            items[0] = RongContext.getInstance().getString(R.string.rc_conversation_list_dialog_cancel_top);
        } else {
            items[0] = RongContext.getInstance().getString(R.string.rc_conversation_list_dialog_set_top);
        }

        items[1] = RongContext.getInstance().getString(R.string.rc_conversation_list_dialog_remove);
        OptionsPopupDialog.newInstance(this.getActivity(), items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
            public void onOptionsItemClicked(int which) {
                if (which == 0) {
                    RongIM.getInstance().setConversationToTop(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), !uiConversation.isTop(), new RongIMClient.ResultCallback() {

                        @Override
                        public void onSuccess(Object o) {
                            Boolean data;
                            if (o == null) {
                                data = null;
                            } else {
                                data = (Boolean) o;
                            }
                            if (uiConversation.isTop()) {
                                Toast.makeText(RongContext.getInstance(), CustomConversationListFragment.this.getString(R.string.rc_conversation_list_popup_cancel_top), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RongContext.getInstance(), CustomConversationListFragment.this.getString(R.string.rc_conversation_list_dialog_set_top), Toast.LENGTH_SHORT).show();
                            }
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    });
                } else if (which == 1) {
                    RongIM.getInstance().removeConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), (RongIMClient.ResultCallback) null);
                }

            }
        }).show();
    }

    private void buildSingleDialog(final UIConversation uiConversation) {
        String[] items = new String[]{RongContext.getInstance().getString(R.string.rc_conversation_list_dialog_remove)};
        OptionsPopupDialog.newInstance(this.getActivity(), items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
            public void onOptionsItemClicked(int which) {
                RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback() {

                    @Override
                    public void onSuccess(Object object) {
                        List<Conversation> data;
                        if (object == null) {
                            data = new ArrayList<>();
                        } else {
                            data = (List<Conversation>) object;
                        }
                        if (data != null && data.size() > 0) {
                            Iterator i$ = data.iterator();

                            while (i$.hasNext()) {
                                Conversation conversation = (Conversation) i$.next();
                                RongIMClient.getInstance().removeConversation(conversation.getConversationType(), conversation.getTargetId(), (RongIMClient.ResultCallback) null);
                            }
                        }
                    }

                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }
                }, new Conversation.ConversationType[]{uiConversation.getConversationType()});
                int position = CustomConversationListFragment.this.mAdapter.findGatheredItem(uiConversation.getConversationType());
                CustomConversationListFragment.this.mAdapter.remove(position);
                CustomConversationListFragment.this.mAdapter.notifyDataSetChanged();
            }
        }).show();
    }

    private void makeUiConversationList(List<Conversation> conversationList) {
        Iterator i$ = conversationList.iterator();

        while (i$.hasNext()) {
            Conversation conversation = (Conversation) i$.next();
            Conversation.ConversationType conversationType = conversation.getConversationType();
            String targetId = conversation.getTargetId();
            boolean gatherState = this.getGatherState(conversationType);
            UIConversation uiConversation;
            int originalIndex;
            if (gatherState) {
                originalIndex = this.mAdapter.findGatheredItem(conversationType);
                if (originalIndex >= 0) {
                    uiConversation = (UIConversation) this.mAdapter.getItem(originalIndex);
                    uiConversation.updateConversation(conversation, true);
                } else {
                    uiConversation = UIConversation.obtain(conversation, true);
                    this.mAdapter.add(uiConversation);
                }
            } else {
                originalIndex = this.mAdapter.findPosition(conversationType, targetId);
                if (originalIndex < 0) {
                    uiConversation = UIConversation.obtain(conversation, false);//空指针异常
                    this.mAdapter.add(uiConversation);
                } else {
                    uiConversation = (UIConversation) this.mAdapter.getItem(originalIndex);
                    uiConversation.setUnReadMessageCount(conversation.getUnreadMessageCount());
                }
            }
        }
    }

    private UIConversation makeUIConversation(List<Conversation> conversations) {
        int unreadCount = 0;
        boolean topFlag = false;
        boolean isMentioned = false;
        Conversation newest = (Conversation) conversations.get(0);

        Conversation conversation;
        for (Iterator uiConversation = conversations.iterator(); uiConversation.hasNext(); unreadCount += conversation.getUnreadMessageCount()) {
            conversation = (Conversation) uiConversation.next();
            if (newest.isTop()) {
                if (conversation.isTop() && conversation.getSentTime() > newest.getSentTime()) {
                    newest = conversation;
                }
            } else if (conversation.isTop() || conversation.getSentTime() > newest.getSentTime()) {
                newest = conversation;
            }

            if (conversation.isTop()) {
                topFlag = true;
            }

            if (conversation.getMentionedCount() > 0) {
                isMentioned = true;
            }
        }

        UIConversation uiConversation1 = UIConversation.obtain(newest, this.getGatherState(newest.getConversationType()));
        uiConversation1.setUnReadMessageCount(unreadCount);
        uiConversation1.setTop(false);
        uiConversation1.setMentionedFlag(isMentioned);
        return uiConversation1;
    }

    private int getPosition(UIConversation uiConversation) {
        int count = this.mAdapter.getCount();
        int position = 0;

        for (int i = 0; i < count; ++i) {
            if (uiConversation.isTop()) {
                if (!((UIConversation) this.mAdapter.getItem(i)).isTop() || ((UIConversation) this.mAdapter.getItem(i)).getUIConversationTime() <= uiConversation.getUIConversationTime()) {
                    break;
                }

                ++position;
            } else {
                if (!((UIConversation) this.mAdapter.getItem(i)).isTop() && ((UIConversation) this.mAdapter.getItem(i)).getUIConversationTime() <= uiConversation.getUIConversationTime()) {
                    break;
                }

                ++position;
            }
        }

        return position;
    }

    private boolean isConfigured(Conversation.ConversationType conversationType) {
        for (int i = 0; i < this.mConversationsConfig.size(); ++i) {
            if (conversationType.equals(((CustomConversationListFragment.ConversationConfig) this.mConversationsConfig.get(i)).conversationType)) {
                return true;
            }
        }

        return false;
    }

    public boolean getGatherState(Conversation.ConversationType conversationType) {
        Iterator i$ = this.mConversationsConfig.iterator();

        CustomConversationListFragment.ConversationConfig config;
        do {
            if (!i$.hasNext()) {
                return false;
            }

            config = (CustomConversationListFragment.ConversationConfig) i$.next();
        } while (!config.conversationType.equals(conversationType));

        return config.isGathered;
    }

    private Conversation.ConversationType[] getConfigConversationTypes() {
        Conversation.ConversationType[] conversationTypes = new Conversation.ConversationType[this.mConversationsConfig.size()];

        for (int i = 0; i < this.mConversationsConfig.size(); ++i) {
            conversationTypes[i] = ((CustomConversationListFragment.ConversationConfig) this.mConversationsConfig.get(i)).conversationType;
        }

        return conversationTypes;
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this.mThis);
        super.onDestroy();
    }

    private class ConversationConfig {
        Conversation.ConversationType conversationType;
        boolean isGathered;

        private ConversationConfig() {
        }
    }
}
