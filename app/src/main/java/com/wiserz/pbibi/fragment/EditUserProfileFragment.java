package com.wiserz.pbibi.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.bean.LoginBean;
import com.wiserz.pbibi.util.DataManager;
import com.wiserz.pbibi.view.CircleImageView;

/**
 * Created by jackie on 2017/8/21 14:50.
 * QQ : 971060378
 * Used as : 编辑个人资料的页面
 */
public class EditUserProfileFragment extends BaseFragment {

    private CircleImageView iv_circle_image;
    private EditText et_edit_nickname;
    private EditText et_edit_gender;
    private EditText et_edit_profile;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_user_profile;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_title)).setText("编辑资料");
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setVisibility(View.VISIBLE);
        btn_register.setText("完成");
        btn_register.setOnClickListener(this);

        LoginBean.UserInfoBean userInfoBean = DataManager.getInstance().getUserInfo();
        et_edit_nickname = (EditText) view.findViewById(R.id.et_edit_nickname);
        et_edit_gender = (EditText) view.findViewById(R.id.et_edit_gender);
        et_edit_profile = (EditText) view.findViewById(R.id.et_edit_profile);
        iv_circle_image = (CircleImageView) view.findViewById(R.id.iv_circle_image);
        iv_circle_image.setOnClickListener(this);
        if (EmptyUtils.isNotEmpty(userInfoBean)) {
            Glide.with(mContext)
                    .load(userInfoBean.getProfile().getAvatar())
                    .placeholder(R.drawable.user_photo)
                    .into(iv_circle_image);
            et_edit_nickname.setText(userInfoBean.getProfile().getNickname());
            et_edit_gender.setText(String.valueOf(userInfoBean.getProfile().getGender() == 1 ? getString(R.string.man) : getString(R.string.woman)));
            et_edit_profile.setText(userInfoBean.getProfile().getSignature());
        }
    }

    private String getNickName() {
        return et_edit_nickname.getText().toString().trim();
    }

    private String getSignature() {
        return et_edit_profile.getText().toString().trim();
    }

    private String getGender() {
        return et_edit_gender.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                goBack();
                break;
            case R.id.btn_register:
                ToastUtils.showShort("完成");
                break;
            case R.id.et_edit_nickname:
                ToastUtils.showShort("编辑姓名");
                break;
            default:
                break;
        }
    }
}
