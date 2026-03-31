package com.wx.service.member_course;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.pojo.member_course.MemberCourse;

public interface MemberCourseService extends IService<MemberCourse> {

    void joinCourse(MemberCourse memberCourse);
}
