package com.pulamsi.angel.presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lidroid.xutils.util.LogUtils;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.angel.AngelActivity;
import com.pulamsi.angel.AngelDetailsActivity;
import com.pulamsi.angel.adapter.AngelRecyclerViewAdapter;
import com.pulamsi.angel.bean.AngelDateBean;
import com.pulamsi.angel.view.IAngelActivity;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseListview.CommonListLoadMoreHandler;
import com.pulamsi.home.entity.AngelMerchantsBean;
import com.pulamsi.utils.SoftInputUtil;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.PtrStylelFrameLayout;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-13
 * Time: 10:47
 * FIXME
 */
public class AngelActivityPrestener extends RecyclerView.OnScrollListener implements PtrHandler, View.OnClickListener, TextView.OnEditorActionListener {

    public IAngelActivity iAngelActivity;
    /**
     * 下拉控件
     */
    private PtrStylelFrameLayout ptr;
    /**
     * 列表控件
     */
    private TRecyclerView angelRecyclerView;
    /**
     * 搜索控件
     */
    private ImageView searchView;
    /**
     * 搜索的对话框
     */
    private Dialog dialog;
    /**
     * 搜索的返回键
     */
    private ImageView searchBack;
    private EditText searchContent;
    private ImageView search;
    /**
     * 天使列表适配器
     */
    private AngelRecyclerViewAdapter angelRecyclerViewAdapter;

    private Context context;

    /**
     * 网络请求列表数据
     */
    List<AngelMerchantsBean> angelMerchantsBeanList;

    /**
     * 滚动翻页提示工具
     */
    private CommonListLoadMoreHandler mLoadMoreHandler;
    /**
     * 是否开启加载更多
     */
    private boolean mIsLoadMoreOpen;
    private StaggeredGridLayoutManager likelayoutManager;


    /**
     * 业务请求参数
     */
    public Map<String, String> mBizParams;

    public AngelActivityPrestener(IAngelActivity iAngelActivity) {
        this.iAngelActivity = iAngelActivity;
        init();
    }

