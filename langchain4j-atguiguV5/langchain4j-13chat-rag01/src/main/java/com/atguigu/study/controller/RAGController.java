package com.atguigu.study.controller;

import com.atguigu.study.service.ChatAssistant;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import dev.langchain4j.data.segment.TextSegment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @auther zzyybs@126.com
 * @Date 2025-06-02 21:05
 * @Description: https://docs.langchain4j.dev/tutorials/rag#rag-flavours-in-langchain4j
 */
@RestController
@Slf4j
public class RAGController {
    @Resource
    InMemoryEmbeddingStore<TextSegment> embeddingStore;

    @Resource
    ChatAssistant chatAssistant;

    // http://localhost:9013/rag/add
    @GetMapping(value = "/rag/add")
    public String testAdd() throws FileNotFoundException {
        //Document document = FileSystemDocumentLoader.loadDocument("D:\\44\\alibaba-java.docx");

        FileInputStream fileInputStream = new FileInputStream("D:\\44\\alibaba-java.docx");
        Document document = new ApacheTikaDocumentParser().parse(fileInputStream);

        EmbeddingStoreIngestor.ingest(document, embeddingStore);

        String result = chatAssistant.chat("错误码00000和A0001分别是什么");

        System.out.println(result);

        return result;
    }
}

