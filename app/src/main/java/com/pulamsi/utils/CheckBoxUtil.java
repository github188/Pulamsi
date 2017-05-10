package com.pulamsi.utils;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.pulamsi.R;
import com.pulamsi.constant.Constants;

import java.text.DecimalFormat;

/**
 * check box 工具类，统一封装点击 check box 的逻辑
 *
 * @author WilliamChik on 15/9/3.
 */
public class CheckBoxUtil {

  public static final String CHECK_TAG = "check";

  public static final String UNCHECK_TAG = "uncheck";

  public static void checkCheckBox(View clickView) {
    if (clickView instanceof ImageView) {
      clickView.setBackgroundResource(R.drawable.ic_selected_press);
      clickView.setTag(CHECK_TAG);
    } else if (clickView instanceof TextView) {
      ((TextView) clickView).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_selected_press, 0, 0, 0);
      clickView.setTag(CHECK_TAG);
    }
  }

  public static void uncheckCheckBox(View clickView) {
    if (clickView instanceof ImageView) {
      clickView.setBackgroundResource(R.drawable.ic_selected_nor);
      clickView.setTag(UNCHECK_TAG);
    } else if (clickView instanceof TextView) {
      ((TextView) clickView).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_selected_nor, 0, 0, 0);
      clickView.setTag(UNCHECK_TAG);
    }
  }

  public static void setNumToTextView(TextView tv, float unitNum, float updateItemCount, boolean isAdd, boolean isInt,
                                      String prefixStr, String suffixStr) {
    String updateItemCountStr = String.valueOf(updateItemCount);
    setNumToTextView(tv, unitNum, updateItemCountStr, isAdd, isInt, prefixStr, suffixStr);
  }

  public static void setNumToTextView(TextView tv, String unitNum, float updateItemCount, boolean isAdd, boolean isInt,
                                      String prefixStr, String suffixStr) {
    String updateItemCountStr = String.valueOf(updateItemCount);
    setNumToTextView(tv, unitNum, updateItemCountStr, isAdd, isInt, prefixStr, suffixStr);
  }

  public static void setNumToTextView(TextView tv, float unitNum, String updateItemCount, boolean isAdd, boolean isInt,
                                      String prefixStr, String suffixStr) {
    String unitNumStr = String.valueOf(unitNum);
    setNumToTextView(tv, unitNumStr, updateItemCount, isAdd, isInt, prefixStr, suffixStr);
  }

  /**
   * 在 text view 原来显示的数字内容中 增加 | 减少 相应的数字
   *
   * @param tv              需要 增加 | 减少 数字的 text view
   * @param unitNum         需要 增加 | 减少 的单位数字
   * @param updateItemCount 需要 增加 | 减少 的单位数字的数量
   * @param isAdd           true 增加，false 减少
   * @param isInt           true 结果以 int 输出， false  结果以 float 输出
   * @param prefixStr       需要添加的字符串前缀
   * @param suffixStr       需要添加的字符串后缀
   */
  public static void setNumToTextView(TextView tv, String unitNum, String updateItemCount, boolean isAdd, boolean isInt,
                                      String prefixStr, String suffixStr) {
    if (tv == null) {
      return;
    }

    try {
      // 剔除 text view 文本中的字符串前后缀，取出数字文本
      String tvCurrentStr = tv.getText().toString().trim();
      if (!TextUtils.isEmpty(prefixStr.trim())) {
        int prefixLength = prefixStr.trim().length();
        tvCurrentStr = tvCurrentStr.substring(prefixLength);
      }
      if (!TextUtils.isEmpty(suffixStr.trim())) {
        int suffixIndex = tvCurrentStr.indexOf(suffixStr.trim());
        tvCurrentStr = tvCurrentStr.substring(0, suffixIndex);
      }

      float tvCurrentNum = Float.valueOf(tvCurrentStr);
      float unitNumNeedToSet = Float.valueOf(unitNum);
      float itemCountNeedToSet = Integer.valueOf(updateItemCount);
      Float resultNum;
      String resultStr;
      DecimalFormat df = new DecimalFormat(Constants.DEFAULT_PRICE_NUM);

      if (isAdd) {
        resultNum = tvCurrentNum + unitNumNeedToSet * itemCountNeedToSet;
      } else {
        resultNum = tvCurrentNum - unitNumNeedToSet * itemCountNeedToSet;
        if (resultNum <= 0) {
          resultNum = Float.valueOf(Constants.DEFAULT_PRICE_NUM);
        }
      }

      if (isInt) {
        resultStr = String.valueOf(resultNum.intValue());
      } else {
        resultStr = df.format(resultNum);
      }

      if (!TextUtils.isEmpty(prefixStr)) {
        resultStr = prefixStr.trim() + resultStr;
      }
      if (!TextUtils.isEmpty(suffixStr)) {
        resultStr = resultStr + suffixStr.trim();
      }

      tv.setText(resultStr);
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
  }

}
