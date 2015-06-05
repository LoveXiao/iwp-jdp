package com.cloudtec.modules.warnInfo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudtec.common.config.Global;
import com.cloudtec.common.controller.BaseController;
import com.cloudtec.common.utils.StringUtils;
import com.cloudtec.modules.sys.utils.DictUtils;
import com.cloudtec.modules.warnInfo.entity.Warn;
import com.cloudtec.modules.warnInfo.service.WarnService;

@Controller
@RequestMapping("${adminPath}/warnInfo/warn")
public class WarnController extends BaseController {
	
	@Autowired
	@Qualifier("warnService")
	private WarnService warnService;
	
	@RequiresPermissions("warninfo:warn:view")
	@RequestMapping(value={"list",""})
	public String list(Warn warn,Model model,HttpServletRequest request, HttpServletResponse response){
		Page<Warn> warnPage = warnService.findWarns(warn,warn.getPageNo(),warn.getPageSize(),null);
		model.addAttribute("warns", warnPage);
		model.addAttribute("warn", warn);
		return "modules/warninfo/warnList";
	}
	@RequiresPermissions("warninfo:warn:view")
	@RequestMapping(value="/form")
	public String form(Warn warn,Model model){
		if(!StringUtils.isBlank(warn.getRecid())){
			warn = warnService.findEntityById(warn.getRecid());
		}
		model.addAttribute("allIssueClient", DictUtils.getDictList(Warn.ISSUEDICTTYPE));
		model.addAttribute("warn",warn);
		return "modules/warninfo/warnForm";
	}
	
	@RequiresPermissions("warninfo:warn:edit")
	@RequestMapping(value="save")
	public String save(Warn warn,Model model,RedirectAttributes redirectAttributes){
		Boolean resultBoolean = warnService.saveWarnInfo(warn);
		if(!resultBoolean){
			addMessage(model, "保存预警信息: "+warn.getTitle()+" 失败!");
			return form(warn, model);
		}else{
			addMessage(redirectAttributes, "保存预警信息: "+warn.getTitle()+" 成功。");
		}
		return "redirect:"+Global.getAdminPath()+"/warnInfo/warn/?repage";
	}
	
}
