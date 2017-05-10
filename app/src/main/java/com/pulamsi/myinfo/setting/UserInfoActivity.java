package com.pulamsi.myinfo.setting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lidroid.xutils.util.LogUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.base.otto.BusProvider;
import com.pulamsi.base.otto.MessageEvent;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.setting.activity.DetailedAddress;
import com.pulamsi.myinfo.setting.activity.ModifyEmail;
import com.pulamsi.myinfo.setting.activity.ModifyName;
import com.pulamsi.myinfo.setting.activity.ModifyPhone;
import com.pulamsi.myinfo.setting.entity.Member;
import com.pulamsi.start.init.entity.City;
import com.pulamsi.start.init.entity.District;
import com.pulamsi.start.init.entity.Province;
import com.pulamsi.start.init.utils.DBhelper;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.PhotoUtils;
import com.pulamsi.utils.RequestDataUtils;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.avatarView.AvatarImageView;
import com.pulamsi.views.dialog.LoadingDialog;
import com.pulamsi.views.selector.CitySelector;
import com.pulamsi.views.selector.SexSelector;
import com.pulamsi.views.selector.TimeSelector;
import com.squareup.otto.Subscribe;
import com.umeng.socialize.utils.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息界面
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener, TextWatcher, AvatarImageView.AfterCropListener {

    /**
     * 帐户信息对象
     */
    private Member member;
    /**
     * 数据显示控件
     */
    private TextView loginname, name, sex, birthDay, mobile, area, address, email;
    /**
     * 加载进度条
     */
    private ProgressWheel progressWheel;

    /**
     * 点击修改控件
     *
     * @param savedInstanceState
     */
    private RelativeLayout rl_modifyEmail, rl_birthday, rl_sex, rl_detailed_Address, rl_name, rl_Region,rl_modifyPhone;

    /**
     * 头部右键实例
     */
    TextView rightTetxtView;
    LoadingDialog LoadingDialog;
    /**
     * 头像控件
     */
    private AvatarImageView avatarImageView;

    /**
     * 输入的字符
     */
    String sBirthDay;
    String sSex;
    String sAddress;
    String sEmail;
    String sName;

    HashMap<String, String> areaMap;

    CitySelector citySelector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册bus
        BusProvider.getInstance().register(this);
        setContentView(R.layout.userinfo_layout);
        requestData();
        initUI();
    }


    /**
     * 请求数据
     */
    private void requestData() {
        String showAccount = getString(R.string.serverbaseurl) + getString(R.string.showMember) + Constants.userId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, showAccount, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        member = gson.fromJson(result, Member.class);
                        setUIData();
                        progressWheel.setVisibility(View.GONE);
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "帐户信息装配出错");
                    }
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError arg0) {
                ToastUtil.showToast(R.string.notice_networkerror);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(stringRequest);
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        LoadingDialog = new LoadingDialog(UserInfoActivity.this);
        setHeaderTitle(R.string.my_info_userinfo_title);
        setRightText("提交");
        rightTetxtView = getRightTetxtView();
        rightTetxtView.setVisibility(View.GONE);
        avatarImageView = (AvatarImageView) findViewById(R.id.avatarIv);
        progressWheel = (ProgressWheel) findViewById(R.id.userinfo_pw);
        loginname = (TextView) findViewById(R.id.userinfo_loginname);
        name = (TextView) findViewById(R.id.userinfo_name);
        sex = (TextView) findViewById(R.id.userinfo_sex);
        birthDay = (TextView) findViewById(R.id.userinfo_birthDay);
        mobile = (TextView) findViewById(R.id.userinfo_mobile);
        area = (TextView) findViewById(R.id.userinfo_area);
        address = (TextView) findViewById(R.id.userinfo_address);
        email = (TextView) findViewById(R.id.userinfo_email);
        rl_modifyEmail = (RelativeLayout) findViewById(R.id.rl_modifyEmail);
        rl_modifyPhone = (RelativeLayout) findViewById(R.id.rl_modifyPhone);
        rl_birthday = (RelativeLayout) findViewById(R.id.rl_birthday);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        rl_detailed_Address = (RelativeLayout) findViewById(R.id.rl_detailed_Address);
        rl_name = (RelativeLayout) findViewById(R.id.rl_name);
        rl_Region = (RelativeLayout) findViewById(R.id.rl_Region);

        //初始化选择城市
        citySelector = new CitySelector(UserInfoActivity.this, area);

        initListener();
    }

    private void initListener() {
        rl_modifyEmail.setOnClickListener(this);
        rl_birthday.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_detailed_Address.setOnClickListener(this);
        rl_name.setOnClickListener(this);
        rl_Region.setOnClickListener(this);
        rl_modifyPhone.setOnClickListener(this);

        rightTetxtView.setOnClickListener(new SubmitListener());
        avatarImageView.setAfterCropListener(this);
    }
    
    /**
     * 设置控件
     */
    private void setUIData() {
        //设置头像
        if (!TextUtils.isEmpty(member.getImgUrl())) {
            String url = getResources().getString(R.string.serverbaseurl) + member.getImgUrl();
            PulamsiApplication.imageLoader.displayImage(url, avatarImageView, new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.default_avatar) //设置图片在下载期间显示的图片
                    .showImageForEmptyUri(R.drawable.default_avatar)//设置图片Uri为空或是错误的时候显示的图片
                    .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                    .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                    .build());
        }
        loginname.setText(member.getUsername());
        name.setText(member.getName());
        if ("male".equals(member.getSex())) {
            sex.setText("男");
            sex.setTag(member.getSex());
        } else if ("female".equals(member.getSex())) {
            sex.setText("女");
            sex.setTag(member.getSex());
        }

        birthDay.setText(member.getBirthDay().substring(0, 10));
        mobile.setText(member.getMobile());


        DBhelper dBhelper = new DBhelper(this);
        if (!member.getCityId().equals("")) {
            City city = dBhelper.getCitybycityID(member.getCityId());
            if (!member.getProvinceId().equals("") && !member.getDistrictId().equals("")) {
                Province province = dBhelper.getProvinceByID(member.getProvinceId());
                District district = dBhelper.getDistrictByID(member.getDistrictId());
                area.setText(province.getName() + "-" + city.getName() + "-" + district.getName());
            }
        }

        address.setText(member.getAdress());
        email.setText(member.getEmail());
        setTextChangedListener();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_modifyEmail:
                Intent email_intent = new Intent(UserInfoActivity.this, ModifyEmail.class);
                email_intent.putExtra("email", email.getText().toString());
                startActivity(email_intent);
                break;
            case R.id.rl_birthday:
                new TimeSelector(UserInfoActivity.this, birthDay).show();
                break;
            case R.id.rl_sex:
                new SexSelector(UserInfoActivity.this, sex).show();
                break;
            case R.id.rl_Region:
                citySelector.init();
                citySelector.show();
                break;
            case R.id.rl_detailed_Address:
                Intent address_intent = new Intent(UserInfoActivity.this, DetailedAddress.class);
                address_intent.putExtra("address", address.getText().toString());
                startActivity(address_intent);
                break;
            case R.id.rl_name:
                Intent name_intent = new Intent(UserInfoActivity.this, ModifyName.class);
                name_intent.putExtra("name", name.getText().toString());
                startActivity(name_intent);
                break;
            case R.id.rl_modifyPhone:
                Intent phone_intent = new Intent(UserInfoActivity.this, ModifyPhone.class);
                startActivity(phone_intent);
                break;

        }

    }


    @Subscribe   //订阅事件DataChangedEvent
    public void onDataChangedEvent(MessageEvent event) {
        if ("address".equals(event.getTag())) {
            address.setText(event.getMsg());
        } else if ("email".equals(event.getTag())) {
            email.setText(event.getMsg());
        } else if ("name".equals(event.getTag())) {
            name.setText(event.getMsg());
        } else if ("area".equals(event.getTag())) {
            areaMap = event.getMap();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }


    /**
     * 监听修改
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        rightTetxtView.setVisibility(View.VISIBLE);
    }

    public void setTextChangedListener() {
        email.addTextChangedListener(this);
        name.addTextChangedListener(this);
        sex.addTextChangedListener(this);
        birthDay.addTextChangedListener(this);
        area.addTextChangedListener(this);
        address.addTextChangedListener(this);
    }

    @Override
    public void afterCrop(Bitmap photo) {
        if (photo != null) {
            String avatarBase64 = PhotoUtils.bitmapToBase64(photo);//转为base64编码
            requestAvatar(avatarBase64);//提交头像
        } else {
            ToastUtil.toastShow(PulamsiApplication.context, "上传失败！");
        }
    }

    private void requestAvatar(final String avatarBase64) {
        DialogUtil.showLoadingDialog(UserInfoActivity.this, "上传中...");
        String url = getResources().getString(R.string.serverbaseurl) + getResources().getString(R.string.modify_avatar);
        StringRequest requestAvatarRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String message = jsonObject.getString("message");
                        if (jsonObject.getBoolean("successful")) {
                            ToastUtil.toastShow(PulamsiApplication.context, "上传成功！");
                            RequestDataUtils.getinfo(UserInfoActivity.this);
                        } else {
                            ToastUtil.toastShow(PulamsiApplication.context, message);
                        }
                    } catch (Exception e) {
                        LogUtils.e("防伪验证装配出错");
                    }
                }
                DialogUtil.hideLoadingDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.toastShow(PulamsiApplication.context, getResources().getString(R.string.notice_networkerror));
                DialogUtil.hideLoadingDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("memberId", Constants.userId);
                map.put("imgStr", avatarBase64);
                return map;
            }
        };
        requestAvatarRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(requestAvatarRequest);
    }

    class SubmitListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (dataIsEmpty()) {
                LoadingDialog.show();
                submitRequestData();
            } else {
                ToastUtil.toastShow(PulamsiApplication.context, "请先完善资料！");
            }
        }
    }

    /**
     * 判断是否为空输入
     *
     * @return
     */
    private boolean dataIsEmpty() {
        sBirthDay = birthDay.getText().toString().trim();
        sAddress = address.getText().toString().trim();
        sEmail = email.getText().toString().trim();
        sName = name.getText().toString().trim();
        sSex = (String) sex.getTag();
        if (sBirthDay.isEmpty())
            return false;
        if (sAddress.isEmpty())
            return false;
        if (sEmail.isEmpty())
            return false;
        if (sName.isEmpty())
            return false;
        if (sSex == null || sSex.isEmpty())
            return false;

        return true;
    }

    /**
     * 提交请求数据
     */
    private void submitRequestData() {
        String updateUserInfo = getString(R.string.serverbaseurl) + getString(R.string.updateUserInfo);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUserInfo, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Log.e("返回的:", result);
                        JSONObject jsonObject = new JSONObject(result);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            ToastUtil.showToast("修改成功");
                            rightTetxtView.setVisibility(View.GONE);
                        } else {
                            ToastUtil.showToast(R.string.notice_networkerror);
                        }
                        LoadingDialog.dismiss();
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "提交信息装配出错");
                    }
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError arg0) {
                LoadingDialog.dismiss();
                ToastUtil.showToast("网络错误");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("mid", Constants.userId);
                map.put("birth", sBirthDay);
                map.put("adress", sAddress);
                map.put("email", sEmail);
                map.put("name", sName);
                map.put("sex", sSex);
                if (areaMap == null) {
                    map.put("provinceId", member.getProvinceId());
                    map.put("cityId", member.getCityId());
                    map.put("districtId", member.getDistrictId());
                } else {
                    map.put("provinceId", areaMap.get("provinceId"));
                    map.put("cityId", areaMap.get("cityId"));
                    map.put("districtId", areaMap.get("districtId"));
                }
                for (String key : map.keySet()) {
                    LogUtils.e("键= " + key + " 值= " + map.get(key));
                }
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //在拍照、选取照片、裁剪Activity结束后，调用的方法
        if (avatarImageView != null) {
            avatarImageView.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.BOTTOM;
    }

    @Override
    public boolean toggleOverridePendingTransition() {
        return true;
    }
}
