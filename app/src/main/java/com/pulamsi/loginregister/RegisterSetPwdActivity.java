package com.pulamsi.loginregister;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lidroid.xutils.db.sqlite.Selector;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.start.init.entity.User;
import com.pulamsi.utils.CheckInputUtil;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.SoftInputUtil;
import com.pulamsi.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 注册操作的设置密码页面
 *
 * @author WilliamChik on 15/10/17.
 */
public class RegisterSetPwdActivity extends BaseActivity {

  private EditText mInputPwdEt;

  private EditText mConfirmInputPwdEt;

  private String mRegisterPhone;

  private boolean setnewpwd;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.register_set_pwd_activity);
    BaseAppManager.getInstance().addActivity(this);
    initBundle();
    initUI();
    showSoftInput();
  }

  private void initBundle() {
    if (getIntent() == null) {
      return;
    }
    //获取传过来的参数
    mRegisterPhone  = getIntent().getStringExtra("phone");
    setnewpwd = getIntent().getBooleanExtra("setnewpwd",false);
  }

  private void initUI() {
    setHeaderTitle(R.string.register_set_pwd_title_str);
    mInputPwdEt = (EditText) findViewById(R.id.et_register_pwd);
    mConfirmInputPwdEt = (EditText) findViewById(R.id.et_register_confirm_pwd);
    final TextView registerBtn = (TextView) findViewById(R.id.tv_register_confirm_btn);
    if (setnewpwd){
      registerBtn.setText("设置新密码");
    }
    registerBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        register();
      }
    });
  }

  private void register() {
    // 判断输入是否合法
    if (!CheckInputUtil.checkPassword(mInputPwdEt.getText().toString().trim()) || !CheckInputUtil
            .checkPassword(mConfirmInputPwdEt.getText().toString().trim())) {
      ToastUtil.showToast(R.string.register_password_invalid_msg);
      return;
    } else if (!mInputPwdEt.getText().toString().trim().equals(mConfirmInputPwdEt.getText().toString().trim())) {
      ToastUtil.showToast(R.string.register_password_and_confirm_password_inconsistent);
      return;
    }

    //网络请求
    if (setnewpwd){
      //设置新密码方法
      String productSearchurlPath = getString(R.string.serverbaseurl) + getString(R.string.resetPwd);
      StringRequest productSearchRequest = new StringRequest(Request.Method.POST, productSearchurlPath,
              new Response.Listener<String>() {
                public void onResponse(String result) {
                  if (result != null) {
                    try {
                      JSONObject jsonObject = new JSONObject(result);
                      if (jsonObject.getBoolean("success")) {
                        ToastUtil.showToast(jsonObject.getString("msg"));
                        BaseAppManager.getInstance().clear();
                      } else {
                        ToastUtil.showToast(jsonObject.getString("msg"));
                      }
                    } catch (JSONException e) {
                      e.printStackTrace();
                    }
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
          map.put("username",mRegisterPhone );
          map.put("password", mInputPwdEt.getText().toString().trim());
          return map;
        }
      };
      PulamsiApplication.requestQueue.add(productSearchRequest);

    }else {

      //注册方法
      DialogUtil.showLoadingDialog(RegisterSetPwdActivity.this, "注册中");
      String urlPath = getString(R.string.serverbaseurl)
              + getString(R.string.requestsaveurl);
      StringRequest stringRequest = new StringRequest(Request.Method.POST,
              urlPath, new Response.Listener<String>() {

        public void onResponse(String arg0) {
          try {
            JSONObject jsonObject = new JSONObject(arg0);
            if (jsonObject.getBoolean("success")) {
              ToastUtil.showToast(R.string.register_register_success_msg);
              //删除在集合里面的activity回到登录界面
              User user = PulamsiApplication.dbUtils.findFirst(Selector.from(User.class));
              user.setUserName(mRegisterPhone);
              PulamsiApplication.dbUtils.update(user);
              BaseAppManager.getInstance().clear();
            } else {
              ToastUtil.showToast(jsonObject.getString("message"));
            }
          } catch (Exception e) {
            e.printStackTrace();
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
        protected Map<String, String> getParams()
                throws AuthFailureError {
          HashMap<String, String> map = new HashMap<String, String>();
          map.put("regName", mRegisterPhone);
          map.put("pwd", mInputPwdEt.getText().toString().trim());
          return map;
        }
      };
      stringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
      PulamsiApplication.requestQueue.add(stringRequest);

    }
  }

  /**
   * 展示软键盘
   */
  private void showSoftInput() {
    // 避免界面未加载完成软键盘加载失败，延迟半秒
    new Handler().postDelayed(new Runnable() {

      @Override
      public void run() {
        SoftInputUtil.showSoftInput(mInputPwdEt);
      }
    }, 500);
  }

}
