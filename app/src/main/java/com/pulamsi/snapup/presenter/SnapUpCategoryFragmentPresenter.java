package com.pulamsi.snapup.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.util.LogUtils;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.snapup.SnapUpCategoryFragment;
import com.pulamsi.snapup.adapter.SnapUpRecyclerViewAdapter;
import com.pulamsi.snapup.bean.SnapUpBean;
import com.pulamsi.snapup.bean.SnapUpPackBean;
import com.pulamsi.snapup.view.ISnapUpCategoryFragment;
import com.pulamsi.views.PtrStylelFrameLayout;
import com.taobao.uikit.feature.view.TRecyclerView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-08-25
 * Time: 16:56
 * FIXME
 */
public class SnapUpCategoryFragmentPresenter implements PtrHandler {

    /**
     * Fragment的接口
     */
    private ISnapUpCategoryFragment iSnapUpCategoryFragment;
    /**
     * 下拉刷新控件
     */
    private PtrStylelFrameLayout ptr;
    /**
     * 列表控件
     */
    private TRecyclerView snapUpRecyclerView;
    /**
     * 上下文对象
     */
    private Context context;
    /**
     * 抢购列表适配器
     */
    private SnapUpRecyclerViewAdapter recyclerViewAdapter;

    /**
     * 标记是下拉还是滑动刷新
     */
    public final boolean PULL_REFRESH = false;
    public final boolean MOVE_REFRESH = true;
    /**
     * 标识为正在抢购还是即将开始
     */
    private String snapUpType;

    public SnapUpCategoryFragmentPresenter(ISnapUpCategoryFragment iSnapUpCategoryFragment) {
        this.iSnapUpCategoryFragment = iSnapUpCategoryFragment;
        init();
    }

    private void init() {
        ptr = iSnapUpCategoryFragment.getPtrStylelFrameLayout();
        snapUpRecyclerView = iSnapUpCategoryFragment.getTRecyclerView();
        snapUpType = iSnapUpCategoryFragment.getSnapUpType();
        ptr.setPtrHandler(this);//设置下下刷新回调
        context = iSnapUpCategoryFragment.getContext();
        GridLayoutManager layoutManager = new GridLayoutManager(context, 1);//用LineLayoutManger不能全屏
        snapUpRecyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new SnapUpRecyclerViewAdapter((Activity) context);
        snapUpRecyclerView.setHasFixedSize(true);
        snapUpRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .color(context.getResources().getColor(R.color.app_divider_line_bg_cc))
                .size(1)
                .build());//分割线
        snapUpRecyclerView.setAdapter(recyclerViewAdapter);
    }

    /**
     * 请求网络的代码
     */
    public void request(boolean refreshFlag) {
        if (refreshFlag)
            iSnapUpCategoryFragment.showLoading();
        String url = context.getString(R.string.serverbaseurl) + "android/panicBuy/catList.html";
        final StringRequest snapUpRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        List<SnapUpPackBean> SnapUpPackBeanList = gson.fromJson(result, new TypeToken<List<SnapUpPackBean>>() {
                        }.getType());//外层
                        List<SnapUpBean> snapUpBeanList = new ArrayList<>();
                        for (SnapUpPackBean snapUpPackBean : SnapUpPackBeanList) {
                            for (SnapUpBean snapUpBean : snapUpPackBean.getList()) {
                                if (snapUpType.equals(SnapUpCategoryFragment.SNAPUP_NOW)) {
                                    if (isBegin(snapUpBean))
                                        snapUpBeanList.add(snapUpBean);
                                } else if (snapUpType.equals(SnapUpCategoryFragment.SNAPUP_LATER)){
                                    if (!isBegin(snapUpBean))
                                        snapUpBeanList.add(snapUpBean);
                                }
                            }
                        }
                        refreshData(snapUpBeanList);  //填充数据
                        //判断是否为空
                        if ((snapUpBeanList == null || snapUpBeanList.size() == 0) && recyclerViewAdapter.getItemCount() == 0) {
                            iSnapUpCategoryFragment.showEmpty();
                        } else {
                            iSnapUpCategoryFragment.showSuccessful();
                        }

                    } catch (IllegalArgumentException e) {
                        LogUtils.e("更多抢购包装异常");
                    }
                }
                //如果启用下拉就让他完成
                if (ptr.isRefreshing()) {
                    ptr.refreshComplete();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //如果启用下拉就让他完成
                if (ptr.isRefreshing()) {
                    ptr.refreshComplete();
                }
                iSnapUpCategoryFragment.showError();
            }
        });
        snapUpRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        PulamsiApplication.requestQueue.add(snapUpRequest);

    }

    /**
     * 判断是否开始，true为已经开始
     *
     * @param snapUpBean
     * @return
     */
    private boolean isBegin(SnapUpBean snapUpBean) {
        Long nowTime = System.currentTimeMillis();  //获取当前时间戳
        Date nowDate = new Date(Long.parseLong(String.valueOf(nowTime)));//当前时间转为Date对象
        Date beginTime = new Date(Long.parseLong(String.valueOf(snapUpBean.getPanicBuyBeginTimeLong())));//开始时间转为Date对象
        if (nowDate.after(beginTime)) {//现在时间大于开始时间，已开始
            return true;
        }
        return false;
    }

    //填充数据
    private void refreshData(List<SnapUpBean> snapUpBeanList) {
        recyclerViewAdapter.clearDataList();
        recyclerViewAdapter.addDataList(snapUpBeanList);
        recyclerViewAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, snapUpRecyclerView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        request(PULL_REFRESH);
    }
}
