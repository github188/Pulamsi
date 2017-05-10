package com.pulamsi.views.header;


import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pulamsi.R;


/**
 * 普通标题头部的实现:
 * 左侧返回
 * 中部标题
 * 右侧文字
 *
 * @author WilliamChik on 2015/7/21
 */
public class TitleHeaderBar extends HeaderBarBase {

    private RelativeLayout mMainContainer;

    private RelativeLayout rl_title_bar_left;
    private TextView mLeftTextView;
    private ImageView mLeftReturnImageView;

    private TextView mCenterTitleTextView;

    private RelativeLayout rl_title_bar_right;
    private TextView mRightTextView;
    private ImageView mRightImageView;

    private TextView tv_title_bar_title_fake;

    private View line;

    private String title;

    public TitleHeaderBar(Context context) {
        super(context);
        init();
    }

    public TitleHeaderBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleHeaderBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mMainContainer = (RelativeLayout) findViewById(R.id.header_bar_main_container);

        rl_title_bar_left = (RelativeLayout) findViewById(R.id.header_bar_left_container);
        mLeftTextView = (TextView) findViewById(R.id.tv_title_bar_left);
        mLeftReturnImageView = (ImageView) findViewById(R.id.iv_title_bar_left);

        mCenterTitleTextView = (TextView) findViewById(R.id.tv_title_bar_title);

        rl_title_bar_right = (RelativeLayout) findViewById(R.id.header_bar_right_container);
        mRightTextView = (TextView) findViewById(R.id.tv_title_bar_right);
        mRightImageView = (ImageView) findViewById(R.id.iv_title_bar_right);

        tv_title_bar_title_fake = (TextView) findViewById(R.id.tv_title_bar_title_fake);

        line = findViewById(R.id.divider_line);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.base_header_bar_title;
    }

    public void setBackgroundColor(int bgColor) {
        mMainContainer.setBackgroundColor(bgColor);
    }

    public void setLeftContainerWidth(int width) {
        rl_title_bar_left.getLayoutParams().width = width;
    }

    public ImageView getLeftImageView() {
        return mLeftReturnImageView;
    }

    public TextView getLeftTextView() {
        return mLeftTextView;
    }

    public TextView getTitleTextView() {
        return mCenterTitleTextView;
    }

    public void setTitle(String title) {
        this.title = title;
        mCenterTitleTextView.setText(title);
    }

    public ImageView getRightImageView() {
        return mRightImageView;
    }

    public TextView getRightTextView() {
        return mRightTextView;
    }

    private LayoutParams makeLayoutParams(View view) {
        ViewGroup.LayoutParams lpOld = view.getLayoutParams();
        LayoutParams lp = null;
        if (lpOld == null) {
            lp = new LayoutParams(-2, -1);
        } else {
            lp = new LayoutParams(lpOld.width, lpOld.height);
        }
        return lp;
    }

    /**
     * set customized view to leftPos side
     *
     * @param view the view to be added to leftPos side
     */
    public void setCustomizedLeftView(View view) {
        mLeftReturnImageView.setVisibility(GONE);
        getLeftViewContainer().addView(view);
    }

    /**
     * set customized view to leftPos side
     *
     * @param layoutId the xml layout file id
     */
    public void setCustomizedLeftView(int layoutId) {
        View view = inflate(getContext(), layoutId, null);
        setCustomizedLeftView(view);
    }

    public void setLeftWidth(int width) {
        ViewGroup.LayoutParams lpOld = rl_title_bar_left.getLayoutParams();
        lpOld.width = width;
    }

    /**
     * set customized view to center
     *
     * @param view the view to be added to center
     */
    public void setCustomizedCenterView(View view) {
        mCenterTitleTextView.setVisibility(GONE);
        LayoutParams lp = makeLayoutParams(view);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        getCenterViewContainer().addView(view, lp);
    }

    /**
     * set customized view to center
     *
     * @param layoutId the xml layout file id
     */
    public View setCustomizedCenterView(int layoutId) {
        View view = inflate(getContext(), layoutId, null);
        setCustomizedCenterView(view);
        return view;
    }

    /**
     * set customized view to rightPos side
     *
     * @param view the view to be added to rightPos side
     */
    public void setCustomizedRightView(View view) {
        mRightTextView.setVisibility(GONE);
        getRightViewContainer().addView(view);
    }

    public void setRightContainerWidth(int width) {
        rl_title_bar_right.getLayoutParams().width = width;
    }

    /**
     * set customized view to rightPos side
     *
     * @param layoutId the xml layout file id
     */
    public void setCustomizedRightView(int layoutId) {
        View view = inflate(getContext(), layoutId, null);
        setCustomizedRightView(view);
    }

    public void setCustomizedRightView(int layoutId, int width) {
        ViewGroup.LayoutParams rightLp = rl_title_bar_right.getLayoutParams();
        rightLp.width = width;
        View view = inflate(getContext(), layoutId, null);
        setCustomizedRightView(view);

        ViewGroup.LayoutParams leftLp = rl_title_bar_left.getLayoutParams();
        leftLp.width = width;

        rl_title_bar_left.setLayoutParams(leftLp);

        if (!TextUtils.isEmpty(title)) {
            mCenterTitleTextView.setVisibility(View.INVISIBLE);
            tv_title_bar_title_fake.setVisibility(View.VISIBLE);
            tv_title_bar_title_fake.setText(title);

        }
    }

    /**
     * 设置右文本（iconfont）2015-11-05
     */
    public void setRightText(String text) {
        getRightTextView().setVisibility(View.VISIBLE);
        getRightTextView().setText(text);
        getRightImageView().setVisibility(View.GONE);
    }

    /**
     * 设置背景透明度
     * 有BUG魅族手机
     */
    public void setHeaderAlpha(int i) {
        if (i >= 0 && i <= 255) {
            mMainContainer.getBackground().setAlpha(i);
            line.getBackground().setAlpha(i);
            mCenterTitleTextView.setTextColor(Color.argb(i, 0, 188, 180)); //文字透明度
        }
    }
}