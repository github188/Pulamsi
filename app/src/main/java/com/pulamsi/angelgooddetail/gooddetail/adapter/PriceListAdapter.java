package com.pulamsi.angelgooddetail.gooddetail.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.pulamsi.R;
import com.pulamsi.angelgooddetail.gooddetail.GoodDetailConfirmBoard;
import com.pulamsi.angelgooddetail.gooddetail.viewholder.PriceItemViewHolder;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.myinfo.order.entity.ProductPrice;

/**
 * 商品价格适配器
 *
 */
public class PriceListAdapter extends HaiBaseListAdapter<ProductPrice> {
  private Activity activity;
  private GoodDetailConfirmBoard goodDetailConfirmBoard;

  public PriceListAdapter(Activity activity,GoodDetailConfirmBoard goodDetailConfirmBoard) {
    super(activity);
    this.activity = activity;
    this.goodDetailConfirmBoard = goodDetailConfirmBoard;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View convertView = mInflater.inflate(R.layout.gooddetailprice_trecyclerview_item,null);
    if (convertView == null) {
      return null;
    }


    return new PriceItemViewHolder(convertView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    if (holder == null || !(holder instanceof PriceItemViewHolder) || !(getItem(position) instanceof ProductPrice)) {
      return;
    }

    final PriceItemViewHolder newHolder = (PriceItemViewHolder) holder;
    final ProductPrice newItem = (ProductPrice) getItem(position);


    String price;
    /**
     * 判断价格是否为空
     */
    if (newItem.getIntegralPrice() <= 0){
      price = "¥" + newItem.getProductPrice();
    }else {
      if (newItem.getProductPrice().floatValue() <= 0){
        price = newItem.getIntegralPrice() +"积分";
      }else {
        price = "¥" + newItem.getProductPrice() +"+"+ newItem.getIntegralPrice() +"积分";
      }
    }

    newHolder.priceContent.setText(price);
    newHolder.checkBox.setChecked(newItem.isSelect());
    newHolder.checkBox.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //checkbox的选择事件交给diag处理
        goodDetailConfirmBoard.setOnItemSelect(position);
      }
    });
  }
}
