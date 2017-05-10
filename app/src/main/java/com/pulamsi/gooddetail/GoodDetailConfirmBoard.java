package com.pulamsi.gooddetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.constant.Constants;
import com.pulamsi.gooddetail.adapter.PriceListAdapter;
import com.pulamsi.myinfo.order.OrderConfirmationActivity;
import com.pulamsi.myinfo.order.entity.Product;
import com.pulamsi.myinfo.order.entity.ProductPrice;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.ItemNumberEditView;
import com.pulamsi.views.dialog.CommonBaseSafeDialog;
import com.taobao.uikit.feature.view.TRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 商品详情确认面板（【立即购买】 和 【加入购物车】 公用），整个编辑面板本身是一个自定义的 dialog，封装编辑面板里的相关操作
 *
 * @author WilliamChik on 2015/9/16.
 */
public class GoodDetailConfirmBoard extends CommonBaseSafeDialog implements View.OnClickListener {

    private Product mItemDO;

    private GoodDetailBottomOperationBarFragment goodDetailBottomOperationBarFragment;

    private TRecyclerView priceTRecyclerView;

    private PriceListAdapter priceListAdapter;

    private ArrayList<ProductPrice> productPrices;

    // 当前的确认面板是属于【加入购物车】还是【立即购买】
    private boolean mIsAddToShoppingCar;

    // 数量编辑按钮
    private ItemNumberEditView mNumberEditView;

    // sku text view
    private TextView mGoodSkuTv;

    String count;//商品数量
    String priceId;

    private Activity activity;

    public void setIsAddToShoppingCar(boolean isAddToShoppingCar) {
        mIsAddToShoppingCar = isAddToShoppingCar;
    }

    public GoodDetailConfirmBoard(Activity activity, GoodDetailBottomOperationBarFragment goodDetailBottomOperationBarFragment, Product itemDO) {
        super(activity, R.style.Hai_AlertDialogFromBottom);
        this.goodDetailBottomOperationBarFragment = goodDetailBottomOperationBarFragment;
        this.activity = activity;
        mItemDO = itemDO;
    }

