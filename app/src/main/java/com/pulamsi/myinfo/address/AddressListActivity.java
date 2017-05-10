package com.pulamsi.myinfo.address;

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
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.address.adapter.AddressListAdapter;
import com.pulamsi.myinfo.address.entity.Address;
import com.pulamsi.utils.IGetChildAtPosition;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.BlankLayout;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lanqiang on 15/12/1.
 */
public class AddressListActivity extends BaseActivity implements IGetChildAtPosition {

    /**
     * 收货地址列表
     */
    private TRecyclerView addressListTRecyclerView;

    /**
     *
     */
    private StaggeredGridLayoutManager addresslayoutManager;

    /**
     *  加载进度条
     */
    private ProgressWheel addressProgressWheel;

    /**
     * 列表适配器
     */
    private AddressListAdapter addressListAdapter;

    /**
     * 新建地址按钮
     */
    private TextView addAddressBtn;

    /**
     * 空值显示view
     */
    private BlankLayout mBlankLayout;

    /**
     * 是否是第一次
     */
    private boolean isFirst = true;
    /**
     * 记录地址对象集合
     */
    private ArrayList<Address> addresses;

    private boolean isCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCallBack = getIntent().getBooleanExtra("iscallback",false);
        initData();
        setContentView(R.layout.addresslist_activity);
        initView();
    }


    private void initView() {
        setHeaderTitle(R.string.address_title);
        mBlankLayout = (BlankLayout) findViewById(R.id.blank_layout);
        addressListTRecyclerView = (TRecyclerView) findViewById(R.id.addresslist_trecyclerview);
        addressProgressWheel = (ProgressWheel) findViewById(R.id.pw_addresslist_activity);
        addressListAdapter = new AddressListAdapter(this);
        //必须在列表中删除,不然会出现满8个删除的时候新建不了
        addressListAdapter.setDeleAddressCallback(new AddressListAdapter.DeleAddressCallback() {
            @Override
            public void deleAddressPosition(int position) {
                addresses.remove(position);
                addressListAdapter.notifyDataSetChanged();
            }
        });
        addresslayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        addressListTRecyclerView.setLayoutManager(addresslayoutManager);
        addressListTRecyclerView.setAdapter(addressListAdapter);
        addressListTRecyclerView.setHasFixedSize(true);
        addAddressBtn = (TextView) findViewById(R.id.addresslist_new_address_btn);
        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != addresses) {
                    if (addresses.size() < 8) {
                        Intent add = new Intent(AddressListActivity.this, AddAddressActivity.class);
                        add.putExtra("isfirst",false);
                        startActivity(add);
                    } else {
                        ToastUtil.showToast(R.string.shipping_address_alladdress);
                    }
                } else {
                    ToastUtil.showToast(R.string.shipping_address_getaddress);
                }


            }
        });
        addressListTRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                //点击事件用在选择地址返回给确认订单
                if (isCallBack){
                    Address address = addressListAdapter.getItem(position);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("address",address);
                    AddressListActivity.this.setResult(RESULT_OK, resultIntent);
                    AddressListActivity.this.finish();
                }

            }
        });
    }

    private void initData() {
        // 获取收货地址
            String showAddressurlPath = getString(R.string.serverbaseurl) + getString(R.string.showAddress);
            StringRequest showAddressRequest = new StringRequest(Request.Method.POST, showAddressurlPath, new Response.Listener<String>() {

                public void onResponse(String result) {
                    if (result != null) {
                        Gson gson = new Gson();
                         addresses = gson.fromJson(result, new TypeToken<List<Address>>() {}.getType());
                        if ((addresses == null || addresses.size() == 0) && addressListTRecyclerView.getItemCount() == 0) {
                            //显示地址列表为空提示
                            showBlankLayout();
                        } else {
                            hideBlankLayout();
                            updateAddressList(addresses);
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
        showAddressRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(showAddressRequest);
    }

    /**
     * 收货地址数据
     */
    public void updateAddressList(List<Address> data) {
        //  状态还原
        int formerItemCount = addressListAdapter.getItemCount();
        addressListAdapter.clearDataList();
        addressListAdapter.notifyItemRangeRemoved(0, formerItemCount);
        addressListAdapter.addDataList(data);
        addressListAdapter.notifyItemRangeInserted(0, data.size());

        addressListTRecyclerView.setVisibility(View.VISIBLE);
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

    @Override
    public TextView getChildAtPosition(int clickPosition) {
        // 注意这里不能使用 RecyclerView 的 getChildAt(position) 和 StaggeredGridLayoutManager 的 getChildAt(position) 方法获取指定位置的 child view。
        // 因为列表 item 的重用问题，该两个方法获取到的 child view 数量上限是列表在一页中最多显示的 item 数量，例如列表一页只能显示10个 item，
        // 则 getChildAt() 方法的索引最多只能是9。用 StaggeredGridLayoutManager 的 findViewByPosition(position) 可解决问题。
        View itemView = addresslayoutManager.findViewByPosition(clickPosition);
        if (itemView == null) {
            return null;
        }
        return (TextView) itemView.findViewById(R.id.tv_shipping_address_management_set_to_default_address_btn);
    }

    /** 设置为空和隐藏 **/
    public void hideBlankLayout() {
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setVisibility(View.INVISIBLE);
        addressListTRecyclerView.setVisibility(View.VISIBLE);
        addressProgressWheel.setVisibility(View.GONE);
    }

    public void showBlankLayout() {
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setBlankLayoutInfo(R.drawable.ic_my_address_empty, R.string.my_info_my_address_empty);
        mBlankLayout.setVisibility(View.VISIBLE);
        addressListTRecyclerView.setVisibility(View.INVISIBLE);
        addressProgressWheel.setVisibility(View.GONE);
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
