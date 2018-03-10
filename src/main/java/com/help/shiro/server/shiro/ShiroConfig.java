package com.help.shiro.server.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

/**
 * @author created by BangZhuLi
 * @date 2018/1/29  10:01
 * shiro配置类
 */
@Configuration
public class ShiroConfig {

    @Autowired
    private RedisCacheManager cacheManager;

    @Autowired
    private RedisSessionDao sessionDao;


    @Bean
    public ShiroFilterFactoryBean shirFilter() {
        //System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new MyShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        Map<String, Filter> filters = new HashMap<>();
        filters.put("authc", formAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filters);
        //拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
        filterChainDefinitionMap.put("/remoteService", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        return shiroFilterFactoryBean;
    }

    @Bean
    public MyShiroRealm myShiroRealm(){
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        return myShiroRealm;
    }

    /**
     * shiro核心管理器
     * @return
     */
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(myShiroRealm());
        // session管理器
        securityManager.setSessionManager(configWebSessionManager());
        //注入记住我管理器;
        securityManager.setRememberMeManager(rememberMeManager());
        securityManager.setCacheManager(cacheManager);
        return securityManager;
    }

    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }



    /**
     * cookie管理对象;
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie(){
        System.out.println("ShiroConfiguration.rememberMeCookie()");
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        simpleCookie.setHttpOnly(true);
        return simpleCookie;
    }

    /**
     * cookie管理对象;
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        System.out.println("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    @Bean
    public DefaultWebSessionManager configWebSessionManager(){
        DefaultWebSessionManager manager = new ShiroSessionManager();
        manager.setCacheManager(cacheManager);// 加入缓存管理器
        manager.setSessionDAO(sessionDao);// 设置SessionDao
        manager.setDeleteInvalidSessions(true);// 删除过期的session
        manager.setGlobalSessionTimeout(1800000);// 设置全局session超时时间
        manager.setSessionValidationSchedulerEnabled(true);// 是否定时检查session
        manager.setSessionIdCookie(sessionIdCookie());
        return manager;
    }

    @Bean
    public ServerFormAuthenticationFilter formAuthenticationFilter() {
        ServerFormAuthenticationFilter formAuthenticationFilter = new ServerFormAuthenticationFilter();
        return formAuthenticationFilter;
    }

    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie cookie = new SimpleCookie("JSESSIONID");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        return cookie;
    }


}
