package com.pulamsi.security;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.security.presenter.SecurityPresenter;
import com.pulamsi.security.view.ISecurityView;
import com.pulamsi.utils.CheckInputUtil;
import com.pulamsi.views.LoadView.LoadViewHelper;
import com.pulamsi.views.SweetAlert.SweetAlertDialog;
import com.pulamsi.views.secucrity.SecuritySuccessView;
import tr.xip.errorview.ErrorView;

/**
 * User: daidinkang(ddk19941017@Gmail.com)
 * Date: 2016-04-06
 * Time: 14:29
 * FIXME
 */
public class SecurityActivity extends BaseActivity implements View.OnClickListener, ISecurityView {
    TextInputLayout til_secret_code;
    TextInputLayout til_phone;
    TextView tv_code, tv_queryCount, tv_firstQueryDate, tv_content;
    SecuritySuccessView successView;
    Button submit;
    String phone, secretCode;
    String code;
    SecurityPresenter securityPresenter;
    View contentLayout;//LoadHelper显示布局
    LoadViewHelper loadViewHelper;
    ScrollView lowerPart;
    LinearLayout ll_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        initData();
        initView();
        initPresenter();//初始化Prestener
    }

    private void initPresenter() {
        securityPresenter = new SecurityPresenter(this);
        securityPresenter.init();
    }

    private void initData() {
        code = getIntent().getStringExtra("code");
    }

    private void initView() {
        setHeaderTitle("防伪查询");
        contentLayout = findViewById(R.id.content_layout);
        loadViewHelper = new LoadViewHelper(contentLayout);
        tv_code = (TextView) findViewById(R.id.tv_code);
        tv_code.setText(code);
        til_secret_code = (TextInputLayout) findViewById(R.id.til_secret_code);
        successView = (SecuritySuccessView) findViewById(R.id.ss_SuccessView);
        lowerPart = (ScrollView) findViewById(R.id.sl_lower_part);
        tv_content = (TextView) findViewById(R.id.tv_security_content);
        ll_message = (LinearLayout) findViewById(R.id.ll_security_message);
        tv_queryCount = (TextView) findViewById(R.id.tv_security_queryCount);
        tv_firstQueryDate = (TextView) findViewById(R.id.tv_security_first_querydate);
        til_phone = (TextInputLayout) findViewById(R.id.til_phone);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (isCorrect())//判断是否输入正确
                    integralCommit();
                break;
        }
    }


    public boolean isCorrect() {
        secretCode = til_secret_code.getEditText().getText().toString();
        phone = til_phone.getEditText().getText().toString();
        if (TextUtils.isEmpty(secretCode)) {
            til_secret_code.setError("输入不能为空");
            return false;
        } else if (secretCode.length() != 6) {
            til_secret_code.setError("格式错误");
            return false;
        }
        til_secret_code.setErrorEnabled(false);
        if (TextUtils.isEmpty(phone)) {
            til_phone.setError("输入不能为空");
            return false;
        } else if (!CheckInputUtil.isMobile(phone)) {
            til_phone.setError("格式错误");
            return false;
        }
        til_phone.setErrorEnabled(false);
        return true;
    }


    @Override
    public void showDialogMessage(int type, String title, String message) {
        SweetAlertDialog sDialog = new SweetAlertDialog(this, type);
        sDialog.setTitleText(title)
                .setContentText(message)
                .setConfirmText("   确定  ")
                .setConfirmClickListener(null)
                .show();
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getSecretCode() {
        return secretCode;
    }

    @Override
    public Context getContext() {
        return SecurityActivity.this;
    }

    @Override
    public void showLoading() {
        loadViewHelper.showLoading();
    }

    @Override
    public void showEmpty() {
        loadViewHelper.showEmpty("没有数据");
    }

    @Override
    public void showError(ErrorView.RetryListener onClickListener) {
        loadViewHelper.showError(onClickListener);
    }

    @Override
    public void showComplete() {
        loadViewHelper.restore();


    }

    @Override
    public void setqueryCount(String number) {
        if ("0".equals(number)) {
            ll_message.setVisibility(View.INVISIBLE);
        } else {
            tv_queryCount.setText(number + "次");
            tv_content.setText("恭喜您，防伪码正确！" + "\n" + "您的防伪码已被查询多次,请您验证暗码是否可以进行积分！");
        }
    }

    @Override
    public void setfirstQueryDate(String date) {
        tv_firstQueryDate.setText(date);
    }

    @Override
    public void integralCommit() {
        securityPresenter.isRegistered();
    }

    @Override
    public void showSuccess() {
        successView.setVisibility(View.VISIBLE);
        tv_content.setText("恭喜您，防伪码正确！" + "\n" + "您使用的是普兰氏正品");
        successView.Start();
    }

    @Override
    public void showFailure() {
        successView.setVisibility(View.VISIBLE);
        successView.Failure();
        lowerPart.setVisibility(View.INVISIBLE);
        tv_content.setText("对不起，您的防伪码不正确!" + "\n" + "请认准正品,谨防假冒！");
        ll_message.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean toggleOverridePendingTransition() {
        return true;
    }

    @Override
    public TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }
}
