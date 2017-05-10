package com.pulamsi.myinfo.slotmachineManage.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.home.entity.ChannelMobile;
import com.pulamsi.home.viewholder.HomeDianzhuViewHolder;
import com.pulamsi.myinfo.slotmachineManage.entity.SlotmachineMenu;
import com.pulamsi.myinfo.slotmachineManage.viewholder.SlotmachineMenuViewHolder;
import com.pulamsi.utils.DensityUtil;
import com.taobao.uikit.feature.view.TRecyclerView;

/**
 * 售货机菜单列表数据适配器
 *
 */
public class SlotmachineMenuListAdapter extends HaiBaseListAdapter<SlotmachineMenu> {

  public SlotmachineMenuListAdapter(Activity activity) {
    super(activity);
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View convertView = mInflater.inflate(R.layout.slotmachine_manage_menu_item,null);
    if (convertView == null) {
      return null;
    }

    return new SlotmachineMenuViewHolder(convertView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder == null || !(holder instanceof SlotmachineMenuViewHolder) || !(getItem(position) instanceof SlotmachineMenu)) {
      return;
    }

    SlotmachineMenuViewHolder newHolder = (SlotmachineMenuViewHolder) holder;
    SlotmachineMenu newItem = (SlotmachineMenu) getItem(position);



    // 菜单名字
    if (!TextUtils.isEmpty(newItem.getContent())) {
      newHolder.textView.setText(newItem.getContent());
    } else {
      newHolder.textView.setText("暂无信息");
    }

    // 菜单图标
    if (null != newItem.getImageRID()) {
      newHolder.imageView.setImageResource(newItem.getImageRID());
    } else {
      newHolder.imageView.setImageResource(R.drawable.pulamsi_icon);
    }


  }

}
