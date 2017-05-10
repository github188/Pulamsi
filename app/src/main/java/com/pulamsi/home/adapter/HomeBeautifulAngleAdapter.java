package com.pulamsi.home.adapter;

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
import com.pulamsi.home.entity.AngelMerchantsBean;
import com.pulamsi.home.viewholder.HomeAngelViewHolder;
import com.pulamsi.utils.GlideCircleTransform;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-10
 * Time: 15:32
 * FIXME
 */
public class HomeBeautifulAngleAdapter extends HaiBaseListAdapter<AngelMerchantsBean> {
    private Activity activity;


    public HomeBeautifulAngleAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.item_home_beautiful_angel, null);
        if (convertView == null) {
            return null;
        }
        return new HomeAngelViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null || !(holder instanceof HomeAngelViewHolder) || !(getItem(position) instanceof AngelMerchantsBean)) {
            return;
        }

        HomeAngelViewHolder viewHolder = (HomeAngelViewHolder) holder;
        AngelMerchantsBean newItem = getItem(position);

        // 天使名称
        if (!TextUtils.isEmpty(newItem.getShopName())) {
            viewHolder.angle_name.setText(newItem.getShopName());
        } else {
            viewHolder.angle_name.setText("暂无信息");
        }
        //商品图片
        if (!TextUtils.isEmpty(newItem.getSellerImg())) {
            Glide.with(activity)//更改图片加载框架
                    .load(PulamsiApplication.context.getString(R.string.serverbaseurl) + newItem.getSellerImg())
//                    .centerCrop()圆形图片不能设置此参数
                    .placeholder(R.drawable.angel_default_photo)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new GlideCircleTransform(activity))//glide设置成圆形图片
                    .into(viewHolder.angle_image);
        }

        // 天使简介
        if (!TextUtils.isEmpty(newItem.getStatement())) {
            viewHolder.angle_introduce.setText(newItem.getStatement());
        } else {
            viewHolder.angle_introduce.setText("暂无信息");
        }

    }
}
