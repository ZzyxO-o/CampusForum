package cn.zuo.config.agent;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfiguration {

    @Resource(name = "SimpleDashScopeChatModel")
    private ChatModel simpleDashScopeChatModel;

    @Resource(name = "MaxDashScopeChatModel")
    private ChatModel maxDashScopeChatModel;

    @Bean("SimpleDashScopeChatClient")
    public ChatClient getSimpleDashScopeChatClient() {
        return ChatClient.builder(simpleDashScopeChatModel)
                .defaultSystem("你是校园论坛的助手,你叫小Z")
                .build();
    }

    @Bean("MaxDashScopeChatClient")
    public ChatClient getMaxDashScopeChatClient() {
        return ChatClient.builder(maxDashScopeChatModel)
                .defaultSystem("你是校园论坛的助手,你叫小Z")
                .build();
    }
}
