package com.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.CourseMapper;
import com.wx.pojo.course.Course;
import com.wx.service.course.CourseService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = CourseService.class)
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

}
