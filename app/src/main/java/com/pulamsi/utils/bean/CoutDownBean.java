package com.pulamsi.utils.bean;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-02
 * Time: 10:00
 * FIXME
 */
public class CoutDownBean {

    private Boolean isBegin;
    private Boolean isEnd;
    private DatePoorBean datePoorBean;
    /**
     * 抢购总时间
     */
    private DatePoorBean datePoorTotalBean;

    public Boolean getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(Boolean isEnd) {
        this.isEnd = isEnd;
    }



    public Boolean getIsBegin() {
        return isBegin;
    }

    public void setIsBegin(Boolean isBegin) {
        this.isBegin = isBegin;
    }

    public DatePoorBean getDatePoorBean() {
        return datePoorBean;
    }

    public void setDatePoorBean(DatePoorBean datePoorBean) {
        this.datePoorBean = datePoorBean;
    }

    public DatePoorBean getDatePoorTotalBean() {
        return datePoorTotalBean;
    }

    public void setDatePoorTotalBean(DatePoorBean datePoorTotalBean) {
        this.datePoorTotalBean = datePoorTotalBean;
    }
}
