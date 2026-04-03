package com.wx.service.member_course;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.pojo.course.PageParam;
import com.wx.pojo.member_course.MemberCourse;

public interface MemberCourseService extends IService<MemberCourse> {

    void joinCourse(MemberCourse memberCourse);

    boolean existsByCourseAndMember(Long courseId, Long memberId);

    IPage<MemberCourse> queryPageByMember(PageParam param);

    IPage<MemberCourse> queryPageAll(PageParam param);
}
