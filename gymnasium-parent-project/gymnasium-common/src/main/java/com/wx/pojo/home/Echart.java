package com.wx.pojo.home;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Echart implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> names = new ArrayList<>();
    private List<Integer> values = new ArrayList<>();
}
