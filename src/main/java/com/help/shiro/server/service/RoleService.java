package com.help.shiro.server.service;

import com.help.shiro.server.dao.PermissionDao;
import com.help.shiro.server.dao.RoleDao;
import com.help.shiro.server.dao.UserDao;
import com.help.shiro.server.domain.Permission;
import com.help.shiro.server.domain.Role;
import com.help.shiro.server.domain.User;
import com.help.shiro.server.page.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.*;

@Service
@SuppressWarnings("unchecked")
public class RoleService {

	@Autowired
	RoleDao roleDao;
	@Autowired
	UserDao userDao;
	@Autowired
	PermissionDao permissionDao;


	public void deleteById(Integer id) {
		 roleDao.delete(id);
	}


	public Role save(Role record) {
		return roleDao.save(record);
	}



	public Role findById(Integer id) {
		return roleDao.findOne(id);
	}


	public Role update(Role record) {
		return roleDao.save(record);
	}


	/**
	 * 分页查询
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Pagination<Role> findPage(Integer pageNo, Integer pageSize) {
		pageNo = (null == pageNo ? 1 : pageNo);
		pageSize = (null == pageSize ? 10 : pageSize);
		Pagination page = new Pagination();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		Pageable pageable = new PageRequest(pageNo-1, pageSize, Sort.Direction.ASC, "id");
		Page page1 = roleDao.findAll(pageable);
		page.setList(page1.getContent());
		page.setTotalCount(page1.getNumberOfElements());
		return page;
	}

	/**
	 * 根据角色名模糊搜索
	 * @param name
	 * @param page
	 * @param count
	 * @return
	 */
	public Pagination<Role> findAllByLikeName(final String name, int page, int count) {
		Specification<Role> specification = new Specification<Role>() {
			@Override
			public Predicate toPredicate(Root<Role> root,
										 CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Path<String> _name = root.get("role");
				Predicate _key = criteriaBuilder.like(_name, "%" + name + "%");
				return criteriaBuilder.and(_key);
			}
		};
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = new PageRequest(page - 1, count, sort);
		Page page1 = roleDao.findAll(specification, pageable);
		Pagination<Role> pagination = new Pagination();
		pagination.setPageNo(page);
		pagination.setPageSize(count);
		pagination.setTotalCount(page1.getNumberOfElements());
		pagination.setList(page1.getContent());
		return pagination;
	}

	public List<Map<String, Object>> findRolesByUserId(String userId) {
		List<User> users = new ArrayList<>();
		users.add(userDao.findOne(userId));
		List<Role> roles = roleDao.findByUserInfos(users);
		return toTreeData(roles);
	}

	public static List<Map<String,Object>> toTreeData(List<Role> roles){
		List<Map<String,Object>> resultData = new LinkedList<Map<String,Object>>();
		for (Role u : roles) {
			//角色列表
			Map<String,Object> map = new LinkedHashMap<String, Object>();
			map.put("text", u.getDescription());//名称
			map.put("href", "javascript:void(0)");//链接
			List<Permission> ps = u.getPermissions();
			map.put("tags",  new Integer[]{ps.size()});//显示子数据条数
			if(null != ps && ps.size() > 0){
				List<Map<String,Object>> list = new LinkedList<Map<String,Object>>();
				//权限列表
				for (Permission up : ps) {
					Map<String,Object> mapx = new LinkedHashMap<String, Object>();
					mapx.put("text", up.getName());//权限名称
					mapx.put("href", up.getUrl());//权限url
					//mapx.put("tags", "0");//没有下一级
					list.add(mapx);
				}
				map.put("nodes", list);
			}
			resultData.add(map);
		}
		return resultData;

	}

	
}
