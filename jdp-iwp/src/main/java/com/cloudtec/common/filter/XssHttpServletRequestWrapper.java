package com.cloudtec.common.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.cloudtec.common.utils.CheckUtil;
import com.cloudtec.common.utils.StringUtils;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	HttpServletRequest orgRequest = null;

	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		orgRequest = request;
	}

	/**
	* 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
	* 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
	* getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
	*/
	@Override
	public String getParameter(String name) {
		String value = super.getParameter(CheckUtil.replaceInvaidStr(name));
		if (!StringUtils.isEmpty(value)) {
			value = CheckUtil.replaceInvaidStr(value);
		}
		return value;
	}

	/**
	* 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
	* 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
	* getHeaderNames 也可能需要覆盖
	*/
	@Override
	public String getHeader(String name) {

		String value = super.getHeader(CheckUtil.replaceInvaidStr(name));
		if (!StringUtils.isEmpty(value)) {
			value = CheckUtil.replaceInvaidStr(value);
		}
		return value;
	}
	/**
	* 获取最原始的request
	*
	* @return
	*/
	public HttpServletRequest getOrgRequest() {
		return orgRequest;
	}
	/**
	* 获取最原始的request的静态方法
	*
	* @return
	*/
	public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
		if(req instanceof XssHttpServletRequestWrapper){
			return ((XssHttpServletRequestWrapper)req).getOrgRequest();
		}
		return req;
	}
}
