package com.pulamsi.myinfo.setting.presenter;

import android.content.Context;
import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.entity.Feedback;
import com.pulamsi.constant.Constants;
import com.pulamsi.loginregister.LoginActivity;
import com.pulamsi.myinfo.setting.ChangePasswordActivity;
import com.pulamsi.myinfo.setting.entity.Member;
import com.pulamsi.myinfo.setting.view.IChangePasswordView;
import com.pulamsi.pay.wx.simcpux.MD5Util;
import com.pulamsi.start.init.entity.Role;
import com.pulamsi.start.init.entity.User;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-05-19
 * Time: 09:27
 * FIXME
 */
public class ChangePasswordPresenter {
    private IChangePasswordView iChangePasswordView;
    /**
     * 用户对象
     */
    private User user;
    /**
     * 上下文对象
     */
    private Context mContext;
    /**
     * 验证码
     */
    private String resultCode;

    public ChangePasswordPresenter(IChangePasswordView iChangePasswordView, Context context) {
        this.iChangePasswordView = iChangePasswordView;
        mContext = context;
        init();//初始化
    }

    private void init() {
        setPhone();
    }

    /**
     * 设置提示的号码
     */
    private void setPhone() {
        try {
            user = PulamsiApplication.dbUtils.findFirst(Selector.from(User.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (user != null && user.isHasLogin())
            iChangePasswordView.setChangePasswordPhone(user.getMobile());
    }


    /**
     * 发送验证码
     */
    public void sendVerificationCode() {
        String url = mContext.getResources().getString(R.string.serverbaseurl) + mContext.getString(R.string.change_password_send_verification_code);
        StringRequest sendVerificationCodeRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    Gson gson = new Gson();
                    try {
                        Feedback fb = gson.fromJson(result, Feedback.class);
                        if (fb.isSuccessful()) {
                            resultCode = fb.getMessage();
                            iChangePasswordView.startCountDown();
                        } else {
                            ToastUtil.showToast(fb.getMessage());
                        }
                    } catch (Exception e) {
                        LogUtils.e("验证码装载出错");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showToast(R.string.notice_networkerror);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("mid", Constants.userId);
                map.put("key", MD5Util.MD5Encode(user.getMobile() + Constants.SMSkey, "utf-8"));// key
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(sendVerificationCodeRequest);
    }

    /**
     * 提交修改密码
     */
    public void ChangePasswordSubmit() {
        final String PassWord = iChangePasswordView.getPassWord();
        if (judgeResultCode()) {
            DialogUtil.showLoadingDialog(mContext, "提交中...");
            String url = mContext.getResources().getString(R.string.serverbaseurl) + mContext.getString(R.string.change_password_submit);
            StringRequest ChangePasswordSubmitRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String result) {
                    if (result != null) {
                        Gson gson = new Gson();
                        try {
                            Feedback fb = gson.fromJson(result, Feedback.class);
                            if (fb.isSuccessful()) {
                                ToastUtil.showToast(fb.getMessage());
                                clearAccount();//清除数据
                            } else {
                                ToastUtil.showToast(fb.getMessage());
                            }
                        } catch (Exception e) {
                            LogUtils.e("修改密码装载出错");
                        }
                    }
                    DialogUtil.hideLoadingDialog();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    DialogUtil.hideLoadingDialog();
                    ToastUtil.showToast(R.string.notice_networkerror);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("mid", Constants.userId);
                    map.put("password", PassWord);
                    map.put("type", "Android");
                    return map;
                }
            };
            PulamsiApplication.requestQueue.add(ChangePasswordSubmitRequest);
        }
    }

    /**
     * 清除账户数据
     * 重新登录
     */
    private void clearAccount() {
        if (null == user) {
            user = new User();
        }
        user.setHasLogin(false);
        user.setUserId("");
        try {
            PulamsiApplication.dbUtils.update(user);
            PulamsiApplication.dbUtils.deleteAll(Role.class);
            PulamsiApplication.dbUtils.deleteAll(Member.class);
            ToastUtil.showToast("请重新登录");
            Intent loginandregister = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(loginandregister);
            ((ChangePasswordActivity)mContext).finish();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private boolean judgeResultCode() {
        String VerificationCode = iChangePasswordView.getVerificationCode();
        if (!VerificationCode.equals(resultCode)) {
            ToastUtil.showToast("您输入的验证码错误");
            return false;
        }
        return true;
    }
}
