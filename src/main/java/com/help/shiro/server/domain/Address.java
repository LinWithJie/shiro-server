package com.help.shiro.server.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author created by BangZhuLi
 * @date 2018/1/26  14:13
 * 收货地址
 */
@Entity
@Data
@ToString
public class Address implements Serializable{
    private static final long serialVersionUID = -4802710395144826304L;

    @Id
    @GeneratedValue
    private Integer id;//标识ID

    private String userId;//所属用户

    private String name;//收货人姓名

    private String address;//具体收货地址

    private String phone;//联系电话
}
