package com.zxh.myBlog.utils;

import java.awt.Image;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zxh.myBlog.constant.WebConst;
import com.zxh.myBlog.controller.admin.AttachController;
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
	
	/**
	 * get the directory where to save the files, where does jar located
	 * @return
	 */
	public static String getUploadFilePath() {
		// TODO Auto-generated method stub
		String Path = TaleUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		Path = Path.substring(1, Path.length());
		try {
			Path = java.net.URLDecoder.decode(Path, "utf-8");
			
		} catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		
		int lastIndex = Path.lastIndexOf("/") + 1;
		Path  = Path.substring(0, lastIndex);
		File file = new File("");
		
		return file.getAbsolutePath()+"/";
	}

	public static boolean isImage(InputStream inputStream) {
		// TODO Auto-generated method stub
		try {
			Image img = ImageIO.read(inputStream);
			if(img == null || img.getWidth(null) == 0 || img.getHeight(null) == 0) {
				return false;
			}
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	public static String getFileKey(String fname) {
		// TODO Auto-generated method stub
		String prefix = "/upload" + DateKit.dateFormat(new Date(), "yyyy/MM");
		if(!new File(AttachController.CLASSPATH + prefix).exists()) {
			new File(AttachController.CLASSPATH + prefix).mkdirs();
		}
		
		fname = StringUtils.trimToNull(fname);
		if(fname == null) {
			return prefix + "/" + UUID.UU32() + "." + null;
		} else {
			fname = fname.replaceAll("\\", "/");
			fname = fname.substring(fname.lastIndexOf("/") + 1);
			int index = fname.lastIndexOf(".");
			String ext = null;
			if(index >= 0) {
				ext = StringUtils.trimToNull(fname.substring(index+1));
			}
			return prefix + "/" + UUID.UU32() + "." + (ext == null ? null : (ext));
		}
	}

	/**
     * markdown转换为html
     *
     * @param markdown
     * @return
     */
    public static String mdToHtml(String markdown) {
        if (StringUtils.isBlank(markdown)) {
            return "";
        }
        java.util.List<Extension> extensions = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder().extensions(extensions).build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();
        String content = renderer.render(document);
        content = Commons.emoji(content);
        return content;
    }

    /**
     * 提取html中的文字
     *
     * @param html
     * @return
     */
    public static String htmlToText(String html) {
        if (StringUtils.isNotBlank(html)) {
            return html.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
        }
        return "";
    }

	
	
}
