package com.wx.web.course.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wx.pojo.course.Course;
import com.wx.pojo.course.CourseList;
import com.wx.pojo.course.PageParam;
import com.wx.pojo.member.Member;
import com.wx.pojo.member_course.MemberCourse;
import com.wx.service.course.CourseService;
import com.wx.service.member.MemberService;
import com.wx.service.member_course.MemberCourseService;
import com.wx.utils.ResultUtils;
import com.wx.utils.ResultVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @DubboReference
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
        IPage<Course> list = courseService.queryPage(courseList);
        return ResultUtils.success("查询成功", list);
    }

    @DubboReference
    MemberCourseService memberCourseService;

    @DubboReference
    MemberService memberService;

    //报名课程
    @PostMapping("/joinCourse")
    public ResultVo joinCourse(@RequestBody MemberCourse memberCourse){
        //查询是否已经报名该课程
        boolean joined = memberCourseService.existsByCourseAndMember(memberCourse.getCourseId(), memberCourse.getMemberId());
        if(joined){
            return ResultUtils.error("您已经报名过该课程");
        }
        //判断余额是否充足
        Course course = courseService.getById(memberCourse.getCourseId());
        Member member = memberService.getById(memberCourse.getMemberId());
        int flag = member.getMoney().compareTo(course.getCoursePrice());
        if(flag == -1){
            return ResultUtils.error("您的余额不足，请先充值");
        }
        memberCourseService.joinCourse(memberCourse);
        return ResultUtils.success("报名成功");
    }

    //查询我的课程列表
    @GetMapping("/getMyCourseList")
    public ResultVo getMyCourseList(PageParam param){
        if("1".equals(param.getUserType())){
            IPage<MemberCourse> list = memberCourseService.queryPageByMember(param);
            return ResultUtils.success("查询成功",list);
        } else if ("2".equals(param.getUserType())) {
            IPage<MemberCourse> list = memberCourseService.queryPageAll(param);
            return ResultUtils.success("查询成功",list);
        }
        IPage<Course> list = courseService.queryPageByTeacher(param);
        return ResultUtils.success("查询成功",list);
    }
}
