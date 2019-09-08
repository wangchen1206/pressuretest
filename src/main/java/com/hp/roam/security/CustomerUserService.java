package com.hp.roam.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.hp.roam.dao.UserDao;
import com.hp.roam.model.SysUser;
import com.hp.roam.util.HttpUtils;

/**
 * @author ck
 * @date 2019年2月27日 下午4:32:43
 */
public class CustomerUserService implements UserDetailsService{

	@Autowired
	UserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		SysUser user = userDao.findByUserName(username);
		if(user == null){
			throw new UsernameNotFoundException("用户名不存在");
		}else{
			HttpUtils.logger.info("login user is "+user);
		}
		//当前用户实现了 UserDetails接口，可直接返回个Spring Security 使用
		return user;
	}

}
