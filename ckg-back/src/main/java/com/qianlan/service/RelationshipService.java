package com.qianlan.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.qianlan.entity.Relationship;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 浅蓝
 * @since 2021-02-07
 */
public interface RelationshipService extends IService<Relationship> {
    List<Relationship> getRelationships(String name);
}
