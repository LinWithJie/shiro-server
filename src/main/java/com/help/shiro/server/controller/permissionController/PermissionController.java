package com.help.shiro.server.controller.permissionController;

import com.help.shiro.server.controller.BaseController;
import com.help.shiro.server.domain.Permission;
import com.help.shiro.server.page.Pagination;
import com.help.shiro.server.service.PermissionService;
import com.help.shiro.server.service.RoleService;
import com.help.shiro.server.vo.RolePermissionAllocation;
import com.help.shiro.server.vo.RolePermissionMark;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/permission")
@Slf4j
public class PermissionController extends BaseController {
	
	@Autowired
	PermissionService permissionService;

	@Autowired
	RoleService roleService;
	/**
	 * 权限列表
	 * @param findContent	查询内容
	 * @param pageNo		页码
	 * @return
	 */
	@RequestMapping(value="/index")
	public ModelAndView index(String findContent, Integer pageNo){
		Pagination page;
		if (!StringUtils.isEmpty(findContent)) {
			page = permissionService.findAllByLikeName(findContent,pageNo==null?getPageNo():pageNo,pageSize);
		}else {
			page = permissionService.findPage(pageNo==null?getPageNo():pageNo,pageSize);
		}
		return new ModelAndView("ftl/permission/index","page",page);
	}
	/**
	 * 权限添加
	 * @param permission
	 * @return
	 */
	@RequestMapping(value="/addPermission",method= RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addPermission(Permission permission){
		try {
			Permission entity = permissionService.save(permission);
			resultMap.put("status", 200);
			resultMap.put("entity", entity);
		} catch (Exception e) {
			resultMap.put("status", 500);
			resultMap.put("message", "添加失败，请刷新后再试！");
		}
		return resultMap;
	}
	/**
	 * 删除权限，根据ID，但是删除权限的时候，需要查询是否有赋予给角色，如果有角色在使用，那么就不能删除。
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/deletePermissionById",method= RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deletePermissionById(@RequestParam("ids") String id){
		try {
			permissionService.deletePermissionById(id);
			resultMap.put("status", 200);
		} catch (Exception e) {
			resultMap.put("status", 500);
			resultMap.put("message", "删除失败，请刷新后再试或具有赋予角色！");
		}
		return resultMap;
	}

	@RequestMapping(value="/allocation")
	public ModelAndView allocation(ModelMap modelMap,Integer pageNo,String findContent){
		modelMap.put("findContent", findContent);
		Pagination<RolePermissionAllocation> boPage = roleService.findRoleAndPermissionPage(findContent,pageNo==null?1:pageNo,pageSize);
		modelMap.put("page", boPage);
		return new ModelAndView("ftl/permission/allocation");
	}


	/**
	 * 根据角色ID查询权限
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/selectPermissionById")
	@ResponseBody
	public List<RolePermissionMark> selectPermissionById(String id){
		List<RolePermissionMark> rolePermissionMarks = permissionService.selectPermissionsByRoleId(id);
		return rolePermissionMarks;
	}

    /**
     * 操作角色的权限
     * @param roleId 	角色ID
     * @param ids		权限ID，以‘,’间隔
     * @return
     */
    @RequestMapping(value="/addPermission2Role")
    @ResponseBody
    public Map<String,Object> addPermission2Role(Integer roleId,String ids){
        return permissionService.addPermission2Role(roleId,ids);
    }

    /**
     * 根据角色id清空权限。
     * @param roleIds	角色ID ，以‘,’间隔
     * @return
     */
    @RequestMapping(value="/clearPermissionByRoleIds")
    @ResponseBody
    public Map<String,Object> clearPermissionByRoleIds(String roleIds){
        return permissionService.deleteByRoleIds(roleIds);
    }
}
