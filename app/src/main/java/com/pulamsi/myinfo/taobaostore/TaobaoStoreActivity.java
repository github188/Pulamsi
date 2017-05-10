package com.pulamsi.myinfo.taobaostore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseListview.CommonListLoadMoreHandler;
import com.pulamsi.constant.Constants;
import com.pulamsi.home.adapter.HomeDianzhuListAdapter;
import com.pulamsi.views.BlankLayout;
import com.pulamsi.views.PtrStylelFrameLayout;
import com.pulamsi.webview.CommonWebViewActivity;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * 淘宝商家界面
 */
public class TaobaoStoreActivity extends BaseActivity implements PtrHandler {

  /**
   * 包括网络请求和下一页操作
   */
  private TaobaoStoreWrapperGet taobaoStoreWrapperGet;

  /** 回顶按钮 */
  private ImageView mBackTopBtn;

  /** 搜索列表 */
  private TRecyclerView taobaoTRecyclerView;


  private HomeDianzhuListAdapter dianzhuListAdapter;
  /**
   * 空值显示view
   */
  private BlankLayout mBlankLayout;

  /**
   * 加载进度条
   */
  private ProgressWheel progressWheel;

  /**
   * 下拉刷新控件
   */
  private PtrStylelFrameLayout tb_ptrStylelFrameLayout;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.taobaostore_activity);
    initUI();
    startDataRequest();
  }

  private void initUI() {
    setHeaderTitle(R.string.my_info_taobaostore_title);
    tb_ptrStylelFrameLayout = (PtrStylelFrameLayout) findViewById(R.id.tb_ptrMaterialFrameLayout);
    tb_ptrStylelFrameLayout.setPtrHandler(this);
    mBackTopBtn = (ImageView) findViewById(R.id.my_info_taobaostore_back_top);
    taobaoTRecyclerView = (TRecyclerView) findViewById(R.id.my_info_taobaostore_list);
    mBlankLayout = (BlankLayout) findViewById(R.id.blank_layout);
    progressWheel = (ProgressWheel) findViewById(R.id.my_info_taobaostore_pw);
    mBackTopBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (taobaoTRecyclerView == null) {
          return;
        }
        taobaoTRecyclerView.scrollToPosition(0);
      }
    });


    dianzhuListAdapter = new HomeDianzhuListAdapter(this);
    taobaoStoreWrapperGet = new TaobaoStoreWrapperGet(this);
    CommonListLoadMoreHandler loadMoreHandler = new CommonListLoadMoreHandler();
    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    taobaoStoreWrapperGet.setListAdapter(dianzhuListAdapter).setListView(taobaoTRecyclerView).setLayoutManager(layoutManager)
            .setLoadMoreHandler(loadMoreHandler).setProgressWheel(progressWheel).init();
    taobaoStoreWrapperGet.setRequestListener(new TaobaoStoreWrapperGet.RequestListener() {
      @Override
      public void onRequestSuccess(List data) {
        if ((data == null || data.size() == 0) && dianzhuListAdapter.getItemCount() == 0) {
          //显示为空提示
          showBlankLayout();
        } else {
          hideBlankLayout();
        }
      }

      @Override
      public void onRequestError(VolleyError volleyError) {

      }
    });
    taobaoTRecyclerView.setHasFixedSize(true);
    taobaoTRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
      @Override
      public void onItemClick(TRecyclerView parent, View view, int position, long id) {
        Intent taobao = new Intent(TaobaoStoreActivity.this, CommonWebViewActivity.class);
        taobao.putExtra(Constants.WEBVIEW_LOAD_URL,dianzhuListAdapter.getItem(position).getUrl());
        taobao.putExtra(Constants.WEBVIEW_TITLE,"店铺详情");
        TaobaoStoreActivity.this.startActivity(taobao);
      }
    });
  }

  /**
   * 在子线程中进行数据请求，完成后回到主线程实现UI更新，而在此前UI线程正在执行界面渲染的操作，
   * 由于UI线程线程非安全，所以会等到界面渲染完成才会去执行UI更新的操作
   */
  private void startDataRequest() {
    Map<String, String> bizParams = new HashMap<>();
    bizParams.put("pageNumber", "1");
    taobaoStoreWrapperGet.setPtrStylelFrameLayout(tb_ptrStylelFrameLayout);
    taobaoStoreWrapperGet.startQuery(PulamsiApplication.context.getString(R.string.serverbaseurl) + PulamsiApplication.context.getString(R.string.channelMgrImplurl), bizParams);
  }

  /** 设置为空和隐藏 **/
  public void hideBlankLayout() {
    mBlankLayout.hideBlankBtn();
    mBlankLayout.setVisibility(View.INVISIBLE);
    mBackTopBtn.setVisibility(View.VISIBLE);
  }

  public void showBlankLayout() {
    mBlankLayout.hideBlankBtn();
    mBlankLayout.setBlankLayoutInfo(R.drawable.ic_my_address_empty, R.string.search_door_hot_search_empty);
    mBlankLayout.setVisibility(View.VISIBLE);
    mBackTopBtn.setVisibility(View.GONE);
  }

  @Override
  public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view1) {
    return PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, taobaoTRecyclerView, view1);
  }

  @Override
  public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
    startDataRequest();
  }

  @Override
  public TransitionMode getOverridePendingTransitionMode() {
    return TransitionMode.RIGHT;
  }

  @Override
  public boolean toggleOverridePendingTransition() {
    return true;
  }
}
