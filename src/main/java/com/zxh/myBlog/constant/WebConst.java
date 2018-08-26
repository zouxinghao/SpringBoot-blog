package com.zxh.myBlog.constant;

import org.springframework.stereotype.Component;

/**
 * 
 * @author xzou
 *
 */
@Component
public class WebConst {
	public static String LOGIN_SESSION_KEY = "login_user";

	/**
     * aes encode with salt
     */
    public static String AES_SALT = "0123456789abcdef";
    public static final String USER_IN_COOKIE = "S_L_ID";

}
