package com.wiserz.pbibi.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.wiserz.pbibi.R;
import com.wiserz.pbibi.fragment.BaseFragment;
import com.wiserz.pbibi.fragment.CarCenterFragment;
import com.wiserz.pbibi.fragment.CommunityFragment;
import com.wiserz.pbibi.fragment.MessageFragment;
import com.wiserz.pbibi.fragment.MyFragment;
import com.wiserz.pbibi.fragment.MyFragmentForCompany;
import com.wiserz.pbibi.fragment.PublishStateFragment;
import com.wiserz.pbibi.fragment.RecommendFragment;
import com.wiserz.pbibi.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private List<BaseFragment> mBaseFragment;
    private Fragment fromFragment;
    public int position;

    private LinearLayout ll_home;
    private LinearLayout ll_car_center;
    private RelativeLayout rl_community;
    private LinearLayout ll_message;
    private LinearLayout ll_me;

    private ImageView iv_home;
    private TextView tv_home;
    private ImageView iv_car_center;
    private TextView tv_car_center;
    private ImageView iv_community;
    private TextView tv_community;
    private ImageView iv_message;
    private TextView tv_message;
    private ImageView iv_me;
    private TextView tv_me;

    private ImageView iv_publish_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化View
        initView();
        //初始化Fragment
        initFragment();
        //设置RadioGroup的监听
        setListener();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        ll_home = (LinearLayout) findViewById(R.id.ll_home);
        ll_car_center = (LinearLayout) findViewById(R.id.ll_car_center);
        rl_community = (RelativeLayout) findViewById(R.id.rl_community);
        ll_message = (LinearLayout) findViewById(R.id.ll_message);
        ll_me = (LinearLayout) findViewById(R.id.ll_me);

        iv_home = (ImageView) findViewById(R.id.iv_home);
        tv_home = (TextView) findViewById(R.id.tv_home);
        iv_car_center = (ImageView) findViewById(R.id.iv_car_center);
        tv_car_center = (TextView) findViewById(R.id.tv_car_center);
        iv_community = (ImageView) findViewById(R.id.iv_community);
        tv_community = (TextView) findViewById(R.id.tv_community);
        iv_message = (ImageView) findViewById(R.id.iv_message);
        tv_message = (TextView) findViewById(R.id.tv_message);
        iv_me = (ImageView) findViewById(R.id.iv_me);
        tv_me = (TextView) findViewById(R.id.tv_me);

        iv_publish_state = (ImageView) findViewById(R.id.iv_publish_state);
    }

    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new RecommendFragment());//主页
        mBaseFragment.add(new CarCenterFragment());//车市
        mBaseFragment.add(new CommunityFragment());//圈子
        mBaseFragment.add(new MessageFragment());//消息
        mBaseFragment.add(new MyFragment());//我的
        mBaseFragment.add(new MyFragmentForCompany());//企业中心
    }

    private void setListener() {
        ll_home.setOnClickListener(this);
        ll_car_center.setOnClickListener(this);
        rl_community.setOnClickListener(this);
        ll_message.setOnClickListener(this);
        ll_me.setOnClickListener(this);

        position = 0;
        setCheck(0);
        switchFragment(fromFragment, getFragment());
    }

    /**
     * @param from 刚显示的Fragment,马上就要被隐藏了
     * @param to   马上要切换到的Fragment，一会要显示
     */
    private void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            fromFragment = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //才切换
            //判断有没有被添加
            if (!to.isAdded()) {
                //to没有被添加
                //from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //添加to
                ft.add(R.id.fl_content, to).commit();
            } else {
                //to已经被添加
                // from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //显示to
                ft.show(to).commit();
            }
        }
    }

    private BaseFragment getFragment() {
        return mBaseFragment.get(position);
    }

    private long startTime = 0;

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - startTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            startTime = currentTime;
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                position = 0;
                break;
            case R.id.ll_car_center:
                position = 1;
                break;
            case R.id.rl_community:
                position = 2;
                break;
            case R.id.ll_message:
                if(CommonUtil.isHadLogin()) {
                    position = 3;
                }else{
                    gotoPager(RegisterAndLoginActivity.class, null);
                    return;
                }
                break;
            case R.id.ll_me:
                if(CommonUtil.isHadLogin()) {
                    position = 4;
                }else{
                    gotoPager(RegisterAndLoginActivity.class, null);
                    return;
                }
                break;
            default:
                break;
        }
        setCheck(position);
        //根据位置得到对应的Fragment
        BaseFragment toFragment = getFragment();
        //切换Fragment
        switchFragment(fromFragment, toFragment);
    }

    private void setCheck(int position) {
        switch (position) {
            case 0:
                resetTab();
                iv_home.setImageResource(R.drawable.tab_home_c3x);
                tv_home.setTextColor(getResources().getColor(R.color.main_text_color));
                break;
            case 1:
                resetTab();
                iv_car_center.setImageResource(R.drawable.tab_market_c3x);
                tv_car_center.setTextColor(getResources().getColor(R.color.main_text_color));
                break;
            case 2:
                resetTab();
                iv_community.setVisibility(View.GONE);
                tv_community.setVisibility(View.GONE);
                iv_publish_state.setVisibility(View.VISIBLE);
                break;
            case 3:
                resetTab();
                iv_message.setImageResource(R.drawable.tab_news_c3x);
                tv_message.setTextColor(getResources().getColor(R.color.main_text_color));
                break;
            case 4:
                resetTab();
                iv_me.setImageResource(R.drawable.tab_me_c3x);
                tv_me.setTextColor(getResources().getColor(R.color.main_text_color));
                break;
            default:
                break;
        }
    }

    public void onResume(){
        super.onResume();
        if((position==3 || position==4) && !CommonUtil.isHadLogin()){
            findViewById(R.id.ll_home).performClick();
        }
    }

    private void resetTab() {
        iv_home.setImageResource(R.drawable.tab_home3x);
        tv_home.setTextColor(getResources().getColor(R.color.second_text_color));
        iv_car_center.setImageResource(R.drawable.tab_market3x);
        tv_car_center.setTextColor(getResources().getColor(R.color.second_text_color));
        iv_community.setVisibility(View.VISIBLE);
        tv_community.setVisibility(View.VISIBLE);
        iv_publish_state.setVisibility(View.GONE);
        iv_message.setImageResource(R.drawable.tab_news3x);
        tv_message.setTextColor(getResources().getColor(R.color.second_text_color));
        iv_me.setImageResource(R.drawable.tab_me3x);
        tv_me.setTextColor(getResources().getColor(R.color.second_text_color));
    }

    public void publishState(View view) {
        if(!CommonUtil.isHadLogin()) {
            gotoPager(RegisterAndLoginActivity.class, null);
            return;
        }
        gotoPager(PublishStateFragment.class, null);//发布动态
    }
}
