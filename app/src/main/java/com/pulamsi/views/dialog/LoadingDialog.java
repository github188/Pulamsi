package com.pulamsi.views.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;

/**
 * 加载中Dialog
 *
 * @author xm
 */
public class LoadingDialog extends AlertDialog {

    private TextView tips_loading_msg;

    private String message = null;

    private Context context;

    public LoadingDialog(Context context) {
        super(context);
        message = getContext().getResources().getString(R.string.msg_load_ing);
        this.context = context;
    }

    public LoadingDialog(Context context, String message) {
        super(context);
        this.message = message;
        this.setCancelable(false);
        this.context = context;

    }

    public LoadingDialog(Context context, int theme, String message) {
        super(context, theme);
        this.message = message;
        this.setCancelable(false);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_tips_loading);
        getWindow().setLayout((int) (PulamsiApplication.ScreenWidth / 3), (int) (PulamsiApplication.ScreenWidth / 3));//设置高宽
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//去除白底

        tips_loading_msg = (TextView) findViewById(R.id.tips_loading_msg);
        tips_loading_msg.setText(this.message);

        //设置返回键关闭Dialog
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    hindDialog();
                }
                return false;
            }
        });
    }

    public void setText(String message) {
        this.message = message;
        tips_loading_msg.setText(this.message);
    }

    public void setText(int resId) {
        setText(getContext().getResources().getString(resId));
    }


    public void hindDialog() {
        if (isShowing()) {
            dismiss();
            LogUtils.e("执行");
        }
    }
}
