package com.pulamsi.angel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gxz.PagerSlidingTabStrip;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.angel.presenter.AngelDetailsActivityPrestener;
import com.pulamsi.angel.view.IAngelDetailsActivity;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.home.entity.AngelMerchantsBean;
import com.pulamsi.utils.GlideCircleTransform;
import com.pulamsi.views.BlankLayout;
import com.pulamsi.views.LoadView.LoadViewHelper;
import com.pulamsi.views.StickyNavLayout;

import tr.xip.errorview.ErrorView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-21
 * Time: 16:51
 * FIXME
 */
public class AngelDetailsActivity extends BaseActivity implements IAngelDetailsActivity, ErrorView.RetryListener {


    private AngelDetailsActivityPrestener angelDetailsActivityPrestener;
    private ViewPager viewPager;
    private PagerSlidingTabStrip pagerSlidingTabStrip;//指示器
    private StickyNavLayout stickyNavLayout;//悬停根部局
    private LoadViewHelper loadViewHelper;

    private String sellerId;

    /**
     * 界面信息控件
     */
    private ImageView angelImg;//头像
    private TextView angelName;//名字
    private TextView angelStatement;//签名
    private RatingBar angelRatingBar;//评分控件
    private TextView angelCredit;//正能量
    private TextView angelCreditCompare;//正能量比较
    private TextView angelServe;//天使服务
    private TextView angelServeCompare;//天使服务比较
    private TextView angelAfter;//天使售后
    private TextView angelAfterCompare;//天使售后比较
    private TextView angelAdress;//天使地址


