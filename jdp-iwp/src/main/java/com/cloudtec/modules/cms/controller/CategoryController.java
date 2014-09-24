/**
 * Project Name:jdp-iwp 
 * File Name:CategoryController.java 
 * Package Name:com.cloudtec.modules.cms.controller 
 * Date:2014-9-23上午11:35:40 
 * Copyright &copy; 2013-2014 <a href="http://www.svnchina.com/svn/iwp">iwp</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.cloudtec.modules.cms.controller;


import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudtec.common.controller.BaseController;
import com.cloudtec.common.utils.StringUtils;
import com.cloudtec.modules.cms.entity.Category;
import com.cloudtec.modules.cms.service.CategoryService;

/** 
 * @ClassName: CategoryController <br/> 
 * @Description: TODO <br/> 
 * @date: 2014-9-23 上午11:35:40 <br/> 
 * 
 * @author wangqi01 
 * @version  
 * @since JDK 1.6 
 */

@Controller
@RequestMapping(value="${adminPath}/cms/category")
public class CategoryController extends BaseController {
	
	
	
	@Autowired
	@Qualifier("categoryService")
	private CategoryService categoryService;
	
	@RequiresPermissions("cms:category:view")
	@RequestMapping(value={"list",""})
	public String treeCategory(Category category,Model model){
		//按分类名称，按栏目名称，栏目模型，关键字，描述查询
		categoryService.findAll(category);
		return "/modules/cms/categoryList";
	}
	/**
	 * @Title: CategoryController.form
	 * @Author wangqi01 2014-9-23
	 * @Description: TODO 跳转到明细列表
	 * @param category
	 * @param model
	 * @param request
	 * @return String
	 */
	@RequiresPermissions("cms:category:view")
	@RequestMapping(value="form")
	public String form(Category category,Model model){
		if(StringUtils.isNotBlank(category.getRecid())){
			category = categoryService.findByRecid(category.getRecid());
		}else{
			if(category.getParent() == null || StringUtils.isBlank(category.getParent().getRecid())){
				category.setParent(new Category("1"));
			}
			category.setParent(categoryService.findByRecid(category.getParent().getRecid()));
		}
		model.addAttribute("category", category);
		return "/modules/cms/categoryForm";
	}
	/**
	 * @Title: CategoryController.save
	 * @Author wangqi01 2014-9-23
	 * @Description: TODO 保存新增分类
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return String
	 */
	@RequestMapping(value="/save")
	public String save(Category category ,Model model,RedirectAttributes redirectAttributes){
		return "";
	}
}
