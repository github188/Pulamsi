
package com.pulamsi.start;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseUtil.FragmentUtils;
import com.pulamsi.base.widget.TabView;
import com.pulamsi.category.CategoryFragment;
import com.pulamsi.constant.Constants;
import com.pulamsi.home.HomeFragment;
import com.pulamsi.myinfo.MyinfoFragment;
import com.pulamsi.shoppingcar.ShoppingCarFragment;
import com.pulamsi.slotmachine.SlotmachineFragment;
import com.pulamsi.utils.update.CheckUpdate;
import com.pulamsi.utils.update.DownloadService;

public class MainActivity extends FragmentActivity implements TabView.OnTabChangeListener{

    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;
    public static TabView mTabView;

    /** 当前状态 */
    private int mCurrentTabIndex = 1;

    /** 再按一次退出程序*/
    private long exitTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//解决Fragment中使用地图，切换会闪一下黑屏的问题
        super.onCreate(savedInstanceState);
        CheckUpdate.getInstance().startCheck(this);//自动检测更新
        PulamsiApplication.fragmentActivity = this;
        mFragmentManager = getSupportFragmentManager();
        mCurrentTabIndex = 1;
        setupViews();
    }
    private void setupViews()
    {
        setContentView(R.layout.main_activity);
        mTabView = (TabView) findViewById(R.id.view_tab);
        mTabView.setOnTabChangeListener(this);
        mTabView.setCurrentTab(0);
    }

    /* (non-Javadoc)
     * @see app.ui.widget.TabView.OnTabChangeListener#onTabChange(java.lang.String)
     */
    @Override
    public void onTabChange(String tag) {
        if (tag != null) {
            if (tag.equals("home")) {
                mCurrentTabIndex = 0;
                replaceFragment(HomeFragment.class);
            }else if ("category".equals(tag)) {
                mCurrentTabIndex = 1;
                replaceFragment(CategoryFragment.class);
                //点击分类需要做更新操作
                if (null != Constants.categoryCatFragment){
                    Constants.categoryCatFragment.startDataRequest();
                }
            } else if (tag.equals("slotmachine")) {
                mCurrentTabIndex = 2;
                replaceFragment(SlotmachineFragment.class);
            } else if (tag.equals("shoppingcar")) {
                mCurrentTabIndex = 3;
                replaceFragment(ShoppingCarFragment.class);
                //点击购物车，需要做下拉刷新操作
                if (null != Constants.shoppingCarController){
                    Constants.shoppingCarController.refreshList();
                }
            }else if(tag.equals("myinfo")){
                 mCurrentTabIndex = 4;
                 replaceFragment(MyinfoFragment.class);
			}
        }
    }

    private void replaceFragment(Class<? extends Fragment> newFragment) {
        mCurrentFragment = FragmentUtils.switchFragment(mFragmentManager,
                R.id.layout_content, mCurrentFragment,
                newFragment, null, false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this,R.string.click_back_quit_message,Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                if (DownloadService.isDownload)//正在下载新版本就清除通知栏
                    ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
