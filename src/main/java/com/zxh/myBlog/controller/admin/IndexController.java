package com.zxh.myBlog.controller.admin;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zxh.myBlog.controller.BaseController;
import com.zxh.myBlog.exception.TipException;

@Controller("adminIndexController")
@RequestMapping("/admin")
@Transactional(rollbackFor = TipException.class)
public class IndexController extends BaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class); 
}
