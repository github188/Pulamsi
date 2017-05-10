package com.taobao.uikit.feature.callback;

/**
 * Created by kangyong.lt on 14-4-29.
 */
public interface ScrollCallback
{
    public void beforeComputeScroll();
    public void afterComputeScroll();
    
    public void beforeOnScrollChanged(int l, int t, int oldl, int oldt);
    public void afterOnScrollChanged(int l, int t, int oldl, int oldt);
}
