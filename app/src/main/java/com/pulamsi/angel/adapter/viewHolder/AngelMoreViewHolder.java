package com.pulamsi.angel.adapter.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pulamsi.R;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-13
 * Time: 15:03
 * FIXME
 */
public class AngelMoreViewHolder extends RecyclerView.ViewHolder {

    public ImageView angelImg;//天使图片
    public TextView angelName;//天使名字
    public TextView angelLevel;//天使等级
    public RatingBar angelRating;//天使星星
    public TextView angelScore;//天使评分


    public AngelMoreViewHolder(View convertView) {
        super(convertView);
        angelImg = (ImageView) convertView.findViewById(R.id.angel_item_img);
        angelName = (TextView) convertView.findViewById(R.id.angel_item_title);
        angelLevel = (TextView) convertView.findViewById(R.id.angel_item_level);
        angelRating = (RatingBar) convertView.findViewById(R.id.angel_item_ratingBar);
        angelScore = (TextView) convertView.findViewById(R.id.angel_item_score);
    }
}
