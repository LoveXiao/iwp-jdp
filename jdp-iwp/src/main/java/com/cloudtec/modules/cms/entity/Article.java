/**
 * Project Name:jdp-iwp 
 * File Name:Article.java 
 * Package Name:com.cloudtec.modules.cms.entity 
 * Date:2014-9-23上午10:35:44 
 * Copyright &copy; 2013-2014 <a href="http://www.svnchina.com/svn/iwp">iwp</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.cloudtec.modules.cms.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.cloudtec.common.persistence.IdEntity;
import com.google.common.collect.Lists;

/** 
 * @ClassName: Article <br/> 
 * @Description: TODO <br/> 
 * @date: 2014-9-23 上午10:35:44 <br/> 
 * @author wangqi01 
 * @version  
 * @since JDK 1.6 
 */
@Entity
@Table(name="RBAC_CMS_ARTICLE")
@DynamicInsert @DynamicUpdate
public class Article extends IdEntity {
	private static final long serialVersionUID = 1L;
	private Category category;// 分类编号
	private String title;	// 标题
    private String link;	// 外部链接
	private String color;	// 标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）
	private String image;	// 文章图片
	private String keywords;// 关键字
	private String description;// 描述、摘要
	private Integer weight;	// 权重，越大越靠前
	private Date weightDate;// 权重期限，超过期限，将weight设置为0
	private Integer hits;	// 点击数
	private String posid;	// 推荐位，多选（1：首页焦点图；2：栏目页文章推荐；）
    private String customContentView;	// 自定义内容视图
   	private String viewConfig;	// 视图参数
	private ArticleData articleData;	//文章副表
	
	public Article() {
		super();
		this.weight = 0;
		this.hits = 0;
		this.posid = "";
	}

	public Article(String recid){
		this();
		this.recid = recid;
	}
	
	public Article(Category category){
		this();
		this.category = category;
	}

	@PrePersist
	public void prePersist(){
		super.prePersist();
		articleData.setRecid(this.recid);
	}
	
	@ManyToOne
	@JoinColumn(name="category_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
        this.image = image;//CmsUtils.formatImageSrcToDb(image);
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Date getWeightDate() {
		return weightDate;
	}

	public void setWeightDate(Date weightDate) {
		this.weightDate = weightDate;
	}

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	public String getPosid() {
		return posid;
	}

	public void setPosid(String posid) {
		this.posid = posid;
	}

    public String getCustomContentView() {
        return customContentView;
    }

    public void setCustomContentView(String customContentView) {
        this.customContentView = customContentView;
    }

    public String getViewConfig() {
        return viewConfig;
    }

    public void setViewConfig(String viewConfig) {
        this.viewConfig = viewConfig;
    }

	@OneToOne(mappedBy="article",cascade=CascadeType.ALL,optional=false)
	@Valid
	public ArticleData getArticleData() {
		return articleData;
	}

	public void setArticleData(ArticleData articleData) {
		this.articleData = articleData;
	}

	@Transient
	public List<String> getPosidList() {
		List<String> list = Lists.newArrayList();
		if (posid != null){
			for (String s : StringUtils.split(posid, ",")) {
				list.add(s);
			}
		}
		return list;
	}

	@Transient
	public void setPosidList(List<Long> list) {
		posid = ","+StringUtils.join(list, ",")+",";
	}

//    @Transient
//   	public String getUrl() {
//        return CmsUtils.getUrlDynamic(this);
//   	}
//
//    @Transient
//   	public String getImageSrc() {
//        return CmsUtils.formatImageSrcToWeb(this.image);
//   	}
}
