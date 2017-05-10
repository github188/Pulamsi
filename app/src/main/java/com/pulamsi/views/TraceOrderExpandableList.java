package com.pulamsi.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class TraceOrderExpandableList extends ExpandableListView {

	
	public TraceOrderExpandableList(Context context) {
		super(context);
	}

	public TraceOrderExpandableList(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TraceOrderExpandableList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
