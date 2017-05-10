package com.pulamsi.myinfo.setting.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.CountdownView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-03
 * Time: 15:50
 * FIXME
 */
public class ModifyPhone extends BaseActivity implements View.OnClickListener {

    private TextView phone,submit;
    private EditText code;
    private CountdownView countdownView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mineinfo_modify_phone);
        initView();
    }

    private void initView() {
        setHeaderTitle(R.string.change_phone);
        phone = (TextView) findViewById(R.id.tv_change_password_phone);
        code = (EditText) findViewById(R.id.et_change_password_verification_code);
        countdownView = (CountdownView) findViewById(R.id.cdv_verification_code);
        submit = (TextView) findViewById(R.id.tv_change_password_confirm_btn);

        countdownView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cdv_verification_code:
                // 判断验证码是否冻结
                if (countdownView.isFreezing()) {
                    return;
                }
                //发送验证码
                sendVerificationCode();
                break;
            case R.id.tv_change_password_confirm_btn:
                if (judgeInput())
                    ChangePhoneSubmit();//提交换绑
                break;

        }

    }

    private void ChangePhoneSubmit() {



    }

    /**
     * 判断输入
     *
     * @return
     */
    private boolean judgeInput() {
        String VerificationCode = code.getText().toString().trim();
        if (TextUtils.isEmpty(VerificationCode)) {
            ToastUtil.showToast("输入不能为空！");
            return false;
        }
        if (VerificationCode.length() < 6) {
            ToastUtil.showToast("验证码格式错误！");
            return false;
        }
        return true;
    }

    private void sendVerificationCode() {



    }
}
