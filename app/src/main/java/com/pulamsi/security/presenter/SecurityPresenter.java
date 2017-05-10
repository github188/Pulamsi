package com.pulamsi.security.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lidroid.xutils.util.LogUtils;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.security.model.CodeSecurityBean;
import com.pulamsi.security.view.ISecurityView;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.SweetAlert.SweetAlertDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tr.xip.errorview.ErrorView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-04-22
 * Time: 17:47
 * FIXME
 */
public class SecurityPresenter implements ErrorView.RetryListener {

    private ISecurityView iSecurityView;
    String code;
    Context context;
    String secretCode;
    String phone;


    public SecurityPresenter(ISecurityView iSecurityView) {
        this.iSecurityView = iSecurityView;
    }

    public void init() {
        iSecurityView.showLoading();
        code = iSecurityView.getCode();
        context = iSecurityView.getContext();
        initData();
    }

    private void initData() {
        requestCode();
    }

    public void requestCode() {
        String url = context.getString(R.string.serverbaseurl) + context.getString(R.string.security_code_validation);
        StringRequest codeRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        CodeSecurityBean codeSecurityBean = gson.fromJson(result, CodeSecurityBean.class);
                        if (!(TextUtils.isEmpty(codeSecurityBean.getCode()))){
                            SetDate(codeSecurityBean);
                        }else {
                            iSecurityView.showFailure();
                        }
                        iSecurityView.showComplete();
                    } catch (Exception e) {
                        LogUtils.e("防伪验证装配出错");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                iSecurityView.showError(SecurityPresenter.this);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("code",code);
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(codeRequest);
    }

    private void SetDate(CodeSecurityBean codeSecurityBean) {
        if (codeSecurityBean == null)
            return;
        iSecurityView.showSuccess();
        iSecurityView.setqueryCount(codeSecurityBean.getQueryCount() + "");
        iSecurityView.setfirstQueryDate(codeSecurityBean.getFirstQueryDate());
    }

    @Override
    public void onRetry() {
        iSecurityView.showLoading();
        requestCode();
    }


    public void requestIntegral() {
        String url = context.getString(R.string.serverbaseurl) + context.getString(R.string.security_secret_validation);
        StringRequest IntegralRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String message = jsonObject.getString("message");
                        if (jsonObject.getBoolean("successful")) {
                            iSecurityView.showDialogMessage(SweetAlertDialog.SUCCESS_TYPE, "积分成功", message);
                        } else {
                            iSecurityView.showDialogMessage(SweetAlertDialog.ERROR_TYPE, "积分失败", message);
                        }
                    } catch (Exception e) {
                        LogUtils.e("防伪验证装配出错");
                    }
                }
                DialogUtil.hideLoadingDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                DialogUtil.hideLoadingDialog();
                ToastUtil.showToast(R.string.notice_networkerror);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("code", code);
                map.put("codePwd", secretCode);
                map.put("mobile", phone);
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(IntegralRequest);
    }

    public void isRegistered() {
        secretCode = iSecurityView.getSecretCode();
        phone = iSecurityView.getPhone();

        DialogUtil.showLoadingDialog(context, context.getResources().getString(R.string.LOADING));
        // 发送网络请求先判断用户是否已经注册过
        String urlPath = context.getString(R.string.serverbaseurl) + context.getString(R.string.validateMobileurl) + phone;

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                urlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    if (Boolean.parseBoolean(result)) {
                        iSecurityView.showDialogMessage(SweetAlertDialog.ERROR_TYPE, "积分失败", "该手机号码未注册！");
                        DialogUtil.hideLoadingDialog();
                    } else {
                        requestIntegral();
                    }
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError arg0) {
                ToastUtil.showToast(R.string.notice_networkerror);
                DialogUtil.hideLoadingDialog();
            }
        });
        PulamsiApplication.requestQueue.add(stringRequest);
    }
}
