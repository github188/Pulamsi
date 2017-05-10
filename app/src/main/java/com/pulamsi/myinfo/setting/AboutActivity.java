package com.pulamsi.myinfo.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.utils.PulamsiHelper;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.utils.Utils;
import com.pulamsi.utils.update.CheckUpdate;
import com.pulamsi.utils.update.IUpdateCallBack;

/**
 * 关于我们界面
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private TextView Version_Code, about_weibo, about_home;

    private String weibo = "http://weibo.com/pulamsi?refer_flag=1005055013_";
    private String home = "http://www.pulamsi.com";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        initUI();
    }

    private void initUI() {
        setHeaderTitle(R.string.my_info_setting_about);
        Version_Code = (TextView) findViewById(R.id.tv_Version_Code);
        about_weibo = (TextView) findViewById(R.id.about_weibo);
        about_home = (TextView) findViewById(R.id.about_home);
        TextView update = (TextView) findViewById(R.id.about_update);
        update.setOnClickListener(this);
        about_weibo.setOnClickListener(this);
        about_home.setOnClickListener(this);
        String versionName = PulamsiHelper.getVersionName(this);
        Version_Code.setText("Version Code: " + versionName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_update:
                PulamsiApplication.updateIgnore = false;
                //检查更新
                CheckUpdate.getInstance().setOnIUpdateCallBack(new IUpdateCallBack() {
                    @Override
                    public void noUpdate() {
                        ToastUtil.showToast("已经是最新版本");
                    }

                    @Override
                    public void errorUpdate() {
                        ToastUtil.showToast("检测失败");
                    }

                    @Override
                    public void nowUpdate() {
                        ToastUtil.showToast("正在更新");
                    }
                });
                CheckUpdate.getInstance().startCheck(AboutActivity.this);//自动检测更新
                break;
            case R.id.about_weibo:
                Utils.redirectBrowser(weibo, AboutActivity.this);
                break;
            case R.id.about_home:
                Utils.redirectBrowser(home, AboutActivity.this);
                break;
        }
    }

    @Override
    public boolean toggleOverridePendingTransition() {
        return true;
    }

    @Override
    public TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.FADE;
    }
}
