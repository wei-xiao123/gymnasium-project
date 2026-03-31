package com.wx.pojo.course;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long currentPage;
    private Long pageSize;
    private String userType;
    private Long userId;
}
