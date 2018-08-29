package com.zxh.myBlog.service;

import java.util.List;

import com.zxh.myBlog.model.Vo.LogVo;


public interface ILogService {
	/**
	 * save the operation data
	 * 
	 * @param logVo
	 */
	void insertLog(LogVo logVo);
	
	/**
	 * 
	 * @param action
	 * @param data
	 * @param ip
	 * @param authorId
	 */
	void insertLog(String action, String data, String ip, Integer authorId);
	
	/**
	 * get sub-page log
	 * @param page current page
	 * @param limit number of each page
	 * @return
	 */
	List<LogVo> getLogs(int page, int limit);
}
