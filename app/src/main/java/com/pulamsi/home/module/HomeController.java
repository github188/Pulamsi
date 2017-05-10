package com.pulamsi.home.module;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.angel.AngelActivity;
import com.pulamsi.angel.AngelDetailsActivity;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseListview.CommonListLoadMoreHandler;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.category.adapter.CategroyTopCatListAdapter;
import com.pulamsi.constant.Constants;
import com.pulamsi.home.adapter.HomeBeautifulAngleAdapter;
import com.pulamsi.home.adapter.HomeCateGoodListAdapter;
import com.pulamsi.home.adapter.HomeDianzhuListAdapter;
import com.pulamsi.home.adapter.HomeSnapUpListAdapter;
import com.pulamsi.home.adapter.NoticeAdapter;
import com.pulamsi.home.entity.AdverNotice;
import com.pulamsi.home.entity.AngelData;
import com.pulamsi.home.entity.AngelMerchantsBean;
import com.pulamsi.home.entity.ChannelMobile;
import com.pulamsi.home.entity.ChannelMobileTotal;
import com.pulamsi.home.entity.HotSellProduct;
import com.pulamsi.home.entity.IndexSource;
import com.pulamsi.home.entity.SearchGoodsList;
import com.pulamsi.home.entity.SnapUpData;
import com.pulamsi.integral.IntegralStoreActivity;
import com.pulamsi.search.SearchDoorActivity;
import com.pulamsi.search.SearchListActivity;
import com.pulamsi.snapup.SnapUpActivity;
import com.pulamsi.start.MainActivity;
import com.pulamsi.utils.DateUtils;
import com.pulamsi.utils.GoodsHelper;
import com.pulamsi.utils.bean.DatePoorBean;
import com.pulamsi.views.PtrStylelFrameLayout;
import com.pulamsi.views.SnapUpCountDownTimerView.SnapUpCountDownTimerView;
import com.pulamsi.views.gallery.BannerAdapter;
import com.pulamsi.views.gallery.DotContainer;
import com.pulamsi.views.gallery.SliderBanner;
import com.pulamsi.views.horizontalRecycleViewLoadMore.HorizontaloadMoreRecycleView;
import com.pulamsi.views.qrcode.activity.CaptureActivity;
import com.pulamsi.views.rollAdverView.RollAdverView;
import com.pulamsi.webview.CommonWebViewActivity;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lanqiang on 15/11/23.
 * FIXME
 * 跳转列表不完善
 */
public class HomeController implements HorizontaloadMoreRecycleView.LoadMoreListener {

    /**
     * 下拉透明
     */
    //默认的透明度最大值（255）
    private static final int MAX_ALPHA = 255;
    private int mAlpha = 0;

    RelativeLayout rl_seach_bg;

    /**
     * activity
     */
    private Activity activity;
    /**
     * list view 滚动的总距离
     */
    private int mScrollY;

    /**
     * headerView
     */
    private View headerView;
    /**
     * rootview
     */
    private View rootView;
    /**
     * 精选推荐列表
     */
    private TRecyclerView tuijianTRecyclerView;
    /**
     * 店主推荐列表
     */
    private TRecyclerView dianzhuTRecyclerView;
    /**
     * 猜你喜欢列表列表
     */
    private TRecyclerView likeTRecyclerView;

    /**
     * 精选推荐适配器
     */
    private HomeCateGoodListAdapter tuijianGoodAdapter;
    /**
     * 猜你喜欢适配器
     */
    private HomeCateGoodListAdapter likeGoodAdapter;

    /**
     * 店主适配器
     */
    private HomeDianzhuListAdapter dianzhuGoodAdapter;
    /**
     * 猜你喜欢包装类，包括网络请求和下一页操作
     */
    private HomeMoreGoodsWrapperGet likeWrapper;
    /**
     * 精选推荐进度条
     */
    private ProgressWheel tuijianprogressWheel;
    /**
     * 店主推荐进度条
     */
    private ProgressWheel dianzhuprogressWheel;

