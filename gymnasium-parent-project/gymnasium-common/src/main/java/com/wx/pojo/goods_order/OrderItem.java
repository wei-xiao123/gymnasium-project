package com.wx.pojo.goods_order;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long goodsId;
    private Integer num;
}
