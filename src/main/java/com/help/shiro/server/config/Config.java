package com.help.shiro.server.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.shiro.core.remote.RemoteServiceInterface;
import com.help.shiro.server.service.RemoteService;
import com.help.shiro.server.shiro.MyRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author created by BangZhuLi
 * @date 2018/1/26  14:53
 */
@Configuration
public class Config extends CachingConfigurerSupport {


    @Bean
    @SuppressWarnings("rawtypes")
    public RedisSerializer redisSerializer() {
        MyRedisSerializer redisSerializer =  new MyRedisSerializer<Object>();
        /*Jackson2JsonRedisSerializer redisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);*/
        return redisSerializer;
    }

    @Bean
    public RedisTemplate<Serializable, Object> redisTemplate(RedisConnectionFactory factory, RedisSerializer redisSerializer)
    {
        RedisTemplate<Serializable, Object> template = new RedisTemplate<Serializable, Object>();
        template.setConnectionFactory(factory);
        template.setDefaultSerializer(redisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Bean(name = "/remoteService")
    public HttpInvokerServiceExporter remoteService(RemoteService remoteService) {
        System.out.println("暴露remoteService服务");
        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setService(remoteService);
        exporter.setServiceInterface(RemoteServiceInterface.class);
        return exporter;
    }


}
