package com.pulamsi.myinfo.address;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.address.entity.Address;
import com.pulamsi.slotmachine.entity.Area;
import com.pulamsi.slotmachine.utils.SlotmachineDBhelper;
import com.pulamsi.utils.CheckInputUtil;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.SoftInputUtil;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.utils.Utils;
import com.pulamsi.views.androidwheel.OnWheelChangedListener;
import com.pulamsi.views.androidwheel.WheelView;
import com.pulamsi.views.androidwheel.adapters.ArrayWheelAdapter;
import com.taobao.uikit.component.LoopViewPager;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by lanqiang on 15/12/1.
 */
public class AddAddressActivity extends BaseActivity {

    /**
     * android_wheel
     * 省市区
     */
    private WheelView province;
    private WheelView city;
    private WheelView district;
    /**
     * popupWindow
     */
    private PopupWindow popupWindow;

    /**
     * 输入框
     */
    private EditText nameEdit, phoneEdit, addressEdit, zipcode;
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
     * 完成按钮
     */
    private Button addBtn;
    /**
     * 选择区域
     */
    private TextView area;
    /**
     * 记录修改地址传过来的对象
     */
    private Address address;
    /**
     * 记录是否是第一条记录
     */
    private boolean isFirst;

    /**
     * 省市区数据库处理类
     */
    private SlotmachineDBhelper dBhelper;

