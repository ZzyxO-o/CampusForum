package cn.zuo.config.database;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class VectorInitConfiguration {

    @Resource
    private VectorStore vectorStore;
}
