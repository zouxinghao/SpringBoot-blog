package com.zxh.myBlog.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.PageInfo;
import com.zxh.myBlog.constant.WebConst;
import com.zxh.myBlog.dao.ContentVoMapper;
import com.zxh.myBlog.dao.MetaVoMapper;
import com.zxh.myBlog.model.Vo.ContentVo;
import com.zxh.myBlog.model.Vo.ContentVoExample;
import com.zxh.myBlog.service.IContentService;
import com.zxh.myBlog.service.IMetaService;

public class ContentServiceImpl implements IContentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentServiceImpl.class);
	
	@Resource
	private ContentVoMapper contentDao;
	
	@Resource
	private MetaVoMapper metaDao;
	
	@Resource
	private IMetaService metaService;
	
	
	@Override
	public String publish(ContentVo contentVo) {
		// TODO Auto-generated method stub
		if(contentVo == null) {
			return "The content is empty";
		}
		if(StringUtils.isBlank(contentVo.getTitle())) {
			return "The title should not be empty";
		}
		if(StringUtils.isBlank(contentVo.getContent())) {
			return "The content should not be empty";
		}
		if(contentVo.getTitle().length()>WebConst.MAX_TITLE_COUNT) {
			return "The Title is too long";
		}
		if(contentVo.getContent().length()>WebConst.MAX_TEXT_COUNT) {
			return "The content is too long";
		}
		if(contentVo.getAuthorId()==null) {
			return "Please Log in";
		}
	}

	@Override
	public PageInfo<ContentVo> getContents(Integer p, Integer limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentVo getContents(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateContentByCid(ContentVo contentVo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PageInfo<ContentVo> getArticles(Integer mid, int page, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageInfo<ContentVo> getArticles(String keyword, Integer page, Integer limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageInfo<ContentVo> getArticlesWithpage(ContentVoExample commentVoExample, Integer page, Integer limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteByCid(Integer cid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateArticle(ContentVo contents) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateCategory(String ordinal, String newCatefory) {
		// TODO Auto-generated method stub
		
	}

}
