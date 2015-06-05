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

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cloudtec.common.service.BaseService;
import com.cloudtec.modules.sys.dao.OrganDao;
import com.cloudtec.modules.sys.entity.Organ;
import com.cloudtec.modules.sys.entity.User;
import com.cloudtec.modules.sys.result.ServiceResult;
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
	
	@Autowired
	@Qualifier(value="userService")
	private UserService userService;

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

	/**
	 * @Title: OrganService.findByRecid
	 * @Author wangqi01 2014-9-29
	 * @Description: TODO
	 * @param recid
	 * @return Organ
	 * 
	 */
	public Organ findByRecid(String recid) {
		return organDao.findByRecid(recid);
	}

	/**
	 * @Title: OrganService.findByName
	 * @Author wangqi01 2014-9-30
	 * @Description: TODO
	 * @param organname
	 * @return Organ
	 */
	public Organ findByName(String organname) {
		return organDao.findByName(organname);
	}

	/**
	 * @Title: OrganService.save
	 * @Author wangqi01 2014-9-30
	 * @Description: TODO
	 * @param organ
	 * @return boolean
	 * 
	 */
	public boolean save(Organ organ) {
		try{
			organDao.saveAndFlush(organ);
		}catch(Exception e){
			logger.error("保存单位信息失败。"+e.getMessage());
			throw new ServiceException("保存单位信息失败。");
		}
		return true;
	}

	public ServiceResult delete(String recid) {
		ServiceResult result = new ServiceResult();
		try{
				Organ organ = organDao.findByRecid(recid);
				if(organ !=null){
					for(Organ organchild :  organ.getChildList()){
						organchild.setParent(organ.getParent());
					}
					for(User user :  organ.getUserList()){
						userService.delete(user.getRecid());
					}
					organDao.delete(recid);
					result.setSuccess(true);
					result.setMessage("单位【"+organ.getName()+"】删除成功!");
					return result;
				}
				result.setMessage("要删除的单位不存在!");
			}catch(Exception e){
				logger.error("删除单位信息失败。"+e.getMessage());
				result.setMessage("删除单位异常。");
			}
			return result;
		
	}
}
