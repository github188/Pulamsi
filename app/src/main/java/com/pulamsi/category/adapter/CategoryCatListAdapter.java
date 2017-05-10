package com.pulamsi.category.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pulamsi.R;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.category.data.CategoryCatItem;
import com.pulamsi.category.viewholder.CategoryCatItemViewHolder;


/**
 * 【分类】页面 分类列表数据适配器
 *
 * @author WilliamChik on 2015/8/10.
 */
public class CategoryCatListAdapter extends HaiBaseListAdapter<CategoryCatItem> {
  private Activity activity;
  public CategoryCatListAdapter(Activity activity) {
    super(activity);
    this.activity = activity;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View convertView = mInflater.inflate(R.layout.category_cat_list_item, null);
    if (convertView == null) {
      return null;
    }
    return new CategoryCatItemViewHolder(convertView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder == null || !(holder instanceof CategoryCatItemViewHolder) || !(getItem(position) instanceof CategoryCatItem)) {
      return;
    }

    CategoryCatItemViewHolder newHolder = (CategoryCatItemViewHolder) holder;
    CategoryCatItem newItem = (CategoryCatItem) getItem(position);

    if (!TextUtils.isEmpty(newItem.getName())) {
      newHolder.catNameTv.setText(newItem.getName());
    }

    if (!TextUtils.isEmpty(newItem.getImage())) {
      Glide.with(activity)//更改图片加载框架
              .load(mActivity.getString(R.string.serverbaseurl) + newItem.getImage())
              .centerCrop()
              .placeholder(R.drawable.pulamsi_loding)
              .crossFade()
              .diskCacheStrategy(DiskCacheStrategy.ALL)
              .into(newHolder.catImg);
//      PulamsiApplication.imageLoader.displayImage(mActivity.getString(R.string.serverbaseurl)+newItem.getImage(), newHolder.catImg, PulamsiApplication.options);
    }
  }

}
