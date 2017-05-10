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
import com.pulamsi.myinfo.slotmachineManage.viewholder.SelectProductViewHolder;
import com.taobao.uikit.feature.view.TRecyclerView;


/**
 * 售货机选择商品适配器
 *
 */
public class SelectProductAdapter extends HaiBaseListAdapter<GoodsInfo> {
  private Handler handler;

  public SelectProductAdapter(Activity activity,Handler handler) {
    super(activity);
    this.handler = handler;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View convertView = mInflater.inflate(R.layout.slotmachine_manage_discount_selectproduct_item,null);
//    View convertView = new SelectSlotmachineListItemLayout(parent.getContext());
    if (convertView == null) {
      return null;
    }

    return new SelectProductViewHolder(convertView);
  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
    if (holder == null || !(holder instanceof SelectProductViewHolder) || !(getItem(position) instanceof GoodsInfo)) {
      return;
    }

    SelectProductViewHolder newHolder = (SelectProductViewHolder) holder;
    GoodsInfo newItem = (GoodsInfo) getItem(position);

    if (!TextUtils.isEmpty(newItem.getGoodsname())){
      newHolder.name.setText(newItem.getGoodsname());
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

  public class SelectSlotmachineListItemLayout extends LinearLayout {
    private CheckBox check;
    private TextView name;

    public SelectSlotmachineListItemLayout(Context context) {
      super(context);
      this.setLayoutParams(new TRecyclerView.LayoutParams(TRecyclerView.LayoutParams.MATCH_PARENT, (int) (PulamsiApplication.ScreenHeight * 0.1)));
      this.setBackgroundColor(Color.WHITE);
      this.setOrientation(LinearLayout.HORIZONTAL);
      check = new CheckBox(context);
      check.setId(1);
      check.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
      check.setButtonDrawable(R.drawable.checkbox_circle_selector);
      addView(check);
      name = new TextView(context);
      name.setId(2);
      name.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
      addView(name);
    }
  }

}
