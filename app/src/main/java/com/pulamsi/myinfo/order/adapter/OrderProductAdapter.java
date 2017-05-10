package com.pulamsi.myinfo.order.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.myinfo.order.entity.OrderItem;
import com.pulamsi.myinfo.order.entity.Product;
import com.pulamsi.myinfo.order.viewholder.OrderProductViewHolder;

/**
 *
 * 订单子项产品适配器
 *
 */
public class OrderProductAdapter extends HaiBaseListAdapter<OrderItem> {

  private Activity activity;
  public OrderProductAdapter(Activity activity) {
    super(activity);
    this.activity = activity;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View convertView = mInflater.inflate(R.layout.orderlist_product_item, null);
    if (convertView == null) {
      return null;
    }

    return new OrderProductViewHolder(convertView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder == null || !(holder instanceof OrderProductViewHolder) || !(getItem(position) instanceof OrderItem)) {
      return;
    }

    OrderProductViewHolder newHolder = (OrderProductViewHolder) holder;
    OrderItem newItem = (OrderItem) getItem(position);


    //产品名字
    if (!TextUtils.isEmpty(newItem.getProductName())) {
      newHolder.name.setText(newItem.getProductName());
    }else {
      newHolder.name.setText("暂无信息");
    }
    //产品数量
    if (!TextUtils.isEmpty(newItem.getProductQuantity())) {
      newHolder.num.setText("X" +newItem.getProductQuantity());
    }else {
      newHolder.num.setText("X1");
    }

    //产品价格
    String total = "¥0.00";
    if (newItem.getProductPrice().floatValue() <= 0){
      if (newItem.getIntegralPrice() > 0){
        total = newItem.getIntegralPrice()+"积分";
      }
    }else {
      if (newItem.getIntegralPrice() > 0){
        total = "¥"+newItem.getProductPrice()+"\n+"+newItem.getIntegralPrice()+"积分";
      }else {
        total = "¥"+newItem.getProductPrice();
      }
    }
      newHolder.pice.setText(total);
    //产品参数
//    if (!TextUtils.isEmpty(newItem.getProductPrice())) {
//      newHolder.name.setText(newItem.getProductPrice());
//    }else {
//      newHolder.name.setText("暂无价格");
//    }

    /**
     * 天使商品需要作此判断
     * 我写这段代码的时候只有我和上帝看得懂
     *
     * 现在
     * 只有上帝看得懂了
     */
    Product product = null;

    if (newItem.getProduct() != null){
      product = newItem.getProduct();
    }else if (newItem.getSellerProduct() != null){
      product = newItem.getSellerProduct();
    }

    //产品图片
    if (!TextUtils.isEmpty(product.getPic())) {
      Glide.with(activity)//更改图片加载框架
              .load(PulamsiApplication.context.getString(R.string.serverbaseurl) + product.getPic())
              .centerCrop()
              .placeholder(R.drawable.pulamsi_loding)
              .crossFade()
              .diskCacheStrategy(DiskCacheStrategy.ALL)
              .into(newHolder.itemIcon);
//      PulamsiApplication.imageLoader.displayImage(PulamsiApplication.context.getString(R.string.serverbaseurl) + product.getPic(),newHolder.itemIcon,PulamsiApplication.options);
    }

  }

}
