package com.pulamsi.myinfo.slotmachineManage.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.myinfo.slotmachineManage.entity.PortBean;
import com.pulamsi.myinfo.slotmachineManage.entity.VenderBean;
import com.pulamsi.myinfo.slotmachineManage.viewholder.SlotmachineGoodsRoadListViewHolder;
import com.pulamsi.myinfo.slotmachineManage.viewholder.SlotmachineListViewHolder;
import com.taobao.uikit.feature.view.TRecyclerView;

/**
 * 售货机货道数据适配器
 *
 */
public class SlotmachineGoodsRoadListAdapter extends HaiBaseListAdapter<PortBean> {

  public SlotmachineGoodsRoadListAdapter(Activity activity) {
    super(activity);
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View convertView = new GoodsRoadListItemLayout(parent.getContext());
    if (convertView == null) {
      return null;
    }

    return new SlotmachineGoodsRoadListViewHolder(convertView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder == null || !(holder instanceof SlotmachineGoodsRoadListViewHolder) || !(getItem(position) instanceof PortBean)) {
      return;
    }

    SlotmachineGoodsRoadListViewHolder newHolder = (SlotmachineGoodsRoadListViewHolder) holder;
    PortBean newItem = (PortBean) getItem(position);



    // 售道号
    if (!TextUtils.isEmpty(newItem.getInneridname())) {
      newHolder.goodsroadNum.setText(newItem.getInneridname());
    } else {
      newHolder.goodsroadNum.setText("暂无信息");
    }
    // 名字
    if (!TextUtils.isEmpty(newItem.getGoodroadname())) {
      newHolder.goodsroadName.setText(newItem.getGoodroadname());
    } else {
      newHolder.goodsroadName.setText("暂无信息");
    }
    // 剩余
    if (!TextUtils.isEmpty(newItem.getAmount())) {
      newHolder.goodsSurplus.setText(newItem.getAmount());
    } else {
      newHolder.goodsSurplus.setText("暂无信息");
    }
    // 价格
    newHolder.price.setText(newItem.getPrice()/100 +"");



  }

  public class GoodsRoadListItemLayout extends LinearLayout {
    private TextView goodsroadNum;// 货道号
    private TextView goodsroadName;// 名字
    private TextView goodsSurplus;// 剩余
    private TextView price;// 价格

    public GoodsRoadListItemLayout(final Context context) {
      super(context);
      this.setLayoutParams(new TRecyclerView.LayoutParams(TRecyclerView.LayoutParams.MATCH_PARENT, (int) (PulamsiApplication.ScreenHeight * 0.1)));
      this.setBackgroundColor(Color.parseColor("#f2f2f2"));
      this.setOrientation(LinearLayout.VERTICAL);
      LinearLayout linearLayout = new LinearLayout(context);
      linearLayout.setOrientation(LinearLayout.HORIZONTAL);
      linearLayout.setLayoutParams(new TRecyclerView.LayoutParams(TRecyclerView.LayoutParams.MATCH_PARENT, (int) (PulamsiApplication.ScreenHeight * 0.1)));
      linearLayout.setBackgroundColor(Color.WHITE);
      goodsroadNum = new TextView(context);
      goodsroadNum.setId(1);
      goodsroadNum.setLayoutParams(new LayoutParams((int) (PulamsiApplication.ScreenWidth * 0.2), (int) (PulamsiApplication.ScreenHeight * 0.1)));
      goodsroadNum.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
      linearLayout.addView(goodsroadNum);
      goodsroadName = new TextView(context);
      goodsroadName.setId(2);
      goodsroadName.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
      goodsroadName.setLayoutParams(new LayoutParams((int) (PulamsiApplication.ScreenWidth * 0.4), (int) (PulamsiApplication.ScreenHeight * 0.1)));
      linearLayout.addView(goodsroadName);
      goodsSurplus = new TextView(context);
      goodsSurplus.setId(3);
      goodsSurplus.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
      goodsSurplus.setLayoutParams(new LayoutParams((int) (PulamsiApplication.ScreenWidth * 0.2), (int) (PulamsiApplication.ScreenHeight * 0.1)));
      linearLayout.addView(goodsSurplus);
      price = new TextView(context);
      price.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
      price.setId(4);
      price.setLayoutParams(new LayoutParams((int) (PulamsiApplication.ScreenWidth * 0.2), (int) (PulamsiApplication.ScreenHeight * 0.1)));
      linearLayout.addView(price);
      View view = new View(context);
      view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 4));
      this.addView(view);
      this.addView(linearLayout);
      View view1 = new View(context);
      view1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 4));
      this.addView(view1);
    }
  }

}
