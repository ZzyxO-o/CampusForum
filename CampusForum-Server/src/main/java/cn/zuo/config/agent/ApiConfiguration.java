package cn.zuo.config.agent;

import cn.zuo.properties.ApiKeyProperties;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfiguration {

    @Resource
    private ApiKeyProperties apiKeyProperties;

    @Bean
    public DashScopeApi dashScopeApi() {
        return DashScopeApi.builder()
                .apiKey(apiKeyProperties.getDashScopeApiKey())
                .build();
    }


}
