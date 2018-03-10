package com.help.shiro.server.shiro;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author created by BangZhuLi
 * @date 2018/1/26  17:59
 * 分布式session共享----基于redis
 */
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
@Slf4j
public class RedisSessionDao extends AbstractSessionDAO {

    // Session超时时间，单位为秒
    @Setter
    @Getter
    private long expireTime = 3600;

    @Autowired
    private RedisTemplate<Serializable, Object> redisTemplate ;



    @Override
    public Collection<Session> getActiveSessions() {
        return null;
    }

    @Override // 更新session
    public void update(Session session) throws UnknownSessionException {


        HttpServletRequest request =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String url = request.getServletPath();
        if (url.indexOf("js") >= 0 || url.indexOf("css") >= 0) {
            return;
        }
        if (session == null || session.getId() == null) {
            return;
        }
        log.info("===============update================");
        //session.setTimeout(expireTime);
        redisTemplate.opsForValue().set(session.getId(), session, expireTime, TimeUnit.SECONDS);
    }

    @Override // 删除session
    public void delete(Session session) {
        log.info("===============delete================");
        if (null == session) {
            return;
        }
        redisTemplate.opsForValue().getOperations().delete(session.getId());
    }

    /*@Override// 获取活跃的session，可以用来统计在线人数，如果要实现这个功能，可以在将session加入redis时指定一个session前缀，统计的时候则使用keys("session-prefix*")的方式来模糊查找redis中所有的session集合
    public Collection<Session> getActiveSessions() {
        log.info("==============getActiveSessions=================");
        return redisTemplate.keys("*");
    }*/

    @Override// 加入session
    protected Serializable doCreate(Session session) {
        log.info("===============doCreate================");
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);

        redisTemplate.opsForValue().set(session.getId(), session, expireTime, TimeUnit.SECONDS);
        return sessionId;
    }

    @Override// 读取session
    protected Session doReadSession(Serializable sessionId) {

        HttpServletRequest request =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String url = request.getServletPath();
        if (url.indexOf("js") >= 0 || url.indexOf("css") >= 0) {
            return null;
        }
        if (sessionId == null) {
            return null;
        }
        log.info("==============doReadSession=================");
        return (Session) redisTemplate.opsForValue().get(sessionId);
    }
}
