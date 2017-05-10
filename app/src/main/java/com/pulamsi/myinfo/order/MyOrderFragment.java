package com.pulamsi.myinfo.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseLazyFragment;
import com.pulamsi.base.baseListview.CommonListLoadMoreHandler;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.base.otto.BusProvider;
import com.pulamsi.base.otto.MessageEvent;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.order.adapter.OrderListAdapter;
import com.pulamsi.myinfo.order.entity.Order;
import com.pulamsi.views.LoadView.LoadViewHelper;
import com.pulamsi.views.PtrStylelFrameLayout;
import com.squareup.otto.Subscribe;
import com.taobao.uikit.feature.view.TRecyclerView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.HashMap;
import java.util.List;

import tr.xip.errorview.ErrorView;

/**
 * 我的订单 Fragment 页面，全部、待付款、代发货、待收货四个 tab 的订单 Fragment 页面共同
 */
public class MyOrderFragment extends BaseLazyFragment implements ErrorView.RetryListener {
    public static final String RETURN_REFRESH = "RETURN_REFRESH";
    /**
     * 订单列表
     */
    private TRecyclerView orderListTRecyclerView;

    /**
     * 订单列表适配器
     */
    private OrderListAdapter orderlistAdapter;

    /**
     * 下拉刷新控件
     */
    private PtrStylelFrameLayout ptrStylelFrameLayout;

    /**
     * 请求的类别
     */
    private String orderType;
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
    /**
     * 我的订单，包括网络请求和下一页操作
     */
    private MyOrderListWrapperPost orderCommonListViewWrapperPost;

    public static final String TYPE_ALL = "0";//全部订单
    public static final String TYPE_NOT_PAY = "1";//待支付
    public static final String TYPE_NOT_SHIP = "3";//待收货
    public static final String TYPE_NOT_RECEIVE = "2";//待发货
    public static final String TYPE_NOT_EVALUATION = "4";//待评价


    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    View contentLayout;
    /**
     * 动态加载失败，空，成功的状态
     */
    LoadViewHelper loadHelper;


    //静态方法构造带参数Fragment
    public static MyOrderFragment newInstance(String type) {
        MyOrderFragment instance = new MyOrderFragment();
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
        mRootView = inflater.inflate(R.layout.orderlist_activity, null);
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
        contentLayout = mRootView.findViewById(R.id.content_layout);
        loadHelper = new LoadViewHelper(contentLayout);
        isPrepared = true;
        orderType = bundle.getString("type");
        initUI();
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        request();
    }

    private void initUI() {
        orderListTRecyclerView = (TRecyclerView) mRootView.findViewById(R.id.orderlist_trecyclerview);
        ptrStylelFrameLayout = (PtrStylelFrameLayout) mRootView.findViewById(R.id.ptr_home_material_style_ptr_frame);

        orderlistAdapter = new OrderListAdapter(mActivity, this);

        orderCommonListViewWrapperPost = new MyOrderListWrapperPost(mActivity);

        CommonListLoadMoreHandler loadMoreHandler = new CommonListLoadMoreHandler();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        orderCommonListViewWrapperPost
                .setListAdapter(orderlistAdapter)
                .setPtrStylelFrameLayout(ptrStylelFrameLayout)
                .setLoadViewHelper(loadHelper)
                .setListView(orderListTRecyclerView)
                .setLayoutManager(layoutManager)
                .setLoadMoreHandler(loadMoreHandler)
                .init();

        orderListTRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
                .color(getResources().getColor(R.color.background))
                .size(20)
                .build());//分割线

        orderListTRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                Order order = orderlistAdapter.getItem(position);
                HaiLog.d("order", order.getId());
                Intent orderDetail = new Intent(mActivity, OrderDetailActivity.class);
                orderDetail.putExtra("order", order);
                mActivity.startActivityForResult(orderDetail, Integer.parseInt(orderType));
            }
        });

        orderCommonListViewWrapperPost.setRequestListener(new MyOrderListWrapperPost.RequestListener() {
            @Override
            public void onRequestSuccess(List data) {
                if ((data == null || data.size() == 0) && orderlistAdapter.getItemCount() == 0) {
                    //显示订单列表为空提示
                    showBlankLayout();
                } else {
//                    hideBlankLayout();不需要显示会导致加载未完成进度条消失
                }
                ptrStylelFrameLayout.refreshComplete();
            }

            @Override
            public void onRequestError(VolleyError volleyError) {
                loadHelper.showError(MyOrderFragment.this);
            }
        });
    }

    private void request() {
        loadHelper.showLoading();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mid", Constants.userId);
        map.put("page", "1");
        map.put("pageSize", "10");//每页请求十条数据
        map.put("states", orderType);
        String showOrderListurlPath = getString(R.string.serverbaseurl) + getString(R.string.showOrderList);
        orderCommonListViewWrapperPost.startQuery(showOrderListurlPath, map);
    }

    /**
     * 刷新列表
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

    /**
     * 设置为空和隐藏
     **/
    public void hideBlankLayout() {
        loadHelper.restore();
    }

    public void showBlankLayout() {
        loadHelper.showEmpty(mActivity.getResources().getString(R.string.my_order_empty));
    }

    /**
     * 加载失败布局的重新加载按钮
     */
    @Override
    public void onRetry() {
        request();
    }


    /**
     * 监听otto事件，返回刷新数据
     *
     * @param event
     */
    @Subscribe
    public void doReturnRefresh(MessageEvent event) {
        if (event.getTag().equals(RETURN_REFRESH))
            request();
    }


    @Override
    public void onResume() {
        BusProvider.getInstance().register(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        BusProvider.getInstance().unregister(this);
        super.onPause();
    }
}
