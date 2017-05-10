package com.pulamsi.base.baseUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 折扣工具
 */
public class DiscountUtil {

    private static DiscountUtil instance;

    private NumberFormat formater;

    private DiscountUtil() {
    }

    public static DiscountUtil getInstance() {
        if (instance == null) {
            instance = new DiscountUtil();
            NumberFormat formater = DecimalFormat.getInstance();
            formater.setMaximumFractionDigits(1);
            formater.setMinimumFractionDigits(1);
            instance.setFormater(formater);
        }
        return instance;
    }

    /**
     * 获取展示折扣（保留小数点后一位返回字符串）
     *
     * @param discount
     * @return
     */
    public String getDisplayDiscount(double discount) {
        return formater.format(discount);
    }

    /**
     * 获取展示折扣（保留小数点后一位返回）
     *
     * @param discount
     * @return
     */
    public String getDisplayDiscount(String discount) {
        String displayDiscount = discount;
        double discountDouble = Double.parseDouble(discount);
        if (discountDouble > 0) {
            displayDiscount = getDisplayDiscount(discountDouble);
        }
        return displayDiscount;
    }

    public void setFormater(NumberFormat formater) {
        this.formater = formater;
    }
}
