package com.zxh.myBlog.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zxh.myBlog.dao.OptionVoMapper;
import com.zxh.myBlog.model.Vo.OptionVo;
import com.zxh.myBlog.model.Vo.OptionVoExample;
import com.zxh.myBlog.service.IOptionService;

@Service
public class OptionServiceImpl implements IOptionService{
	private static final Logger LOGGER = LoggerFactory.getLogger(OptionServiceImpl.class);
	
	@Resource
	private OptionVoMapper optionDao;

	
	public void insertOption(OptionVo optionVo) {
		// TODO Auto-generated method stub
		LOGGER.debug("Enter insertOption method:optionVo={}", optionVo);
		optionDao.insertSelective(optionVo);
		LOGGER.debug("Exit insertOption method.");
	}

	@Transactional
	public void insertOption(String name, String value) {
		// TODO Auto-generated method stub
		LOGGER.debug("Enter insertOption method:name={},value={}", name, value);
		OptionVo optionVo = new OptionVo();
		optionVo.setName(name);
		optionVo.setValue(value);
		if(optionDao.selectByPrimaryKey(name) == null) {
			optionDao.insertSelective(optionVo);
		} else {
			optionDao.updateByPrimaryKeySelective(optionVo);
		}
		LOGGER.debug("Exit insertOption method.");
	}

	public List<OptionVo> getOptions() {
		// TODO Auto-generated method stub
		return optionDao.selectByExample(new OptionVoExample());
	}

	public OptionVo getOptionByName(String name) {
		// TODO Auto-generated method stub
		return optionDao.selectByPrimaryKey(name);
	}

	@Transactional
	public void saveOption(Map<String, String> option) {
		// TODO Auto-generated method stub
		if(option != null && !option.isEmpty()) {
			option.forEach(this::insertOption);
		}
	}

}
