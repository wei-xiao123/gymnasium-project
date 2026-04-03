package com.wx.service.course;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.pojo.course.Course;
import com.wx.pojo.course.CourseList;
import com.wx.pojo.course.PageParam;


public interface CourseService extends IService<Course> {

	IPage<Course> queryPage(CourseList param);

	IPage<Course> queryPageByTeacher(PageParam param);
}
