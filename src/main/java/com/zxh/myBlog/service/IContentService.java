package com.zxh.myBlog.service;

import com.github.pagehelper.PageInfo;
import com.zxh.myBlog.model.Vo.ContentVo;
import com.zxh.myBlog.model.Vo.ContentVoExample;

public interface IContentService {
	
	/**
	 * publish articles
	 * @param contentVo
	 * @return
	 */
	String publish(ContentVo contentVo);
	
	/**
	 * get mutiple return for query
	 * @param p current page
	 * @param limit number of each page
	 * @return
	 */
	PageInfo<ContentVo> getContents(Integer p, Integer limit);
	
	/**
	 * query content through id or slug
	 * @param id
	 * @return
	 */
	ContentVo getContents(String id);
	
	void updateContentByCid(ContentVo contentVo);
	 
	PageInfo<ContentVo> getArticles(Integer mid, int page, int limit);
	
	PageInfo<ContentVo> getArticles(String keyword,Integer page,Integer limit);
	
	/**
     * @param commentVoExample
     * @param page
     * @param limit
     * @return
     */
    PageInfo<ContentVo> getArticlesWithpage(ContentVoExample commentVoExample, Integer page, Integer limit);
    /**
     * 根据文章id删除
     * @param cid
     */
    String deleteByCid(Integer cid);

    /**
     * 编辑文章
     * @param contents
     */
    String updateArticle(ContentVo contents);


    /**
     * 更新原有文章的category
     * @param ordinal
     * @param newCatefory
     */
    void updateCategory(String ordinal,String newCatefory);
}
