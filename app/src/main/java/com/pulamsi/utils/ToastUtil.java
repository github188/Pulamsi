package com.pulamsi.utils;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;

/**
 * Manager toast
 *
 * @author WilliamChik on 2015/09/11
 */
public class ToastUtil {

  private final static Handler handler = new ToastHandler(Looper.getMainLooper());

  private final static int DEFAULT_DURATION = 300;

  private static Toast toast;
  public static void toastShow(Context context,String content){
    if(toast == null){
      toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
    }else{
      toast.setText(content);
    }
    toast.setGravity(Gravity.BOTTOM, 0, PulamsiApplication.ScreenHeight / 5);
    toast.show();
  }

  public static void showToast(CharSequence tips) {
    showToast(tips, DEFAULT_DURATION, Gravity.BOTTOM, 0, PulamsiApplication.ScreenHeight / 5);
  }

  public static void showToast(int resId) {
    showToast(PulamsiApplication.context.getResources().getString(resId), DEFAULT_DURATION, Gravity.BOTTOM, 0, PulamsiApplication.ScreenHeight / 5);
  }

  public static void showToast(int resId, int time, int gravity, int xOffset, int yOffset) {
    showToast(PulamsiApplication.context.getResources().getString(resId), time, gravity, xOffset, yOffset);
  }

  /**
   * 自动区分当前线程，建议使用此方法
   */
  public static void showToast(CharSequence tips, int time, int gravity, int xOffset, int yOffset) {
    // 如果当前是在主线程，直接创建toast，否则发送消息创建toast
    if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
      makeToast(tips, time, gravity, xOffset, yOffset);
    } else {
      Message msg = Message.obtain();
      msg.obj = new ToastInfo(tips, time, gravity, xOffset, yOffset);
      handler.sendMessage(msg);
    }
  }

  private static void makeToast(CharSequence tips, int time, int gravity, int xOffset, int yOffset) {
    Toast mToast = makeText(PulamsiApplication.context, tips, time);
    mToast.setGravity(gravity, xOffset, yOffset);
    mToast.show();
  }

  private static class ToastHandler extends Handler {

    public ToastHandler(Looper looper) {
      super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
      ToastInfo toastInfo = (ToastInfo) msg.obj;
      makeToast(toastInfo.tips, toastInfo.time, toastInfo.gravity, toastInfo.xOffset, toastInfo.yOffset);
    }
  }

  /**
   * 生成自定义布局的 toast
   */
  private static Toast makeText(Context context, CharSequence text, int duration) {
    Toast result = new Toast(context);

    LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View v = inflate.inflate(R.layout.hai_transient_notification, null);
    TextView tv = (TextView) v.findViewById(R.id.toast_message);
    tv.setText(text);

    result.setView(v);
    result.setDuration(duration);

    return result;
  }

  private static class ToastInfo {

    public CharSequence tips;
    public int time;
    public int gravity;
    public int xOffset;
    public int yOffset;

    public ToastInfo(CharSequence tips, int time, int gravity, int xOffset, int yOffset) {
      this.tips = tips;
      this.time = time;
      this.gravity = gravity;
      this.xOffset = xOffset;
      this.yOffset = yOffset;
    }
  }
}
