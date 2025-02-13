package com.example.demo.demo.database.service.es;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.example.demo.infruastructure.util.IdGenerator;
import com.example.demo.user.dto.UserInfoDto;
import com.example.demo.user.param.UserInfoParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ElasticsearchTestService {

    @Resource
    private RestHighLevelClient restHighLevelClient;


    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private UserRepository userRepository;
    @Resource
    private IdGenerator idGenerator;

    public UserInfoDto insertTest(UserInfoParam param) {
        UserInfoEs userInfoEs = BeanUtil.copyProperties(param, UserInfoEs.class);
        userInfoEs.setId(idGenerator.nextId());
        userInfoEs.setCreateTime(LocalDateTime.now());
        userInfoEs.setDeleted(1);
        UserInfoEs save = userRepository.save(userInfoEs);
        return BeanUtil.copyProperties(save, UserInfoDto.class);
    }

    public UserInfoDto findByIdTest() {
//        userRepository.deleteAll();
//        Optional<UserInfoEs> byId = userRepository.findById(1889859493598408704L);
        List<UserInfoEs> ljptest = userRepository.findByUserName("ljptest1");
        UserInfoEs userInfoEs = ljptest.get(0);
        List<UserInfoEs> byCreateTimeBetween = userRepository.findByCreateTimeBetween(userInfoEs.getCreateTime().minusDays(1), userInfoEs.getCreateTime().plusDays(1));
        List<UserInfoEs> byUserNameOrUserPwd = userRepository.findByUserNameOrUserPwd("ljptest", "password");
        return null;
    }


    public List<UserInfoEs> findByParamTest() {
        // builder
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        // ******************************** 条件 ********************************
        // 查询（如果是keyword，则是等于完全匹配）
//        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("userName", "ljptest"));
        // 精确查询（不分词） term 查询是基于精确值
//        nativeSearchQueryBuilder.withQuery(QueryBuilders.termQuery("userPwd", "password"));
        // 分词查询
//        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("userPwd", "password"));
        // 模糊匹配
//        nativeSearchQueryBuilder.withQuery(QueryBuilders.wildcardQuery("userPwd", "password*"));
        // 正则匹配
//        nativeSearchQueryBuilder.withQuery(QueryBuilders.regexpQuery("userPwd", ".*ljptest.*"));
        // 范围
//        nativeSearchQueryBuilder.withQuery(QueryBuilders.rangeQuery("createTime").gte("2025-02-13 14:16:21").lte("2025-02-13 14:16:23"));

        // boolQuery 可以组合多个查询条件，must 表示必须满足的条件，should 表示可选条件，must_not 表示必须不满足的条件
//        nativeSearchQueryBuilder.withQuery(QueryBuilders.boolQuery()
//                .must(QueryBuilders.matchQuery("userPwd", "password"))
//                .filter(QueryBuilders.rangeQuery("createTime").lte("2025-02-13 14:16:21")));
        // 用于查询与某个值相似的字段，常用于容忍拼写错误的场景。
//        nativeSearchQueryBuilder.withQuery(QueryBuilders.fuzzyQuery("userName", "lxptest datw"));

        // 查询后高亮提示
//        HighlightBuilder highlightBuilder = new HighlightBuilder()
//                .field("userName").preTags("<em>").postTags("</em>");
//        nativeSearchQueryBuilder.withQuery(QueryBuilders.fuzzyQuery("userName", "lxptest datw"))
//                .withHighlightFields(highlightBuilder.fields());
//        List<UserInfoEs> collect1 = elasticsearchOperations.search(nativeSearchQueryBuilder.build(), UserInfoEs.class)
//                .stream().map(searchHit -> {
//                    UserInfoEs content = searchHit.getContent();
//                    Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
//                    // 获取高亮片段
//                    if (highlightFields != null && highlightFields.containsKey("userName")) {
//                        List<String> highlightedUserName = highlightFields.get("userName");
//                        content.setUserName(String.join(" ", highlightedUserName));
//                    }
//                    return content;
//                }).collect(Collectors.toList());

        //  用于控制哪些字段需要返回，哪些不需要返回
        nativeSearchQueryBuilder.withQuery(QueryBuilders.wildcardQuery("userPwd", "password*"))
                .withSourceFilter(new FetchSourceFilter(new String[]{"id", "userName"}, null));
        // ******************************** 条件 ********************************

        // 执行查询
        List<UserInfoEs> collect = elasticsearchOperations.search(nativeSearchQueryBuilder.build(), UserInfoEs.class)
                .stream()
                .map(searchHit -> searchHit.getContent())
                .collect(Collectors.toList());
        return collect;
    }


    // ********************************* restHighLevelClient 已过时 *********************************
    public void useClient(Runnable action) throws IOException {
        useClient(() -> {
            action.run();
            return true;
        });
    }

    public <T> T useClient(Supplier<T> action) throws IOException {
        try {
            // 获取分布式锁，5ms自旋一次，有效期60S
            HttpHost host = HttpHost.create("http://localhost:9200");
            RestClientBuilder builder = RestClient.builder(host);
            restHighLevelClient = new RestHighLevelClient(builder);
            return action.get();
        } catch (Exception e) {
            log.error("error msg:", e);
            throw new RuntimeException("网络异常");
        } finally {
            restHighLevelClient.close();
        }
    }

    public void testEs() throws IOException {
        useClient(() -> {
            try {
                CreateIndexRequest request = new CreateIndexRequest("user");
                restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void testSearch() throws IOException {
        useClient(() -> {
            try {
                SearchRequest request = new SearchRequest("userinfoes_index");
                SearchSourceBuilder builder = new SearchSourceBuilder();
                builder.query(QueryBuilders.termQuery("userName", "ljptest"));
                request.source(builder);
                SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
                org.elasticsearch.search.SearchHits hits = response.getHits();
                for (org.elasticsearch.search.SearchHit hit : hits) {
                    String source = hit.getSourceAsString();
                    UserInfoEs userInfoEs = JSON.parseObject(source, UserInfoEs.class);
                    System.out.println(userInfoEs);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
