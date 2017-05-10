package com.pulamsi.loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.constant.Constants;
import com.pulamsi.pay.wx.simcpux.MD5Util;
import com.pulamsi.utils.CheckInputUtil;
import com.pulamsi.utils.SoftInputUtil;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.CountdownView;
import com.pulamsi.webview.CommonWebViewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册页面
 *
 * @author WilliamChik  on 2015/8/28.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

  //  发送验证码的 textview
  private CountdownView mSendVericationCodeTv;

  // 手机号的 edittext
  private EditText mRegisterPhoneEt;

  // 验证码的 edittext
  private EditText mRegisterVerificationCodeEt;

  // 验证码错误信息 textview
  private TextView mVerificationErrorMsgTv;

  // 保密和授权协议
  private TextView mAgreement;

  /**
   * 记录返回的短信验证码内容
   */
  private String phoneCode = null;


  private boolean forgotpwd;

  private String username;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    BaseAppManager.getInstance().addActivity(this);
    forgotpwd = getIntent().getBooleanExtra("forgotpwd",false);
    setContentView(R.layout.register_activity);
    initUI();
    showSoftInput();
  }

  private void initUI() {
    if (forgotpwd){
      setHeaderTitle(getResources().getString(R.string.register_forgotpwd));
    }else {
      setHeaderTitle(getResources().getString(R.string.register_title_str));
    }

    mAgreement = (TextView) findViewById(R.id.tv_register_confidentiality_agreement);
    if (forgotpwd){
      mAgreement.setVisibility(View.GONE);
    }
    mVerificationErrorMsgTv = (TextView) findViewById(R.id.tv_register_verification_code_error_message);
    mSendVericationCodeTv = (CountdownView) findViewById(R.id.cdv_register_send_verifacation_code);
    mRegisterPhoneEt = (EditText) findViewById(R.id.et_register_pwd);
    mRegisterVerificationCodeEt = (EditText) findViewById(R.id.et_register_confirm_pwd);
    TextView registerConfirmBtn = (TextView) findViewById(R.id.tv_register_confirm_btn);
    if (forgotpwd){
     registerConfirmBtn.setText("下一步");
    }
    mAgreement.setOnClickListener(this);
    registerConfirmBtn.setOnClickListener(this);
    mSendVericationCodeTv.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    int viewId = v.getId();
    if (viewId == R.id.tv_register_confirm_btn) {
      register();
    } else if (viewId == R.id.cdv_register_send_verifacation_code) {
      sendVericationCode();
    } else if (viewId == R.id.tv_register_confidentiality_agreement) {
      openPrivatePolicy();
    }
  }

  /**
   * 打开保密及授权协议
   */
  private void openPrivatePolicy() {

    Intent privatePolicy = new Intent(RegisterActivity.this, CommonWebViewActivity.class);
    String url = getString(R.string.serverbaseurl) + getString(R.string.registhelp);
    privatePolicy.putExtra(Constants.WEBVIEW_LOAD_URL,url);
    privatePolicy.putExtra(Constants.WEBVIEW_TITLE,"普兰氏用户协议");
    startActivity(privatePolicy);

  }

  /**
   * 注册操作
   */
  private void register() {
    // 判断输入是否合法
    if ("".equals(mRegisterPhoneEt.getText().toString().trim())){
      ToastUtil.showToast(R.string.register_phone_no_input_phone);
      return;
    }
    if (!CheckInputUtil.checkPhoneNum(mRegisterPhoneEt.getText().toString().trim())) {
      ToastUtil.showToast(R.string.register_phone_invalid_msg);
      return;
    } else if (TextUtils.isEmpty(mRegisterVerificationCodeEt.getText().toString().trim())) {
      ToastUtil.showToast(R.string.register_verification_code_hint_str);
      return;
    }

    // 跳转
    if (mRegisterVerificationCodeEt.getText().toString().trim().equals(phoneCode)){
      Intent registerSetPwd = new Intent(RegisterActivity.this,RegisterSetPwdActivity.class);
      if (forgotpwd){
        registerSetPwd.putExtra("setnewpwd",true);
      }
      registerSetPwd.putExtra("phone", username);
      startActivity(registerSetPwd);
    }else {
      mVerificationErrorMsgTv.setVisibility(View.VISIBLE);
      mVerificationErrorMsgTv.setText(R.string.register_verification_code_error_msg);
    }

  }

  /**
   * 发送验证码的操作
   */
  private void sendVericationCode() {
    // 判断验证码是否冻结
    if (mSendVericationCodeTv.isFreezing()) {
      return;
    }
    //判断是否为空
    if ("".equals(mRegisterPhoneEt.getText().toString().trim())){
      ToastUtil.showToast(R.string.register_phone_no_input_phone);
      return;
    }
    // 判断输入是否合法
    if (!CheckInputUtil.checkPhoneNum(mRegisterPhoneEt.getText().toString().trim())) {
      ToastUtil.showToast(R.string.register_phone_invalid_msg);
      return;
    }


    if (forgotpwd){
      //判断用户是否存在
      String urlPath = getString(R.string.serverbaseurl) + getString(R.string.isExistUsername);

      StringRequest stringRequest = new StringRequest(Request.Method.POST,
              urlPath, new Response.Listener<String>() {

        public void onResponse(String arg0) {
          try {
            JSONObject jsonObject = new JSONObject(arg0);
            if (jsonObject.getBoolean("success")) {
              username =mRegisterPhoneEt.getText().toString().trim();
              sendSMS(username);
              // 开始倒数冻结时间
              mSendVericationCodeTv.startCountDown(60);
            } else {
              mVerificationErrorMsgTv.setVisibility(View.VISIBLE);
              mVerificationErrorMsgTv.setText(jsonObject.getString("msg"));
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }, new Response.ErrorListener() {

        public void onErrorResponse(VolleyError arg0) {
          ToastUtil.showToast(R.string.notice_networkerror);
        }
      }){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
          HashMap<String,String> map = new HashMap<>();
          map.put("username",mRegisterPhoneEt.getText().toString().trim());
          return map;
        }
      };
      PulamsiApplication.requestQueue.add(stringRequest);
    }else {
      // 发送网络请求先判断用户是否已经注册过
      String urlPath = getString(R.string.serverbaseurl) + getString(R.string.validateMobileurl) + mRegisterPhoneEt.getText().toString().trim();

      StringRequest stringRequest = new StringRequest(Request.Method.GET,
              urlPath, new Response.Listener<String>() {

        public void onResponse(String arg0) {
          if (Boolean.parseBoolean(arg0)) {
            username =mRegisterPhoneEt.getText().toString().trim();
            sendSMS(username);
            // 开始倒数冻结时间
            mSendVericationCodeTv.startCountDown(60);
          } else {
            mVerificationErrorMsgTv.setVisibility(View.VISIBLE);
            mVerificationErrorMsgTv.setText(R.string.register_phone_has_register_msg);
          }
        }
      }, new Response.ErrorListener() {

        public void onErrorResponse(VolleyError arg0) {
          ToastUtil.showToast(R.string.notice_networkerror);
        }
      });
      PulamsiApplication.requestQueue.add(stringRequest);
    }
  }

  private void sendSMS(final String phone) {
    String urlPath = getString(R.string.serverbaseurl) + getString(R.string.requestsmsurl);

    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPath, new Response.Listener<String>() {

      public void onResponse(String arg0) {
        try {
          JSONObject jsonObject = new JSONObject(arg0);
          if (jsonObject.getBoolean("success")) {
            phoneCode = jsonObject.getString("message");
            HaiLog.d("pulamsi","短信验证码"+phoneCode);
          } else {
            mVerificationErrorMsgTv.setVisibility(View.VISIBLE);
            mVerificationErrorMsgTv.setText(jsonObject.getString("message"));
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {

      public void onErrorResponse(VolleyError arg0) {
      }
    }) {
      @Override
      protected Map<String, String> getParams() throws AuthFailureError {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mobile", phone);// 用户ID
        map.put("key", MD5Util.MD5Encode(phone + Constants.SMSkey, "utf-8"));// key
        return map;
      }
    };
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    PulamsiApplication.requestQueue.add(stringRequest);
  }


  /**
   * 展示软键盘
   */
  private void showSoftInput() {
    // 避免界面未加载完成软键盘加载失败，延迟半秒
    new Handler().postDelayed(new Runnable() {

      @Override
      public void run() {
        SoftInputUtil.showSoftInput(mRegisterPhoneEt);
      }
    }, 500);
  }

}
