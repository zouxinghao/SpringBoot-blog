package com.zxh.myBlog.service;

import java.util.List;

import com.zxh.myBlog.dto.MetaDto;
import com.zxh.myBlog.model.Vo.MetaVo;

/**
 * 分类信息Service接口
 * @author xzou
 *
 */
public interface IMetaService {
	
	/**
	 * Query by name or type
	 * @param type
	 * @param name
	 * @return
	 */
	MetaDto getMeta(String type, String name);
	
	/**
	 * get the number through article id
	 * @param mid
	 * @return
	 */
	Integer countMeta(Integer mid);
	
	/**
	 * query through (return value may be more than one, saved in list)
	 * @param type
	 * @return
	 */
	List<MetaVo> getMetas(String type);
	
	/**
     * save multiple pros
     * @param cid
     * @param names
     * @param type
     */
    void saveMetas(Integer cid, String names, String type);

    /**
     * save project
     * @param type
     * @param name
     * @param mid
     */
    void saveMeta(String type, String name, Integer mid);
    
    /**
     * 删除项目
     * @param mid
     */
    void delete(int mid);

    /**
     * 保存项目
     * @param metas
     */
    void saveMeta(MetaVo metas);

    /**
     * 更新项目
     * @param metas
     */
    void update(MetaVo metas);
    
    List<MetaDto> getMetaList(String type, String orderby, int limit);
}
