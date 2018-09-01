package com.zxh.myBlog.controller.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.zxh.myBlog.constant.WebConst;
import com.zxh.myBlog.controller.BaseController;
import com.zxh.myBlog.dto.LogActions;
import com.zxh.myBlog.dto.Types;
import com.zxh.myBlog.model.Bo.RestResponseBo;
import com.zxh.myBlog.model.Vo.AttachVo;
import com.zxh.myBlog.model.Vo.UserVo;
import com.zxh.myBlog.service.IAttachService;
import com.zxh.myBlog.service.ILogService;
import com.zxh.myBlog.utils.Commons;
import com.zxh.myBlog.utils.TaleUtils;

@Controller
@RequestMapping("admin/attach")
public class AttachController extends BaseController{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AttachController.class);
	
	public static final String CLASSPATH = TaleUtils.getUploadFilePath();
	
	@Resource
	private IAttachService attachService;
	
	@Resource
	private ILogService logService;
	
	@GetMapping(value = "")
	public String index(HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") int page,
						@RequestParam(value = "limit", defaultValue = "12") int limit) {
		PageInfo<AttachVo> attach = attachService.getAttaches(page, limit);
		request.setAttribute("attachs", attach);
		request.setAttribute(Types.ATTACH_URL.getType(), Commons.site_option(Types.ATTACH_URL.getType(), Commons.site_url()));
		request.setAttribute("max_file_size", WebConst.MAX_FILE_SIZE/1024);
		return "admin/attach";
	}
	
	 @PostMapping(value = "upload")
	    @ResponseBody
	    public RestResponseBo upload(HttpServletRequest request, @RequestParam("file") MultipartFile[] multipartFiles) throws IOException {
	        UserVo users = this.user(request);
	        Integer uid = users.getUid();
	        List<String> errorFiles = new ArrayList<>();
	        try {
	            for (MultipartFile multipartFile : multipartFiles) {
	                String fname = multipartFile.getOriginalFilename();
	                if (multipartFile.getSize() <= WebConst.MAX_FILE_SIZE) {
	                    String fkey = TaleUtils.getFileKey(fname);
	                    String ftype = TaleUtils.isImage(multipartFile.getInputStream()) ? Types.IMAGE.getType() : Types.FILE.getType();
	                    File file = new File(CLASSPATH + fkey);
	                    try {
	                        FileCopyUtils.copy(multipartFile.getInputStream(), new FileOutputStream(file));
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                    attachService.save(fname, fkey, ftype, uid);
	                } else {
	                    errorFiles.add(fname);
	                }
	            }
	        } catch (Exception e) {
	            return RestResponseBo.fail();
	        }
	        return RestResponseBo.ok(errorFiles);
	}
	
	@RequestMapping(value = "delet")
	@ResponseBody
	public RestResponseBo delet(@RequestParam Integer id, HttpServletRequest request) {
		try {
			AttachVo attach = attachService.selectById(id);
			if(attach == null) {
				return RestResponseBo.fail("File does not exist!");
			}
			attachService.deleteById(id);
			new File(CLASSPATH + attach.getFkey()).delete();
			logService.insertLog(LogActions.DEL_ARTICLE.getAction(), attach.getFkey(), request.getRemoteAddr(), this.getUid(request));
		} catch(Exception e) {
			LOGGER.error("附件删除失败", e);
			return RestResponseBo.fail("附件删除失败");
		}
		return RestResponseBo.ok();
	}
}
