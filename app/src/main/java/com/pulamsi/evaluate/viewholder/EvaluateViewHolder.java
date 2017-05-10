package com.pulamsi.evaluate.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pulamsi.R;


/**
 *商品评论视图容器
 *
 */
public class EvaluateViewHolder extends RecyclerView.ViewHolder {

  public TextView userName;//用户名
  public TextView content;//评论内容
  public TextView time;//评论时间
  public RatingBar ratingBar;//评分控件

  public EvaluateViewHolder(View itemView) {
    super(itemView);

    userName = (TextView) itemView.findViewById(R.id.evaluate_username);
    content = (TextView) itemView.findViewById(R.id.evaluate_content);
    time = (TextView) itemView.findViewById(R.id.evaluate_time);
    ratingBar = (RatingBar) itemView.findViewById(R.id.evaluate_ratingbar);
  }

}
