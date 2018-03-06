package com.help.shiro.server.service;

import com.help.shiro.server.dao.PermissionDao;
import com.help.shiro.server.dao.RoleDao;
import com.help.shiro.server.dao.UserDao;
import com.help.shiro.server.domain.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;



@Service
public class PermissionService {

	@Autowired
	UserDao userDao;
	@Autowired
	PermissionDao permissionDao;
	@Autowired
	RoleDao roleDao;

	

	public void deleteById(Integer id) {
		 permissionDao.delete(id);
	}


	public Permission save(Permission record) {
		permissionDao.save(record);
		return record;
	}

	public Permission update(Permission record) {
		return permissionDao.save(record);
	}


	@SuppressWarnings("unchecked")
	public Page<Permission> findPage(Integer pageNo, Integer pageSize) {
		Pageable pageable = new PageRequest(pageNo, pageSize, Sort.Direction.ASC, "id");
		Page<Permission> page = permissionDao.findAll(pageable);
		return page;
	}

	
}
