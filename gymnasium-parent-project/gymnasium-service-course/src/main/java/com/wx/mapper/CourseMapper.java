package com.wx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.pojo.course.Course;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
}
