package com.help.shiro.server.service;

import com.help.shiro.server.dao.PermissionDao;
import com.help.shiro.server.dao.RoleDao;
import com.help.shiro.server.dao.UserDao;
import com.help.shiro.server.domain.Permission;
import com.help.shiro.server.domain.Role;
import com.help.shiro.server.page.Pagination;
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
import java.util.HashMap;
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


}
