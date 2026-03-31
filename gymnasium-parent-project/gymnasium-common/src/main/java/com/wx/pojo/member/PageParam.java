package com.wx.pojo.member;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageParam implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long currentPage;
    private Long pageSize;
    private String name;
    private String phone;
    private String username;
    private String userType;
    private String memberId;
}
