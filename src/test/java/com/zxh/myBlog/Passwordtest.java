package com.zxh.myBlog;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zxh.myBlog.model.Vo.UserVo;
import com.zxh.myBlog.utils.TaleUtils;


@Disabled
@RunWith(JUnitPlatform.class)
@DisplayName("Testing Login System ... ")
public class Passwordtest{
	private static final Logger LOGGER = LoggerFactory.getLogger(Passwordtest.class);
	@Test
	@DisplayName("Testing password function ...")
	public void testPassword () {
		LOGGER.info("@Test:testPassword");
		UserVo user = new UserVo();
		user.setUsername("admin");
		user.setPassword("J9lew2irojE23");
		String encodePwd = TaleUtils.MD5encode(user.getUsername() + user.getPassword());
	}
}