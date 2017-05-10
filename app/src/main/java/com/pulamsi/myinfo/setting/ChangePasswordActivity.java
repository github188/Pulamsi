package com.pulamsi.myinfo.setting;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.myinfo.setting.presenter.ChangePasswordPresenter;
import com.pulamsi.myinfo.setting.view.IChangePasswordView;
import com.pulamsi.utils.CheckInputUtil;
import com.pulamsi.utils.SoftInputUtil;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.utils.Utils;
import com.pulamsi.views.CountdownView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-05-13
 * Time: 09:34
 * FIXME
 * 此界面采用MVP模式
 */
public class ChangePasswordActivity extends BaseActivity implements IChangePasswordView, View.OnClickListener {
    /**
     * 修改密码的Presenter
     */
    private ChangePasswordPresenter changePasswordPresenter;
    /**
     * 将要发送的手机号码
     */
    private TextView sendPhone;
    /**
     * 自定义倒计时控件
     */
    private CountdownView sendVerificationCode;
    /**
     * 验证码输入控件
     */
    private EditText et_VerificationCode;
    /**
     * 新密码输入控件
     */
    private EditText et_Password;
    /**
     * 提交修改
     */
    private TextView tv_changePasswordConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_change_password_activity);
        initView();
        initData();
        showSoftInput();
    }

    private void initData() {
        changePasswordPresenter = new ChangePasswordPresenter(this, this);
    }

    private void initView() {
        setHeaderTitle(R.string.change_password_btn);
        sendPhone = (TextView) findViewById(R.id.tv_change_password_phone);
        et_VerificationCode = (EditText) findViewById(R.id.et_change_password_verification_code);
        et_Password = (EditText) findViewById(R.id.et_change_password_pwd);
        tv_changePasswordConfirm = (TextView) findViewById(R.id.tv_change_password_confirm_btn);
        sendVerificationCode = (CountdownView) findViewById(R.id.cdv_verification_code);
        sendVerificationCode.setOnClickListener(this);
        tv_changePasswordConfirm.setOnClickListener(this);
    }

    /**
     * 设置手机号码
     *
     * @param phone
     */
    @Override
    public void setChangePasswordPhone(String phone) {
        sendPhone.setText(Utils.phoneToFormat(phone));
    }


    /**
     * 开始倒计时
     */
    @Override
    public void startCountDown() {
        sendVerificationCode.startCountDown(60);
    }

    /**
     * 返回验证码
     *
     * @return
     */
    @Override
    public String getVerificationCode() {
        return et_VerificationCode.getText().toString().trim();
    }

    /**
     * 返回新密码
     *
     * @return
     */
    @Override
    public String getPassWord() {
        return et_Password.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cdv_verification_code:
                // 判断验证码是否冻结
                if (sendVerificationCode.isFreezing()) {
                    return;
                }
                //发送验证码
                changePasswordPresenter.sendVerificationCode();
                break;
            case R.id.tv_change_password_confirm_btn:
                if (judgeInput())
                    changePasswordPresenter.ChangePasswordSubmit();//提交修改密码
                break;
        }
    }

    /**
     * 判断输入
     *
     * @return
     */
    private boolean judgeInput() {
        String VerificationCode = et_VerificationCode.getText().toString().trim();
        String Password = et_Password.getText().toString().trim();
        if (TextUtils.isEmpty(VerificationCode) || TextUtils.isEmpty(Password)) {
            ToastUtil.showToast("输入不能为空！");
            return false;
        }
        if (VerificationCode.length() < 6) {
            ToastUtil.showToast("验证码格式错误！");
            return false;
        }
        if (!CheckInputUtil.checkPassword(Password)) {
            ToastUtil.showToast("密码格式错误！");
            return false;
        }
        return true;
    }

    /**
     * 展示软键盘
     */
    private void showSoftInput() {
        // 避免界面未加载完成软键盘加载失败，延迟半秒
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SoftInputUtil.showSoftInput(et_VerificationCode);
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
