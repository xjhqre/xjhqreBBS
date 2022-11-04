// package com.xjhqre.picture.utils;
//
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;
// import java.util.UUID;
//
// import javax.annotation.PostConstruct;
//
// import org.apache.commons.lang3.StringUtils;
// import org.apache.http.HttpHost;
// import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
// import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
// import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
// import org.elasticsearch.action.bulk.BulkRequest;
// import org.elasticsearch.action.bulk.BulkResponse;
// import org.elasticsearch.action.delete.DeleteRequest;
// import org.elasticsearch.action.delete.DeleteResponse;
// import org.elasticsearch.action.get.GetRequest;
// import org.elasticsearch.action.get.GetResponse;
// import org.elasticsearch.action.index.IndexRequest;
// import org.elasticsearch.action.index.IndexResponse;
// import org.elasticsearch.action.search.SearchRequest;
// import org.elasticsearch.action.search.SearchResponse;
// import org.elasticsearch.action.support.master.AcknowledgedResponse;
// import org.elasticsearch.action.update.UpdateRequest;
// import org.elasticsearch.action.update.UpdateResponse;
// import org.elasticsearch.client.RequestOptions;
// import org.elasticsearch.client.RestClient;
// import org.elasticsearch.client.RestClientBuilder;
// import org.elasticsearch.client.RestHighLevelClient;
// import org.elasticsearch.client.indices.GetIndexRequest;
// import org.elasticsearch.common.Strings;
// import org.elasticsearch.common.text.Text;
// import org.elasticsearch.common.unit.TimeValue;
// import org.elasticsearch.common.xcontent.XContentType;
// import org.elasticsearch.search.SearchHit;
// import org.elasticsearch.search.builder.SearchSourceBuilder;
// import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
// import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
// import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;
//
// import com.alibaba.fastjson2.JSON;
//
// import lombok.extern.slf4j.Slf4j;
//
/// **
// * ElasticSearch工具类
// *
// * @author 154594742@qq.com
// * @date 2021/3/4 19:34
// */
//
// @Slf4j
// @Component
// public class ElasticSearchUtils {
//
// @Value("${spring.elasticsearch.rest.uris}")
// private String uris;
//
// private RestHighLevelClient restHighLevelClient;
//
// /**
// * 在Servlet容器初始化前执行
// */
// @PostConstruct
// private void init() {
// try {
// if (this.restHighLevelClient != null) {
// this.restHighLevelClient.close();
// }
// if (StringUtils.isBlank(this.uris)) {
// log.error("spring.elasticsearch.rest.uris is blank");
// return;
// }
//
// // 解析yml中的配置转化为HttpHost数组
// String[] uriArr = this.uris.split(",");
// HttpHost[] httpHostArr = new HttpHost[uriArr.length];
// int i = 0;
// for (String uri : uriArr) {
// if (StringUtils.isEmpty(this.uris)) {
// continue;
// }
//
// try {
// // 拆分出ip和端口号
// String[] split = uri.split(":");
// String host = split[0];
// String port = split[1];
// HttpHost httpHost = new HttpHost(host, Integer.parseInt(port), "http");
// httpHostArr[i++] = httpHost;
// } catch (Exception e) {
// log.error(e.getMessage());
// }
// }
// RestClientBuilder builder = RestClient.builder(httpHostArr);
// this.restHighLevelClient = new RestHighLevelClient(builder);
// } catch (IOException e) {
// log.error(e.getMessage());
// }
// }
//
// /**
// * 创建索引
// *
// * @param index
// * @return
// */
// public boolean createIndex(String index) throws IOException {
// if (this.isIndexExist(index)) {
// log.error("Index is exits!");
// return false;
// }
// // 1.创建索引请求
// CreateIndexRequest request = new CreateIndexRequest(index);
// // 2.执行客户端请求
// CreateIndexResponse response = this.restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
// return response.isAcknowledged();
// }
//
// /**
// * 判断索引是否存在
// *
// * @param index
// * @return
// */
// public boolean isIndexExist(String index) throws IOException {
// GetIndexRequest request = new GetIndexRequest(index);
// return this.restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
// }
//
// /**
// * 删除索引
// *
// * @param index
// * @return
// */
// public boolean deleteIndex(String index) throws IOException {
// if (!this.isIndexExist(index)) {
// log.error("Index is not exits!");
// return false;
// }
// DeleteIndexRequest request = new DeleteIndexRequest(index);
// AcknowledgedResponse delete = this.restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
// return delete.isAcknowledged();
// }
//
// /**
// * 新增/更新数据
// *
// * @param object
// * 要新增/更新的数据
// * @param index
// * 索引，类似数据库
// * @param id
// * 数据ID
// * @return
// */
// public String submitData(Object object, String index, String id) throws IOException {
// if (null == id) {
// return this.addData(object, index);
// }
// if (this.existsById(index, id)) {
// return this.updateDataByIdNoRealTime(object, index, id);
// } else {
// return this.addData(object, index, id);
// }
// }
//
// /**
// * 新增数据，自定义id
// *
// * @param object
// * 要增加的数据
// * @param index
// * 索引，类似数据库
// * @param id
// * 数据ID,为null时es随机生成
// * @return
// */
// public String addData(Object object, String index, String id) throws IOException {
// if (null == id) {
// return this.addData(object, index);
// }
// if (this.existsById(index, id)) {
// return this.updateDataByIdNoRealTime(object, index, id);
// }
// // 创建请求
// IndexRequest request = new IndexRequest(index);
// request.id(id);
// request.timeout(TimeValue.timeValueSeconds(1));
// // 将数据放入请求 json
// request.source(JSON.toJSONString(object), XContentType.JSON);
// // 客户端发送请求
// IndexResponse response = this.restHighLevelClient.index(request, RequestOptions.DEFAULT);
// log.info("添加数据成功 索引为: {}, response 状态: {}, id为: {}", index, response.status().getStatus(), response.getId());
// return response.getId();
// }
//
// /**
// * 数据添加 随机id
// *
// * @param object
// * 要增加的数据
// * @param index
// * 索引，类似数据库
// * @return
// */
// public String addData(Object object, String index) throws IOException {
// return this.addData(object, index, UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
// }
//
// /**
// * 通过ID删除数据
// *
// * @param index
// * 索引，类似数据库
// * @param id
// * 数据ID
// * @return
// */
// public String deleteDataById(String index, String id) throws IOException {
// DeleteRequest request = new DeleteRequest(index, id);
// DeleteResponse deleteResponse = this.restHighLevelClient.delete(request, RequestOptions.DEFAULT);
// return deleteResponse.getId();
// }
//
// /**
// * 通过ID 更新数据
// *
// * @param object
// * 要更新数据
// * @param index
// * 索引，类似数据库
// * @param id
// * 数据ID
// * @return
// */
// public String updateDataById(Object object, String index, String id) throws IOException {
// UpdateRequest updateRequest = new UpdateRequest(index, id);
// updateRequest.timeout("1s");
// updateRequest.doc(JSON.toJSONString(object), XContentType.JSON);
// UpdateResponse updateResponse = this.restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
// log.info("索引为: {}, id为: {},updateResponseID：{}, 更新数据成功", index, id, updateResponse.getId());
// return updateResponse.getId();
// }
//
// /**
// * 通过ID 更新数据,保证实时性
// *
// * @param object
// * 要增加的数据
// * @param index
// * 索引，类似数据库
// * @param id
// * 数据ID
// * @return
// */
// public String updateDataByIdNoRealTime(Object object, String index, String id) throws IOException {
// // 更新请求
// UpdateRequest updateRequest = new UpdateRequest(index, id);
//
// // 保证数据实时更新
// updateRequest.setRefreshPolicy("wait_for");
//
// updateRequest.timeout("1s");
// updateRequest.doc(JSON.toJSONString(object), XContentType.JSON);
// // 执行更新请求
// UpdateResponse updateResponse = this.restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
// log.info("索引为: {}, id为: {},updateResponseID：{}, 实时更新数据成功", index, id, updateResponse.getId());
// return updateResponse.getId();
// }
//
// /**
// * 通过ID获取数据
// *
// * @param index
// * 索引，类似数据库
// * @param id
// * 数据ID
// * @param fields
// * 需要显示的字段，逗号分隔（缺省为全部字段）
// * @return
// */
// public Map<String, Object> searchDataById(String index, String id, String fields) throws IOException {
// GetRequest request = new GetRequest(index, id);
// if (StringUtils.isNotEmpty(fields)) {
// // 只查询特定字段。如果需要查询所有字段则不设置该项。
// request.fetchSourceContext(new FetchSourceContext(true, fields.split(","), Strings.EMPTY_ARRAY));
// }
// GetResponse response = this.restHighLevelClient.get(request, RequestOptions.DEFAULT);
// return response.getSource();
// }
//
// /**
// * 通过ID判断文档是否存在
// *
// * @param index
// * 索引，类似数据库
// * @param id
// * 数据ID
// * @return
// */
// public boolean existsById(String index, String id) throws IOException {
// GetRequest request = new GetRequest(index, id);
// // 不获取返回的_source的上下文
// request.fetchSourceContext(new FetchSourceContext(false));
// request.storedFields("_none_");
// return this.restHighLevelClient.exists(request, RequestOptions.DEFAULT);
// }
//
// /**
// * 批量插入false成功
// *
// * @param index
// * 索引，类似数据库
// * @param objects
// * 数据
// * @return
// */
// public boolean bulkPost(String index, List<?> objects) {
// BulkRequest bulkRequest = new BulkRequest();
// BulkResponse response = null;
// // 最大数量不得超过20万
// for (Object object : objects) {
// IndexRequest request = new IndexRequest(index);
// request.source(JSON.toJSONString(object), XContentType.JSON);
// bulkRequest.add(request);
// }
// try {
// response = this.restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
// } catch (IOException e) {
// e.printStackTrace();
// }
// return null != response && response.hasFailures();
// }
//
// /**
// * 获取低水平客户端
// *
// * @return
// */
// public RestClient getLowLevelClient() {
// return this.restHighLevelClient.getLowLevelClient();
// }
//
// /**
// * 高亮结果集 特殊处理 map转对象 JSONObject.parseObject(JSONObject.toJSONString(map), Content.class)
// *
// * @param searchResponse
// * @param highlightField
// */
// private List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse, String highlightField) {
// // 解析结果
// ArrayList<Map<String, Object>> list = new ArrayList<>();
// for (SearchHit hit : searchResponse.getHits().getHits()) {
// Map<String, HighlightField> high = hit.getHighlightFields();
// HighlightField title = high.get(highlightField);
// // 原来的结果
// Map<String, Object> sourceAsMap = hit.getSourceAsMap();
// // 解析高亮字段,将原来的字段换为高亮字段
// if (title != null) {
// Text[] texts = title.fragments();
// StringBuilder nTitle = new StringBuilder();
// for (Text text : texts) {
// nTitle.append(text);
// }
// // 替换
// sourceAsMap.put(highlightField, nTitle.toString());
// }
// list.add(sourceAsMap);
// }
// return list;
// }
//
// /**
// * 查询并分页
// *
// * @param index
// * 索引名称
// * @param query
// * 查询条件
// * @param highlightField
// * 高亮字段
// * @return
// */
// public List<Map<String, Object>> searchListData(String index, SearchSourceBuilder query, String highlightField)
// throws IOException {
// SearchRequest request = new SearchRequest(index);
//
// // 高亮
// HighlightBuilder highlight = new HighlightBuilder();
// highlight.field(highlightField);
// // 关闭多个高亮
// highlight.requireFieldMatch(false);
// highlight.preTags("<span style='color:red'>");
// highlight.postTags("</span>");
// query.highlighter(highlight);
// // 不返回源数据。只有条数之类的数据。
// // builder.fetchSource(false);
// request.source(query);
// SearchResponse response = this.restHighLevelClient.search(request, RequestOptions.DEFAULT);
// log.info("totalHits:" + response.getHits().getTotalHits());
// if (response.status().getStatus() == 200) {
// // 解析对象
// return this.setSearchResponse(response, highlightField);
// }
// return null;
// }
// }
