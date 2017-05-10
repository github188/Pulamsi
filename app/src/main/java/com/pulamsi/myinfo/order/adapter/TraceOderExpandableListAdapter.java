package com.pulamsi.myinfo.order.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.myinfo.order.entity.LogisticsData;

public class TraceOderExpandableListAdapter extends BaseExpandableListAdapter {
	private LayoutInflater inflater = null;
	private List<LogisticsData> logisticsDatas;

	public TraceOderExpandableListAdapter(Context context,
			List<LogisticsData> logisticsDatas) {
		this.logisticsDatas = logisticsDatas;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getGroupCount() {
		return logisticsDatas.size();
	}

	public int getChildrenCount(int groupPosition) {
		return 0;
	}

	public LogisticsData getGroup(int groupPosition) {
		return logisticsDatas.get(groupPosition);
	}

	public LogisticsData getChild(int groupPosition, int childPosition) {
		return null;
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public boolean hasStableIds() {
		return false;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		groupViewHolder viewHolder = null;
		if (convertView != null) {
			viewHolder = (groupViewHolder) convertView.getTag();
		} else {
			viewHolder = new groupViewHolder();
			convertView = inflater.inflate(R.layout.traceorder_item, null);
			viewHolder.traceOrderName = (TextView) convertView
					.findViewById(R.id.traceorder_name);
			viewHolder.traceOrderTime = (TextView) convertView
					.findViewById(R.id.traceorder_time);
		}

		viewHolder.traceOrderName.setText(logisticsDatas.get(groupPosition)
				.getContext());
		viewHolder.traceOrderTime.setText(logisticsDatas.get(groupPosition)
				.getTime());
		convertView.setTag(viewHolder);
		return convertView;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		return null;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	private class groupViewHolder {
		public TextView traceOrderName;
		public TextView traceOrderTime;
	}
}
