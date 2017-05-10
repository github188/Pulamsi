package com.pulamsi.utils;

import com.lidroid.xutils.util.LogUtils;
import com.pulamsi.utils.bean.CoutDownBean;
import com.pulamsi.utils.bean.DatePoorBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-07-08
 * Time: 10:53
 * FIXME
 */
public class DateUtils {
    /**
     * 获取两个时间的时间查 如1天2小时30分钟
     */
    public static DatePoorBean getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;
//       day + "天" + hour + "小时" + min + "分钟" + sec + "秒"
        return new DatePoorBean(day, hour, min, sec);
    }

    public static CoutDownBean ComputationTime(long panicBuyBeginTime,long panicBuyEndTime) {
        CoutDownBean coutDownBean = new CoutDownBean();//初始化时间

        Long nowTime = System.currentTimeMillis();  //获取当前时间戳
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置时间格式

        Date nowDate = new Date(Long.parseLong(String.valueOf(nowTime)));//当前时间转为Date对象
        String sNowTime = sdf.format(nowDate);   // 当前时间戳转换成时间String

        Date BuyBeginTime = new Date(Long.parseLong(String.valueOf(panicBuyBeginTime)));//获取开始时间戳转为Date对象
        String sBeginTime = sdf.format(BuyBeginTime);   // 时间戳转换成时间

        Date BuyEndTime = new Date(Long.parseLong(String.valueOf(panicBuyEndTime)));//获取结束时间戳转为Date对象
        String sEndTime = sdf.format(BuyEndTime);   // 时间戳转换成时间

        LogUtils.e("当前：" + sNowTime);
        LogUtils.e("开始：" + sBeginTime);
        LogUtils.e("结束：" + sEndTime);
        //抢购总持续时间
        DatePoorBean datePoorTotalBean = DateUtils.getDatePoor(BuyEndTime, BuyBeginTime);//抢购时间包含天，小时，分钟，秒
        coutDownBean.setDatePoorTotalBean(datePoorTotalBean);//抢购总持续时间


        if (nowDate.after(BuyEndTime)){//现在时间大于结束时间，已结束
            coutDownBean.setIsEnd(true);
            LogUtils.e("已结束");
            return coutDownBean;
        }else {
            coutDownBean.setIsEnd(false);
            LogUtils.e("未结束");
        }

        if (BuyBeginTime.after(nowDate)){//开始时间大于当前时间，还未开始
            coutDownBean.setIsBegin(false);
            DatePoorBean datePoor = DateUtils.getDatePoor(nowDate, BuyBeginTime);//抢购时间包含天，小时，分钟，秒
            coutDownBean.setDatePoorBean(datePoor);
            LogUtils.e("未开始");
            return coutDownBean;
        }else {
            coutDownBean.setIsBegin(true);
            DatePoorBean datePoor = DateUtils.getDatePoor(BuyEndTime, nowDate);//抢购时间包含天，小时，分钟，秒
            coutDownBean.setDatePoorBean(datePoor);
            LogUtils.e("已开始");
            return coutDownBean;
        }


//      endDate.after(nowDate);//当endDate大于nowDate时，返回TRUE，当小于等于时，返回false;
    }

}
