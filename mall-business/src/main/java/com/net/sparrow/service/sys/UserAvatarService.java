package com.net.sparrow.service.sys;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.net.sparrow.mapper.sys.UserAvatarMapper;
import com.net.sparrow.entity.sys.UserAvatarConditionEntity;
import com.net.sparrow.entity.sys.UserAvatarEntity;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.mapper.BaseMapper;
import com.net.sparrow.service.BaseService;
/**
 * 用户头像 服务层
 *
 * @author sparrow
 * @date 2025-02-17 20:14:35
 */
@Service
public class UserAvatarService extends BaseService< UserAvatarEntity,  UserAvatarConditionEntity> {

	@Autowired
	private UserAvatarMapper userAvatarMapper;

	/**
     * 查询用户头像信息
     *
     * @param id 用户头像ID
     * @return 用户头像信息
     */
	public UserAvatarEntity findById(Long id) {
	    return userAvatarMapper.findById(id);
	}

	/**
     * 根据条件分页查询用户头像列表
     *
     * @param userAvatarConditionEntity 用户头像信息
     * @return 用户头像集合
     */
	public ResponsePageEntity<UserAvatarEntity> searchByPage(UserAvatarConditionEntity userAvatarConditionEntity) {
		int count = userAvatarMapper.searchCount(userAvatarConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(userAvatarConditionEntity);
		}
		List<UserAvatarEntity> dataList = userAvatarMapper.searchByCondition(userAvatarConditionEntity);
		return ResponsePageEntity.build(userAvatarConditionEntity, count, dataList);
	}

    /**
     * 新增用户头像
     *
     * @param userAvatarEntity 用户头像信息
     * @return 结果
     */
	public int insert(UserAvatarEntity userAvatarEntity) {
	    return userAvatarMapper.insert(userAvatarEntity);
	}

	/**
     * 修改用户头像
     *
     * @param userAvatarEntity 用户头像信息
     * @return 结果
     */
	public int update(UserAvatarEntity userAvatarEntity) {
	    return userAvatarMapper.update(userAvatarEntity);
	}

	/**
     * 批量删除用户头像对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<UserAvatarEntity> entities = userAvatarMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "用户头像已被删除");

		UserAvatarEntity entity = new UserAvatarEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return userAvatarMapper.deleteByIds(ids, entity);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return userAvatarMapper;
	}

}
