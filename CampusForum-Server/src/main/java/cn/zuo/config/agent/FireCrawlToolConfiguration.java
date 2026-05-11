package cn.zuo.config.agent;

import com.alibaba.cloud.ai.toolcalling.firecrawl.FireCrawlService;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FireCrawlToolConfiguration {

    @Resource
    private FireCrawlService fireCrawlService;

    @Bean(name = "firecrawlTool")
    public ToolCallback toolCallback() {
        ToolCallback toolCallback = FunctionToolCallback
                .builder("在线网页数据读取", fireCrawlService)
                .description("在线网页数据读取工具")
                .inputType(FireCrawlService.Request.class)
                .build();
        return toolCallback;
    }
}
