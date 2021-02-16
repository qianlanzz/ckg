package com.qianlan.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @创建人 qianlan
 * @创建时间 2020/11/17
 * @描述
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String jwt) {
        this.token = jwt;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
