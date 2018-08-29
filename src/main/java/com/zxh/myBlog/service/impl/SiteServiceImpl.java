package com.zxh.myBlog.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.zxh.myBlog.controller.admin.AttachController;
import com.zxh.myBlog.dao.AttachVoMapper;
import com.zxh.myBlog.dao.CommentVoMapper;
import com.zxh.myBlog.dao.ContentVoMapper;
import com.zxh.myBlog.dao.MetaVoMapper;
import com.zxh.myBlog.dto.MetaDto;
import com.zxh.myBlog.dto.Types;
import com.zxh.myBlog.exception.TipException;
import com.zxh.myBlog.model.Bo.ArchiveBo;
import com.zxh.myBlog.model.Bo.BackResponseBo;
import com.zxh.myBlog.model.Bo.StatisticsBo;
import com.zxh.myBlog.model.Vo.AttachVoExample;
import com.zxh.myBlog.model.Vo.CommentVo;
import com.zxh.myBlog.model.Vo.CommentVoExample;
import com.zxh.myBlog.model.Vo.ContentVo;
import com.zxh.myBlog.model.Vo.ContentVoExample;
import com.zxh.myBlog.model.Vo.MetaVoExample;
import com.zxh.myBlog.service.ISiteService;
import com.zxh.myBlog.utils.DateKit;
import com.zxh.myBlog.utils.TaleUtils;
import com.zxh.myBlog.utils.ZipUtils;

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
		BackResponseBo backResponse = new BackResponseBo();
		if(bk_type.equals("attach")) {
			if(StringUtils.isBlank(bk_path)) {
				throw new TipException("Please input the Directory for the files you want to backup");
			}
			if(!(new File(bk_path)).isDirectory()) {
				throw new TipException("Please input a correct Directory");
			}
			String bkAttachDir = AttachController.CLASSPATH + "upload";
			String bkThemesDir = AttachController.CLASSPATH + "templates/themes";
			
			String fname = DateKit.dateFormat(new Date(), fmt) + "_" + "001.zip";
			
			String attachPath = bk_path + "/" + "attachs_" + fname;
            String themesPath = bk_path + "/" + "themes_" + fname;

            ZipUtils.zipFolder(bkAttachDir, attachPath);
            ZipUtils.zipFolder(bkThemesDir, themesPath);

            backResponse.setAttachPath(attachPath);
            backResponse.setThemePath(themesPath);

		}
		
		if(bk_type.equals("db")) {
			String bkAttachDir = AttachController.CLASSPATH + "upload/";
			if(!(new File(bkAttachDir)).isDirectory()) {
				File file = new File(bkAttachDir);
				if(!file.exists())
					file.mkdirs();
			}
		}
		
		String sqlFileName = "tale_" + DateKit.dateFormat(new Date(), fmt) + "_" + "001.sql";
        String zipFile = sqlFileName.replace(".sql", ".zip");
        
        // Backup backup = new Ba
		return null;
	}

	@Override
	public StatisticsBo getStatistics() {
		// TODO Auto-generated method stub
		LOGGER.info("GET Statics info ...");
		StatisticsBo statisticsBo = new StatisticsBo();
		
		ContentVoExample contentVoExample = new ContentVoExample();
		contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
		Long articles = contentDao.countByExample(contentVoExample);
		
		Long comments = commentDao.countByExample(new CommentVoExample());
		
		Long attachs = attachDao.countByExample(new AttachVoExample());
		
		MetaVoExample metaVoExample = new MetaVoExample();
		metaVoExample.createCriteria().andTypeEqualTo(Types.LINK.getType());
		Long links = metaDao.countByExample(metaVoExample);
		
		statisticsBo.setArticles(articles);
		statisticsBo.setComments(comments);
		statisticsBo.setAttachs(attachs);
		statisticsBo.setLinks(links);
		LOGGER.debug("Exit getStatistics method");
        return statisticsBo;
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
