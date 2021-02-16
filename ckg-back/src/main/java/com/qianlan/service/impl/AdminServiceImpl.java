package com.qianlan.service.impl;

import com.qianlan.entity.Admin;
import com.qianlan.mapper.AdminMapper;
import com.qianlan.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 浅蓝
 * @since 2021-02-14
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
