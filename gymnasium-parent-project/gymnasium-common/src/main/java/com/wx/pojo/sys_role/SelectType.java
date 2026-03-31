package com.wx.pojo.sys_role;

import lombok.Data;

import java.io.Serializable;

@Data
public class SelectType implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long value;
    public String label;
}
