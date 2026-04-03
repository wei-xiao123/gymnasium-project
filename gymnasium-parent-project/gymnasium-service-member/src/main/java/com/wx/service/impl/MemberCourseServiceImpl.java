package com.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.MemberCourseMapper;
import com.wx.mapper.MemberMapper;
import com.wx.pojo.course.Course;
import com.wx.pojo.course.PageParam;
import com.wx.pojo.member.RechargeParam;
import com.wx.pojo.member_course.MemberCourse;
import com.wx.service.course.CourseService;
import com.wx.service.member_course.MemberCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DubboService(interfaceClass = MemberCourseService.class)
public class MemberCourseServiceImpl extends ServiceImpl<MemberCourseMapper, MemberCourse> implements MemberCourseService {

    @DubboReference
    CourseService courseService;

    @Autowired
    MemberMapper memberMapper;
    @Override
    @Transactional
    public void joinCourse(MemberCourse memberCourse) {
        //根据课程id查询课程信息
        Course course = courseService.getById(memberCourse.getCourseId());
        BeanUtils.copyProperties(course,memberCourse);
        //插入报名表
        int insert = this.baseMapper.insert(memberCourse);
        if(insert > 0){ //报名成功，执行充值金额的扣钱
            RechargeParam param = new RechargeParam();
            param.setMemberId(memberCourse.getMemberId());
            param.setMoney(course.getCoursePrice());
            memberMapper.subMoney(param);
        }

    }

    @Override
    public boolean existsByCourseAndMember(Long courseId, Long memberId) {
        if (courseId == null || memberId == null) {
            return false;
        }
        QueryWrapper<MemberCourse> query = new QueryWrapper<>();
        query.eq("course_id", courseId).eq("member_id", memberId);
        return this.baseMapper.selectCount(query) > 0;
    }

    @Override
    public IPage<MemberCourse> queryPageByMember(PageParam param) {
        long currentPage = param.getCurrentPage() == null ? 1L : param.getCurrentPage();
        long pageSize = param.getPageSize() == null ? 10L : param.getPageSize();
        IPage<MemberCourse> page = new Page<>(currentPage, pageSize);
        QueryWrapper<MemberCourse> query = new QueryWrapper<>();
        query.eq("member_id", param.getUserId());
        return this.baseMapper.selectPage(page, query);
    }

    @Override
    public IPage<MemberCourse> queryPageAll(PageParam param) {
        long currentPage = param.getCurrentPage() == null ? 1L : param.getCurrentPage();
        long pageSize = param.getPageSize() == null ? 10L : param.getPageSize();
        IPage<MemberCourse> page = new Page<>(currentPage, pageSize);
        QueryWrapper<MemberCourse> query = new QueryWrapper<>();
        query.orderByDesc("member_course_id");
        return this.baseMapper.selectPage(page, query);
    }
}
