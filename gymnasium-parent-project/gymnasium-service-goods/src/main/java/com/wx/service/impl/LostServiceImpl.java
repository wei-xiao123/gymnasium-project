package com.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.LostMapper;
import com.wx.pojo.lost.Lost;
import com.wx.service.lost.LostService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = LostService.class)
public class LostServiceImpl extends ServiceImpl<LostMapper, Lost> implements LostService{
}