    /**
     * 猜你喜欢进度条
     */
    private ProgressWheel likeprogressWheel;
    /**
     * 下拉刷新组件布局
     */
    private PtrStylelFrameLayout ptrStylelFrameLayout;
    /**
     * 回顶按钮
     */
    private FloatingActionButton floatingActionButton;
    /**
     * 图片轮播控件
     */
    private SliderBanner mSliderBanner;
    /**
     * 图片轮播点
     */
    private DotContainer mDotContainer;

    /**
     * 回顶按钮渐变动画
     */
    private AnimationSet animationset;
    AlphaAnimation alphaanimation;

    View home_search;
    /**
     * 倒计时控件
     */
    private SnapUpCountDownTimerView countDownTimer;
    /**
     * 横向滑动在家更多的ReycleView
     */
    private HorizontaloadMoreRecycleView horizontaloadMoreRecycleView;

    /**
     * 抢购数据适配器
     */
    private HomeSnapUpListAdapter homeSnapUpListAdapter;
    /**
     * 天使数据适配器
     */
    private HomeBeautifulAngleAdapter homeBeautifulAngleAdapter;

    /**
     * 普兰氏快报控件
     */
    public RollAdverView rollAdverView;
    /**
     * 抢购更多按钮
     */
    private TextView snapUpMore;
    /**
     * 天使更多按钮
     */
    private TextView angelMore;

    /**
     * 天使抢购List
     */
    private TRecyclerView anglerRecycleView;

    /**
     * 天使列表数据
     */
    private List<AngelMerchantsBean> angelMerchantsBeanList;



    /**
     * 图片轮播点击监听器
     */
    private View.OnClickListener mOnItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Object tag = view.getTag();
            if (tag == null || !(tag instanceof InnerBannerItemDataWrapper)) {
                return;
            }
            InnerBannerItemDataWrapper wrapper = (InnerBannerItemDataWrapper) tag;
            IndexSource item = wrapper.mItem;
            LogUtils.e(item.getMold() + "<<<类型：1商品，2分类，3通知");//类型：1商品，2分类，3通知
            String url = item.getUrl();
            if (TextUtils.isEmpty(url))
                return;
            if (url.contains("keyword")) {
                if (url.contains("cat")) {//判断是跳列表
                    BaseAppManager.getInstance().clear();
                    MainActivity.mTabView.setCurrentTab(1);
                    CategroyTopCatListAdapter.setGotoCatTagPosition(true, 4);
                } else {//判断是跳搜索
                    String[] split = item.getUrl().split("keyword=");
                    Intent search = new Intent(activity, SearchListActivity.class);
                    try {
                        search.putExtra("keyword", URLDecoder.decode(split[1], "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    activity.startActivity(search);
                }
            } else {
                //跳商品详情页
                GoodsHelper.gotoDetail(item.getMemo(), activity);

            }
        }
    };


    /**
     * 设置View的布局属性，使得view随着手指移动 注意：view所在的布局必须使用RelativeLayout 而且不得设置居中等样式
     *
     * @param view
     * @param rawX
     * @param rawY
     */
    private void moveViewWithFinger(View view, float rawX, float rawY) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
                .getLayoutParams();
        params.leftMargin = (int) rawX - PulamsiApplication.ScreenWidth / 2;
        params.topMargin = (int) rawY - PulamsiApplication.ScreenHeight / 2;
        view.setLayoutParams(params);
    }


