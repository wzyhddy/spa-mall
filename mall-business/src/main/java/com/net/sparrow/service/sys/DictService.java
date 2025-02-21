package com.net.sparrow.service.sys;

import cn.hutool.json.JSONUtil;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.sys.DictConditionEntity;
import com.net.sparrow.entity.sys.DictDetailConditionEntity;
import com.net.sparrow.entity.sys.DictDetailEntity;
import com.net.sparrow.entity.sys.DictEntity;
import com.net.sparrow.mapper.BaseMapper;
import com.net.sparrow.mapper.sys.DictDetailMapper;
import com.net.sparrow.mapper.sys.DictMapper;
import com.net.sparrow.service.BaseService;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.util.RedisUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据字典 服务层
 */
@Service
public class DictService extends BaseService<DictEntity, DictConditionEntity> {

    @Autowired
    private DictMapper dictMapper;

	@Autowired
	private DictDetailMapper dictDetailMapper;

    @Autowired
    private RedisUtil redisUtil;

    private static final String DICT_DATA_KEY = "dictData";


    /**
     * 从缓存中获取数据字典
     *@Cacheable 声明这个方法使用了Spring的二级缓存。
     * 使用dictCacheKeyGenerator指定了key的生成规则
     * @param dictName 数据字典名称
     * @return 数据字典
     */
    @Cacheable(value = "dict_data", keyGenerator = "dictCacheKeyGenerator")
    public List<DictDetailEntity> queryDictDetailEntity(String dictName) {
        return getDictDataFromRedis(dictName);
    }

    private List<DictDetailEntity> getDictDataFromRedis(String hashKey) {
        String json = (String) redisUtil.getHashValue(DICT_DATA_KEY, hashKey);
        if (!StringUtils.hasLength(json)) {
            return Collections.emptyList();
        }

        return JSONUtil.toList(json, DictDetailEntity.class);
    }
    /**
     * 查询数据字典信息
     *
     * @param id 数据字典ID
     * @return 数据字典信息
     */
    public DictEntity findById(Long id) {
        return dictMapper.findById(id);
    }

    /**
     * 根据条件分页查询数据字典列表
     *
     * @param dictConditionEntity 数据字典信息
     * @return 数据字典集合
     */
    public ResponsePageEntity<DictEntity> searchByPage(DictConditionEntity dictConditionEntity) {
        int count = dictMapper.searchCount(dictConditionEntity);
        if (count == 0) {
            return ResponsePageEntity.buildEmpty(dictConditionEntity);
        }
        List<DictEntity> dataList = dictMapper.searchByCondition(dictConditionEntity);
        return ResponsePageEntity.build(dictConditionEntity, count, dataList);
    }

    /**
     * 新增数据字典
     *
     * @param dictEntity 数据字典信息
     * @return 结果
     */
    public int insert(DictEntity dictEntity) {
        FillUserUtil.fillCreateUserInfo(dictEntity);
        return dictMapper.insert(dictEntity);
    }

    /**
     * 修改数据字典
     *
     * @param dictEntity 数据字典信息
     * @return 结果
     */
    public int update(DictEntity dictEntity) {
        FillUserUtil.fillUpdateUserInfo(dictEntity);
        return dictMapper.update(dictEntity);
    }

    /**
     * 删除数据字典对象
     *
     * @param ids 系统ID
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<DictEntity> dictEntities = dictMapper.findByIds(ids);
        AssertUtil.notEmpty(dictEntities, "数据字典详情已被删除");

        DictEntity dictEntity = new DictEntity();
        FillUserUtil.fillUpdateUserInfo(dictEntity);
        return dictMapper.deleteByIds(ids, dictEntity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return dictMapper;
    }

    public void refreshDict() {
        DictConditionEntity dictConditionEntity = new DictConditionEntity();
        dictConditionEntity.setPageSize(0);
        List<DictEntity> dictEntities = dictMapper.searchByCondition(dictConditionEntity);
        if (CollectionUtils.isEmpty(dictEntities)) {
            return;
        }
        List<Long> dictIdList = dictEntities.stream().map(DictEntity::getId).collect(Collectors.toList());
        DictDetailConditionEntity detailConditionEntity = new DictDetailConditionEntity();
        detailConditionEntity.setDictIdList(dictIdList);
        detailConditionEntity.setPageSize(0);
        List<DictDetailEntity> dictDetailEntities = dictDetailMapper.searchByCondition(detailConditionEntity);
        Map<Long, List<DictDetailEntity>> dictDetailMap = dictDetailEntities.stream().collect(Collectors.groupingBy(DictDetailEntity::getDictId));
        HashMap<Object, Object> dictMap = new HashMap<>(dictEntities.size());
        for (DictEntity dictEntity : dictEntities) {
            List<DictDetailEntity> detailEntityList = dictDetailMap.get(dictEntity.getId());
                dictMap.put(dictEntity.getDictName(), JSONUtil.toJsonStr(detailEntityList));
        }
        redisUtil.putHashMap(DICT_DATA_KEY, dictMap);
    }
}
