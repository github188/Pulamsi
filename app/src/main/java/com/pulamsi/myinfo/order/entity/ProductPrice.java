package com.pulamsi.myinfo.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lanqiang on 16/1/4.
 * 货物积分
 */
public class ProductPrice  implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String productId;
    private int integralPrice;// 所需积分
    private BigDecimal productPrice;// 所需金额
    private boolean isSelect;//是否是选中

    public int getId() {
        return id;
    }

    public ProductPrice setId(int id) {
        this.id = id;
        return this;
    }

    public String getProductId() {
        return productId;
    }

    public ProductPrice setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public int getIntegralPrice() {
        return integralPrice;
    }

    public ProductPrice setIntegralPrice(int integralPrice) {
        this.integralPrice = integralPrice;
        return this;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public ProductPrice setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public ProductPrice setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
        return this;
    }
}
