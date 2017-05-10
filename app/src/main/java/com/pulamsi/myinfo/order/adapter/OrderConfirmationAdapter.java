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
import com.pulamsi.myinfo.order.entity.Product;
import com.pulamsi.myinfo.order.viewholder.OrderProductViewHolder;
import com.pulamsi.shoppingcar.entity.CartCommodity;

/**
 * 确认订单产品子项适配器
 */
public class OrderConfirmationAdapter extends HaiBaseListAdapter<CartCommodity> {

    private Activity activity;

    public OrderConfirmationAdapter(Activity activity) {
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
        if (holder == null || !(holder instanceof OrderProductViewHolder) || !(getItem(position) instanceof CartCommodity)) {
            return;
        }

        OrderProductViewHolder newHolder = (OrderProductViewHolder) holder;
        CartCommodity newItem = (CartCommodity) getItem(position);

        //商品数据
        Product product;
        /**
         * 判断是什么商品
         */
        if (newItem.getProduct() == null) {//天使商品
            product = newItem.getSellerProduct();
        } else {
            product = newItem.getProduct();
        }


        //产品名字
        if (!TextUtils.isEmpty(product.getName())){
            newHolder.name.setText(product.getName());
        }else{
            newHolder.name.setText("暂无信息");
        }
        //产品数量
        newHolder.num.setText("X" + newItem.getCount());

        //产品价格
        String total = "¥0.00";
        if (newItem.getProductPrice().floatValue() <= 0) {
            if (newItem.getIntegralPrice() > 0) {
                total = newItem.getIntegralPrice() + "积分";
            }
        } else {
            if (newItem.getIntegralPrice() > 0) {
                total = "¥" + newItem.getProductPrice() + "\n+" + newItem.getIntegralPrice() + "积分";
            } else {
                total = "¥" + newItem.getProductPrice();
            }
        }
        newHolder.pice.setText(total);

        //产品参数
//    if (!TextUtils.isEmpty(newItem.getProductPrice())) {
//      newHolder.name.setText(newItem.getProductPrice());
//    }else {
//      newHolder.name.setText("暂无价格");
//    }

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
