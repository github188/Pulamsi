package com.pulamsi.category.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pulamsi.R;
import com.pulamsi.base.otto.BusProvider;
import com.pulamsi.category.adapter.CategroyTopCatListAdapter;
import com.pulamsi.constant.Constants;
import com.squareup.otto.Subscribe;

/**
 * 【分类】页面的 分类 Fragment
 *
 * @author WilliamChik on 15/9/23.
 */
public class CategoryCatFragment extends Fragment {

  // 顶级类目列表处理模块
  private CategoryTopCatComponent mTopCatComponent;

  // 顶级类目对应的分类打包列表处理模块
  private CategoryCatComponent mCatComponent;

  @Override
  public void onAttach(Activity activity) {

    super.onAttach(activity);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.category_cat_fragment, null);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    if (view != null) {
      initUI(view);
      // 数据请求提前
      startDataRequest();
      Constants.categoryCatFragment = this;
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    BusProvider.getInstance().register(this);
  }

  @Override
  public void onPause() {
    super.onPause();
    BusProvider.getInstance().unregister(this);
  }

  private void initUI(View convertView) {
    mCatComponent = new CategoryCatComponent();
    mTopCatComponent = new CategoryTopCatComponent();
    mTopCatComponent.initUI(convertView, getActivity());
    mCatComponent.initUI(convertView, getActivity());
  }

  public void startDataRequest() {
    // 请求顶级目录列表
    mTopCatComponent.startDataRequest(mCatComponent);
  }

  @Subscribe
  public void onNotifyCategoryTopCatListToScrollEvent(CategroyTopCatListAdapter.NotifyCategoryTopCatListToScrollEvent event) {
    // 点击顶级分类列表指定位置的元素时，列表自动把该位置的元素顶到列表的第一个位置来显示
    mTopCatComponent.scrollPositionToTop(event.clickPosition);
    // 更新分类列表数据
    mCatComponent.updateCatPackage(event.clickPosition);
  }

}
