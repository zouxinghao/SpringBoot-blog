package com.zxh.myBlog.service.impl;

import java.util.List;

import com.zxh.myBlog.dto.MetaDto;
import com.zxh.myBlog.model.Bo.ArchiveBo;
import com.zxh.myBlog.model.Bo.BackResponseBo;
import com.zxh.myBlog.model.Bo.StatisticsBo;
import com.zxh.myBlog.model.Vo.CommentVo;
import com.zxh.myBlog.model.Vo.ContentVo;
import com.zxh.myBlog.service.ISiteService;

public class SiteServiceImpl implements ISiteService {

	@Override
	public List<CommentVo> recentComments(int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContentVo> recentContents(int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommentVo getComment(Integer coid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BackResponseBo backup(String bk_type, String bk_path, String fmt) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatisticsBo getStatistics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ArchiveBo> getArchives() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetaDto> metas(String type, String orderBy, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

}
