package com.pulamsi.myinfo.wallet.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.wallet.entity.MemberAccountCashBankInfo;
import com.pulamsi.myinfo.wallet.viewholder.BankinfoViewHolder;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.dialog.CommonAlertDialog;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 银行卡列表适配器
 *
 */
public class BankinfoListAdapter extends HaiBaseListAdapter<MemberAccountCashBankInfo> {

  private Activity activity;
  private boolean isCallBack;


  public BankinfoListAdapter(Activity activity,boolean isCallBack) {
    super(activity);
    this.activity = activity;
    this.isCallBack = isCallBack;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View convertView = mInflater.inflate(R.layout.bankinfolist_item, null);
    if (convertView == null) {
      return null;
    }

    return new BankinfoViewHolder(convertView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    if (holder == null || !(holder instanceof BankinfoViewHolder) || !(getItem(position) instanceof MemberAccountCashBankInfo)) {
      return;
    }

    BankinfoViewHolder newHolder = (BankinfoViewHolder) holder;
    final MemberAccountCashBankInfo newItem = (MemberAccountCashBankInfo) getItem(position);

    newHolder.setDeleAddressCallback(new BankinfoViewHolder.BankinfoCallback() {
      @Override
      public void deleBankinfoPosition() {
        if (isCallBack){
          Intent resultIntent = new Intent();
          Bundle bundle = new Bundle();
          bundle.putSerializable("cashBankInfo", newItem);
          bundle.putBoolean("islast", false);
          resultIntent.putExtras(bundle);
          activity.setResult(Activity.RESULT_OK, resultIntent);
          activity.finish();
        }
      }
    });
    //银行图标
    if ("中国农业银行".equals(newItem.getBankName())){
      newHolder.bankicon.setImageResource(R.drawable.ic_yh_ny);
    }else if ("中国银行".equals(newItem.getBankName())){
      newHolder.bankicon.setImageResource(R.drawable.ic_yh_zg);
    }else if ("中国工商银行".equals(newItem.getBankName())){
      newHolder.bankicon.setImageResource(R.drawable.ic_yh_gs);
    }else if ("中国建设银行".equals(newItem.getBankName())){
      newHolder.bankicon.setImageResource(R.drawable.ic_yh_js);
    }else if ("交通银行".equals(newItem.getBankName())){
      newHolder.bankicon.setImageResource(R.drawable.ic_yh_jt);
    }else {
      newHolder.bankicon.setImageResource(R.drawable.img_place_hold_bg);
    }

    //银行卡号
    if (!TextUtils.isEmpty(newItem.getHideID())){
      newHolder.bankhideid.setText(newItem.getHideID());
    }else {
      newHolder.bankhideid.setText("暂无信息");
    }

    //姓名
    if (!TextUtils.isEmpty(newItem.getBankAccountName())) {
      newHolder.bankaccountname.setText(newItem.getBankAccountName());
    }else {
      newHolder.bankaccountname.setText("暂无信息");
    }

    newHolder.del.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        CommonAlertDialog alertDialog = new CommonAlertDialog(activity, "确定解除绑定?", "确定", "取消", new View.OnClickListener() {
          @Override
          public void onClick(View v) {
          }
        }, new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            delBankinfo(newItem,position);
          }
        });
        alertDialog.show();
      }
    });
  }

  private void delBankinfo(final MemberAccountCashBankInfo bankinfo,final int position) {
    String delBankInfo = activity.getString(R.string.serverbaseurl) + activity.getString(R.string.delBankInfo);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, delBankInfo, new Response.Listener<String>() {

      public void onResponse(String result) {
        if (result != null) {
          ToastUtil.showToast("解除绑定成功");
          mData.remove(bankinfo);
          notifyItemRemoved(position);
          if (null != deleBankinfoCallback){
            deleBankinfoCallback.deleBankinfoPosition(position);
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
        map.put("banckInfoId", bankinfo.getId());
        return map;
      }
    };
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
    PulamsiApplication.requestQueue.add(stringRequest);
  }

  public interface DeleBankinfoCallback{
    public abstract void deleBankinfoPosition(int position);
  }
  private DeleBankinfoCallback deleBankinfoCallback;

  public void setDeleAddressCallback(DeleBankinfoCallback deleBankinfoCallback) {
    this.deleBankinfoCallback = deleBankinfoCallback;
  }
}
