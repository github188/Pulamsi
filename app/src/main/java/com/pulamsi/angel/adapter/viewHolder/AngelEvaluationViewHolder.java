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
public class AngelEvaluationViewHolder extends RecyclerView.ViewHolder {

    public ImageView aevaluationImg;
    public TextView evaluationName;
    public TextView evaluationContent;
    public TextView evaluationNTime;

    public AngelEvaluationViewHolder(View convertView) {
        super(convertView);
        aevaluationImg = (ImageView) convertView.findViewById(R.id.angel_evaluation_img);
        evaluationName = (TextView) convertView.findViewById(R.id.angel_evaluation_name);
        evaluationContent = (TextView) convertView.findViewById(R.id.angel_evaluation_content);
        evaluationNTime = (TextView) convertView.findViewById(R.id.angel_evaluation_time);
    }
}
