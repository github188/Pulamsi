package com.pulamsi.myinfo.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.pulamsi.R;
import com.pulamsi.base.baseListview.CommonListLoadMoreHandler;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.wallet.adapter.BalanceDetailListAdapter;
import com.pulamsi.views.BlankLayout;
import com.pulamsi.views.PtrStylelFrameLayout;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.HashMap;
import java.util.List;

/**
 * 交易明细 Fragment 页面，全部、平台、佣金、售卖四个 tab 的订单 Fragment 页面共同
 *
 */
public class BalanceDetailFragment extends Fragment  {


  /**
   * 列表
   */
  private TRecyclerView balanceDetailTRecyclerView;

  /**
   * 列表适配器
   */
  private BalanceDetailListAdapter balanceDetailListAdapter;

  /**
   * 加载进度条
   */
  private ProgressWheel progressWheel;
  /**
   * 下拉刷新控件
   */
  private PtrStylelFrameLayout ptrStylelFrameLayout;

  /**
   * 请求的类别
   */
  private String balanceDetailType;
  /**
   * 主布局
   */
  private View mRootView;
  /**
   * activity
   */
  private Activity mActivity;

  /**
   * 为空的时候显示布局
   */
  private BlankLayout mBlankLayout;
  /**
   * 包括网络请求和下一页操作
   */
  private BalanceDetailListWrapperPost balanceDetailListWrapperPost;

  public static final String TYPE_ALL = "-1";//全部
  public static final String TYPE_TERRACE = "0";//平台
  public static final String TYPE_SLOTMACHINE = "1";//售货机
  public static final String TYPE_STORE = "2";//佣金

  //静态方法构造带参数Fragment
  public static BalanceDetailFragment newInstance(String type) {
    BalanceDetailFragment instance = new BalanceDetailFragment();
    Bundle bundle = new Bundle();
    bundle.putString("type", type);
    instance.setArguments(bundle);
    return instance;
  }

  @Override
  public void onAttach(Activity activity) {
    mActivity = activity;

    super.onAttach(activity);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mRootView = inflater.inflate(R.layout.balancedetail_fragment, null);
    return mRootView;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    if (mRootView == null) {
      return;
    }

    Bundle bundle = getArguments();
    if (bundle == null) {
      return;
    }
    balanceDetailType = bundle.getString("type");
    initUI();

  }


  private void initUI() {
    balanceDetailTRecyclerView = (TRecyclerView) mRootView.findViewById(R.id.balancedetail_activity_trecyclerview);
    mBlankLayout = (BlankLayout) mRootView.findViewById(R.id.blank_layout);
    ptrStylelFrameLayout = (PtrStylelFrameLayout) mRootView.findViewById(R.id.balancedetail_activity_ptr_frame);
    progressWheel = (ProgressWheel) mRootView.findViewById(R.id.balancedetail_activity_pw);
    balanceDetailListAdapter = new BalanceDetailListAdapter(mActivity);
    balanceDetailListWrapperPost = new BalanceDetailListWrapperPost(mActivity);
    CommonListLoadMoreHandler loadMoreHandler = new CommonListLoadMoreHandler();
    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    balanceDetailListWrapperPost.setListAdapter(balanceDetailListAdapter).setPtrStylelFrameLayout(ptrStylelFrameLayout).setProgressWheel(progressWheel).setListView(balanceDetailTRecyclerView).setLayoutManager(layoutManager)
            .setLoadMoreHandler(loadMoreHandler).init();
    balanceDetailTRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
      @Override
      public void onItemClick(TRecyclerView parent, View view, int position, long id) {

      }
    });
    balanceDetailListWrapperPost.setRequestListener(new BalanceDetailListWrapperPost.RequestListener() {
      @Override
      public void onRequestSuccess(List data) {
        if ((data == null || data.size() == 0) && balanceDetailListAdapter.getItemCount() == 0) {
          //显示订单列表为空提示
          showBlankLayout();
        } else {
          hideBlankLayout();
        }
        ptrStylelFrameLayout.refreshComplete();
      }

      @Override
      public void onRequestError(VolleyError volleyError) {

      }
    });

    HashMap<String, String> map = new HashMap<String, String>();
    map.put("mid", Constants.userId);
    map.put("pageSize",  "20");
    map.put("transactionStatus", "-1");
    map.put("transactionCategory", balanceDetailType);
    map.put("page", "1");
    String showOrderListurlPath = getString(R.string.serverbaseurl) + getString(R.string.searchAccountTran);
    balanceDetailListWrapperPost.startQuery(showOrderListurlPath, map);

  }


  /** 设置为空和隐藏 **/
  public void hideBlankLayout() {
    mBlankLayout.hideBlankBtn();
    mBlankLayout.setVisibility(View.INVISIBLE);
    balanceDetailTRecyclerView.setVisibility(View.VISIBLE);
    progressWheel.setVisibility(View.GONE);
  }

  public void showBlankLayout() {
    mBlankLayout.hideBlankBtn();
    mBlankLayout.setBlankLayoutInfo(R.drawable.ic_space_order, R.string.my_info_wallet_empty);
    mBlankLayout.setVisibility(View.VISIBLE);
    balanceDetailTRecyclerView.setVisibility(View.INVISIBLE);
    progressWheel.setVisibility(View.GONE);
  }

}

