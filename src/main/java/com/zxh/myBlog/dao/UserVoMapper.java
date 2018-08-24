package com.zxh.myBlog.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zxh.myBlog.model.Vo.UserVo;
import com.zxh.myBlog.model.Vo.UserVoExample;

@Component
public interface UserVoMapper {
	
	long countByExample(UserVoExample example);
	
	long deleteByExample(UserVoExample example);
	
	long deleteByPrimaryKey(Integer uid);
	
	int insert(UserVo user);
	
	int insertSelective(UserVo user);
	
	List<UserVo> selectByExample(UserVoExample example);
	
	UserVo selectByPrimaryKey(Integer uid);

    int updateByExampleSelective(@Param("record") UserVo record, @Param("example") UserVoExample example);

    int updateByExample(@Param("record") UserVo record, @Param("example") UserVoExample example);

    int updateByPrimaryKeySelective(UserVo record);

    int updateByPrimaryKey(UserVo record);
}
