package com.zxh.myBlog.model.Vo;

public class LogVo {
	
	/**
	 * Primary Key: log ID
	 */
	private Integer id;
	
	/**
	 * action
	 */
	
	private String action;
	 
	private String data;
	
	/**
	 * who start this action
	 */
	private Integer authorId;
	
	private String ip;
	
	/**
	 * created date
	 */
	private Integer created;
	
	private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }
}
