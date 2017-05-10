package com.pulamsi.base.baseActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.utils.SoftInputUtil;
import com.pulamsi.views.header.TitleHeaderBar;


/**
 * 除了导航页不需要继承此类，其他activity都要继承此类
 *
 * @author WilliamChik on 2015/7/18.
 */
public abstract class BaseActivity extends AppCompatActivity {

    // 头部标题栏
    protected TitleHeaderBar mTitleHeaderBar;
    // activity主布局
    private ViewGroup mContentViewContainer;

    /**
     * overridePendingTransition mode
     */
    public enum TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        switchTransition();
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.base_title_activity);
        initUI();
    }

    /**
     * 切换动画
     */
    private void switchTransition() {
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
    }

    /**
     * 是否使用动画效果
     *
     * @return
     */
    public boolean toggleOverridePendingTransition() {
        return false;
    }


    /**
     * get the overridePendingTransition mode
     */
    public TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    private void initUI() {
        mContentViewContainer = (ViewGroup) findViewById(R.id.hai_content);
        mTitleHeaderBar = (TitleHeaderBar) findViewById(R.id.ly_header_bar_title_wrap);
        if (enableDefaultBack()) {
            //使用默认的返回按钮
            mTitleHeaderBar.setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logClickReturnBack();
                    // 如果子类实现了点击返回按钮的操作，则不做面板的返回操作；否则做面板的返回操作；
                    // 子类实现的返回操作优先级高于超类的面板返回操作
                    if (!processClickBack()) {
                        returnBack();
                    }
                }
            });
        } else {
            // 否则隐藏返回按钮
            mTitleHeaderBar.getLeftViewContainer().setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 点击返回按钮的埋点操作
     */
    protected void logClickReturnBack() {
//        AutoUserTrack.triggerReturn();
    }

    /**
     * 点击返回按钮后的处理（子类实现，默认不处理）
     *
     * @return true 处理，false 不处理
     */
    protected boolean processClickBack() {
        return false;
    }

    /**
     * 返回上一面板
     */
    protected void returnBack() {
        this.finish();
    }

    /**
     * 是否使用默认的返回处理
     */
    protected boolean enableDefaultBack() {
        return true;
    }

    /**
     * 点击头部中央返回页面头部
     */
    protected void setOnClickHeaderCenterBackToTopHandler(View.OnClickListener handler) {
        mTitleHeaderBar.getCenterViewContainer().setOnClickListener(handler);
    }

    /**
     * 点击头部中央返回页面头部
     */
    protected TitleHeaderBar getTitleHeaderBar() {
        return mTitleHeaderBar;
    }

    /**
     * 设置标题
     */
    protected void setHeaderTitle(int id) {
        mTitleHeaderBar.getTitleTextView().setText(id);
    }

    /**
     * 设置标题
     */
    protected void setHeaderTitle(String title) {
        mTitleHeaderBar.setTitle(title);
    }

    /**
     * 设置左文本（iconfont）
     */
    protected void setLeftText(String title, int textSize) {
        mTitleHeaderBar.getLeftTextView().setTextSize(textSize);
        setLeftText(title);
    }

    /**
     * 设置左文本（iconfont）
     */
    protected void setLeftText(String title) {
        mTitleHeaderBar.getLeftTextView().setVisibility(View.VISIBLE);
        mTitleHeaderBar.getLeftTextView().setText(title);
        mTitleHeaderBar.getLeftImageView().setVisibility(View.GONE);
    }

    /**
     * 设置左图标
     */
    protected void setLeftImage(int resId) {
        mTitleHeaderBar.getLeftImageView().setVisibility(View.VISIBLE);
        mTitleHeaderBar.getLeftImageView().setImageResource(resId);
        mTitleHeaderBar.getLeftTextView().setVisibility(View.GONE);
    }


    /**
     * 设置右文本（iconfont）
     */
    protected void setRightText(String text) {
        mTitleHeaderBar.getRightTextView().setVisibility(View.VISIBLE);
        mTitleHeaderBar.getRightTextView().setText(text);
        mTitleHeaderBar.getRightImageView().setVisibility(View.GONE);
    }

    /**
     * 设置右文本（iconfont）
     */
    protected void setRightText(String text, int size) {
        mTitleHeaderBar.getRightTextView().setTextSize(size);
        mTitleHeaderBar.getRightTextView().setVisibility(View.VISIBLE);
        mTitleHeaderBar.getRightTextView().setText(text);
        mTitleHeaderBar.getRightImageView().setVisibility(View.GONE);
    }

    /**
     * 设置右图标
     */
    protected void setRightImage(int resId) {
        mTitleHeaderBar.getRightImageView().setVisibility(View.VISIBLE);
        mTitleHeaderBar.getRightImageView().setImageResource(resId);
        mTitleHeaderBar.getRightTextView().setVisibility(View.GONE);
    }

    /**
     * 获得右文实例
     */
    protected TextView getRightTetxtView() {
        return mTitleHeaderBar.getRightTextView();
    }
    /**
     * 获得右图片实例
     */
    protected ImageView getRightImageView() {
        return mTitleHeaderBar.getRightImageView();
    }
    /**
     * 获得左文实例
     */
    protected TextView getLeftTetxtView() {
        return mTitleHeaderBar.getLeftTextView();
    }


    /**
     * 设置右图标
     */
    protected void setRightImage(int resId, int width, int height) {
        setRightImage(resId);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mTitleHeaderBar.getRightImageView().getLayoutParams();
        lp.width = width;
        lp.height = height;
    }

    /**
     * 重写，将内容置于LinearLayout中的统一头部的下方
     */
    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        mContentViewContainer.addView(view);
    }

    /**
     * 重写，将内容置于LinearLayout中的统一头部的下方
     */
    @Override
    public void setContentView(View view) {
        mContentViewContainer.addView(view);
    }

    /**
     * 重写，将内容置于RelativeLayout中的统一头部的下面，重叠，用于商品详情页
     */
    public void setContentViewDown(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        ((ViewGroup) findViewById(R.id.top_content)).addView(view);
    }



    /**
     * 设置右文本（iconfont）为灰
     */
    protected void setRightTextGray() {
        mTitleHeaderBar.getRightTextView().setTextColor(0xff999999);
    }


    /**
     * 隐藏页头
     */
    protected void hideTitleBar() {
        mTitleHeaderBar.setVisibility(View.GONE);
    }


    /**
     * 隐藏键盘
     */
    public void hideKeyboardForCurrentFocus() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* if (!NetWorkUtil.isNetworkAvailable(BaseActivity.this)) {
            Toast.makeText(this.getApplicationContext(), R.string.notice_networkerror, Toast.LENGTH_LONG).show();
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideKeyboardForCurrentFocus();
    }

    /**
     * 主动弹出软键盘
     */
    public void showSoftInput(final EditText edittext) {
        // 避免界面未加载完成软键盘加载失败，延迟半秒
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                SoftInputUtil.showSoftInput(edittext);
            }
        }, 500);
    }


    @Override
    public void finish() {
        super.finish();
        switchTransition();
    }
}