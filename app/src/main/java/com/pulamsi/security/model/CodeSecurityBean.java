package com.pulamsi.security.model;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-04-23
 * Time: 10:59
 * FIXME
 */
public class CodeSecurityBean {
    private String id;
    private Integer codeIntegral; // 积分
    private String code; // 积分码--防伪码
    private String codePwd; // 积分暗号--涂层码
    private String mobile; // 手机号码
    private Integer codestatus; // 积分状态 0、未查过 ;1、已经被查过 ; 2、已经用暗号查过 ; 3、已积分
    private String firstQueryDate; // 首次查询时间--防伪码查询
    private String exchangeDate; // 积分时间
    private Integer queryCount;// 查询次数
    private String createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCodeIntegral() {
        return codeIntegral;
    }

    public void setCodeIntegral(Integer codeIntegral) {
        this.codeIntegral = codeIntegral;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodePwd() {
        return codePwd;
    }

    public void setCodePwd(String codePwd) {
        this.codePwd = codePwd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getCodestatus() {
        return codestatus;
    }

    public void setCodestatus(Integer codestatus) {
        this.codestatus = codestatus;
    }

    public String getFirstQueryDate() {
        return firstQueryDate;
    }

    public void setFirstQueryDate(String firstQueryDate) {
        this.firstQueryDate = firstQueryDate;
    }

    public String getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(String exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public Integer getQueryCount() {
        return queryCount;
    }

    public void setQueryCount(Integer queryCount) {
        this.queryCount = queryCount;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "CodeSecurityBean{" +
                "id='" + id + '\'' +
                ", codeIntegral=" + codeIntegral +
                ", code='" + code + '\'' +
                ", codePwd='" + codePwd + '\'' +
                ", mobile='" + mobile + '\'' +
                ", codestatus=" + codestatus +
                ", firstQueryDate='" + firstQueryDate + '\'' +
                ", exchangeDate='" + exchangeDate + '\'' +
                ", queryCount=" + queryCount +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
