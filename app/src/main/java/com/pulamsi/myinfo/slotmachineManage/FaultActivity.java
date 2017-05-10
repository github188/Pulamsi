package com.pulamsi.myinfo.slotmachineManage;

import android.os.Bundle;
import android.view.View;

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
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.slotmachineManage.adapter.FaultExpandableAdapter;
import com.pulamsi.myinfo.slotmachineManage.entity.Stockout;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.BlankLayout;
import com.pulamsi.views.CustomExpandableListView;

import java.util.ArrayList;
import java.util.List;


/**
 * 故障情况列表界面
 */
public class FaultActivity extends BaseActivity {
  /**
   * 故障情况列表
   */
  private CustomExpandableListView customExpandableListView;

  /**
   * 故障情况适配器
   */
  private FaultExpandableAdapter faultExpandableAdapter;

  /**
   * 加载进度条
   */
  private ProgressWheel progressWheel;

  private ArrayList<Stockout> stockouts;
  /**
   * 为空的时候显示布局
   */
  private BlankLayout mBlankLayout;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.slotmachine_manage_fault);
    initUI();
    getFaultList();
  }

  private void initUI() {
    setHeaderTitle(R.string.slotmachine_manage_fault_title);

    customExpandableListView = (CustomExpandableListView) findViewById(R.id.slotmachine_manage_fault_expandablelistview);
    mBlankLayout = (BlankLayout) findViewById(R.id.blank_layout);
    progressWheel = (ProgressWheel) findViewById(R.id.slotmachine_manage_fault_pw);
  }


  private void getFaultList() {
    String quickerrLookList = getString(R.string.serverbaseurl)
            + getString(R.string.quick_err_lookurl)+ "?mid="
            + Constants.userId;
    StringRequest quickerrLookListRequest = new StringRequest(Request.Method.GET,
            quickerrLookList, new Response.Listener<String>() {

      public void onResponse(String result) {
        if (result != null) {
          try {
            Gson gson = new Gson();
            stockouts = gson.fromJson(result,
                    new TypeToken<List<Stockout>>() {
                    }.getType());

            if (stockouts == null || stockouts.size() == 0) {
              //显示订单列表为空提示
              showBlankLayout();
            } else {
              hideBlankLayout();
             faultExpandableAdapter = new FaultExpandableAdapter(stockouts, FaultActivity.this);
              customExpandableListView.setAdapter(faultExpandableAdapter);
            }

          } catch (Exception e) {
            HaiLog.e("pulamsi", "售货机故障快速浏览数据装配出错");
          }
        }
      }
    }, new Response.ErrorListener() {

      public void onErrorResponse(VolleyError arg0) {
        ToastUtil.showToast(R.string.notice_networkerror);
      }
    }) ;
    quickerrLookListRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
    PulamsiApplication.requestQueue.add(quickerrLookListRequest);
  }


  /** 设置为空和隐藏 **/
  public void hideBlankLayout() {
    mBlankLayout.hideBlankBtn();
    mBlankLayout.setVisibility(View.INVISIBLE);
    progressWheel.setVisibility(View.GONE);
  }

  public void showBlankLayout() {
    mBlankLayout.hideBlankBtn();
    mBlankLayout.setBlankLayoutInfo(R.drawable.ic_space_order, R.string.slotmachine_manage_deallist_empty);
    mBlankLayout.setVisibility(View.VISIBLE);
    progressWheel.setVisibility(View.GONE);
  }


}
