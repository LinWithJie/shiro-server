package com.help.shiro.server.service;

import com.help.shiro.server.dao.PermissionDao;
import com.help.shiro.server.dao.RoleDao;
import com.help.shiro.server.dao.UserDao;
import com.help.shiro.server.domain.Permission;
import com.help.shiro.server.domain.Role;
import com.help.shiro.server.domain.User;
import com.help.shiro.server.page.Pagination;
import com.help.shiro.server.shiro.RedisSessionDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@Slf4j
@SuppressWarnings("unchecked")
public class RoleService {

	@Autowired
	RoleDao roleDao;
	@Autowired
	UserDao userDao;
	@Autowired
	PermissionDao permissionDao;
	@Autowired
    RedisSessionDao redisSessionDao;



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
				Path<String> _name = root.get("templates/role");
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

	public List<Map<String,Object>> toTreeData(List<Role> roles){
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

	public Map<String, Object> deleteRoleById(String ids) {
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

	private int deleteByPrimaryKey(String id) {
		roleDao.delete(Integer.parseInt(id));
		return 1;
	}

    public List<Role> selectRoleByUserId(String id) {
	    List<User> users = new ArrayList<>();
	    users.add(userDao.findOne(id));
	    return roleDao.findByUserInfos(users);
    }

    public Map<String,Object> addRole2User(String userId, String ids) {
	    User user = userDao.findOne(userId);
        Map<String,Object> resultMap = new HashMap<String, Object>();
        int count = 0;
        try {
            //先删除原有的。
            //userRoleMapper.deleteByUserId(userId);
            //如果ids,role 的id 有值，那么就添加。没值象征着：把这个用户（userId）所有角色取消。
            if(StringUtils.isNotBlank(ids)){
                String[] idArray = null;

                //这里有的人习惯，直接ids.split(",") 都可以，我习惯这么写。清楚明了。
                if(StringUtils.contains(ids, ",")){
                    idArray = ids.split(",");
                }else{
                    idArray = new String[]{ids};
                }
                //添加新的。
                for (String rid : idArray) {
                    //这里严谨点可以判断，也可以不判断。这个{@link StringUtils 我是重写了的}
                    if(StringUtils.isNotBlank(rid)){
                        user.getRoleList().add(roleDao.findOne(Integer.valueOf(rid)));
                        count += 1;
                    }
                }
                userDao.save(user);
            }
            resultMap.put("status", 200);
            resultMap.put("message", "操作成功");
        } catch (Exception e) {
            resultMap.put("status", 200);
            resultMap.put("message", "操作失败，请重试！");
        }
        //清空用户的权限，迫使再次获取权限的时候，得重新加载
        //redisSessionDao.delete().clearUserAuthByUserId(userId);
        resultMap.put("count", count);
        return resultMap;
    }

    public Map<String,Object> deleteRoleByUserIds(String userIds) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        try {
            resultMap.put("userIds", userIds);
            if(StringUtils.isNotBlank(userIds)){
                String[] idArray = null;

                //这里有的人习惯，直接ids.split(",") 都可以，我习惯这么写。清楚明了。
                if(StringUtils.contains(userIds, ",")){
                    idArray = userIds.split(",");
                }else{
                    idArray = new String[]{userIds};
                }
                //一个一个删除
                for (String uid : idArray) {
                    //这里严谨点可以判断，也可以不判断。这个{@link StringUtils 我是重写了的}
                    if(StringUtils.isNotBlank(uid)){
                        User user = userDao.findOne(uid);
                        user.setRoleList(null);
                        userDao.save(user);
                    }
                }
            }
            resultMap.put("status", 200);
            resultMap.put("message", "操作成功");
        } catch (Exception e) {
            resultMap.put("status", 200);
            resultMap.put("message", "操作失败，请重试！");
        }
        return resultMap;
    }
}
