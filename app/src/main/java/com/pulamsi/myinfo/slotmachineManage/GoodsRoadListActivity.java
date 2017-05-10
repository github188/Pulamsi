package com.pulamsi.myinfo.slotmachineManage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.myinfo.slotmachineManage.adapter.SlotmachineGoodsRoadListAdapter;
import com.pulamsi.myinfo.slotmachineManage.entity.PortBean;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.dialog.CommonAlertDialog;
import com.taobao.uikit.feature.view.TRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 售货机货道列表界面
 */
public class GoodsRoadListActivity extends BaseActivity {
  /**
   * 售货机货道列表
   */
  private TRecyclerView slotmachineManageTRecyclerView;

  private TextView alipay;

  private TextView weixin;

  /**
   * 售货机货道列表适配器
   */
  private SlotmachineGoodsRoadListAdapter slotmachinelistAdapter;

  private ProgressWheel progressWheel;
  private String searchMachineid;
  private ArrayList<PortBean>  portBeans;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.slotmachine_manage_goodsroadlist);
    searchMachineid = getIntent().getStringExtra("searchMachineid");
    initData(searchMachineid);
    initUI();
  }

  private void initUI() {
    setHeaderTitle(getString(R.string.slotmachine_manage_goodsroadlist_title) + "(" +searchMachineid + ")");
    slotmachineManageTRecyclerView = (TRecyclerView) findViewById(R.id.slotmachine_manage_goodsroadlist_trecyclerview);
    progressWheel = (ProgressWheel) findViewById(R.id.pw_slotmachine_manage_goodsroadlist);
    slotmachinelistAdapter = new SlotmachineGoodsRoadListAdapter(this);
    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    slotmachineManageTRecyclerView.setLayoutManager(layoutManager);
    slotmachineManageTRecyclerView.setAdapter(slotmachinelistAdapter);
    slotmachineManageTRecyclerView.setHasFixedSize(true);
    slotmachineManageTRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
      @Override
      public void onItemClick(TRecyclerView parent, View view, int position, long id) {
        Intent goodsroaddetail = new Intent(GoodsRoadListActivity.this, GoodsRoadDetailActivity.class);
        goodsroaddetail.putExtra("portBean", portBeans.get(position));
        GoodsRoadListActivity.this.startActivity(goodsroaddetail);
      }
    });

    alipay = (TextView) findViewById(R.id.slotmachine_manage_goodsroadlist_alipay);
    weixin = (TextView) findViewById(R.id.slotmachine_manage_goodsroadlist_weixin);
    alipay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        CommonAlertDialog alertDialog = new CommonAlertDialog(GoodsRoadListActivity.this, "确定更新支付宝二维码?", "确定", "取消", new View.OnClickListener() {
          @Override
          public void onClick(View v) {
          }
        }, new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            updateAlipay();
          }
        });
        alertDialog.show();
      }
    });
    weixin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        CommonAlertDialog alertDialog = new CommonAlertDialog(GoodsRoadListActivity.this, "确定更新微信二维码?", "确定", "取消", new View.OnClickListener() {
          @Override
          public void onClick(View v) {
          }
        }, new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            updateWeixin();
          }
        });
        alertDialog.show();
      }
    });
  }

  private void updateAlipay() {
    DialogUtil.showLoadingDialog(GoodsRoadListActivity.this,"更新中...");
    String updateAlipay = getString(R.string.serverbaseurl)
            + getString(R.string.updateAlipay) + searchMachineid;
    StringRequest stringRequest = new StringRequest(
            Request.Method.GET, updateAlipay, new Response.Listener<String>() {

      public void onResponse(String result) {
        if (result != null) {
          try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getBoolean("successful")) {
              ToastUtil.showToast(jsonObject.getString("message"));
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
    });
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
    PulamsiApplication.requestQueue.add(stringRequest);
  }

  private void updateWeixin() {
    DialogUtil.showLoadingDialog(GoodsRoadListActivity.this,"更新中...");
    String updateWeixin = getString(R.string.serverbaseurl)
            + getString(R.string.updateWeixin) + searchMachineid;
    StringRequest updateWeixinRequest = new StringRequest(
            Request.Method.GET, updateWeixin, new Response.Listener<String>() {

      public void onResponse(String result) {
        if (result != null) {
          try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getBoolean("successful")) {
              ToastUtil.showToast(jsonObject.getString("message"));
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
    });
    updateWeixinRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
    PulamsiApplication.requestQueue.add(updateWeixinRequest);
  }

  /**
   * 网络请求售货机列表数据
   */
  private void initData(String searchMachineid) {

    String getVenderList = getString(R.string.serverbaseurl)
            + getString(R.string.getPortListurl) + "?searchMachineid=" + searchMachineid;
    StringRequest stringRequest = new StringRequest(Request.Method.GET,
            getVenderList, new Response.Listener<String>() {

      public void onResponse(String result) {
        if (result != null) {
          System.out.println(result);
          Gson gson = new Gson();
          portBeans = gson.fromJson(result,new TypeToken<List<PortBean>>() {}.getType());
          initListData(portBeans);
        }
      }
    }, new Response.ErrorListener() {

      public void onErrorResponse(VolleyError arg0) {
       ToastUtil.showToast(R.string.notice_networkerror);
      }
    });
    PulamsiApplication.requestQueue.add(stringRequest);
  }

  /**
   * 装配售货机菜单数据
   */
  public void initListData(List<PortBean> data) {
    //  状态还原
    int formerItemCount = slotmachinelistAdapter.getItemCount();
    slotmachinelistAdapter.clearDataList();
    slotmachinelistAdapter.notifyItemRangeRemoved(0, formerItemCount);

    slotmachinelistAdapter.addDataList(data);
    slotmachinelistAdapter.notifyItemRangeInserted(0, data.size());
    progressWheel.setVisibility(View.GONE);
    slotmachineManageTRecyclerView.setVisibility(View.VISIBLE);
  }

}
