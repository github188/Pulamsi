package com.pulamsi.home.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulamsi.R;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-07-05
 * Time: 10:08
 * FIXME
 */
public class HomeSnapUpViewHolder extends RecyclerView.ViewHolder {

    public ImageView product_image;//商品图片
    public TextView product_name;//商品名称
    public TextView product_price;//商品价格
    public TextView product_panicBuyPrice;//抢购价格

    public HomeSnapUpViewHolder(View itemView) {
        super(itemView);
        product_image = (ImageView) itemView.findViewById(R.id.iv_image);
        product_name = (TextView) itemView.findViewById(R.id.tv_name);
        product_price = (TextView) itemView.findViewById(R.id.tv_price);
        product_panicBuyPrice = (TextView) itemView.findViewById(R.id.tv_panicBuyprice);
    }

}
