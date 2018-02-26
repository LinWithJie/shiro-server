package com.help.shiro.server.service;


import com.help.shiro.server.dao.UserRepository;
import com.help.shiro.server.domain.User;
import com.help.shiro.server.shiro.RedisSessionDao;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author created by BangZhuLi
 * @date 2018/1/26  14:54
 * 用户服务
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisSessionDao redisSessionDao;

    /**
     *
     * @param id  当前默认ID为用户注册手机号
     * @param password
     * @return 若登陆成功则直接返回对象，若登录失败则返回空
     *//*
    public User login(String id, String password) {

    }*/

    /**
     * 根据用户ID查找用户(当前用户ID默认指的是电话号码)
     * @param userId
     * @return
     */
    public User findById(String userId) {
        return userRepository.findOne(userId);
    }


    public Session getSession(String sessionId) {
        Session session = redisSessionDao.readSession(sessionId);

        return session;
    }
}
