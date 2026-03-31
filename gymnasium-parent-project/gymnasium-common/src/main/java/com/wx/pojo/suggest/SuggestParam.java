package com.wx.pojo.suggest;

import lombok.Data;

import java.io.Serializable;

@Data
public class SuggestParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long currentPage;
    private Long pageSize;
    private String title;
}