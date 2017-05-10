package com.pulamsi.myinfo.slotmachineManage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.myinfo.address.AddAddressActivity;
import com.pulamsi.myinfo.slotmachineManage.entity.AddVender;
import com.pulamsi.pay.wx.simcpux.Util;
import com.pulamsi.slotmachine.entity.Area;
import com.pulamsi.slotmachine.utils.SlotmachineDBhelper;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.SoftInputUtil;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.utils.Utils;
import com.pulamsi.views.androidwheel.OnWheelChangedListener;
import com.pulamsi.views.androidwheel.WheelView;
import com.pulamsi.views.androidwheel.adapters.ArrayWheelAdapter;
import com.taobao.uikit.component.LoopViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;


/**
 * 添加售货机界面
 */
public class AddVenderActivity extends BaseActivity {

  /**
   * android_wheel
   * 省市区
   */
  private WheelView province ;
  private WheelView city ;
  private WheelView district ;
  /**
   * popupWindow
   */
  private PopupWindow popupWindow;
  /**
   * 省市区选择下标
   */
  private Integer provinceSelectedItemId = 0;
  private Integer citySelectedItemId = 0;
  private Integer countySelectedItemId = 0;
  /**
   * 省市区列表数据
   */
  private ArrayList<Area> provinces, citys, countys;
  /**
   * 省市区数据库处理类
   */
  private SlotmachineDBhelper dBhelper;
  /**
   * 选择区域
   */
  private TextView area,errorhint;

  /**
   * 输入框
   */
  private EditText id,sn, terminalName, pos_PWD, SellerTyp, TelNum, jiedao;

  private Button addvenderBtn;

  private boolean ischeckVender = false;

