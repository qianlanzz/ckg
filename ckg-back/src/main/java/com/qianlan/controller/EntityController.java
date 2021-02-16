package com.qianlan.controller;
import com.qianlan.common.lang.Result;
import com.qianlan.entity.Entity;
import com.qianlan.entity.Relationship;
import com.qianlan.service.EntityService;
import com.qianlan.service.RelationshipService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 浅蓝
 * @since 2021-02-07
 */
@RestController
@RequestMapping("/entity")
public class EntityController {
    @Autowired
    private EntityService entityService;

    @Autowired
    private RelationshipService relationshipService;
    @GetMapping("/{name}")
    public Result getEntityByName(@PathVariable String name) throws IOException {
        List<Entity> entities = entityService.getEntityByName(name);
        List<Relationship> relationships = relationshipService.getRelationships(name);
        Map<String, List> map = new HashMap<>();
        map.put("relationship", relationships);
        map.put("node", entities);
        return Result.succ(200,"查询成功",map);
    }

    @DeleteMapping("/delete")
    public Result deleteNode(@RequestBody Map map) {
        return entityService.deleteEntity(map);
    }

    @PutMapping("/update")
    public Result updateEntity(@RequestBody Map map) {
        return entityService.updateEntity(map);
    }

    @PostMapping("/add")
    public Result addEntity(@RequestBody Map map){
        return entityService.addEntity(map);
    }

    @PostMapping("/addOne")
    public Result addOneNode(@RequestBody Entity entity){
        return entityService.addOneEntity(entity);
    }
}

