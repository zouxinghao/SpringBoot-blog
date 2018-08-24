package com.zxh.myBlog.controller;

import javax.servlet.http.HttpServletRequest;

import com.zxh.myBlog.model.Vo.UserVo;
import com.zxh.myBlog.utils.MapCache;

public abstract class BaseController {
	public static String THEME = "themes/default";
	
	protected MapCache cache = MapCache.single();
	
	/**
	 * Direct the theme for the main page
	 * @param viewName
	 * @return direction
	 */
	public String render(String viewName) {
		return THEME + "/" + viewName;
	}
	
	public BaseController title(HttpServletRequest request, String title) {
		request.setAttribute("title", title);
		return this;
	}
	
	public BaseController keyword(HttpServletRequest request, String keyword) {
		request.setAttribute("keyword", keyword);
		return this;
	}
	
	public UserVo user(HttpServletRequest request) {
		return this.user(request);
	}
	
	 public Integer getUid(HttpServletRequest request){
	        return this.user(request).getUid();
	    }
	
	public String render_404() {
		return "comm/error_404";
	}
}
