package com.pulamsi.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.start.MainActivity;

/**
 * 统一的空白布局
 *
 * @author WilliamChik on 15/11/4.
 */
public class BlankLayout extends LinearLayout {

  // 空白图标
  private ImageView mBlankIv;

  // 空白文本
  private TextView mBlankTv;

  // 空白按钮
  private TextView mBlankBtn;

  public OnBlankButtonClickListener mOnBlankButtonClickListener;

  public BlankLayout(Context context) {
    super(context);
    init(context);
  }

  public BlankLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public BlankLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setOnBlankButtonClickListener(OnBlankButtonClickListener onBlankButtonClickListener) {
    mOnBlankButtonClickListener = onBlankButtonClickListener;
  }

  private void init(Context context) {
    LayoutInflater.from(context).inflate(R.layout.activity_blank_layout, this);
    mBlankIv = (ImageView) findViewById(R.id.iv_blank_layout_img);
    mBlankTv = (TextView) findViewById(R.id.tv_blank_layout_txt);
    mBlankBtn = (TextView) findViewById(R.id.tv_black_layout_btn);
    mBlankBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        BaseAppManager.getInstance().clear();
        MainActivity.mTabView.setCurrentTab(0);
      }
    });
  }

  public void setBlankLayoutInfo(int imgResId, int txtResId) {
    setBlankLayoutInfo(imgResId, getResources().getString(txtResId));
  }

  /**
   * 设置空白页面的信息
   *
   * @param imgResId 空白图标
   * @param text     空白文案
   */
  public void setBlankLayoutInfo(int imgResId, String text) {
    setBlankImg(imgResId);
    setBlankText(text);
  }

  /**
   * 设置空白文案
   *
   * @param txtResId 文案资源 id
   */
  public void setBlankText(int txtResId) {
    mBlankTv.setText(txtResId);
  }

  /**
   * 设置空白文案
   *
   * @param text 文案文本
   */
  public void setBlankText(String text) {
    mBlankTv.setText(text);
  }

  /**
   * 设置空白图标
   *
   * @param imgResId 图标资源 id
   */
  public void setBlankImg(int imgResId) {
    mBlankIv.setImageResource(imgResId);
  }

  private interface OnBlankButtonClickListener {

    void OnBlankButtonClick();
  }

  /**
   * 隐藏按钮
   */
  public void hideBlankBtn(){
    mBlankBtn.setVisibility(INVISIBLE);
  }

}
