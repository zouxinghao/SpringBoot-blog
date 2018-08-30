package com.zxh.myBlog.controller.admin;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageInfo;
import com.zxh.myBlog.constant.WebConst;
import com.zxh.myBlog.controller.BaseController;
import com.zxh.myBlog.dto.Types;
import com.zxh.myBlog.model.Vo.ContentVo;
import com.zxh.myBlog.model.Vo.ContentVoExample;
import com.zxh.myBlog.service.IContentService;
import com.zxh.myBlog.service.ILogService;

@Controller
@RequestMapping("admin/page")
public class PageController extends BaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PageController.class);
	
	@Resource
    private IContentService contentsService;

    @Resource
    private ILogService logService;
    
    @GetMapping(value = "")
    public String index(HttpServletRequest request) {
    	ContentVoExample example = new ContentVoExample();
    	example.setOrderByClause("created desc");
    	example.createCriteria().andTypeEqualTo(Types.PAGE.getType());
    	PageInfo<ContentVo> contentsPaginator = contentsService.getArticlesWithpage(example, 1, WebConst.MAX_POSTS);
        request.setAttribute("articles", contentsPaginator);
        return "admin/page_list";
    }
    
}
