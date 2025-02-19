package com.net.sparrow.service.common;

import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.common.CommonNotifyConditionEntity;
import com.net.sparrow.entity.common.CommonNotifyEntity;
import com.net.sparrow.mapper.common.CommonNotifyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通知 服务层
 */
@Service
public class CommonNotifyService {

	@Autowired
	private CommonNotifyMapper commonNotifyMapper;

	/**
     * 查询通知信息
     *
     * @param id 通知ID
     * @return 通知信息
     */
	public CommonNotifyEntity findById(Long id) {
	    return commonNotifyMapper.findById(id);
	}

	/**
     * 根据条件分页查询通知列表
     *
     * @param commonNotifyConditionEntity 通知信息
     * @return 通知集合
     */
	public ResponsePageEntity<CommonNotifyEntity> searchByPage(CommonNotifyConditionEntity commonNotifyConditionEntity) {
		int count = commonNotifyMapper.searchCount(commonNotifyConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(commonNotifyConditionEntity);
		}
		List<CommonNotifyEntity> dataList = commonNotifyMapper.searchByCondition(commonNotifyConditionEntity);
		return ResponsePageEntity.build(commonNotifyConditionEntity, count, dataList);
	}

    /**
     * 新增通知
     *
     * @param commonNotifyEntity 通知信息
     * @return 结果
     */
	public int insert(CommonNotifyEntity commonNotifyEntity) {
	    return commonNotifyMapper.insert(commonNotifyEntity);
	}

	/**
     * 修改通知
     *
     * @param commonNotifyEntity 通知信息
     * @return 结果
     */
	public int update(CommonNotifyEntity commonNotifyEntity) {
	    return commonNotifyMapper.update(commonNotifyEntity);
	}

	/**
     * 删除通知对象
     *
     * @param id 系统ID
     * @return 结果
     */
	public int deleteById(Long id) {
		return commonNotifyMapper.deleteById(id);
	}

}
