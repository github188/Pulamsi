package com.pulamsi.myinfo.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.wallet.entity.MemberAccount;
import com.pulamsi.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * 钱包界面
 */
public class WalletActivity extends BaseActivity implements View.OnClickListener {

  /**
   * 钱包对象
   */
  private MemberAccount memberAccount;

  //布局对象
  private LinearLayout walletNumberLayout;
  private TextView store,slotmachine,recharge,withdrawDeposit,bankCard,number;

  private ProgressWheel progressWheel;



  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.wallet_activity);
    initUI();
  }

  private void initUI() {
    setHeaderTitle(R.string.my_info_wallet_title);

    walletNumberLayout = (LinearLayout) findViewById(R.id.wallet_number_layout);
    store = (TextView) findViewById(R.id.wallet_store);
    slotmachine = (TextView) findViewById(R.id.wallet_slotmachine);
    recharge = (TextView) findViewById(R.id.wallet_recharge);
    withdrawDeposit = (TextView) findViewById(R.id.wallet_withdrawdeposit);
    bankCard = (TextView) findViewById(R.id.wallet_bankcard);
    number = (TextView) findViewById(R.id.wallet_number);
    progressWheel = (ProgressWheel) findViewById(R.id.wallet_activity_pw);
    walletNumberLayout.setOnClickListener(this);
    store.setOnClickListener(this);
    slotmachine.setOnClickListener(this);
    recharge.setOnClickListener(this);
    withdrawDeposit.setOnClickListener(this);
    bankCard.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.wallet_number_layout:
        //点击余额明细
        Intent all = new Intent(this, BalanceDetailActivity.class);
        all.putExtra(BalanceDetailActivity.KEY_OPEN_PAGE, BalanceDetailFragment.TYPE_ALL);
        startActivity(all);
        break;
      case R.id.wallet_store:
        //佣金明细
        Intent store = new Intent(this, BalanceDetailActivity.class);
        store.putExtra(BalanceDetailActivity.KEY_OPEN_PAGE, BalanceDetailFragment.TYPE_STORE);
        startActivity(store);
        break;
      case R.id.wallet_slotmachine:
        //售货机售卖明细
        Intent slotmachine = new Intent(this, BalanceDetailActivity.class);
        slotmachine.putExtra(BalanceDetailActivity.KEY_OPEN_PAGE, BalanceDetailFragment.TYPE_SLOTMACHINE);
        startActivity(slotmachine);
        break;
      case R.id.wallet_recharge:
        //充值
        break;
      case R.id.wallet_withdrawdeposit:
        //提现
        Intent withdrawDeposit = new Intent(this, WithdrawDepositActivity.class);
        withdrawDeposit.putExtra("memberAccountBalance",memberAccount.getMemberAccountBalance());
        startActivity(withdrawDeposit);
        break;
      case R.id.wallet_bankcard:
        //银行卡
        Intent bankinfo = new Intent(this, BankinfoListActivity.class);
        startActivity(bankinfo);
        break;
    }

  }


  private void getData() {
    String showAccount = getString(R.string.serverbaseurl) + getString(R.string.showAccount);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, showAccount, new Response.Listener<String>() {

      public void onResponse(String result) {
        if (result != null) {
          try {
            Gson gson = new Gson();
            memberAccount = gson.fromJson(result, MemberAccount.class);
            number.setText(memberAccount.getMemberAccountBalance() + "");
            progressWheel.setVisibility(View.GONE);
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

  @Override
  protected void onResume() {
    super.onResume();
    getData();
  }

  @Override
  public boolean toggleOverridePendingTransition() {
    return true;
  }

  @Override
  public TransitionMode getOverridePendingTransitionMode() {
    return TransitionMode.RIGHT;
  }
}
