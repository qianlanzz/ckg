package com.qianlan.controller;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qianlan.common.dto.LoginDto;
import com.qianlan.common.lang.Result;
import com.qianlan.entity.Admin;
import com.qianlan.service.AdminService;
import com.qianlan.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @创建人 qianlan
 * @创建时间 2020/11/17
 * @描述
 */
@RestController
public class AccountController {
    @Autowired
    AdminService adminService;
    @Autowired
    JwtUtils jwtUtils;
    @PostMapping("/login")
    public Result login(@RequestBody LoginDto loginDto, HttpServletResponse response){

        Admin admin = adminService.getOne(new QueryWrapper<Admin>().eq("username", loginDto.getUsername()));
        Assert.notNull(admin,"用户不存在");
        String s = SecureUtil.md5(loginDto.getPassword());
        System.out.println(s);
        if(!admin.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
            return Result.fail("密码不正确");
        }
        String jwt = jwtUtils.generateToken(admin.getId());
        response.setHeader("Authorization",jwt);
        response.setHeader("Access-control-Expose-Headers","Authorization");

        return Result.succ(200,"登录成功",null);
    }

    @PostMapping("/logout")
    public Result logout(){
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }
}
