package com.zxh.myBlog.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zxh.myBlog.constant.WebConst;
import com.zxh.myBlog.controller.BaseController;
import com.zxh.myBlog.model.Bo.RestResponseBo;
import com.zxh.myBlog.model.Vo.OptionVo;
import com.zxh.myBlog.service.ILogService;
import com.zxh.myBlog.service.IOptionService;
import com.zxh.myBlog.service.ISiteService;

@Controller
@RequestMapping("/admin/setting")
public class SettingController extends BaseController{

	private static final Logger LOGGER = LoggerFactory.getLogger(SettingController.class);
	
	@Resource
	private IOptionService optionService;
	
	@Resource
	private ILogService logService;
	
	@Resource 
	private ISiteService siteService;
	
	@GetMapping(value = "")
	public String index(HttpServletRequest request) {
		List<OptionVo> optionList = optionService.getOptions();
		Map<String, String> map = new HashMap<>();
		optionList.forEach((o) -> {
			map.put(o.getName(), o.getValue());
		});
		if (map.get("site_record") == null) {
            map.put("site_record", "");
        }
		request.setAttribute("options", map);
		return "admin/setting";
	}
	
	@PostMapping(value = "")
	@ResponseBody
	public RestResponseBo saveSetting(@RequestParam(required = false) String site_theme, HttpServletRequest request) {
		try {
			Map<String, String[]> parameterMap = request.getParameterMap();
			Map<String, String> querys = new HashMap<>();
			parameterMap.forEach((key, value) -> {
				querys.put(key, join(value));
			});
			optionService.saveOption(querys);
			WebConst.initConfig = querys;
			if(StringUtils.isBlank(site_theme)) {
				BaseController.THEME = "themes/" + site_theme;
			}
			// Log function
			return RestResponseBo.ok();
		} catch (Exception e) {
			String msg = "保存设置失败";
            return RestResponseBo.fail(msg);
		}
	}

	private String join(String[] arr) {
		StringBuilder ret = new StringBuilder();
        String[] var3 = arr;
        int var4 = arr.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            String item = var3[var5];
            ret.append(',').append(item);
        }

        return ret.length() > 0 ? ret.substring(1) : ret.toString();
	}
}
