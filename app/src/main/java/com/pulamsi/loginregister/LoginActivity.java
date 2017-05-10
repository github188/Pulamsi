package com.pulamsi.loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.constant.Constants;
import com.pulamsi.start.init.entity.Role;
import com.pulamsi.start.init.entity.User;
import com.pulamsi.utils.CheckInputUtil;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.RequestDataUtils;
import com.pulamsi.utils.SoftInputUtil;
import com.pulamsi.utils.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录页面
 *
 * @author WilliamChik on 15/8/25.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mLoginPwdVisibleSwitch;

    private EditText mLoginPassword;

    private EditText mLoginPhone;

    private Handler mSoftInputHandler = new Handler();

    /**
     * 密码是否明文显示
     */
    private boolean isPwdVisible = false;

    private User user = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initUI();
        showSoftInput();
    }

    private void initUI() {
        /**
         * 从数据库拿出用户数据，如果存在就把用户名自动填充进去
         */
        try {
            user = PulamsiApplication.dbUtils.findFirst(Selector.from(User.class));

        } catch (DbException e) {
            e.printStackTrace();
        }
        setHeaderTitle(getResources().getString(R.string.login_title_str));

        TextView privatePolicy = (TextView) findViewById(R.id.tv_login_private_policy);
        TextView loginConfirmBtn = (TextView) findViewById(R.id.tv_login_confirm_btn);
        TextView forgotPwdBtn = (TextView) findViewById(R.id.tv_login_forget_password);
        TextView fastLoginBtn = (TextView) findViewById(R.id.tv_login_phone_fast_login);
        mLoginPassword = (EditText) findViewById(R.id.et_login_password);
        mLoginPhone = (EditText) findViewById(R.id.et_login_phone);
        if (null != user) {
            if (!TextUtils.isEmpty(user.getUserName())) {
                mLoginPhone.setText(user.getUserName());
            }
        }
        mLoginPwdVisibleSwitch = (ImageView) findViewById(R.id.iv_login_pwd_visible_switch);
        privatePolicy.setOnClickListener(this);
        loginConfirmBtn.setOnClickListener(this);
        forgotPwdBtn.setOnClickListener(this);
        fastLoginBtn.setOnClickListener(this);
        mLoginPassword.setOnClickListener(this);
        mLoginPwdVisibleSwitch.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.iv_login_pwd_visible_switch) {
            passwordDisplaySwitch();
        } else if (viewId == R.id.tv_login_confirm_btn) {
            login();
        } else if (viewId == R.id.tv_login_forget_password) {
            forgotPwd();
        } else if (viewId == R.id.tv_login_phone_fast_login) {
            fastLogin();
        } else if (viewId == R.id.tv_login_private_policy) {
            openPrivatePolicy();
        }
    }

    /**
     * 打开隐私策略
     */
    private void openPrivatePolicy() {

    }

    /**
     * 快捷注册
     */
    private void fastLogin() {
        Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(register);
    }

    /**
     * 忘记密码
     */
    private void forgotPwd() {
        Intent forgotPwd = new Intent(LoginActivity.this, RegisterActivity.class);
        forgotPwd.putExtra("forgotpwd", true);
        startActivity(forgotPwd);
    }

    /**
     * 登录
     */
    private void login() {
        // 判断输入是否合法
        if (TextUtils.isEmpty(mLoginPhone.getText().toString().trim())) {
            ToastUtil.showToast(R.string.login_phone_invalid_msg);
            return;
        } else if (!CheckInputUtil.checkPassword(mLoginPassword.getText().toString().trim())) {
            ToastUtil.showToast(R.string.login_please_input_password_legal);
            return;
        }
        DialogUtil.showLoadingDialog(LoginActivity.this, "登录中...");

        //请求网络登陆
        String urlPath = getString(R.string.serverbaseurl) + getString(R.string.loginurl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                urlPath, new Response.Listener<String>() {
            public void onResponse(String arg0) {
                try {
                    JSONObject object = new JSONObject(arg0);
                    if (object.getBoolean("success")) {
                        if (null == user) {
                            user = new User();
                        }
                        user.setUserId(object.getString("id"));
                        user.setHasLogin(true);
                        user.setUserName(mLoginPhone.getText().toString().trim());
                        user.setMobile(object.getString("mobile"));
                        PulamsiApplication.dbUtils.update(user);//更新
                        Constants.userId = user.getUserId();
                        // 关闭输入法和弹出框
                        SoftInputUtil.hideSoftInput(LoginActivity.this);
                        getRole(Constants.userId);
                        RequestDataUtils.getinfo(LoginActivity.this);
                    } else {
                        DialogUtil.hideLoadingDialog();
                        ToastUtil.showToast(R.string.login_username_or_pwd_is_not_right_str);
                    }
                } catch (Exception e) {
                    DialogUtil.hideLoadingDialog();
                    ToastUtil.showToast(R.string.notice_networkerror);
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError arg0) {
                DialogUtil.hideLoadingDialog();
                ToastUtil.showToast(R.string.notice_networkerror);
            }
        }) {
            @Override
            protected Map<String, String> getParams()
                    throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("loginname", mLoginPhone.getText().toString().trim());
                map.put("nloginpwd", mLoginPassword.getText().toString().trim());
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(stringRequest);
    }



    /**
     * 密码明文和密文之间的切换
     */
    private void passwordDisplaySwitch() {
        if (mLoginPwdVisibleSwitch == null || mLoginPassword == null) {
            return;
        }

        if (isPwdVisible) {
            isPwdVisible = false;
            mLoginPwdVisibleSwitch.setImageResource(R.drawable.ic_display_password_press);
            mLoginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            isPwdVisible = true;
            mLoginPwdVisibleSwitch.setImageResource(R.drawable.ic_display_password_nor);
            mLoginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }

        // 将光标移动到末尾
        Editable content = mLoginPassword.getText();
        if (content != null) {
            mLoginPassword.setSelection(content.length());
        }
    }

    /**
     * 重新获取前端，需要进行权限控制
     */
    @Override
    public void onResume() {
        super.onResume();
        try {
            user = PulamsiApplication.dbUtils.findFirst(Selector.from(User.class));
            if (!TextUtils.isEmpty(user.getUserName())) {
                mLoginPhone.setText(user.getUserName());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示软键盘
     */
    private void showSoftInput() {
        // 避免界面未加载完成软键盘加载失败，延迟半秒
        mSoftInputHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mLoginPhone.getText().length() == 0) {
                    SoftInputUtil.showSoftInput(mLoginPhone);
                } else if (mLoginPassword.getText().length() == 0) {
                    SoftInputUtil.showSoftInput(mLoginPassword);
                }
            }
        }, 500);
    }

    /**
     * 获取权限的工具类
     *
     * @param userId 用户id
     */
    private void getRole(final String userId) {
        String RoleResourePath = PulamsiApplication.context.getString(R.string.serverbaseurl)
                + PulamsiApplication.context.getString(R.string.getRoleResoure);
        StringRequest RoleResoureRequest = new StringRequest(Request.Method.POST,
                RoleResourePath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        PulamsiApplication.dbUtils.deleteAll(Role.class);
                        Gson gson = new Gson();
                        List<Role> roles = gson.fromJson(result, new TypeToken<List<Role>>() {
                        }.getType());//序列化或者反序列化的时候需要用到TypeToken
                        PulamsiApplication.dbUtils.configAllowTransaction(true);//标示开启事务,这样多个线程操作数据库时就不会出现问题了
                        PulamsiApplication.dbUtils.saveAll(roles);
                        LoginActivity.this.finish();
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "登录权限数据装配出错");
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
                map.put("mid", userId);// 用户ID
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(RoleResoureRequest);
    }

    @Override
    public boolean toggleOverridePendingTransition() {
        return true;
    }

    @Override
    public TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.BOTTOM;
    }
}
