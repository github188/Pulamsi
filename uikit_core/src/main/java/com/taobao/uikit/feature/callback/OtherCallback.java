package com.taobao.uikit.feature.callback;

import android.view.KeyEvent;

/**
 * Created by kangyong.lt on 14-4-15.
 */
public interface OtherCallback
{
    public void beforeOnSizeChanged(int w, int h, int oldw, int oldh);

    public void afterOnSizeChanged(int w, int h, int oldw, int oldh);

    public void beforeOnKeyDown(int keyCode, KeyEvent event);

    public void afterOnKeyDown(int keyCode, KeyEvent event);

    public void beforeOnKeyUp(int keyCode, KeyEvent event);

    public void afterOnKeyUp(int keyCode, KeyEvent event);

 

    public void beforeOnAttachedToWindow();

    public void afterOnAttachedToWindow();

    public void beforeOnDetachedFromWindow();

    public void afterOnDetachedFromWindow();

    public void beforeOnWindowVisibilityChanged(int visibility);

    public void afterOnWindowVisibilityChanged(int visibility);

}
