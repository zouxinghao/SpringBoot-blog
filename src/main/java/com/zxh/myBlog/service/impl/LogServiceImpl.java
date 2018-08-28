package com.zxh.myBlog.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.PageHelper;
import com.zxh.myBlog.constant.WebConst;
import com.zxh.myBlog.dao.LogVoMapper;
import com.zxh.myBlog.model.Vo.LogVo;
import com.zxh.myBlog.model.Vo.LogVoExample;
import com.zxh.myBlog.service.ILogService;
import com.zxh.myBlog.utils.DateKit;

public class LogServiceImpl implements ILogService{

	private static final Logger LOGGER = LoggerFactory.getLogger(LogServiceImpl.class);
	
	@Resource
	private LogVoMapper logDao;
	
	@Override
	public void insertLog(LogVo logVo) {
		// TODO Auto-generated method stub
		logDao.insertSelective(logVo);
	}

	@Override
	public void insertLog(String action, String data, String ip, Integer authorId) {
		// TODO Auto-generated method stub
		LogVo logs = new LogVo();
		logs.setAction(action);
		logs.setIp(ip);
		logs.setAuthorId(authorId);
		logs.setIp(ip);
		logs.setCreated(DateKit.getCurrentUnixTime());
		logDao.insertSelective(logs);
	}

	@Override
	public List<LogVo> getLogs(int page, int limit) {
		// TODO Auto-generated method stub
		LOGGER.debug("Enter getLogs method:page={},linit={}",page,limit);
        if (page <= 0) {
            page = 1;
        }
        if (limit < 1 || limit > WebConst.MAX_POSTS) {
            limit = 10;
        }
        LogVoExample logVoExample = new LogVoExample();
        logVoExample.setOrderByClause("id desc");
        PageHelper.startPage((page - 1) * limit, limit);
        List<LogVo> logVos = logDao.selectByExample(logVoExample);
        LOGGER.debug("Exit getLogs method");
        return logVos;
	}

}
