package com.net.sparrow.service.sys;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.util.BooleanUtil;
import com.net.sparrow.dto.DeptTreeDTO;
import com.net.sparrow.util.BetweenTimeUtil;
import com.net.sparrow.util.ExcelUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.net.sparrow.mapper.sys.DeptMapper;
import com.net.sparrow.entity.sys.DeptConditionEntity;
import com.net.sparrow.entity.sys.DeptEntity;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import com.net.sparrow.mapper.BaseMapper;
import com.net.sparrow.service.BaseService;

import javax.servlet.http.HttpServletResponse;

/**
 * 部门 服务层
 *
 * @author sparrow
 * @date 2025-02-17 20:14:34
 */
@Service
public class DeptService extends BaseService< DeptEntity,  DeptConditionEntity> {

	@Autowired
	private DeptMapper deptMapper;

	/**
     * 查询部门信息
     *
     * @param id 部门ID
     * @return 部门信息
     */
	public DeptEntity findById(Long id) {
	    return deptMapper.findById(id);
	}

	/**
	 * 根据条件分页查询部门列表
	 *
	 * @param deptConditionEntity 部门信息
	 * @return 部门集合
	 */
	public ResponsePageEntity<DeptTreeDTO> searchByPage(DeptConditionEntity deptConditionEntity) {
		int count = deptMapper.searchCount(deptConditionEntity);
		if (count == 0) {
			return ResponsePageEntity.buildEmpty(deptConditionEntity);
		}
		List<DeptEntity> dataList = deptMapper.searchByCondition(deptConditionEntity);
		List<DeptTreeDTO> deptTreeDTOList = buildDeptTree(dataList, deptConditionEntity.getQueryTree());
		return ResponsePageEntity.build(deptConditionEntity, count, deptTreeDTOList);
	}

	private List<DeptTreeDTO> buildDeptTree(List<DeptEntity> dataList, Boolean queryTree) {
		if (CollectionUtils.isEmpty(dataList)) {
			return Collections.emptyList();
		}

		List<DeptTreeDTO> deptTreeDTOList = dataList.stream().map(x -> convertToDeptTreeDTO(x)).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(deptTreeDTOList)) {
			return Collections.emptyList();
		}

		if (queryTree) {
			for (DeptTreeDTO deptTreeDTO : deptTreeDTOList) {
				buildChildren(deptTreeDTO);
			}
		}
		return deptTreeDTOList;
	}

	private void buildChildren(DeptTreeDTO deptTreeDTO) {
		DeptConditionEntity deptConditionEntity = new DeptConditionEntity();
		deptConditionEntity.setPid(deptTreeDTO.getId());
		deptConditionEntity.setPageSize(0);
		List<DeptEntity> deptEntities = deptMapper.searchByCondition(deptConditionEntity);
		if(CollectionUtils.isEmpty(deptEntities)) {
			for (DeptEntity deptEntity : deptEntities) {
				DeptTreeDTO childDeptTreeDTO = convertToDeptTreeDTO(deptEntity);
				deptTreeDTO.addChildren(childDeptTreeDTO);
				buildChildren(childDeptTreeDTO);
			}
		}
	}

	private DeptTreeDTO convertToDeptTreeDTO(DeptEntity deptEntity) {
		DeptTreeDTO deptTreeDTO = new DeptTreeDTO();
		deptTreeDTO.setId(deptEntity.getId());
		deptTreeDTO.setName(deptEntity.getName());
		deptTreeDTO.setLabel(deptEntity.getName());
		deptTreeDTO.setPid(deptEntity.getPid());
		deptTreeDTO.setCreateTime(deptEntity.getCreateTime());
		return deptTreeDTO;
	}
	/**
     * 新增部门
     *
     * @param deptEntity 部门信息
     * @return 结果
     */
	public int insert(DeptEntity deptEntity) {
	    return deptMapper.insert(deptEntity);
	}

	/**
     * 修改部门
     *
     * @param deptEntity 部门信息
     * @return 结果
     */
	public int update(DeptEntity deptEntity) {
	    return deptMapper.update(deptEntity);
	}

	/**
     * 批量删除部门对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
	public int deleteByIds(List<Long> ids) {
		List<DeptEntity> entities = deptMapper.findByIds(ids);
		AssertUtil.notEmpty(entities, "部门已被删除");

		DeptEntity entity = new DeptEntity();
		FillUserUtil.fillUpdateUserInfo(entity);
		return deptMapper.deleteByIds(ids, entity);
	}

	@Override
	protected BaseMapper getBaseMapper() {
		return deptMapper;
	}

	public void export(HttpServletResponse response, DeptConditionEntity deptConditionEntity) throws IOException {
		BetweenTimeUtil.parseTime(deptConditionEntity);
		deptConditionEntity.setPageSize(0);
		List<DeptEntity> deptEntities = deptMapper.searchByCondition(deptConditionEntity);
		ExcelUtil.exportExcel("部门数据", DeptEntity.class, deptEntities, response);
	}
}
