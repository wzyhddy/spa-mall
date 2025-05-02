package com.net.sparrow.mapper;


import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * 公共mapper
 */
public interface BaseMapper<K, V> {

    /**
     * 根据条件查询数据
     *
     * @param v 实体类
     * @return List<K> 实体类的集合
     * @throws DataAccessException 数据访问异常
     */
    List<K> searchByCondition(V v) throws DataAccessException;

    int searchCount(V v);
}
