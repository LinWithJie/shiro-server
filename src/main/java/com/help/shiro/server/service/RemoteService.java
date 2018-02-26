package com.help.shiro.server.service;

import com.help.shiro.core.remote.PermissionContext;
import com.help.shiro.core.remote.RemoteServiceInterface;
import com.help.shiro.server.shiro.RedisSessionDao;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.UsesSunHttpServer;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @author created by BangZhuLi
 * @date 2018/2/7  16:07
 * 用户服务
 */
@Service
public class RemoteService implements RemoteServiceInterface {


    @Autowired
    private RedisSessionDao sessionDAO;

    @Autowired
    private AuthorizationService authorizationService;

    @Override
    public Session getSession(Serializable sessionId) {
        return sessionDAO.readSession(sessionId);
    }

    @Override
    public Serializable createSession(Session session) {
        return sessionDAO.create(session);
    }

    @Override
    public void updateSession(Session session) {
        sessionDAO.update(session);
    }

    @Override
    public void deleteSession(Session session) {
        sessionDAO.delete(session);
    }

    @Override
    public void deleteSession(Serializable sessionId) {
        sessionDAO.delete(getSession(sessionId));
    }

    @Override
    public PermissionContext getPermissions(String userId) {
        return authorizationService.findPermissionContext(userId);
    }

}
