package com.wx.pojo.member;

import lombok.Data;

import java.io.Serializable;

@Data
public class RechargeParamList implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long currentPage;
    private Long pageSize;
    private Long memberId;
    private String userType;
}