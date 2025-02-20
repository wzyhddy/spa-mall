package com.net.sparrow.service.log;

import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.log.BizLogConditionEntity;
import com.net.sparrow.entity.log.BizLogEntity;
import com.net.sparrow.mapper.log.BizLogMapper;
import com.net.sparrow.util.FillUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务日志 服务层
 */
@Service
public class BizLogService {

    @Autowired
    private BizLogMapper bizLogMapper;

    /**
     * 查询业务日志信息
     *
     * @param id 业务日志ID
     * @return 业务日志信息
     */
    public BizLogEntity findById(Long id) {
        return bizLogMapper.findById(id);
    }

    /**
     * 根据条件分页查询业务日志列表
     *
     * @param bizLogConditionEntity 业务日志信息
     * @return 业务日志集合
     */
    public ResponsePageEntity<BizLogEntity> searchByPage(BizLogConditionEntity bizLogConditionEntity) {
        int count = bizLogMapper.searchCount(bizLogConditionEntity);
        if (count == 0) {
            return ResponsePageEntity.buildEmpty(bizLogConditionEntity);
        }
        List<BizLogEntity> dataList = bizLogMapper.searchByCondition(bizLogConditionEntity);
        return ResponsePageEntity.build(bizLogConditionEntity, count, dataList);
    }

    /**
     * 新增业务日志
     *
     * @param bizLogEntity 业务日志信息
     * @return 结果
     */
    public void save(BizLogEntity bizLogEntity) {
		FillUserUtil.fillCreateUserInfo(bizLogEntity);
		bizLogMapper.insert(bizLogEntity);
    }

    /**
     * 修改业务日志
     *
     * @param bizLogEntity 业务日志信息
     * @return 结果
     */
    public int update(BizLogEntity bizLogEntity) {
        return bizLogMapper.update(bizLogEntity);
    }

    /**
     * 删除业务日志对象
     *
     * @param id 系统ID
     * @return 结果
     */
    public int deleteById(Long id) {
        return bizLogMapper.deleteById(id);
    }

}
