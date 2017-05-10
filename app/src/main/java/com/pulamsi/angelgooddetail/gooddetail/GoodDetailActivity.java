package com.pulamsi.angelgooddetail.gooddetail;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;

import com.pulamsi.R;
import com.pulamsi.angel.bean.AngelProductBean;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.start.MainActivity;
import com.pulamsi.utils.bean.CoutDownBean;


/**
 * 商品详情界面
 */
public class GoodDetailActivity extends BaseActivity {

    /**
     * 包含两个 ScrollView 的主布局容器
     */
    private GoodDetailLayoutContainer mMainContainer;

    private boolean mIsFirstLoad = true;
    /**
     * 商品对象
     */
    private AngelProductBean angelProductBean;

    /**
     * 底部操作
     */
    private GoodDetailBottomOperationBarFragment goodDetailBottomOperationBarFragment;
    /**
     * 商品参数
     */
    private GoodDetailGoodParametersFragment goodDetailGoodParametersFragment;

    /**
     * 图文详情
     */
    private GoodDetailGoodImageDetailFragment mImgDetailFragment;

    CoutDownBean coutDownBean;//初始化计时器

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseAppManager.getInstance().addActivity(this);
        setContentView(R.layout.angel_good_detail_activity);//置于header下方，重叠，正在修复中
        angelProductBean = (AngelProductBean) getIntent().getSerializableExtra("product");
        initUI();
    }

    private void initUI() {
        setHeaderTitle(R.string.gooddetail_title);//不设置标题，改为渐变
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final ImageView backTopButton = (ImageView) findViewById(R.id.iv_good_detail_back_top_button);
        backTopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backTopButton.setVisibility(View.GONE);
                mMainContainer.scrollToTop();
            }
        });

        setRightImage(R.drawable.ic_shoppingcar_normal);
        mTitleHeaderBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseAppManager.getInstance().clear();
                MainActivity.mTabView.setCurrentTab(3);
            }
        });

        mImgDetailFragment = new GoodDetailGoodImageDetailFragment();
        mMainContainer =(GoodDetailLayoutContainer) findViewById(R.id.svc_good_detail_main_container);
        goodDetailBottomOperationBarFragment = (GoodDetailBottomOperationBarFragment) fragmentManager.findFragmentById(R.id.fragment_good_detail_bottom_operation_bar);
        goodDetailBottomOperationBarFragment.setData(angelProductBean);
        goodDetailGoodParametersFragment = (GoodDetailGoodParametersFragment) fragmentManager.findFragmentById(R.id.fragment_good_detail_good_img_banner);
        goodDetailGoodParametersFragment.setData(angelProductBean);
        mImgDetailFragment.setData(angelProductBean);

        mMainContainer.setOnViewShowListener(new GoodDetailLayoutContainer.OnViewShowListener() {
            @Override
            public void onCompleteShowBottomView() {
                if (mIsFirstLoad) {

                    // 初次滑动到图文详情，开始加载图文详情的 TabLayout 模块和包含 WebView 的模块
                    fragmentManager.beginTransaction()
                            .replace(R.id.rl_good_detail_img_detail_tab_container, new GoodDetailImgDetailTabFragment()).commit();
                    fragmentManager.beginTransaction().replace(R.id.sv_good_detail_img_detail_container, mImgDetailFragment).commit();
                    mIsFirstLoad = false;
                }
                backTopButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onShowTopView() {
                backTopButton.setVisibility(View.GONE);
            }
        });
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
