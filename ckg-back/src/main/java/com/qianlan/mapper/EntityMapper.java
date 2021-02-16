package com.qianlan.mapper;

import com.qianlan.common.lang.Result;
import com.qianlan.entity.Entity;
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
public interface EntityMapper extends BaseMapper<Entity> {
    List<Entity> getEntityByName(String name);
}
