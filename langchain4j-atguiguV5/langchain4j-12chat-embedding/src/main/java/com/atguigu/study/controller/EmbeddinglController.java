package com.atguigu.study.controller;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

/**
 * @auther zzyybs@126.com
 * @Date 2025-06-02 20:47
 * @Description: 知识出处，https://docs.langchain4j.dev/tutorials/rag#embedding-store
 */
@RestController
@Slf4j
public class EmbeddinglController {
    @Resource
    private EmbeddingModel embeddingModel;
    @Resource
    private QdrantClient qdrantClient;
    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;


    /**
     * 文本向量化测试，看看形成向量后的文本
     * http://localhost:9012/embedding/embed
     *
     * @return
     */
    @GetMapping(value = "/embedding/embed")
    public String embed() {
        String prompt = """
                   咏鸡
                鸡鸣破晓光，
                红冠映朝阳。
                金羽披霞彩，
                昂首步高岗。
                """;
        Response<Embedding> embeddingResponse = embeddingModel.embed(prompt);

        System.out.println(embeddingResponse);

        return embeddingResponse.content().toString();
    }

    /**
     * 新建向量数据库实例和创建索引：test-qdrant
     * 类似mysql create database test-qdrant
     * http://localhost:9012/embedding/createCollection
     */
    @GetMapping(value = "/embedding/createCollection")
    public void createCollection() {
        var vectorParams = Collections.VectorParams.newBuilder()
                .setDistance(Collections.Distance.Cosine)
                .setSize(1024)
                .build();
        qdrantClient.createCollectionAsync("test-qdrant", vectorParams);
    }

    /*
     往向量数据库新增文本记录
     */
    @GetMapping(value = "/embedding/add")
    public String add() {
        String prompt = """
                咏鸡
                鸡鸣破晓光，
                红冠映朝阳。
                金羽披霞彩，
                昂首步高岗。
                """;
        TextSegment segment1 = TextSegment.from(prompt);
        segment1.metadata().put("author", "zzyy");
        Embedding embedding1 = embeddingModel.embed(segment1).content();
        String result = embeddingStore.add(embedding1, segment1);

        System.out.println(result);

        return result;
    }

    @GetMapping(value = "/embedding/query1")
    public void query1() {
        Embedding queryEmbedding = embeddingModel.embed("咏鸡说的是什么").content();
        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(1)
                .build();
        EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(embeddingSearchRequest);
        System.out.println(searchResult.matches().get(0).embedded().text());
    }

    @GetMapping(value = "/embedding/query2")
    public void query2() {
        Embedding queryEmbedding = embeddingModel.embed("咏鸡").content();

        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .filter(metadataKey("author").isEqualTo("zzyy2"))
                .maxResults(1)
                .build();

        EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(embeddingSearchRequest);

        System.out.println(searchResult.matches().get(0).embedded().text());
    }
}

