package com.qianlan.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @创建人 qianlan
 * @创建时间 2020/11/17
 * @描述
 */
@Data
public class LoginDto{
    private String username;
    private String password;
}
