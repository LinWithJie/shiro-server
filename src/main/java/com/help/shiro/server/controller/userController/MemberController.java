package com.help.shiro.server.controller.userController;

import com.help.shiro.server.controller.BaseController;
import com.help.shiro.server.domain.User;
import com.help.shiro.server.page.Paginable;
import com.help.shiro.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Created by BangZhuLi on 2018/3/5
 */
@Controller
@RequestMapping("/member")
public class MemberController extends BaseController {

    @Autowired
    UserService userService;
    /**
     * 用户列表管理
     * @return
     */
    @RequestMapping(value="/list")
    public ModelAndView list(ModelMap map, Integer pageNo, String findContent){
        Paginable page;
        if (!StringUtils.isEmpty(findContent)) {
            page = userService.findAllByLikeName(findContent,pageNo==null?1:pageNo,pageSize);
        }else  {
            page = userService.findPage(pageNo==null?1:pageNo,pageSize);
        }
        map.put("page", page);
        return new ModelAndView("ftl/member/list","page",page);
}
    /**
     * 根据ID删除，
     * @param ids	如果有多个，以“,”间隔。
     * @return
     */
    @RequestMapping(value="/deleteUserById",method=RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleteUserById(String ids){
        return userService.deleteUserById(ids);
    }
    /**
     * 禁止登录
     * @param id		用户ID
     * @param status	1:有效，0:禁止登录
     * @return
     */
    @RequestMapping(value="/forbidUserById",method=RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> forbidUserById(String id,byte status){
        return userService.updateForbidUserById(id,status);
    }



}