  private AddVender addVender;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.addvender_activity);
    dBhelper  = new SlotmachineDBhelper(this);
    getprovince();
    initUI();
    showSoftInput();
  }

  private void initUI() {
    setHeaderTitle(R.string.slotmachine_manage_addvender_title);
    area = (TextView) findViewById(R.id.address_add_edit_area);

    addvenderBtn = (Button) findViewById(R.id.addvender_btn_add);
    id = (EditText) findViewById(R.id.addvender_edit_id);
    sn = (EditText) findViewById(R.id.addvender_edit_sn);
    terminalName = (EditText) findViewById(R.id.addvender_edit_terminalname);
    pos_PWD = (EditText) findViewById(R.id.addvender_eidt_pass);
    SellerTyp = (EditText) findViewById(R.id.addvender_edit_sellertype);
    TelNum = (EditText) findViewById(R.id.addvender_edit_telnum);
    jiedao = (EditText) findViewById(R.id.addvender_edit_address);
    area = (TextView) findViewById(R.id.addvender_edit_area);
    errorhint = (TextView) findViewById(R.id.addvender_errorhint);

    initandroidwheel();
    area.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        popupWindow.showAtLocation(area.getRootView(), Gravity.CENTER, 0, 0);
      }
    });

    id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!"".equals(id.getText().toString().trim())) {
          if (!hasFocus) {
            venderCheck();
          }
        }
      }
    });
    //隐藏错误提示框
    id.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        errorhint.setVisibility(View.GONE);
      }

      @Override
      public void afterTextChanged(Editable s) {
      }
    });

    addvenderBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (assetAnyInputIsNull()) {
          return;
        }
        bindData();
        addvender();
      }
    });
  }

  //新增售货机方法
  private void addvender() {
    DialogUtil.showLoadingDialog(AddVenderActivity.this,"添加中...");
    String saveurlPath = getString(R.string.serverbaseurl) + getString(R.string.addvender);
    StringRequest saveRequest = new StringRequest(Request.Method.POST, saveurlPath, new Response.Listener<String>() {
      public void onResponse(String result) {
        if (result != null) {
          try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getBoolean("successful")) {
              DialogUtil.hideLoadingDialog();
              // 售货机列表
              Intent slotmachineList = new Intent(AddVenderActivity.this, SlotmachineListActivity.class);
              startActivity(slotmachineList);
              AddVenderActivity.this.finish();
            } else {
              ToastUtil.showToast("添加失败,请重新添加");
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
          DialogUtil.hideLoadingDialog();
        }
      }
    }, new Response.ErrorListener() {

      public void onErrorResponse(VolleyError arg0) {
        ToastUtil.showToast("添加成功");
        ToastUtil.showToast(R.string.notice_networkerror);
      }
    }) {
      @Override
      protected Map<String, String> getParams() throws AuthFailureError {
        return Utils.objToMap(addVender);
      }
    };
    saveRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
    PulamsiApplication.requestQueue.add(saveRequest);
  }

  /**
   * 绑定数据
   */
  private void bindData() {
    addVender = new AddVender();
    addVender.setId(id.getText().toString().trim());
    addVender.setSn(sn.getText().toString().trim());
    addVender.setTerminalName(terminalName.getText().toString().trim());
    addVender.setPos_PWD(pos_PWD.getText().toString().trim());
    addVender.setSellerTyp(SellerTyp.getText().toString().trim());
    addVender.setTelNum(TelNum.getText().toString().trim());
    addVender.setProvinceId(provinces.get(provinceSelectedItemId).getId());
    addVender.setCityId(citys.get(citySelectedItemId).getId());
    addVender.setDistrictId(countys.get(countySelectedItemId).getId());
    addVender.setJiedao(jiedao.getText().toString().trim());
  }


  /**
   * 判断售货机是否存在
   */
  private void venderCheck() {
    String checkIdurlPath = getString(R.string.serverbaseurl) + getString(R.string.vendercheckId)
            + id.getText().toString().trim();
    StringRequest checkIdRequest = new StringRequest(Request.Method.GET, checkIdurlPath, new Response.Listener<String>() {

      public void onResponse(String result) {
        if (result != null) {
          try {
            JSONObject jsonObject = new JSONObject(result);
            if (!jsonObject.getBoolean("successful")) {
              errorhint.setText("该售货机已存在");
              errorhint.setVisibility(View.VISIBLE);
              ischeckVender = false;
            }else {
              ischeckVender = true;
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }
    }, new Response.ErrorListener() {

      public void onErrorResponse(VolleyError arg0) {
        ToastUtil.showToast(R.string.notice_networkerror);
      }
    });
    PulamsiApplication.requestQueue.add(checkIdRequest);
  }

  private void getprovince() {
    provinces =  dBhelper.getProvince();
  }

  private void initandroidwheel(){
    LayoutInflater localinflater = (LayoutInflater) AddVenderActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    LinearLayout view = (LinearLayout) localinflater.inflate(R.layout.androidwheellayout, null);
    popupWindow = new PopupWindow(view, LoopViewPager.LayoutParams.MATCH_PARENT, LoopViewPager.LayoutParams.MATCH_PARENT);
    popupWindow.setFocusable(true);
    TextView right = (TextView) view.findViewById(R.id.selectpoplayout_right_button);
    TextView left = (TextView) view.findViewById(R.id.selectpoplayout_left_button);
    province = (WheelView) view.findViewById(R.id.android_wheel_province);
    city = (WheelView) view.findViewById(R.id.android_wheel_city);
    district = (WheelView) view.findViewById(R.id.android_wheel_district);
    province.addChangingListener(new OnWheelChangedListener() {
      @Override
      public void onChanged(WheelView wheel, int oldValue, int newValue) {
        updateCities();
      }
    });
    city.addChangingListener(new OnWheelChangedListener() {
      @Override
      public void onChanged(WheelView wheel, int oldValue, int newValue) {
        updateAreas();
      }
    });
    district.addChangingListener(new OnWheelChangedListener() {
      @Override
      public void onChanged(WheelView wheel, int oldValue, int newValue) {
        countySelectedItemId = district.getCurrentItem();
      }
    });
    left.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        popupWindow.dismiss();
      }
    });
    right.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        area.setText(provinces.get(provinceSelectedItemId).getName() + "-" + citys.get(citySelectedItemId).getName() + "-"
                + countys.get(countySelectedItemId).getName());
        popupWindow.dismiss();
      }
    });
    province.setViewAdapter(new ArrayWheelAdapter<String>(AddVenderActivity.this, Utils.stringforListarray(provinces)));
    province.setVisibleItems(7);
    city.setVisibleItems(7);
    district.setVisibleItems(7);
    updateCities();
    updateAreas();


  }
  private void updateAreas() {
    citySelectedItemId = city.getCurrentItem();
    countys = dBhelper.getDistrict(citys.get(citySelectedItemId).getId());

    district.setViewAdapter(new ArrayWheelAdapter<String>(this, Utils.stringforListarray(countys)));
    district.setCurrentItem(0);

  }

  private void updateCities() {
    provinceSelectedItemId = province.getCurrentItem();
    citys = dBhelper.getCity(provinces.get(provinceSelectedItemId).getId());
    city.setViewAdapter(new ArrayWheelAdapter<String>(this, Utils.stringforListarray(citys)));
    city.setCurrentItem(0);
    updateAreas();
  }

  /**
   * 判断是否存在空的输入项，并弹窗提示
   *
   * @return true 有一个输入项为空，false 全部输入项都不为空
   */
  private boolean assetAnyInputIsNull() {
    if (TextUtils.isEmpty(id.getText())) {
      ToastUtil.showToast(R.string.slotmachine_manage_addvender_id_null_hint_str);
      return true;
    }
    if (TextUtils.isEmpty(sn.getText())) {
      ToastUtil.showToast(R.string.slotmachine_manage_addvender_sn_null_hint_str);
      return true;
    }
    if (TextUtils.isEmpty(terminalName.getText())) {
      ToastUtil.showToast(R.string.slotmachine_manage_addvender_terminalname_null_hint_str);
      return true;
    }
    if (TextUtils.isEmpty(pos_PWD.getText())) {
      ToastUtil.showToast(R.string.slotmachine_manage_addvender_pass_null_hint_str);
      return true;
    }
    if (TextUtils.isEmpty(SellerTyp.getText())) {
      ToastUtil.showToast(R.string.slotmachine_manage_addvender_sellertyp_null_hint_str);
      return true;
    }
    if (TextUtils.isEmpty(TelNum.getText())) {
      ToastUtil.showToast(R.string.slotmachine_manage_addvender_telnum_null_hint_str);
      return true;
    }
    if (TextUtils.isEmpty(area.getText())) {
      ToastUtil.showToast(R.string.slotmachine_manage_addvender_area_null_hint_str);
      return true;
    }
    if (TextUtils.isEmpty(jiedao.getText())) {
      ToastUtil.showToast(R.string.slotmachine_manage_addvender_jiedao_null_hint_str);
      return true;
    }
    if (!ischeckVender){
      ToastUtil.showToast(R.string.slotmachine_manage_addvender_checkid_null_hint_str);
      return true;
    }

    return false;
  }

  /**
   * 展示软键盘
   */
  private void showSoftInput() {
    // 避免界面未加载完成软键盘加载失败，延迟半秒
    new Handler().postDelayed(new Runnable() {

      @Override
      public void run() {
        SoftInputUtil.showSoftInput(id);
      }
    }, 500);
  }


}
