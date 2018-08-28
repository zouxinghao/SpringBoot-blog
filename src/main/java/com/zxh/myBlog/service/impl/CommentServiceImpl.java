package com.zxh.myBlog.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zxh.myBlog.constant.WebConst;
import com.zxh.myBlog.dao.CommentVoMapper;
import com.zxh.myBlog.exception.TipException;
import com.zxh.myBlog.model.Bo.CommentBo;
import com.zxh.myBlog.model.Vo.CommentVo;
import com.zxh.myBlog.model.Vo.CommentVoExample;
import com.zxh.myBlog.model.Vo.ContentVo;
import com.zxh.myBlog.service.ICommentService;
import com.zxh.myBlog.service.IContentService;
import com.zxh.myBlog.utils.DateKit;
import com.zxh.myBlog.utils.TaleUtils;

@Service
public class CommentServiceImpl implements ICommentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);
	
	 @Resource
	 private CommentVoMapper commentDao;

	 @Resource
	 private IContentService contentService;
	
	@Override
	@Transactional
	public String insertComments(CommentVo comment) {
		// TODO Auto-generated method stub
		if(comment == null) {
			return "Comment is empty";
		}
		if(StringUtils.isBlank(comment.getAuthor())) {
			comment.setAuthor("Passenger");
		}
		if(StringUtils.isNotBlank(comment.getMail()) && !TaleUtils.isEmail(comment.getMail())) {
			return "Please input correct email";
		}
		if(StringUtils.isBlank(comment.getContent())) {
			return "The content cannot be empty";
		}
		if(comment.getContent().length()<5 || comment.getContent().length()>2000) {
			return "评论字数在5-2000个字符";
		}
		if(comment.getCid() == null) {
			 return "评论文章不能为空";
		}
		ContentVo contents = contentService.getContents(String.valueOf(comment.getCid()));
        if (null == contents) {
            return "不存在的文章";
        }
		
        comment.setOwnerId(contents.getAuthorId());
        comment.setStatus("not_audit");
        comment.setCreated(DateKit.getCurrentUnixTime());
        commentDao.insertSelective(comment);
        
        ContentVo temp = new ContentVo();
        temp.setCid(contents.getCid());
        temp.setCommentsNum(contents.getCommentsNum() + 1);
        contentService.updateContentByCid(temp);
        
		return WebConst.SUCCESSFUL_RESULT;
	}

	@Override
	public PageInfo<CommentBo> getComments(Integer cid, int page, int limit) {

        if (null != cid) {
            PageHelper.startPage(page, limit);
            CommentVoExample commentVoExample = new CommentVoExample();
            commentVoExample.createCriteria().andCidEqualTo(cid).andParentEqualTo(0).andStatusIsNotNull().andStatusEqualTo("approved");
            commentVoExample.setOrderByClause("coid desc");
            List<CommentVo> parents = commentDao.selectByExampleWithBLOBs(commentVoExample);
            PageInfo<CommentVo> commentPaginator = new PageInfo<>(parents);
            PageInfo<CommentBo> returnBo = copyPageInfo(commentPaginator);
            if (parents.size() != 0) {
                List<CommentBo> comments = new ArrayList<>(parents.size());
                parents.forEach(parent -> {
                    CommentBo comment = new CommentBo(parent);
                    comments.add(comment);
                });
                returnBo.setList(comments);
            }
            return returnBo;
        }
        return null;
    }

	private <T> PageInfo<T> copyPageInfo(PageInfo ordinal) {
        PageInfo<T> returnBo = new PageInfo<T>();
        returnBo.setPageSize(ordinal.getPageSize());
        returnBo.setPageNum(ordinal.getPageNum());
        returnBo.setEndRow(ordinal.getEndRow());
        returnBo.setTotal(ordinal.getTotal());
        returnBo.setHasNextPage(ordinal.isHasNextPage());
        returnBo.setHasPreviousPage(ordinal.isHasPreviousPage());
        returnBo.setIsFirstPage(ordinal.isIsFirstPage());
        returnBo.setIsLastPage(ordinal.isIsLastPage());
        returnBo.setNavigateFirstPage(ordinal.getNavigateFirstPage());
        returnBo.setNavigateLastPage(ordinal.getNavigateLastPage());
        returnBo.setNavigatepageNums(ordinal.getNavigatepageNums());
        returnBo.setSize(ordinal.getSize());
        returnBo.setPrePage(ordinal.getPrePage());
        returnBo.setNextPage(ordinal.getNextPage());
        return returnBo;
    }

	@Override
	public PageInfo<CommentVo> getCommentsWithPage(CommentVoExample commentVoExample, int page, int limit) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, limit);
		List<CommentVo> commentVos = commentDao.selectByExampleWithBLOBs(commentVoExample);
		PageInfo<CommentVo> pageInfo = new PageInfo<>(commentVos);
		return pageInfo;
	}

	@Override
    @Transactional
    public void update(CommentVo comments) {
        if (null != comments && null != comments.getCoid()) {
            commentDao.updateByPrimaryKeyWithBLOBs(comments);
        }
    }

    @Override
    @Transactional
    public void delete(Integer coid, Integer cid) {
        if (null == coid) {
            throw new TipException("主键为空");
        }
        commentDao.deleteByPrimaryKey(coid);
        ContentVo contents = contentService.getContents(cid + "");
        if (null != contents && contents.getCommentsNum() > 0) {
            ContentVo temp = new ContentVo();
            temp.setCid(cid);
            temp.setCommentsNum(contents.getCommentsNum() - 1);
            contentService.updateContentByCid(temp);
        }
    }

    @Override
    public CommentVo getCommentById(Integer coid) {
        if (null != coid) {
            return commentDao.selectByPrimaryKey(coid);
        }
        return null;
    }

}
