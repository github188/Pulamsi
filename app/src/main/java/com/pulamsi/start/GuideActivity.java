package com.pulamsi.start;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.views.gallery.BannerAdapter;
import com.pulamsi.views.gallery.DotContainer;
import com.pulamsi.views.gallery.SliderBanner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import in.srain.cube.image.CubeImageView;

/**
 * 新人引导页面，每次更新版本后启动 app 都需要跳转到新人引导页面一次，之后都不需要跳转
 */
public class GuideActivity extends BaseActivity {

  private SliderBanner mSliderBanner;
  private DotContainer mDotContainer;
  private TextView mStartBtn;

  private Bitmap[] mGuideBitmaps = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    try {
      // 因为引导页都是大图，有可能 OOM，需要 catch 一下
      super.onCreate(savedInstanceState);
      setContentView(R.layout.guide_activity);
      mTitleHeaderBar.setVisibility(View.GONE);
      init();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void init() {
    initView();
    initGuideImg();
    setGuideImg(Arrays.asList(mGuideBitmaps));
  }

  /**
   * 初始化 UI 元素
   */
  private void initView() {
    mStartBtn = (TextView) findViewById(R.id.tv_guide_start_btn);
    mStartBtn.setVisibility(View.GONE);
    mStartBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        goHomePage();
      }
    });
    mSliderBanner = (SliderBanner) findViewById(R.id.sb_guide_slide_banner);
    mSliderBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        if (position == mGuideBitmaps.length - 1) {
          // 切换到最后一页，展示启动按钮
          mStartBtn.setVisibility(View.VISIBLE);
        } else {
          mStartBtn.setVisibility(View.GONE);
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
    mDotContainer = (DotContainer) mSliderBanner.findViewById(R.id.dc_guide_banner_indicator);
  }

  /**
   * 获取引导图片
   */
  private void initGuideImg() {
    String[] picNames = null;
    try {
      picNames = getAssets().list("guide_img");
    } catch (IOException e) {
      e.printStackTrace();
    }

    mGuideBitmaps = new Bitmap[picNames.length];
    for (int i = 0; i < picNames.length; i++) {
      try {
        mGuideBitmaps[i] = getBootBitmap("guide_img/guide_" + i + ".jpg", 1);
      } catch (OutOfMemoryError error) {
        // 如果发现OOM，则把全部图片压缩一半进行显示
        for (int j = 0; j < picNames.length; j++) {
          if (mGuideBitmaps[j] != null) {
            mGuideBitmaps[j].recycle();
            mGuideBitmaps = null;
          }
        }

        for (int k = 0; k < picNames.length; k++) {
          mGuideBitmaps[k] = getBootBitmap("guide_img/guide_" + i + ".jpg", 1);
        }
        break;
      }
    }
  }

  /**
   * 根据地址获取引导图的 bitmap
   *
   * @param mBootImageFileName 图片路径
   * @param inSampleSize       缩小的倍数，例如 2 表示缩小到原来的一半
   */
  public Bitmap getBootBitmap(String mBootImageFileName, int inSampleSize) {
    InputStream fis = null;
    Bitmap bm = null;
    try {
      fis = getAssets().open(mBootImageFileName);
      BitmapFactory.Options o2 = new BitmapFactory.Options();
      o2.inSampleSize = inSampleSize;
      bm = BitmapFactory.decodeStream(fis, null, o2);
    } catch (Exception e1) {
      e1.printStackTrace();
      bm = null;
    } finally {
      try {
        if (fis != null) {
          fis.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return bm;
  }


  /**
   * 跳转到首页，包括持久化版本号、跳转到首页、关闭页面
   */
  private void goHomePage() {
    Intent intent = new Intent(GuideActivity.this,MainActivity.class);
    startActivity(intent);
    finish();
  }

  /**
   * 设置引导图
   *
   * @param imgBitmaps 引导图 bitmap 集合
   */
  public void setGuideImg(List<Bitmap> imgBitmaps) {
    if (imgBitmaps == null || imgBitmaps.size() == 0) {
      return;
    }

    InnerBannerAdapter bannerAdapter = new InnerBannerAdapter();
    bannerAdapter.setData(imgBitmaps);
    mSliderBanner.setAdapter(bannerAdapter);
    mDotContainer.setNum(imgBitmaps.size());
    mSliderBanner.beginPlay();
  }

  private class InnerBannerAdapter extends BannerAdapter<Bitmap> {

    @Override
    public View getView(LayoutInflater layoutInflater, int position) {
      View convertView = layoutInflater.inflate(R.layout.guide_img_banner_item, null);
      if (convertView == null || getItem(position) == null) {
        return convertView;
      }

      CubeImageView imageView = (CubeImageView) convertView.findViewById(R.id.iv_guide_banner_item);
      imageView.setAdjustViewBounds(false);
      imageView.setImageBitmap(getItem(position));

      return convertView;
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mGuideBitmaps == null) {
      return;
    }

    for (Bitmap bitmap : mGuideBitmaps) {
      if (bitmap != null) {
        bitmap.recycle();
        bitmap = null;
      }
    }
    System.gc();
  }
}