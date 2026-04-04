package com.wx.service.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.pojo.goods.Goods;
import com.wx.pojo.goods.GoodsParam;

public interface GoodsService extends IService<Goods> {

	IPage<Goods> queryPage(GoodsParam param);
}
