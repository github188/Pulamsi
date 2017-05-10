package com.pulamsi.myinfo.slotmachineManage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

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
import com.pulamsi.myinfo.slotmachineManage.adapter.SlotmachinelistAdapter;
import com.pulamsi.myinfo.slotmachineManage.entity.VenderBean;
import com.pulamsi.myinfo.slotmachineManage.view.ICheckBoxHelper;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.BlankLayout;
import com.pulamsi.views.bottomBar.BottomColumnBar;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * 售货机列表界面
 */
public class SlotmachineListActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 售货机列表
     */
    private TRecyclerView slotmachineManageTRecyclerView;

    /**
     * 售货机列表适配器
     */
    private SlotmachinelistAdapter slotmachinelistAdapter;

    /**
     *
     */
    private ProgressWheel progressWheel;

    private ArrayList<VenderBean> venderBeans;

    /**
     * 空值显示
     */
    private BlankLayout mBlankLayout;
    /**
     * CheckBox帮助类
     */
    private ICheckBoxHelper iCheckBoxHelper;

    /**
     * Box事件条
     */
    public BottomColumnBar bottomColumnBar;

    /**
     * 是否显示
     */
    private boolean isShow;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slotmachine_manage_list);
        initData();
        initUI();
    }

    private void initUI() {
        setHeaderTitle(R.string.slotmachine_manage_list_title);
        bottomColumnBar = (BottomColumnBar) findViewById(R.id.slotmachine_manage_list_bottom_columnbar);
        bottomColumnBar.setOnClickListener(this);
        slotmachineManageTRecyclerView = (TRecyclerView) findViewById(R.id.slotmachine_manage_list_trecyclerview);
        progressWheel = (ProgressWheel) findViewById(R.id.pw_slotmachine_manage_list);
        slotmachinelistAdapter = new SlotmachinelistAdapter(this);
        setICheckBoxHelper(slotmachinelistAdapter);
        mBlankLayout = (BlankLayout) findViewById(R.id.blank_layout);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        slotmachineManageTRecyclerView.setLayoutManager(layoutManager);
        slotmachineManageTRecyclerView.setAdapter(slotmachinelistAdapter);
        slotmachineManageTRecyclerView.setHasFixedSize(true);
        slotmachineManageTRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                if (!isShow){//显示的时候屏蔽之前的点击事件
                    Intent goodsRoad = new Intent(SlotmachineListActivity.this, GoodsRoadListActivity.class);
                    goodsRoad.putExtra("searchMachineid", venderBeans.get(position).getId());
                    startActivity(goodsRoad);
                }
            }
        });
        slotmachineManageTRecyclerView.setOnItemLongClickListener(new TRecyclerView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(TRecyclerView parent, View view, int position, long id) {
                iCheckBoxHelper.showCheckBox();
                return true;
            }
        });
    }

    /**
     * 网络请求售货机列表数据
     */
    private void initData() {
        String getVenderList = getString(R.string.serverbaseurl)
                + getString(R.string.getVenderListurl) + "?mid="
                + Constants.userId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                getVenderList, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        venderBeans = gson.fromJson(result, new TypeToken<List<VenderBean>>() {
                        }.getType());
                        if (null != venderBeans && venderBeans.size() != 0) {
                            initListData(venderBeans);
                            hideBlankLayout();
                        } else {
                            showBlankLayout();
                        }
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "售货机列表数据装配出错");
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
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
        slotmachinelistAdapter.addDataList(data);
        slotmachinelistAdapter.notifyItemRangeInserted(0, data.size());
        slotmachineManageTRecyclerView.setVisibility(View.VISIBLE);
    }


    /**
     * 设置为空和隐藏
     **/
    public void hideBlankLayout() {
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setVisibility(View.INVISIBLE);
        slotmachineManageTRecyclerView.setVisibility(View.VISIBLE);
        progressWheel.setVisibility(View.GONE);
    }

    public void showBlankLayout() {
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setBlankLayoutInfo(R.drawable.ic_space_order, R.string.slotmachine_list_empty);
        mBlankLayout.setVisibility(View.VISIBLE);
        slotmachineManageTRecyclerView.setVisibility(View.INVISIBLE);
        progressWheel.setVisibility(View.GONE);
    }

    private void setICheckBoxHelper(ICheckBoxHelper iCheckBoxHelper) {
        this.iCheckBoxHelper = iCheckBoxHelper;
    }

    /**
     * 显示多选框架
     */
    public void showCheckBox() {
        isShow = true;
        setLeftText("取消");
        setRightText(getString(R.string.slotmachine_check_all));
        setHeaderTitle(getString(R.string.select_entry));
        if (bottomColumnBar.getVisibility() == View.GONE) {
            bottomColumnBar.setVisibility(View.VISIBLE);
            bottomColumnBar.startAnimation(AnimationUtils.loadAnimation(SlotmachineListActivity.this, R.anim.fade_in));
        }
        getLeftTetxtView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCheckBoxHelper.hideCheckBox();
            }
        });

        getRightTetxtView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRightTetxtView().getText().toString().equals(getString(R.string.slotmachine_check_all))) {
                    iCheckBoxHelper.checkAll();
                } else {
                    iCheckBoxHelper.unCheckAll();
                }
            }
        });

    }
    /**
     * 隐藏多选框架
     */
    public void hideCheckBox() {
        isShow = false;
        setHeaderTitle(R.string.slotmachine_manage_list_title);
        setLeftImage(R.drawable.ic_back);
        getRightTetxtView().setVisibility(View.GONE);
        if (bottomColumnBar.getVisibility() == View.VISIBLE) {
            bottomColumnBar.setVisibility(View.GONE);
            bottomColumnBar.startAnimation(AnimationUtils.loadAnimation(SlotmachineListActivity.this, R.anim.fade_out));
        }
    }


    /**
     * 拦截返回键，隐藏掉正在show的多选菜单
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            processExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void processExit() {
        if (isShow) {
            iCheckBoxHelper.hideCheckBox();
            isShow = false;
        } else {
            this.finish();
        }
    }

    /**
     * 设置顶部文本信息
     *
     * @param text
     */
    public void setTopBarTitle(String text) {
        setRightText(text);
    }
    /**
     * 设置顶部文本信息
     *
     * @param text
     */
    public void setHeaderText(String text) {
        setHeaderTitle(text);
    }


    @Override
    public boolean toggleOverridePendingTransition() {
        return true;
    }

    @Override
    public TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.slotmachine_manage_list_distribution:
                ToastUtil.showToast("点击事件");
                break;
        }
    }
}
