package com.pulamsi.slotmachine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.myinfo.slotmachineManage.entity.VenderBean;
import com.pulamsi.slotmachine.adapter.ListAdapter;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.BlankLayout;
import com.pulamsi.views.PtrStylelFrameLayout;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 前端售货机列表界面
 */
public class SlotmachineListFragment extends Fragment implements PtrHandler {

    /**
     * 列表
     */
    private TRecyclerView tRecyclerView;
    /**
     * 空值显示内容
     */
    private BlankLayout mBlankLayout;

    /**
     * 下拉刷新控件
     */
    private PtrStylelFrameLayout ptrStylelFrameLayout;

    /**
     * 售货机列表适配器
     */
    private ListAdapter listAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.slotmachine_list_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ptrStylelFrameLayout = (PtrStylelFrameLayout) view.findViewById(R.id.slotmachine_list_ptrFrame);
        ptrStylelFrameLayout.setPtrHandler(this);
        mBlankLayout = (BlankLayout) view.findViewById(R.id.blank_layout);
        tRecyclerView = (TRecyclerView) view.findViewById(R.id.slotmachine_list_trecyclerview);
        listAdapter = new ListAdapter(getActivity());
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        tRecyclerView.setLayoutManager(layoutManager);
        tRecyclerView.setAdapter(listAdapter);
        tRecyclerView.setHasFixedSize(true);
        tRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                //列表点击事件
            }
        });
        refreshList();
    }


    /**
     * 网络请求售货机列表数据
     */
    public void initData(String provinceId,String cityId ,String districtId) {
        String getVenderList = getString(R.string.serverbaseurl) + getString(R.string.getIndexVenderList)+"?provinceId="+provinceId+"&cityId="+cityId+"&districtId="+districtId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                getVenderList, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        List<VenderBean> venderBeans = gson.fromJson(result, new TypeToken<List<VenderBean>>() {
                        }.getType());
                        if (null != venderBeans && venderBeans.size() != 0) {
                            initListData(venderBeans);
                            hideBlankLayout();
                        } else {
                            showBlankLayout();
                        }
                        ptrStylelFrameLayout.refreshComplete();
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "售货机列表数据装配出错");
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                ptrStylelFrameLayout.refreshComplete();
                ToastUtil.showToast(R.string.notice_networkerror);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(stringRequest);
    }

    /**
     * 装配售货机菜单数据
     */
    public void initListData(List<VenderBean> data) {
        int formerItemCount = listAdapter.getItemCount();
        listAdapter.clearDataList();
        listAdapter.notifyItemRangeRemoved(0, formerItemCount);
        listAdapter.addDataList(data);
        listAdapter.notifyItemRangeInserted(0, data.size());
        tRecyclerView.setVisibility(View.VISIBLE);
    }


    /**
     * 设置为空和隐藏
     **/
    public void hideBlankLayout() {
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setVisibility(View.INVISIBLE);
        tRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showBlankLayout() {
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setBlankLayoutInfo(R.drawable.ic_space_order, R.string.slotmachine_list_slotmachine_empty);
        mBlankLayout.setVisibility(View.VISIBLE);
        tRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, tRecyclerView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        initData("","","");
    }

    /**
     * 刷新购物车列表
     */
    public void refreshList() {
        if (ptrStylelFrameLayout == null) {
            return;
        }
        ptrStylelFrameLayout.post(new Runnable() {
            @Override
            public void run() {
                ptrStylelFrameLayout.autoRefresh(true);
            }
        });
    }
}
