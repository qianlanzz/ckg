package com.qianlan.util;


import com.qianlan.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

/**
 * @创建人 qianlan
 * @创建时间 2020/11/17
 * @描述
 */
public class ShiroUtil {
    public static AccountProfile getProfile(){
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }
}
