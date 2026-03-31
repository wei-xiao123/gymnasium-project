package com.wx.pojo.member_course;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("member_course")
public class MemberCourse implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long memberCourseId;
    private Long courseId;
    private Long memberId;
    private Long teacherId;
    private String courseName;
    private String image;
    private String teacherName;
    private Integer courseHour;
    private String courseDetails;
    private BigDecimal coursePrice;
}
