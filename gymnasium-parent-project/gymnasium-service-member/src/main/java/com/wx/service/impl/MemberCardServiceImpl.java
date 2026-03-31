package com.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.MemberCardMapper;
import com.wx.pojo.member_card.MemberCard;
import com.wx.service.member_card.MemberCardService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = MemberCardService.class)
public class MemberCardServiceImpl extends ServiceImpl<MemberCardMapper, MemberCard> implements MemberCardService {
}
