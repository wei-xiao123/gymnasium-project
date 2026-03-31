package com.wx.pojo.equipment;

import lombok.Data;

import java.io.Serializable;

@Data
public class ListParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long currentPage;
    private Long pageSize;
    private String name;
}
