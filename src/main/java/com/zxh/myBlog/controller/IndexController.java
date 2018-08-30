package com.zxh.myBlog.controller;

import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import com.zxh.myBlog.constant.WebConst;
import com.zxh.myBlog.dto.MetaDto;
import com.zxh.myBlog.dto.Types;
import com.zxh.myBlog.model.Bo.ArchiveBo;
import com.zxh.myBlog.model.Bo.CommentBo;
import com.zxh.myBlog.model.Bo.RestResponseBo;
import com.zxh.myBlog.model.Vo.CommentVo;
import com.zxh.myBlog.model.Vo.ContentVo;
import com.zxh.myBlog.service.ICommentService;
import com.zxh.myBlog.service.IContentService;
import com.zxh.myBlog.service.IMetaService;
import com.zxh.myBlog.service.ISiteService;
import com.zxh.myBlog.utils.IPKit;
import com.zxh.myBlog.utils.PatternKit;
import com.zxh.myBlog.utils.TaleUtils;

@Controller
public class IndexController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	
	@Resource
	private IContentService contentService;
	
	@Resource
	private ICommentService commentService;
	
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
        completeArticle(request, content);
        if (!checkHitsFrequency(request, cid)) {
            updateArticleHit(content.getCid(), content.getHits());
        }
        return this.render("post");
	}
	

	/**
     * 文章页(预览)
     *
     * @param request 请求
     * @param cid     文章主键
     * @return
     */
    @GetMapping(value = {"article/{cid}/preview", "article/{cid}.html"})
    public String articlePreview(HttpServletRequest request, @PathVariable String cid) {
        ContentVo contents = contentService.getContents(cid);
        if (null == contents) {
            return this.render_404();
        }
        request.setAttribute("article", contents);
        request.setAttribute("is_post", true);
        completeArticle(request, contents);
        if (!checkHitsFrequency(request, cid)) {
            updateArticleHit(contents.getCid(), contents.getHits());
        }
        return this.render("post");


    }

	private void completeArticle(HttpServletRequest request, ContentVo contents) {
		// TODO Auto-generated method stub
		if(contents.getAllowComment()) {
			String cp = request.getParameter("cp");
			if (StringUtils.isBlank(cp)) {
                cp = "1";
			}
			request.setAttribute("cp", cp);
            PageInfo<CommentBo> commentsPaginator = commentService.getComments(contents.getCid(), Integer.parseInt(cp), 6);
            request.setAttribute("comments", commentsPaginator);
		}
	}
	
	private void updateArticleHit(Integer cid, Integer chits) {
		// TODO Auto-generated method stub
		Integer hits = cache.hget("article" + cid, "hits");
        if (chits == null) {
            chits = 0;
        }
        hits = null == hits ? 1 : hits + 1;
        if (hits >= WebConst.HIT_EXCEED) {
            ContentVo temp = new ContentVo();
            temp.setCid(cid);
            temp.setHits(chits + hits);
            contentService.updateContentByCid(temp);
            cache.hset("article" + cid, "hits", 1);
        } else {
            cache.hset("article" + cid, "hits", hits);
        }
	}

	private boolean checkHitsFrequency(HttpServletRequest request, String cid) {
		// TODO Auto-generated method stub
		String val = IPKit.getIpAddrByRequest(request) + ":" + cid;
        Integer count = cache.hget(Types.HITS_FREQUENCY.getType(), val);
        if (null != count && count > 0) {
            return true;
        }
        cache.hset(Types.HITS_FREQUENCY.getType(), val, 1, WebConst.HITS_LIMIT_TIME);
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
	
	@PostMapping(value = "comment")
    @ResponseBody
    public RestResponseBo comment(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam Integer cid, @RequestParam Integer coid,
                                  @RequestParam String author, @RequestParam String mail,
                                  @RequestParam String url, @RequestParam String text, @RequestParam String _csrf_token) {

        String ref = request.getHeader("Referer");
        if (StringUtils.isBlank(ref) || StringUtils.isBlank(_csrf_token)) {
            return RestResponseBo.fail("BAD REQUEST");
        }

        Integer token = cache.hget(Types.CSRF_TOKEN.getType(), _csrf_token);
        if (token == null) {
            return RestResponseBo.fail("BAD REQUEST");
        }

        if (null == cid || StringUtils.isBlank(text)) {
            return RestResponseBo.fail("请输入完整后评论");
        }

        if (StringUtils.isNotBlank(author) && author.length() > 50) {
            return RestResponseBo.fail("姓名过长");
        }

        if (StringUtils.isNotBlank(mail) && !TaleUtils.isEmail(mail)) {
            return RestResponseBo.fail("请输入正确的邮箱格式");
        }

        if (StringUtils.isNotBlank(url) && !PatternKit.isURL(url)) {
            return RestResponseBo.fail("请输入正确的URL格式");
        }

        if (text.length() > 200) {
            return RestResponseBo.fail("请输入200个字符以内的评论");
        }

        String val = IPKit.getIpAddrByRequest(request) + ":" + cid;
        Integer count = cache.hget(Types.COMMENTS_FREQUENCY.getType(), val);
        if (null != count && count > 0) {
            return RestResponseBo.fail("您发表评论太快了，请过会再试");
        }

        author = TaleUtils.cleanXSS(author);
        text = TaleUtils.cleanXSS(text);

        author = EmojiParser.parseToAliases(author);
        text = EmojiParser.parseToAliases(text);

        CommentVo comments = new CommentVo();
        comments.setAuthor(author);
        comments.setCid(cid);
        comments.setIp(request.getRemoteAddr());
        comments.setUrl(url);
        comments.setContent(text);
        comments.setMail(mail);
        comments.setParent(coid);
        try {
            String result = commentService.insertComments(comments);
            cookie("tale_remember_author", URLEncoder.encode(author, "UTF-8"), 7 * 24 * 60 * 60, response);
            cookie("tale_remember_mail", URLEncoder.encode(mail, "UTF-8"), 7 * 24 * 60 * 60, response);
            if (StringUtils.isNotBlank(url)) {
                cookie("tale_remember_url", URLEncoder.encode(url, "UTF-8"), 7 * 24 * 60 * 60, response);
            }
            // 设置对每个文章1分钟可以评论一次
            cache.hset(Types.COMMENTS_FREQUENCY.getType(), val, 1, 60);
            if (!WebConst.SUCCESSFUL_RESULT.equals(result)) {
                return RestResponseBo.fail(result);
            }
            return RestResponseBo.ok();
        } catch (Exception e) {
            String msg = "评论发布失败";
            LOGGER.error(msg, e);
            return RestResponseBo.fail(msg);
        }
    }

	private void cookie(String name, String value, int i, HttpServletResponse response) {
		// TODO Auto-generated method stub
		Cookie cookie = new Cookie(name, value); 
		cookie.setMaxAge(i);
        cookie.setSecure(false);
        response.addCookie(cookie);
	}
	
	/**
     * 归档页
     *
     * @return
     */
    @GetMapping(value = "archives")
    public String archives(HttpServletRequest request) {
        List<ArchiveBo> archives = siteService.getArchives();
        request.setAttribute("archives", archives);
        return this.render("archives");
    }
    
    @PostMapping(value = "search/{keyword}")
    public String search(HttpServletRequest request, @PathVariable String keyword, @RequestParam(value = "limit", defaultValue = "12") int limit) {
    	return this.search(request, keyword, 1, limit);
    }
    
    @GetMapping(value = "search/{keyword}/{page}")
    public String search(HttpServletRequest request, @PathVariable String keyword, @PathVariable int page, @RequestParam(value = "limit", defaultValue = "12") int limit) {
    	page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;
    	PageInfo<ContentVo> articles = contentService.getArticles(keyword, page, limit);
        request.setAttribute("articles", articles);
        request.setAttribute("type", "搜索");
        request.setAttribute("keyword", keyword);
        return this.render("page-category");
    }
    /**
     * 标签页
     *
     * @param name
     * @return
     */
    @GetMapping(value = "tag/{name}")
    public String tags(HttpServletRequest request, @PathVariable String name, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.tags(request, name, 1, limit);
    }

    /**
     * 标签分页
     *
     * @param request
     * @param name
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "tag/{name}/{page}")
    public String tags(HttpServletRequest request, @PathVariable String name, @PathVariable int page, @RequestParam(value = "limit", defaultValue = "12") int limit) {

        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;
//        对于空格的特殊处理
        name = name.replaceAll("\\+", " ");
        MetaDto metaDto = metaService.getMeta(Types.TAG.getType(), name);
        if (null == metaDto) {
            return this.render_404();
        }

        PageInfo<ContentVo> contentsPaginator = contentService.getArticles(metaDto.getMid(), page, limit);
        request.setAttribute("articles", contentsPaginator);
        request.setAttribute("meta", metaDto);
        request.setAttribute("type", "标签");
        request.setAttribute("keyword", name);

        return this.render("page-category");
    }
    
    /**
     * 分类页
     *
     * @return
     */
    @GetMapping(value = "category/{keyword}")
    public String categories(HttpServletRequest request, @PathVariable String keyword, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.categories(request, keyword, 1, limit);
    }

    @GetMapping(value = "category/{keyword}/{page}")
    public String categories(HttpServletRequest request, @PathVariable String keyword,
                             @PathVariable int page, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;
        MetaDto metaDto = metaService.getMeta(Types.CATEGORY.getType(), keyword);
        if (null == metaDto) {
            return this.render_404();
        }

        PageInfo<ContentVo> contentsPaginator = contentService.getArticles(metaDto.getMid(), page, limit);

        request.setAttribute("articles", contentsPaginator);
        request.setAttribute("meta", metaDto);
        request.setAttribute("type", "分类");
        request.setAttribute("keyword", keyword);

        return this.render("page-category");
    }

}
