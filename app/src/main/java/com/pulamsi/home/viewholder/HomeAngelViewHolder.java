package com.pulamsi.home.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulamsi.R;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-12
 * Time: 09:47
 * FIXME
 */
public class HomeAngelViewHolder extends RecyclerView.ViewHolder {

    public ImageView angle_image;//天使图片
    public TextView angle_name;//天使名称
    public TextView angle_introduce;//天使介绍


    public HomeAngelViewHolder(View convertView) {
        super(convertView);
        angle_image = (ImageView) convertView.findViewById(R.id.iv_angle_photo);
        angle_name = (TextView) convertView.findViewById(R.id.tv_angle_name);
        angle_introduce = (TextView) convertView.findViewById(R.id.tv_angle_introduce);
    }
}