    public void initUI() {
        ImageView goodImg = (ImageView) findViewById(R.id.civ_good_detail_board_good_img);
        TextView goodPrice = (TextView) findViewById(R.id.tv_good_detail_board_good_price);
        mGoodSkuTv = (TextView) findViewById(R.id.tv_good_detail_board_sku_str);
        mNumberEditView = (ItemNumberEditView) findViewById(R.id.inev_good_detail_edit_number);
        priceTRecyclerView = (TRecyclerView) findViewById(R.id.price_trecyclerview);
        ImageView boardCloseBtn = (ImageView) findViewById(R.id.iv_good_detail_board_close_btn);
        TextView confirmBtn = (TextView) findViewById(R.id.tv_good_detail_board_confirm_btn);
        boardCloseBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

        /**
         * 商品图片
         */
        String urlPath = activity.getString(R.string.serverbaseurl) + mItemDO.getPic();

        Glide.with(activity)//更改图片加载框架
                .load(urlPath)
                .centerCrop()
//            .placeholder(R.drawable.pulamsi_loding)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(goodImg);
//    PulamsiApplication.imageLoader.displayImage(urlPath, goodImg, PulamsiApplication.options);


        if (mItemDO.getPanicBuyEndTime() != 0 && mItemDO.getPanicBuyBeginTime() != 0) {//是否为抢购
            goodPrice.setText("¥" + mItemDO.getPanicBuyPrice());

        }else {
            // 商品价格
            goodPrice.setText("¥" + mItemDO.getPrice());
        }


        // 商品上限
        mNumberEditView.setDiplayNumAndUpperLimit("1", "200");

        mGoodSkuTv.setText(mItemDO.getName());

        /**
         * 是积分商品，要显示价格选择器
         */
        if (null != mItemDO.getIsIntegral() && mItemDO.getIsIntegral()) {
            goodPrice.setVisibility(View.GONE);
            priceListAdapter = new PriceListAdapter(activity, this);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            priceTRecyclerView.setLayoutManager(layoutManager);
            priceTRecyclerView.setAdapter(priceListAdapter);
            priceTRecyclerView.setHasFixedSize(true);

            productPrices = new ArrayList<>();
            ProductPrice price = new ProductPrice();
            price.setIsSelect(true);
            price.setId(0);
            price.setIntegralPrice(0);
            price.setProductPrice(mItemDO.getPrice());
            productPrices.add(price);
            productPrices.addAll(mItemDO.getProductPrices());
//      for (int i =0 ; i < 3 ;i ++){
//        ProductPrice productPrice = new ProductPrice();
//        if (i == 0){
//          productPrice.setIsSelect(true);
//        }else {
//          productPrice.setIsSelect(false);
//        }
//        productPrice.setIntegralPrice(100);
//        productPrice.setProductPrice(new BigDecimal(100));
//        productPrices.add(productPrice);
//      }
            priceListAdapter.addDataList(productPrices);
            priceListAdapter.notifyItemRangeInserted(0, productPrices.size());
            priceTRecyclerView.setVisibility(View.VISIBLE);
            priceTRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
                @Override
                public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                    setOnItemSelect(position);
                }
            });
        }

    }


    public void setOnItemSelect(int position) {
        for (int i = 0; i < productPrices.size(); i++) {
            if (i == position) {
                productPrices.get(i).setIsSelect(true);
            } else {
                productPrices.get(i).setIsSelect(false);
            }
        }
        priceListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_detail_confirm_board);
        // 5.0系统以上时，布局相关的属性不能通过 style 文件的方式来设置性，只能通过代码设置
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        initUI();
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.iv_good_detail_board_close_btn) {
            hide();
        } else if (viewId == R.id.tv_good_detail_board_confirm_btn) {
            /**
             *  hide() 必须在发送消息之前，因为 dialog 显示的时候 fragment 正处于 onPause() 状态，是无法订阅消息的，
             *  只有把 dialog 隐藏了 fragment 才能显示出来
             */
            hide();
            priceId = "0";
            if (null != productPrices) {
                for (int i = 0; i < productPrices.size(); i++) {
                    if (productPrices.get(i).isSelect()) {
                        priceId = productPrices.get(i).getId() + "";
                        break;
                    }
                }
            }
            count = mNumberEditView.getItemDisplayNumString();
            if (!TextUtils.isEmpty(count) && Integer.parseInt(count) < 1) {
                ToastUtil.showToast("请您至少选择一件商品");
                return;
            }
            if (mIsAddToShoppingCar) {
                //加入购物车
                goodDetailBottomOperationBarFragment.addShoppingCar(count, priceId);
            } else {
                //立即购买
                if (mItemDO.getPanicBuyEndTime() != 0 && mItemDO.getPanicBuyBeginTime() != 0) {//是否为抢购，是否开始
                    isSnapUp();
                } else {
                    Intent confirmation = new Intent(activity, OrderConfirmationActivity.class);
                    confirmation.putExtra("isonce", true);
                    confirmation.putExtra("isSnapUp", false);//不是抢购
                    confirmation.putExtra("priceId", priceId);//价格id
                    confirmation.putExtra("sn", mItemDO.getSn());//商品编号
                    confirmation.putExtra("count", count);//商品数量
                    activity.startActivity(confirmation);
                }

            }
        }
    }

    //是否符合抢购
    private void isSnapUp() {
        if (Integer.parseInt(count) > 1) {
            ToastUtil.showToast("每次限购一件");
        }else {
            requestSnapup();//是否符合抢购规则
        }
    }

    private void requestSnapup() {
        DialogUtil.showLoadingDialog(activity, "正在下单");
        String url = activity.getString(R.string.serverbaseurl)+"android/panicBuy/isBuy.html";
        StringRequest snapUpRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    JSONObject jo = null;
                    try {
                        jo = new JSONObject(response);
                        Boolean success = jo.getBoolean("success");
                        String message  = jo.getString("message");
                        if (!success){
                            ToastUtil.showToast(message);
                        }else {
                            Intent confirmation = new Intent(activity, OrderConfirmationActivity.class);
                            confirmation.putExtra("isonce", true);//判断是从哪里进来
                            confirmation.putExtra("isSnapUp", true);//是抢购
                            confirmation.putExtra("priceId", priceId);//价格id
                            confirmation.putExtra("sn", mItemDO.getSn());//商品编号
                            confirmation.putExtra("count", "1");//商品数量,抢购默认为1件
                            activity.startActivity(confirmation);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                DialogUtil.hideLoadingDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showToast(PulamsiApplication.context.getResources().getString(R.string.notice_networkerror));
                DialogUtil.hideLoadingDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("pid", mItemDO.getId());
                map.put("mid", Constants.userId);
                return map;
            }
        };
        snapUpRequest.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        PulamsiApplication.requestQueue.add(snapUpRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
