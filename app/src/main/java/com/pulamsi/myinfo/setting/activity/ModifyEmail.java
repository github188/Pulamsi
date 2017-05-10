package com.pulamsi.myinfo.setting.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.otto.BusProvider;
import com.pulamsi.base.otto.MessageEvent;
import com.pulamsi.utils.ToastUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DaiDingKang
 * Date: 2016-03-07
 * Time: 16:06
 */
public class ModifyEmail extends BaseActivity implements View.OnClickListener {

    /**
     * 输入框
     */
    private EditText et_email;

    /**
     *清除按钮
     */
    private ImageView iv_delete;

    /**
     * 右文本实例
     */
    private TextView rightTetxtView;

    /**
     * 用户信息
     */
    String email;
    /**
     * 输入信息
     */
    String sEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mineinfo_modify_email);
        initData();
        initView();
    }

    private void initData() {
        email = getIntent().getStringExtra("email");
    }

    private void initView() {
        setHeaderTitle(R.string.modify_email);
        setRightText("确定");
        et_email = (EditText) findViewById(R.id.et_email);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        rightTetxtView = getRightTetxtView();

        et_email.setText(email);
        et_email.setHint("如a@b.com");
        //光标移至末尾
        et_email.setSelection(et_email.length());
        showSoftInput(et_email);
        initListener();
    }

    private void initListener() {
        et_email.addTextChangedListener(new EditChangedListener());
        iv_delete.setOnClickListener(this);
        //确定按钮的事件
        rightTetxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataIsEmpty()) {
                    ToastUtil.toastShow(PulamsiApplication.context, "邮箱不能为空！");
                    return;
                }else {
                    if (!isEmail(sEmail)){
                        ToastUtil.toastShow(PulamsiApplication.context, "邮箱格式有误！");
                        return;
                    }
                }
                BusProvider.getInstance().post(new MessageEvent(sEmail, "email"));
                finish();
            }
        });
    }

    private boolean dataIsEmpty() {
        sEmail = et_email.getText().toString().trim();
        if (sEmail.isEmpty())
            return true;

        return false;
    }


    /**
     * 正则判断是否正确格式
     * @param email
     * @return
     */
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 输入监听
     */
    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

             if ("".equals(s.toString())){
                 if (iv_delete.getVisibility() == View.VISIBLE){
                     iv_delete.setVisibility(View.GONE);
                 }
             }else {
                 if (iv_delete.getVisibility() == View.GONE){
                     iv_delete.setVisibility(View.VISIBLE);
                 }
             }
        }

        @Override
        public void afterTextChanged(Editable s) {

       }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_delete:
                et_email.setText("");
                break;


        }

    }

}
