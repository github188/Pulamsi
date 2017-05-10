package com.pulamsi.angel.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulamsi.R;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-23
 * Time: 15:36
 * FIXME
 */
public class AngelProducteViewHolder extends RecyclerView.ViewHolder {

    public ImageView productImg;
    public TextView productPrice;
    public TextView productMarketPrice;

    public AngelProducteViewHolder(View convertView) {
        super(convertView);
        productImg = (ImageView) convertView.findViewById(R.id.angel_product_item_img);
        productPrice = (TextView) convertView.findViewById(R.id.angel_product_item_price);
        productMarketPrice = (TextView) convertView.findViewById(R.id.angel_product_item_marketPrice);
    }
}
