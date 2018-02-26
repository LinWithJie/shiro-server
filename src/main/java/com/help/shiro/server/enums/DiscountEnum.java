package com.help.shiro.server.enums;

import lombok.Getter;

/**
 * @author created by BangZhuLi
 * @date 2018/1/26  14:17
 * 消费超过多少对应的折扣
 */
@Getter
public enum DiscountEnum {

    LEVEL1(1000, 0.95)

    ;

    private double level;

    private double discount;

    DiscountEnum(Integer level, double discount) {
        this.level = level;
        this.discount = discount;
    }
}
