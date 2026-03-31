package com.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.SuggestMapper;
import com.wx.pojo.suggest.Suggest;
import com.wx.service.suggest.SuggestService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = SuggestService.class)
public class SuggestServiceImpl extends ServiceImpl<SuggestMapper, Suggest> implements SuggestService {
}