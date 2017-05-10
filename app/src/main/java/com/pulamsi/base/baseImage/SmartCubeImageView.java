package com.pulamsi.base.baseImage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;

import in.srain.cube.image.CubeImageView;
import in.srain.cube.image.drawable.RecyclingBitmapDrawable;

/**
 * CubeImageView 的扩展，增加 setBackground() 方法的优化，由于CubeImageView 只针对 setImageDrawable() 做了优化，
 * 而 HaiImageLoadHandler 如果采用 setBackground() 设置背景占位图，用 setImageDrawable() 设置前景显示图的话，会出现两层的绘制，导致 OverDraw 的问题。
 * 两层为：占位图和前景图
 * 因此在 HaiImageLoadHandler 中统一采用 setBackground() 的方式来同时设置占位图和显示图，不区分前后景，所以就有了这个类里面关于 setBackground() 方法的优化。
 *
 * @author WilliamChik on 15/9/7.
 */
public class SmartCubeImageView extends CubeImageView {

  public SmartCubeImageView(Context context) {
    super(context);
  }

  public SmartCubeImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SmartCubeImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  /**
   * @see in.srain.cube.image.CubeImageView#setImageDrawable(Drawable)
   */
  @Override
  public void setBackgroundDrawable(Drawable background) {
    // Keep hold of previous background Drawable
    final Drawable previousBackground = getBackground();

    super.setBackgroundDrawable(background);

    // Notify new background Drawable that it is being displayed
    notifyDrawable(background, true);

    // Notify old background Drawable so it is no longer being displayed
    notifyDrawable(previousBackground, false);

    super.setBackgroundDrawable(background);
  }

  /**
   * Notifies the drawable that it's displayed state has changed.
   */
  private static void notifyDrawable(Drawable drawable, final boolean isDisplayed) {
    if (drawable instanceof RecyclingBitmapDrawable) {
      // The drawable is a CountingBitmapDrawable, so notify it
      ((RecyclingBitmapDrawable) drawable).setIsDisplayed(isDisplayed);
    } else if (drawable instanceof LayerDrawable) {
      // The drawable is a LayerDrawable, so recurse on each layer
      LayerDrawable layerDrawable = (LayerDrawable) drawable;
      for (int i = 0, z = layerDrawable.getNumberOfLayers(); i < z; i++) {
        notifyDrawable(layerDrawable.getDrawable(i), isDisplayed);
      }
    }
  }




}