    /**
     * 记录是否是修改地址
     */
    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dBhelper = new SlotmachineDBhelper(this);
        address = (Address) getIntent().getSerializableExtra("address");
        isFirst = getIntent().getBooleanExtra("isfirst", false);
        setContentView(R.layout.address_add_activity);
        getprovince();
        initView();
    }


    private void initView() {
        showSoftInput();
        nameEdit = (EditText) findViewById(R.id.address_add_edit_name);
        phoneEdit = (EditText) findViewById(R.id.address_add_eidt_phone);
        addressEdit = (EditText) findViewById(R.id.address_add_edit_address);
        zipcode = (EditText) findViewById(R.id.address_add_eidt_zipcode);
        area = (TextView) findViewById(R.id.address_add_edit_area);
        addBtn = (Button) findViewById(R.id.address_btn_add);
        if (null == address) {
            setHeaderTitle(R.string.my_info_my_address_newaddress);
        } else {
            isUpdate = true;
            setHeaderTitle(R.string.my_info_my_address_updateaddress);
            nameEdit.setText(address.getName());
            phoneEdit.setText(address.getMobile());
            zipcode.setText(address.getZipCode());
            area.setText(address.getProvinceName() + "-" + address.getCityName() + "-" + address.getCountyName());
            addressEdit.setText(address.getAddressAlias());
            addBtn.setText("完成");
        }
        initandroidwheel();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assetAnyInputIsNull()) {
                    return;
                }
                bindData();
                if (isUpdate) {
                    updateAddress(address);
                } else {
                    addAddress(address);
                }
            }
        });
        area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAtLocation(area.getRootView(), Gravity.CENTER, 0, 0);
            }
        });
        phoneEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!CheckInputUtil.checkPhoneNum(phoneEdit.getText().toString().trim())) {
                        ToastUtil.showToast(R.string.register_phone_invalid_msg);
                    }
                }
            }
        });

    }

    private void initandroidwheel() {
        LayoutInflater localinflater = (LayoutInflater) AddAddressActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        province.setViewAdapter(new ArrayWheelAdapter<String>(AddAddressActivity.this, Utils.stringforListarray(provinces)));
        province.setVisibleItems(7);
        city.setVisibleItems(7);
        district.setVisibleItems(7);
        if (isUpdate) {
            for (int i = 0; i < provinces.size(); i++) {
                if (address.getProvinceId().equals(provinces.get(i).getId())) {
                    province.setCurrentItem(i);
                    break;
                }

            }
        }
        updateCities();
        updateAreas();


    }

    private void updateAreas() {
        citySelectedItemId = city.getCurrentItem();
        countys = dBhelper.getDistrict(citys.get(citySelectedItemId).getId());

        district.setViewAdapter(new ArrayWheelAdapter<String>(this, Utils.stringforListarray(countys)));
        if (isUpdate) {
            for (int i = 0; i < countys.size(); i++) {
                if (address.getCountyId().equals(countys.get(i).getId())) {
                    district.setCurrentItem(i);
                    break;
                }

            }
        } else {
            district.setCurrentItem(0);
        }
    }

    private void updateCities() {
        provinceSelectedItemId = province.getCurrentItem();
        citys = dBhelper.getCity(provinces.get(provinceSelectedItemId).getId());
        city.setViewAdapter(new ArrayWheelAdapter<String>(this, Utils.stringforListarray(citys)));
        if (isUpdate) {
            for (int i = 0; i < citys.size(); i++) {
                if (address.getCityId().equals(citys.get(i).getId())) {
                    city.setCurrentItem(i);
                    break;
                }
            }
        } else {
            city.setCurrentItem(0);
        }
        updateAreas();
    }

    /**
     * 绑定数据
     */
    private void bindData() {
        String name = nameEdit.getText().toString().trim();
        String phone = phoneEdit.getText().toString().trim();
        String addresinfo = addressEdit.getText().toString().trim();
        String code = zipcode.getText().toString().trim();
        if (null == address) {
            address = new Address();
        }
        address.setName(name);
        address.setAddressAlias(addresinfo);
        address.setZipCode(code);
        if (!isUpdate) {
            if (isFirst) {
                address.setIsDefault("1");
            } else {
                address.setIsDefault("0");
            }
        }
        address.setMobile(phone);
        address.setMid(Constants.userId);
        address.setCityId(citys.get(citySelectedItemId).getId());
        address.setCityName(citys.get(citySelectedItemId).getName());
        address.setCountyId(countys.get(countySelectedItemId).getId());
        address.setCountyName(countys.get(countySelectedItemId).getName());
        address.setProvinceId(provinces.get(provinceSelectedItemId).getId());
        address.setProvinceName(provinces.get(provinceSelectedItemId).getName());
    }

    /**
     * 新增收货地址方法
     */
    private void addAddress(final Address address) {
        DialogUtil.showLoadingDialog(this, "添加中...");
        String urlPath = getString(R.string.serverbaseurl) + getString(R.string.addAddress);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPath, new Response.Listener<String>() {

            public void onResponse(String arg0) {
                if (arg0 != null) {
                    DialogUtil.hideLoadingDialog();
                    ToastUtil.showToast(R.string.shipping_address_saveOKmsg);
                    AddAddressActivity.this.finish();
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
                return Utils.objToMap(address);
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(stringRequest);
    }

    /**
     * 修改收货地址方法
     */
    private void updateAddress(final Address address) {
        DialogUtil.showLoadingDialog(this, "修改中...");
        String urlPath = getString(R.string.serverbaseurl) + getString(R.string.addAddress);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPath, new Response.Listener<String>() {

            public void onResponse(String arg0) {
                if (arg0 != null) {
                    DialogUtil.hideLoadingDialog();
                    ToastUtil.showToast(R.string.shipping_address_updateOKmsg);
                    AddAddressActivity.this.finish();
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
                return Utils.objToMap(address);
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(stringRequest);

    }

    /**
     * 判断是否存在空的输入项，并弹窗提示
     *
     * @return true 有一个输入项为空，false 全部输入项都不为空
     */
    private boolean assetAnyInputIsNull() {
        if (TextUtils.isEmpty(nameEdit.getText())) {
            ToastUtil.showToast(R.string.new_shipping_address_user_name_null_hint_str);
            return true;
        }
        if (TextUtils.isEmpty(phoneEdit.getText())) {
            ToastUtil.showToast(R.string.new_shipping_address_user_num_null_str);
            return true;
        }
        if (!CheckInputUtil.checkPhoneNum(phoneEdit.getText().toString().trim())) {
            ToastUtil.showToast(R.string.register_phone_invalid_msg);
            return true;
        }
        if (TextUtils.isEmpty(area.getText())) {
            ToastUtil.showToast(R.string.new_shipping_address_user_area_null_str);
            return true;
        }
        if (TextUtils.isEmpty(addressEdit.getText())) {
            ToastUtil.showToast(R.string.new_shipping_address_user_detail_address_null_str);
            return true;
        }

        return false;
    }

    private void getprovince() {
        provinces = dBhelper.getProvince();
    }

    /**
     * 展示软键盘
     */
    private void showSoftInput() {
        // 避免界面未加载完成软键盘加载失败，延迟半秒
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                SoftInputUtil.showSoftInput(nameEdit);
            }
        }, 500);
    }


    @Override
    public boolean toggleOverridePendingTransition() {
        return true;
    }

    @Override
    public TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.BOTTOM;
    }
}
