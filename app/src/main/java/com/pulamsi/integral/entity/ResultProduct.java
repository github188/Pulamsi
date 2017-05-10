package com.pulamsi.integral.entity;

import com.pulamsi.base.entity.ResultContext;
import com.pulamsi.myinfo.order.entity.Product;

import java.util.List;

/**
 * Created by lanqiang on 16/1/7.
 */
public class ResultProduct extends ResultContext {
       private List<Product> return_context;//返回的数据

    public List<Product> getReturn_context() {
        return return_context;
    }

    public ResultProduct setReturn_context(List<Product> return_context) {
        this.return_context = return_context;
        return this;
    }
}
