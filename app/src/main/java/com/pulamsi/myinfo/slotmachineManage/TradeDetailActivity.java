package com.pulamsi.myinfo.slotmachineManage;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.myinfo.slotmachineManage.entity.TradeBean;


/**
 * 交易详情界面
 */
public class TradeDetailActivity extends BaseActivity {
  private TradeBean tradeBean;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    tradeBean= (TradeBean) getIntent().getSerializableExtra("tradeBean");
    setContentView(R.layout.slotmachine_manage_tradedetail);
    initUI();
  }

  private void initUI() {
    setHeaderTitle(R.string.slotmachine_manage_tradedetail_title);

    TextView id = (TextView) findViewById(R.id.slotmachine_manage_tradedetail_id);
    id.setText(tradeBean.getId());


    TextView liushuiid = (TextView) findViewById(R.id.slotmachine_manage_tradedetail_liushuiid);
    liushuiid.setText(tradeBean.getLiushuiid());

    TextView orderid = (TextView) findViewById(R.id.slotmachine_manage_tradedetail_orderid);
    orderid.setText(tradeBean.getOrderid());

    TextView cardinfo = (TextView) findViewById(R.id.slotmachine_manage_tradedetail_cardinfo);
    cardinfo.setText(tradeBean.getCardinfo());

    TextView price = (TextView) findViewById(R.id.slotmachine_manage_tradedetail_price);
    price.setText(Double.parseDouble(tradeBean.getPrice())/100 +"");

    TextView receicetime = (TextView) findViewById(R.id.slotmachine_manage_tradedetail_receicetime);
    receicetime.setText(tradeBean.getReceivetime());

    TextView tradetype = (TextView) findViewById(R.id.slotmachine_manage_tradedetail_tradetype);
    switch (Integer.parseInt(tradeBean.getTradetype())){
      case 100:
        tradetype.setText("全部");
        break;
      case 4:
        tradetype.setText("支付宝扫码");
        break;
      case 6:
        tradetype.setText("微信扫码");
        break;
      case 7:
        tradetype.setText("取货码");
        break;
    }


    TextView goodmachineid = (TextView) findViewById(R.id.slotmachine_manage_tradedetail_goodmachineid);
    goodmachineid.setText(tradeBean.getGoodmachineid());

    TextView innerid = (TextView) findViewById(R.id.slotmachine_manage_tradedetail_innerid);
    innerid.setText(tradeBean.getInneridname());

    TextView goodsname = (TextView) findViewById(R.id.slotmachine_manage_tradedetail_goodsname);
    if (TextUtils.isEmpty(tradeBean.getGoodsName())){
      goodsname.setText(tradeBean.getGoodsName());
    }else {
      goodsname.setText("暂无信息");
    }

    TextView changestatus = (TextView) findViewById(R.id.slotmachine_manage_tradedetail_changestatus);
    changestatus.setText(Integer.parseInt(tradeBean.getChangestatus()) != 0 ?"成功":"失败");

    TextView sendstatus = (TextView) findViewById(R.id.slotmachine_manage_tradedetail_sendstatus);
    sendstatus.setText(Integer.parseInt(tradeBean.getSendstatus()) != 0 ?"成功":"失败");

  }

}
