package com.pulamsi.slotmachine.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.pulamsi.R;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.myinfo.slotmachineManage.entity.VenderBean;
import com.pulamsi.myinfo.slotmachineManage.viewholder.SlotmachineListViewHolder;

/**
 * 售货机列表数据适配器
 *
 */
public class ListAdapter extends HaiBaseListAdapter<VenderBean> {

  public ListAdapter(Activity activity) {
    super(activity);
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View convertView = mInflater.inflate(R.layout.slotmachine_list_item,null);
    if (convertView == null) {
      return null;
    }

    return new SlotmachineListViewHolder(convertView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder == null || !(holder instanceof SlotmachineListViewHolder) || !(getItem(position) instanceof VenderBean)) {
      return;
    }

    SlotmachineListViewHolder newHolder = (SlotmachineListViewHolder) holder;
    VenderBean newItem = (VenderBean) getItem(position);



    // 售货机名字
    if (!TextUtils.isEmpty(newItem.getTerminalName())) {
      newHolder.name.setText(newItem.getTerminalName());
    } else {
      newHolder.name.setText("暂无信息");
    }


    // 售货机地址
    if (!TextUtils.isEmpty(newItem.getTerminalAddress())) {
      newHolder.address.setText(newItem.getTerminalAddress());
    } else {
      newHolder.address.setText("暂无地址");
    }

  }

}
