package cn.zuo.service.impl;

import cn.zuo.service.VectorService;
import com.alibaba.cloud.ai.reader.poi.PoiDocumentReader;
import com.alibaba.cloud.ai.transformer.splitter.RecursiveCharacterTextSplitter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
public class VectorServiceImpl implements VectorService {

    @Resource
    private VectorStore vectorStore;

    @Override
    public void uploadDocument(MultipartFile file) throws Exception {
        org.springframework.core.io.Resource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        // 将 MultipartFile 包装为 Spring Resource，保留文件名供 POI 判断格式
        // 读取 Word 文档内容
        PoiDocumentReader reader = new PoiDocumentReader(resource);
        List<Document> documents = reader.get();
        // 按 character 分块
        List<Document> chunks = new RecursiveCharacterTextSplitter().apply(documents);
        // 向量化并存入 Qdrant，每批最多 10 个文本块
        int batchSize = 10;
        for (int i = 0; i < chunks.size(); i += batchSize) {
            List<Document> batch = chunks.subList(i, Math.min(i + batchSize, chunks.size()));
            vectorStore.add(batch);
        }
        log.info("文件 {} 嵌入完成，共 {} 个文本块", file.getOriginalFilename(), chunks.size());
    }
}
