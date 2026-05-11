package cn.zuo.config.agent;

import com.alibaba.cloud.ai.toolcalling.baidusearch.BaiduSearchService;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaiDuToolConfiguration {
    @Resource
    private BaiduSearchService baiduSearchService;

    @Bean(name = "baiduSearchTool")
    public ToolCallback toolCallback() {
        ToolCallback toolCallback = FunctionToolCallback
                .builder("百度搜索", baiduSearchService)
                .description("百度搜索工具")
                .inputType(BaiduSearchService.Request.class)
                .build();

        return toolCallback;
    }
}
