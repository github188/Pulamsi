package com.pulamsi.views.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;

/**
 * 公用提示 dialog
 *
 * @author WilliamChik on 2015/7/21
 */
public class CommonAlertDialog extends CommonBaseSafeDialog implements OnClickListener {

  // 确认按钮文本
  private static final String DEFAULT_CONFIRM_MAG = "确认";
  // 取消按钮文本
  private static final String DEFAULT_CANCEL_MAG = "取消";

  // 确认按钮文本
  private String okMsg = DEFAULT_CONFIRM_MAG;
  // 取消按钮文本
  private String cancelMag = DEFAULT_CANCEL_MAG;
  // 内容文本
  private String message;

  private TextView msgView;
  private TextView leftButton;
  private TextView rightButton;
  private View.OnClickListener rightButtonlistener;
  private View.OnClickListener leftButtonlistener;
  private View middleContentView;//中间的view

  public CommonAlertDialog(Activity activity, int messageResId, String confirmMsg, View.OnClickListener rightListener) {
    this(activity, PulamsiApplication.context.getResources().getString(messageResId), confirmMsg, null, null, rightListener);
  }

  public CommonAlertDialog(Activity activity, String message, View.OnClickListener rightListener) {
    this(activity, message, DEFAULT_CONFIRM_MAG, DEFAULT_CANCEL_MAG, null, rightListener);
  }

  /**
   * 最简单的用法，只有提示信息和确认按钮的监听
   */
  public CommonAlertDialog(Activity activity, View.OnClickListener rightListener) {
    this(activity, DEFAULT_CONFIRM_MAG, DEFAULT_CANCEL_MAG, null, rightListener);
  }

  public CommonAlertDialog(Activity activity, String message, String confirmMsg, String cancelMsg,
                           View.OnClickListener rightListener) {
    this(activity, message, confirmMsg, cancelMsg, null, rightListener);
  }



  /**
   * 最全面的功能，标题，内容，左右按钮内容，左右按钮的事件监听
   *
   * @param activity      activity上下文
   * @param message       内容
   * @param confirmMsg    确认信息
   * @param cancelMsg     取消信息
   * @param leftListener  左按钮监听
   * @param rightListener 右按钮监听
   */
  public CommonAlertDialog(Activity activity, String message, String confirmMsg, String cancelMsg,
                           View.OnClickListener leftListener, View.OnClickListener rightListener) {
    super(activity);
    this.activity = activity;
    this.message = message;
    this.okMsg = confirmMsg;
    this.cancelMag = cancelMsg;
    this.leftButtonlistener = leftListener;
    this.rightButtonlistener = rightListener;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    View contentView = LayoutInflater.from(activity).inflate(R.layout.common_alert_dialog, null);
    if (middleContentView != null) {//如果添加了中间内容布局
      addMiddleContentView(contentView, middleContentView);
    }
    setContentView(contentView);
    initUI();
  }


  private void initUI() {
    // 设置正文内容
    msgView = (TextView) findViewById(R.id.tv_common_alert_dialog_message);
    // 支持html
    msgView.setText(Html.fromHtml(message));
    // 设置左按钮文本
    leftButton = (TextView) findViewById(R.id.tv_common_alert_dialog_left_button);
    if (TextUtils.isEmpty(cancelMag)) {
      leftButton.setVisibility(View.GONE);
    } else {
      leftButton.setText(cancelMag);
      leftButton.setVisibility(View.VISIBLE);
      leftButton.setOnClickListener(this);
    }
    // 设置右按钮文本
    rightButton = (TextView) findViewById(R.id.tv_common_alert_dialog_right_button);
    if (TextUtils.isEmpty(okMsg)) {
      rightButton.setVisibility(View.GONE);
    } else {
      rightButton.setText(okMsg);
      rightButton.setVisibility(View.VISIBLE);
      rightButton.setOnClickListener(this);
    }
  }

  /**
   * 右边按钮文本颜色。注意 要在show之后才调用
   *
   * @param color 文本颜色
   */
  public void setRightButtonTextColor(int color) {
    rightButton.setTextColor(color);
  }

  public View setMiddleContentView(View view) {
    return view;
  }

  /**
   * 添加中间内容布局
   */
  public View addMiddleContentView(View contentView, View midView) {
    //中间内容
    ViewGroup vgMiddleContent;
    View dividerMiddleContentBottom;
    vgMiddleContent = (ViewGroup) contentView.findViewById(R.id.vg_middle_content);
    dividerMiddleContentBottom = contentView.findViewById(R.id.divider_middle_content);
    vgMiddleContent.setVisibility(View.VISIBLE);
    dividerMiddleContentBottom.setVisibility(View.VISIBLE);
    ViewGroup.LayoutParams lp =
        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    vgMiddleContent.addView(midView, lp);
    return midView;
  }


  @Override
  public void onClick(View view) {
    dismiss();
    int viewId = view.getId();
    if (viewId == R.id.tv_common_alert_dialog_left_button) {
      // 如果设置了取消按钮的监听，则同样透传
      if (leftButtonlistener != null) {
        leftButtonlistener.onClick(view);
      }
    } else if (viewId == R.id.tv_common_alert_dialog_right_button) {
      if (rightButtonlistener != null) {
        rightButtonlistener.onClick(view);
      }
    }
  }

}
