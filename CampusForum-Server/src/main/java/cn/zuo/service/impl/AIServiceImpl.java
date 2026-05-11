package cn.zuo.service.impl;

import cn.zuo.dto.aidto.ChatDto;
import cn.zuo.dto.aidto.ChatMemoryDto;
import cn.zuo.entity.SystemPrompt;
import cn.zuo.mapper.SystemPromptMapper;
import cn.zuo.service.AIService;
import cn.zuo.service.DiscussionService;
import cn.zuo.vo.chat.ChatMemoryVo;
import cn.zuo.vo.chat.ChatVO;
import com.alibaba.cloud.ai.memory.jdbc.MysqlChatMemoryRepository;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class AIServiceImpl implements AIService {

    @Resource(name = "MaxDashScopeChatClient")
    private ChatClient maxDashScopeChatClient;

    @Resource
    private MysqlChatMemoryRepository mysqlChatMemoryRepository;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private SystemPromptMapper systemPromptMapper;

    @Resource
    private VectorStore vectorStore;

    @Resource
    private DiscussionService discussionService;

    private MessageWindowChatMemory chatMemory;

    private Advisor retrievalAugmentationAdvisor;

    @PostConstruct
    public void init() {
        chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(20)
                .chatMemoryRepository(mysqlChatMemoryRepository)
                .build();

        retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.50)
                        .vectorStore(vectorStore)
                        .build())
                .queryAugmenter(ContextualQueryAugmenter.builder()
                        .allowEmptyContext(true)
                        .build())
                .build();
    }

    private String getSystemPrompt() {
        String systemPrompt = (String) redisTemplate.opsForValue().get("ChatClient_system_prompt");
        if (systemPrompt == null) {
            log.info("缓存中AI的系统提示词的占位符内容不存在,更新");
            QueryWrapper<SystemPrompt> queryWrapper = new QueryWrapper<>();
            SystemPrompt record = systemPromptMapper.selectOne(queryWrapper);
            if (record == null) {
                return "你是HUT校园论坛的AI助手";
            }
            systemPrompt = record.getPrompt();
            redisTemplate.opsForValue().set("ChatClient_system_prompt", systemPrompt);
        }
        return systemPrompt;
    }

    private MessageChatMemoryAdvisor buildMemoryAdvisor(ChatDto chatDto) {
        return MessageChatMemoryAdvisor.builder(chatMemory)
                .conversationId(chatDto.getUserId() + "_" + chatDto.getSessionId())
                .build();
    }

    @Override
    public ChatVO chat(ChatDto chatDto) {
        ChatVO chatVo = maxDashScopeChatClient.prompt()
                .system(getSystemPrompt())
                .user(chatDto.getUserInput())
                .advisors(buildMemoryAdvisor(chatDto), retrievalAugmentationAdvisor)
                .tools(discussionService)
                .call()
                .entity(new BeanOutputConverter<ChatVO>(ChatVO.class));
        log.info("AI回复：{}", chatVo);
        return chatVo;
    }

    @Override
    public Flux<String> chatStream(ChatDto chatDto) {
        return maxDashScopeChatClient.prompt()
                .system(getSystemPrompt())
                .user(chatDto.getUserInput())
                .advisors(buildMemoryAdvisor(chatDto), retrievalAugmentationAdvisor)
                .tools(discussionService)
                .stream()
                .content()
                .doOnNext(chunk -> log.debug("Streaming chunk: {}", chunk))
                .doOnError(error -> log.error("Streaming error", error))
                .doOnComplete(() -> log.debug("Streaming completed"))
                .onErrorReturn("data: 抱歉，AI服务暂时不可用，请稍后重试\n\n");
    }

    @Override
    public ChatMemoryVo getChatMemory(ChatMemoryDto chatMemoryDto) {
        throw new UnsupportedOperationException("请使用 AiChatMemoryService.getUserSessionDetail()");
    }

}