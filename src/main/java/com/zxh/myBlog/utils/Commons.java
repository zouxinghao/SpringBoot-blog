package com.zxh.myBlog.utils;

import org.apache.commons.lang3.StringUtils;

import com.github.pagehelper.PageInfo;
import com.zxh.myBlog.constant.WebConst;

/**
 * Using for Theme function
 * 
 * @author xzou
 *
 */
public class Commons {
	
	public static String THEME = "themes/default";
	
	/**
	 * Wether there is data in page
	 * @param page
	 * @return
	 */
	public static boolean is_empty(PageInfo page) {
		return page == null || (page.getList()==null||(page.getList().size())==0);
	}
	
	/**
	 * connect site
	 * @return
	 */
	public static String site_url() {
		return site_url("");
	}

	/**
	 * return complete address
	 * @param string the left part address
	 * @return
	 */
	private static String site_url(String string) {
		// TODO Auto-generated method stub
		return site_option("site_url")+ string;
	}
	/**
	 * the title for the website
	 * @return
	 */
	public static String site_title() {
		return site_option("site_title");
	}

	private static String site_option(String string) {
		// TODO Auto-generated method stub
		return site_option(string, "");
	}

	/**
	 * configure the website
	 * @param string
	 * @param string2
	 * @return
	 */
	private static String site_option(String string, String string2) {
		// TODO Auto-generated method stub
		if(StringUtils.isBlank(string))
			return "";
		String str = WebConst.initConfig.get(string);
		if(StringUtils.isBlank(str)) {
			return str;
		}else {
			return string2;
		}
	}
	
	public static String theme_url() {
		return site_url(Commons.THEME);
	}
	
	public static String substr(String str, int len) {
		if(str.length()>len)
			return str.substring(0, len);
		return str;
	}
	
	public static String theme_url(String sub) {
		return site_url(Commons.THEME+sub);
	}
}