    private void init() {
        mLoadMoreHandler = iAngelActivity.getCommonListLoadMoreHandler();//滚动翻页提示工具初始化
        mLoadMoreHandler.setLoadFinishMsg(R.string.order_load_finish_footer);
        mIsLoadMoreOpen = true;//默认开启自动加载更多
        ptr = iAngelActivity.getPtrStylelFrameLayout();
        angelRecyclerView = iAngelActivity.getTRecyclerView();
        searchView = (ImageView) iAngelActivity.getRightSearchView();
        searchView.setOnClickListener(this);
        ptr.setPtrHandler(this);//设置下下刷新回调
        context = iAngelActivity.getContext();

        angelRecyclerView.setHasFixedSize(true);
//        angelRecyclerView.setLayoutManager();
        likelayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        layoutManager = new GridLayoutManager(context, 2);
        angelRecyclerView.setLayoutManager(likelayoutManager);
        angelRecyclerViewAdapter = new AngelRecyclerViewAdapter((Activity) context);
        angelRecyclerView.setAdapter(angelRecyclerViewAdapter);

        angelRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                Intent intent = new Intent(context, AngelDetailsActivity.class);
                intent.putExtra("sellerId", angelRecyclerViewAdapter.getItem(position).getId());//传递天使ID
                ((Activity) context).startActivity(intent);

            }
        });

        angelRecyclerView.addOnScrollListener(this);
    }


    /**
     * 请求网络数据
     */
    public void RequestDate(final Map mapDatePage) {
        mBizParams = mapDatePage;
        String url = context.getString(R.string.serverbaseurl) + context.getString(R.string.angelSearchList);
        StringRequest angelRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Gson gson = new Gson();
                        AngelDateBean angelDateBean = gson.fromJson(response, AngelDateBean.class);
                        angelMerchantsBeanList = angelDateBean.getList();
                        updateAngelData(angelMerchantsBeanList);//更新数据
                        if (angelMerchantsBeanList != null && angelMerchantsBeanList.size() < 8) {
                            removeFooter();//如果数据小于十条则不需要加载更多
                        }
                        //判断是否为空
                        if ((angelMerchantsBeanList == null || angelMerchantsBeanList.size() == 0) && angelRecyclerViewAdapter.getItemCount() == 0) {
                            iAngelActivity.showEmpty();
                        } else {
                            iAngelActivity.showSuccessful();
                        }
                    } catch (IllegalArgumentException e) {
                        LogUtils.e("天使更多包装错误");
                    }
                    //如果启用下拉就让他完成
                    if (ptr.isRefreshing()) {
                        ptr.refreshComplete();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //如果启用下拉就让他完成
                if (ptr.isRefreshing()) {
                    ptr.refreshComplete();
                }
                iAngelActivity.showError();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return mapDatePage;
            }
        };
        angelRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        PulamsiApplication.requestQueue.add(angelRequest);
    }


    private void removeFooter() {
        if (mIsLoadMoreOpen) {
            mIsLoadMoreOpen = false;
            mLoadMoreHandler.removeFooter(angelRecyclerView);
        }
    }


    /**
     * 更新数据上去
     */
    private void updateAngelData(List<AngelMerchantsBean> angelMerchantsBeanList) {
        if (mIsLoadMoreOpen) {
            mLoadMoreHandler.onWaitToLoadMore(angelRecyclerView);//要预先加载出来，添加到尾部
        }
        angelRecyclerViewAdapter.addDataList(angelMerchantsBeanList);
        angelRecyclerViewAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, angelRecyclerView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        mBizParams.put("pageNumber", "1");//下拉刷新需要重置为第一页
        RequestDate(mBizParams);//请求数据
        angelRecyclerViewAdapter.clearDataList();//刷新需要清除数据
        mIsLoadMoreOpen = true;
    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        // 加载更多功能开启，则在列表滚动到底部时加入加载更多的逻辑
        if (mIsLoadMoreOpen) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                // 列表已停止滑动
                int[] lastVisiblePosition = likelayoutManager.findLastCompletelyVisibleItemPositions(null);
                int lastItem = Math.max(lastVisiblePosition[0], lastVisiblePosition[1]);
                if (lastItem >= likelayoutManager.getItemCount() - 1 && !mLoadMoreHandler.isLoadingMore()) {//如果是最后一个位置就是滑到底部了
                    mLoadMoreHandler.onLoadStart();
                    doNextPage();
                }
            }
        }
    }

    private void doNextPage() {
        String page = pagePlusOne();
        if ("-1".equals(page)) {
            return;
        }
        mBizParams.put("pageNumber", page);
        RequestMoreDate(mBizParams);
    }


    /**
     * “page” 参数加 1
     *
     * @return "-1" if error occurred
     */
    private String pagePlusOne() {
        if (mBizParams == null) {
            return "-1";
        }

        String page = mBizParams.get("pageNumber").toString();
        try {
            page = String.valueOf(Integer.valueOf(page) + 1);
        } catch (Exception e) {
            e.printStackTrace();
            page = "-1";
        }
        return page;
    }


    /**
     * 请求网络数据
     */
    public void RequestMoreDate(final Map mapDatePageMore) {
        String url = context.getString(R.string.serverbaseurl) + context.getString(R.string.angelSearchList);
        StringRequest angelRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Gson gson = new Gson();
                        AngelDateBean angelDateBean = gson.fromJson(response, AngelDateBean.class);
                        angelMerchantsBeanList = angelDateBean.getList();
                        //判断是否为空
                        if ((angelMerchantsBeanList == null || angelMerchantsBeanList.size() == 0)) {
                            //如果为空就说明没有数据了，关闭加载更多
                            if (mIsLoadMoreOpen) {
                                mLoadMoreHandler.onLoadFinish(angelRecyclerView);
                                mIsLoadMoreOpen = false;
                            }
                        } else {
                            updateAngelData(angelMerchantsBeanList);//更新数据
                        }
                    } catch (IllegalArgumentException e) {
                        LogUtils.e("天使更多包装错误");
                    }
                    //如果启用下拉就让他完成
                    if (ptr.isRefreshing()) {
                        ptr.refreshComplete();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.e(error.toString());
                //如果启用下拉就让他完成
                if (ptr.isRefreshing()) {
                    ptr.refreshComplete();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return mapDatePageMore;
            }
        };
        angelRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        PulamsiApplication.requestQueue.add(angelRequest);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == searchView.getId()) {
            initSearch();
        }
        if (searchBack.getId() == v.getId()) {
            SoftInputUtil.hideSoftInput((Activity) context);
            dialog.dismiss();
        }
        if (v.getId() == search.getId()) {
            searchSubmit(searchContent.getText().toString());
        }
    }

    private void initSearch() {
        dialog = new Dialog(context, R.style.Search_Dialog);
        dialog.setContentView(R.layout.angel_search_dialog);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = PulamsiApplication.ScreenWidth; // 宽度设置为屏幕
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        dialogWindow.setAttributes(lp);
        dialog.show();
        initSearchView();//初始化控件
    }

    private void initSearchView() {
        searchContent = (EditText) dialog.findViewById(R.id.et_search_content);
        search = (ImageView) dialog.findViewById(R.id.iv_search_view);
        searchBack = (ImageView) dialog.findViewById(R.id.iv_search_back);
        ((BaseActivity) context).showSoftInput(searchContent);
        searchBack.setOnClickListener(this);
        searchContent.setOnEditorActionListener(this);
        search.setOnClickListener(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchSubmit(v.getText().toString());
        }
        return false;
    }


    private void searchSubmit(String content) {
        if (content.equals("")) {
            ToastUtil.showToast("输入不能为空");
        } else {
            dialog.dismiss();
            Intent intent = new Intent(context, AngelActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("isSearch", true);
            intent.putExtra("searchContent", content);
            context.startActivity(intent);
        }
    }
}
