package com.zxh.myBlog.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.zxh.myBlog.constant.WebConst;
import com.zxh.myBlog.model.Vo.ContentVo;
import com.zxh.myBlog.service.ICommentService;
import com.zxh.myBlog.service.IContentService;
import com.zxh.myBlog.service.IMetaService;
import com.zxh.myBlog.service.ISiteService;

@Controller
public class IndexController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	
	@Resource
	private IContentService contentService;
	
	@Resource
	private ICommentService commentServic;
	
	@Resource
	private IMetaService metaService;
	
	@Resource
	private ISiteService siteService;
	
	@GetMapping(value = "/")
	public String index(HttpServletRequest request, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.index(request, 1, limit);
    }

	/**
	 * Main page with page info
	 * @param request
	 * @param p
	 * @param limit
	 * @return
	 */
	@GetMapping(value ="page/{p}")
	public String index(HttpServletRequest request, @PathVariable int p, @RequestParam(value = "limit", defaultValue = "12") int limit) {
		// TODO Auto-generated method stub
		p = p < 0 || p > WebConst.MAX_PAGE ? 1 : p;
		PageInfo<ContentVo> articles = contentService.getContents(p, limit);
		request.setAttribute("articles", articles);
		if(p>1) {
			this.title(request, "Page" + p);
			
		}
		return this.render("index");
	}
	
	@GetMapping(value = {"article/{cid}", "article/{cid}.html"})
	public String getArticle(HttpServletRequest request, @PathVariable String cid) {
		ContentVo content = contentService.getContents(cid);
		if(content == null || (content.getStatus()).equals("draft")) {
			return this.render_404();
		}
		request.setAttribute("article", content);
		request.setAttribute("is_post", true);
        //renderArticle(request, content);
        //if (!checkHitsFrequency(request, cid)) {
            //updateArticleHit(content.getCid(), content.getHits());
        //}
        return this.render("post");
	}

	private boolean checkHitsFrequency(HttpServletRequest request, String cid) {
		// TODO Auto-generated method stub
		return false;
	}

	public void renderArticle(HttpServletRequest request, ContentVo content) {
		// TODO Auto-generated method stub
		if(content.getAllowComment()) {
			String co = request.getParameter("cp");
			if(StringUtils.isBlank(co)) {
				co = "1";
			}
			request.setAttribute("cp", co);
			//PageInfo<CommentBo>
		}
	}
}
