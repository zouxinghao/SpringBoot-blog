package com.zxh.myBlog.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.zxh.myBlog.dao.UserVoMapper;
import com.zxh.myBlog.exception.TipException;
import com.zxh.myBlog.model.Vo.UserVo;
import com.zxh.myBlog.model.Vo.UserVoExample;
import com.zxh.myBlog.service.IUserService;
import com.zxh.myBlog.utils.TaleUtils;

import java.util.List;

import javax.annotation.Resource;

public class UserServiceImpl implements IUserService{

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Resource
	private UserVoMapper userDao;
	
	//@Override
    @Transactional
	public Integer insertUser(UserVo userVo) {
		// TODO Auto-generated method stub
		//Integer uid = null;
		if(StringUtils.isNoneBlank(userVo.getUsername()) && StringUtils.isNoneBlank(userVo.getPassword())) {
			String encodePassword = TaleUtils.MD5encode(userVo.getUsername()+userVo.getPassword());
			userVo.setPassword(encodePassword);
			userDao.insertSelective(userVo);
		}
		return userVo.getUid();
	}

	public UserVo queryById(Integer uid) {
		// TODO Auto-generated method stub
		UserVo userVo = null;
		if(uid != null)
			userVo = userDao.selectByPrimaryKey(uid);
		return userVo;
	}

	public UserVo login(String username, String password) {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			throw new TipException("Please Enter the Username or Passowrd!");
		}
		UserVoExample example = new UserVoExample();
		UserVoExample.Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		long count = userDao.countByExample(example);
		if(count<1) {
			throw new TipException("User does not exist!");
		}
		String pwd = TaleUtils.MD5encode(username + password);
		criteria.andPasswordEqualTo(pwd);
		List<UserVo> res = userDao.selectByExample(example);
		if(res.size()!=1)
			throw new TipException("Password or username is incorrect!");
		return res.get(0);
	}
	
	@Transactional
	public void updateById(UserVo userVo) {
		// TODO Auto-generated method stub
		if (userVo==null || userVo.getUid()==null) {
			throw new TipException("User is invalid");
		}
		int index = userDao.updateByPrimaryKeySelective(userVo);
		if(index != 1) {
			throw new TipException("Update user failed, the return value is not 1");
		}
	}
	
}
