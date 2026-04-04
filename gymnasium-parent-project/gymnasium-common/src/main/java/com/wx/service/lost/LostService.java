package com.wx.service.lost;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.pojo.lost.Lost;
import com.wx.pojo.lost.LostParam;

public interface LostService extends IService<Lost> {

	IPage<Lost> queryPage(LostParam param);
}