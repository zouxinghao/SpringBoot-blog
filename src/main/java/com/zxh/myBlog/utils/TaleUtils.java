package com.zxh.myBlog.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zxh.myBlog.constant.WebConst;
import com.zxh.myBlog.model.Vo.UserVo;

/**
 * 
 * @author xzou
 * Referring Project Tale
 * Utils for blog function
 */

public class TaleUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(TaleUtils.class);
	
	
	// Regular match for path
	private static final Pattern SLUG_REGEX = Pattern.compile("^[A-Za-z0-9_-]{5,100}$", Pattern.CASE_INSENSITIVE);
	private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
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
	/**
	 * get User information
	 * @param request
	 * @return
	 */
	public static UserVo getLoginUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session == null)
			return null;
		return (UserVo)session.getAttribute(WebConst.LOGIN_SESSION_KEY);
	}
	
	public static Integer getCookieUid(HttpServletRequest request) {
		if(request != null) {
			Cookie cookie = cookieRaw(WebConst.USER_IN_COOKIE, request);
			if(cookie != null && cookie.getValue()!=null) {
				try {
					String uid = Tools.deAes(cookie.getValue(), WebConst.AES_SALT);
					return StringUtils.isBlank(uid) && Tools.isNumber(uid) ? Integer.valueOf(uid):null;
				} catch (Exception e){
					
				}
			}
		}
		return null;
	}
	/**
	 * Extract cookie from cookies
	 * @return
	 */
	public static Cookie cookieRaw(String name, HttpServletRequest request) {
		// TODO Auto-generated method stub
		javax.servlet.http.Cookie[] servletCookies = request.getCookies();
		if(servletCookies == null) 
			return null;
		for(javax.servlet.http.Cookie c:servletCookies) {
			if(c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	public static boolean isPath(String slug) {
		// TODO Auto-generated method stub
		if(StringUtils.isNotBlank(slug)) {
			if(slug.contains("/") || slug.contains(" ") || slug.contains(".")) {
				return false;
			}
			Matcher matcher = SLUG_REGEX.matcher(slug);
			return matcher.find();
		}
		return false;
	}

	public static boolean isEmail(String mail) {
		// TODO Auto-generated method stub
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(mail);
		return matcher.find();
	}
}
