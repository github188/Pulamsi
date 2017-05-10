package com.pulamsi.myinfo.order.entity;

import com.pulamsi.base.entity.ResultContext;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 实体类 - 订单请求返回对象
 */

public class OrderResult extends ResultContext {

    private Order return_context;//返回的数据

    public Order getReturn_context() {
        return return_context;
    }

    public OrderResult setReturn_context(Order return_context) {
        this.return_context = return_context;
        return this;
    }
}