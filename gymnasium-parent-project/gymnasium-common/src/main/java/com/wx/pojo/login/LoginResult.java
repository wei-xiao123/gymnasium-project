package com.wx.pojo.login;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String username;
    private String token;
    private String userType;
}
