package com.zxh.myBlog.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zxh.myBlog.dao.AttachVoMapper;
import com.zxh.myBlog.model.Vo.AttachVo;
import com.zxh.myBlog.model.Vo.AttachVoExample;
import com.zxh.myBlog.service.IAttachService;
import com.zxh.myBlog.utils.DateKit;

@Service
public class AttachServiceImpl implements IAttachService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AttachServiceImpl.class);
	
	@Resource
	private AttachVoMapper attachDao;
	
	
	@Override
	public PageInfo<AttachVo> getAttaches(Integer page, Integer limit) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, limit);
		AttachVoExample attachVoExample = new AttachVoExample();
        attachVoExample.setOrderByClause("id desc");
        List<AttachVo> attachVos = attachDao.selectByExample(attachVoExample);
        return new PageInfo<>(attachVos);
	}

	@Override
	@Transactional
	public void save(String fname, String fkey, String ftype, Integer author) {
		// TODO Auto-generated method stub
		AttachVo attach = new AttachVo();
        attach.setFname(fname);
        attach.setAuthorId(author);
        attach.setFkey(fkey);
        attach.setFtype(ftype);
        attach.setCreated(DateKit.getCurrentUnixTime());
        attachDao.insertSelective(attach);
	}

	@Override
	public AttachVo selectById(Integer id) {
		// TODO Auto-generated method stub
		if(id != null) {
			return attachDao.selectByPrimaryKey(id);
		}
		return null;
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		if(id != null) {
			attachDao.deleteByPrimaryKey(id);
		}
	}

}
