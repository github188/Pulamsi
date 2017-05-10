package com.pulamsi.base.entity;


import java.io.Serializable;

/**
 * Created by lanqiang on 16/1/7.
 */
public class ResultContext implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    private String return_code;// SUCCESS/FAIL/错误码
    private String return_msg;// 返回信息，为错误原因, 比如：参数格式校验错误


    public String getReturn_code() {
        return return_code;
    }

    public ResultContext setReturn_code(String return_code) {
        this.return_code = return_code;
        return this;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public ResultContext setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
        return this;
    }
}
