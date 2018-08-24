package com.zxh.myBlog.service;

import com.zxh.myBlog.model.Vo.UserVo;

public interface IUserService {

	/**
	 * Save(Insert) date the (new) user
	 * @Param UserVo
	 * @return Primary Key
	 */
	
	Integer insertUser(UserVo userVo);
	
	/**
	 * Query user by id
	 * @param uid
	 * @return 
	 */
	UserVo queryById(Integer uid);
	
	/**
	 * login
	 * @param username
	 * @param password
	 * @return
	 */
	UserVo login(Integer username, Integer password);
	
	/**
	 * update user info by primary key
	 * @param userVo
	 * @return
	 */
	void updateById(UserVo userVo);
	
}
