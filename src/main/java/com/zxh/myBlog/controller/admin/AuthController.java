package com.zxh.myBlog.controller.admin;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zxh.myBlog.constant.WebConst;
import com.zxh.myBlog.controller.BaseController;
import com.zxh.myBlog.dto.LogActions;
import com.zxh.myBlog.exception.TipException;
import com.zxh.myBlog.model.Bo.RestResponseBo;
import com.zxh.myBlog.model.Vo.UserVo;
import com.zxh.myBlog.service.ILogService;
import com.zxh.myBlog.service.IUserService;
import com.zxh.myBlog.utils.TaleUtils;

@Controller
@RequestMapping("/admin")
@Transactional(rollbackFor = TipException.class)
public class AuthController extends BaseController{
	
	private Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
	
	@Resource
	private IUserService usersService;
	
	@Resource
	private ILogService logService;
	
	@GetMapping(value = "/login")
	public String login() {
		return "admin/login";
	}
	
	/**
	 * Longin
	 * @param username
	 * @param password
	 * @param remeber_me
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping(value = "login")
	@ResponseBody
	public RestResponseBo doLogin(@RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String remeber_me,
            HttpServletRequest request,
            HttpServletResponse response) {
		Integer error_count = cache.get("login_error_count");
		try {
			UserVo user = usersService.login(username, password);
			request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, user);
			if(StringUtils.isNotBlank(remeber_me)) {
				//pending cookie function
				TaleUtils.setCookie(response, user.getUid());
			}
			logService.insertLog(LogActions.LOGIN.getAction(), null, request.getRemoteAddr(), user.getUid());
		}catch (Exception e) {
			error_count = error_count==null?1:error_count+1;
			if(error_count>3) {
				return RestResponseBo.fail("Try it again 10 minutes later");
			}
			cache.set("login_error_count", error_count, 10*60);
			String msg = "Login fail";
			if(e instanceof TipException) {
				msg = e.getMessage();
			}else {
				LOGGER.error(msg, e);
			}
			return RestResponseBo.fail();
		}
		return RestResponseBo.ok();
	}
	
	/**
	 * 
	 * @param httpsession
	 * @param response
	 * @param quest
	 */
	@RequestMapping("/logout")
	public void logout(HttpSession httpsession, HttpServletResponse response, HttpServletRequest quest) {
		httpsession.removeAttribute(WebConst.LOGIN_SESSION_KEY);
		Cookie cookie = new Cookie(WebConst.USER_IN_COOKIE, "");
		cookie.setValue(null);
		cookie.setMaxAge(0); // destory immediately
		cookie.setPath("/");
		response.addCookie(cookie);
		try {
			response.sendRedirect("/admin/login");
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("Logout fail", e);
		}
	}
	
}
