package com.zxh.myBlog.model.Vo;

import java.io.Serializable;

public class UserVo implements Serializable {
	
	/**
	 * User Id : Primary Key
	 */
	
	private Integer uid;
	
	private String username;
	
	private String password;
	
	private String email;
	
	/**
	 * home page address
	 */
	private String homeUrl;
	
	/**
	 * nick name (dispaly name)
	 */
	private String screenName;
	
	/**
	 * Register Time (GMT) unix time stamp
	 */
	private Integer created;
	
	/**
	 * Last Active
	 */
	private Integer activated;
	
	/**
	 * The Last Time Login
	 */
	private Integer logged;
	
	/**
	 * User Group
	 */
	private String groupName;
	
	private static final long serialVersionUID = 1L;
	
    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public Integer getActivated() {
        return activated;
    }

    public void setActivated(Integer activated) {
        this.activated = activated;
    }

    public Integer getLogged() {
        return logged;
    }

    public void setLogged(Integer logged) {
        this.logged = logged;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
	
}
