package com.pulamsi.gooddetail;

import android.content.Intent;
import android.graphics.Color;
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
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseActivity.BaseFragment;
import com.pulamsi.base.otto.BusProvider;
import com.pulamsi.base.otto.MessageEvent;
import com.pulamsi.constant.Constants;
import com.pulamsi.loginregister.LoginActivity;
import com.pulamsi.myinfo.order.entity.Product;
import com.pulamsi.start.MainActivity;
import com.pulamsi.start.init.entity.User;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.utils.bean.CoutDownBean;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;

public class GoodDetailBottomOperationBarFragment extends BaseFragment implements OnClickListener {

    /**
     * 商品对象
     */
    private Product product;
    private CoutDownBean coutDownBean;
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
        switch (v.getId()){
            case R.id.ll_good_detail_go_to_home_btn:
                //回首页
                BaseAppManager.getInstance().clear();
                MainActivity.mTabView.setCurrentTab(0);
                break;
            case R.id.tv_good_detail_add_to_shopping_car_btn:
                //加入购物车
                if (mUser.isHasLogin()){
                    if (product.getIsMarketable() != null && !product.getIsMarketable()){
                        ToastUtil.showToast("商品已下架,请选择其他商品购买");
                        return;
                    }
                    if (mConfirmBoard == null) {
                        mConfirmBoard = new GoodDetailConfirmBoard(getActivity(),this, product);
                    }
                    mConfirmBoard.setIsAddToShoppingCar(true);
                    mConfirmBoard.show();
                }else {
                    Intent loginandregister = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginandregister);
                }
                break;
            case R.id.tv_good_detail_buy_now_car_btn:
                //立即购买
                if (product.getIsMarketable() != null &&!product.getIsMarketable()){
                    ToastUtil.showToast("商品已下架,请选择其他商品购买");
                    return;
                }
                if (mUser.isHasLogin()){
                        if (mConfirmBoard == null) {
                            mConfirmBoard = new GoodDetailConfirmBoard(getActivity(),this, product);
                        }
                        mConfirmBoard.setIsAddToShoppingCar(false);
                        mConfirmBoard.show();

                }else {
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
        BusProvider.getInstance().register(this);

    }

    //把商品信息传递进来，给按钮
    public void setData(Product product,CoutDownBean coutDownBean){
        this.product = product;
        this.coutDownBean = coutDownBean;
        if (product != null && product.getPanicBuyEndTime() != 0 && product.getPanicBuyBeginTime() != 0)//判断是否抢购
            initSnapUp();
    }

    /**
     * 初始化抢购
     */
    private void initSnapUp() {
        shoppingCar.setBackgroundColor(Color.WHITE);
        shoppingCar.setTextSize(12);
        shoppingCar.setTextColor(getResources().getColor(R.color.app_font_color_77));
        shoppingCar.setText("每个ID限购" + product.getPanicBuyQuantity() + "件");
        shoppingCar.setEnabled(false);

       if (coutDownBean.getIsEnd()){
           buynow.setText("已结束");
           buynow.setEnabled(false);
           buynow.setTextColor(getResources().getColor(R.color.white_seach));
           buynow.setBackgroundColor(Color.GRAY);
       }else {
           if (coutDownBean.getIsBegin()){//如果已经开始
               buynow.setText("立即抢购");
           }else {
               buynow.setEnabled(false);
               buynow.setTextColor(getResources().getColor(R.color.white_seach));
               buynow.setBackgroundColor(getResources().getColor(R.color.app_pulamsi_main_color));
               buynow.setText("即将开始");
           }
       }
    }


    public void addShoppingCar(final String pcount,final String priceId){
        String addurlPath = getString(R.string.serverbaseurl) + getString(R.string.addCartItem);
        StringRequest addRequest = new StringRequest(Request.Method.POST, addurlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    ToastUtil.showToast("已加入购物车");
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
                map.put("pid", product.getId());
                map.put("pcount", pcount);
                map.put("priceId", priceId);
                return map;
            }
        };
        addRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(addRequest);
    }


    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }


    @Subscribe   //订阅事件DataChangedEvent
    public void OnSnapupEvent(MessageEvent event){
        if (event.getTag().equals("complete")){
            if (!coutDownBean.getIsBegin()){
                buynow.setText("立即抢购");
                buynow.setTextColor(Color.WHITE);
                buynow.setEnabled(true);
                buynow.setBackground(getResources().getDrawable(R.drawable.selector_btn_solid_main_color));
            }else {
                buynow.setText("已结束");
                buynow.setEnabled(false);
                buynow.setTextColor(getResources().getColor(R.color.white_seach));
                buynow.setBackgroundColor(Color.GRAY);
            }
        }
    }
}

