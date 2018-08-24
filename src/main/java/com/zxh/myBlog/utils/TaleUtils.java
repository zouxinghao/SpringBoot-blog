package com.zxh.myBlog.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
