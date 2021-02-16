package com.qianlan.controller;


import com.qianlan.common.lang.Result;
import com.qianlan.entity.Relationship;
import com.qianlan.mapper.RelationshipMapper;
import com.qianlan.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 浅蓝
 * @since 2021-02-07
 */
@RestController
@RequestMapping("/relationship")
public class RelationshipController {

    @Autowired
    RelationshipService relationshipService;
    @PostMapping("/add")
    public Result addRelationship(@RequestBody Relationship relationship){
        if (relationshipService.save(relationship)){
            return Result.succ("添加成功");
        }
        else
            return Result.fail("添加失败");
    }
}