    public void initUI(FloatingActionButton floatingActionButton, View rootView, View headerView, final Activity activity) {
        //默认为全透明
        home_search = rootView.findViewById(R.id.home_search);
        home_search.getBackground().setAlpha(0);
        rl_seach_bg = (RelativeLayout) rootView.findViewById(R.id.rl_seach_bg);
        likeprogressWheel = (ProgressWheel) headerView.findViewById(R.id.pw_home_activity_like);
        likeTRecyclerView = (TRecyclerView) rootView.findViewById(R.id.home_trecyclerview);
        this.headerView = headerView;
        this.activity = activity;
        this.rootView = rootView;
        mSliderBanner = (SliderBanner) headerView.findViewById(R.id.sb_home_banner);

        mDotContainer = (DotContainer) headerView.findViewById(R.id.dc_home_banner_indicator);
        initSliderBannerData();
        this.floatingActionButton = floatingActionButton;
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //回顶操作
                likeTRecyclerView.scrollBy(0, -mScrollY);
            }
        });
        likeTRecyclerView.addHeaderView(headerView);
        ptrStylelFrameLayout = (PtrStylelFrameLayout) rootView.findViewById(R.id.ptr_home_material_style_ptr_frame);
        dianzhuTRecyclerView = (TRecyclerView) headerView.findViewById(R.id.home_dianzhu_trecyclerview);
        tuijianTRecyclerView = (TRecyclerView) headerView.findViewById(R.id.home_tuijian_trecyclerview);
        tuijianprogressWheel = (ProgressWheel) headerView.findViewById(R.id.pw_home_activity_tuijian);
        dianzhuprogressWheel = (ProgressWheel) headerView.findViewById(R.id.pw_home_activity_dianzhu);

        //抢购
        countDownTimer = (SnapUpCountDownTimerView) headerView.findViewById(R.id.sudtv_countDownTimerView);
        horizontaloadMoreRecycleView = (HorizontaloadMoreRecycleView) headerView.findViewById(R.id.hmr_horizontaloadMoreRecycleView);
        rollAdverView = (RollAdverView) headerView.findViewById(R.id.rav_adverView);
        snapUpMore = (TextView) headerView.findViewById(R.id.tv_snapUp_more);
        angelMore = (TextView) headerView.findViewById(R.id.tv_angel_more);

        //天使
        anglerRecycleView = (TRecyclerView) headerView.findViewById(R.id.angle_recycleView);


        //初始化普兰氏快报
        initRollAdver();

        //抢购初始化
        initSnapUp();

        //天使初始化
        initBeautifulAngle();

        //精选推荐初始化
        initTuijianData();

        /**
         * 隐藏这个，好丑
         */
        //店主推荐初始化
//        initDianzhuData();

        //猜你喜欢初始化包装类
        likeGoodAdapter = new HomeCateGoodListAdapter(activity);
        likeWrapper = new HomeMoreGoodsWrapperGet(activity);

//       初始化界面点击事件
        initHeaderOnClick();

        //recycleView布局管理器
        InnerOnScrollListener onScrollListener = new InnerOnScrollListener();
        CommonListLoadMoreHandler likeloadMoreHandler = new CommonListLoadMoreHandler();
        StaggeredGridLayoutManager likelayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        likeWrapper.setListAdapter(likeGoodAdapter)
                .setPtrStylelFrameLayout(ptrStylelFrameLayout)
                .setProgressWheel(likeprogressWheel)
                .setOnScrollListener(onScrollListener)
                .setListView(likeTRecyclerView)
                .setLayoutManager(likelayoutManager)
                .setLoadMoreHandler(likeloadMoreHandler).init();
        likeTRecyclerView.setHasFixedSize(true);

        likeTRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                GoodsHelper.gotoDetail(likeGoodAdapter.getItem(position).getId(), activity);

            }
        });

    }




    private void initAnimation(int start, int end, View view) {
        animationset = new AnimationSet(true);
        alphaanimation = new AlphaAnimation(start, end);
        alphaanimation.setDuration(500);
        animationset.addAnimation(alphaanimation);
        view.startAnimation(animationset);
    }

    /**
     * 初始化界面点击事件
     */
    private void initHeaderOnClick() {
        //售货机
        TextView slotmachine = (TextView) headerView.findViewById(R.id.home_header_slotmachine);
        slotmachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mTabView.setCurrentTab(2);
            }
        });
        //分类
        TextView category = (TextView) headerView.findViewById(R.id.home_header_category);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mTabView.setCurrentTab(1);
            }
        });
        //积分
        TextView jf = (TextView) headerView.findViewById(R.id.home_header_jf);
        jf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent integral = new Intent(activity, IntegralStoreActivity.class);
                activity.startActivity(integral);
            }
        });
        //我的
        TextView myinfo = (TextView) headerView.findViewById(R.id.home_header_myinfo);
        myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mTabView.setCurrentTab(4);
            }
        });

