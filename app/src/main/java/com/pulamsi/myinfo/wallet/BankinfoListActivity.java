package com.pulamsi.myinfo.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.wallet.adapter.BankinfoListAdapter;
import com.pulamsi.myinfo.wallet.entity.MemberAccountCashBankInfo;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.BlankLayout;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行卡列表
 */
public class BankinfoListActivity extends BaseActivity {

    /**
     * 列表
     */
    private TRecyclerView bankinfoListTRecyclerView;

    /**
     *  加载进度条
     */
    private ProgressWheel progressWheel;

    /**
     * 列表适配器
     */
    private BankinfoListAdapter bankinfoListAdapter;

    /**
     * 新增银行卡按钮
     */
    private TextView addbankinfobtn;

    /**
     * 空值显示view
     */
    private BlankLayout mBlankLayout;

    /**
     * 是否是第一次
     */
    private boolean isFirst = true;
    /**
     * 记录银行卡对象集合
     */
    private List<MemberAccountCashBankInfo> bankInfos;

    private boolean isCallBack ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCallBack = getIntent().getBooleanExtra("iscallback",false);
        BaseAppManager.getInstance().addActivity(this);
        initData();
        setContentView(R.layout.bankinfolist_activity);
        initView();
    }


    private void initView() {
        setHeaderTitle(R.string.my_info_wallet_bankinfo_title);
        mBlankLayout = (BlankLayout) findViewById(R.id.blank_layout);
        bankinfoListTRecyclerView = (TRecyclerView) findViewById(R.id.bankinfo_activity_trecyclerview);
        progressWheel = (ProgressWheel) findViewById(R.id.bankinfo_activity_pw);
        bankinfoListAdapter = new BankinfoListAdapter(this,isCallBack);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        bankinfoListTRecyclerView.setLayoutManager(layoutManager);
        bankinfoListTRecyclerView.setAdapter(bankinfoListAdapter);
        bankinfoListTRecyclerView.setHasFixedSize(true);
        if (isCallBack){
            View footerView = BankinfoListActivity.this.getLayoutInflater().inflate(R.layout.bankinfolist_viewfooterview,bankinfoListTRecyclerView,false);
            bankinfoListTRecyclerView.addFooterView(footerView);
            footerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("islast", true);
                    resultIntent.putExtras(bundle);
                    BankinfoListActivity.this.setResult(RESULT_OK, resultIntent);
                    BankinfoListActivity.this.finish();
                }
            });
        }

        //必须在列表中删除,不然会出现删除bug
        bankinfoListAdapter.setDeleAddressCallback(new BankinfoListAdapter.DeleBankinfoCallback() {
            @Override
            public void deleBankinfoPosition(int position) {
                bankInfos.remove(position);
                bankinfoListAdapter.notifyDataSetChanged();
            }
        });
        addbankinfobtn = (TextView) findViewById(R.id.bankinfo_activity_addbankinfo);
        addbankinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inputbankInfo = new Intent(BankinfoListActivity.this, InputBankInfoActivity.class);
                inputbankInfo.putExtra("iscallback", true);
                BankinfoListActivity.this.startActivity(inputbankInfo);
            }
        });
    }

    private void initData() {
        // 获取银行卡列表
        final String bankInfoList = getString(R.string.serverbaseurl) + getString(R.string.bankInfoList);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, bankInfoList, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        bankInfos = gson.fromJson(result, new TypeToken<List<MemberAccountCashBankInfo>>() {}.getType());
                        if (null == bankInfos || bankInfos.size() == 0){
                            showBlankLayout();
                        }else {
                            hideBlankLayout();
                            updateAddressList(bankInfos);
                        }
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "钱包银行卡数据装配出错");
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                ToastUtil.showToast(R.string.notice_networkerror);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("mid", Constants.userId);
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(stringRequest);
    }

    /**
     * 银行卡列表数据装配
     */
    public void updateAddressList(List<MemberAccountCashBankInfo> data) {
        //  状态还原
        int formerItemCount = bankinfoListAdapter.getItemCount();
        bankinfoListAdapter.clearDataList();
        bankinfoListAdapter.notifyItemRangeRemoved(0, formerItemCount);
        bankinfoListAdapter.addDataList(data);
        bankinfoListAdapter.notifyItemRangeInserted(0, data.size());

        bankinfoListTRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * 重新获取前端，需要重新获取地址列表
     */
    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst){
            initData();
        }else {
            isFirst = false;
        }

    }


    /** 设置为空和隐藏 **/
    public void hideBlankLayout() {
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setVisibility(View.INVISIBLE);
        bankinfoListTRecyclerView.setVisibility(View.VISIBLE);
        progressWheel.setVisibility(View.GONE);
    }

    public void showBlankLayout() {
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setBlankLayoutInfo(R.drawable.ic_my_address_empty, R.string.my_info_wallet_bankinfo_empty);
        mBlankLayout.setVisibility(View.VISIBLE);
        bankinfoListTRecyclerView.setVisibility(View.INVISIBLE);
        progressWheel.setVisibility(View.GONE);
    }
}
