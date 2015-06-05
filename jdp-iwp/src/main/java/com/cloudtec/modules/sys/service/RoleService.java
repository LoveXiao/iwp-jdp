/**
 * @Title: RoleService.java
 * @Package com.lovexiao.jmp.modules.sys.services
 * @Description: TODO
 * Copyright: Copyright (c) 2014-2017 
 * @author Comsys-wangqi01
 * @date 2014-8-11 上午10:57:31
 * @version V1.0
 */

package com.cloudtec.modules.sys.service;

import java.util.List;


import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cloudtec.common.service.BaseService;
import com.cloudtec.modules.sys.dao.RoleDao;
import com.cloudtec.modules.sys.entity.Role;
import com.cloudtec.modules.sys.entity.User;




/**
 * @ClassName: RoleService
 * @Description: TODO
 * @author wangqi01
 * @date 2014-8-11 上午10:57:31
 */
@Service("roleService")
public class RoleService  extends BaseService {
	
	@Autowired
	@Qualifier("roleDao")
	private RoleDao roleDao;

	/**
	  * @Title: RoleService.findAllRole
	  * @Author wangqi01 2014-8-12
	  * @Description: TODO
	  * @return List<Role>
	 */
	public List<Role> findAllRole() {
		return roleDao.findAll();
	}

	/**
	 * @Title: RoleService.findByRecid
	 * @Author wangqi01 2014-9-15
	 * @Description: TODO
	 * @param recid
	 * @return Role
	 * 
	 */
	public Role findByRecid(String recid) {
		return roleDao.findOne(recid);
	}

	/**
	 * @Title: RoleService.delete
	 * @Author wangqi01 2014-9-16
	 * @Description: TODO
	 * @param recid
	 * @return boolean
	 * 
	 */
	public boolean delete(String recid) {
		try {
			Role role = roleDao.findOne(recid);
			for(User user : role.getUserList()){
				user.getRoleList().remove(role);
			}
			roleDao.delete(recid);
		} catch (Exception e) {
			logger.error("根据角色ID '"+recid+"' 删除角色，失败。\n"+e.getMessage());
			throw new ServiceException("根据角色ID '"+recid+"' 删除角色，失败。");
		}
		return true;
	}

	/**
	 * @Title: RoleService.save
	 * @Author wangqi01 2014-9-16
	 * @Description: TODO
	 * @param role void
	 * 
	 */
	public void save(Role role) {
		roleDao.save(role);
	}

	/**
	 * @Title: RoleService.findByName
	 * @Author wangqi01 2014-9-22
	 * @Description: TODO
	 * @param roleName
	 * @return Object
	 * 
	 */
	public Role findByName(String roleName) {
		return roleDao.findByName(roleName);
		
	}
}
