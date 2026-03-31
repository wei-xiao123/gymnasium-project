package com.wx.pojo.member_card;

import lombok.Data;

import java.io.Serializable;

@Data
public class ListCard implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long currentPage;
    private Long pageSize;
    private String title;
}
