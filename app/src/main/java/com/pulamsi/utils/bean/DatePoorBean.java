package com.pulamsi.utils.bean;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-07-08
 * Time: 10:54
 * FIXME
 * 时间差
 */
public class DatePoorBean {
    private long day;
    private long hour;
    private long min;
    private long sec;


    public DatePoorBean(long day, long hour, long min, long sec) {
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }
    //倒计时需要全部转为正数Math.abs()
    public long getDay() {
        return Math.abs(day);
    }

    public void setDay(long day) {
        this.day = day;
    }

    public long getHour() {
        return Math.abs(hour);
    }

    public void setHour(long hour) {
        this.hour = hour;
    }

    public long getMin() {
        return Math.abs(min);
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getSec() {
        return Math.abs(sec);
    }

    public void setSec(long sec) {
        this.sec = sec;
    }


    @Override
    public String toString() {
        return "DatePoorBean{" +
                "day=" + day +
                ", hour=" + hour +
                ", min=" + min +
                ", sec=" + sec +
                '}';
    }
}
