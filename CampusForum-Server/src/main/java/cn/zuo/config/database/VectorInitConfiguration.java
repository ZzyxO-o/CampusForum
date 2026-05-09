package cn.zuo.config.database;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class VectorInitConfiguration {

    @Resource
    private VectorStore vectorStore;

    /**
     * 初始化向量库
     */
    @PostConstruct
    public void init() {
        log.info("VectorInitConfig-向量数据库初始化完毕");
    }
}
