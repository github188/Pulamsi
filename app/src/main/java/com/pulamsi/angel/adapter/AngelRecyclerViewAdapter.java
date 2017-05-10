package com.pulamsi.angel.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.angel.adapter.viewHolder.AngelMoreViewHolder;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.home.entity.AngelMerchantsBean;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-13
 * Time: 14:22
 * FIXME
 */
public class AngelRecyclerViewAdapter extends HaiBaseListAdapter<AngelMerchantsBean> {
    private Activity activity;

    public AngelRecyclerViewAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.angel_list_item, null);
        if (convertView == null) {
            return null;
        }
        return new AngelMoreViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null || !(holder instanceof AngelMoreViewHolder) || !(getItem(position) instanceof AngelMerchantsBean)) {
            return;
        }
        AngelMoreViewHolder viewHolder = (AngelMoreViewHolder) holder;
        AngelMerchantsBean itemBean = getItem(position);


        viewHolder.angelImg.getLayoutParams().height = PulamsiApplication.ScreenWidth / 2;


        //天使图片
        if (!TextUtils.isEmpty(itemBean.getSellerImg())) {
            Glide.with(activity)//更改图片加载框架
                    .load(PulamsiApplication.context.getString(R.string.serverbaseurl) + itemBean.getSellerImg())
                    .centerCrop()
//                    .placeholder(R.drawable.pulamsi_loding)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.angelImg);
        }

        // 天使名称
        if (!TextUtils.isEmpty(itemBean.getShopName())) {
            viewHolder.angelName.setText(itemBean.getShopName());
        } else {
            viewHolder.angelName.setText("暂无信息");
        }

        // 天使等级
        if (!TextUtils.isEmpty(itemBean.getTypeName())) {
            viewHolder.angelLevel.setText(itemBean.getTypeName());
        } else {
            viewHolder.angelLevel.setText("暂无信息");
        }

        // 天使星星
        if (!TextUtils.isEmpty(itemBean.getSellerCredit()+"")) {
            viewHolder.angelScore.setText(itemBean.getSellerCredit()+"分");
            viewHolder.angelRating.setRating(itemBean.getSellerCredit());
        } else {
            viewHolder.angelScore.setText("");
        }


    }
}
