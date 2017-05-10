package com.pulamsi.myinfo.slotmachineManage.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pulamsi.R;


/**
 * 售货机选择售货机视图容器
 *
 */
public class SelectSlotmachineViewHolder extends RecyclerView.ViewHolder {
  public CheckBox check;
  public TextView name;

  public SelectSlotmachineViewHolder(View itemView) {
    super(itemView);
    check = (CheckBox) itemView.findViewById(R.id.slotmachine_manage_discount_selectslotmachine_item_checkbox);
    name = (TextView) itemView.findViewById(R.id.slotmachine_manage_discount_selectslotmachine_item_content);
  }

}
