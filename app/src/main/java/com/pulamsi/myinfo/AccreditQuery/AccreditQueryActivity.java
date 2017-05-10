package com.pulamsi.myinfo.AccreditQuery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.constant.Constants;
import com.pulamsi.home.entity.ChannelMobile;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.SoftInputUtil;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.BlankLayout;
import com.pulamsi.webview.CommonWebViewActivity;

import java.util.List;

/**
 * 授权查询界面
 * Created by lanqiang on 15/12/14.
 */
public class AccreditQueryActivity  extends BaseActivity implements TextView.OnEditorActionListener, TextWatcher, View.OnClickListener {

    private TextView username,address,qq,weixin,phone,mobile,sinaweibo,usersn,principal,signdate,describes,auditsta,dianname,diannamedetail,dianurl;

    private RelativeLayout dianurlrelative;

    private ImageView usericon;

    /** 用户输入词的 edit view */
    private EditText mUserInputEt;

    /** 清除输入词的按钮 */
    private ImageView mClearInputBtn;

    private ChannelMobile channel;

    private ScrollView dataScrollView;

    /**
     * 空值显示view
     */
    private BlankLayout mBlankLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accredit_query_activity);
        showSoftInput();
        initView();
    }

    private void initView() {
        initHeader();
        mBlankLayout = (BlankLayout) findViewById(R.id.blank_layout);
        mTitleHeaderBar.setOnClickListener(this);
        username = (TextView) findViewById(R.id.accreditquery_username);
        address = (TextView) findViewById(R.id.accreditquery_address);
        qq = (TextView) findViewById(R.id.accreditquery_qq);
        weixin = (TextView) findViewById(R.id.accreditquery_weixin);
        phone = (TextView) findViewById(R.id.accreditquery_phone);
        mobile = (TextView) findViewById(R.id.accreditquery_mobile);
        sinaweibo = (TextView) findViewById(R.id.accreditquery_sinaweibo);
        usersn = (TextView) findViewById(R.id.accreditquery_usersn);
        principal = (TextView) findViewById(R.id.accreditquery_principal);
        signdate = (TextView) findViewById(R.id.accreditquery_signdate);
        describes = (TextView) findViewById(R.id.accreditquery_describes);
        auditsta = (TextView) findViewById(R.id.accreditquery_auditsta);
        dianname = (TextView) findViewById(R.id.accreditquery_dianname);
        diannamedetail = (TextView) findViewById(R.id.accreditquery_diannamedetail);
        dianurl = (TextView) findViewById(R.id.accreditquery_dianurl);
        dianurlrelative = (RelativeLayout) findViewById(R.id.accreditquery_dianurl_relative);
        dianurlrelative.setOnClickListener(this);
        usericon = (ImageView) findViewById(R.id.accreditquery_usericon);
        dataScrollView = (ScrollView) findViewById(R.id.channel_search_datascrollview);
    }

    private void initHeader() {
        mTitleHeaderBar.setCustomizedCenterView(R.layout.search_door_header_bar);
        setRightText(getResources().getString(R.string.search_door_right_title_str));
        mTitleHeaderBar.setLeftContainerWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        mTitleHeaderBar.setRightContainerWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        mClearInputBtn =
                (ImageView) mTitleHeaderBar.getCenterViewContainer().findViewById(R.id.iv_search_door_header_clear_input_btn);
        mClearInputBtn.setOnClickListener(this);
        mUserInputEt = (EditText) mTitleHeaderBar.getCenterViewContainer().findViewById(R.id.et_search_door_header_input_text);
        mUserInputEt.setOnEditorActionListener(this);
        mUserInputEt.addTextChangedListener(this);
        mUserInputEt.setHint("请输入合同编号/手机号/QQ");
    }

    private void searchDate(String searchText) {
        DialogUtil.showLoadingDialog(AccreditQueryActivity.this, "搜索中...");

        String RoleResourePath = getString(R.string.serverbaseurl) + getString(R.string.searchValue) + searchText;
        StringRequest request = new StringRequest(Request.Method.GET, RoleResourePath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        if (!"[]".equals(result)) {
                            Gson gson = new Gson();
                            List<ChannelMobile> channelMobileList = gson.fromJson(result, new TypeToken<List<ChannelMobile>>() {}.getType());
                            channel = channelMobileList.get(0);
                            if (channel != null) {
                                setData();
                            }
                        } else {
                            showBlankLayout();
                            channel = null;
                        }
                    }catch (Exception e){
                        showBlankLayout();
                    }
                }
                DialogUtil.hideLoadingDialog();
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                DialogUtil.hideLoadingDialog();
                ToastUtil.showToast(R.string.notice_networkerror);
            }
        });
        PulamsiApplication.requestQueue.add(request);
    }


    /**
     * 搜到到内容，装配数据
     */
    private void setData() {
        String iconurl = AccreditQueryActivity.this.getString(R.string.serverbaseurl) + channel.getLogo();
        Glide.with(this)//更改图片加载框架
                .load(iconurl)
                .centerCrop()
                .placeholder(R.drawable.pulamsi_loding)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(usericon);
//        PulamsiApplication.imageLoader.displayImage(iconurl, usericon, PulamsiApplication.options);
        username.setText(channel.getUserName());
        address.setText(channel.getAdress());
        qq.setText(channel.getQq());
        weixin.setText(channel.getWeiXin());
        phone.setText(channel.getPhone());
        mobile.setText(channel.getTel());
        sinaweibo.setText(channel.getSinaWeibo());
        usersn.setText(channel.getUserSn());
        principal.setText(channel.getPrincipal());
        signdate.setText(channel.getSignDate());
        describes.setText(channel.getDescribes());
        if ("0".equals(channel.getAuditState())) {
            auditsta.setText("审核失败");
        } else if ("1".equals(channel.getAuditState())) {
            auditsta.setText("审核通过");
        } else if ("2".equals(channel.getAuditState())) {
            auditsta.setText("待审核");
        }
        dianname.setText(channel.getName());
        diannamedetail.setText(channel.getNameDetail());
        dianurl.setText(channel.getUrl());
        SoftInputUtil.hideSoftInput(AccreditQueryActivity.this);
        hideBlankLayout();
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            return true;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String userInputStr = mUserInputEt.getText().toString();
        if (userInputStr.length() > 0) {
            mClearInputBtn.setVisibility(View.VISIBLE);
        } else {
            mClearInputBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.iv_search_door_header_clear_input_btn) {
            mUserInputEt.setText("");
        } else if (viewId == R.id.ly_header_bar_title_wrap) {//点击搜索
            if (!"".equals(mUserInputEt.getText().toString().trim())){
                searchDate(mUserInputEt.getText().toString().trim());
            }else {
                ToastUtil.showToast("搜索条件不能为空");
            }
        }else if (viewId == R.id.accreditquery_dianurl_relative) {//点击跳转店铺详情
            Intent taobao = new Intent(AccreditQueryActivity.this, CommonWebViewActivity.class);
            taobao.putExtra(Constants.WEBVIEW_LOAD_URL,channel.getUrl());
            taobao.putExtra(Constants.WEBVIEW_TITLE,"店铺详情");
            AccreditQueryActivity.this.startActivity(taobao);
        }
    }

    /** 设置为空和隐藏 **/
    public void hideBlankLayout() {
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setVisibility(View.INVISIBLE);
        dataScrollView.setVisibility(View.VISIBLE);
    }

    public void showBlankLayout() {
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setBlankLayoutInfo(R.drawable.ic_space_order, R.string.my_info_accreditquery_empty);
        dataScrollView.setVisibility(View.GONE);
        mBlankLayout.setVisibility(View.VISIBLE);
    }


    /**
     * 展示软键盘
     */
    private void showSoftInput() {
        // 避免界面未加载完成软键盘加载失败，延迟半秒
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                SoftInputUtil.showSoftInput(mUserInputEt);
            }
        }, 500);
    }

    @Override
    public TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    @Override
    public boolean toggleOverridePendingTransition() {
        return true;
    }
}
