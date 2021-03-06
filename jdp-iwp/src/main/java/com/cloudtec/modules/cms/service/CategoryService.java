/**
 * Project Name:jdp-iwp 
 * File Name:CategoryService.java 
 * Package Name:com.cloudtec.modules.cms.service 
 * Date:2014-9-23下午2:20:17 
 * Copyright &copy; 2013-2014 <a href="http://www.svnchina.com/svn/iwp">iwp</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.cloudtec.modules.cms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import com.cloudtec.common.service.BaseService;
import com.cloudtec.modules.cms.dao.CategoryDao;
import com.cloudtec.modules.cms.entity.Category;

/** 
 * @ClassName: CategoryService <br/> 
 * @Description: TODO <br/> 
 * @date: 2014-9-23 下午2:20:17 <br/> 
 * 
 * @author wangqi01 
 * @version  
 * @since JDK 1.6 
 */
@Service("categoryService")
public class CategoryService extends BaseService {

	@Autowired
	@Qualifier("categoryDao")
	private CategoryDao categoryDao;

	/**
	 * @Title: CategoryService.findAll
	 * @Author wangqi01 2014-9-23
	 * @Description: TODO
	 */
	public List<Category> findAll() {
		/**
		 * 根据当前用户，查询当前单位的分类。
		 * 目前没有根据单位区分，后期增加单位Id字段。
		 */
//		PageBuildUtils<Category> pageBuildUtils = new PageBuildUtils<Category>();
//		categoryDao.findAll(pageBuildUtils.buildSpecification(category));
		return categoryDao.findAll();
	}

	/**
	 * @Title: CategoryService.findByRecid
	 * @Author wangqi01 2014-9-24
	 * @Description: TODO 根据ID,查询分类信息
	 * @param recid
	 * @return Category
	 * 
	 */
	public Category findByRecid(String recid) {
		return categoryDao.findByRecid(recid);
	}

	/**
	 * @Title: CategoryService.save
	 * @Author wangqi01 2014-10-13
	 * @Description: TODO
	 * @param category
	 * @return boolean
	 * 
	 */
	public boolean save(Category category) {
		try{
			categoryDao.save(category);
		}catch(Exception e){
			logger.error(getCurrentUser().getUsername()+"保存栏目失败。",e);
			return false;
		}
		return true;
	}
}
