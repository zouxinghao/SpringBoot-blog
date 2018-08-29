package com.zxh.myBlog.service;

import com.github.pagehelper.PageInfo;
import com.zxh.myBlog.model.Vo.AttachVo;

public interface IAttachService {
	
	PageInfo<AttachVo> getAttaches(Integer page, Integer limit);
	
	void save(String fname, String fkey, String ftype, Integer author);
	
	AttachVo selectById(Integer id);
	
	void deleteById(Integer id);
}
