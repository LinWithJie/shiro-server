package com.help.shiro.server.controller;

import com.help.shiro.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author created by BangZhuLi
 * @date 2018/1/29  11:55
 */
@Controller
@Slf4j
public class UserController {

    @Autowired
    private UserService service;

    // 登录提交地址和applicationontext-shiro.xml配置的loginurl一致。 (配置文件方式的说法)
    @PostMapping("/login")
    public String login(HttpServletRequest request,
                        HttpServletResponse response) throws Exception {
        //是否已登录，已登录情况下直接请求中带的回调地址，或者返回默认首页
        String user = request.getRemoteUser();
        if (user != null) {
            response.sendRedirect(request.getContextPath() + "/index.html");
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

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/index")
    public String test() {
        return "index";
    }

    @GetMapping("/getSession/{id}")
    @ResponseBody
    public Session getSessionById(@PathVariable("id") String sessionId) {
        return service.getSession(sessionId);
    }

}
