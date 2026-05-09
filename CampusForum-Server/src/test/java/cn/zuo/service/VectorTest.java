package cn.zuo.service;

import com.alibaba.cloud.ai.reader.poi.PoiDocumentReader;
import com.alibaba.cloud.ai.transformer.splitter.RecursiveCharacterTextSplitter;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootTest
public class VectorTest {

    @Autowired
    private VectorStore vectorStore;

    @Value("classpath:word/Java基础.docx")
    private Resource resource;

    @Test
    public void testVectorStore() {
        PoiDocumentReader reader = new PoiDocumentReader(resource);
        List<Document> documents = reader.get();
        RecursiveCharacterTextSplitter splitter = new RecursiveCharacterTextSplitter();
        List<Document> texts = splitter.split(documents);
        vectorStore.add(texts);
    }
}
