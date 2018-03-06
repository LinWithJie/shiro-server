package com.help.shiro.server.controller.permissionController;

import com.help.shiro.server.controller.BaseController;
import com.help.shiro.server.domain.Permission;
import com.help.shiro.server.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/permission")
@Slf4j
public class PermissionController extends BaseController {
	
	@Autowired
	PermissionService permissionService;
	/**
	 * 权限列表
	 * @param findContent	查询内容
	 * @param pageNo		页码
	 * @param modelMap		参数回显
	 * @return
	 */
	@RequestMapping(value="/index")
	public ModelAndView index(String findContent, ModelMap modelMap, Integer pageNo){
		modelMap.put("findContent", findContent);
		Page page = permissionService.findPage(pageNo,pageSize);
		return new ModelAndView("ftl/permission/index","page",page);
	}
	/**
	 * 权限添加
	 * @param psermission
	 * @return
	 */
	@RequestMapping(value="/addPermission",method= RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addPermission(Permission psermission){
		try {
			Permission entity = permissionService.save(psermission);
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
	public Map<String,Object> deleteRoleById(String id){
		try {
			permissionService.deleteById(Integer.parseInt(id));
			resultMap.put("status", 200);
		} catch (Exception e) {
			resultMap.put("status", 500);
			resultMap.put("message", "删除失败，请刷新后再试或具有赋予角色！");
		}
		return null;
	}
}
