package com.taobao.uikit.feature.callback;

import android.graphics.Canvas;

/**
 * Created by kangyong.lt on 14-4-15.
 */
public interface CanvasCallback
{
    public void beforeDraw(Canvas canvas);

    public void afterDraw(Canvas canvas);

    public void beforeDispatchDraw(Canvas canvas);

    public void afterDispatchDraw(Canvas canvas);

    public void beforeOnDraw(Canvas canvas);

    public void afterOnDraw(Canvas canvas);
}
