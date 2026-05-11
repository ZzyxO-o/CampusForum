package cn.zuo.config.agent;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatClientConfiguration {

    @Resource(name = "MaxDashScopeChatModel")
    private ChatModel maxDashScopeChatModel;

    @Autowired
    private List<ToolCallback> toolCallbacks;

    @Bean("MaxDashScopeChatClient")
    public ChatClient getMaxDashScopeChatClient() {
        return ChatClient.builder(maxDashScopeChatModel)
                .defaultToolCallbacks(toolCallbacks)
                .build();
    }
}
