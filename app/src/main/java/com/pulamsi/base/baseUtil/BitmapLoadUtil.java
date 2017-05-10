package com.pulamsi.base.baseUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.pulamsi.activity.PulamsiApplication;

/**
 * 本地图片加载工具
 * Created by dai on 2014/5/26.
 */
public class BitmapLoadUtil {

  /**
   * 根据resId解析资源
   */
  public static Bitmap decodeResource(int resId, int sampleSize) {
    BitmapFactory.Options opts = null;
    if (sampleSize > 0) {
      opts = new BitmapFactory.Options();
      opts.inSampleSize = sampleSize;
    }

    Bitmap bmp;

    try {
      if (opts != null) {
        bmp = BitmapFactory.decodeResource(PulamsiApplication.context.getResources(), resId, opts);
      } else {
        bmp = BitmapFactory.decodeResource(PulamsiApplication.context.getResources(), resId);
      }
    } catch (Exception e) {
      bmp = null;
    }

    return bmp;
  }
}