    private final float ANGEL_LEVEL = 4.0f;//水平

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseAppManager.getInstance().addActivity(this);
        setContentView(R.layout.angel_details_activity);
        initView();
        init();//初始化
    }

    private void initView() {
        setHeaderTitle("天使详情");
        viewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.id_stickynavlayout_indicator);
        stickyNavLayout = (StickyNavLayout) findViewById(R.id.id_stick);

        angelImg = (ImageView) findViewById(R.id.angel_img);
        angelName = (TextView) findViewById(R.id.angel_name);
        angelStatement = (TextView) findViewById(R.id.angel_statement);
        angelRatingBar = (RatingBar) findViewById(R.id.angel_ratingBar);
        angelCredit = (TextView) findViewById(R.id.angel_credit);
        angelCreditCompare = (TextView) findViewById(R.id.angel_credit_compare);
        angelServe = (TextView) findViewById(R.id.angel_serve);
        angelServeCompare = (TextView) findViewById(R.id.angel_serve_compare);
        angelAfter = (TextView) findViewById(R.id.angel_after);
        angelAfterCompare = (TextView) findViewById(R.id.angel_after_compare);

        angelAdress = (TextView) findViewById(R.id.angel_adress);


        View view = findViewById(R.id.content_layout);//动态状态的布局
        loadViewHelper = new LoadViewHelper(view);

        sellerId = getIntent().getStringExtra("sellerId");
    }

    private void init() {
        angelDetailsActivityPrestener = new AngelDetailsActivityPrestener(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    public PagerSlidingTabStrip getPagerSlidingTabStrip() {
        return pagerSlidingTabStrip;
    }

    @Override
    public StickyNavLayout getStickyNavLayout() {
        return stickyNavLayout;
    }


    @Override
    public void showLoading() {
        loadViewHelper.showLoading();
    }

    @Override
    public void showError() {
        loadViewHelper.showError(this);
    }

    @Override
    public void showSuccessful() {
        loadViewHelper.restore();
    }

    @Override
    public void showEmpty() {
        BlankLayout blankLayout = loadViewHelper.showEmpty("暂无数据");
    }

    @Override
    public String getSellerId() {
        return sellerId;
    }

    /**
     * 设置界面数据
     */
    @Override
    public void setAngelDaetailDate(AngelMerchantsBean angelMerchantsBean) {



        angelName.setText(angelMerchantsBean.getShopName());
        angelRatingBar.setRating(angelMerchantsBean.getSellerCredit());

        Drawable drawableAngelAbove= getResources().getDrawable(R.drawable.angel_above);
        Drawable drawableAngelFlat= getResources().getDrawable(R.drawable.angel_flat);
        Drawable drawableAngelBelow= getResources().getDrawable(R.drawable.angel_below);

        //正能量信息
        angelCredit.setText("正能量" + angelMerchantsBean.getSellerCredit());
        int angelCreditRetval = Float.compare(angelMerchantsBean.getSellerCredit(), ANGEL_LEVEL);
        //返回值：如果f1在数字上等于f2，则返回0；如果f1在数字上小于f2，则返回小于0的值；如果f1在数字上大于f2，则返回大于0的值。
        if (angelCreditRetval > 0) {
            angelCreditCompare.setText(R.string.above_level);
            // 这一步必须要做,否则不会显示.
            drawableAngelAbove.setBounds(0, 0, drawableAngelAbove.getMinimumWidth(), drawableAngelAbove.getMinimumHeight());
            angelCreditCompare.setCompoundDrawables(drawableAngelAbove, null, null, null);
        } else if (angelCreditRetval < 0) {
            angelCreditCompare.setText(R.string.below_level);
            // 这一步必须要做,否则不会显示.
            drawableAngelBelow.setBounds(0, 0, drawableAngelAbove.getMinimumWidth(), drawableAngelAbove.getMinimumHeight());
            angelCreditCompare.setCompoundDrawables(drawableAngelBelow, null, null, null);
        } else {
            angelCreditCompare.setText(R.string.equal_level);
            // 这一步必须要做,否则不会显示.
            drawableAngelFlat.setBounds(0, 0, drawableAngelAbove.getMinimumWidth(), drawableAngelAbove.getMinimumHeight());
            angelCreditCompare.setCompoundDrawables(drawableAngelFlat, null, null, null);
        }

        //天使服务信息
        angelServe.setText("天使服务" + angelMerchantsBean.getSellerServe());
        int angelServeRetval = Float.compare(angelMerchantsBean.getSellerServe(), ANGEL_LEVEL);
        //返回值：如果f1在数字上等于f2，则返回0；如果f1在数字上小于f2，则返回小于0的值；如果f1在数字上大于f2，则返回大于0的值。
        if (angelServeRetval > 0) {
            angelServeCompare.setText(R.string.above_level);
            // 这一步必须要做,否则不会显示.
            drawableAngelAbove.setBounds(0, 0, drawableAngelAbove.getMinimumWidth(), drawableAngelAbove.getMinimumHeight());
            angelServeCompare.setCompoundDrawables(drawableAngelAbove, null, null, null);
        } else if (angelServeRetval < 0) {
            angelServeCompare.setText(R.string.below_level);
            // 这一步必须要做,否则不会显示.
            drawableAngelBelow.setBounds(0, 0, drawableAngelAbove.getMinimumWidth(), drawableAngelAbove.getMinimumHeight());
            angelServeCompare.setCompoundDrawables(drawableAngelBelow, null, null, null);
        } else {
            angelServeCompare.setText(R.string.equal_level);
            // 这一步必须要做,否则不会显示.
            drawableAngelFlat.setBounds(0, 0, drawableAngelAbove.getMinimumWidth(), drawableAngelAbove.getMinimumHeight());
            angelServeCompare.setCompoundDrawables(drawableAngelFlat, null, null, null);
        }

        //天使售后信息
        angelAfter.setText("天使售后" + angelMerchantsBean.getSellerServe());
        int angelAfterRetval = Float.compare(angelMerchantsBean.getSellerAfter(), ANGEL_LEVEL);
        //返回值：如果f1在数字上等于f2，则返回0；如果f1在数字上小于f2，则返回小于0的值；如果f1在数字上大于f2，则返回大于0的值。
        if (angelAfterRetval > 0) {
            angelAfterCompare.setText(R.string.above_level);
            // 这一步必须要做,否则不会显示.
            drawableAngelAbove.setBounds(0, 0, drawableAngelAbove.getMinimumWidth(), drawableAngelAbove.getMinimumHeight());
            angelAfterCompare.setCompoundDrawables(drawableAngelAbove, null, null, null);
        } else if (angelAfterRetval < 0) {
            angelAfterCompare.setText(R.string.below_level);
            // 这一步必须要做,否则不会显示.
            drawableAngelBelow.setBounds(0, 0, drawableAngelAbove.getMinimumWidth(), drawableAngelAbove.getMinimumHeight());
            angelAfterCompare.setCompoundDrawables(drawableAngelBelow, null, null, null);
        } else {
            angelAfterCompare.setText(R.string.equal_level);
            // 这一步必须要做,否则不会显示.
            drawableAngelFlat.setBounds(0, 0, drawableAngelAbove.getMinimumWidth(), drawableAngelAbove.getMinimumHeight());
            angelAfterCompare.setCompoundDrawables(drawableAngelFlat, null, null, null);
        }


        if (!TextUtils.isEmpty(angelMerchantsBean.getAddress()))
            angelAdress.setText(angelMerchantsBean.getAddress());

        if (!TextUtils.isEmpty(angelMerchantsBean.getStatement()))
            angelStatement.setText(angelMerchantsBean.getStatement());


        //头像
        if (!TextUtils.isEmpty(angelMerchantsBean.getSellerImg())) {
            Glide.with(this)//更改图片加载框架
                    .load(PulamsiApplication.context.getString(R.string.serverbaseurl) + angelMerchantsBean.getSellerImg())
//                    .centerCrop()圆形图片不能设置此参数，否则无效
                    .placeholder(R.drawable.angel_default_photo)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new GlideCircleTransform(this))//glide设置成圆形图片
                    .into(angelImg);
        }
    }

    @Override
    public void onRetry() {
        //重新加载按钮在这里响应
        angelDetailsActivityPrestener.request();
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
