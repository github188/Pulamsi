package com.pulamsi.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检查输入是否合法，手机、密码等
 *
 * @author WilliamChik on 15/10/19.
 */
public class CheckInputUtil {

  /**
   * 检查手机号的输入是否合法
   *
   * @param phoneNum 手机号
   * @return true 合法，false 非法
   */
  public static boolean checkPhoneNum(String phoneNum) {
    //去掉左右空格
    String pn = phoneNum.trim();
    if (TextUtils.isEmpty(pn)) {
      return false;
    }

    // 匹配数字，长度 11 位
    String patternStr = "^\\d{11}$";
    return Pattern.matches(patternStr, pn);
  }


  /**
   * 手机号验证
   *
   * @param  mobile
   * @return 验证通过返回true
   */
  public static boolean isMobile(String mobile) {
    //去掉左右空格
    String pn = mobile.trim();
    if (TextUtils.isEmpty(pn)) {
      return false;
    }

    Pattern p = null;
    Matcher m = null;
    boolean b = false;
    p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
    m = p.matcher(mobile);
    b = m.matches();
    return b;
  }

  /**
   * 检查密码是否合法
   *
   * @param password 密码
   * @return true 合法，false 非法
   */
  public static boolean checkPassword(String password) {
    String pwd = password.trim();
    if (TextUtils.isEmpty(pwd)) {
      return false;
    }

    // 匹配任何字类字符，包括下划线。与“[A-Za-z0-9_]”等效，长度 6 ~ 20 位,包含@#
    String patternStr = "^[\\w@#!$%^&*.,]{6,20}$";
    return Pattern.matches(patternStr, pwd);
  }
}
