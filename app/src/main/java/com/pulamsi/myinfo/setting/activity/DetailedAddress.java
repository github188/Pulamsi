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

/**
 * Created by DaiDingKang
 * Date: 2016-03-07
 * Time: 16:06
 */
public class DetailedAddress extends BaseActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mineinfo_modify_email);
        initData();
        initView();
    }

    private void initData() {
        email = getIntent().getStringExtra("address");
    }

    private void initView() {
        setHeaderTitle(R.string.detailed_address);
        setRightText("确定");
        et_email = (EditText) findViewById(R.id.et_email);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        rightTetxtView = getRightTetxtView();

        et_email.setText(email);
        et_email.setHint("请输入详细地址");
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
                if ("".equals(et_email.getText().toString())){
                    ToastUtil.toastShow(PulamsiApplication.context, "地址不能为空！");
                    return;
                }
                BusProvider.getInstance().post(new MessageEvent(et_email.getText().toString(),"address"));
                finish();
            }
        });
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
