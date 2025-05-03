package com.net.sparrow.service;

import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.entity.common.CommonSensitiveWordConditionEntity;
import com.net.sparrow.entity.common.CommonSensitiveWordEntity;
import com.net.sparrow.helper.IdGenerateHelper;
import com.net.sparrow.mapper.BaseMapper;
import com.net.sparrow.mapper.common.CommonSensitiveWordMapper;
import com.net.sparrow.util.AssertUtil;
import com.net.sparrow.util.FillUserUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 敏感词 服务层
 */
@Slf4j
@Service
public class CommonSensitiveWordService extends BaseService<CommonSensitiveWordEntity, CommonSensitiveWordConditionEntity> {

    @Autowired
    private CommonSensitiveWordMapper commonSensitiveWordMapper;
    @Autowired
    private IdGenerateHelper idGenerateHelper;


    @Value("#{'${mall.mgt.customDictionary:}'.split(',')}")
    private List<String> customDictionaryList;
    private static Map<String, Integer> sensitiveWordMap = Maps.newHashMap();

    @PostConstruct
    public void refreshSensitiveWord() {
        CommonSensitiveWordConditionEntity commonSensitiveWordConditionEntity = new CommonSensitiveWordConditionEntity();
        commonSensitiveWordConditionEntity.setPageSize(0);
        List<CommonSensitiveWordEntity> commonSensitiveWordEntities = commonSensitiveWordMapper.searchByCondition(commonSensitiveWordConditionEntity);
        if (CollectionUtils.isEmpty(commonSensitiveWordEntities)) {
            return;
        }

        for (CommonSensitiveWordEntity commonSensitiveWordEntity : commonSensitiveWordEntities) {
            sensitiveWordMap.put(commonSensitiveWordEntity.getWord().trim(), commonSensitiveWordEntity.getType());
        }
    }

    /**
     * 定时刷新自定义的敏感词库
     */
    private void initCustomDictionary() {
        if (MapUtils.isEmpty(sensitiveWordMap)) {
            return;
        }

        sensitiveWordMap.forEach((word, value) -> {
            CustomDictionary.add(word);
        });
    }

    /**
     * 校验敏感词
     *
     * @param text
     */
    public void checkSensitiveWord(String text) {
        List<String> matchList = Lists.newArrayList();
        Map<String, String> segment = segment(text);
        segment.forEach((word, nature) -> {
            if (sensitiveWordMap.containsKey(word)) {
                matchList.add(word);
            }
        });

        AssertUtil.isTrue(CollectionUtils.isEmpty(matchList), String.format("您输入的内容，包含敏感词：%s", matchList));
    }


    private Map<String, String> segment(String text) {
        Map<String, String> wordMap = Maps.newHashMap();
        Segment segment = HanLP.newSegment().enableCustomDictionary(true);
        initCustomDictionary();
        List<Term> termList = segment.seg(text);
        for (Term term : termList) {
            String word = term.toString().substring(0, term.length());
            String nature = term.toString().substring(term.length() + 1);
            if (StringUtils.hasLength(word) && StringUtils.hasLength(nature)) {
                wordMap.put(word, nature);
            }
        }
        return wordMap;
    }

    /**
     * 初始化敏感词
     *
     * @param type     类型
     * @param filePath 文件路径
     */
    public Boolean initSensitiveWord(int type, String filePath) {
        List<CommonSensitiveWordEntity> addList = Lists.newArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!StringUtils.hasLength(line)) {
                    continue;
                }
                addList.add(creatCommonSensitiveWordEntity(type, line.trim()));
            }
        } catch (IOException e) {
            log.error("初始化敏感词失败，原因：", e);
        }

        if (CollectionUtils.isEmpty(addList)) {
            return Boolean.FALSE;
        }
        commonSensitiveWordMapper.batchInsert(addList);
        return Boolean.TRUE;
    }

    private CommonSensitiveWordEntity creatCommonSensitiveWordEntity(int type, String word) {
        CommonSensitiveWordEntity commonSensitiveWordEntity = new CommonSensitiveWordEntity();
        commonSensitiveWordEntity.setId(idGenerateHelper.nextId());
        commonSensitiveWordEntity.setType(type);
        commonSensitiveWordEntity.setWord(word.replace(",", ""));
        return commonSensitiveWordEntity;
    }

    /**
     * 查询敏感词信息
     *
     * @param id 敏感词ID
     * @return 敏感词信息
     */
    public CommonSensitiveWordEntity findById(Long id) {
        return commonSensitiveWordMapper.findById(id);
    }

    /**
     * 根据条件分页查询敏感词列表
     *
     * @param commonSensitiveWordConditionEntity 敏感词信息
     * @return 敏感词集合
     */
    public ResponsePageEntity<CommonSensitiveWordEntity> searchByPage(CommonSensitiveWordConditionEntity
                                                                              commonSensitiveWordConditionEntity) {
        int count = commonSensitiveWordMapper.searchCount(commonSensitiveWordConditionEntity);
        if (count == 0) {
            return ResponsePageEntity.buildEmpty(commonSensitiveWordConditionEntity);
        }
        List<CommonSensitiveWordEntity> dataList = commonSensitiveWordMapper.searchByCondition(commonSensitiveWordConditionEntity);
        return ResponsePageEntity.build(commonSensitiveWordConditionEntity, count, dataList);
    }

    /**
     * 新增敏感词
     *
     * @param commonSensitiveWordEntity 敏感词信息
     * @return 结果
     */
    public int insert(CommonSensitiveWordEntity commonSensitiveWordEntity) {
        return commonSensitiveWordMapper.insert(commonSensitiveWordEntity);
    }

    /**
     * 修改敏感词
     *
     * @param commonSensitiveWordEntity 敏感词信息
     * @return 结果
     */
    public int update(CommonSensitiveWordEntity commonSensitiveWordEntity) {
        return commonSensitiveWordMapper.update(commonSensitiveWordEntity);
    }

    /**
     * 批量删除敏感词对象
     *
     * @param ids 系统ID集合
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<CommonSensitiveWordEntity> entities = commonSensitiveWordMapper.findByIds(ids);
        AssertUtil.notEmpty(entities, "敏感词已被删除");

        CommonSensitiveWordEntity entity = new CommonSensitiveWordEntity();
        FillUserUtil.fillUpdateUserInfo(entity);
        return commonSensitiveWordMapper.deleteByIds(ids, entity);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return commonSensitiveWordMapper;
    }

}
