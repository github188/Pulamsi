package com.pulamsi.utils;

import android.content.Context;

import com.pulamsi.activity.PulamsiApplication;

/**
 * 手机屏幕密度工具类
 *
 * @author WilliamChik on 2015/7/22
 */
public class DensityUtil {

    /**
     * dp转px
     */
    public static int dp2px(float dpValue) {
        if (PulamsiApplication.ScreenDensity == 0.0f) {
            PulamsiApplication.ScreenDensity = PulamsiApplication.context.getResources().getDisplayMetrics().density;
        }
        final float scale = PulamsiApplication.ScreenDensity;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     */
    public static int px2dp(float pxValue) {
        if (PulamsiApplication.ScreenDensity == 0.0f) {
            PulamsiApplication.ScreenDensity = PulamsiApplication.context.getResources().getDisplayMetrics().density;
        }
        final float scale = PulamsiApplication.ScreenDensity;
        int result = (int) (pxValue / scale + 0.5f);
        return result;
    }

    /**
     * 获取图片高度像素
     */
    public static int getImageHeightPx() {
        int screenWidth = PulamsiApplication.ScreenWidth;
        int space = dp2px(12);
        return (screenWidth - space * 3) / 2;
    }

    public static int getRecommendItemHeightPx() {
        int screenWidth = PulamsiApplication.ScreenWidth;
        int space = dp2px(10);
        int padding = dp2px(12);
        return (screenWidth - space * 2 - padding * 2) / 3;
    }


    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
