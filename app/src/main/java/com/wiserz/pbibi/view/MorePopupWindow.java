package com.wiserz.pbibi.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/9/7 15:34.
 * QQ : 971060378
 * Used as : 更多PopupWindow
 */
public class MorePopupWindow extends PopupWindow implements View.OnClickListener {

    public enum MORE_POPUP_WINDOW_TYPE {
        TYPE_SALE_CAR,
        TYPE_ADD_PICTURE_MODEL,
        TYPE_IN_MY_LOVE_CAR,
        TYPE_IN_MY_SALE_CAR,
        TYPE_SAVE_PHOTO
    }

    private MorePopupWindowClickListener listener;
    //    protected View viewG;
    //    protected LinearLayout popLayout;
    protected Activity context;
    //    protected Button btnSecond, btnThird;
    protected MORE_POPUP_WINDOW_TYPE type;

    private int mCurrentIndex1;
    private int mCurrentIndex2;
    private int mCurrentIndex3;

    public interface MorePopupWindowClickListener {
         void onFirstBtnClicked();

         void onSecondBtnClicked();

         void onThirdBtnClicked();

         void onFourthBtnClicked();

         void onCancelBtnClicked();
    }

    public MorePopupWindow(Activity context, MorePopupWindowClickListener listener, MORE_POPUP_WINDOW_TYPE type) {
        super(context);
        setContentView(LayoutInflater.from(context).inflate(R.layout.popup_view_more, null));
        this.listener = listener;
        this.context = context;
        this.type = type;
        // initView(morePopup, type);
        // setWindowLayoutMode(LayoutParams.MATCH_PARENT,
        // LayoutParams.MATCH_PARENT);
        setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        // 设置弹出窗体需要软键盘，
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        // 再设置模式，和Activity的一样，覆盖，调整大小。
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 设置SelectPicPopupWindow弹出窗体可点击
        // this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // 实例化一个ColorDrawable颜色为半透明
        // ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        setAnimationStyle(R.style.PopupWindowAnimation);
        this.setBackgroundDrawable(null);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        getContentView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = getMoreMenuView().getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        getContentView().setFocusableInTouchMode(true); // 设置view能够接听事件 标注2
        getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
                if (arg1 == KeyEvent.KEYCODE_BACK) {
                        dismiss();
                }
                return false;
            }
        });
    }

    public void setCurrentIndex(int index1, int index2, int index3) {
        mCurrentIndex1 = index1;
        mCurrentIndex2 = index2;
        mCurrentIndex3 = index3;
    }

    public void initView() {
        if (type == null) {
            return;
        }
        getFirstBtn().setVisibility(View.VISIBLE);
        getPopupFirstLine().setVisibility(View.VISIBLE);
        getFirstBtn().setOnClickListener(this);
        getFirstBtn().setTextColor(context.getResources().getColor(R.color.main_color));

        getSecondBtn().setVisibility(View.VISIBLE);
        getSecondBtn().setOnClickListener(this);
        getPopupSecondLine().setVisibility(View.VISIBLE);
        getSecondBtn().setTextColor(context.getResources().getColor(R.color.main_color));

        getThirdBtn().setTextColor(context.getResources().getColor(R.color.main_color));
        getThirdBtn().setOnClickListener(this);

        getFourthBtn().setTextColor(context.getResources().getColor(R.color.main_color));
        getFourthBtn().setOnClickListener(this);

        getThirdBtn().setVisibility(View.GONE);
        if (type == MORE_POPUP_WINDOW_TYPE.TYPE_SALE_CAR) {
            getFirstBtn().setText(context.getResources().getString(R.string.post_new_car));
            getSecondBtn().setText(context.getResources().getString(R.string.post_second_hanle_car));
        } else if (type == MORE_POPUP_WINDOW_TYPE.TYPE_ADD_PICTURE_MODEL) {
            getFirstBtn().setText(context.getResources().getString(R.string.take_photo));
            getSecondBtn().setText(context.getResources().getString(R.string.select_from_album));
        } else if (type == MORE_POPUP_WINDOW_TYPE.TYPE_IN_MY_LOVE_CAR) {
            getThirdBtn().setVisibility(View.GONE);
            getFirstBtn().setText(context.getResources().getString(R.string.modify_love_car));//修改
            getSecondBtn().setText(context.getResources().getString(R.string.delete));//删除
        } else if (type == MORE_POPUP_WINDOW_TYPE.TYPE_IN_MY_SALE_CAR) {
            getThirdBtn().setVisibility(View.VISIBLE);
            getFourthBtn().setVisibility(View.VISIBLE);
            getPopupThirdLine().setVisibility(View.VISIBLE);
            getFirstBtn().setText(context.getResources().getString(R.string.post_new_car));
            getSecondBtn().setText(context.getResources().getString(R.string.post_second_hanle_car));
            getThirdBtn().setText(context.getResources().getString(R.string.edit));
            getFourthBtn().setText(context.getResources().getString(R.string.delete));
        } else if (type == MORE_POPUP_WINDOW_TYPE.TYPE_SAVE_PHOTO) {
            getSecondBtn().setVisibility(View.GONE);
            getFirstBtn().setText(context.getResources().getString(R.string.save_to_album));
        }

        getCancelBtn().setOnClickListener(this);
    }


    public Button getFirstBtn() {
        return (Button) getContentView().findViewById(R.id.btnFirst);
    }

    public Button getSecondBtn() {
        return (Button) getContentView().findViewById(R.id.btnSecond);
    }

    public Button getThirdBtn() {
        return (Button) getContentView().findViewById(R.id.btnThird);
    }

    public Button getFourthBtn() {
        return (Button) getContentView().findViewById(R.id.btnFourth);
    }

    public Button getCancelBtn() {
        return (Button) getContentView().findViewById(R.id.btnCancel);
    }

    public Button getFifthBtn() {
        return (Button) getContentView().findViewById(R.id.btnFifth);
    }

    public View getViewG() {
        return getContentView().findViewById(R.id.view_bg);
    }

    public View getPopupFirstLine() {
        return getContentView().findViewById(R.id.popupFirstLine);
    }

    public View getPopupSecondLine() {
        return getContentView().findViewById(R.id.popupSecondLine);
    }

    public View getPopupThirdLine() {
        return getContentView().findViewById(R.id.popupThirdLine);
    }

    public View getPopupFourthLine() {
        return getContentView().findViewById(R.id.popupFourthLine);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        viewShowLocation();
    }

    protected void viewShowLocation() {
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(getViewG(), "alpha", 0.5f).setDuration(250);
        fadeAnim.setStartDelay(250);
        fadeAnim.start();
    }

    @Override
    public void update() {
        super.update();
    }

    private LinearLayout getMoreMenuView() {
        return (LinearLayout) getContentView().findViewById(R.id.pop_layout);
    }


    @Override
    public void dismiss() {
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(getViewG(), "alpha", 0f).setDuration(250);
        fadeAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            public void onAnimationEnd(Animator animation) {
                MorePopupWindow.super.dismiss();
            }
        });
        fadeAnim.start();
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.btnFirst:
                if (listener != null) {
                    listener.onFirstBtnClicked();
                }
                break;
            case R.id.btnSecond:
                if (listener != null) {
                    listener.onSecondBtnClicked();
                }
                break;
            case R.id.btnThird:
                if (listener != null) {
                    listener.onThirdBtnClicked();
                }
                break;
            case R.id.btnFourth:
                if (listener != null) {
                    listener.onFourthBtnClicked();
                }
                break;
            case R.id.btnCancel:
                if (listener != null) {
                    listener.onCancelBtnClicked();
                }
                break;
            default:
                break;
        }
    }
}
