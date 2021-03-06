/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.cloudtec.modules.cms.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.cloudtec.common.persistence.IdEntity;
import com.cloudtec.common.utils.search.annontation.SearchField;
import com.cloudtec.common.utils.search.status.Operator;
import com.cloudtec.modules.common.Constants;
import com.google.common.collect.Lists;

/**
 * 栏目Entity
 * @author ThinkGem
 * @version 2013-05-15
 */
@Entity
@Table(name = "RBAC_CMS_CATEGORY")
@DynamicInsert @DynamicUpdate
public class Category extends IdEntity {

    public static final String DEFAULT_TEMPLATE = "frontList";

	private static final long serialVersionUID = 1L;
	private Category parent;// 父级菜单
	private String parentIds;// 所有父级编号
	private String module; 	// 栏目模型（article：文章；picture：图片；download：下载；link：链接；special：专题）
	private String name; 	// 栏目名称
	private String image; 	// 栏目图片
	private String href; 	// 链接
	private String target; 	// 目标（ _blank、_self、_parent、_top）
	private String description; 	// 描述，填写有助于搜索引擎优化
	private String keywords; 	// 关键字，填写有助于搜索引擎优化
	private Integer sort; 		// 排序（升序）
	private String allowComment;// 是否允许评论
	
	private List<Category> childList = Lists.newArrayList(); 	// 拥有子分类列表

	public Category(){
		super();
		this.module = "article";
		this.sort = 30;
		this.allowComment = Constants.NO;
		this.del_flag = Constants.DEL_FLAG_NORMAL;
	}

	public Category(String recid){
		this();
		this.recid = recid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	@SearchField(operator=Operator.LIKE)
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	@SearchField(operator=Operator.LIKE)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	@SearchField(operator=Operator.LIKE)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@SearchField(operator=Operator.LIKE)
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	//@NotNull//@NotNull
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getAllowComment() {
		return allowComment;
	}

	public void setAllowComment(String allowComment) {
		this.allowComment = allowComment;
	}



	@OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE},fetch=FetchType.LAZY,mappedBy="parent")
	@Where(clause="del_flag='"+Constants.DEL_FLAG_NORMAL+"'")
	@OrderBy(value="sort")
	@NotFound(action = NotFoundAction.IGNORE)
	public List<Category> getChildList() {
		return childList;
	}

	public void setChildList(List<Category> childList) {
		this.childList = childList;
	}

	@Transient
	public static void sortList(List<Category> list, List<Category> sourcelist, String parentId){
		for (int i=0; i<sourcelist.size(); i++){
			Category e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getRecid()!=null
					&& e.getParent().getRecid().equals(parentId)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					Category child = sourcelist.get(j);
					if (child.getParent()!=null && child.getParent().getRecid()!=null
							&& child.getParent().getRecid().equals(e.getRecid())){
						sortList(list, sourcelist, e.getRecid());
						break;
					}
				}
			}
		}
	}
	
	@Transient
	public String getIds() {
		return (this.getParentIds() !=null ? this.getParentIds().replaceAll(",", " ") : "") 
				+ (this.getRecid() != null ? this.getRecid() : "");
	}

	@Transient
	public boolean isRoot(){
		return isRoot(this.recid);
	}
	
	@Transient
	public static boolean isRoot(String recid){
		return recid != null && recid.equals("1");
	}

    @Transient
   	public String getUrl() {
        return null;//CmsUtils.getUrlDynamic(this);
   	}
}