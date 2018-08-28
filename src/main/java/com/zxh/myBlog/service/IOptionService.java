package com.zxh.myBlog.service;

import java.util.List;
import java.util.Map;

import com.zxh.myBlog.model.Vo.OptionVo;

public interface IOptionService {

	void insertOption(OptionVo optionVo);
	
	void insertOption(String name, String value);
	
	List<OptionVo> getOptions();
	
	void saveOption(Map<String, String> option);
	
	OptionVo getOptionByName(String name);
}
