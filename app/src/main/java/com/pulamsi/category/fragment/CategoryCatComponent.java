package com.pulamsi.category.fragment;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.pulamsi.R;
import com.pulamsi.category.adapter.CategoryCatListAdapter;
import com.pulamsi.category.data.CategoryCatItem;
import com.pulamsi.utils.GoodsHelper;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.List;


/**
 * 【分类】页面 右边分类打包列表的处理模块
 */
public class CategoryCatComponent {

    private ProgressWheel mProgressWheel;

    // 分类列表
    private TRecyclerView mCatList;

    // 分类列表数据适配器
    private CategoryCatListAdapter mCatListAdapter;

    // 广告位 image view
    private ImageView mTopBannerIv;

    private List<CategoryCatItem> categoryCatItems;

    private Activity activity;

    public void initUI(View convertView, final Activity activity) {

        this.activity = activity;

        mProgressWheel = (ProgressWheel) convertView.findViewById(R.id.pw_category_cat_list_progress_wheel);
        // 初始化顶级类目对应的分类列表
        mCatList = (TRecyclerView) convertView.findViewById(R.id.rv_category_cat_detail_list);
        StaggeredGridLayoutManager catListLm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mCatListAdapter = new CategoryCatListAdapter(activity);
        mCatList.setHasFixedSize(true);
        mCatList.setAdapter(mCatListAdapter);
        mCatList.setLayoutManager(catListLm);
        /**
         * 点击跳转
         * 分类内容item点击事件
         */
        mCatList.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                if (mCatListAdapter.getItem(position) == null) {
                    return;
                }
                //跳转详情页
                GoodsHelper.gotoDetail(mCatListAdapter.getItem(position).getId(), activity);

            }
        });

        // 广告位banner，作为 header 加入到分类列表中
        View bannerHeader = activity.getLayoutInflater().inflate(R.layout.category_cat_top_banner, null);
        mTopBannerIv = (ImageView) bannerHeader.findViewById(R.id.iv_category_top_banner);
        mTopBannerIv.setImageResource(R.drawable.banner);
        mCatList.addHeaderView(bannerHeader);
    }


    public void setData(List<CategoryCatItem> categoryCatItems) {
        this.categoryCatItems = categoryCatItems;
    }

    /**
     * 更新广告 banner 的信息
     */
    private void updateBanner(@NonNull final int clickPosition) {
        // 展示图片的处理
//    if (clickPosition > 7){
//      PulamsiApplication.imageLoader.displayImage(topBanner[0], mTopBannerIv, PulamsiApplication.options);
//    }else {
//      PulamsiApplication.imageLoader.displayImage(topBanner[clickPosition], mTopBannerIv, PulamsiApplication.options);
//    }
//    mTopBannerIv.setImageResource(R.drawable.banner);
    }

    /**
     * 更新分类列表数据，流程如下：
     * 1. 从缓存中读取指定分类 id 的分类列表打包数据，如果存在则直接用该分类列表打包数据更新当前页面列表；
     * 2. 如果指定 id 的分类列表打包数据不存在，则发送网络请求拉取分类列表打包数据，并更新当前分类列表，同时缓存该分类打包列表的数据。
     */
    public void updateCatPackage(int clickPosition) {
        mProgressWheel.setVisibility(View.VISIBLE);
        mCatListAdapter.clearDataList();
        mCatListAdapter.notifyDataSetChanged();
        // 缓存存在，直接读取缓存更新列表
        mCatListAdapter.addDataList(categoryCatItems.get(clickPosition).getList());
        // 通知更新的话，可以连 header 一起通知更新
        mCatListAdapter.notifyDataSetChanged();
        mProgressWheel.setVisibility(View.GONE);
    }



}
