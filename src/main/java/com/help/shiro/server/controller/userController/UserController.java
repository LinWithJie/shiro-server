package com.help.shiro.server.controller.userController;

import com.help.shiro.server.controller.BaseController;
import com.help.shiro.server.domain.User;
import com.help.shiro.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author created by BangZhuLi
 * @date 2018/1/29  11:55
 */
@Controller
@Slf4j
public class UserController extends BaseController {

    @Autowired
    private UserService service;

    @GetMapping("/login")
    public String login() {
        return "ftl/user/login";
    }


    @GetMapping("/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("ftl/user/index");
        return modelAndView;
    }

    @GetMapping("/")
    public void toIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/index").forward(request,response);
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request,
                        HttpServletResponse response) throws Exception {
        //是否已登录，已登录情况下直接请求中带的回调地址，或者返回默认首页
        String user = request.getRemoteUser();
        if (user != null) {
            response.sendRedirect(request.getContextPath() + "/index");
        }

        //log.info("超时时间：{}" + request.getSession().getMaxInactiveInterval());
        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.
        String exception = (String) request.getAttribute("shiroLoginFailure");
        log.info("exception = {}" + exception);
        String msg = "";
        if (exception != null) {
            if (UnknownAccountException.class.getName().equals(exception)) {
                System.out.println("UnknownAccountException -- > 账号不存在：");
                msg = "UnknownAccountException -- > 账号不存在：";
            } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
                System.out.println("IncorrectCredentialsException -- > 密码不正确：");
                msg = "IncorrectCredentialsException -- > 密码不正确：";
            } else if ("kaptchaValidateFailed".equals(exception)) {
                System.out.println("kaptchaValidateFailed -- > 验证码错误");
                msg = "kaptchaValidateFailed -- > 验证码错误";
            } else {
                msg = "else >> "+exception;
                System.out.println("else -- >" + exception);
            }
        }
        request.setAttribute("msg", msg);
        // 此方法不处理登录成功,由shiro进行处理.
        return "/login";

    }

    /**
     * 偷懒一下，通用页面跳转
     * @param page
     * @return
     */
    @RequestMapping(value="{page}",method=RequestMethod.GET)
    public ModelAndView toPage(@PathVariable("page")String page){
        return new ModelAndView(String.format("ftl/user/%s", page));
    }

    /**
     * 密码修改
     * @return
     */
    @RequestMapping(value="updatePswd",method=RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updatePswd(@RequestParam("pswd") String pswd,
                                         @RequestParam("newPswd") String newPswd){
        //根据当前登录的用户帐号 + 老密码，查询。
        String userId = (String) SecurityUtils.getSubject().getPrincipal();
        User user  = service.findById(userId);
        String password = user.getPassword();
        if(!password.endsWith(pswd)){
            resultMap.put("status", 300);
            resultMap.put("message", "密码不正确！");
        }else{
            user.setPassword(newPswd);
            //修改密码
            service.update(user);
            resultMap.put("status", 200);
            resultMap.put("message", "修改成功!");
            //重新登录一次
            //TokenManager.login(user, Boolean.TRUE);
        }
        return resultMap;
    }
    /**
     * 个人资料修改
     * @return
     */
    @RequestMapping(value="updateSelf",method=RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateSelf(User entity){
        try {
            User user = service.findById((String) SecurityUtils.getSubject().getPrincipal());
            user.setNickname(entity.getNickname());
            service.update(user);
            resultMap.put("status", 200);
            resultMap.put("message", "修改成功!");
        } catch (Exception e) {
            resultMap.put("status", 500);
            resultMap.put("message", "修改失败!");
            logger.info("修改个人资料出错：{}" + e);
        }
        return resultMap;
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "dqwdqw";
    }
}
