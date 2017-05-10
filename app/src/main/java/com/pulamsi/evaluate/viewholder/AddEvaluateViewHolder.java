package com.pulamsi.evaluate.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pulamsi.R;


/**
 *新增商品评论视图容器
 *
 */
public class AddEvaluateViewHolder extends RecyclerView.ViewHolder {

  public EditText content;//评论内容
  public RatingBar ratingBar;//评分控件
  public ImageView image;//商品图片
  public ImageView more;//商品图片
  public TextView name;//商品名字
  public TextView state;//评价状态
  public RelativeLayout hintContent;

  public AddEvaluateViewHolder(View itemView) {
    super(itemView);

    content = (EditText) itemView.findViewById(R.id.addevaluate_content);
    ratingBar = (RatingBar) itemView.findViewById(R.id.addevaluate_ratingbar);
    image = (ImageView) itemView.findViewById(R.id.addevaluate_image);
    more = (ImageView) itemView.findViewById(R.id.load_more);
    name = (TextView) itemView.findViewById(R.id.addevaluate_name);
    state = (TextView) itemView.findViewById(R.id.evaluate_state);
    hintContent = (RelativeLayout) itemView.findViewById(R.id.hint_content);
  }

}
