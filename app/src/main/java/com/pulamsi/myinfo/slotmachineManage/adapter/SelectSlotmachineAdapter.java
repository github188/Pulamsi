package com.pulamsi.myinfo.slotmachineManage.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.myinfo.slotmachineManage.entity.GoodsInfo;
import com.pulamsi.myinfo.slotmachineManage.entity.TradeBean;
import com.pulamsi.myinfo.slotmachineManage.entity.VenderBean;
import com.pulamsi.myinfo.slotmachineManage.viewholder.SelectProductViewHolder;
import com.pulamsi.myinfo.slotmachineManage.viewholder.SelectSlotmachineViewHolder;
import com.taobao.uikit.feature.view.TRecyclerView;


/**
 * 售货机选择售货机适配器
 *
 */
public class SelectSlotmachineAdapter extends HaiBaseListAdapter<VenderBean> {
  private Handler handler;

  public SelectSlotmachineAdapter(Activity activity, Handler handler) {
    super(activity);
    this.handler = handler;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View convertView = mInflater.inflate(R.layout.slotmachine_manage_discount_selectslotmachine_item,null);
    if (convertView == null) {
      return null;
    }

    return new SelectSlotmachineViewHolder(convertView);
  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
    if (holder == null || !(holder instanceof SelectSlotmachineViewHolder) || !(getItem(position) instanceof VenderBean)) {
      return;
    }

    SelectSlotmachineViewHolder newHolder = (SelectSlotmachineViewHolder) holder;
    VenderBean newItem = (VenderBean) getItem(position);

    if (!TextUtils.isEmpty(newItem.getTerminalName())){
      newHolder.name.setText(newItem.getTerminalName());
    }else {
      newHolder.name.setText("暂无信息");
    }
    newHolder.check.setChecked(newItem.isIsselect());
    newHolder.check.setTag(position);
    newHolder.check.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int position = (int) v.getTag();
        mData.get(position).setIsselect(((CheckBox)v).isChecked());
        handler.sendEmptyMessage(100);
      }
    });


  }
}
