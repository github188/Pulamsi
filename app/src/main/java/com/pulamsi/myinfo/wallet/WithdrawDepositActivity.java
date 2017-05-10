package com.pulamsi.myinfo.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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
import com.google.gson.reflect.TypeToken;
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
import java.util.List;
import java.util.Map;


/**
 * 提现界面
 */
public class WithdrawDepositActivity extends BaseActivity {

  //账号总余额
  private float memberAccountBalance;
  //金额输入框
  private EditText moneyedit;
  //选择银行卡
  private TextView bankCardedit;
  private MemberAccountCashBankInfo CashBank;
  //下一步按钮
  private TextView nextBtn;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    BaseAppManager.getInstance().addActivity(this);
    initData();
    setContentView(R.layout.wallet_withdrawdeposit_activity);
    memberAccountBalance =  getIntent().getFloatExtra("memberAccountBalance",0.0f);
    initUI();
  }

  //获取银行卡列表
  private void initData() {
    String bankInfoList = getString(R.string.serverbaseurl) + getString(R.string.bankInfoList);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, bankInfoList, new Response.Listener<String>() {

      public void onResponse(String result) {
        if (result != null) {
          try {
            Gson gson = new Gson();
            List<MemberAccountCashBankInfo> bankInfos = gson.fromJson(result, new TypeToken<List<MemberAccountCashBankInfo>>() {}.getType());
            if (null != bankInfos && bankInfos.size() > 0){
              for (int i = 0; i < bankInfos.size(); i++) {
                MemberAccountCashBankInfo accountCashBankInfo = bankInfos.get(i);
                if (accountCashBankInfo.getIsDefault()) {
                  CashBank = accountCashBankInfo;
                  break;
                }
              }
              if (null == CashBank) {
                CashBank = bankInfos.get(0);
              }
              bankCardedit.setHint(bankInfos.get(0).getBankName() + "(" + bankInfos.get(0).getTailID() + ")  " + bankInfos.get(0).getBankAccountName());
            }
          } catch (Exception e) {
            HaiLog.e("pulamsi", "钱包数据装配出错");
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
        return map;
      }
    };
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
    PulamsiApplication.requestQueue.add(stringRequest);
  }

  private void initUI() {
    setHeaderTitle(R.string.my_info_wallet_withdrawdeposit_title);

    moneyedit = (EditText) findViewById(R.id.wallet_withdrawdeposit_money_edit);
    moneyedit.setHint("当前余额" + memberAccountBalance + "元");
    bankCardedit = (TextView) findViewById(R.id.wallet_withdrawdeposit_bankcard_edit);
    nextBtn = (TextView) findViewById(R.id.wallet_withdrawdeposit_btn);
    moneyedit.addTextChangedListener(new TextWatcher() {

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().contains(".")) {
          if (s.length() - 1 - s.toString().indexOf(".") > 2) {
            s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
            moneyedit.setText(s);
            moneyedit.setSelection(s.length());
          }
        }
        if (s.toString().trim().substring(0).equals(".")) {
          s = "0" + s;
          moneyedit.setText(s);
          moneyedit.setSelection(2);
        }
        if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
          if (!s.toString().substring(1, 2).equals(".")) {
            moneyedit.setText(s.subSequence(0, 1));
            moneyedit.setSelection(1);
            return;
          }
        }
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void afterTextChanged(Editable arg0) {
      }
    });
    //选择银行卡
    bankCardedit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent bankinfo = new Intent(WithdrawDepositActivity.this, BankinfoListActivity.class);
        bankinfo.putExtra("iscallback", true);
        startActivityForResult(bankinfo, 0);
      }
    });

    //下一步
    nextBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (assetAnyInputIsNull()) {
          return;
        }
        if (null == CashBank) {
          //跳转新增银行卡界面
          Intent intent = new Intent(WithdrawDepositActivity.this, InputBankInfoActivity.class);
          intent.putExtra("iscallback", false);
          intent.putExtra("money", moneyedit.getEditableText().toString().trim());
          startActivity(intent);
        } else {
          //直接提现
          withdrawdeposit();

        }
      }
    });


  }

  //提现方法
  private void withdrawdeposit() {
    DialogUtil.showLoadingDialog(WithdrawDepositActivity.this,"提现中...");
    String bankExtract = getString(R.string.serverbaseurl) + getString(R.string.bankExtract);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, bankExtract, new Response.Listener<String>() {

      public void onResponse(String result) {
        if (result != null) {
          try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getBoolean("success")) {
              ToastUtil.showToast("提现成功");
              SoftInputUtil.hideSoftInput(WithdrawDepositActivity.this);
              WithdrawDepositActivity.this.finish();
            }
          } catch (JSONException e) {
            e.printStackTrace();
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
        map.put("mid", Constants.userId);
        map.put("banckInfoId", CashBank.getId());
        map.put("cashMoney", moneyedit.getEditableText().toString().trim());
        return map;
      }
    };
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
    PulamsiApplication.requestQueue.add(stringRequest);

  }

  // 选择银行卡回调
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      Bundle bundle = data.getExtras();
      if (bundle.getBoolean("islast")) {
        bankCardedit.setHint("使用新卡提现");
        CashBank = null;
      } else {
        CashBank = (MemberAccountCashBankInfo) bundle.getSerializable("cashBankInfo");
        bankCardedit.setHint(CashBank.getBankName() + "(" + CashBank.getTailID() + ")  " + CashBank.getBankAccountName());
      }
    }
  }

  /**
   * 判断是否存在空的输入项，并弹窗提示
   *
   * @return true 有一个输入项为空，false 全部输入项都不为空
   */
  private boolean assetAnyInputIsNull() {
    if (TextUtils.isEmpty(moneyedit.getText())) {
      ToastUtil.showToast(R.string.my_info_wallet_withdrawdeposit_money_null);
      return true;
    }
    if (!(Float.parseFloat(moneyedit.getEditableText().toString().trim()) <= memberAccountBalance) ){
      ToastUtil.showToast(R.string.my_info_wallet_withdrawdeposit_money_full);
      return true;
    }
    if (Float.parseFloat(moneyedit.getEditableText().toString().trim()) <= 0){
      ToastUtil.showToast(R.string.my_info_wallet_withdrawdeposit_money_not);
      return true;
    }
    return false;
  }

}
