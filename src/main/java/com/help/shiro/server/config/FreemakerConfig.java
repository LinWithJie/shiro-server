package com.help.shiro.server.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * Created by BangZhuLi on 2018/3/2
 */
@Configuration
public class FreemakerConfig {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Bean
    public InternalResourceViewResolver internalResourceViewResolver1() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        InternalResourceViewResolver resourceViewResolver = new InternalResourceViewResolver();
        resourceViewResolver.setPrefix("views/");
        resourceViewResolver.setSuffix(".jsp");
        resourceViewResolver.setViewClass(InternalResourceView.class);
        resourceViewResolver.setOrder(1);
        return  resourceViewResolver;
    }

    @Bean
    public InternalResourceViewResolver InternalResourceViewResolver2() {
        InternalResourceViewResolver resourceViewResolver = new InternalResourceViewResolver();
        resourceViewResolver.setPrefix("views/");
        resourceViewResolver.setSuffix(".jsp");
        resourceViewResolver.setViewClass(JstlView.class);
        resourceViewResolver.setOrder(2);
        return  resourceViewResolver;
    }

    @Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setViewClass(FreeMarkerView.class);
        resolver.setContentType("text/html; charset=utf-8");
        resolver.setCache(true);
        resolver.setSuffix(".ftl");
        resolver.setOrder(0);
        return resolver;
    }

    /*@Bean
    public  FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer configurer =  new FreeMarkerConfigExtend();
        //configurer.setTemplateLoaderPath("/templates/");
        configurer.setDefaultEncoding("utf-8");
        Properties properties = new  Properties();
        properties.put("template_update_delay", "0");
        properties.put("defaultEncoding", "UTF-8");
        properties.put("url_escaping_charset", "UTF-8");
        properties.put("locale", "zh_CN");
        properties.put("boolean_format", "true,false");
        properties.put("datetime_format", "yyyy-MM-dd HH:mm:ss");
        properties.put("date_format", "yyyy-MM-dd");
        properties.put("time_format", "HH:mm:ss");
        properties.put("number_format", "#");
        properties.put("whitespace_stripping", "true");
        properties.put("auto_import", "/ftl/common/config/top.ftl as _top,/ftl/common/config/left.ftl as _left");
        configurer.setFreemarkerSettings(properties);
        return configurer;
    }*/

    /*@Bean
    public FreeMarkerConfigurer freeMarkerConfigExtend(FreeMarkerConfigurer freeMarkerConfigurer) {
        freemarker.template.Configuration configuration = freeMarkerConfigurer.getConfiguration();
        configuration.setSharedVariable("shiro", new ShiroTags());
        configuration.setNumberFormat("#");
        return freeMarkerConfigurer;
    }*/

    @PostConstruct
    public void  setSharedVariable(){
        freeMarkerConfigurer.getConfiguration().setSharedVariable("shiro", new ShiroTags());
        freeMarkerConfigurer.getConfiguration().setNumberFormat("#");
    }

}
