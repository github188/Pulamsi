package com.pulamsi.home.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.base.baseUtil.PriceUtil;
import com.pulamsi.home.entity.HotSellProduct;
import com.pulamsi.home.viewholder.HomeSnapUpViewHolder;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-07-05
 * Time: 09:40
 * FIXME
 */
public class HomeSnapUpListAdapter extends HaiBaseListAdapter<HotSellProduct> {
    private Activity activity;

    public HomeSnapUpListAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.item_home_snap_up, null);
        if (convertView == null) {
            return null;
        }
        return new HomeSnapUpViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null || !(holder instanceof HomeSnapUpViewHolder) || !(getItem(position) instanceof HotSellProduct)) {
            return;
        }
        HomeSnapUpViewHolder viewHolder = (HomeSnapUpViewHolder) holder;
        HotSellProduct newItem = getItem(position);

        // 商品名称
        if (!TextUtils.isEmpty(newItem.getName())) {
            viewHolder.product_name.setText(newItem.getName());
        } else {
            viewHolder.product_name.setText("暂无信息");
        }
        //商品图片
        if (!TextUtils.isEmpty(newItem.getPic())) {
            Glide.with(activity)//更改图片加载框架
                    .load(PulamsiApplication.context.getString(R.string.serverbaseurl) + newItem.getPic())
                    .centerCrop()
//                    .placeholder(R.drawable.pulamsi_loding)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.product_image);
        }

        // 商品抢购价格
        if (!TextUtils.isEmpty(newItem.getPanicBuyPrice())) {
            viewHolder.product_panicBuyPrice.setText(PriceUtil.RMB + newItem.getPanicBuyPrice());
        } else {
            viewHolder.product_panicBuyPrice.setText("暂无信息");
        }
        // 商品历史价格
        if (!TextUtils.isEmpty(newItem.getPrice())) {
            viewHolder.product_price.setText(newItem.getPrice());
        } else {
            viewHolder.product_price.setText("暂无信息");
        }
        viewHolder.product_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//删除线
    }
}
