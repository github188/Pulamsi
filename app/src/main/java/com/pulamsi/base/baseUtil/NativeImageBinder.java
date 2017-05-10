package com.pulamsi.base.baseUtil;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.pulamsi.R;


/**
 * 本地图片绑定器
 * <br/> 传入SD卡路径与要绑定的ImageView来进行图片绑定
 *
 * @author cangfei.hgy 2013-12-18
 */
public class NativeImageBinder {

  private static NativeImageBinder instance;

  private NativeImageBinder() {
  }

  public static NativeImageBinder getInstance() {
    if (instance == null) {
      instance = new NativeImageBinder();
    }
    return instance;
  }

  /**
   * 绑定图片
   */
  public boolean setImageDrawable(String path, ImageView imageView) {
    if (imageView != null && !TextUtils.isEmpty(path)) {
      Bitmap bitmap = getBitmapFromSDCard(path);
      if (bitmap != null) {
        imageView.setImageBitmap(bitmap);
        return true;
      }
      imageView.setImageResource(R.drawable.ic_load_image_failed);
    }
    return false;
  }

  private Bitmap getBitmapFromSDCard(String path) {
    Bitmap bitmap = BitmapFactory.decodeFile(path);
    return bitmap;
  }
}
