package com.zxh.myBlog;

import com.zxh.myBlog.model.Vo.UserVo;
import com.zxh.myBlog.utils.TaleUtils;

public class Passwordtest {
	public static void mian(String args[]) {
		UserVo user = new UserVo();
		user.setUsername("admin");
		user.setPassword("J9lew2irojE23");
		String encode = TaleUtils.MD5encode(user.getUsername() + user.getPassword());
		System.out.println(encode);
	}
}

