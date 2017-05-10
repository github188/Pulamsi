package com.pulamsi.snapup.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.base.baseUtil.PriceUtil;
import com.pulamsi.gooddetail.GoodDetailActivity;
import com.pulamsi.myinfo.order.entity.Product;
import com.pulamsi.snapup.adapter.viewHolder.SnapUpMoreViewHolder;
import com.pulamsi.snapup.bean.SnapUpBean;
import com.pulamsi.utils.DialogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-08-26
 * Time: 09:53
 * FIXME
 */
public class SnapUpRecyclerViewAdapter extends HaiBaseListAdapter<SnapUpBean> {

    private Activity activity;

    public SnapUpRecyclerViewAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.snap_up_list_item, null);
        if (convertView == null) {
            return null;
        }
        return new SnapUpMoreViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null || !(holder instanceof SnapUpMoreViewHolder) || !(getItem(position) instanceof SnapUpBean)) {
            return;
        }
        SnapUpMoreViewHolder viewHolder = (SnapUpMoreViewHolder) holder;
        final SnapUpBean snapUpBean = getItem(position);

        //商品图片
        if (!TextUtils.isEmpty(snapUpBean.getImage())) {
            Glide.with(activity)//更改图片加载框架
                    .load(PulamsiApplication.context.getString(R.string.serverbaseurl) + snapUpBean.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.pulamsi_loding)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.productImage);
        }

        // 商品名称
        if (!TextUtils.isEmpty(snapUpBean.getName())) {
            viewHolder.productName.setText(snapUpBean.getName());
        } else {
            viewHolder.productName.setText("暂无信息");
        }

        // 商品抢购价格
        if (!TextUtils.isEmpty(snapUpBean.getPanicBuyPrice())) {
            viewHolder.productPrice.setText(PriceUtil.RMB + snapUpBean.getPanicBuyPrice());
        } else {
            viewHolder.productPrice.setText("暂无信息");
        }

        // 商品历史价格
        if (!TextUtils.isEmpty(snapUpBean.getPrice())) {
            viewHolder.productMarketPrice.setText(PriceUtil.RMB + snapUpBean.getPrice());
        } else {
            viewHolder.productMarketPrice.setText("暂无信息");
        }

        // 销量
        if (!TextUtils.isEmpty(snapUpBean.getSales())) {
            viewHolder.productSales.setText("已售" + snapUpBean.getSales()+"件");
        } else {
            viewHolder.productSales.setText("暂无信息");
        }

        viewHolder.productMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//删除线


        viewHolder.snapUpBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSnapUpDetail(snapUpBean.getId());//跳转抢购详情
            }
        });



    }

    /**
     * 跳转抢购商品详情页
     *
     * @param pid
     */
    private void gotoSnapUpDetail(final String pid) {
        DialogUtil.showLoadingDialog(activity, "加载中");
        String showDetailsurlPath = activity.getString(R.string.serverbaseurl)+activity.getString(R.string.snap_up_product_details);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, showDetailsurlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        Product product = gson.fromJson(result, Product.class);
                        Intent intent = new Intent(activity, GoodDetailActivity.class);
                        intent.putExtra("product", product);
                        activity.startActivity(intent);
                    } catch (Exception e) {
                    }
                }
                DialogUtil.hideLoadingDialog();
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                DialogUtil.hideLoadingDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("pid", pid);
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(stringRequest);
    }
}

