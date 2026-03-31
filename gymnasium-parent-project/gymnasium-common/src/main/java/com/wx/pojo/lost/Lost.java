package com.wx.pojo.lost;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("lost")
public class Lost implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long lostId;
    private String lostName;
    private String foundTime;
    private String foundAddres;
    private String foundPerson;
    private String foundPhone;
    private String status;
    private String lostPerson;
}