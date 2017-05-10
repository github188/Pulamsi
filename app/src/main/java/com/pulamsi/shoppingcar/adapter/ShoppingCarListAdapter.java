package com.pulamsi.shoppingcar.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.order.entity.Product;
import com.pulamsi.shoppingcar.entity.CartCommodity;
import com.pulamsi.shoppingcar.viewholder.ShoppingCarItemViewHolder;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.GoodsHelper;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.ItemNumberEditView;
import com.pulamsi.views.dialog.CommonAlertDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * 购物车数据适配器
 */
public class ShoppingCarListAdapter extends HaiBaseListAdapter<CartCommodity> {
    private Activity activity;




    public ShoppingCarListAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.shoppingcar_trecyclerview_item, null);
        if (convertView == null) {
            return null;
        }

        return new ShoppingCarItemViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder == null || !(holder instanceof ShoppingCarItemViewHolder) || !(getItem(position) instanceof CartCommodity)) {
            return;
        }

        final ShoppingCarItemViewHolder newHolder = (ShoppingCarItemViewHolder) holder;
        final CartCommodity newItem = (CartCommodity) getItem(position);


        /**
         * 设置数字输入框的上限和初始值和监听器
         */
        if (!newItem.isupdate()) {
            if (newItem.getCount() <= 200) {
                newHolder.numberEditView.setDiplayNumAndUpperLimit(newItem.getCount() + "", "200");
            } else {
                //自动把超过200的变成200
                newHolder.numberEditView.setDiplayNumAndUpperLimit("200", "200");
            }
        }
        /**
         * 编辑模式切换
         */
        if (newItem.isEdit()) {
            newHolder.editBoard.setVisibility(View.VISIBLE);
            newHolder.checkBox.setVisibility(View.GONE);
        } else {
            //当切换到完成模式时需要对手动输入的数量进行更改，当数量为1时不做更新和数量和数据中的数量一样时不做更新。
            if (!"".equals(newHolder.numberEditView.getItemDisplayNumString())) {
                if (newItem.getCount() != Integer.parseInt(newHolder.numberEditView.getItemDisplayNumString())) {
                    newItem.setCount(Integer.parseInt(newHolder.numberEditView.getItemDisplayNumString()));
                    updateCartItemCount(newHolder.numberEditView.getItemDisplayNumString(), newItem.getId());
                    if (null != updateCallback) {
                        updateCallback.update();
                    }
                    newItem.setIsupdate(false);
                }
            }
            newHolder.editBoard.setVisibility(View.GONE);
            newHolder.checkBox.setVisibility(View.VISIBLE);
        }
        newHolder.numberEditView.setNumberEditViewClickListener(new ItemNumberEditView.NumberEditViewClickListener() {

            @Override
            public void onNumberChange() {
                editItemCountBtnClickLogic(newHolder, newItem);
            }

            @Override
            public void onMinusBtnClick() {
            }

            @Override
            public void onPlusBtnClick() {
            }
        });

        /**
         * 天使商品需要作此判断
         * 我写这段代码的时候只有我和上帝看得懂
         *
         * 现在
         * 只有上帝看得懂了
         */
        Product product = null;

        if (newItem.getProduct() != null){
            product = newItem.getProduct();
        }else if (newItem.getSellerProduct() != null){
            product = newItem.getSellerProduct();
        }


        /**
         * 商品图片
         */
        String urlPath = activity.getString(R.string.serverbaseurl) + product.getPic();
//    String tag = (String) newHolder.goodIcon.getTag();
//    if (!(null != tag && tag.equals(urlPath))){
        //这里会出现异常，不能用setTag()
        Glide.with(activity)//更改图片加载框架
                .load(urlPath)
                .centerCrop()
                .placeholder(R.drawable.pulamsi_loding)
                .crossFade()//淡入淡出
                .diskCacheStrategy(DiskCacheStrategy.ALL)//磁盘缓存
                .into(newHolder.goodIcon);
