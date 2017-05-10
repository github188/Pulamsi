package com.pulamsi.integral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lidroid.xutils.util.LogUtils;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.setting.entity.Member;
import com.pulamsi.utils.ToastUtil;


/**
 * 积分商城总界面
 */
public class IntegralActivity extends BaseActivity implements View.OnClickListener {

    private ProgressWheel progressWheel;
    private TextView number;
    /**
     * 帐户信息对象
     */
    private Member member;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseAppManager.getInstance().addActivity(this);
        setContentView(R.layout.integral_activity);
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    /**
     * 请求用户数据
     */
    private void requestData() {
        String showAccount = getString(R.string.serverbaseurl) + getString(R.string.showMember) + Constants.userId;
        System.out.println(showAccount);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, showAccount, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        member = gson.fromJson(result, Member.class);
                         LogUtils.e(member.toString());
                        //设置积分
                        number.setText(member.getPoint()+"");
                        progressWheel.setVisibility(View.GONE);
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "我的积分帐户信息装配出错");
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                ToastUtil.showToast(R.string.notice_networkerror);
            }
        });
        PulamsiApplication.requestQueue.add(stringRequest);
    }

    private void initUI() {
        setHeaderTitle(R.string.integral_my_title);

        progressWheel = (ProgressWheel) findViewById(R.id.integral_activity_pw);
        number = (TextView) findViewById(R.id.integral_number);
        TextView detail = (TextView) findViewById(R.id.intrgral_integraldetail);
        TextView store = (TextView) findViewById(R.id.intrgral_integralstore);
        detail.setOnClickListener(this);
        store.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.intrgral_integraldetail:
                //积分明细
                Intent integraldetail = new Intent(IntegralActivity.this, IntegralDetailListActivity.class);
                startActivity(integraldetail);
                break;
            case R.id.intrgral_integralstore:
                //积分商城
                Intent integralStore = new Intent(IntegralActivity.this, IntegralStoreActivity.class);
                startActivity(integralStore);
                break;
        }
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
