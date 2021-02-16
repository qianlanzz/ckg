package com.qianlan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.qianlan.common.lang.Result;
import com.qianlan.entity.Entity;
import com.qianlan.entity.Relationship;
import com.qianlan.mapper.EntityMapper;
import com.qianlan.mapper.RelationshipMapper;
import com.qianlan.service.EntityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 浅蓝
 * @since 2021-02-07
 */
@Service
public class EntityServiceImpl extends ServiceImpl<EntityMapper, Entity> implements EntityService {

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private RelationshipMapper relationshipMapper;

    @Override
    public List<Entity> getEntityByName(String name) {

        return entityMapper.getEntityByName(name);
    }

//    @Transactional(propagation = Propagation.REQUIRED)
//    @Override
//    public int deleteById(long id) {
//        int i = entityMapper.deleteById(id);
//        int n = relationshipMapper.deleteRelationship(id);
//        return (i == 1 && n == 1) ? 1 : 0;
//    }

    /**
     * 修改节点的信息和叶子节点的属性
     *
     * @param map
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Result updateEntity(Map map) {
        Long sourceId = Long.parseLong((String) map.get("sourceId"));
        String ename = (String) map.get("ename");
        String imgurl = (String) map.get("imgurl");
        String rname = (String) map.get("rname");
        Entity entity = new Entity();
        entity.setName(ename);
        entity.setImgsrc(imgurl);
        entity.setUuid(sourceId);
        int b = entityMapper.updateById(entity);
        if (b == 1) {
            QueryWrapper<Relationship> selectSourceWrapper = new QueryWrapper<>();
            selectSourceWrapper.eq("sourceid", sourceId);
            List<Relationship> relationships = relationshipMapper.selectList(selectSourceWrapper);
            System.out.println("++++++++++++++++++++++");
            System.out.println(relationships.size());
            System.out.println("++++++++++++++++++++++");
            //若size为0说明该节点没有子节点，可以修改属性值
            if (relationships.size() == 0) {
                QueryWrapper<Relationship> wrapper = new QueryWrapper<>();
                wrapper.eq("targetid", sourceId);
                Long sourceid = null;
                try {
                    sourceid = relationshipMapper.selectOne(wrapper).getSourceid();
                } catch (Exception e) {
                    return Result.fail("修改错误，请联系管理员");
                }
                Relationship relationship = new Relationship();
                relationship.setName(rname);
                UpdateWrapper<Relationship> relationshipUpdateWrapper = new UpdateWrapper<>();
                relationshipUpdateWrapper.eq("sourceid", sourceid);
                relationshipUpdateWrapper.eq("targetid", sourceId);
                relationshipMapper.update(relationship, relationshipUpdateWrapper);
            }
            return Result.succ("添加成功");
        } else {
            return Result.fail("添加失败");
        }
    }

    /**
     * 追加子节点
     *
     * @param map
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Result addEntity(Map map) {
        Long sourceId = Long.parseLong((String) map.get("sourceId"));
        String ename = (String) map.get("ename");
        String imgurl = (String) map.get("imgurl");
        String rname = (String) map.get("rname");

        QueryWrapper<Entity> isAdded = new QueryWrapper<>();   //是否可添加，只有数据库没有的节点才需要添加
        QueryWrapper<Entity> queryWrapper = isAdded.eq("name", ename);
        Entity queryEntity = entityMapper.selectOne(queryWrapper);
        Entity isCreatedEntity = null;
        Long uuid = -1l;
        if (queryEntity == null) {    //没有该节点，需要添加
            Entity entity = new Entity();
            entity.setName(ename);
            entity.setImgsrc(imgurl);
            entityMapper.insert(entity);    //插入数据
            isCreatedEntity = entityMapper.selectById(entity);
        }
        //关系的存储
        if (queryEntity == null){
            uuid = isCreatedEntity.getUuid();
        }else {
            uuid = queryEntity.getUuid();
        }
        Relationship relationship = new Relationship();
        relationship.setName(rname);
        relationship.setSourceid(sourceId);
        relationship.setTargetid(uuid);
        int relationSave = relationshipMapper.insert(relationship);
        if (relationSave == 1) {
            return Result.succ("添加成功");
        } else {
            return Result.fail("添加失败");
        }
    }

    /**
     * 删除节点
     * @param map
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Result deleteEntity(Map map) {
        Long id = Long.parseLong((String) map.get("id"));   //选中节点的id
        String name = (String) map.get("name");//该节点的name
        QueryWrapper<Relationship> selectSourceWrapper = new QueryWrapper<>();   //根据这个节点找sourceid，若存在说明该节点下面有节点，无法删除
        selectSourceWrapper.eq("sourceid", id);
        QueryWrapper<Relationship> selectTargetWrapper = new QueryWrapper<>();   //根据这个节点找targetid，若存在多个则说明该节点名称被多个引用，不能删除节点，只要删除关系即可
        selectTargetWrapper.eq("targetid", id);
        selectTargetWrapper.eq("name", name);
        QueryWrapper<Relationship> selectTargetWrapper1 = new QueryWrapper<>();
        selectTargetWrapper1.eq("targetid",id);

        List<Relationship> sourceRelationships = relationshipMapper.selectList(selectSourceWrapper);
        List<Relationship> relationships = relationshipMapper.selectList(selectTargetWrapper1);
        if (sourceRelationships.size() == 0) {
            if (relationships.size()==0){      //孤零零的一个节点，可以删除
                entityMapper.deleteById(id);
                return Result.succ("删除成功");
            }
            if (relationships.size() == 1) {  //基本情况
                entityMapper.deleteById(id);
                relationshipMapper.delete(selectTargetWrapper);
                return Result.succ("删除成功");

            } else {                         //该节点被多处引用，只需要删除关系即可，不要删除节点
                relationshipMapper.delete(selectTargetWrapper);
                return Result.succ("删除成功");
            }
        } else
            return Result.fail("该节点下面还有节点，谨慎删除");

    }



    /**
     * 添加一个新节点，若存在该节点，则添加不了
     *
     * @param entity
     * @return
     */
    @Override
    public Result addOneEntity(Entity entity) {
        String name = entity.getName();
        QueryWrapper<Entity> wrapper = new QueryWrapper<>();
        QueryWrapper<Entity> queryWrapper = wrapper.eq("name", name);
        List<Entity> entities = entityMapper.selectList(queryWrapper);
        if (entities.size() == 0) {
            int save = entityMapper.insert(entity);
            if (save == 1) {
                return Result.succ("添加成功");
            } else {
                return Result.fail("添加失败");
            }
        } else
            return Result.fail("已有重复节点，无需重新添加");
    }
}
