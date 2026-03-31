package com.wx.pojo.goods_order;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderParam implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private List<OrderItem> orderList;
}
