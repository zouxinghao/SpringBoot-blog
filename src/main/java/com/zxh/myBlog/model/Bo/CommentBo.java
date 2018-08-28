package com.zxh.myBlog.model.Bo;

import java.util.List;

import com.zxh.myBlog.model.Vo.CommentVo;


/**
 * 在原有的基础上多态出新的功能，追加父级与子级的功能
 * @author xzou
 *
 */
public class CommentBo extends CommentVo {
	
	private int level;
	private List<CommentVo> children;
	
	public CommentBo(CommentVo comments) {
        setAuthor(comments.getAuthor());
        setMail(comments.getMail());
        setCoid(comments.getCoid());
        setAuthorId(comments.getAuthorId());
        setUrl(comments.getUrl());
        setCreated(comments.getCreated());
        setAgent(comments.getAgent());
        setIp(comments.getIp());
        setContent(comments.getContent());
        setOwnerId(comments.getOwnerId());
        setCid(comments.getCid());
    }
	
	public int getLevels() {
		return level;
	}
	
	public void setLevels(int levels) {
		this.level = levels;
	}
	
	public List<CommentVo> getChildren(){
		return children;
	}
	
	public void setChildren(List<CommentVo> children) {
		this.children = children;
	}
}
