package com.net.sparrow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Sparrow
 * @Description: ReponsePageEntity
 * @DateTime: 2025/2/17 17:54
 **/
@AllArgsConstructor
@Data
public class ResponsePageEntity<T> implements Serializable {

	private static final Integer ZERO = 0;

	private Integer pageNo;
	private Integer pageSize;
	/**
	 * 总页数
	 */
	private Integer totalPage;
	/**
	 * 总记录数
	 */
	private Integer totalCount;

	private List<T> data;

	public static <T> ResponsePageEntity<T> buildEmpty(RequestPageEntity requestPageEntity) {
		return build(requestPageEntity, 0, null);
	}

	public static <T> ResponsePageEntity<T> build(RequestPageEntity requestPageEntity, Integer totalCount, List<T> data) {
		Integer totalPage = getTotalPage(requestPageEntity.getPageNo(), requestPageEntity.getPageSize());
		return new ResponsePageEntity(requestPageEntity.getPageNo(), requestPageEntity.getPageSize(), totalPage, totalCount, data);
	}


	private static Integer getTotalPage(Integer pageSize, Integer totalCount) {
		if (Objects.isNull(pageSize) || Objects.isNull(totalCount)) {
			return ZERO;
		}

		if (pageSize <= 0 || totalCount <= 0) {
			return ZERO;
		}
		return totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
	}
}
