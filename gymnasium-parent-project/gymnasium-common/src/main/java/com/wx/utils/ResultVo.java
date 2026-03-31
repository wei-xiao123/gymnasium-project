package com.wx.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 封装返回值数据
 * @param <T>
 */
@Data
@AllArgsConstructor
public class ResultVo <T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code;
    private T data;
}
