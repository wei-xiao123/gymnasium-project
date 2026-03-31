package com.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.mapper.MaterialMapper;
import com.wx.pojo.equipment.Material;
import com.wx.service.equipment.MaterialService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = MaterialService.class)
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService {
}
