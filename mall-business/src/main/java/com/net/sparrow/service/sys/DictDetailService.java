package com.net.sparrow.service.sys;

import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.sys.DictDetailConditionEntity;
import com.net.sparrow.entity.sys.DictDetailEntity;
import com.net.sparrow.mapper.sys.DictDetailMapper;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据字典 服务层
 */
@Service
public class DictDetailService {

    @Autowired
    private DictDetailMapper dictDetailMapper;

    @Autowired
    private DictService dictService;

    /**
     * 查询信息
     *
     * @param id 数据字典ID
     * @return 数据字典信息
     */
    public DictDetailEntity findById(Long id) {
        return dictDetailMapper.findById(id);
    }

    /**
     * 根据条件分页查询数据字典列表
     *
     * @param dictDetailConditionEntity 数据字典信息
     * @return 数据字典集合
     */
    public ResponsePageEntity<DictDetailEntity> searchByPage(DictDetailConditionEntity dictDetailConditionEntity) {
        int count = dictDetailMapper.searchCount(dictDetailConditionEntity);
        if (count == 0) {
            return ResponsePageEntity.buildEmpty(dictDetailConditionEntity);
        }
        List<DictDetailEntity> dataList = dictDetailMapper.searchByCondition(dictDetailConditionEntity);
        return ResponsePageEntity.build(dictDetailConditionEntity, count, dataList);
    }

    /**
     * 新增数据字典
     *
     * @param dictDetailEntity 数据字典信息
     * @return 结果
     */
    public int insert(DictDetailEntity dictDetailEntity) {
        AssertUtil.notNull(dictDetailEntity.getDict(), "dict不能为空");
        FillUserUtil.fillCreateUserInfo(dictDetailEntity);
        dictDetailEntity.setDictId(dictDetailEntity.getDict().getId());
        return dictDetailMapper.insert(dictDetailEntity);
    }

    /**
     * 修改数据字典
     *
     * @param dictDetailEntity 数据字典信息
     * @return 结果
     */
    public int update(DictDetailEntity dictDetailEntity) {
        FillUserUtil.fillUpdateUserInfo(dictDetailEntity);
        return dictDetailMapper.update(dictDetailEntity);
    }

    /**
     * 删除数据字典门对象
     *
     * @param ids 系统ID
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<DictDetailEntity> detailEntityList = dictDetailMapper.findByIds(ids);
        AssertUtil.notEmpty(detailEntityList, "数据字典详情已被删除");

        DictDetailEntity dictDetailEntity = new DictDetailEntity();
        FillUserUtil.fillUpdateUserInfo(dictDetailEntity);
        return dictDetailMapper.deleteByIds(ids, dictDetailEntity);
    }

    /**
     * 从缓存中查询数据字典详情
     * @param dictDetailConditionEntity 查询条件
     * @return 数据字典详情
     */
    public List<DictDetailEntity> searchDictDetailFromCache(DictDetailConditionEntity dictDetailConditionEntity) {
        return dictService.queryDictDetailEntity(dictDetailConditionEntity.getDictName());
    }

}
