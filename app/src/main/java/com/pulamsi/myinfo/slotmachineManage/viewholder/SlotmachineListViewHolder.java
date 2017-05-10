package com.pulamsi.myinfo.slotmachineManage.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.views.checkBox.SmoothCheckBox;


/**
 * 售货机列表视图容器
 */
public class SlotmachineListViewHolder extends RecyclerView.ViewHolder {

    public ImageView leftimage;
    public ImageView rightimage;
    public TextView name;
    public TextView address;
    public SmoothCheckBox smoothCheckBox;

    public SlotmachineListViewHolder(View itemView) {
        super(itemView);
        leftimage = (ImageView) itemView.findViewById(R.id.slotmachine_manage_listleft_image);
        rightimage = (ImageView) itemView.findViewById(R.id.slotmachine_manage_list_rightimage);
        name = (TextView) itemView.findViewById(R.id.slotmachine_manage_list_name);
        address = (TextView) itemView.findViewById(R.id.slotmachine_manage_list_address);
        smoothCheckBox = (SmoothCheckBox) itemView.findViewById(R.id.slotmachine_manage_list_scb);
    }
}
