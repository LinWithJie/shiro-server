package com.help.shiro.server.service;

import com.help.shiro.server.dao.PermissionDao;
import com.help.shiro.server.dao.RoleDao;
import com.help.shiro.server.dao.UserDao;
import com.help.shiro.server.domain.Permission;
import com.help.shiro.server.domain.Role;
import com.help.shiro.server.domain.User;
import com.help.shiro.server.page.Pagination;
import com.help.shiro.server.vo.RolePermissionAllocation;
import com.help.shiro.server.vo.RolePermissionMark;
import com.help.shiro.server.vo.UserRoleAllocation;
import com.help.shiro.server.vo.UserRoleMark;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class PermissionService {

	@Autowired
	UserDao userDao;
	@Autowired
	PermissionDao permissionDao;
	@Autowired
	RoleDao roleDao;

	




	public Permission save(Permission record) {
		permissionDao.save(record);
		return record;
	}

	public Permission update(Permission record) {
		return permissionDao.save(record);
	}


	/**
	 * 分页查询
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Pagination<Permission> findPage(Integer pageNo, Integer pageSize) {
		pageNo = (null == pageNo ? 1 : pageNo);
		pageSize = (null == pageSize ? 10 : pageSize);
		Pageable pageable = new PageRequest(pageNo-1, pageSize, Sort.Direction.ASC, "id");
		Page page1 = permissionDao.findAll(pageable);
        Pagination page = new Pagination(pageNo,pageSize, (int) permissionDao.count(),page1.getContent());
		return page;
	}

	/**
	 * 根据角色名模糊搜索
	 * @param name
	 * @param page
	 * @param count
	 * @return
	 */
	public Pagination<Permission> findAllByLikeName(final String name, int page, int count) {
		Specification<Permission> specification = new Specification<Permission>() {
			@Override
			public Predicate toPredicate(Root<Permission> root,
										 CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Path<String> _name = root.get("permission");
				Predicate _key = criteriaBuilder.like(_name, "%" + name + "%");
				return criteriaBuilder.and(_key);
			}
		};
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = new PageRequest(page - 1, count, sort);
		Page page1 = permissionDao.findAll(specification, pageable);
		Pagination<Permission> pagination = new Pagination(page,count,page1.getSize(),page1.getContent());
		return pagination;
	}

    public Map<String, Object> deletePermissionById(String ids) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        try {
            int count=0;
            String resultMsg = "删除成功。";
            String[] idArray = new String[]{};
            if(StringUtils.contains(ids, ",")){
                idArray = ids.split(",");
            }else{
                idArray = new String[]{ids};
            }

            c:for (String idx : idArray) {
                if(new Integer(1).equals(idx)){
                    resultMsg = "操作成功，But'系统管理员不能删除。";
                    continue c;
                }else{
                    count+=this.deleteByPrimaryKey(idx);
                }
            }
            resultMap.put("status", 200);
            resultMap.put("count", count);
            resultMap.put("resultMsg", resultMsg);
        } catch (Exception e) {
            log.info("根据IDS删除用户出现错误，ids：{},错误：{}", ids,e);
            resultMap.put("status", 500);
            resultMap.put("message", "删除出现错误，请刷新后再试！");
        }
        return resultMap;
    }

    private int deleteByPrimaryKey(String idx) {
        permissionDao.delete(Integer.parseInt(idx));
        return 1;
    }

    public List<RolePermissionMark> selectPermissionsByRoleId(String id) {
        List<Role> roles = new ArrayList<>();
        roles.add(roleDao.findOne(Integer.parseInt(id)));
        List<Permission> permissions = permissionDao.findByRoles(roles);
        List<RolePermissionMark> rolePermissionMarks = new ArrayList<>();
        permissions.forEach(permission -> {
            RolePermissionMark rolePermissionMark = new RolePermissionMark();
            BeanUtils.copyProperties(permission, rolePermissionMark);
            rolePermissionMark.setRoleId(id);
            rolePermissionMark.setMarker(id);
            rolePermissionMarks.add(rolePermissionMark);
        });
        List<Permission> allPermissions = permissionDao.findAll();
        allPermissions.removeAll(permissions);
        allPermissions.forEach(permission -> {
            RolePermissionMark rolePermissionMark = new RolePermissionMark();
            BeanUtils.copyProperties(permission, rolePermissionMark);
            rolePermissionMark.setRoleId(id + "noEquals");
            rolePermissionMark.setMarker(id);
            rolePermissionMarks.add(rolePermissionMark);
        });
        return rolePermissionMarks;
    }
    /**
     * 处理权限
     * @param roleId
     * @param ids
     * @return
     */
    public Map<String,Object> addPermission2Role(Integer roleId, String ids) {
        deleteByRoleId(roleId);
        Map<String,Object> resultMap = new HashMap<String, Object>();
        int count = 0;
        try {
            //如果ids,permission 的id 有值，那么就添加。没值象征着：把这个角色（roleId）所有权限取消。
            if(StringUtils.isNotBlank(ids)){
                String[] idArray = null;

                //这里有的人习惯，直接ids.split(",") 都可以，我习惯这么写。清楚明了。
                if(StringUtils.contains(ids, ",")){
                    idArray = ids.split(",");
                }else{
                    idArray = new String[]{ids};
                }
                //添加新的。
                Role role = roleDao.findOne(roleId);
                for (String pid : idArray) {
                    //这里严谨点可以判断，也可以不判断。这个{@link StringUtils 我是重写了的}
                    if(StringUtils.isNotBlank(pid)){
                        Permission permission = permissionDao.findOne(Integer.valueOf(pid));
                        role.getPermissions().add(permission);
                        count += 1;
                    }
                }
                roleDao.save(role);
            }
            resultMap.put("status", 200);
            resultMap.put("message", "操作成功");
        } catch (Exception e) {
            resultMap.put("status", 200);
            resultMap.put("message", "操作失败，请重试！");
        }
        resultMap.put("count", count);
        return resultMap;
    }

    public Map<String,Object> deleteByRoleId(Integer roleId) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        try {
            Role role = roleDao.findOne(roleId);
            role.setPermissions(new ArrayList<>());
            roleDao.save(role);
            resultMap.put("status", 200);
            resultMap.put("message", "操作成功");
        } catch (Exception e) {
            resultMap.put("status", 200);
            resultMap.put("message", "操作失败，请重试！");
        }
        return resultMap;
    }

    public Map<String,Object> deleteByRoleIds(String roleIds) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        try {
            int count=0;
            String resultMsg = "删除成功。";
            String[] idArray = new String[]{};
            if(StringUtils.contains(roleIds, ",")){
                idArray = roleIds.split(",");
            }else{
                idArray = new String[]{roleIds};
            }
            c:for (String idx : idArray) {
                deleteByRoleId(Integer.valueOf(idx));
                count+=1;
            }
            resultMap.put("status", 200);
            resultMap.put("count", count);
            resultMap.put("resultMsg", resultMsg);
        } catch (Exception e) {
            log.info("根据roleIDS删除权限出现错误，roleIDS：{},错误：{}", roleIds,e);
            resultMap.put("status", 500);
            resultMap.put("message", "删除出现错误，请刷新后再试！");
        }
        return resultMap;
    }
}
