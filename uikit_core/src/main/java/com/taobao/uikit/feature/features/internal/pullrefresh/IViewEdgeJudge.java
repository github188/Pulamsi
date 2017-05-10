package com.taobao.uikit.feature.features.internal.pullrefresh;

import android.view.View;

public interface IViewEdgeJudge {

	public boolean hasArrivedTopEdge();
	
	public boolean hasArrivedBottomEdge();
	
	public void setHeadView(View View);
	
	public void setFooterView(View view);
	
	public void keepTop();
	
	public void keepBottom();

}
