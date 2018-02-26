package com.help.shiro.server.dao;


import com.help.shiro.server.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author created by BangZhuLi
 * @date 2018/1/26  14:44
          */
@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    Address findByUserId(String userId);
}
