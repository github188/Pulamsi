package com.pulamsi.myinfo.slotmachineManage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.slotmachineManage.entity.GoodsInfo;
import com.pulamsi.myinfo.slotmachineManage.entity.VenderBean;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 商品折扣界面
 */
public class DiscountActivity extends BaseActivity implements View.OnClickListener {

  private RelativeLayout rl_discount_way;
  private TextView selectProduct, selectSlotmachine,discountBtn, selectProductexamine, selectSlotmachineexamine,tv_discount_way,tv_discount_suffix;
  private EditText discountedit;
  private ArrayList<VenderBean> venderBeans;
  private ArrayList<GoodsInfo> goodsInfos;
  public ArrayList<Integer> selectProductIndex = null;
  public ArrayList<Integer> selectSlotmachineIndex = null;

  private String leixin;//打折类型



  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.slotmachine_manage_discount);
    initUI();
  }

  private void initUI() {
    setHeaderTitle(R.string.slotmachine_manage_discount_title);

    rl_discount_way = (RelativeLayout) findViewById(R.id.rl_discount_way);
    tv_discount_way = (TextView) findViewById(R.id.tv_discount_way);
    tv_discount_suffix = (TextView) findViewById(R.id.tv_discount_suffix);
    discountedit = (EditText) findViewById(R.id.slotmachine_manage_discount_price);
    selectProduct = (TextView) findViewById(R.id.slotmachine_manage_discount_selectgoods);
    selectSlotmachine = (TextView) findViewById(R.id.slotmachine_manage_discount_selectslotmachine);
    discountBtn = (TextView) findViewById(R.id.slotmachine_manage_discount_btn);
    selectProductexamine = (TextView) findViewById(R.id.slotmachine_manage_discount_selectgoods_look);
    selectSlotmachineexamine = (TextView) findViewById(R.id.slotmachine_manage_discount_selectslotmachine_look);


    discountedit.setOnClickListener(this);
    selectProduct.setOnClickListener(this);
    selectSlotmachine.setOnClickListener(this);
    discountBtn.setOnClickListener(this);
    selectProductexamine.setOnClickListener(this);
    selectSlotmachineexamine.setOnClickListener(this);
    rl_discount_way.setOnClickListener(this);

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.slotmachine_manage_discount_selectgoods:
        //选择商品
        if (null != goodsInfos){
          Intent selectProduct = new Intent(DiscountActivity.this, DiscountSelectProductActivity.class);
          Bundle bundle = new Bundle();
          bundle.putSerializable("goodsInfos", (Serializable) goodsInfos);
          selectProduct.putExtras(bundle);
          startActivityForResult(selectProduct, 1);
        }else {
          getGoodsInfo();
        }
        break;

      case R.id.slotmachine_manage_discount_selectslotmachine:
        //选择售货机
        if (null != venderBeans) {
          Intent selectSlotmachine = new Intent(DiscountActivity.this, DiscountSelectSlotmachineActivity.class);
          Bundle bundle = new Bundle();
          bundle.putSerializable("venderBeans", (Serializable) venderBeans);
          selectSlotmachine.putExtras(bundle);
          startActivityForResult(selectSlotmachine, 2);
        } else {
          getverderList();
        }
        break;

      case R.id.slotmachine_manage_discount_btn:
        //确定打折
        if (null == selectSlotmachine){
          ToastUtil.showToast("请先选择售货机");
          return;
        }
        if (null == selectProductIndex){
          ToastUtil.showToast("请先选择商品");
          return;
        }
        if (null == leixin){
          ToastUtil.showToast("请先选择打折方式");
          return;
        }
        if ("".equals(discountedit.getEditableText().toString().trim())){
          ToastUtil.showToast("请填写折扣值");
          return;
        }

        if (leixin.equals("rate")) {
          if (!(Utils.isInteger(discountedit.getEditableText().toString().trim()))){
            ToastUtil.showToast("请输入整数进行百分比打折");
            return;
          }
          if (Integer.valueOf(discountedit.getEditableText().toString().trim()) >= 100) {
            ToastUtil.showToast("折扣不能超过原价");
            return;
          }
        }
        if (leixin.equals("price")){
          if (Double.valueOf(discountedit.getEditableText().toString().trim()) <= 0){
            ToastUtil.showToast("指定价格应该大于0");
            return;
          }
        }
        //折扣
        discountSend();
        break;

      case R.id.slotmachine_manage_discount_selectgoods_look:
        //查看选择商品
        if (null != selectProductIndex) {
          new AlertDialog.Builder(DiscountActivity.this).setTitle("已选择商品").setItems(getAlertDialogGoodsInfo(), null)
                  .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                    }
                  }).create().show();
        } else {
          ToastUtil.showToast("请先选择商品");
        }
        break;

      case R.id.slotmachine_manage_discount_selectslotmachine_look:
        //查看选择售货机
        if (null != selectSlotmachineIndex) {
          new AlertDialog.Builder(DiscountActivity.this).setTitle("已选择售货机").setItems(getAlertDialogSlotmachine(), null)
                  .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                    }
                  }).create().show();
        } else {
          ToastUtil.showToast("请先选择售货机");
        }
        break;

      case R.id.rl_discount_way:
        showDiscountDialog();
        break;
    }
  }


  /**
   * 弹出打折方式选择框
   */
  private void showDiscountDialog() {
    String[] discount_way = new String[]{"按百分比","指定价格"};
    new AlertDialog.Builder(DiscountActivity.this).setSingleChoiceItems(discount_way, 0, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        switch (which)
        {
          case 0:
            tv_discount_way.setText("按百分比");
            tv_discount_suffix.setText("%");
            leixin = "rate";//按百分比打折
            break;
          case 1:
            tv_discount_way.setText("指定价格");
            tv_discount_suffix.setText("元");
            leixin = "price";//按制定价格打折
            break;
        }
        dialog.dismiss();
      }
    }).create().show();


  }

  //提交打折
  private void discountSend() {
    DialogUtil.showLoadingDialog(DiscountActivity.this,"加载中...");
    String setRebate = getString(R.string.serverbaseurl) + getString(R.string.setRebate);

    StringRequest stringRequest = new StringRequest(Request.Method.POST, setRebate, new Response.Listener<String>() {

      public void onResponse(String result) {
        if (result != null) {
          try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getBoolean("success")) {
              ToastUtil.showToast(jsonObject.getString("message"));
              DiscountActivity.this.finish();
            }
          } catch (JSONException e1) {
            Log.e("pulamsi", "打折数据装配出错");
            e1.printStackTrace();
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
        map.put("goodsIds", getGoodsInfoParameters());
        map.put("venderIds", getSlotmachineParameters());
        map.put("rate", discountedit.getEditableText().toString().trim());
        map.put("leixin", leixin);//判断按什么方式打折
        return map;
      }
    };
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
    PulamsiApplication.requestQueue.add(stringRequest);
  }

  //获取售货机商品的方法
  private void getGoodsInfo() {
    DialogUtil.showLoadingDialog(DiscountActivity.this, "加载中...");
    String getGoodsList = getString(R.string.serverbaseurl) + getString(R.string.getGoodsList);
    StringRequest stringRequest = new StringRequest(Request.Method.GET, getGoodsList, new Response.Listener<String>() {

      public void onResponse(String result) {
        if (result != null) {
          try {
            Gson gson = new Gson();
            goodsInfos = gson.fromJson(result, new TypeToken<List<GoodsInfo>>() {
            }.getType());
            Intent selectProduct = new Intent(DiscountActivity.this, DiscountSelectProductActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("goodsInfos", (Serializable) goodsInfos);
            selectProduct.putExtras(bundle);
            startActivityForResult(selectProduct, 1);
          } catch (Exception e) {
            HaiLog.e("pulamsi", "打折商品数据装配出错");
          }
          DialogUtil.hideLoadingDialog();
        }
      }
    }, new Response.ErrorListener() {

      public void onErrorResponse(VolleyError arg0) {
        DialogUtil.hideLoadingDialog();
        ToastUtil.showToast(R.string.notice_networkerror);
      }
    });
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
    PulamsiApplication.requestQueue.add(stringRequest);
  }



  //选择售货机方法
  private void getverderList() {
    DialogUtil.showLoadingDialog(DiscountActivity.this,"加载中...");
    String getVenderList = getString(R.string.serverbaseurl) + getString(R.string.getVenderListurl) + "?mid="
            + Constants.userId;
    StringRequest stringRequest = new StringRequest(Request.Method.GET, getVenderList, new Response.Listener<String>() {

      public void onResponse(String result) {
        if (result != null) {
          try {
            Gson gson = new Gson();
            venderBeans = gson.fromJson(result, new TypeToken<List<VenderBean>>() {
            }.getType());
            Intent selectSlotmachine = new Intent(DiscountActivity.this, DiscountSelectSlotmachineActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("venderBeans", (Serializable) venderBeans);
            selectSlotmachine.putExtras(bundle);
            startActivityForResult(selectSlotmachine, 2);
          } catch (Exception e) {
            HaiLog.e("pulamsi", "商品打折售货机列表数据装配出错");
          }
          DialogUtil.hideLoadingDialog();
        }
      }
    }, new Response.ErrorListener() {

      public void onErrorResponse(VolleyError arg0) {
        DialogUtil.hideLoadingDialog();
        ToastUtil.showToast(R.string.notice_networkerror);
      }
    });
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
    PulamsiApplication.requestQueue.add(stringRequest);
  }

  @SuppressWarnings({ "unchecked" })
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      if (requestCode == 1) {
        // 处理选择产品
        selectProductIndex = (ArrayList<Integer>) data.getSerializableExtra("selectProduct");
        if (null != selectProductIndex && selectProductIndex.size() > 0) {
          selectProduct.setText("已选择产品");
        } else {
          selectProduct.setText("点击选择产品");
          selectProductIndex = null;
        }
      } else if (requestCode == 2) {
        // 处理选择售货机
        selectSlotmachineIndex = (ArrayList<Integer>) data.getSerializableExtra("selectSlotmachine");
        if (null != selectSlotmachineIndex && selectSlotmachineIndex.size() > 0) {
          selectSlotmachine.setText("已选择售货机");
        } else {
          selectSlotmachine.setText("点击选择售货机");
          selectSlotmachineIndex = null;
        }
      }
    }
  }

  // 选择商品弹出框数据
  private String[] getAlertDialogGoodsInfo() {
    String[] str = new String[selectProductIndex.size()];
    for (int i = 0; i < selectProductIndex.size(); i++) {
      str[i] = goodsInfos.get(selectProductIndex.get(i)).getGoodsname();
    }
    return str;
  }

  // 选择售货机弹出框数据
  private String[] getAlertDialogSlotmachine() {
    String[] str = new String[selectSlotmachineIndex.size()];
    for (int i = 0; i < selectSlotmachineIndex.size(); i++) {
      str[i] = venderBeans.get(selectSlotmachineIndex.get(i)).getTerminalName();
    }
    return str;
  }


  // 打折提交商品的参数
  private String getGoodsInfoParameters() {
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < selectProductIndex.size(); i++) {
      buffer.append("," + goodsInfos.get(selectProductIndex.get(i)).getId());
    }
    String temp = buffer.toString();
    return temp.substring(1, temp.length());
  }

  // 打折提交售货机的参数
  private String getSlotmachineParameters() {
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < selectSlotmachineIndex.size(); i++) {
      buffer.append("," + venderBeans.get(selectSlotmachineIndex.get(i)).getId());
    }
    String temp = buffer.toString();
    return temp.substring(1, temp.length());
  }
}
