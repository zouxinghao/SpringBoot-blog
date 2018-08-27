package com.zxh.myBlog.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zxh.myBlog.service.IUserService;
import com.zxh.myBlog.utils.MapCache;

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

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
		// TODO Auto-generated method stub
		String context = request.getContextPath();
		String Uri = request.getRequestURI();
		
		LOGGER.info("UserAgent: {}", request.getHeader(USER_AGENT));
		LOGGER.info("User's visit IP address: {}, IP address: {}", Uri, IPKit.getIpAddrByRequest(request));
		return false;
	}
}
