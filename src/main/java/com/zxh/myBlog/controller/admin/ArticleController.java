package com.zxh.myBlog.controller.admin;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.zxh.myBlog.constant.WebConst;
import com.zxh.myBlog.controller.BaseController;
import com.zxh.myBlog.dto.LogActions;
import com.zxh.myBlog.dto.Types;
import com.zxh.myBlog.model.Bo.RestResponseBo;
import com.zxh.myBlog.model.Vo.ContentVo;
import com.zxh.myBlog.model.Vo.ContentVoExample;
import com.zxh.myBlog.model.Vo.MetaVo;
import com.zxh.myBlog.model.Vo.UserVo;
import com.zxh.myBlog.service.IContentService;
import com.zxh.myBlog.service.ILogService;
import com.zxh.myBlog.service.IMetaService;

@Controller
@RequestMapping("/admin/article")
public class ArticleController extends BaseController{
	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);
	
	@Resource
	private IContentService contentService;
	
	@Resource
	private IMetaService metaService;
	
	@Resource
	private ILogService logService;
	
	@GetMapping(value = "")
	public String index(@RequestParam(value = "page", defaultValue = "1") int page, 
						@RequestParam(value = "limit", defaultValue = "10") int limit, HttpServletRequest request) {
		ContentVoExample example = new ContentVoExample();
		example.setOrderByClause("created desc");
		example.createCriteria().andTypeEqualTo(Types.ARTICLE.getType());
		PageInfo<ContentVo> contentPage = contentService.getArticlesWithpage(example, page, limit);
		request.setAttribute("articles", contentPage);
		return "admin/article_list";
	}
	
	@GetMapping(value = "/publish")
	public String newArticle(HttpServletRequest request) {
		List<MetaVo> meta = metaService.getMetas(Types.CATEGORY.getType());
		request.setAttribute("categories", meta);
		return "admin/article_edit";
	}
	
	@GetMapping(value = "/{cid}")
	public String editArticle(@PathVariable String cid, HttpServletRequest request) {
		ContentVo content = contentService.getContents(cid);
		request.setAttribute("contents", content);
		List<MetaVo> categories = metaService.getMetas(Types.CATEGORY.getType());
		request.setAttribute("categories", categories);
        request.setAttribute("active", "article");
        return "admin/article_edit";
	}
	
	@PostMapping(value = "/publish")
	@ResponseBody
	public RestResponseBo publishArticle(ContentVo content, HttpServletRequest request) {
		UserVo user = this.user(request);
		content.setAuthorId(user.getUid());
		content.setCategories(Types.ARTICLE.getType());
		if(StringUtils.isBlank(content.getCategories())) {
			content.setCategories("Oopse");
		}
		String result = contentService.publish(content);
		if(!result.equals(WebConst.SUCCESSFUL_RESULT)) {
			return RestResponseBo.fail(result);
		}
		return RestResponseBo.ok();
	}
	
	@PostMapping("/modify")
	@ResponseBody
	public RestResponseBo modifyArticle(ContentVo contents, HttpServletRequest request) {
		UserVo user = this.user(request);
		contents.setAuthorId(user.getUid());
		contents.setType(Types.ARTICLE.getType());
        String result = contentService.updateArticle(contents);
        if (!WebConst.SUCCESSFUL_RESULT.equals(result)) {
            return RestResponseBo.fail(result);
        }
        return RestResponseBo.ok();
	}
	
	@RequestMapping(value = "/delete")
    @ResponseBody
    public RestResponseBo delete(@RequestParam int cid, HttpServletRequest request) {
        String result = contentService.deleteByCid(cid);
        //logService.insertLog(LogActions.DEL_ARTICLE.getAction(), cid + "", request.getRemoteAddr(), this.getUid(request));
        if (!WebConst.SUCCESSFUL_RESULT.equals(result)) {
            return RestResponseBo.fail(result);
        }
        return RestResponseBo.ok();
    }
}
