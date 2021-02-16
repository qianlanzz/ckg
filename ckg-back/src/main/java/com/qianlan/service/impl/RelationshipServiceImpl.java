package com.qianlan.service.impl;

import com.qianlan.entity.Relationship;
import com.qianlan.mapper.RelationshipMapper;
import com.qianlan.service.RelationshipService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 浅蓝
 * @since 2021-02-07
 */
@Service
public class RelationshipServiceImpl extends ServiceImpl<RelationshipMapper, Relationship> implements RelationshipService {

    @Autowired
    RelationshipMapper relationshipMapper;
    @Override
    public List<Relationship> getRelationships(String name) {
        return relationshipMapper.getRelationships(name);
    }
}
