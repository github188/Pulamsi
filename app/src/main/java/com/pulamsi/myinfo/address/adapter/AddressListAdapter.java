package com.pulamsi.myinfo.address.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lidroid.xutils.exception.DbException;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.myinfo.address.AddAddressActivity;
import com.pulamsi.myinfo.address.AddressListActivity;
import com.pulamsi.myinfo.address.entity.Address;
import com.pulamsi.myinfo.address.viewholder.ShippingAddressItemViewHolder;
import com.pulamsi.start.init.entity.User;
import com.pulamsi.utils.CheckBoxUtil;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.IGetChildAtPosition;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.dialog.CommonAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 收货地址
 *
 * @author WilliamChik on 15/8/12.
 */
public class AddressListAdapter extends HaiBaseListAdapter<Address> {
  /** 用于记录当前选中的 View 的位置，避免列表滚动时由于布局重用导致的选中状态重复的问题 */
  private int mCurrentCheckPos = -1;
  private IGetChildAtPosition mGetChildAtPositionImpl;
  private Activity activity;

  public AddressListAdapter(AddressListActivity activity) {
    super(activity);
    this.activity = activity;
    mGetChildAtPositionImpl = activity;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View convertView = mInflater.inflate(R.layout.shipping_address_list_item, null);
    if (convertView == null) {
      return null;
    }

    return new ShippingAddressItemViewHolder(convertView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder == null || !(holder instanceof ShippingAddressItemViewHolder) || !(getItem(position) instanceof Address)) {
      return;
    }

    ShippingAddressItemViewHolder newHolder = (ShippingAddressItemViewHolder) holder;
    Address newItem = (Address) getItem(position);



    // 用户姓名
    if (!TextUtils.isEmpty(newItem.getName())) {
      newHolder.shippingUserNameTv.setText(newItem.getName());
    }

    // 电话
    if (!TextUtils.isEmpty(newItem.getMobile())) {
      newHolder.shippingPhoneNumTv.setText(newItem.getMobile());
    }

    // 地址
    if (!TextUtils.isEmpty(newItem.getAddressAlias()) || !TextUtils.isEmpty(newItem.getProvinceName())|| !TextUtils.isEmpty(newItem.getCountyName())|| !TextUtils.isEmpty(newItem.getCityName())) {
      newHolder.shippingUserAddressTv.setText(newItem.getProvinceName() + newItem.getCityName() + newItem.getCountyName() + newItem.getAddressAlias());
    }


    // 设为默认
    newHolder.shippingSetToDefaultAddressBtn.setOnClickListener(new OnClickListener(newItem, position));
    // 编辑
    newHolder.shippingEditAddressBtn.setOnClickListener(new OnClickListener(newItem, position));
    // 删除
    newHolder.shippingDeleteAddressBtn.setOnClickListener(new OnClickListener(newItem, position));

    // 是否是默认地址
    if ("true".equals(newItem.getIsDefault())) {
      mCurrentCheckPos = position;
      checkBtn(newHolder.shippingSetToDefaultAddressBtn, position);
    } else {
      uncheckBtn(newHolder.shippingSetToDefaultAddressBtn);
    }


  }

  private class OnClickListener implements View.OnClickListener {

    private Address itemDO;

    private int position;

    public OnClickListener(Address itemDO) {
      this.itemDO = itemDO;
    }

    public OnClickListener(Address itemDO, int position) {
      this.itemDO = itemDO;
      this.position = position;
    }

    @Override
    public void onClick(View clickView) {
      int viewId = clickView.getId();
      if (viewId == R.id.tv_shipping_address_management_set_to_default_address_btn) {
        setToDefaultAddressBtnClickLogic(itemDO, clickView, position);
      } else if (viewId == R.id.tv_shipping_address_management_address_edit_str) {
        addressEditBtnClickLogic(itemDO, position);
      } else if (viewId == R.id.tv_shipping_address_management_address_delete_btn) {
        addressDeleteBtnClickLogic(itemDO, position);
      }
    }
  }

  /**
   * 选中【设为默认】按钮，设置按钮状态并发送请求
   *
   * @param setToDefaultBtn 选中的 View
   * @param itemDO          View 对应的 item data object
   * @param position        position of the click view
   */
  private void checkBtn(TextView setToDefaultBtn, Address itemDO, int position) {
    CheckBoxUtil.checkCheckBox(setToDefaultBtn);
    mCurrentCheckPos = position;
  }

  /**
   * 选中【设为默认】按钮，只设置按钮状态，不发送请求
   *
   * @param setToDefaultBtn 选中的 View
   * @param position        position of the click view
   */
  private void checkBtn(TextView setToDefaultBtn, int position) {
    checkBtn(setToDefaultBtn, null, position);
  }

  /**
   * 取消选中【设为默认】按钮
   *
   * @param setToDefaultBtn 取消选中的 View
   */
  private void uncheckBtn(TextView setToDefaultBtn) {
    CheckBoxUtil.uncheckCheckBox(setToDefaultBtn);
  }

