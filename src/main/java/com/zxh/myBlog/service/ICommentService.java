package com.zxh.myBlog.service;

import com.github.pagehelper.PageInfo;
import com.zxh.myBlog.model.Bo.CommentBo;
import com.zxh.myBlog.model.Vo.CommentVo;
import com.zxh.myBlog.model.Vo.CommentVoExample;

public interface ICommentService {

	
	String insertComments(CommentVo comment);
	
	/**
	 * Get all of the comments of the page
	 * @param cid
	 * @param page
	 * @param limit
	 * @return
	 */
	PageInfo<CommentBo> getComments(Integer cid, int page, int limit);
	/**
     * 获取文章下的评论
     * @param commentVoExample
     * @param page
     * @param limit
     * @return CommentVo
     */
    PageInfo<CommentVo> getCommentsWithPage(CommentVoExample commentVoExample, int page, int limit);


    /**
     * 根据主键查询评论
     * @param coid
     * @return
     */
    CommentVo getCommentById(Integer coid);


    /**
     * 删除评论，暂时没用
     * @param coid
     * @param cid
     * @throws Exception
     */
    void delete(Integer coid, Integer cid);

    /**
     * 更新评论状态
     * @param comments
     */
    void update(CommentVo comments);

}
