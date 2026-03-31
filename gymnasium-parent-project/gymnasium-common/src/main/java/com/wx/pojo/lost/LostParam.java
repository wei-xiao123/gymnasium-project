package com.wx.pojo.lost;

import lombok.Data;

import java.io.Serializable;

@Data
public class LostParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long currentPage;
    private Long pageSize;
    private String lostName;
}