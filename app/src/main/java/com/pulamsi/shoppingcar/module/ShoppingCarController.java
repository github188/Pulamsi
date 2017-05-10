package com.pulamsi.shoppingcar.module;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.order.OrderConfirmationActivity;
import com.pulamsi.shoppingcar.adapter.ShoppingCarListAdapter;
import com.pulamsi.shoppingcar.entity.CartCommodity;
import com.pulamsi.start.init.entity.User;
import com.pulamsi.utils.SoftInputUtil;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.BlankLayout;
import com.pulamsi.views.PtrStylelFrameLayout;
import com.pulamsi.views.header.TitleHeaderBar;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 购物车处理类
 * Created by lanqiang on 15/11/23.
 */
public class ShoppingCarController implements PtrHandler {

    /**
     * activity
     */
    private Activity activity;
    /**
     * rootview
     */
    private View rootView;
    /**
     * 头部标题栏
     */
    protected TitleHeaderBar mTitleHeaderBar;

    private TRecyclerView shoppingcarTRecyclerView;

    /**
     * 购物车适配器
     */
    private ShoppingCarListAdapter shoppingCarListAdapter;

    private boolean isEdit = false;

    /**
     * 购物车数据列表
     */
    private List<CartCommodity> carts;
    /**
     * 空值显示view
     */
    private BlankLayout mBlankLayout;

    /**
     * 底部操作layout
     */
    private RelativeLayout bottomLayout;

    /**
     * 下拉刷新控件
     */
    private PtrStylelFrameLayout ptrStylelFrameLayout;

    /**
     * 全选按钮
     */
    private CheckBox allCheckBox;

    /**
     * 选中商品价格
     */
    private TextView goodTotalprice;
    /**
     * 结算
     */
    private TextView settlementBtn;


