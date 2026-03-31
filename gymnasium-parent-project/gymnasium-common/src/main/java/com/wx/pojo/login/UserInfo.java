package com.wx.pojo.login;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String name;
    private Object[] permissions;
}
