package com.pulamsi.base.baseListview;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.otto.BusProvider;
import com.taobao.uikit.feature.view.TRecyclerView;

/**
 * 列表滚动加载更多处理类
 *
 * @author by dai on 15/8/14.
 */
public class CommonListLoadMoreHandler implements ILoadMoreHandler, View.OnClickListener {

  private String LOADING_MSG = PulamsiApplication.context.getResources().getString(R.string.loading_more_footer);
  private String LOAD_FINISH_MSG = PulamsiApplication.context.getResources().getString(R.string.load_finish_footer);

  /** 提示跟布局 */
  private View mFooterRootView;

  /** 提示文本视图 */
  private TextView mFooterStateView;

  /** 是否正在加载更多 */
  private boolean mIsLoadingMore;

  /**
   * looding图
   */
  private ProgressWheel loading_pw;


  public CommonListLoadMoreHandler() {
    mFooterRootView = LayoutInflater.from(PulamsiApplication.context).inflate(R.layout.common_list_pager_footer, null);
    mFooterRootView.setOnClickListener(this);
    mFooterStateView = (TextView) mFooterRootView.findViewById(R.id.tv_common_list_pager_footer);
    loading_pw = (ProgressWheel) mFooterRootView.findViewById(R.id.loading_pw);
  }

  /**
   * 删除脚部
   */
  public void removeFooter(TRecyclerView listView) {
    listView.removeFooterView(mFooterRootView);
  }

  /** 是否正在加载更多 */
  public boolean isLoadingMore() {
    return mIsLoadingMore;
  }

  @Override
  public void onLoadStart() {//开始加载
    mIsLoadingMore = true;
  }

  @Override
  public void onLoadFinish(TRecyclerView listView) {
    mFooterStateView.setText(LOAD_FINISH_MSG);
    listView.removeFooterView(mFooterRootView);
    listView.addFooterView(mFooterRootView);
    mIsLoadingMore = false;
    loading_pw.setVisibility(View.GONE);
  }

  @Override
  public void onWaitToLoadMore(TRecyclerView listView) {//等待加载，预先出来
    mFooterStateView.setText(LOADING_MSG);
    listView.removeFooterView(mFooterRootView);
    listView.addFooterView(mFooterRootView);
    mIsLoadingMore = false;
    loading_pw.setVisibility(View.VISIBLE);
  }

  public void setLoadingMsg(int Rid){
    LOADING_MSG = PulamsiApplication.context.getString(Rid);
  }
  public void setLoadFinishMsg(int Rid){
    LOAD_FINISH_MSG = PulamsiApplication.context.getString(Rid);
  }

  @Override
  public void onLoadError(TRecyclerView listView, int errorCode, String errorMessage) {

  }

  @Override
  public void onClick(View v) {
    if (LOAD_FINISH_MSG.equals(mFooterStateView.getText())) {
      // 通知CommonListViewWrapper进行翻页操作
      // 注意需要将当前LoadMoreHandler对象的引用传给evenbus，
      // 然后在CommonListViewWrapper中通过判断LoadMoreHandler对象引用来区分通知的是哪一个CommonListViewWrapper。
      // 如果不做区分，所有订阅LoadMoreEvent对象的CommonListViewWrapper都会做翻页操作。
      BusProvider.getInstance().post(new LoadMoreEvent(this));
    }
  }

  public class LoadMoreEvent {

    public CommonListLoadMoreHandler mLoadMoreHandler;

    public LoadMoreEvent(CommonListLoadMoreHandler loadMoreHandler) {
      mLoadMoreHandler = loadMoreHandler;
    }
  }
}
