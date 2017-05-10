package com.pulamsi.myinfo.slotmachineManage.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulamsi.R;


/**
 * 售货机菜单列表视图容器
 *
 */
public class SlotmachineMenuViewHolder extends RecyclerView.ViewHolder {

  public ImageView imageView;
  public TextView textView;

  public SlotmachineMenuViewHolder(View itemView) {
    super(itemView);

    imageView = (ImageView) itemView.findViewById(R.id.slotmachine_manage_menu_image);
    textView = (TextView) itemView.findViewById(R.id.slotmachine_manage_menu_text);

  }

}
