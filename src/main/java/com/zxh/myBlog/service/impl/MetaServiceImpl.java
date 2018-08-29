package com.zxh.myBlog.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zxh.myBlog.dto.MetaDto;
import com.zxh.myBlog.model.Vo.MetaVo;
import com.zxh.myBlog.service.IMetaService;

@Service
public class MetaServiceImpl implements IMetaService{

	@Override
	public MetaDto getMeta(String type, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer countMeta(Integer mid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetaVo> getMetas(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveMetas(Integer cid, String names, String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveMeta(String type, String name, Integer mid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int mid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveMeta(MetaVo metas) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(MetaVo metas) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<MetaDto> getMetaList(String type, String orderby, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

}