  /**
   * 点击【设为默认】按钮，发送请求并更改按钮状态
   *
   * @param itemDO    item data object
   * @param clickView view to click
   * @param position  position of the click view
   */
  private void setToDefaultAddressBtnClickLogic(Address itemDO, View clickView, int position) {
    if (position != mCurrentCheckPos) {
      //发送请求改变默认地址
      Address oldDefault = null;
      for (int i = 0; i < mData.size(); i++) {
        Address temp = mData.get(i);
        if ("true".equals(temp.getIsDefault())) {
          oldDefault = temp;
        }
      }
      if (null == oldDefault) {
        setDefaultAddress("", mData.get(position).getId(),itemDO,clickView,position);
      } else {
        setDefaultAddress(oldDefault.getId(), mData.get(position).getId(),itemDO,clickView,position);
      }
    }
  }

  // 设置默认地址
  private void setDefaultAddress(final String oldId, final String newId,final Address itemDO,final View clickView, final int position) {

    DialogUtil.showLoadingDialog(activity,"设置默认中...");
    String delAddressurlPath = activity.getString(R.string.serverbaseurl) + activity.getString(R.string.updateIsDefaul);

    StringRequest delAddressRequest = new StringRequest(Request.Method.POST, delAddressurlPath, new Response.Listener<String>() {

      public void onResponse(String result) {
        if (result != null) {
          try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getBoolean("successful")) {
              ToastUtil.showToast(jsonObject.getString("message"));
              // 必须先取消选中上一个选中的 View，才能执行选中当前 View 的操作
              TextView currentClickItemBtn = mGetChildAtPositionImpl.getChildAtPosition(mCurrentCheckPos);
              if (currentClickItemBtn != null) {
                uncheckBtn(currentClickItemBtn);
              }
              checkBtn((TextView) clickView, itemDO, position);
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
          DialogUtil.hideLoadingDialog();
        }
      }
    }, new Response.ErrorListener() {

      public void onErrorResponse(VolleyError arg0) {
        DialogUtil.hideLoadingDialog();
        ToastUtil.showToast(R.string.notice_networkerror);
      }
    }) {
      @Override
      protected Map<String, String> getParams() throws AuthFailureError {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("oldId", oldId);
        map.put("newId", newId);
        return map;
      }
    };
    PulamsiApplication.requestQueue.add(delAddressRequest);
  }

  /**
   * 【修改地址】按钮点击逻辑，跳转到【修改地址】页面，并带上需要修改的数据对象及索引
   *
   * @param itemDO   item data object
   * @param position clickPosition of the item data object
   */
  private void addressEditBtnClickLogic(Address itemDO, int position) {
    Intent intent = new Intent(activity, AddAddressActivity.class);
    Bundle bundle = new Bundle();
    bundle.putSerializable("address",itemDO);
    intent.putExtras(bundle);
    activity.startActivity(intent);
  }


  public interface DeleAddressCallback{
    public abstract void deleAddressPosition(int position);
  }
  private DeleAddressCallback deleAddressCallback;

  public void setDeleAddressCallback(DeleAddressCallback deleAddressCallback) {
    this.deleAddressCallback = deleAddressCallback;
  }

  /**
   * 【删除】按钮点击逻辑
   *
   * @param position clickPosition of the item data object
   */
  private void addressDeleteBtnClickLogic(final Address address, final int position) {
    CommonAlertDialog alertDialog = new CommonAlertDialog(activity, "确定要删除?", "确定", "取消", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
      }
    }, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DialogUtil.showLoadingDialog(activity,"删除中...");
        deleteAddress(address, position);
      }
    });
    alertDialog.show();

  }

  /**
   * 删除收货地址请求网络方法
   * @param address  地址对象
   * @param position  选中的下标
   */
  private void deleteAddress(final Address address,final int position){
    String delAddressurlPath = activity.getString(R.string.serverbaseurl) + activity.getString(R.string.delAddress);

    StringRequest delAddressRequest = new StringRequest(Request.Method.POST, delAddressurlPath, new Response.Listener<String>() {

      public void onResponse(String result) {
        if (result != null) {
          mData.remove(address);
          if (mData.size()==0){//删除后收货地址为空
            ((AddressListActivity) mActivity).showBlankLayout();
          }
          notifyItemRemoved(position);
          ToastUtil.showToast(R.string.shipping_address_deleteOKmsg);
          if (null != deleAddressCallback){
            deleAddressCallback.deleAddressPosition(position);
          }
        }
        DialogUtil.hideLoadingDialog();
      }
    }, new Response.ErrorListener() {

      public void onErrorResponse(VolleyError arg0) {
        DialogUtil.hideLoadingDialog();
        ToastUtil.showToast(R.string.notice_networkerror);
      }
    }) {
      @Override
      protected Map<String, String> getParams() throws AuthFailureError {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("addressId", address.getId());
        return map;
      }
    };
    PulamsiApplication.requestQueue.add(delAddressRequest);
  }


}
