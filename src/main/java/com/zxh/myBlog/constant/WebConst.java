package com.zxh.myBlog.constant;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * 
 * @author xzou
 *
 */
@Component
public class WebConst {
	public static String LOGIN_SESSION_KEY = "login_user";

	
	public static Map<String, String> initConfig = new HashMap<String, String>();
	/**
     * aes encode with salt
     */
    public static String AES_SALT = "0123456789abcdef";
    public static final String USER_IN_COOKIE = "S_L_ID";


	public static final int MAX_POSTS = 10;


	public static final int MAX_TITLE_COUNT = 200;


	public static final int MAX_TEXT_COUNT = 200000;


	public static final String SUCCESSFUL_RESULT = "SUCCESS";


	public static final int MAX_FILE_SIZE = 10240;


	public static final int MAX_PAGE = 20;

}
