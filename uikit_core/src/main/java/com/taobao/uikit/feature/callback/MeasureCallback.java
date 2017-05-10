package com.taobao.uikit.feature.callback;

/**
 * Created by kangyong.lt on 14-4-15.
 */
public interface MeasureCallback
{
    public void beforeOnMeasure(int widthMeasureSpec, int heightMeasureSpec);

    public void afterOnMeasure(int widthMeasureSpec, int heightMeasureSpec);
}
