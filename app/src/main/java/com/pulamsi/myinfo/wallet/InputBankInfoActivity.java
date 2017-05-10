package com.pulamsi.myinfo.wallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.wallet.entity.MemberAccountCashBankInfo;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.SoftInputUtil;
import com.pulamsi.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 新增银行卡界面
 */
public class InputBankInfoActivity extends BaseActivity {

  private EditText CardNumberedit, nameedit;
  private String money;
  private TextView bankedit,nextBtn;
  private boolean iscallback;
  private String[] bankStr = new String[] { "中国银行", "中国工商银行", "中国建设银行", "中国农业银行", "交通银行" };
  private MemberAccountCashBankInfo CashBank;
  private String bankeditStr = "";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    BaseAppManager.getInstance().addActivity(this);
    setContentView(R.layout.inputbankinfo_activity);
    iscallback = getIntent().getBooleanExtra("iscallback", false);
    if (!iscallback) {
      money = getIntent().getStringExtra("money");
    }
    showSoftInput();
    initUI();
  }

  private void initUI() {
    setHeaderTitle(R.string.my_info_wallet_bankinfo_add_title);

    nextBtn = (TextView) findViewById(R.id.my_info_wallet_bankinfo_add_btn);
    CardNumberedit = (EditText) findViewById(R.id.my_info_wallet_bankinfo_add_cardNumber);
    nameedit = (EditText) findViewById(R.id.my_info_wallet_bankinfo_add_name);
    bankedit = (TextView) findViewById(R.id.my_info_wallet_bankinfo_add_bank);
    bankCardNumAddSpace(CardNumberedit);
    //选择银行
    bankedit.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        new AlertDialog.Builder(InputBankInfoActivity.this).setSingleChoiceItems(bankStr, 0, new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {
            bankedit.setText(bankStr[which]);
            bankeditStr = bankStr[which];
            dialog.dismiss();
          }
        }).create().show();
      }
    });

    //下一步按钮
    nextBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (assetAnyInputIsNull()) {
          return;
        }
        addBankinfo();
      }
    });

  }
  //新增银行卡
  private void addBankinfo() {
    DialogUtil.showLoadingDialog(InputBankInfoActivity.this, "提交中");
    String saveBankInfo = getString(R.string.serverbaseurl) + getString(R.string.saveBankInfo);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, saveBankInfo, new Response.Listener<String>() {

      public void onResponse(String result) {
        if (result != null) {
          try {
            Gson gson = new Gson();
            CashBank = gson.fromJson(result, MemberAccountCashBankInfo.class);
            if (null != CashBank) {
              //是否为新建
              if (iscallback) {
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("CashBank", CashBank);
                resultIntent.putExtras(bundle);
                InputBankInfoActivity.this.setResult(RESULT_OK, resultIntent);
                InputBankInfoActivity.this.finish();
                DialogUtil.hideLoadingDialog();
              } else {
                // 提交提现申请
                putBankExtract();
              }
              SoftInputUtil.hideSoftInput(InputBankInfoActivity.this);
            }
          } catch (Exception e) {
            ToastUtil.showToast(R.string.notice_networkerror);
            HaiLog.e("pulamsi", "钱包数据装配出错");
            DialogUtil.hideLoadingDialog();
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
      protected Map<String, String> getParams() throws AuthFailureError {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mid", Constants.userId);
        map.put("bankAccountName", nameedit.getEditableText().toString().trim());
        map.put("bankName", bankeditStr);
        map.put("bankAccountID", CardNumberedit.getEditableText().toString().trim().replace(" ", ""));
        return map;
      }
    };
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
    PulamsiApplication.requestQueue.add(stringRequest);
  }

  /**
   * 判断是否存在空的输入项，并弹窗提示
   *
   * @return true 有一个输入项为空，false 全部输入项都不为空
   */
  private boolean assetAnyInputIsNull() {
    if (TextUtils.isEmpty(nameedit.getText())) {
      ToastUtil.showToast(R.string.new_shipping_address_user_name_null_hint_str);
      return true;
    }
    if ("".equals(bankeditStr)){
      ToastUtil.showToast(R.string.my_info_wallet_bankinfo_add_bank_null_str);
      return true;
    }
    if (TextUtils.isEmpty(CardNumberedit.getText())) {
      ToastUtil.showToast(R.string.my_info_wallet_bankinfo_add_cardNumber_hint);
      return true;
    }
    String cardNumberStr = CardNumberedit.getEditableText().toString().trim().replace(" ", "");
    if (cardNumberStr.length() != 19) {
      ToastUtil.showToast(R.string.my_info_wallet_bankinfo_add_cardNumber_no_input);
      return true;
    }


    return false;
  }

  // 提交提现申请
  private void putBankExtract() {
    String bankExtract = getString(R.string.serverbaseurl) + getString(R.string.bankExtract);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, bankExtract, new Response.Listener<String>() {

      public void onResponse(String result) {
        if (result != null) {
          try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getBoolean("success")) {
              ToastUtil.showToast("提交成功");
              BaseAppManager.getInstance().clear();
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
        map.put("mid", Constants.userId);
        map.put("banckInfoId", CashBank.getId());
        map.put("cashMoney", money);
        return map;
      }
    };
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
    PulamsiApplication.requestQueue.add(stringRequest);
  }

  /**
   * 银行卡四位加空格
   *
   * @param mEditText
   */
  protected void bankCardNumAddSpace(final EditText mEditText) {
    mEditText.addTextChangedListener(new TextWatcher() {
      int beforeTextLength = 0;
      int onTextLength = 0;
      boolean isChanged = false;

      int location = 0;// 记录光标的位置
      private char[] tempChar;
      private StringBuffer buffer = new StringBuffer();
      int konggeNumberB = 0;

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        beforeTextLength = s.length();
        if (buffer.length() > 0) {
          buffer.delete(0, buffer.length());
        }
        konggeNumberB = 0;
        for (int i = 0; i < s.length(); i++) {
          if (s.charAt(i) == ' ') {
            konggeNumberB++;
          }
        }
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        onTextLength = s.length();
        buffer.append(s.toString());
        if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
          isChanged = false;
          return;
        }
        isChanged = true;
      }

      @Override
      public void afterTextChanged(Editable s) {
        if (isChanged) {
          location = mEditText.getSelectionEnd();
          int index = 0;
          while (index < buffer.length()) {
            if (buffer.charAt(index) == ' ') {
              buffer.deleteCharAt(index);
            } else {
              index++;
            }
          }

          index = 0;
          int konggeNumberC = 0;
          while (index < buffer.length()) {
            if ((index == 4 || index == 9 || index == 14 || index == 19)) {
              buffer.insert(index, ' ');
              konggeNumberC++;
            }
            index++;
          }

          if (konggeNumberC > konggeNumberB) {
            location += (konggeNumberC - konggeNumberB);
          }

          tempChar = new char[buffer.length()];
          buffer.getChars(0, buffer.length(), tempChar, 0);
          String str = buffer.toString();
          if (location > str.length()) {
            location = str.length();
          } else if (location < 0) {
            location = 0;
          }

          mEditText.setText(str);
          Editable etable = mEditText.getText();
          Selection.setSelection(etable, location);
          isChanged = false;
        }
      }
    });
  }

  /**
   * 展示软键盘
   */
  private void showSoftInput() {
    // 避免界面未加载完成软键盘加载失败，延迟半秒
    new Handler().postDelayed(new Runnable() {

      @Override
      public void run() {
        SoftInputUtil.showSoftInput(nameedit);
      }
    }, 500);
  }


}
