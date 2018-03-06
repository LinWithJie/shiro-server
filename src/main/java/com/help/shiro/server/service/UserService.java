package com.help.shiro.server.service;


import com.help.shiro.server.dao.UserDao;
import com.help.shiro.server.domain.Permission;
import com.help.shiro.server.domain.User;
import com.help.shiro.server.page.Paginable;
import com.help.shiro.server.page.Pagination;
import com.help.shiro.server.shiro.RedisSessionDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author created by BangZhuLi
 * @date 2018/1/26  14:54
 * 用户服务
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserDao userDao;

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
        return userDao.findOne(userId);
    }


    public Session getSession(String sessionId) {
        Session session = redisSessionDao.readSession(sessionId);

        return session;
    }

    public User update(User user) {
        return userDao.save(user);
    }

    /**
     * 分页查询用户
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    public Pagination<User> findPage(Integer pageNo, Integer pageSize) {
        pageNo = (null == pageNo ? 1 : pageNo);
        pageSize = (null == pageSize ? 10 : pageSize);
        Pagination page = new Pagination();
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        Pageable pageable = new PageRequest(pageNo-1, pageSize, Sort.Direction.ASC, "id");
        Page page1 = userDao.findAll(pageable);
        page.setList(page1.getContent());
        page.setTotalCount(page1.getNumberOfElements());
        return page;
    }

    /**
     * 根据用户昵称模糊搜索
     * @param name
     * @param page
     * @param count
     * @return
     */
    public Pagination<User> findAllByLikeName(final String name, int page, int count) {
        Specification<User> specification = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root,
                                         CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Path<String> _name = root.get("nickname");
                Predicate _key = criteriaBuilder.like(_name, "%" + name + "%");
                return criteriaBuilder.and(_key);
            }
        };
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(page - 1, count, sort);
        Page page1 = userDao.findAll(specification, pageable);
        Pagination<User> pagination = new Pagination();
        pagination.setPageNo(page);
        pagination.setPageSize(count);
        pagination.setTotalCount(page1.getNumberOfElements());
        pagination.setList(page1.getContent());
        return pagination;
    }

    /**
     * 通过ID批量删除用户
     * @param ids
     * @return
     */
    public Map<String, Object> deleteUserById(String ids) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        try {
            int count=0;
            String[] idArray = new String[]{};
            if(StringUtils.contains(ids, ",")){
                idArray = ids.split(",");
            }else{
                idArray = new String[]{ids};
            }

            for (String id : idArray) {
                count+=1;
            }
            resultMap.put("status", 200);
            resultMap.put("count", count);
        } catch (Exception e) {
            //LoggerUtils.fmtError(getClass(), e, "根据IDS删除用户出现错误，ids[%s]", ids);
            resultMap.put("status", 500);
            resultMap.put("message", "删除出现错误，请刷新后再试！");
        }
        return resultMap;
    }

    public void deleteByPrimaryKey(String userId) {
        userDao.delete(userId);
    }

    /**
     * 改变用户状态
     * @param id
     * @param status
     * @return
     */
    public Map<String, Object> updateForbidUserById(String id, byte status) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        try {
            User user = findById(id);
            user.setState(status);
            update(user);

            //如果当前用户在线，需要标记并且踢出
            //customSessionManager.forbidUserById(id,status);


            resultMap.put("status", 200);
        } catch (Exception e) {
            resultMap.put("status", 500);
            resultMap.put("message", "操作失败，请刷新再试！");
            //LoggerUtils.fmtError(getClass(), "禁止或者激活用户登录失败，id[%s],status[%s]", id,status);
        }
        return resultMap;
    }


}
