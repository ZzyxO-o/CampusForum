package cn.zuo.config.agent;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatModelConfiguration {

    @Resource
    private DashScopeApi dashScopeApi;

    @Bean("SimpleDashScopeChatModel")
    public ChatModel getSimpleDashScopeChatModel() {
        return DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .defaultOptions(DashScopeChatOptions.builder()
                        .withModel(DashScopeChatModel.DEFAULT_MODEL_NAME)
                        .withTemperature(0.5)
                        .withMaxToken(1000)
                        .build())
                .build();
    }

    @Bean("MaxDashScopeChatModel")
    public ChatModel GetMaxDashScopeChatModel() {
        return DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .defaultOptions(DashScopeChatOptions.builder()
                        .withModel("qwen3-max")
                        .withTemperature(0.5)
                        .withMaxToken(1000)
                        .build())
                .build();
    }
}
