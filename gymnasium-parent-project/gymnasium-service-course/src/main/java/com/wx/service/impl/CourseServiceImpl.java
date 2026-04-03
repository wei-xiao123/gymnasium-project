package com.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.CourseMapper;
import com.wx.pojo.course.Course;
import com.wx.pojo.course.CourseList;
import com.wx.pojo.course.PageParam;
import com.wx.service.course.CourseService;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = CourseService.class)
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

	@Override
	public IPage<Course> queryPage(CourseList param) {
		long currentPage = param.getCurrentPage() == null ? 1L : param.getCurrentPage();
		long pageSize = param.getPageSize() == null ? 10L : param.getPageSize();
		IPage<Course> page = new Page<>(currentPage, pageSize);
		QueryWrapper<Course> query = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(param.getCourseName())) {
			query.like("course_name", param.getCourseName());
		}
		if (StringUtils.isNotEmpty(param.getTeacherName())) {
			query.like("teacher_name", param.getTeacherName());
		}
		return this.baseMapper.selectPage(page, query);
	}

	@Override
	public IPage<Course> queryPageByTeacher(PageParam param) {
		long currentPage = param.getCurrentPage() == null ? 1L : param.getCurrentPage();
		long pageSize = param.getPageSize() == null ? 10L : param.getPageSize();
		IPage<Course> page = new Page<>(currentPage, pageSize);
		QueryWrapper<Course> query = new QueryWrapper<>();
		query.eq("teacher_id", param.getUserId());
		return this.baseMapper.selectPage(page, query);
	}

}
