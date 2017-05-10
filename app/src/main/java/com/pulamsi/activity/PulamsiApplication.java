package com.pulamsi.activity;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.pulamsi.R;
import com.pulamsi.constant.Constants;
import com.pulamsi.slotmachine.entity.Area;
import com.pulamsi.slotmachine.utils.SlotmachineDBhelper;
import com.pulamsi.start.init.entity.User;
import com.pulamsi.start.init.utils.DBManager;
import com.pulamsi.start.init.utils.InitUtils;
import com.pulamsi.views.selector.bean.ProvinceBean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * 普兰氏全局application，做一些app初始化时的全局配置工作
 */


public class PulamsiApplication extends Application {
    /**
     * 全局实例化对象
     */
    public static Application context;
    /**
     * 网络请求对象
     */
    public static RequestQueue requestQueue;
    /**
     * 屏幕参数
     */
    public static float ScreenDensity = 0;
    public static int ScreenWidth = 480;
    public static int ScreenHeight = 800;
    /**
     * activity记录集合
     */
    public static List<Activity> activityLists = new LinkedList<Activity>();
    /**
     * 主框架的Activity
     */
    public static FragmentActivity fragmentActivity;
    /**
     * 图片加载实例对象
     */
    public static ImageLoader imageLoader;
    /**
     * 是否自动切换图片质量
     */
    public static boolean autoSwitchPic;
    /**
     * 是否切换到了高质量图片
     */
    public static boolean switch2high;

    /**
     * 自动更新
     */
    public static boolean updateIgnore = false;


    /**
     * 显示图片的配置
     */
    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showStubImage(R.drawable.pulamsi_loding)          // image在加载过程中，显示的图片
            .showImageForEmptyUri(R.drawable.pulamsi_loding)  // empty URI时显示的图片
            .showImageOnFail(R.drawable.pulamsi_loding)       // 不是图片文件 显示图片
            .cacheInMemory(true)           // default 不缓存至内存
            .cacheOnDisc(true)             // default 不缓存至手机SDCard
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)// default
            .bitmapConfig(Bitmap.Config.ARGB_8888)              // default
            .displayer(new SimpleBitmapDisplayer()) // default 可以设置动画，比如圆角或者渐变
            .build();
    /**
     * 数据库链接
     */
    public static DbUtils dbUtils;

    /**
     * 选择省市区初始化相关
     */
    public static ArrayList<ProvinceBean> options1Items = new ArrayList<ProvinceBean>();
    public static ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
    public static ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();
    private SlotmachineDBhelper dBhelper;
    private ArrayList<Area> areaProvinces;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        dbUtils = DbUtils.create(PulamsiApplication.context, "pulamsi.db");
        requestQueue = Volley.newRequestQueue(context);
        initApplicationConfig();
    }

    private void initApplicationConfig() {
        initImageLoder();
        initScreenConfig();
        initData();
        initCitySelect();//初始化城市选择器
    }



    private void initCitySelect() {
        new Thread(){
            @Override
            public void run() {
                LogUtils.e("开始执行");
                super.run();
                dBhelper = new SlotmachineDBhelper(context);
                areaProvinces = dBhelper.getProvince();
                for (int i = 0; i < areaProvinces.size(); i++) {
                    Area provincesArea = areaProvinces.get(i);
                    PulamsiApplication.options1Items.add(new ProvinceBean(i, provincesArea.getName(), "", provincesArea.getId()));
                    ArrayList<String> options2Items_ = new ArrayList<String>();
                    ArrayList<ArrayList<String>> options3Items_01_0 = new ArrayList<ArrayList<String>>();
                    ArrayList<Area> city = dBhelper.getCity(provincesArea.getId());
                    for (int j=0;j<city.size();j++){
                        Area cityaArea =  city.get(j);
                        options2Items_.add(cityaArea.getName());
                        ArrayList<String> options3Items_01_1 = new ArrayList<String>();
                        ArrayList<Area> district = dBhelper.getDistrict(cityaArea.getId());
                        for (int k=0;k<district.size();k++){
                            Area districtArea = district.get(k);
                            options3Items_01_1.add(districtArea.getName());
                        }
                        options3Items_01_0.add(options3Items_01_1);
                    }
                    PulamsiApplication.options2Items.add(options2Items_);
                    PulamsiApplication.options3Items.add(options3Items_01_0);
                }
                LogUtils.e("子线程完成");
            }
        }.start();
    }

    /**
     * 初始化省市区数据,首页数据
     */
    private void initData() {
        /**
         * 加载省市区数据库
         */
        DBManager dbHelper = new DBManager(PulamsiApplication.context);
        dbHelper.openDatabase();
        dbHelper.closeDatabase();
        /**
         * 获取首页图片轮播
         */
        InitUtils.getSliderBannerData();
        /**
         * 初始化推荐商品数据
         */
        InitUtils.getHotSellProductData();
        /**
         * 初始化天使商家数据
         */
        InitUtils.getAngleData();
        /**
         * 初始化店主推荐数据
         */
//        InitUtils.getChannelData();
        /**
         * 获取用户权限
         */
        try {
            User user = dbUtils.findFirst(Selector.from(User.class));
            if (null != user && user.isHasLogin()) {
                Constants.userId = user.getUserId();
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化图片加载单例
     */
    private void initImageLoder() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }


    /**
     * 初始化屏幕变量
     */
    private void initScreenConfig() {
        DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
        PulamsiApplication.ScreenDensity = metrics.density;
        if (metrics.widthPixels > metrics.heightPixels) {
            PulamsiApplication.ScreenWidth = metrics.heightPixels;
            PulamsiApplication.ScreenHeight = metrics.widthPixels;
        } else {
            PulamsiApplication.ScreenWidth = metrics.widthPixels;
            PulamsiApplication.ScreenHeight = metrics.heightPixels;
        }
    }
}
