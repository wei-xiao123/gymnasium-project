package com.wx.pojo.course;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("course")
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long courseId;
    private String courseName;
    private String image;
    private String teacherName;
    private Integer courseHour;
    private String courseDetails;
    private BigDecimal coursePrice;

    private Long teacherId;
}
