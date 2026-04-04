package com.wx.service.suggest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.pojo.suggest.Suggest;
import com.wx.pojo.suggest.SuggestParam;

import java.util.List;

public interface SuggestService extends IService<Suggest> {

	IPage<Suggest> queryPage(SuggestParam param);

	List<Suggest> queryTopList(int limit);
}