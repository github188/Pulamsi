
package com.pulamsi.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.start.init.entity.User;
import com.pulamsi.utils.PulamsiHelper;
import com.pulamsi.views.PulamsiStartView;

/**
 * 启动页
 */
public class StartActivity extends BaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        PulamsiStartView pulamsiStartView = new PulamsiStartView(StartActivity.this);
        layout.addView(pulamsiStartView);
        mTitleHeaderBar.setVisibility(View.GONE);

        /**
         * 设置1650毫秒完成自定义进度条的加载
         * 跳转到首页
         */
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    /**
                     * 获取app的版本号和存数据库的版本对比，如果比数据库版本高就需要跳到新人引导页
                     * user对象存数据库的不能删除，只能修改
                     */
                    User user =  PulamsiApplication.dbUtils.findFirst(Selector.from(User.class));
                    int newVersionCode = PulamsiHelper.getVersionCode(StartActivity.this);
                    if (null == user){
                        /**
                         * 首次安装
                         */
                        Intent intent = new Intent(StartActivity.this,GuideActivity.class);
                        startActivity(intent);
                        user = new User();
                        user.setVersionCode(newVersionCode);
                        PulamsiApplication.dbUtils.save(user);
                    }else {
                        /**
                         * 更新或者下一次打开
                         */
                        if (newVersionCode > user.getVersionCode()){
                            Intent intent = new Intent(StartActivity.this,GuideActivity.class);
                            startActivity(intent);
                            user.setVersionCode(newVersionCode);
                            PulamsiApplication.dbUtils.update(user);
                      }else {
                            Intent intent = new Intent(StartActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }
                    StartActivity.this.finish();
                } catch (DbException e) {
                    e.printStackTrace();
                }

            }
        }, 1650);
    }
}
