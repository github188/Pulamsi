package com.pulamsi.constant;


import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.category.fragment.CategoryCatFragment;
import com.pulamsi.myinfo.order.ChoicePayTypeActivity;
import com.pulamsi.shoppingcar.module.ShoppingCarController;

/**
 * 常量集合类
 *
 * @author WilliamChik on 2015/7/20
 */
public class Constants {

  public final static String DEFAULT_PRICE_NUM =
          PulamsiApplication.context.getResources().getString(R.string.shopping_car_good_list_default_price_str);

  /**
   * 发短信签名
   */
  public static String SMSkey = "2015pulamsi_com";// 短信key

  /**
   * webview
   */
  public static final String WEBVIEW_LOAD_URL = "webview_load_url";
  public static final String WEBVIEW_TITLE = "webview_title";

  /**
   * 用户id
   */
  public static String userId = null;

  /**
   * 购物车控制器
   */
  public static ShoppingCarController shoppingCarController = null;
  /**
   * 分类页面
   */
  public static CategoryCatFragment categoryCatFragment = null;
  /**
   * 支付界面
   */
  public static ChoicePayTypeActivity choicePayTypeActivity = null;

}
