package com.pulamsi.myinfo.slotmachineManage.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.myinfo.slotmachineManage.GoodsRoadListActivity;
import com.pulamsi.myinfo.slotmachineManage.entity.PortBean;
import com.pulamsi.myinfo.slotmachineManage.entity.Stockout;
import com.pulamsi.utils.DensityUtil;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.CustomExpandableListView;

/**
 *
 * 故障查看适配器
 * @author lq
 *
 */
@SuppressWarnings("ResourceType")
public class FaultExpandableAdapter extends BaseExpandableListAdapter {
	/**
	 *
	 */
	private FaultItemLayout faultItemLayout;
	private Context context;
	protected LayoutInflater mInflater;
	private List<Stockout> stockouts;
	private List<PortBean> portBeans;

	public FaultExpandableAdapter(List<Stockout> stockouts, Context context) {
		this.context = context;
		this.stockouts = stockouts;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return stockouts.get(groupPosition).getPortList().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final PortBean portBean = stockouts.get(groupPosition).getPortList()
				.get(childPosition);
		ChildViewHolder childViewHolder = null;
		if (convertView == null) {
			convertView = (View) mInflater.inflate(
					R.layout.slotmachine_manage_fault_childitem, null);
			childViewHolder = new ChildViewHolder();
			childViewHolder.image = (ImageView) convertView.findViewById(R.id.slotmachine_manage_fault_childitem_image);
			childViewHolder.faultcontent = (TextView) convertView.findViewById(R.id.slotmachine_manage_fault_childitem_faultcontent);
			childViewHolder.goodsroadNum = (TextView) convertView.findViewById(R.id.slotmachine_manage_fault_childitem_goodsroadNum);
			convertView.setTag(childViewHolder);
		} else {
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}


			if (portBean != null) {
				if (portBean.getId() != null) {
					childViewHolder.goodsroadNum.setText("货道号:" + portBean.getInneridname());
				}
				if (portBean.getErrorinfo() != null) {
					childViewHolder.faultcontent.setText("故障详情:" + portBean.getErrorinfo());
				}
				convertView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// 点击进入详情
						Intent intent = new Intent(context, GoodsRoadListActivity.class);
						intent.putExtra("searchMachineid", portBean.getMachineid());
						context.startActivity(intent);
					}
				});
			}

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (stockouts.get(groupPosition).getPortList() != null) {
			return stockouts.get(groupPosition).getPortList().size();
		}else {
			return 0;
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		return stockouts.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return stockouts.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		Stockout stockout = stockouts.get(groupPosition);
		if (convertView == null) {// 绑定控件
			faultItemLayout = new FaultItemLayout(stockout, context);
			convertView = faultItemLayout;
			convertView.setTag(faultItemLayout);
		} else {
			faultItemLayout = (FaultItemLayout) convertView.getTag();
			faultItemLayout.goodsroadtext.setText(stockout.getVenderName()
					+ "/" + stockout.getId());
		}
		if (isExpanded) {
			faultItemLayout.imageView.setImageResource(R.drawable.packup_icon);
		} else {
			faultItemLayout.imageView.setImageResource(R.drawable.spread_icon);
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public class FaultItemLayout extends RelativeLayout {
		private TextView goodsroadtext;// 信息
		private ImageView imageView;// 图标

		public FaultItemLayout(Stockout stockout, final Context context) {
			super(context);
			this.setLayoutParams(new CustomExpandableListView.LayoutParams(
					CustomExpandableListView.LayoutParams.MATCH_PARENT,
					CustomExpandableListView.LayoutParams.WRAP_CONTENT));
			this.setBackgroundColor(Color.WHITE);
			goodsroadtext = new TextView(context);
			goodsroadtext.setId(1);
			goodsroadtext.setText(stockout.getVenderName() + "/"
					+ stockout.getId());
			goodsroadtext.setLayoutParams(new LayoutParams(PulamsiApplication.ScreenWidth,
					(int) (PulamsiApplication.ScreenHeight * 0.08)));
			goodsroadtext.setGravity(Gravity.CENTER_VERTICAL);
			goodsroadtext.setPadding((int) (PulamsiApplication.ScreenWidth * 0.05), 0, 0, 0);
			goodsroadtext.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					DensityUtil.dp2px(16f));
			goodsroadtext.setTextColor(Color.parseColor("#1b3c78"));
			this.addView(goodsroadtext);
			imageView = new ImageView(context);
			imageView.setId(2);
			imageView.setImageResource(R.drawable.spread_icon);
			LayoutParams imageViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			imageViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			imageViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
			imageView.setLayoutParams(imageViewParams);
			imageView.setPadding(0, 0, (int) (PulamsiApplication.ScreenWidth * 0.03), 0);
			this.addView(imageView);
			this.setPadding(0, (int) (PulamsiApplication.ScreenWidth * 0.015), 0, (int) (PulamsiApplication.ScreenWidth * 0.015));
		}
	}


	@Override
	public void onGroupExpanded(final int groupPosition) {
		super.onGroupExpanded(groupPosition);
		if (stockouts.get(groupPosition).getPortList() != null) {
			return;
		}
		String quickerrLookList = context.getString(R.string.serverbaseurl)
				+ context.getString(R.string.getQuickErrLookByVenderId)+ "?searchMachineid="
						+ stockouts.get(groupPosition).getId();
		StringRequest quickerrLookListRequest = new StringRequest(Method.GET,
				quickerrLookList, new Listener<String>() {

					public void onResponse(String result) {
						if (result != null) {
							Gson gson = new Gson();
							portBeans = gson.fromJson(result,
									new TypeToken<List<PortBean>>() {
									}.getType());
							stockouts.get(groupPosition).setPortList(portBeans);
							notifyDataSetChanged();
						}
					}
				}, new Response.ErrorListener() {

					public void onErrorResponse(VolleyError arg0) {
						ToastUtil.showToast(R.string.notice_networkerror);
					}
				}) ;
		quickerrLookListRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
		PulamsiApplication.requestQueue.add(quickerrLookListRequest);
	}

	private class ChildViewHolder {
		public ImageView image;
		public TextView goodsroadNum;
		public TextView faultcontent;
	}
}