    public void initUI(View rootView, final Activity activity) {
        Constants.shoppingCarController = this;
        this.activity = activity;
        this.rootView = rootView;

        mTitleHeaderBar = (TitleHeaderBar) rootView.findViewById(R.id.shoppingcar_header);
        mTitleHeaderBar.setTitle("购物车");
        mTitleHeaderBar.getLeftImageView().setVisibility(View.GONE);
        mTitleHeaderBar.getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == carts) {
                    return;
                }
                if (isEdit) {
                    mTitleHeaderBar.setRightText("编辑全部");
                    SoftInputUtil.hideSoftInput(activity);
                } else {
                    mTitleHeaderBar.setRightText("完成");
                }
                for (int i = 0; i < carts.size(); i++) {
                    carts.get(i).setIsEdit(!isEdit);
                    carts.get(i).setIsupdate(true);//要更新
                }
                shoppingCarListAdapter.notifyDataSetChanged();
                isEdit = !isEdit;
            }
        });
        shoppingcarTRecyclerView = (TRecyclerView) rootView.findViewById(R.id.shoppingcar_trecyclerview);
        mBlankLayout = (BlankLayout) rootView.findViewById(R.id.blank_layout);
        bottomLayout = (RelativeLayout) rootView.findViewById(R.id.shoppingcar_bottom_layout);
        allCheckBox = (CheckBox) rootView.findViewById(R.id.shoppingcar_all_check);
        goodTotalprice = (TextView) rootView.findViewById(R.id.shoppingcar_total_price);
        settlementBtn = (TextView) rootView.findViewById(R.id.shoppingcar_settlement_btn);
        ptrStylelFrameLayout = (PtrStylelFrameLayout) rootView.findViewById(R.id.shoppingcar_ptrmaterframe);
        ptrStylelFrameLayout.setPtrHandler(this);
        shoppingCarListAdapter = new ShoppingCarListAdapter(activity);
        StaggeredGridLayoutManager tuijianlayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        shoppingcarTRecyclerView.setLayoutManager(tuijianlayoutManager);
        shoppingcarTRecyclerView.setAdapter(shoppingCarListAdapter);
        shoppingcarTRecyclerView.setHasFixedSize(true);
        shoppingcarTRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {

            }
        });
        shoppingCarListAdapter.setUpdateCallback(new ShoppingCarListAdapter.updateCallback() {
            @Override
            public void update() {
                allCheckBox.setChecked(isAllSelected());
                initBottomLayout();
            }
        });
        //删除必须在carts里删除数据，不然会有缓存
        shoppingCarListAdapter.setDeleAddressCallback(new ShoppingCarListAdapter.DeleShoppingCarCallback() {
            @Override
            public void deleShoppingCarCallback(int position) {
                carts.remove(position);
                if (carts.size() == 0) {
                    showBlankLayout();
                }
                initBottomLayout();
            }
        });
        //全选按钮
        allCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allCheckBox.setClickable(false);
                selectedAll();
                String url = activity.getString(R.string.serverbaseurl) + activity.getString(R.string.selectAllItem);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                    public void onResponse(String result) {
//                        try {
//                            if (result != null) {
//                                Gson gson = new Gson();
//                                ResultContext resultContext = gson.fromJson(result, ResultContext.class);
//                                if (resultContext.getReturn_code().equals(resultContext.SUCCESS)) {
//
//                                }
//                            }
//                        } catch (Exception e) {
//                        }
                        allCheckBox.setClickable(true);
                    }
                }, new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError arg0) {
                        allCheckBox.setClickable(true);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("isSelect", allCheckBox.isChecked() ? "1" : "0");
                        map.put("mid", Constants.userId);
                        return map;
                    }
                };
                PulamsiApplication.requestQueue.add(stringRequest);
            }
        });

        //结算
        settlementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHasSelected()) {
                    //跳转提交订单界面
                    orderConfirmation();
                } else {
                    ToastUtil.showToast("请先勾选需要购买的商品");
                }
            }
        });
        //调用自动下拉刷新
        refreshList();
    }


    //跳转提交订单界面
    private void orderConfirmation() {
        if (hasNOMarketable()) {
            ToastUtil.showToast("请先删除已下架的商品在提交订单");
        } else {
            Intent confirmation = new Intent(activity, OrderConfirmationActivity.class);
            activity.startActivity(confirmation);
        }
    }

    private boolean hasNOMarketable() {
        boolean flag = false;
        for (int i = 0; i < carts.size(); i++) {
            if (carts.get(i).getProduct() != null) {
                if (!carts.get(i).getProduct().getIsMarketable() && carts.get(i).ischeck()) {
                    flag = true;
                    break;
                }
            } else if (carts.get(i).getSellerProduct() != null) {
                if (!carts.get(i).getSellerProduct().getIsMarketable() && carts.get(i).ischeck()) {
                    flag = true;
                    break;
                }
            }

        }
        return flag;
    }

    // 加载购物车列表
    private void getcartsGoods() {
        String showCarturlPath = activity.getString(R.string.serverbaseurl) + activity.getString(R.string.showcarturl);

        StringRequest showCartRequest = new StringRequest(Request.Method.POST, showCarturlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        carts = gson.fromJson(result, new TypeToken<List<CartCommodity>>() {
                        }.getType());

                        if (null != carts && carts.size() > 0) {
                            updateAdapter(carts);
                            hideBlankLayout();
                        } else {
                            showBlankLayout();
                        }
                        // 如果启动了下拉刷新，则在数据加载完成时通知 PtrFrameLayout 完成刷新操作
                        ptrStylelFrameLayout.refreshComplete();
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "购物车数据装配出错");
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                ToastUtil.showToast(R.string.notice_networkerror);
                showBlankLayout();
                // 如果启动了下拉刷新，则在数据加载完成时通知 PtrFrameLayout 完成刷新操作
                ptrStylelFrameLayout.refreshComplete();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("mid", Constants.userId);
                return map;
            }
        };
        showCartRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(showCartRequest);
    }

    /**
     * 装配数据
     */
    public void updateAdapter(List<CartCommodity> data) {
        //  状态还原
        int formerItemCount = shoppingCarListAdapter.getItemCount();
        shoppingCarListAdapter.clearDataList();
        shoppingCarListAdapter.notifyItemRangeRemoved(0, formerItemCount);
        shoppingCarListAdapter.addDataList(data);
        shoppingCarListAdapter.notifyItemRangeInserted(0, data.size());
        shoppingCarListAdapter.notifyDataSetChanged();

        shoppingcarTRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * 刷新购物车列表
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
        mBlankLayout.setVisibility(View.INVISIBLE);
        shoppingcarTRecyclerView.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.VISIBLE);
        mTitleHeaderBar.setRightText("编辑全部");
        initBottomLayout();
    }


    public void showBlankLayout() {
        mBlankLayout.setBlankLayoutInfo(R.drawable.ic_car_empty, R.string.shopping_car_blanck_message);
        mBlankLayout.setVisibility(View.VISIBLE);
        shoppingcarTRecyclerView.setVisibility(View.INVISIBLE);
        bottomLayout.setVisibility(View.GONE);
        mTitleHeaderBar.getRightTextView().setVisibility(View.GONE);
    }

    public void showLoginBlankLayout() {
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setBlankLayoutInfo(R.drawable.ic_car_empty, R.string.shopping_car_blanck_login_message);
        mBlankLayout.setVisibility(View.VISIBLE);
        shoppingcarTRecyclerView.setVisibility(View.INVISIBLE);
        bottomLayout.setVisibility(View.GONE);
        mTitleHeaderBar.getRightTextView().setVisibility(View.GONE);
        ptrStylelFrameLayout.refreshComplete();
    }

    /**
     * 初始化底部菜单数据
     */
    private void initBottomLayout() {
        allCheckBox.setChecked(isAllSelected());
        goodTotalprice.setText(getTotalPrice());
        settlementBtn.setText("结算(" + getTotalSum() + ")");
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, shoppingcarTRecyclerView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        //判断是否已经登陆
        try {
            User user = PulamsiApplication.dbUtils.findFirst(Selector.from(User.class));
            if (null != user && user.isHasLogin()) {
                getcartsGoods();
            } else {
                showLoginBlankLayout();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取选中商品的总价和总积分
     */
    private String getTotalPrice() {
        BigDecimal totalPrice = new BigDecimal(0);
        int totalintegralPrice = 0;
        for (int i = 0; i < carts.size(); i++) {
            CartCommodity cartCommodity = carts.get(i);
            if (cartCommodity.ischeck()) {
                totalPrice = totalPrice.add(cartCommodity.getTotalPrice());
                totalintegralPrice += cartCommodity.getTotalintegralPrice();
            }
        }
        String total = "¥0.00";
        if (totalPrice.floatValue() <= 0) {
            if (totalintegralPrice > 0) {
                total = totalintegralPrice + "积分";
            }
        } else {
            if (totalintegralPrice > 0) {
                total = "¥" + totalPrice + "\n+" + totalintegralPrice + "积分";
            } else {
                total = "¥" + totalPrice;
            }
        }
        return total;
    }

    /**
     * 获取选中商品的件数
     */
    private int getTotalSum() {
        int totalSum = 0;
        for (int i = 0; i < carts.size(); i++) {
            CartCommodity cartCommodity = carts.get(i);
            if (cartCommodity.ischeck()) {
                totalSum += cartCommodity.getCount();
            }
        }
        return totalSum;
    }


    // 全选或全取消
    private void selectedAll() {
        for (int i = 0; i < carts.size(); i++) {
            carts.get(i).setIscheck(allCheckBox.isChecked());
        }
        shoppingCarListAdapter.notifyDataSetChanged();
        initBottomLayout();
    }

    /**
     * 判断是否购物车中所有的商品全部被选中
     *
     * @return true所有条目全部被选中 false还有条目没有被选中
     */
    private boolean isAllSelected() {
        boolean flag = true;
        for (int i = 0; i < carts.size(); i++) {
            if (!carts.get(i).ischeck()) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 判断是否购物车中是否有选中的商品
     *
     * @return true有记录被选中 false没有记录被选中
     */
    private boolean isHasSelected() {
        boolean flag = false;
        for (int i = 0; i < carts.size(); i++) {
            if (carts.get(i).ischeck()) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
