package com.cloudtec.modules.stay.controller;

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
import com.cloudtec.modules.stay.entity.Hotel;
import com.cloudtec.modules.stay.service.HotelService;

@Controller
@RequestMapping("${adminPath}/stay/hotel")
public class HotelController extends BaseController {

	@Autowired
	@Qualifier("hotelService")
	private HotelService hotelService;
	@RequiresPermissions("stay:hotel:view")
	@RequestMapping(value = {"list",""})
	public String list(HttpServletRequest request, HttpServletResponse response, Model model,Hotel hotel){
		
		Page<Hotel> hotelPage = hotelService.findHotels(hotel,hotel.getPageNo(),hotel.getPageSize(),null);
		model.addAttribute("hotels", hotelPage);
		model.addAttribute("hotel", hotel);
		return "modules/stay/hotelList";
	}
	
	@RequiresPermissions("stay:hotel:view")
	@RequestMapping(value = "form")
	public String  form(Hotel hotel ,Model model) {
		if(!StringUtils.isEmpty(hotel.getRecid())){
			hotel = hotelService.getHotelByRrecid(hotel.getRecid());
		}
		model.addAttribute("hotel",hotel);
		return "modules/stay/hotelForm";
	}
	@RequiresPermissions("stay:hotel:edit")
	@RequestMapping(value="save")
	public String  save(Hotel hotel,Model model,RedirectAttributes redirectAttributes) {
		
		Boolean resultBoolean = hotelService.saveHotel(hotel);
		if(!resultBoolean){
			addMessage(model, "保存酒店"+hotel.getName()+"信息失败!");
			return form(hotel, model);
		}else{
			addMessage(redirectAttributes, "保存酒店"+hotel.getName()+"信息成功。");
		}
		return "redirect:"+Global.getAdminPath()+"/stay/hotel/?repage";
	}
}
