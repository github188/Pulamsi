package com.taobao.uikit.feature.callback;

/**
 * Created by kangyong.lt on 14-4-15.
 */
public interface LayoutCallback
{
    public void beforeOnLayout(boolean changed, int left, int top, int right, int bottom);

    public void afterOnLayout(boolean changed, int left, int top, int right, int bottom);
}
