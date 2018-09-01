package com.zxh.myBlog.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import com.zxh.myBlog.constant.WebConst;
import com.zxh.myBlog.dao.ContentVoMapper;
import com.zxh.myBlog.dao.MetaVoMapper;
import com.zxh.myBlog.dto.Types;
import com.zxh.myBlog.exception.TipException;
import com.zxh.myBlog.model.Vo.ContentVo;
import com.zxh.myBlog.model.Vo.ContentVoExample;
import com.zxh.myBlog.service.IContentService;
import com.zxh.myBlog.service.IMetaService;
import com.zxh.myBlog.utils.DateKit;
import com.zxh.myBlog.utils.TaleUtils;
import com.zxh.myBlog.utils.Tools;

@Service
public class ContentServiceImpl implements IContentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentServiceImpl.class);
	
	@Resource
	private ContentVoMapper contentDao;
	
	@Resource
	private MetaVoMapper metaDao;
	
	@Resource
	private IMetaService metaService;
	
	
	@Override
	@Transactional
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
		if(StringUtils.isNotBlank(contentVo.getSlug())) {
			if(contentVo.getSlug().length()<5) {
				return "The path is too short";
			}
			if(!TaleUtils.isPath(contentVo.getSlug())) return "The path you input is illegal!";
			ContentVoExample contentVoExample = new ContentVoExample();
            contentVoExample.createCriteria().andTypeEqualTo(contentVo.getType()).andStatusEqualTo(contentVo.getSlug());
            long count = contentDao.countByExample(contentVoExample);
            if (count > 0) return "该路径已经存在，请重新输入";
        } else {
        	contentVo.setSlug(null);
        }
		
		contentVo.setContent(EmojiParser.parseToAliases(contentVo.getContent()));
		
		int time = DateKit.getCurrentUnixTime();
		contentVo.setCreated(time);
		contentVo.setModified(time);
		contentVo.setHits(0);
		contentVo.setCommentsNum(0);
		
		String tag = contentVo.getTags();
		String category = contentVo.getCategories();
		contentDao.insert(contentVo);
		Integer cid = contentVo.getCid();
		metaService.saveMetas(cid, tag, Types.TAG.getType());
		metaService.saveMetas(cid, category, Types.CATEGORY.getType());
		return WebConst.SUCCESSFUL_RESULT;
	}

	@Override
	public PageInfo<ContentVo> getContents(Integer p, Integer limit) {
		// TODO Auto-generated method stub
		LOGGER.debug("Enter getContents Method ...");
		ContentVoExample contentVoExample = new ContentVoExample();
		contentVoExample.setOrderByClause("created desc");
		contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
		PageHelper.startPage(p, limit);
		List<ContentVo> data = contentDao.selectByExample(contentVoExample);
		PageInfo<ContentVo> pageInfo = new PageInfo<>(data);
		LOGGER.debug("Exit getContents method");
        return pageInfo;
	}

	@Override
	public ContentVo getContents(String id) {
		// TODO Auto-generated method stub
		if(StringUtils.isNotBlank(id)) {
			if(Tools.isNumber(id)) {
				ContentVo contentVo = contentDao.selectByPrimaryKey(Integer.valueOf(id));
				return contentVo;
			} else {
				ContentVoExample example = new ContentVoExample();
				example.createCriteria().andSlugEqualTo(id);
				List<ContentVo> contentVos = contentDao.selectByExampleWithBLOBs(example);
				if(contentVos.size()!=1) {
					throw new TipException("query content by id and return is not one");
				}
				return contentVos.get(0);
			}
		}
		return null;
	}

	@Override
	public void updateContentByCid(ContentVo contentVo) {
		// TODO Auto-generated method stub
		if(contentVo != null && contentVo.getCid() != null){
			contentDao.updateByPrimaryKeySelective(contentVo);
		}
	}

	@Override
	public PageInfo<ContentVo> getArticles(Integer mid, int page, int limit) {
		// TODO Auto-generated method stub
		int total = metaDao.countWithSql(mid);
		PageHelper.startPage(page, limit);
		List<ContentVo> list = contentDao.findByCatalog(mid);
		PageInfo<ContentVo> paginator = new PageInfo<>(list);
		paginator.setTotal(total);
        return paginator;
	}

	@Override
	public PageInfo<ContentVo> getArticles(String keyword, Integer page, Integer limit) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, limit);
        ContentVoExample contentVoExample = new ContentVoExample();
        ContentVoExample.Criteria criteria = contentVoExample.createCriteria();
        criteria.andTypeEqualTo(Types.ARTICLE.getType());
        criteria.andStatusEqualTo(Types.PUBLISH.getType());
        criteria.andTitleLike("%" + keyword + "%");
        contentVoExample.setOrderByClause("created desc");
        List<ContentVo> contentVos = contentDao.selectByExampleWithBLOBs(contentVoExample);
        return new PageInfo<>(contentVos);
	}

	@Override
	public PageInfo<ContentVo> getArticlesWithpage(ContentVoExample commentVoExample, Integer page, Integer limit) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, limit);
		List<ContentVo> list = contentDao.selectByExampleWithBLOBs(commentVoExample);
		return new PageInfo<>(list);
	}

	@Override
	@Transactional
	public String deleteByCid(Integer cid) {
		// TODO Auto-generated method stub
		ContentVo content = this.getContents(cid + "");
		if(content != null) {
			contentDao.deleteByPrimaryKey(cid);
			// relationship function
			return WebConst.SUCCESSFUL_RESULT;
		}
		return "The data does not exist!";
	}

	@Override
	@Transactional
	public String updateArticle(ContentVo contentVo) {
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
		if(StringUtils.isBlank(contentVo.getSlug())) {
			contentVo.setSlug(contentVo.getSlug());
		}
		int time = DateKit.getCurrentUnixTime();
		contentVo.setModified(time);
		Integer cid = contentVo.getCid();
		contentVo.setContent(EmojiParser.parseToAliases(contentVo.getContent()));
		
		contentDao.updateByPrimaryKeySelective(contentVo);
		// relationship function
		metaService.saveMetas(cid, contentVo.getTags(), Types.TAG.getType());
        metaService.saveMetas(cid, contentVo.getCategories(), Types.CATEGORY.getType());
        return WebConst.SUCCESSFUL_RESULT;
	}

	@Override
	public void updateCategory(String ordinal, String newCatefory) {
		// TODO Auto-generated method stub
		ContentVo content = new ContentVo();
		content.setCategories(newCatefory);
        ContentVoExample example = new ContentVoExample();
        example.createCriteria().andCategoriesEqualTo(ordinal);
        contentDao.updateByExampleSelective(content, example);
	}

}
