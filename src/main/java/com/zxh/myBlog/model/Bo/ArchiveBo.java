package com.zxh.myBlog.model.Bo;

import java.io.Serializable;
import java.util.List;

import com.zxh.myBlog.model.Vo.ContentVo;

/**
 * 处理归档文章
 * @author xzou
 *
 */
public class ArchiveBo implements Serializable {

	private String date;
	private String count;
	private List<ContentVo> articles;
	
	public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<ContentVo> getArticles() {
        return articles;
    }

    public void setArticles(List<ContentVo> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
    	return "Archive [" +
                "date='" + date + '\'' +
                ", count='" + count + '\'' +
                ", articles=" + articles +
                ']';
    }
	
}
