package com.common.core.domain.model;

import lombok.Data;

/**
 * 用户注册对象
 */
@Data
public class RegisterBody extends LoginBody {

    /**
     * 注册平台类型
     *  0 后台
     *  1 app
     */
    private Integer platformType;

}

