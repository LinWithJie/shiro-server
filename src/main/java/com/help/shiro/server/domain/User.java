package com.help.shiro.server.domain;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author created by BangZhuLi
 * @date 2018/1/26  12:07
 * 用户实体类
 */
@Entity
@Data
@ToString(exclude = "roleList")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "idCard"))
public class User implements Serializable{
    private static final long serialVersionUID = -4425096841816277473L;

    @Id
    @GeneratedValue(generator = "phone")
    @GenericGenerator(name = "phone", strategy = "assigned")
    private String id;//标识ID--目前默认手机号绑定

    private String idCard;//用户身份证

    private String nickname;//用户名称

    private double discount;//客户消费折扣

    //private String loginName;//登录账号

    private String password;//登录密码

    private Double cardMoney;//用户债券金额

    private Double total;//累计消费额

    private String salt;//加密密码的盐

    private byte state;//用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.

    @ManyToMany(fetch= FetchType.EAGER)//立即从数据库中进行加载数据;
    @JoinTable(name = "UserRole", joinColumns = { @JoinColumn(name = "uid") }, inverseJoinColumns ={@JoinColumn(name = "roleId") })
    private List<Role> roleList;// 一个用户具有多个角色

    /**
     * 密码盐.
     * @return
     */
    public String getCredentialsSalt(){
        return this.id + this.salt;
    }
}
