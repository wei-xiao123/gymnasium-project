package com.xq.web.course.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xq.utils.ResultUtils;
import com.xq.utils.ResultVo;
import com.xq.web.course.entity.Course;
import com.xq.web.course.entity.CourseList;
import com.xq.web.course.entity.PageParam;
import com.xq.web.course.service.CourseService;
import com.xq.web.member.entity.Member;
import com.xq.web.member.service.MemberService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    CourseService courseService;

    //新增
    @PostMapping
    public ResultVo add(@RequestBody Course course){
        if(courseService.save(course)){
            return ResultUtils.success("新增成功");
        }
        return ResultUtils.error("新增失败");
    }

    //编辑
    @PutMapping
    public ResultVo edit(@RequestBody Course course){
        if(courseService.updateById(course)){
            return ResultUtils.success("编辑成功");
        }
        return ResultUtils.error("编辑失败");
    }

    //删除
    @DeleteMapping("/{courseId}")
    public ResultVo delete(@PathVariable("courseId") Long courseId){
        if(courseService.removeById(courseId)){
            return  ResultUtils.success("删除成功");
        }
        return ResultUtils.error("删除失败");
    }

    //课程列表查询
    @GetMapping("/list")
    public ResultVo list(CourseList courseList){
        //构造分页对象
        IPage<Course> page = new Page<>(courseList.getCurrentPage(),courseList.getPageSize());
        //构造查询条件
        QueryWrapper<Course> query = new QueryWrapper();
        if(StringUtils.isNotEmpty(courseList.getCourseName())){
            query.lambda().like(Course::getCourseName,courseList.getCourseName());
        }
        if(StringUtils.isNotEmpty(courseList.getTeacherName())){
            query.lambda().like(Course::getTeacherName,courseList.getTeacherName());
        }
        IPage<Course> list = courseService.page(page,query);
        return ResultUtils.success("查询成功",list);
    }
}
