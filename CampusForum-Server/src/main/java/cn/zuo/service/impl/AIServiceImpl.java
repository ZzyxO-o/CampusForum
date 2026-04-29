package cn.zuo.service.impl;

import cn.zuo.dto.aidto.ChatDto;
import cn.zuo.dto.aidto.ChatMemoryDto;
import cn.zuo.entity.AiChatMemory;
import cn.zuo.entity.SystemPrompt;
import cn.zuo.mapper.AiChatMemoryMapper;
import cn.zuo.mapper.SystemPromptMapper;
import cn.zuo.service.AIService;
import cn.zuo.vo.chat.ChatMemoryVo;
import cn.zuo.vo.chat.ChatVO;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import com.alibaba.cloud.ai.memory.jdbc.MysqlChatMemoryRepository;
import com.alibaba.cloud.ai.memory.redis.RedissonRedisChatMemoryRepository;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.image.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Service
@Slf4j
public class AIServiceImpl implements AIService {

    @Resource(name = "SimpleDashScopeChatClient")
    private ChatClient simpleDashScopeChatClient;

    @Resource(name = "MaxDashScopeChatClient")
    private ChatClient maxDashScopeChatClient;

    @Resource
    private ImageModel imageModel;

    @Resource
    private MysqlChatMemoryRepository mysqlChatMemoryRepository;

    @Resource
    private RedissonRedisChatMemoryRepository redissonRedisChatMemoryRepository;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private SystemPromptMapper systemPromptMapper;

    @Resource
    private AiChatMemoryMapper aiChatMemoryMapper;
    /**
     * ai对话
     * @param chatDto
     * @return
     */
    @Override
    public ChatVO chat(ChatDto chatDto) {
        //查缓存中AI的系统提示词的占位符内容
        String systemPrompt = (String) redisTemplate.opsForValue().get("ChatClient_system_prompt" + chatDto.getPromptType());
        if (systemPrompt == null) {
            log.info("缓存中AI的系统提示词的占位符内容不存在,更新");
            QueryWrapper<SystemPrompt> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("prompt_type", chatDto.getPromptType());
            systemPrompt = systemPromptMapper.selectOne(queryWrapper).getPrompt();
            //缓存系统提示词
            redisTemplate.opsForValue().set("ChatClient_system_prompt" + chatDto.getPromptType(), systemPrompt);
        }

        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder()
                        .maxMessages(20)
                        //mysql持久化会话记忆
                        .chatMemoryRepository(mysqlChatMemoryRepository)
                        //redis持久化会话记忆
//                           .chatMemoryRepository(redissonRedisChatMemoryRepository)
                        .build())
                .conversationId(chatDto.getUserId() + "_" +chatDto.getSessionId())
                .build();
        ChatVO chatVo = maxDashScopeChatClient.prompt()
                .system(systemPrompt)
                .user(chatDto.getUserInput())
                .advisors(messageChatMemoryAdvisor)
                .call()
                .entity(new BeanOutputConverter<ChatVO>(ChatVO.class));
        log.info("AI回复：{}", chatVo);
        return chatVo;
    }

    /**
     * ai流式对话
     * @param chatDto
     * @return
     */
    @Override
    public Flux<String> chatStream(ChatDto chatDto) {
        //查缓存中AI的系统提示词的占位符内容
        String systemPrompt = (String) redisTemplate.opsForValue().get("simple_dashScope_chatClient_system_prompt" + chatDto.getPromptType());
        if (systemPrompt == null) {
            log.info("缓存中AI的系统提示词的占位符内容不存在,更新");
            QueryWrapper<SystemPrompt> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("prompt_type", chatDto.getPromptType());
            systemPrompt = systemPromptMapper.selectOne(queryWrapper).getPrompt();
            //缓存系统提示词
            redisTemplate.opsForValue().set("simple_dashScope_chatClient_system_prompt" + chatDto.getPromptType(), systemPrompt);
        }
        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder()
                        .maxMessages(20)
                        //mysql持久化会话记忆
                        .chatMemoryRepository(mysqlChatMemoryRepository)
                        //redis持久化会话记忆
//                        .chatMemoryRepository(redissonRedisChatMemoryRepository)
                        .build())
                .conversationId(chatDto.getUserId() + "_" +chatDto.getSessionId())
                .build();
        return maxDashScopeChatClient.prompt()
                .system(systemPrompt)
                .user(chatDto.getUserInput())
                .advisors(messageChatMemoryAdvisor)
                .stream()
                .content()
                .doOnNext(chunk -> log.debug("Streaming chunk: {}", chunk))
                .doOnError(error -> log.error("Streaming error", error))
                .doOnComplete(() -> log.debug("Streaming completed"))
                .onErrorReturn("data: 抱歉，AI服务暂时不可用，请稍后重试\n\n");
    }



    /**
     * ai图片生成
     * @param description
     * @return
     */
    @Override
    public String image(String description) {
        DashScopeImageOptions dashScopeImageOptions = DashScopeImageOptions.builder()
                .withHeight(1024)
                .withWidth(1024).build();
        ImagePrompt imagePrompt = new ImagePrompt(description, dashScopeImageOptions);
        ImageResponse imageResponse = imageModel.call(imagePrompt);
        String resultUrl = Optional.ofNullable(imageResponse)
                .map(ImageResponse::getResult)
                .map(ImageGeneration::getOutput)
                .map(Image::getUrl)
                .orElse("生成失败，请重试");
        return resultUrl;
    }

    @Override
    public ChatMemoryVo getChatMemory(ChatMemoryDto chatMemoryDto) {
        String conversationId = chatMemoryDto.getUserId() + "_" + chatMemoryDto.getSessionId();
        AiChatMemory aiChatMemory = aiChatMemoryMapper.selectById(conversationId);
        ChatMemoryVo chatMemoryVo = new ChatMemoryVo();
        chatMemoryVo.setConversationId(conversationId);
        chatMemoryVo.setContent(aiChatMemory.getContent());
        chatMemoryVo.setType(aiChatMemory.getType());
        chatMemoryVo.setTimestamp(aiChatMemory.getTimestamp());
        return chatMemoryVo;
    }
}
