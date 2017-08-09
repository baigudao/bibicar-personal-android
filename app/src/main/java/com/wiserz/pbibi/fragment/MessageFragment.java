package com.wiserz.pbibi.fragment;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.LoginBean;
import com.wiserz.pbibi.util.DataManager;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * Created by jackie on 2017/8/9 18:01.
 * QQ : 971060378
 * Used as : 消息页面
 */
public class MessageFragment extends BaseFragment {

    private CustomConversationListFragment customConversationListFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.tv_title)).setText("消息中心");
        view.findViewById(R.id.btn_register).setVisibility(View.GONE);
        ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
        iv_image.setImageResource(R.drawable.message_add);
        iv_image.setVisibility(View.VISIBLE);
        iv_image.setOnClickListener(this);

        //得到用户信息
        LoginBean.UserInfoBean userInfoBean = DataManager.getInstance().getUserInfo();
        if (EmptyUtils.isNotEmpty(userInfoBean)) {
            UserInfo userInfo = new UserInfo(String.valueOf(userInfoBean.getUser_id()), userInfoBean.getUsername(), Uri.parse(userInfoBean.getProfile().getAvatar()));
            RongIM.getInstance().refreshUserInfoCache(userInfo);//刷新用户缓存数据。
            RongIM.getInstance().setCurrentUserInfo(userInfo);//设置当前用户信息。
        }
        RongIM.getInstance().setMessageAttachedUserInfo(true);//设置消息体内是否携带用户信息。


        String fragmentName = CustomConversationListFragment.class.getName();
        customConversationListFragment = new CustomConversationListFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.message_container, customConversationListFragment, fragmentName);
        ft.commitAllowingStateLoss();

        enterFragment();
    }

    /**
     * 加载 会话列表 ConversationListFragment
     */
    private void enterFragment() {
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();
        customConversationListFragment.setUri(uri);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_image:
                ToastUtils.showShort("添加好友");
                break;
            default:
                break;
        }
    }
}
