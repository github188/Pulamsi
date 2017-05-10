package com.pulamsi.evaluate;

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
import com.pulamsi.evaluate.adapter.EvaluateListAdapter;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.BlankLayout;
import com.pulamsi.views.PtrStylelFrameLayout;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.HashMap;
import java.util.List;

/**
 * 商品评价 Fragment 页面，全部、好评，中评，差评四个 tab 的订单 Fragment 页面共同
 */
public class EvaluateFragment extends Fragment {
    /**
     * 订单列表
     */
    private TRecyclerView evaluateTRecyclerView;

    /**
     * 列表适配器
     */
    private EvaluateListAdapter evaluateListAdapter;

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
    private String evaluateType;
    /**
     * 主布局
     */
    private View mRootView;
    /**
     * activity
     */
    private Activity mActivity;

    /**
     * 商品对象
     */
    private String productId;
    private Boolean isAngelProduct;
    /**
     * 为空的时候显示布局
     */
    private BlankLayout mBlankLayout;
    /**
     * 商品评论，包括网络请求和下一页操作
     */
    private EvaluateListWrapperGet evaluateListWrapperGet;

    public static final String TYPE_ALL = "0";//全部
    public static final String TYPE_BAD = "1";//差评
    public static final String TYPE_ZHONG = "3";//中评
    public static final String TYPE_GOOD = "5";//好评

    //静态方法构造带参数Fragment
    public static EvaluateFragment newInstance(String type, String productId,boolean isAngelProduct) {
        EvaluateFragment instance = new EvaluateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("productId", productId);
        bundle.putBoolean("isAngelProduct", isAngelProduct);
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
        mRootView = inflater.inflate(R.layout.evaluatelist_activity, null);
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
        evaluateType = bundle.getString("type");
        productId =  bundle.getString("productId");
        isAngelProduct = bundle.getBoolean("isAngelProduct");
        initUI();

    }


    private void initUI() {
        evaluateTRecyclerView = (TRecyclerView) mRootView.findViewById(R.id.evaluate_trecyclerview);
        mBlankLayout = (BlankLayout) mRootView.findViewById(R.id.blank_layout);
        ptrStylelFrameLayout = (PtrStylelFrameLayout) mRootView.findViewById(R.id.evaluate_ptr_frame);
        progressWheel = (ProgressWheel) mRootView.findViewById(R.id.evaluate_activity_pw);
        evaluateListAdapter = new EvaluateListAdapter(mActivity);
        evaluateListWrapperGet = new EvaluateListWrapperGet(mActivity);
        CommonListLoadMoreHandler loadMoreHandler = new CommonListLoadMoreHandler();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        evaluateListWrapperGet.setListAdapter(evaluateListAdapter).setPtrStylelFrameLayout(ptrStylelFrameLayout).setProgressWheel(progressWheel).setListView(evaluateTRecyclerView).setLayoutManager(layoutManager)
                .setLoadMoreHandler(loadMoreHandler).init();
        evaluateTRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {

            }
        });
        evaluateListWrapperGet.setRequestListener(new EvaluateListWrapperGet.RequestListener() {
            @Override
            public void onRequestSuccess(List data) {
                if ((data == null || data.size() == 0) && evaluateListAdapter.getItemCount() == 0) {
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
        map.put("pageNumber", "1");
        String url;
        if (isAngelProduct){//是否为天使
            url = getString(R.string.serverbaseurl) + getString(R.string.angelGoodsEvaluate)
                    + "?productId=" + productId + "&pageSize=20&productStars=" + evaluateType;
        }else {
            url = getString(R.string.serverbaseurl) + getString(R.string.getProductEstimateList)
                    + "?productId=" + productId + "&pageSize=20&productStars=" + evaluateType;
        }

        evaluateListWrapperGet.startQuery(url, map);
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
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setVisibility(View.INVISIBLE);
        evaluateTRecyclerView.setVisibility(View.VISIBLE);
        progressWheel.setVisibility(View.GONE);
    }

    public void showBlankLayout() {
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setBlankLayoutInfo(R.drawable.ic_space_order, R.string.good_detail_evaluate_empty);
        mBlankLayout.setVisibility(View.VISIBLE);
        evaluateTRecyclerView.setVisibility(View.INVISIBLE);
        progressWheel.setVisibility(View.GONE);
    }
}

