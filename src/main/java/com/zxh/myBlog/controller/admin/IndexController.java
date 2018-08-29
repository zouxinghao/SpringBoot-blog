package com.zxh.myBlog.controller.admin;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
import com.zxh.myBlog.model.Bo.StatisticsBo;
import com.zxh.myBlog.model.Vo.CommentVo;
import com.zxh.myBlog.model.Vo.ContentVo;
import com.zxh.myBlog.model.Vo.LogVo;
import com.zxh.myBlog.model.Vo.UserVo;
import com.zxh.myBlog.service.ILogService;
import com.zxh.myBlog.service.ISiteService;
import com.zxh.myBlog.service.IUserService;
import com.zxh.myBlog.utils.TaleUtils;

@Controller("adminIndexController")
@RequestMapping("/admin")
@Transactional(rollbackFor = TipException.class)
public class IndexController extends BaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	@Resource
	private ISiteService siteService;
	
	@Resource
	private ILogService logService;

	@Resource
	private IUserService userService;
	 
	 
	@GetMapping(value = {"","/index"})
	public String index(HttpServletRequest request) {
		LOGGER.info("Enter admin index ...");
		List<CommentVo> comments = siteService.recentComments(5);
		List<ContentVo> contents = siteService.recentContents(5);
		StatisticsBo statisticsBo = siteService.getStatistics();
		
		List<LogVo> logs = logService.getLogs(1, 5);
		
		request.setAttribute("comments", comments);
        request.setAttribute("articles", contents);
        request.setAttribute("statistics", statisticsBo);
        request.setAttribute("logs", logs);
        LOGGER.info("Exit admin index method");
        return "admin/index";
	 }
	
	@GetMapping(value = "profile")
	public String profile() {
		return "admin/prifile";
	}
	
	@PostMapping(value = "/profile")
	@ResponseBody
	public RestResponseBo saveProfile(@RequestParam String screenName, @RequestParam String email, HttpServletRequest request, HttpSession session) {
		UserVo user = this.user(request);
		if(StringUtils.isNotBlank(screenName) && StringUtils.isNotBlank(email)) {
			UserVo temp = new UserVo();
			temp.setUid(user.getUid());
			temp.setScreenName(screenName);
            temp.setEmail(email);
            userService.updateById(temp);
            //logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
            
            UserVo original = (UserVo)session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            original.setScreenName(screenName);
            original.setEmail(email);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY,original);
		}
		return RestResponseBo.ok();
	}
	
	@PostMapping(value = "/password")
	@ResponseBody
	public RestResponseBo updatePwd(@RequestParam String oldPassword, @RequestParam String password, HttpServletRequest request,HttpSession session) {
		UserVo user = this.user(request);
		if(StringUtils.isBlank(password) || StringUtils.isBlank(oldPassword)) {
			return RestResponseBo.fail("Please input correct information!");
		}
		if(!user.getPassword().equals(TaleUtils.MD5encode(user.getUsername()+oldPassword))) {
			return RestResponseBo.fail("The password is incorrect!");
		}
		
		try {
			UserVo temp = new UserVo();
			temp.setUid(user.getUid());
			String pwd = TaleUtils.MD5encode(user.getUsername()+password);
			temp.setPassword(pwd);
			userService.updateById(temp);
			
			UserVo original= (UserVo)session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            original.setPassword(pwd);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY,original);
            return RestResponseBo.ok();
		} catch (Exception e) {
			String msg = "Cannot change the password!";
			if(e instanceof TipException) {
				msg = e.getMessage();
			} else {
				LOGGER.error(msg, e);
			}
			return RestResponseBo.fail(msg);
		}
	}
	
}
