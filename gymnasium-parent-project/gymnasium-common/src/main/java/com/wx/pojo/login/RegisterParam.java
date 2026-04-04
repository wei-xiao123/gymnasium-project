package com.wx.pojo.login;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterParam implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String confirmPassword;
    private String name;
    private String phone;
    private String code;
}