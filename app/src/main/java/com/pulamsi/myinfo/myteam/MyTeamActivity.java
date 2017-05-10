package com.pulamsi.myinfo.myteam;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

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
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.myteam.adapter.MyTeamListAdapter;
import com.pulamsi.myinfo.myteam.entity.TeamBean;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.BlankLayout;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的团队界面
 * Created by lanqiang on 15/12/1.
 */
public class MyTeamActivity extends BaseActivity {

    /**
     * 列表
     */
    private TRecyclerView myTeamTRecyclerView;
    /**
     *  加载进度条
     */
    private ProgressWheel progressWheel;

    /**
     * 列表适配器
     */
    private MyTeamListAdapter myTeamListAdapter;
    /**
     * 空值显示view
     */
    private BlankLayout mBlankLayout;
    /**
     * 记录对象集合
     */
    private ArrayList<TeamBean> teamBeans;

    /**
     * 页头
     */
    private LinearLayout top ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myteamlist_activity);
        initView();
        initData();
    }

    private void initData() {
        String memberList = getString(R.string.serverbaseurl) + getString(R.string.memberList) + "?mid=" + Constants.userId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, memberList, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        teamBeans = gson.fromJson(result, new TypeToken<List<TeamBean>>() {}.getType());
                        if (null != teamBeans && teamBeans.size() > 0){
                            hideBlankLayout();
                            updateAdapter(teamBeans);
                        }else {
                            showBlankLayout();
                        }
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "我的团队列表数据装配出错");
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                ToastUtil.showToast(R.string.notice_networkerror);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(stringRequest);
    }


    private void initView() {
        setHeaderTitle(R.string.myteam_title);
        myTeamTRecyclerView = (TRecyclerView) findViewById(R.id.my_info_myteam_trecyclerview);
        progressWheel = (ProgressWheel) findViewById(R.id.my_info_myteam_pw);
        mBlankLayout = (BlankLayout) findViewById(R.id.blank_layout);
        top = (LinearLayout) findViewById(R.id.my_info_myteam_top);
        myTeamListAdapter = new MyTeamListAdapter(this);
        StaggeredGridLayoutManager tuijianlayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        myTeamTRecyclerView.setLayoutManager(tuijianlayoutManager);
        myTeamTRecyclerView.setAdapter(myTeamListAdapter);
        myTeamTRecyclerView.setHasFixedSize(true);
        myTeamTRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {

            }
        });

    }


    /**
     * 装配数据
     */
    public void updateAdapter(List<TeamBean> data) {
        //  状态还原
        int formerItemCount = myTeamListAdapter.getItemCount();
        myTeamListAdapter.clearDataList();
        myTeamListAdapter.notifyItemRangeRemoved(0, formerItemCount);
        myTeamListAdapter.addDataList(data);
        myTeamListAdapter.notifyItemRangeInserted(0, data.size());
    }

    /** 设置为空和隐藏 **/
    public void hideBlankLayout() {
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setVisibility(View.INVISIBLE);
        myTeamTRecyclerView.setVisibility(View.VISIBLE);
        progressWheel.setVisibility(View.GONE);
        top.setVisibility(View.VISIBLE);
    }

    public void showBlankLayout() {
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setBlankLayoutInfo(R.drawable.ic_space_order, R.string.myteam_empty);
        mBlankLayout.setVisibility(View.VISIBLE);
        myTeamTRecyclerView.setVisibility(View.INVISIBLE);
        top.setVisibility(View.GONE);
        progressWheel.setVisibility(View.GONE);
    }
    @Override
    public boolean toggleOverridePendingTransition() {
        return true;
    }

    @Override
    public TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }
}
