package com.taobao.uikit.feature.callback;

import android.graphics.Rect;

public interface FocusCallback {
	public void beforeOnFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect);

	public void afterOnFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect);

	public void beforeOnWindowFocusChanged(boolean hasWindowFocus);

	public void afterOnWindowFocusChanged(boolean hasWindowFocus);
}
