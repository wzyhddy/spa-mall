package com.net.sparrow.mapper;


import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * 公共mapper
 *
 * @author 苏三，该项目是知识星球：java突击队 的内部项目
 * @date 2024/1/30 下午3:57
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
