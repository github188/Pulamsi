package com.pulamsi.myinfo.slotmachineManage.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.myinfo.slotmachineManage.RefundFragment;
import com.pulamsi.myinfo.slotmachineManage.entity.RefundBean;
import com.pulamsi.myinfo.slotmachineManage.viewholder.RefundItemViewHolder;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.SweetAlert.SweetAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-04-18
 * Time: 15:02
 * FIXME
 */
public class RefundManagementAdapter extends HaiBaseListAdapter<RefundBean> {

    private Activity activity;
    private RefundFragment refundFragment;
    private final int STATE_PENDING = 0;//待处理
    private final int STATE_REFUND_SUCCESS = 1;//退款成功
    private final int STATE_REFUND_FAILURE = 3;//退款失败
    private String refundType;
    SweetAlertDialog pDialog;
    SweetAlertDialog sDialog;



    public RefundManagementAdapter(Activity activity, RefundFragment refundFragment) {
        super(activity);
        this.activity = activity;
        this.refundFragment = refundFragment;
        refundType = refundFragment.getArguments().getString("type");

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.refund_item, null);
        if (convertView == null)
            return null;
        return new RefundItemViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null || !(holder instanceof RefundItemViewHolder) || !(getItem(position) instanceof RefundBean))
            return;

        RefundItemViewHolder mHolder = (RefundItemViewHolder) holder;
        final RefundBean mRefundBean  = getItem(position);
        final Integer auditState = mRefundBean.getAuditState();
        mHolder.serialNumber.setText(mRefundBean.getRefundSn());
        switch (auditState) {
            case STATE_PENDING:
                mHolder.refundState.setText("待处理");
                break;
            case STATE_REFUND_SUCCESS:
                mHolder.refundState.setText("退款成功");
                break;
            case STATE_REFUND_FAILURE:
                mHolder.refundState.setText("退款失败");
                break;
        }


        if ("微信支付".equals(mRefundBean.getPaymentConfigName())) {
            mHolder.payment.setText("微信支付");
        } else if ("支付宝支付".equals(mRefundBean.getPaymentConfigName())) {
            mHolder.payment.setText("支付宝支付");
        }

        mHolder.mobilePhone.setText(mRefundBean.getMobilePhone());
        mHolder.refundAmount.setText(mRefundBean.getTotalAmount() + "");
        mHolder.createDate.setText("创建日期:" + mRefundBean.getCreateDate());
        mHolder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auditState == STATE_REFUND_SUCCESS) {//判断是否已经退款
                    new SweetAlertDialog(mActivity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("该订单已退款，无需再次申请!")
                            .setConfirmText("       确定      ")
                            .showCancelButton(false)
                            .setCancelClickListener(null)
                            .setConfirmClickListener(null)
                            .show();
                } else {
                    ShowDialogMessage(mRefundBean);
                }
            }
        });
    }

    /**
     * 对话框
     */
    private void ShowDialogMessage(final RefundBean mRefundBean) {
        sDialog = new SweetAlertDialog(mActivity, SweetAlertDialog.WARNING_TYPE);
        sDialog.setTitleText("您确定要退款吗?")
                .setCancelText("         取消      ")
                .setConfirmText("       确定       ")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.setTitleText("取消操作!")
                                .setConfirmText("       返回      ")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        //支付宝未完善
                        if (refundType.equals(RefundFragment.TYPE_ALIPAY)) {
                            ToastUtil.showToast("支付宝退款尚未完善");
                            return;
                        }

                        pDialog = new SweetAlertDialog(mActivity, SweetAlertDialog.PROGRESS_TYPE)
                                .setTitleText("Loading...");
                        pDialog.getProgressHelper().setBarColor(mActivity.getResources().getColor(R.color.app_pulamsi_main_color));
                        pDialog.show();
                        pDialog.setCancelable(false);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                refundSubmit(mRefundBean);//退款
                            }
                        }, 1000);
                    }
                })
                .show();
            }

    private void refundSubmit(final RefundBean mRefundBean) {
        if (mRefundBean == null)
            return;
        String url = mActivity.getResources().getString(R.string.serverbaseurl) + mActivity.getResources().getString(R.string.weChatRefund);
        StringRequest refundSubmitRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null){
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("successful")) {
                            refundSuccess();
                        }else {
                            refundFailure();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        refundFailure();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                refundFailure();//退款失败
                Toast.makeText(mActivity, R.string.notice_networkerror, Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("refundId", mRefundBean.getId());
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(refundSubmitRequest);
    }

    /**
     * 退款成功
     */
    private void refundSuccess() {
        pDialog.setTitleText("退款成功!")
                .setConfirmText("   确定  ")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        refundFragment.refreshList();
                    }
                })
                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
    }

    /**
     * 退款失败
     */
    private void refundFailure() {
        pDialog.setTitleText("退款失败!")
                .setConfirmText("   确定  ")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        refundFragment.refreshList();
                    }
                })
                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
    }
}
