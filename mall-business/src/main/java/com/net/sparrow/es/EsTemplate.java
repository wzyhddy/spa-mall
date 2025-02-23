package com.net.sparrow.es;

import com.alibaba.fastjson.JSON;
import com.net.sparrow.entity.EsBaseEntity;
import com.net.sparrow.entity.ResponsePageEntity;
import com.net.sparrow.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ES template
 */
@Component
@Slf4j
public class EsTemplate {

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	/**
	 * 添加数据到ES
	 *
	 * @param indexName    索引名称
	 * @param esBaseEntity 数据
	 * @return 是否成功
	 */
	public boolean insert(String indexName, EsBaseEntity esBaseEntity) {
		IndexRequest request = new IndexRequest(indexName);
		request.id(esBaseEntity.getId());
		request.source(esBaseEntity.getData(), XContentType.JSON);
		try {
			IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
			return indexResponse.status().equals(RestStatus.OK);
		} catch (IOException e) {
			log.error("写入ES失败，原因：", e);
			throw new BusinessException("写入ES失败");
		}
	}

	/**
	 * 批量添加数据到ES
	 *
	 * @param indexName      索引名称
	 * @param esBaseEntities 数据集合
	 */
	public void batchInsert(String indexName, List<EsBaseEntity> esBaseEntities) {
		BulkRequest request = new BulkRequest();
		esBaseEntities.forEach(item -> request.add(new IndexRequest(indexName).id(item.getId()).source(item.getData(), XContentType.JSON)));
		try {
			restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			log.error("批量写入ES失败，原因：", e);
			throw new BusinessException("批量写入ES失败");
		}
	}

	/**
	 * 查询数据
	 *
	 * @param idxName index
	 * @param builder 查询参数
	 * @param aClass  结果类对象
	 * @return java.util.List<T>
	 */
	public <T> List<T> search(String idxName, SearchSourceBuilder builder, Class<T> aClass) throws IOException {
		SearchRequest request = new SearchRequest(idxName);
		request.source(builder);
		SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
		SearchHit[] hits = response.getHits().getHits();
		return Arrays.stream(hits).map(hit -> JSON.parseObject(hit.getSourceAsString(), aClass)).collect(Collectors.toList());
	}

	public <T> List<T> search(String idxName, SearchSourceBuilder builder, Class<T> aClass, ResponsePageEntity responsePageEntity) throws IOException {
		SearchRequest request = new SearchRequest(idxName);
		request.source(builder);
		SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
		SearchHit[] hits = response.getHits().getHits();
		int total = (int) response.getHits().getTotalHits().value;
		responsePageEntity.setTotalCount(total);
		return Arrays.stream(hits).map(hit -> JSON.parseObject(hit.getSourceAsString(), aClass)).collect(Collectors.toList());
	}
}
