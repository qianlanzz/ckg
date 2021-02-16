package com.qianlan.mapper;

import com.qianlan.entity.Relationship;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 浅蓝
 * @since 2021-02-07
 */
public interface RelationshipMapper extends BaseMapper<Relationship> {

    List<Relationship> getRelationships(String name);

    int deleteRelationship(long id);
}
