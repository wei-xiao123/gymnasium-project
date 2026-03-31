package com.wx.pojo.login;

import lombok.Data;

import java.io.Serializable;

@Data
public class InfoParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private String userType;
}
