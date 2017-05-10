package com.pulamsi.search;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseListview.CommonListLoadMoreHandler;
import com.pulamsi.home.adapter.HomeCateGoodListAdapter;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.GoodsHelper;
import com.pulamsi.views.BlankLayout;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索列表页
 *
 * @author WilliamChik on 15/7/30.
 */
public class SearchListActivity extends BaseActivity implements View.OnClickListener {

  /**
   * 包括网络请求和下一页操作
   */
  private SearchGoodsWrapperGet searchGoodsWrapperGet;

  /** 回顶按钮 */
  private ImageView mBackTopBtn;

  /** 搜索列表 */
  private TRecyclerView mSearchList;

  /** 关键字 */
  private String keyword;

  private HomeCateGoodListAdapter listAdapter;
  /**
   * 空值显示view
   */
  private BlankLayout mBlankLayout;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    BaseAppManager.getInstance().addActivity(this);
    Bundle bundle = getIntent().getExtras();
    DialogUtil.showLoadingDialog(SearchListActivity.this,"正在加载中");
    keyword = bundle.getString("keyword");
    // UI渲染，需要放在数据请求之后
    setContentView(R.layout.search_list_activity);
    initUI();
    startDataRequest();
  }

  private void initUI() {
    initHeader();
    mBackTopBtn = (ImageView) findViewById(R.id.iv_search_list_back_top);
    mBackTopBtn.setOnClickListener(this);
    mBlankLayout = (BlankLayout) findViewById(R.id.blank_layout);
    mSearchList = (TRecyclerView) findViewById(R.id.rv_search_list_main_list);
    // 数据适配器
    listAdapter = new HomeCateGoodListAdapter(this);
    searchGoodsWrapperGet = new SearchGoodsWrapperGet(this);
    CommonListLoadMoreHandler likeloadMoreHandler = new CommonListLoadMoreHandler();
    StaggeredGridLayoutManager likelayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    searchGoodsWrapperGet.setListAdapter(listAdapter).setListView(mSearchList).setLayoutManager(likelayoutManager)
            .setLoadMoreHandler(likeloadMoreHandler).init();
    searchGoodsWrapperGet.setRequestListener(new SearchGoodsWrapperGet.RequestListener() {
      @Override
      public void onRequestSuccess(List data) {
        if ((data == null || data.size() == 0) && listAdapter.getItemCount() == 0) {
          //显示订单列表为空提示
          showBlankLayout();
        } else {
          hideBlankLayout();
        }
        DialogUtil.hideLoadingDialog();
      }

      @Override
      public void onRequestError(VolleyError volleyError) {

      }
    });
    mSearchList.setHasFixedSize(true);
    mSearchList.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
      @Override
      public void onItemClick(TRecyclerView parent, View view, int position, long id) {
        GoodsHelper.gotoDetail(listAdapter.getItem(position).getId(), SearchListActivity.this);
      }
    });
  }

  private void initHeader() {
    mTitleHeaderBar.setOnClickListener(this);
    View view = mTitleHeaderBar.setCustomizedCenterView(R.layout.category_search_header_area);
    TextView TopText = (TextView) view.findViewById(R.id.search_header_text);
    TopText.setText(keyword);
    mTitleHeaderBar.setLeftContainerWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
    mTitleHeaderBar.setRightContainerWidth(getResources().getDimensionPixelSize(R.dimen.app_main_right_margin));
  }

  /**
   * 在子线程中进行数据请求，完成后回到主线程实现UI更新，而在此前UI线程正在执行界面渲染的操作，
   * 由于UI线程线程非安全，所以会等到界面渲染完成才会去执行UI更新的操作
   */
  private void startDataRequest() {
    Map<String, String> bizParams = new HashMap<>();
    bizParams.put("pageNumber", "1");
    bizParams.put("productName", keyword);
    searchGoodsWrapperGet.startQuery(PulamsiApplication.context.getString(R.string.serverbaseurl) + PulamsiApplication.context.getString(R.string.productSearch), bizParams);
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }


  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    // 设置回顶按钮的边距，使得回顶按钮刚好和导航栏的用户中心图标中轴对齐
    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mBackTopBtn.getLayoutParams();
    int marginBottom = PulamsiApplication.context.getResources().getDimensionPixelSize(R.dimen.app_main_right_margin);
    lp.setMargins(0, 0, marginBottom, marginBottom);

    super.onWindowFocusChanged(hasFocus);
  }

  @Override
  public void onClick(View v) {
    int viewId = v.getId();
    if (viewId == R.id.iv_search_list_back_top) {
      if (mSearchList == null) {
        return;
      }
      mSearchList.scrollToPosition(0);
    } else if (viewId == R.id.ly_header_bar_title_wrap) {
      returnBack();
    }
  }

  /** 设置为空和隐藏 **/
  public void hideBlankLayout() {
    mBlankLayout.setVisibility(View.INVISIBLE);
  }

  public void showBlankLayout() {
    mBlankLayout.setBlankLayoutInfo(R.drawable.ic_my_address_empty, R.string.search_door_hot_search_empty);
    mBlankLayout.setVisibility(View.VISIBLE);
  }

}