//      PulamsiApplication.imageLoader.displayImage(urlPath, newHolder.goodIcon, PulamsiApplication.options);
//      newHolder.goodIcon.setTag(urlPath);
//    }


        final String pid = product.getId();
        final boolean isSeller =  product.getIsSeller();
        newHolder.goodIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSeller){//是否为天使商品
                    GoodsHelper.gotoAngelDetail(pid,activity);
                }else {
                    GoodsHelper.gotoDetail(pid, activity);
                }
            }
        });


        /**
         * 商品标题
         */
        if (product.getIsMarketable() == null || product.getIsMarketable()) {
            newHolder.goodtile.setText(product.getName());
        } else {
            newHolder.goodtile.setText(product.getName() + "(已下架)");
        }
        /**
         * 编辑状态的商品标题
         */
        newHolder.editTitle.setText(product.getName());

        /**
         * 商品价格,如果是积分商品，价格为0就不显示
         */
        if (newItem.getProductPrice().floatValue() > 0) {
            newHolder.price.setText("¥" + newItem.getProductPrice());
        } else {
            /**
             * 价格为0是赋值积分
             */
            newHolder.price.setText(newItem.getIntegralPrice() + "积分");
        }

        /**
         * 商品积分，为0就不显示,价格和积分同时存在的时候才显示后面一段积分
         */
        if (newItem.getProductPrice().floatValue() > 0) {
            if (newItem.getIntegralPrice() > 0) {
                newHolder.integralPrice.setText("+" + newItem.getIntegralPrice() + "积分");
            }
        }
        /**
         * 商品数量
         */
        newHolder.goodnum.setText(newItem.getCount() + "");

        /**
         * 商品选择框监听事件
         */
        newHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                newHolder.checkBox.setClickable(false);
                newItem.setIscheck(((CheckBox) v).isChecked());
                if (null != updateCallback) {
                    updateCallback.update();
                }
                String url = activity.getString(R.string.serverbaseurl) + activity.getString(R.string.selectItem);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                    public void onResponse(String result) {
//                    try {
//                      if (result != null) {
//                        Gson gson = new Gson();
//                        ResultContext resultContext = gson.fromJson(result, ResultContext.class);
//                        if (resultContext.getReturn_code().equals(resultContext.SUCCESS)){
//                        }
//                      }
//                    }catch (Exception e){
//                    }
                        newHolder.checkBox.setClickable(true);
                    }
                }, new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError arg0) {
                        newHolder.checkBox.setClickable(true);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("cid", newItem.getId());
                        map.put("isSelect", ((CheckBox) v).isChecked() ? "1" : "0");
                        map.put("mid", Constants.userId);
                        return map;
                    }
                };
                PulamsiApplication.requestQueue.add(stringRequest);
            }
        });
        /**
         * 设置商品是否为选中
         */
        newHolder.checkBox.setChecked(newItem.ischeck());

        newHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonAlertDialog(mActivity, mActivity.getResources().getString(R.string.alert_dialog_confirm_to_delete_good_msg),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteShoppingCar(newItem, position);
                            }
                        }).show();
            }
        });

    }


    /**
     * 编辑商品数量
     */
    private void editItemCountBtnClickLogic(final ShoppingCarItemViewHolder viewHolder, final CartCommodity itemDO) {
        if (itemDO.getCount() != Integer.parseInt(viewHolder.numberEditView.getItemDisplayNumString())) {
            updateCartItemCount(viewHolder.numberEditView.getItemDisplayNumString(), itemDO.getId());
            // 网络请求编辑商品数量
            itemDO.setCount(Integer.parseInt(viewHolder.numberEditView.getItemDisplayNumString()));
            // UI 更新
            viewHolder.goodnum.setText(viewHolder.numberEditView.getItemDisplayNumString());
            if (null != updateCallback) {
                updateCallback.update();
            }
        }
    }


    public interface updateCallback {
        public abstract void update();
    }

    private updateCallback updateCallback;

    public void setUpdateCallback(updateCallback updateCallback) {
        this.updateCallback = updateCallback;
    }

    /**
     * 购物车货物数量变化
     *
     * @return
     */
    public void updateCartItemCount(final String ccount, final String cid) {

        String updateCartItemCounturlPath = activity.getString(R.string.serverbaseurl) + activity.getString(R.string.updateCartItemCount);
        StringRequest updateCartItemCountRequest = new StringRequest(
                Request.Method.POST, updateCartItemCounturlPath,
                new Response.Listener<String>() {

                    public void onResponse(String result) {
                        if (result != null) {
                            HaiLog.d("pulanmsi", "调用成功");
                            HaiLog.d("pulanmsi", cid);
                            HaiLog.d("pulanmsi", ccount);
                        }
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("cid", cid);
                map.put("ccount", ccount);
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(updateCartItemCountRequest);
    }

    /**
     * 勾选购物车上传状态给服务器
     *
     * @return
     */
    public void selectItem(final String cid, final String isSelect) {


    }


    /**
     * 删除购物车货物
     *
     * @return
     */
    public void deleteShoppingCar(final CartCommodity cartCommodity, final int position) {
        DialogUtil.showLoadingDialog(activity, "删除中...");
        String updateCartItemCounturlPath = activity.getString(R.string.serverbaseurl) + activity.getString(R.string.updateCartItemCount);
        StringRequest updateCartItemCountRequest = new StringRequest(
                Request.Method.POST, updateCartItemCounturlPath,
                new Response.Listener<String>() {

                    public void onResponse(String result) {
                        if (result != null) {
                            mData.remove(cartCommodity);
                            notifyDataSetChanged();
                            ToastUtil.showToast(R.string.shipping_address_deleteOKmsg);
                            if (null != deleShoppingCarCallback) {
                                deleShoppingCarCallback.deleShoppingCarCallback(position);
                            }
                        }
                        DialogUtil.hideLoadingDialog();
                    }
                }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                ToastUtil.showToast(R.string.notice_networkerror);
                DialogUtil.hideLoadingDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("cid", cartCommodity.getId());
                map.put("ccount", "0");
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(updateCartItemCountRequest);
    }


    public interface DeleShoppingCarCallback {
        public abstract void deleShoppingCarCallback(int position);
    }

    private DeleShoppingCarCallback deleShoppingCarCallback;

    public void setDeleAddressCallback(DeleShoppingCarCallback deleShoppingCarCallback) {
        this.deleShoppingCarCallback = deleShoppingCarCallback;
    }

}
