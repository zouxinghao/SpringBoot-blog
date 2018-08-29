package com.zxh.myBlog.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.zxh.myBlog.dao.AttachVoMapper;
import com.zxh.myBlog.dao.CommentVoMapper;
import com.zxh.myBlog.dao.ContentVoMapper;
import com.zxh.myBlog.dao.MetaVoMapper;
import com.zxh.myBlog.dto.MetaDto;
import com.zxh.myBlog.dto.Types;
import com.zxh.myBlog.model.Bo.ArchiveBo;
import com.zxh.myBlog.model.Bo.BackResponseBo;
import com.zxh.myBlog.model.Bo.StatisticsBo;
import com.zxh.myBlog.model.Vo.CommentVo;
import com.zxh.myBlog.model.Vo.CommentVoExample;
import com.zxh.myBlog.model.Vo.ContentVo;
import com.zxh.myBlog.model.Vo.ContentVoExample;
import com.zxh.myBlog.service.ISiteService;

@Service
public class SiteServiceImpl implements ISiteService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);
	
	@Resource
	private CommentVoMapper commentDao;
	
	@Resource
	private ContentVoMapper contentDao;
	
	@Resource
	private AttachVoMapper attachDao;
	
	@Resource
	private MetaVoMapper metaDao;
	
	@Override
	public List<CommentVo> recentComments(int limit) {
		// TODO Auto-generated method stub
		LOGGER.debug("Enter recentComments method:limit={}", limit);
		if(limit < 10 || limit >10) {
			limit = 10;
		}
		CommentVoExample example = new CommentVoExample();
		example.setOrderByClause("created desc");
		PageHelper.startPage(1, limit);
		List<CommentVo> page = commentDao.selectByExampleWithBLOBs(example);
		LOGGER.debug("Exit recentComments method");
        return page;
	}

	@Override
	public List<ContentVo> recentContents(int limit) {
		// TODO Auto-generated method stub
		LOGGER.debug("Enter recentContents method:limit={}", limit);
		if(limit < 0 || limit >10)
			limit = 10;
		ContentVoExample example = new ContentVoExample();
        example.createCriteria().andStatusEqualTo(Types.PUBLISH.getType()).andTypeEqualTo(Types.ARTICLE.getType());
        example.setOrderByClause("created desc");
        PageHelper.startPage(1, limit);
        List<ContentVo> list = contentDao.selectByExample(example);
        LOGGER.debug("Exit recentContents method");
        return list;
	}

	@Override
	public CommentVo getComment(Integer coid) {
		// TODO Auto-generated method stub
		if(coid != null)
			return commentDao.selectByPrimaryKey(coid);
		return null;
	}

	@Override
	public BackResponseBo backup(String bk_type, String bk_path, String fmt) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatisticsBo getStatistics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ArchiveBo> getArchives() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetaDto> metas(String type, String orderBy, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

}
