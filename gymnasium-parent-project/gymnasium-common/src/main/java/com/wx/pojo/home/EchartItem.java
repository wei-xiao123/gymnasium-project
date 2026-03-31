package com.wx.pojo.home;

import lombok.Data;
import java.io.Serializable;

@Data
public class EchartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name; // 统计项的名称
    private Integer value; // 每一个统计项的总数
}
