/**
 * Project Name:jdp-iwp 
 * File Name:OrganController.java 
 * Package Name:com.cloudtec.modules.sys.controller 
 * Date:2014-8-19下午3:14:21 
 * Copyright &copy; 2013-2014 <a href="http://www.svnchina.com/svn/iwp">iwp</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.cloudtec.modules.sys.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudtec.common.config.Global;
import com.cloudtec.common.controller.BaseController;
import com.cloudtec.common.utils.StringUtils;
import com.cloudtec.modules.sys.entity.Organ;
import com.cloudtec.modules.sys.result.ServiceResult;
import com.cloudtec.modules.sys.service.OrganService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;




/** 
 * @ClassName: OrganController <br/> 
 * @Description: TODO <br/> 
 * @date: 2014-8-19 下午3:14:21 <br/> 
 * 
 * @author wangqi01 
 * @version  
 * @since JDK 1.6 
 */
@Controller
@RequestMapping(value="${adminPath}/sys/organ")
public class OrganController extends BaseController {
	
	@Autowired
	@Qualifier(value="organService")
	private OrganService organService;
	
	
	/**
	 * 
	 * @Title: OrganController.list
	 * @Author wangqi01 2014-9-18
	 * @Description: TODO 单位列表分页展示和进入单位管理页面
	 * @Permissions shiro sys:organ:view
	 */
	@RequiresPermissions("sys:organ:view")
	@RequestMapping(value={"list",""})
	public String list(HttpServletRequest request,HttpServletResponse response,Model model,Organ organ){
		Map<String, Object> searchMap = Maps.newHashMap();
		searchMap.put("like_name", organ.getName());
		searchMap.put("like_code", organ.getCode());
		Page<Organ> page = organService.findOrgans(searchMap,organ.getPageNo(),organ.getPageSize(),null);
		model.addAttribute("organs", page);
		model.addAttribute("organ", organ);
		return "modules/sys/organList";
	}
	@RequiresPermissions("sys:organ:view")
	@RequestMapping(value="form")
	public String form(Organ organ , Model model){
		//查看
		if(StringUtils.isNotBlank(organ.getRecid())){
			organ = organService.findByRecid(organ.getRecid());
		}
		/*暂时不考虑单位树情况
		 * else {
			//增加下级单位
			if(organ.getParent()== null || StringUtils.isBlank(organ.getParent().getRecid())){
				organ.setParent(new Organ(Constants.RECID_MENU_ROOTID));
			}
			organ.setParent(organService.findByRecid(organ.getParent().getRecid()));
		}*/
		model.addAttribute("organ", organ);
		return "modules/sys/organForm";
	}
	
	@RequestMapping(value="save")
	public String saveOrgan(String oldorganname,  Organ organ,Model model, RedirectAttributes redirectAttributes){
			
		if("false".equals(checkOrganName(organ.getName(), oldorganname))){
			addMessage(model, "保存单位 "+organ.getName()+" 失败，单位名称已存在。");
			return form(organ, model);
		}
		//验证单位代码
		try{
			organService.save(organ);
			addMessage(redirectAttributes, "保存单位"+organ.getName()+"成功！");
		}catch(Exception e){
			addMessage(model, "保存单位"+organ.getName()+"失败！");
			return form(organ, model);
		}
		 return "redirect:"+Global.getAdminPath()+"/sys/organ?repage";
	}
	@RequiresPermissions("sys:organ:edit")
	@RequestMapping(value="delete")
	public String deleteOrgan(String recid, RedirectAttributes redirectAttributes){
		if(StringUtils.isEmpty(recid)){
				addMessage(redirectAttributes, "单位标识不能为空!");
		}else {
				ServiceResult result= organService.delete(recid);
				addMessage(redirectAttributes, result.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/organ/?repage";
	}
	/**
	 * @Title: OrganController.imprtOrgans
	 * @Author wangqi01 2014-10-8
	 * @Description: TODO 未处理，暂时遗留
	 *
	 */
	@RequestMapping(value="import")
	public String imprtOrgans(MultipartFile file,RedirectAttributes redirectAttributes){
		addMessage(redirectAttributes, "单位信息导入成功，共导入");
		return "redirect:"+Global.getAdminPath()+"/sys/organ/?repage";
	}
	
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) Long extId,HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Organ> list = organService.findAll();
		for (int i=0; i<list.size(); i++){
			Organ e = list.get(i);
			if ((extId == null || (extId!=null && !extId.equals(e.getRecid()) && e.getParentIds().indexOf(","+extId+",")==-1))
				){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getRecid());
//				map.put("pId", !user.isAdmin() && e.getId().equals(user.getOffice().getId())?0:e.getParent()!=null?e.getParent().getId():0);
				map.put("pId", e.getParent()!=null?e.getParent().getRecid():0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	@ResponseBody
	@RequiresPermissions("sys:organ:edit")
	@RequestMapping("checkorganname")
	public String checkOrganName(String name,String oldorganname){
		if(name != null && name.equals(oldorganname)){
			return "true";
		}else if(name != null && organService.findByName(name)==null){
			return "true";
		}
		return "false";
	}
}
