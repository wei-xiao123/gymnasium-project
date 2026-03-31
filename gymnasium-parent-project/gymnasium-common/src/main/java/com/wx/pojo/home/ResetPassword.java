package com.wx.pojo.home;

import lombok.Data;
import java.io.Serializable;

@Data
public class ResetPassword implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String userType;
    private String password;
    private String oldPassword;
}
