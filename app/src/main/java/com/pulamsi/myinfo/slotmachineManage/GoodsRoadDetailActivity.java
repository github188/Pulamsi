package com.pulamsi.myinfo.slotmachineManage;

import android.os.Bundle;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.myinfo.slotmachineManage.entity.PortBean;


/**
 * 货道详情界面
 */
public class GoodsRoadDetailActivity extends BaseActivity {
  private  PortBean portBean;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    portBean= (PortBean) getIntent().getSerializableExtra("portBean");
    setContentView(R.layout.slotmachine_manage_goodsroaddetail);
    initUI();
  }

  private void initUI() {
    setHeaderTitle((getString(R.string.slotmachine_manage_goodsroaddetail_title)) + "(" +portBean.getInneridname() +")");

    TextView cargoNum = (TextView) findViewById(R.id.slotmachine_manage_goodsroaddetail_cargoNum);
    cargoNum.setText(portBean.getMachineid());


    TextView num = (TextView) findViewById(R.id.slotmachine_manage_goodsroaddetail_num);
    num.setText(portBean.getInneridname());

    TextView goodsname = (TextView) findViewById(R.id.slotmachine_manage_goodsroaddetail_goodsname);
    goodsname.setText(portBean.getGoodroadname());

    TextView goodsnum = (TextView) findViewById(R.id.slotmachine_manage_goodsroaddetail_goodsnum);
    goodsnum.setText(portBean.getAmount());


    TextView goodscapacity = (TextView) findViewById(R.id.slotmachine_manage_goodsroaddetail_goodscapacity);
    goodscapacity.setText(portBean.getCapacity());


    TextView goodsprice = (TextView) findViewById(R.id.slotmachine_manage_goodsroaddetail_goodsprice);
    if (portBean.getDiscountPrice() != 0){
      goodsprice.setText(portBean.getDiscountPrice()/100 + "");
    }else {
      goodsprice.setText("");
    }

    TextView goodsdiscountPrice = (TextView) findViewById(R.id.slotmachine_manage_goodsroaddetail_goodsdiscountPrice);
    goodsdiscountPrice.setText(portBean.getPrice()/100 + "");


    TextView updatetime = (TextView) findViewById(R.id.slotmachine_manage_goodsroaddetail_updatetime);
    updatetime.setText(portBean.getUpdatetime());

    TextView faultinfo = (TextView) findViewById(R.id.slotmachine_manage_goodsroaddetail_faultinfo);
    faultinfo.setText(portBean.getErrorinfo());

    TextView faulttime = (TextView) findViewById(R.id.slotmachine_manage_goodsroaddetail_faulttime);
    faulttime.setText(portBean.getLastErrorTime());
  }

}
