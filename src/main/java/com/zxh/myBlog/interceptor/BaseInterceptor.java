package com.zxh.myBlog.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zxh.myBlog.constant.WebConst;
import com.zxh.myBlog.dto.Types;
import com.zxh.myBlog.model.Vo.OptionVo;
import com.zxh.myBlog.model.Vo.UserVo;
import com.zxh.myBlog.service.IOptionService;
import com.zxh.myBlog.service.IUserService;
import com.zxh.myBlog.utils.AdminCommons;
import com.zxh.myBlog.utils.Commons;
import com.zxh.myBlog.utils.IPKit;
import com.zxh.myBlog.utils.MapCache;
import com.zxh.myBlog.utils.TaleUtils;
import com.zxh.myBlog.utils.UUID;



@Component
public class BaseInterceptor implements HandlerInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseInterceptor.class);
	private static final String USER_AGENT = "user-agent";
	
	@Resource
	private IUserService userService;
	
	@Resource
	private IOptionService optionService;
	
	@Resource
	private Commons commons;
	
	@Resource
	private AdminCommons adminCommons;
	
	private MapCache cache = MapCache.single();

	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView m)
			throws Exception {
		// TODO Auto-generated method stub
		OptionVo optionVo = optionService.getOptionByName("site_record");
		request.setAttribute("commons", commons);//一些工具类和公共方法
		request.setAttribute("option", optionVo);
		request.setAttribute("adminCommons", adminCommons);
		
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
		// TODO Auto-generated method stub
		String contextPath = request.getContextPath();
		String uri = request.getRequestURI();
		
		LOGGER.info("UserAgent: {}", request.getHeader(USER_AGENT));
		LOGGER.info("User's visit IP address: {}, IP address: {}", Uri, IPKit.getIpAddrByRequest(request));
		
		// If cookie exist, hold on the request
		UserVo user = TaleUtils.getLoginUser(request);
		if(user == null) {
			Integer uid = TaleUtils.getCookieUid(request);
			if(uid != null) {
				// DANGER: Cookie could be fake
				user = userService.queryById(uid);
				request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, user);
			}
		}
		if (uri.startsWith(contextPath + "/admin") && !uri.startsWith(contextPath + "/admin/login") && null == user) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        }
			
			// set token for GET request
			if(request.getMethod().equals("GET")) {
				String csrf_token = UUID.UU64();
				
				cache.hset(Types.CSRF_TOKEN.getType(), csrf_token, 40*60);
				request.setAttribute("_csrf_token", csrf_token);
			}
			return true;
		}
	}

