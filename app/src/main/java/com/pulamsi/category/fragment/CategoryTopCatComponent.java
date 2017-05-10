package com.pulamsi.category.fragment;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.category.adapter.CategroyTopCatListAdapter;
import com.pulamsi.category.data.CategoryCatItem;
import com.pulamsi.utils.IGetChildAtPosition;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.List;

/**
 * 【分类】页面 左边顶级分类列表处理模块
 *
 * @author WilliamChik on 15/10/31.
 */
public class CategoryTopCatComponent implements IGetChildAtPosition {

    // 顶级分类列表每个 item 的高度
    private final int TOP_CAT_LIST_ITEM_HEIGHT =
            PulamsiApplication.context.getResources().getDimensionPixelSize(R.dimen.top_category_list_item_height);

    private ProgressWheel mProgressWheel;

    // 顶级分类列表
    private TRecyclerView mTopCatList;

    // 顶级分类列表布局器
    private StaggeredGridLayoutManager mTopCatListLayoutManager;

    // 顶级分类列表数据适配器
    private CategroyTopCatListAdapter mTopCatListAdapter;

    // 记录顶级分类列表滑动的 Y 距离，用于点击顶级分类 item 时列表自动把该 item 顶到列表的第一个位置来显示
    private int mListScrolledY = 0;

    public void initUI(View convertView, Activity activity) {
        // 初始化顶级目录列表
        mTopCatList = (TRecyclerView) convertView.findViewById(R.id.rv_category_top_cat_list);
        mTopCatList.setOnScrollListener(new OnScrollListener());
        mTopCatListLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mTopCatListAdapter = new CategroyTopCatListAdapter(activity, this);
        mTopCatList.setHasFixedSize(true);
        mTopCatList.setAdapter(mTopCatListAdapter);
        mTopCatList.setLayoutManager(mTopCatListLayoutManager);
        mTopCatList.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {

            }
        });
        mProgressWheel = (ProgressWheel) convertView.findViewById(R.id.pw_category_top_cat_list_progress_wheel);
    }

    public void startDataRequest(final CategoryCatComponent mCatComponent) {
        String showCategoryList = PulamsiApplication.context.getString(R.string.serverbaseurl) + PulamsiApplication.context.getString(R.string.showCategoryList);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                showCategoryList, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        List<CategoryCatItem> categoryCatItems = gson.fromJson(result, new TypeToken<List<CategoryCatItem>>() {
                        }.getType());
                        if (null != categoryCatItems) {
                            // 更新顶级类目列表
                            int formerItemCount = mTopCatListAdapter.getItemCount();
                            mTopCatListAdapter.clearDataList();
                            mTopCatListAdapter.notifyItemRangeRemoved(0, formerItemCount);

                            mTopCatListAdapter.addDataList(categoryCatItems);
                            mTopCatListAdapter.notifyItemRangeInserted(0, categoryCatItems.size());
                            mCatComponent.setData(categoryCatItems);
                            if (mTopCatListAdapter.mCurrentCheckPos != -1){
                                mCatComponent.updateCatPackage(mTopCatListAdapter.mCurrentCheckPos);
                            }else {
                                mCatComponent.updateCatPackage(0);
                            }
                            mProgressWheel.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "获取分类数据装配错误");
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(stringRequest);
    }

    /**
     * 列表把该位置的元素顶到列表的第一个位置来显示
     *
     * @param position 需要顶到第一个位置显示的元素位置
     */
    public void scrollPositionToTop(int position) {
        // 获取顶级分类列表需要滑动的距离
        int needScrollY = position * TOP_CAT_LIST_ITEM_HEIGHT - mListScrolledY;
        mTopCatList.smoothScrollBy(0, needScrollY);
    }

    @Override
    public TextView getChildAtPosition(int clickPosition) {
        // 注意这里不能使用 RecyclerView 的 getChildAt(position) 和 StaggeredGridLayoutManager 的 getChildAt(position) 方法获取指定位置的 child view。
        // 因为列表 item 的重用问题，该两个方法获取到的 child view 数量上限是列表在一页中最多显示的 item 数量，例如列表一页只能显示10个 item，
        // 则 getChildAt() 方法的索引最多只能是9。用 StaggeredGridLayoutManager 的 findViewByPosition(position) 可解决问题。
        View childView = mTopCatListLayoutManager.findViewByPosition(clickPosition);
        if (childView == null) {
            return null;
        }
        return (TextView) childView.findViewById(R.id.tv_category_top_cat_item);
    }

    private class OnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            mListScrolledY += dy;
        }
    }

}
