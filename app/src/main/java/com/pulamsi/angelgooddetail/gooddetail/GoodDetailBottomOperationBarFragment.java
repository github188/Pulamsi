package com.pulamsi.angelgooddetail.gooddetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.angel.bean.AngelProductBean;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseActivity.BaseFragment;
import com.pulamsi.constant.Constants;
import com.pulamsi.loginregister.LoginActivity;
import com.pulamsi.start.MainActivity;
import com.pulamsi.start.init.entity.User;
import com.pulamsi.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

public class GoodDetailBottomOperationBarFragment extends BaseFragment implements OnClickListener {

    /**
     * 商品对象
     */
    private AngelProductBean angelProductBean;
    /**
     * 记录用户对象
     */
    private User mUser;
    /**
     * 加入购物车
     */
    private TextView shoppingCar;
    /**
     * 立即购买
     */
    private TextView buynow;

    private GoodDetailConfirmBoard mConfirmBoard;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_good_detail_go_to_home_btn:
                //回首页
                BaseAppManager.getInstance().clear();
                MainActivity.mTabView.setCurrentTab(0);
                break;
            case R.id.tv_good_detail_add_to_shopping_car_btn:
                //加入购物车
                if (mUser.isHasLogin()) {
                    if (angelProductBean.getIsMarketable() != null && !angelProductBean.getIsMarketable()) {
                        ToastUtil.showToast("商品已下架,请选择其他商品购买");
                        return;
                    }
                    if (mConfirmBoard == null) {
                        mConfirmBoard = new GoodDetailConfirmBoard(getActivity(), this, angelProductBean);
                    }
                    mConfirmBoard.setIsAddToShoppingCar(true);
                    mConfirmBoard.show();
                } else {
                    Intent loginandregister = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginandregister);
                }
                break;
            case R.id.tv_good_detail_buy_now_car_btn:
                //立即购买
                if (angelProductBean.getIsMarketable() != null && !angelProductBean.getIsMarketable()) {
                    ToastUtil.showToast("商品已下架,请选择其他商品购买");
                    return;
                }
                if (mUser.isHasLogin()) {
                    if (mConfirmBoard == null) {
                        mConfirmBoard = new GoodDetailConfirmBoard(getActivity(), this, angelProductBean);
                    }
                    mConfirmBoard.setIsAddToShoppingCar(false);
                    mConfirmBoard.show();

                } else {
                    Intent loginandregister = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginandregister);
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.good_detail_bottom_operation_bar_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout gotoHome = (LinearLayout) view.findViewById(R.id.ll_good_detail_go_to_home_btn);
        shoppingCar = (TextView) view.findViewById(R.id.tv_good_detail_add_to_shopping_car_btn);
        buynow = (TextView) view.findViewById(R.id.tv_good_detail_buy_now_car_btn);
        gotoHome.setOnClickListener(this);
        shoppingCar.setOnClickListener(this);
        buynow.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        try {
            mUser = PulamsiApplication.dbUtils.findFirst(Selector.from(User.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    //把商品信息传递进来，给按钮
    public void setData(AngelProductBean angelProductBean) {
        this.angelProductBean = angelProductBean;
    }


    public void addShoppingCar(final String pcount, final String priceId) {
        String addurlPath = getString(R.string.serverbaseurl) + getString(R.string.addShoppingCartItem);
        StringRequest addRequest = new StringRequest(Request.Method.POST, addurlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    ToastUtil.showToast("已加入购物车");
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError arg0) {
                ToastUtil.showToast(R.string.notice_networkerror);
                LogUtils.e(arg0.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("mid", Constants.userId);
                map.put("pid", angelProductBean.getId());
                map.put("pcount", pcount);
                map.put("priceId", priceId);//天使不需要此参数
                LogUtils.e(angelProductBean.getId());

                return map;
            }
        };
        addRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(addRequest);
    }
}

