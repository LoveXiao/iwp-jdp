/**
 * Project Name:jdp-iwp 
 * File Name:OrganService.java 
 * Package Name:com.cloudtec.modules.sys.service 
 * Date:2014-8-19下午3:16:48 
 * Copyright &copy; 2013-2014 <a href="http://www.svnchina.com/svn/iwp">iwp</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.cloudtec.modules.sys.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cloudtec.common.service.BaseService;
import com.cloudtec.modules.sys.dao.OrganDao;
import com.cloudtec.modules.sys.entity.Organ;
import com.cloudtec.modules.sys.entity.User;
import com.cloudtec.modules.sys.utils.PageBuildUtils;




/** 
 * @ClassName: OrganService <br/> 
 * @Description: TODO <br/> 
 * @date: 2014-8-19 下午3:16:48 <br/> 
 * 
 * @author wangqi01 
 * @version  
 * @since JDK 1.6 
 */
@Service("organService")
public class OrganService extends BaseService {

	@Autowired
	@Qualifier(value="organDao")
	private OrganDao organDao;

	/**
	 * @Title: OrganService.findAll
	 * @Author wangqi01 2014-8-19
	 * @Description: TODO
	 * @return List<Organ>
	 * 
	 */
	public List<Organ> findAll() {
		return organDao.findAll();
	}

	/**
	 * @Title: OrganService.findOrgans
	 * @Author wangqi01 2014-9-18
	 * @Description: TODO
	 * @param searchMap
	 * @param pageNumber
	 * @param pageSize
	 * @param object
	 * @return Page<Organ>
	 * 
	 */
	public Page<Organ> findOrgans(Map<String, Object> searchParams,
			int pageNumber, int pageSize, String sortType) {
		PageBuildUtils<Organ> pageUtils = new PageBuildUtils<Organ>();
		PageRequest pageRequest = pageUtils.buildPageRequest(pageNumber, pageSize, sortType);
		return organDao.findAll(pageUtils.buildSpecification(searchParams),pageRequest);//spec,
	}
}
