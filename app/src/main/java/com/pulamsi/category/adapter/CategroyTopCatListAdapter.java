package com.pulamsi.category.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.base.otto.BusProvider;
import com.pulamsi.category.data.CategoryCatItem;
import com.pulamsi.category.viewholder.CategoryTopCatItemViewHolder;
import com.pulamsi.utils.IGetChildAtPosition;


/**
 * 【分类】页面 顶级目录数据适配器
 *
 * @author WilliamChik on 15/9/24.
 */
public class CategroyTopCatListAdapter extends HaiBaseListAdapter<CategoryCatItem> {

  // 用于记录当前选中的分类 View 的位置，避免列表滚动时由于布局重用导致的选中状态重复的问题
  public int mCurrentCheckPos = -1;

  // 标志列表是否是第一次加载，第一次加载时需要把顶级目录的第一项选中
  private boolean mIsFirstLoad = true;

  private IGetChildAtPosition mGetChildAtPositionImpl;

  public CategroyTopCatListAdapter(Activity activity, IGetChildAtPosition getChildAtPositionImpl) {
    super(activity);
    mGetChildAtPositionImpl = getChildAtPositionImpl;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View convertView = mInflater.inflate(R.layout.category_top_cat_list_item, null);
    if (convertView == null) {
      return null;
    }
    return new CategoryTopCatItemViewHolder(convertView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    if (holder == null || !(holder instanceof CategoryTopCatItemViewHolder) || !(getItem(position) instanceof CategoryCatItem)) {
      return;
    }

    final CategoryTopCatItemViewHolder newHolder = (CategoryTopCatItemViewHolder) holder;
    final CategoryCatItem newItem = getItem(position);

    if (!TextUtils.isEmpty(newItem.getName())) {
      //判断是不是首页跳转
      if (mFlag)
      {
        checkTopCatItem(newHolder.topCatItemTv, newItem, mPosition);
        uncheckTopCatItem(newHolder.topCatItemTv);
        mFlag = false;
      }else {
        if (position == mCurrentCheckPos) {
          checkTopCatItem(newHolder.topCatItemTv, position);
        } else {
          uncheckTopCatItem(newHolder.topCatItemTv);
        }
        // 第一次加载时需要把顶级目录的第一项选中
        if (mIsFirstLoad && position == 0) {
          mIsFirstLoad = false;
          checkTopCatItem(newHolder.topCatItemTv, newItem, position);
        }
      }
      newHolder.topCatItemTv.setText(newItem.getName());
      newHolder.topCatItemTv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View clickView) {
          itemClickLogic((TextView) clickView, newItem, position);
        }
      });
    }
  }

  /**
   * 点击分类 item，发送通知，并把该分类顶到列表第一个位置来显示
   *
   * @param clickView     点击的分类 View
   * @param newItem       点击的分类 View 对应的 item data object
   * @param clickPosition 点击的分类 View 所在的位置
   */
  private void itemClickLogic(TextView clickView, CategoryCatItem newItem, int clickPosition) {
    if (clickPosition != mCurrentCheckPos) {
      // 必须先取消选中上一个选中的 View，才能执行选中当前 View 的操作
      TextView currenClickItemTv = mGetChildAtPositionImpl.getChildAtPosition(mCurrentCheckPos);
      if (currenClickItemTv != null) {
        uncheckTopCatItem(currenClickItemTv);
      }
      checkTopCatItem(clickView, newItem, clickPosition);
    }
  }

  /**
   * 选中对应的分类 View，并发通知
   *
   * @param topCatItemTv 分类 Text View
   * @param itemDO       item data object
   * @param position     View 对应的位置
   */
  private void checkTopCatItem(TextView topCatItemTv, CategoryCatItem itemDO, int position) {
    topCatItemTv.setBackgroundResource(R.drawable.layer_list_bg_stroke_0_0_0_0_1px_solid_transparent);
    topCatItemTv.setTextColor(mActivity.getResources().getColor(R.color.app_pulamsi_main_color));
        mCurrentCheckPos = position;
    if (itemDO != null) {
      BusProvider.getInstance().post(new NotifyCategoryTopCatListToScrollEvent(itemDO, position));
    }
  }

  /**
   * 选中对应的分类 View，但不发通知
   *
   * @param topCatItemTv 分类 Text View
   * @param position     View 对应的位置
   */
  private void checkTopCatItem(TextView topCatItemTv, int position) {
    checkTopCatItem(topCatItemTv, null, position);
  }

  /**
   * 取消选中对应的分类 View
   *
   * @param topCatItemTv 分类 Text View
   */
  private void uncheckTopCatItem(TextView topCatItemTv) {
    topCatItemTv.setBackgroundResource(R.drawable.layer_list_bg_stroke_0_0_0_1px_1px_solid_f4);
    topCatItemTv.setTextColor(mActivity.getResources().getColor(R.color.app_font_color_2f));
  }

  /**
   * 通知 {} 顶级目录列表把指定位置的 item 顶到头部
   */
  public class NotifyCategoryTopCatListToScrollEvent {

    public CategoryCatItem itemDO;

    public int clickPosition;

    public NotifyCategoryTopCatListToScrollEvent(CategoryCatItem itemDO, int clickPosition) {
      this.itemDO = itemDO;
      this.clickPosition = clickPosition;
    }
  }


  static boolean mFlag = false;
  static int mPosition;


  public static void setGotoCatTagPosition(boolean flag,int position){
    mFlag = flag;
    mPosition = position;
  }

}
