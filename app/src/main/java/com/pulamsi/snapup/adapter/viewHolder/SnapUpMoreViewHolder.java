package com.pulamsi.snapup.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulamsi.R;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-08-26
 * Time: 10:17
 * FIXME
 */
public class SnapUpMoreViewHolder extends RecyclerView.ViewHolder {

    public ImageView productImage;//商品图片
    public TextView productName;//商品名称
    public TextView productPrice;//商品售价
    public TextView productMarketPrice;//商品市场价
    public TextView productSales;//商品销量
    public TextView snapUpBuy;//抢购



    public SnapUpMoreViewHolder(View contentView) {
        super(contentView);
        productImage = (ImageView) contentView.findViewById(R.id.iv_product_image);
        productName = (TextView) contentView.findViewById(R.id.tv_product_name);
        productPrice = (TextView) contentView.findViewById(R.id.tv_product_Price);
        productMarketPrice = (TextView) contentView.findViewById(R.id.tv_product_marketPrice);
        productSales = (TextView) contentView.findViewById(R.id.tv_product_sales);
        snapUpBuy = (TextView) contentView.findViewById(R.id.tv_snapUp_buy);
    }
}
