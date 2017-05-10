package com.pulamsi.angel.adapter;

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
import com.pulamsi.angel.adapter.viewHolder.AngelProducteViewHolder;
import com.pulamsi.angel.bean.AngelProductBean;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;


/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-23
 * Time: 15:25
 * FIXME
 */
public class AngelGoodsAdapter extends HaiBaseListAdapter<AngelProductBean> {

    private Activity activity;

    public AngelGoodsAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.angel_product_item, null);
        if (convertView == null) {
            return null;
        }
        return new AngelProducteViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null || !(holder instanceof AngelProducteViewHolder) || !(getItem(position) instanceof AngelProductBean)) {
            return;
        }
        AngelProductBean itemBean = getItem(position);
        AngelProducteViewHolder viewHolder = (AngelProducteViewHolder) holder;

        viewHolder.productImg.getLayoutParams().height = PulamsiApplication.ScreenWidth / 2;

        //天使图片
        if (!TextUtils.isEmpty(itemBean.getPic())) {
            Glide.with(activity)//更改图片加载框架
                    .load(PulamsiApplication.context.getString(R.string.serverbaseurl) + itemBean.getPic())
                    .centerCrop()
                    .placeholder(R.drawable.pulamsi_loding)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.productImg);
        }

        if (!TextUtils.isEmpty(itemBean.getPrice().toString()))
            viewHolder.productPrice.setText(itemBean.getPrice().toString());


        if (!TextUtils.isEmpty(itemBean.getMarketPrice().toString()))
            viewHolder.productMarketPrice.setText(itemBean.getMarketPrice().toString());

        viewHolder.productMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//删除线
    }
}
