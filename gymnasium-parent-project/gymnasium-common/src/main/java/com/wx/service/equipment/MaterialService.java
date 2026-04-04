package com.wx.service.equipment;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.pojo.equipment.ListParam;
import com.wx.pojo.equipment.Material;


public interface MaterialService extends IService<Material> {

	IPage<Material> queryPage(ListParam param);
}
