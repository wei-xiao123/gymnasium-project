package com.wx.pojo.member;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RechargeParam implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long memberId;
    private BigDecimal money;
}
