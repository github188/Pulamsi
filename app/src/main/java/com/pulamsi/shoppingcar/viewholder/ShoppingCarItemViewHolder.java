package com.pulamsi.shoppingcar.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.views.ItemNumberEditView;


/**
 * 购物车视图容器
 *
 */
public class ShoppingCarItemViewHolder extends RecyclerView.ViewHolder {

  public CheckBox checkBox;
  public ImageView goodIcon;//商品图标
  public TextView goodtile;//商品名字，标题
  public TextView goodsku;//商品参数
  public TextView price;//商品价格
  public TextView integralPrice;//商品积分
  public TextView goodnum;//商品数量
  public TextView delete;//删除按钮
  public LinearLayout editBoard;
  public TextView editTitle;
  public ItemNumberEditView numberEditView;


  public ShoppingCarItemViewHolder(View itemView) {
    super(itemView);

    checkBox = (CheckBox) itemView.findViewById(R.id.iv_shopping_car_good_check_box);
    goodIcon = (ImageView) itemView.findViewById(R.id.iv_home_shopping_car_main_image);
    goodtile = (TextView) itemView.findViewById(R.id.tv_shopping_car_good_title);
    goodsku = (TextView) itemView.findViewById(R.id.tv_shopping_car_good_sku);
    price = (TextView) itemView.findViewById(R.id.tv_shopping_car_good_price);
    integralPrice = (TextView) itemView.findViewById(R.id.tv_shopping_car_good_market_price);
    goodnum = (TextView) itemView.findViewById(R.id.tv_shopping_car_good_num);
    delete = (TextView) itemView.findViewById(R.id.tv_shopping_car_delete_item);
    editTitle = (TextView) itemView.findViewById(R.id.tv_shopping_car_item_title);
    editBoard = (LinearLayout) itemView.findViewById(R.id.ll_shopping_car_edit_board);
    numberEditView = (ItemNumberEditView) itemView.findViewById(R.id.inev_shopping_car_edit_item_num);

  }

}
