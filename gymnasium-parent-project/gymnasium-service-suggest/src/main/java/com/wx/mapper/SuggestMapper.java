package com.wx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.pojo.suggest.Suggest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SuggestMapper extends BaseMapper<Suggest> {
}