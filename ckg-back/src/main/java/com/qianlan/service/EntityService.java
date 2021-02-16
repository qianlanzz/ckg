package com.qianlan.service;

import com.qianlan.common.lang.Result;
import com.qianlan.entity.Entity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 浅蓝
 * @since 2021-02-07
 */
public interface EntityService extends IService<Entity> {

    //根据搜索的名字获得一个知识图谱
    List<Entity> getEntityByName(String name);


    //更新一个节点的信息包括关系
    Result updateEntity(Map map);

    //添加节点并处理好关系的映射
    Result addEntity(Map map);

    Result deleteEntity(Map map);

    Result addOneEntity(Entity entity);
}
