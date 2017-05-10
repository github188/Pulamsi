package com.taobao.uikit.feature.callback;

import android.view.MotionEvent;

/**
 * Created by kangyong.lt on 14-4-15.
 */
public interface TouchEventCallback
{
    public void beforeOnTouchEvent(MotionEvent event);

    public void afterOnTouchEvent(MotionEvent event);

    public void beforeDispatchTouchEvent(MotionEvent event);

    public void afterDispatchTouchEvent(MotionEvent event);
    
}
