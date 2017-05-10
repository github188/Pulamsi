package com.pulamsi.myinfo.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.loginregister.LoginActivity;
import com.pulamsi.myinfo.setting.entity.Member;
import com.pulamsi.start.init.entity.Role;
import com.pulamsi.start.init.entity.User;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.dialog.CommonAlertDialog;

/**
 * 设置界面
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 记录用户对象
     */
    private User mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        initUI();
    }

    private void initUI() {
        setHeaderTitle(R.string.my_info_setting_title);
        TextView quit = (TextView) findViewById(R.id.tv_my_info_my_quit);
        quit.setOnClickListener(this);
        TextView changlePassword = (TextView) findViewById(R.id.setting_change_password);
        changlePassword.setOnClickListener(this);
        TextView about = (TextView) findViewById(R.id.setting_activity_about);
        about.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        try {
            mUser = PulamsiApplication.dbUtils.findFirst(Selector.from(User.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_change_password:
                //修改密码
                if (mUser.isHasLogin()) {
                    Intent changePassword = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                    startActivity(changePassword);
                } else {
                    ToastUtil.showToast(R.string.my_info_noLoginstring);
                }
                break;
            case R.id.setting_activity_about:
                //关于我们
                Intent about = new Intent(SettingActivity.this, AboutActivity.class);
                startActivity(about);
                break;
            case R.id.tv_my_info_my_quit:
                //安全退出
                if (mUser.isHasLogin()) {
                    CommonAlertDialog alertDialog = new CommonAlertDialog(this, "确定退出登录?", "确定", "取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null == mUser) {
                                mUser = new User();
                            }
                            mUser.setHasLogin(false);
                            mUser.setUserId("");
                            try {
                                PulamsiApplication.dbUtils.update(mUser);
                                PulamsiApplication.dbUtils.deleteAll(Role.class);
                                PulamsiApplication.dbUtils.deleteAll(Member.class);
                                ToastUtil.showToast("已退出登录");
                                Intent loginandregister = new Intent(SettingActivity.this, LoginActivity.class);
                                startActivity(loginandregister);
                                SettingActivity.this.finish();
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    alertDialog.show();
                } else {
                    ToastUtil.showToast(R.string.my_info_noLoginstring);
                }
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
