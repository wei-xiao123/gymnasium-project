package com.wx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.pojo.lost.Lost;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LostMapper extends BaseMapper<Lost> {
}