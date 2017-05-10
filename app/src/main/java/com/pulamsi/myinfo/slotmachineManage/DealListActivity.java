package com.pulamsi.myinfo.slotmachineManage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.android.volley.VolleyError;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseListview.CommonListLoadMoreHandler;
import com.pulamsi.myinfo.slotmachineManage.adapter.DealListAdapter;
import com.pulamsi.myinfo.slotmachineManage.entity.TradeBean;
import com.pulamsi.views.BlankLayout;
import com.pulamsi.views.PtrStylelFrameLayout;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 交易查询列表界面
 */
public class DealListActivity extends BaseActivity {
  /**
   * 交易查询列表
   */
  private TRecyclerView dealTRecyclerView;

  /**
   * 交易查询适配器
   */
  private DealListAdapter deallistAdapter;

  /**
   *
   */
  private ProgressWheel progressWheel;

  private ArrayList<TradeBean> tradeBeans;
  /**
   * 为空的时候显示布局
   */
  private BlankLayout mBlankLayout;

  /**
   * 下拉刷新控件
   */
  private PtrStylelFrameLayout ptrFrameLayout;

  /**
   * 包装类，里面有下拉刷新和加载更多操作
   */
  private DealListWrapperGet dealListWrapperGet;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.slotmachine_manage_deallist);
    initUI();
  }

  private void initUI() {
    setHeaderTitle(R.string.slotmachine_manage_deallist_title);

    dealTRecyclerView = (TRecyclerView) findViewById(R.id.slotmachine_manage_deallist_trecyclerview);
    mBlankLayout = (BlankLayout)findViewById(R.id.blank_layout);
    ptrFrameLayout = (PtrStylelFrameLayout) findViewById(R.id.slotmachine_manage_deallist_ptrframe);
    progressWheel = (ProgressWheel)findViewById(R.id.slotmachine_manage_deallist_pw);
    deallistAdapter = new DealListAdapter(this);
    dealListWrapperGet = new DealListWrapperGet(this);
    CommonListLoadMoreHandler loadMoreHandler = new CommonListLoadMoreHandler();
    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    dealListWrapperGet.setListAdapter(deallistAdapter).setPtrStylelFrameLayout(ptrFrameLayout).setProgressWheel(progressWheel).setListView(dealTRecyclerView).setLayoutManager(layoutManager)
            .setLoadMoreHandler(loadMoreHandler).init();
    dealTRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
      @Override
      public void onItemClick(TRecyclerView parent, View view, int position, long id) {
        //点击事件
        Intent intent = new Intent(DealListActivity.this,TradeDetailActivity.class);
        intent.putExtra("tradeBean",deallistAdapter.getItem(position));
        startActivity(intent);
      }
    });
    dealListWrapperGet.setRequestListener(new DealListWrapperGet.RequestListener() {
      @Override
      public void onRequestSuccess(List data) {
        if ((data == null || data.size() == 0) && deallistAdapter.getItemCount() == 0) {
          //显示订单列表为空提示
          showBlankLayout();
        } else {
          hideBlankLayout();
        }
        ptrFrameLayout.refreshComplete();
      }

      @Override
      public void onRequestError(VolleyError volleyError) {

      }
    });

    HashMap<String, String> map = new HashMap<String, String>();
    map.put("pageNumber", "1");
    String showOrderListurlPath = getString(R.string.serverbaseurl) + getString(R.string.Tradelist);
    dealListWrapperGet.startQuery(showOrderListurlPath, map);
  }



  /** 设置为空和隐藏 **/
  public void hideBlankLayout() {
    mBlankLayout.hideBlankBtn();
    mBlankLayout.setVisibility(View.INVISIBLE);
    dealTRecyclerView.setVisibility(View.VISIBLE);
    progressWheel.setVisibility(View.GONE);
  }

  public void showBlankLayout() {
    mBlankLayout.hideBlankBtn();
    mBlankLayout.setBlankLayoutInfo(R.drawable.ic_space_order, R.string.slotmachine_manage_deallist_empty);
    mBlankLayout.setVisibility(View.VISIBLE);
    dealTRecyclerView.setVisibility(View.INVISIBLE);
    progressWheel.setVisibility(View.GONE);
  }

}
