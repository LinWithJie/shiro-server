package com.help.shiro.server.controller.rolecontroller;

import com.help.shiro.server.controller.BaseController;
import com.help.shiro.server.dao.UserDao;
import com.help.shiro.server.domain.Role;
import com.help.shiro.server.domain.User;
import com.help.shiro.server.page.Pagination;
import com.help.shiro.server.service.RoleService;
import com.help.shiro.server.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/role")
public class RoleController extends BaseController {
	@Autowired
	RoleService roleService;

	@Autowired
	UserService userService;
	@Autowired
	UserDao userDao;
	/**
	 * 角色列表
	 * @return
	 */
	@RequestMapping(value="/index")
	public ModelAndView index(String findContent, ModelMap modelMap){
		//modelMap.put("findContent", findContent);
		Pagination roles;
		if (!StringUtils.isEmpty(findContent)) {
			roles = roleService.findAllByLikeName(findContent,pageNo,pageSize);
		}else {
			roles = roleService.findPage(pageNo,pageSize);
		}
		return new ModelAndView("ftl/role/index","page",roles);
	}
	/**
	 * 角色添加
	 * @param role
	 * @return
	 */
	@RequestMapping(value="/addRole",method= RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addRole(Role role){
		try {
			Role rs = roleService.save(role);
			resultMap.put("status", 200);
			resultMap.put("successCount", rs.toString());
		} catch (Exception e) {
			resultMap.put("status", 500);
			resultMap.put("message", "添加失败，请刷新后再试！");
			logger.info(e);
		}
		return resultMap;
	}
	/**
	 * 删除角色，根据ID，但是删除角色的时候，需要查询是否有赋予给用户，如果有用户在使用，那么就不能删除。
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/deleteRoleById",method= RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteRoleById(@RequestParam("ids") String id){
		try {
			roleService.deleteRoleById(id);
			resultMap.put("status", 200);
		} catch (Exception e) {
			resultMap.put("status", 500);
			resultMap.put("message", "删除失败，请刷新后再试或具有赋予角色！");
			logger.info(e);
		}
		return resultMap;
	}
	/**
	 * 我的权限页面
	 * @return
	 */
	@RequestMapping(value="/mypermission",method= RequestMethod.GET)
	public ModelAndView mypermission(){
		return new ModelAndView("ftl/permission/mypermission");
	}
	/**
	 * 我的权限 bootstrap tree data
	 * @return
	 */
	@RequestMapping(value="/getPermissionTree",method= RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> getPermissionTree(){
		//查询我所有的角色 ---> 权限
		List<Map<String, Object>> data = roleService.findRolesByUserId((String) SecurityUtils.getSubject().getPrincipal());
		return data;
	}

	/**
	 * 用户角色权限分配
	 * @param modelMap
	 * @param pageNo
	 * @param findContent
	 * @return
	 */
	@RequestMapping(value="/allocation")
	public ModelAndView allocation(ModelMap modelMap,Integer pageNo,String findContent){
		modelMap.put("findContent", findContent);
		Pagination<User> users = userService.findPage(pageNo,pageSize);
		modelMap.put("page", users);
		return new ModelAndView("role/allocation");
	}

	/**
	 * 根据用户ID查询权限
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/selectRoleByUserId")
	@ResponseBody
	public List<Role> selectRoleByUserId(String id){
		List<Role> bos = roleService.selectRoleByUserId(id);
		return bos;
	}
	/**
	 * 操作用户的角色
	 * @param userId 	用户ID
	 * @param ids		角色ID，以‘,’间隔
	 * @return
	 */
	@RequestMapping(value="/addRole2User")
	@ResponseBody
	public Map<String,Object> addRole2User(String userId,String ids){
		return roleService.addRole2User(userId,ids);
	}
	/**
	 * 根据用户id清空角色。
	 * @param userIds	用户ID ，以‘,’间隔
	 * @return
	 */
	@RequestMapping(value="/clearRoleByUserIds")
	@ResponseBody
	public Map<String,Object> clearRoleByUserIds(String userIds){
		return roleService.deleteRoleByUserIds(userIds);
	}
}