//        rl_search.getBackground().setAlpha(100);
        //头部界面点击事件
        ImageView leftimage = (ImageView) rootView.findViewById(R.id.home_search_leftimage);
        leftimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //左边图片点击事件
            }
        });
        ImageView rightimage = (ImageView) rootView.findViewById(R.id.home_search_rightimage);
        rightimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //右边图片点击事件
                //扫描二维码
                activity.startActivity(new Intent(activity, CaptureActivity.class));
            }
        });
        RelativeLayout searchEdit = (RelativeLayout) rootView.findViewById(R.id.home_search_edit);
        searchEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击搜索框
                Intent search = new Intent(activity, SearchDoorActivity.class);
                activity.startActivity(search);
            }
        });
        //抢购加载更多
        snapUpMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                跳转抢购更多页面
                Intent intent = new Intent(activity, SnapUpActivity.class);
                activity.startActivity(intent);
            }
        });

        //天使在家更多
        angelMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                跳转天使更多页面
                Intent intent = new Intent(activity, AngelActivity.class);
                activity.startActivity(intent);
            }
        });

    }


    /**
     * 初始化普兰氏快报
     */
    private void initRollAdver() {//fix
        List<AdverNotice> datas = new ArrayList<AdverNotice>();
        datas.add(new AdverNotice("白天时间最长的夏至,你的皮肤还好吗？", "最新", "http://mp.weixin.qq.com/s?__biz=MzAwMjI1NDE4OQ==&mid=2652180895&idx=1&sn=a75fac59673447ced6ca253f4a8a779d&scene=23&srcid=07082vvBYPNlY1mrXA1y7etO#rd"));
        datas.add(new AdverNotice("防晒，我们到底在防什么？", "New", "http://mp.weixin.qq.com/s?__biz=MzAwMjI1NDE4OQ==&mid=2652180904&idx=1&sn=67fd6929a165b657ff5875ae1c794041&scene=23&srcid=07088t6TCQ5mGbBInBCDOdw2#rd"));
        datas.add(new AdverNotice("今日头条！普兰氏总冠名《全球华语流行音乐金曲榜》", "HOT", "http://mp.weixin.qq.com/s?__biz=MzAwMjI1NDE4OQ==&mid=2652180915&idx=1&sn=0e88fbbb7e7794726a606e75ab79e8e3&scene=23&srcid=0708J2AwD5CEizw69KpDSng0#rd"));
        datas.add(new AdverNotice("如此猴赛雷的投资项目，必须看一看", "New", "http://mp.weixin.qq.com/s?__biz=MzAwMjI1NDE4OQ==&mid=2652180839&idx=1&sn=296e28985299d59aa5a4215af091ac90&scene=23&srcid=0708YxcClRsvgrexEn7rVOCm#rd"));
        NoticeAdapter adapter = new NoticeAdapter(datas, activity);
        rollAdverView.setAdapter(adapter);
    }

    /**
     * 抢购初始化
     */
    private void initSnapUp() {

        homeSnapUpListAdapter = new HomeSnapUpListAdapter(activity);
        horizontaloadMoreRecycleView.setAdapter(homeSnapUpListAdapter);
//        horizontaloadMoreRecycleView.addItemDecoration(new VerticalDividerItemDecoration.Builder(activity)//垂直改为Vertical
//                .color(activity.getResources().getColor(R.color.app_divider_line_bg_cc))
//                .size(1)
//                .build());//分割线
    }


    /**
     * 初始化天使
     */
    private void initBeautifulAngle() {
//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity,4);
        anglerRecycleView.setLayoutManager(gridLayoutManager);
        anglerRecycleView.setHasFixedSize(true);
        homeBeautifulAngleAdapter = new HomeBeautifulAngleAdapter(activity);
        anglerRecycleView.setAdapter(homeBeautifulAngleAdapter);
        anglerRecycleView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                Intent intent = new Intent(activity, AngelDetailsActivity.class);
                intent.putExtra("sellerId", homeBeautifulAngleAdapter.getItem(position).getId());//传递天使ID
                activity.startActivity(intent);
            }
        });
    }

    /**
     * 限时抢购加载更多回调
     */
    @Override
    public void LoadMore() {
        //                跳转抢购更多页面
        Intent intent = new Intent(activity, SnapUpActivity.class);
        activity.startActivity(intent);
    }


    /**
     * 初始化店主推荐数据和适配器,监听器
     */
    private void initDianzhuData() {
        dianzhuGoodAdapter = new HomeDianzhuListAdapter(activity);
        StaggeredGridLayoutManager dianzhulayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        dianzhuTRecyclerView.setAdapter(dianzhuGoodAdapter);
        dianzhuTRecyclerView.setLayoutManager(dianzhulayoutManager);
        dianzhuTRecyclerView.setHasFixedSize(true);
        dianzhuTRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                HaiLog.d("dianzhu", position + "");
                Intent taobao = new Intent(activity, CommonWebViewActivity.class);
                taobao.putExtra(Constants.WEBVIEW_LOAD_URL, dianzhuGoodAdapter.getItem(position).getUrl());
                taobao.putExtra(Constants.WEBVIEW_TITLE, "店铺详情");
                activity.startActivity(taobao);
            }
        });
    }

    /**
     * 初始化推荐商品数据和适配器,监听器
     */
    private void initTuijianData() {
        tuijianGoodAdapter = new HomeCateGoodListAdapter(activity);
        StaggeredGridLayoutManager tuijianlayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        tuijianTRecyclerView.setLayoutManager(tuijianlayoutManager);
        tuijianTRecyclerView.setAdapter(tuijianGoodAdapter);
        tuijianTRecyclerView.setHasFixedSize(true);
        tuijianTRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                HaiLog.d("tuijian", position + "");
//                Intent  intent = new Intent(activity, GoodDetailActivity.class);
//                activity.startActivity(intent);
                GoodsHelper.gotoDetail(tuijianGoodAdapter.getItem(position).getId(), activity);

            }
        });
    }


    /**
     * 初始化图片轮播数据
     */
    private void initSliderBannerData() {
        try {
            ArrayList<IndexSource> indexSources = (ArrayList<IndexSource>) PulamsiApplication.dbUtils.findAll(IndexSource.class);
            if (null != indexSources && indexSources.size() > 0) {
                updateImgBanner(indexSources);
            } else {
                getSliderBannerData();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void RequestData() {
        RequestTuijianAndDianzhuData();
        Map<String, String> bizParams = new HashMap<>();
        bizParams.put("pageNumber", "1");
        likeWrapper.startQuery(PulamsiApplication.context.getString(R.string.serverbaseurl) + PulamsiApplication.context.getString(R.string.productSearch), bizParams);
    }


    /**
     * 初始化店主和推荐商品数据
     */
    private void RequestTuijianAndDianzhuData() {
        try {
            /**
             * 推荐商品
             */
            ArrayList<HotSellProduct> hotSellProducts = (ArrayList<HotSellProduct>) PulamsiApplication.dbUtils.findAll(HotSellProduct.class);
            if (null != hotSellProducts && hotSellProducts.size() > 0) {
                updateTuijianList(hotSellProducts);
            } else {
                getHotSellProductData();
            }
            /**
             * 店主推荐
             */
//            ArrayList<ChannelMobile> channelMobiles = (ArrayList<ChannelMobile>) PulamsiApplication.dbUtils.findAll(ChannelMobile.class);
//            if (null != channelMobiles && channelMobiles.size() > 0) {
//                updateDianzhuList(channelMobiles);
//            } else {
//                getChannelData();
//            }
            /**
             * 最美天使
             */
            ArrayList<AngelMerchantsBean> merchantsBeanList = (ArrayList<AngelMerchantsBean>) PulamsiApplication.dbUtils.findAll(AngelMerchantsBean.class);
            if (null != merchantsBeanList && merchantsBeanList.size() > 0) {
                updateAngleList(merchantsBeanList);//更新天使数据
            } else {
                getAngleData();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        /**
         * 限时抢购
         */
        getSnapUpData();



    }


    /**
     * 推荐商品数据
     */
    public void updateTuijianList(List<HotSellProduct> data) {
        //  状态还原
        int formerItemCount = tuijianGoodAdapter.getItemCount();
        tuijianGoodAdapter.clearDataList();
        tuijianGoodAdapter.notifyItemRangeRemoved(0, formerItemCount);

        tuijianGoodAdapter.addDataList(data);
        tuijianGoodAdapter.notifyItemRangeInserted(0, data.size());
        tuijianTRecyclerView.getLayoutParams().height =
                PulamsiApplication.context.getResources().getDimensionPixelOffset(R.dimen.home_recommend_list_item_height) * data.size() / 2;
        tuijianprogressWheel.setVisibility(View.GONE);
        tuijianTRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * 店主推荐数据
     */
    public void updateDianzhuList(List<ChannelMobile> data) {
        //  状态还原
        int formerItemCount = dianzhuGoodAdapter.getItemCount();
        dianzhuGoodAdapter.clearDataList();
        dianzhuGoodAdapter.notifyItemRangeRemoved(0, formerItemCount);

        dianzhuGoodAdapter.addDataList(data);
        dianzhuGoodAdapter.notifyItemRangeInserted(0, data.size());
        dianzhuTRecyclerView.getLayoutParams().height =
                PulamsiApplication.context.getResources().getDimensionPixelOffset(R.dimen.home_recommend_list_item_height) * data.size() / 2;
        dianzhuprogressWheel.setVisibility(View.GONE);
        dianzhuTRecyclerView.setVisibility(View.VISIBLE);
    }


    /**
     * recyclerview滚动距离
     */
    private class InnerOnScrollListener extends HomeMoreGoodsWrapperGet.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            // 累加 list view 的滚动距离
            mScrollY += dy;
            if (mScrollY < 255) {
                if (mScrollY == 0) {
                    rl_seach_bg.getBackground().setAlpha(255);
                } else {
                    rl_seach_bg.getBackground().setAlpha(0);
                }
                home_search.getBackground().setAlpha(mScrollY);
//                Drawable a = home_search.getBackground();//安卓3.0以下使用这个
//                a.setAlpha(mScrollY);
//                home_search.setBackgroundDrawable(a);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mAlpha = home_search.getBackground().getAlpha();
                    if (mAlpha < 255)
                        home_search.getBackground().setAlpha(240);//更改透明度
                    rl_seach_bg.getBackground().setAlpha(0);
                } else {
                    home_search.getBackground().setAlpha(240);//更改透明度
                    rl_seach_bg.getBackground().setAlpha(0);
                }
            }
            //大于屏幕高度才出现回顶按钮
            if (mScrollY > PulamsiApplication.ScreenHeight) {
                //判断View是否显示
                if (floatingActionButton.getVisibility() == View.GONE) {
                    floatingActionButton.setVisibility(View.VISIBLE);
                    initAnimation(0, 1, floatingActionButton);
                }
            } else {
                //判断View是否显示
                if (floatingActionButton.getVisibility() == View.VISIBLE) {
                    initAnimation(1, 0, floatingActionButton);
                    floatingActionButton.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 更新商品详情图片 banner 数据
     *
     * @param imgBannerItems 图片 banner 数据
     */
    public void updateImgBanner(List<IndexSource> imgBannerItems) {
        if (imgBannerItems == null || imgBannerItems.size() == 0) {
            return;
        }

        GoodImgBannerAdapter bannerAdapter = new GoodImgBannerAdapter();
        bannerAdapter.setData(imgBannerItems);
        mSliderBanner.setAdapter(bannerAdapter);
        mDotContainer.setNum(imgBannerItems.size());
        mSliderBanner.stopPlay();
        mSliderBanner.beginPlay();
    }

    /**
     * 更新限时抢购数据
     */
    private void updateSnapUpList(final List<HotSellProduct> data) {
        //  状态还原
        int formerItemCount = homeSnapUpListAdapter.getItemCount();
        homeSnapUpListAdapter.clearDataList();
        homeSnapUpListAdapter.notifyItemRangeRemoved(0, formerItemCount);
        homeSnapUpListAdapter.addDataList(data);
        homeSnapUpListAdapter.notifyItemRangeInserted(0, data.size());

        if (data.size() >= 5) {
            View moreView = View.inflate(activity, R.layout.snap_up_more, null);
            horizontaloadMoreRecycleView.addFooterView(moreView);
            horizontaloadMoreRecycleView.setOnLoadMoreListener(this);
        }
        /**
         * 抢购点击事件
         */
        horizontaloadMoreRecycleView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                GoodsHelper.gotoSnapUpDetail(data.get(position).getId(), activity);
            }
        });
    }




    /**
     * 更新抢购倒计时
     *
     * @param panicBuyEndTime
     */
    private void updateSnapUpCountDownTimer(long panicBuyEndTime) {
        Long nowTime = System.currentTimeMillis();  //获取当前时间戳
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDate = new Date(Long.parseLong(String.valueOf(nowTime)));
        String sNowTime = sdf.format(nowDate);   // 时间戳转换成时间
        Date BuyEndTime = new Date(Long.parseLong(String.valueOf(panicBuyEndTime)));
        String sEndTime = sdf.format(BuyEndTime);   // 时间戳转换成时间
        LogUtils.e("当前：" + sNowTime);
        LogUtils.e("结束：" + sEndTime);
        DatePoorBean datePoor = DateUtils.getDatePoor(BuyEndTime, nowDate);//抢购时间包含天，小时，分钟，秒
        LogUtils.e(datePoor.toString());
//      endDate.after(nowDate);//当endDate大于nowDate时，返回TRUE，当小于等于时，返回false;
        countDownTimer.setTime((int) datePoor.getHour(), (int) datePoor.getMin(), (int) datePoor.getSec());
        countDownTimer.start();
    }


    /**
     * 更新天使数据
     * @param angelMerchantsBeanList
     */
    private void updateAngleList(List<AngelMerchantsBean> angelMerchantsBeanList){
        //  状态还原
        int formerItemCount = homeBeautifulAngleAdapter.getItemCount();
        homeBeautifulAngleAdapter.clearDataList();
        homeBeautifulAngleAdapter.notifyItemRangeRemoved(0, formerItemCount);
        homeBeautifulAngleAdapter.addDataList(angelMerchantsBeanList);
        homeBeautifulAngleAdapter.notifyItemRangeInserted(0, angelMerchantsBeanList.size());
    }


    /**
     * 商品详情图片 banner 数据适配器
     */
    private class GoodImgBannerAdapter extends BannerAdapter<IndexSource> {


        @Override
        public View getView(LayoutInflater layoutInflater, int position) {
            View convertView = layoutInflater.inflate(R.layout.home_banner_item, null);
            IndexSource item = getItem(position);
            if (item == null) {
                return convertView;
            }
//            <!--旧得部分保留，已知BUG:滑动出轮播图，图片需要重新加载-->
//            CubeImageView imageView = (CubeImageView) convertView.findViewById(R.id.home_banner_pic);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.home_banner_pic);
            if (!TextUtils.isEmpty(item.getImg())) {
                Glide.with(activity)//更改图片加载框架
                        .load(PulamsiApplication.context.getString(R.string.serverbaseurl) + item.getImg())
                        .centerCrop()
                        .placeholder(R.drawable.pulamsi_loding)
                        .crossFade()
                        .priority(Priority.IMMEDIATE)//图片加载优先级，最高
                        .into(imageView);
//                PulamsiApplication.imageLoader.displayImage(PulamsiApplication.context.getString(R.string.serverbaseurl) + item.getImg(), imageView, PulamsiApplication.options);
            }

            convertView.setOnClickListener(mOnItemClickListener);
            convertView.setTag(new InnerBannerItemDataWrapper(getPositionForIndicator(position), item));
            return convertView;
        }
    }

    private static class InnerBannerItemDataWrapper {

        private IndexSource mItem;
        private int mIndex;

        private InnerBannerItemDataWrapper(int index, IndexSource item) {
            mIndex = index;
            mItem = item;
        }
    }

    /**
     * 获取首页轮播图片数据(当在数据库取不到数据的时候调用)
     */
    public void getSliderBannerData() {
        String indexSourceurlPath = PulamsiApplication.context.getString(R.string.serverbaseurl) + PulamsiApplication.context.getString(R.string.indexSource) + "?begin=2002&end=2005";
        StringRequest indexSourceRequest = new StringRequest(Request.Method.GET,
                indexSourceurlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        List<IndexSource> indexSources = gson.fromJson(
                                result, new TypeToken<List<IndexSource>>() {
                                }.getType());
                        updateImgBanner(indexSources);

                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "获取首页轮播图片数据装配错误");
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
            }
        });
        indexSourceRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(indexSourceRequest);
    }

    /**
     * 获取推荐商品数据(当在数据库取不到数据的时候调用)
     */
    public void getHotSellProductData() {
        String URL1 = PulamsiApplication.context.getString(R.string.serverbaseurl) + PulamsiApplication.context.getString(R.string.productSearch) + "?productName=&pageNumber=1&pageSize=10&searchProperty=&searchValue=";
        StringRequest dataRequest = new StringRequest(Request.Method.GET,
                URL1, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        SearchGoodsList searchGoodsList = gson.fromJson(
                                result, SearchGoodsList.class);
                        updateTuijianList(searchGoodsList.getList());
                    } catch (Exception e) {
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                //请求网络失败
            }
        });
        dataRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(dataRequest);
    }

    /**
     * 获取店主推荐数据(当在数据库取不到数据的时候调用)
     */
    public void getChannelData() {
        String channelMgrImplurlPath = PulamsiApplication.context.getString(R.string.serverbaseurl)
                + PulamsiApplication.context.getString(R.string.channelMgrImplurl)
                + "?pageNumber=0&pageSize=8";
        StringRequest channelstringRequest = new StringRequest(Request.Method.GET,
                channelMgrImplurlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        ChannelMobileTotal channelMobileTotal = gson
                                .fromJson(result,
                                        ChannelMobileTotal.class);
                        updateDianzhuList(channelMobileTotal.getList());
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "首页店主推荐数据装配出错");
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
            }
        });
        channelstringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(channelstringRequest);
    }


    /**
     * 获取限时抢购的列表数据
     */

    private void getSnapUpData() {
        String snapUpUrl = activity.getString(R.string.serverbaseurl) + activity.getString(R.string.snapUpHomeList);
        StringRequest snapUpRequest = new StringRequest(Request.Method.GET, snapUpUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        SnapUpData snapUpData = gson.fromJson(result, SnapUpData.class);
                        List<HotSellProduct> snapUpList = snapUpData.getProList();
                        updateSnapUpList(snapUpList);//更新抢购数据
                        updateSnapUpCountDownTimer(snapUpData.getPanicBuyEndTime());
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "首页限时抢购数据装配出错");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        snapUpRequest.setRetryPolicy(new DefaultRetryPolicy(1000 * 10, 0, 1.0f));
        PulamsiApplication.requestQueue.add(snapUpRequest);
    }


    /**
     * 获取最美天使的列表数据(当在数据库取不到数据的时候调用)
     */

    private void getAngleData(){
        String snapUpUrl = activity.getString(R.string.serverbaseurl) + activity.getString(R.string.angleHomeList);
        StringRequest angleRequest = new StringRequest(Request.Method.POST, snapUpUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        AngelData angerData = gson.fromJson(result, AngelData.class);
                        angelMerchantsBeanList = angerData.getSellerList();
                        updateAngleList(angelMerchantsBeanList);//更新天使数据
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "首页天使数据装配出错");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        angleRequest.setRetryPolicy(new DefaultRetryPolicy(1000 * 10, 0, 1.0f));
        PulamsiApplication.requestQueue.add(angleRequest);

    }
}
