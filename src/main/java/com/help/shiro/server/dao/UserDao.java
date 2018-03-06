package com.help.shiro.server.dao;


import com.help.shiro.server.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author created by BangZhuLi
 * @date 2018/1/26  14:35
 */
@Repository
public interface UserDao extends JpaRepository<User, String> {

    User findByIdAndPassword(String userId, String password);

    List<User> findByTotalGreaterThan(double money);

    Page<User> findAll(Specification specification, Pageable pageable);

}
