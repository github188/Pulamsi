package com.pulamsi.myinfo.slotmachineManage.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.myinfo.slotmachineManage.entity.TradeBean;
import com.pulamsi.myinfo.slotmachineManage.viewholder.SlotmachineDealListViewHolder;
import com.pulamsi.utils.DensityUtil;
import com.taobao.uikit.feature.view.TRecyclerView;

/**
 * 售货机货道数据适配器
 *
 */
public class DealListAdapter extends HaiBaseListAdapter<TradeBean> {

  public DealListAdapter(Activity activity) {
    super(activity);
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View convertView = new DealListItemLayout(parent.getContext());
    if (convertView == null) {
      return null;
    }

    return new SlotmachineDealListViewHolder(convertView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder == null || !(holder instanceof SlotmachineDealListViewHolder) || !(getItem(position) instanceof TradeBean)) {
      return;
    }

    SlotmachineDealListViewHolder newHolder = (SlotmachineDealListViewHolder) holder;
    TradeBean newItem = (TradeBean) getItem(position);



    // 货道号
    if (!TextUtils.isEmpty(newItem.getInneridname())) {
     newHolder.goodsRoadid.setText(newItem.getInneridname());
    } else {
      newHolder.goodsRoadid.setText("暂无信息");
    }
    // 货机号
    if (!TextUtils.isEmpty(newItem.getGoodmachineid())) {
      newHolder.slotmachine_id.setText(newItem.getGoodmachineid());
    } else {
      newHolder.slotmachine_id.setText("暂无信息");
    }
    // 商品名
    if (!TextUtils.isEmpty(newItem.getGoodsName())) {
      newHolder.goodsName.setText(newItem.getGoodsName());
    } else {
      newHolder.goodsName.setText("暂无信息");
    }
    //交易时间
    if (!TextUtils.isEmpty(newItem.getReceivetime())) {
      newHolder.dealTime.setText(newItem.getReceivetime().substring(0, 10));
    } else {
      newHolder.dealTime.setText("暂无时间");
    }
    // 金额
    newHolder.price.setText(Float.parseFloat(newItem.getPrice())/100 +"");
  }

  public class DealListItemLayout extends LinearLayout {
    private TextView slotmachine_id;// 货机号
    private TextView goodsRoadid;// 货道号
    private TextView goodsName;// 商品名
    private TextView price;// 金额
    private TextView dealTime;//交易时间

    public DealListItemLayout(final Context context) {
      super(context);
      this.setLayoutParams(new TRecyclerView.LayoutParams(TRecyclerView.LayoutParams.MATCH_PARENT, (int) (PulamsiApplication.ScreenHeight * 0.1)));
      this.setBackgroundColor(Color.parseColor("#f2f2f2"));
      this.setOrientation(LinearLayout.VERTICAL);
      LinearLayout linearLayout = new LinearLayout(context);
      linearLayout.setOrientation(LinearLayout.HORIZONTAL);
      linearLayout.setLayoutParams(new TRecyclerView.LayoutParams(TRecyclerView.LayoutParams.MATCH_PARENT, (int) (PulamsiApplication.ScreenHeight * 0.1)));
      linearLayout.setBackgroundColor(Color.WHITE);
      LinearLayout nameLayout = new LinearLayout(context);
      nameLayout.setLayoutParams(new LayoutParams((int) (PulamsiApplication.ScreenWidth * 0.2), (int) (PulamsiApplication.ScreenHeight * 0.1)));
      nameLayout.setOrientation(LinearLayout.VERTICAL);
      slotmachine_id = new TextView(context);
      slotmachine_id.setGravity(Gravity.CENTER_HORIZONTAL
              | Gravity.BOTTOM);
      slotmachine_id.setId(1);
      slotmachine_id.setTextSize(TypedValue.COMPLEX_UNIT_PX,
              DensityUtil.dp2px(18));
      slotmachine_id.setLayoutParams(new LayoutParams(
              (int) (PulamsiApplication.ScreenWidth * 0.2), (int) (PulamsiApplication.ScreenHeight * 0.05)));
      nameLayout.addView(slotmachine_id);
      dealTime = new TextView(context);
      dealTime.setId(2);
      dealTime.setTextColor(Color.parseColor("#cccccc"));
      dealTime.setLayoutParams(new LayoutParams((int) (PulamsiApplication.ScreenWidth * 0.2),
              (int) (PulamsiApplication.ScreenHeight * 0.05)));
      dealTime.setTextSize(TypedValue.COMPLEX_UNIT_PX,
              DensityUtil.dp2px(12));
      dealTime.setGravity(Gravity.CENTER_HORIZONTAL
              | Gravity.TOP);
      nameLayout.addView(dealTime);
      linearLayout.addView(nameLayout);
      goodsRoadid = new TextView(context);
      goodsRoadid.setId(3);
      goodsRoadid.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
      goodsRoadid.setLayoutParams(new LayoutParams((int) (PulamsiApplication.ScreenWidth * 0.2), (int) (PulamsiApplication.ScreenHeight * 0.1)));
      linearLayout.addView(goodsRoadid);
      goodsName = new TextView(context);
      goodsName.setId(4);
      goodsName.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
      goodsName.setLayoutParams(new LayoutParams((int) (PulamsiApplication.ScreenWidth * 0.4), (int) (PulamsiApplication.ScreenHeight * 0.1)));
      linearLayout.addView(goodsName);
      price = new TextView(context);
      price.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
      price.setId(5);
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
