/**
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.cloudtec.modules.sys.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudtec.common.utils.StringUtils;
import com.cloudtec.modules.sys.entity.Menu;
import com.cloudtec.modules.sys.entity.Role;
import com.cloudtec.modules.sys.entity.User;
import com.cloudtec.modules.sys.service.UserService;
import com.cloudtec.modules.sys.utils.UserUtils;
import com.google.common.collect.Collections2;
import com.google.common.base.Function;
/**
 * 
  * @ClassName: SystemAuthorizingRealm
  * @Description: TODO 系统安全认证实现类
  * @author wangqi01
  * @date 2014-8-7 下午1:29:13
 */
public class SystemAuthorizingRealm extends AuthorizingRealm {

	private UserService userService;
	private Logger logger =LoggerFactory.getLogger(SystemAuthorizingRealm.class);
	/**
	 * 获取系统业务对象
	 */
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 认证回调函数, 登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		CaptchaUsernamePasswordToken token = (CaptchaUsernamePasswordToken) authcToken;
		
		/*if (LoginController.isValidateCodeLogin(token.getUsername(), false, false)){
			// 判断验证码
			Session session = SecurityUtils.getSubject().getSession();
			String code = (String)session.getAttribute(ValidateCodeServlet.VALIDATE_CODE);
			if (token.getCaptcha() == null || !token.getCaptcha().toUpperCase().equals(code)){
				throw new CaptchaException("验证码错误.");
			}
		}*/
		logger.info(token.getUsername()+"登陆，进入 doGetAuthenticationInfo 方法处理。");
		User user = userService.findUserByLoginName(token.getUsername());
		if (user != null) {
		/*	
		 //后台数据库中密码使用密文存储时使用，使用明文存储时注释
		 byte[] salt = Encodes.decodeHex(user.getPassword().substring(0,16));
			return new SimpleAuthenticationInfo(new Principal(user), 
					user.getPassword().substring(16),ByteSource.Util.bytes(salt), getName());
		*/ 
			//密码使用明文存储时使用
			return new SimpleAuthenticationInfo(new Principal(user), 
					user.getPassword(),null, getName());
		} else {
			return null;
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Principal principal = (Principal) getAvailablePrincipal(principals);
		User user = userService.findUserByLoginName(principal.getLoginName());
		if (user != null) {
			UserUtils.putCache("user", user);
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			List<Menu> list = UserUtils.getMenuList();
			for (Menu menu : list){
				if (StringUtils.isNotBlank(menu.getPermissionFlag())){
					// 添加基于Permission的权限信息
					for (String permission : StringUtils.split(menu.getPermissionFlag(),",")){
						info.addStringPermission(permission);
					}
				}
			}
//			// 更新登录IP和时间
//			getSystemService().updateUserLoginInfo(user.getId());
			
			Collection<String> roleList = Collections2.transform(user.getRoleList(), new Function<Role, String>() {
				public String apply(Role role) {
					return role.getName();
				}
			});
			info.addRoles(roleList);
			return info;
		} else {
			return null;
		}
	}
	
	/**
	 * 设定密码校验的Hash算法与迭代次数
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
//		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(SystemService.HASH_ALGORITHM);
//		matcher.setHashIterations(SystemService.HASH_INTERATIONS);
//		setCredentialsMatcher(matcher);
	}
	
	/**
	 * 清空用户关联权限认证，待下次使用时重新加载
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清空所有关联认证
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

	
	
	/**
	 * 授权用户信息
	 */
	public static class Principal implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String id;
		private String loginName; 
		private String name; //别名
		private Map<String, Object> cacheMap;

		public Principal(User user) {
			this.id = user.getRecid();
			this.loginName = user.getUsername();
			this.name = user.getName();
		}

		public String getId() {
			return id;
		}

		public String getLoginName() {
			return loginName;
		}

		public String getName() {
			return name;
		}

		public Map<String, Object> getCacheMap() {
			if (cacheMap==null){
				cacheMap = new HashMap<String, Object>();
			}
			return cacheMap;
		}
		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString(){
			return loginName;
		}
		/**
		 * 重载hashCode,只计算loginName;
		 */
		@Override
		public int hashCode() {
			return loginName.hashCode();
		}

		/**
		 * 重载equals,只计算loginName;
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Principal other = (Principal) obj;
			if (loginName == null) {
				if (other.loginName != null) {
					return false;
				}
			} else if (!loginName.equals(other.loginName)) {
				return false;
			}
			return true;
		}

	}
}
