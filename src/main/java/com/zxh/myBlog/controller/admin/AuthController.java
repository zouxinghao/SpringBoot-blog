package com.zxh.myBlog.controller.admin;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zxh.myBlog.controller.BaseController;
import com.zxh.myBlog.exception.TipException;
import com.zxh.myBlog.service.IUserService;

@Controller
@RequestMapping("/admin")
@Transactional(rollbackFor = TipException.class)
public class AuthController extends BaseController{
	
	private Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
	
	@Resource
	private IUserService usersService;
	
	@GetMapping(value = "/login")
	public String login() {
		return "admin/login";
	}
	
	@PostMapping(value = "login")
	@ResponseBody
	
	
}
