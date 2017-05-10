package com.pulamsi.integral;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseListview.CommonListLoadMoreHandler;
import com.pulamsi.constant.Constants;
import com.pulamsi.integral.adapter.IntegralDetailListAdapter;
import com.pulamsi.views.BlankLayout;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.HashMap;
import java.util.List;


/**
 * 积分明细总界面
 */
public class IntegralDetailListActivity extends BaseActivity {

    /**
     * 加载进度条
     */
    private ProgressWheel progressWheel;

    /**
     * 积分明细列表
     */
    private TRecyclerView tRecyclerView;

    private IntegralDetailListWrapperGet integralDetailListWrapperGet;

    private IntegralDetailListAdapter integralDetailListAdapter;


    /**
     * 为空的时候显示布局
     */
    private BlankLayout mBlankLayout;

    private LinearLayout top;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integral_detail_list_activity);
        initUI();
    }


    private void initUI() {
        setHeaderTitle(R.string.integral_detail_list_title);

        progressWheel = (ProgressWheel) findViewById(R.id.integral_detail_list_activity_pw);
        tRecyclerView = (TRecyclerView) findViewById(R.id.integral_detail_list_activity_trecyclerview);
        mBlankLayout = (BlankLayout) findViewById(R.id.blank_layout);
        top= (LinearLayout) findViewById(R.id.integral_detail_list_top_layout);

        integralDetailListAdapter = new IntegralDetailListAdapter(this);
        integralDetailListWrapperGet = new IntegralDetailListWrapperGet(this);
        CommonListLoadMoreHandler loadMoreHandler = new CommonListLoadMoreHandler();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        integralDetailListWrapperGet.setListAdapter(integralDetailListAdapter).setProgressWheel(progressWheel).setListView(tRecyclerView).setLayoutManager(layoutManager)
                .setLoadMoreHandler(loadMoreHandler).init();
        tRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                //暂无点击操作
            }
        });
        integralDetailListWrapperGet.setRequestListener(new IntegralDetailListWrapperGet.RequestListener() {
            @Override
            public void onRequestSuccess(List data) {
                if ((data == null || data.size() == 0) && integralDetailListAdapter.getItemCount() == 0) {
                    //显示订单列表为空提示
                    showBlankLayout();
                } else {
                    hideBlankLayout();
                }
            }

            @Override
            public void onRequestError(VolleyError volleyError) {
            }
        });

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("pageNumber", "1");
        String url = getString(R.string.serverbaseurl) + getString(R.string.queryByMemberIntegarlDetail) + "?pageSize=20&integralKind=&memberId=" + Constants.userId;
        integralDetailListWrapperGet.startQuery(url, map);

    }

    /**
     * 设置为空和隐藏
     **/
    public void hideBlankLayout() {
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setVisibility(View.INVISIBLE);
        tRecyclerView.setVisibility(View.VISIBLE);
        top.setVisibility(View.VISIBLE);
        progressWheel.setVisibility(View.GONE);
    }

    public void showBlankLayout() {
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setBlankLayoutInfo(R.drawable.ic_space_order, R.string.integral_detail_empty);
        mBlankLayout.setVisibility(View.VISIBLE);
        top.setVisibility(View.INVISIBLE);
        tRecyclerView.setVisibility(View.INVISIBLE);
        progressWheel.setVisibility(View.GONE);
    }
}
