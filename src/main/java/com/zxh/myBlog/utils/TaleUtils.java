package com.zxh.myBlog.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zxh.myBlog.constant.WebConst;

/**
 * 
 * @author xzou
 * Referring Project Tale
 * Utils for blog function
 */

public class TaleUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(TaleUtils.class);
	
	/**
	 * MD5 to encode
	 * @param source
	 * @return
	 */
	public static String MD5encode(String source) {
		if(StringUtils.isBlank(source))
			return null;
		
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ignored) {
        }
		byte[] encode = messageDigest.digest(source.getBytes());
		StringBuilder hexString = new StringBuilder();
		for(byte code : encode) {
			String hex = Integer.toHexString(0xff & code);
			if(hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
	
	public static void setCookie(HttpServletResponse response, Integer uid) {
		try {
			String val = Tools.enAes(uid.toString(), WebConst.AES_SALT);
			boolean isSSL = false;
			Cookie cookie = new Cookie(WebConst.USER_IN_COOKIE, val);
			cookie.setPath("/");
			cookie.setMaxAge(60*30);
			cookie.setSecure(isSSL);
			response.addCookie(cookie);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
