package com.net.sparrow.service;

import cn.hutool.core.util.ArrayUtil;
import com.net.sparrow.mapper.BaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Value;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;



/**
 * 公共service
 */
@Slf4j
public abstract class BaseService<K, V> {

    @Value("${mall.mgt.exportPageSize:2}")
    private int exportPageSize;

    @Value("${mall.mgt.sheetDataSize:4}")
    private int sheetDataSize;
    /**
     * 获取BaseMapper
     *
     * @return BaseMapper
     */
    protected abstract BaseMapper getBaseMapper();


    /**
     * 用户自定义导出逻辑
     *
     * @param v 查询条件
     * @return 是否自定义
     */
    public boolean customizeExport(V v) {
        return false;
    }

}
