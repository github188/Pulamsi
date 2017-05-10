package com.pulamsi.myinfo.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.pulamsi.R;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.order.entity.Order;
import com.pulamsi.pay.alipay.Alipay;
import com.pulamsi.pay.wx.WxPay;
import com.pulamsi.pay.wx.simcpux.Util;
import com.pulamsi.utils.ToastUtil;

/**
 * 选择支付方式界面
 */
public class ChoicePayTypeActivity extends BaseActivity implements View.OnClickListener {

  private Order order;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Constants.choicePayTypeActivity = this;
    BaseAppManager.getInstance().addActivity(this);
    setContentView(R.layout.pay_choice_payment_type);
    WxPay.getInstance().initWxPay(ChoicePayTypeActivity.this);
    initDatas();
    initUI();
  }

  private void initDatas() {
    if (getIntent() != null) {
      order = (Order) getIntent().getSerializableExtra("order");
    }
  }

  private void initUI() {
    setHeaderTitle(R.string.pay_choice);
    RelativeLayout alipay = (RelativeLayout) findViewById(R.id.pay_choice_type_alipay_layout);
    RelativeLayout wechat = (RelativeLayout) findViewById(R.id.pay_choice_type_wechat_layout);
    alipay.setOnClickListener(this);
    wechat.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.pay_choice_type_alipay_layout:
        //支付宝支付
        try {
          Alipay.getInstance().alipay(ChoicePayTypeActivity.this, "", order.getProductTotalPrice() +"",order.getOrderSn(), "普兰氏面膜");
        }catch (Exception e){
          ToastUtil.toastShow(ChoicePayTypeActivity.this, "支付失败");
          Intent all = new Intent(com.pulamsi.constant.Constants.choicePayTypeActivity, MyOrderActivity.class);
          all.putExtra(MyOrderActivity.KEY_OPEN_PAGE, MyOrderFragment.TYPE_ALL);
          com.pulamsi.constant.Constants.choicePayTypeActivity.startActivity(all);
          BaseAppManager.getInstance().clear();
        }
        break;
      case R.id.pay_choice_type_wechat_layout:
        //微信支付
        if (Util.isWxInstalled(ChoicePayTypeActivity.this)) {
          WxPay.getInstance().wxPay(order.getProductTotalPrice()+"", order.getProductTotalQuantity(),order.getOrderSn() , "普兰氏面膜");
        } else {
          ToastUtil.toastShow(ChoicePayTypeActivity.this, "请安装微信");
        }
        break;
    }

  }


  @Override
  protected void onStop() {
    super.onStop();
    WxPay.getInstance().dismiss();
  }
}
