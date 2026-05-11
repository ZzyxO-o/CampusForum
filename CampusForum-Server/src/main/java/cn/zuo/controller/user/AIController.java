package cn.zuo.controller.user;

import cn.zuo.dto.aidto.ChatDto;
import cn.zuo.result.Result;
import cn.zuo.service.AIService;
import cn.zuo.service.AiChatMemoryService;
import cn.zuo.utils.ThreadLocalUtil;
import cn.zuo.vo.chat.ChatMemoryVo;
import cn.zuo.vo.chat.ChatSessionsVo;
import cn.zuo.vo.chat.ChatVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI聊天接口", description = "AI聊天相关接口")
public class AIController {

    @Resource
    private AIService aiService;

    @Resource
    private AiChatMemoryService aiChatMemoryService;

    @GetMapping("/chat")
    @Operation(summary = "AI对话", description = "与AI学习助手进行对话")
    public Result<ChatVO> chat(ChatDto chatDto) {
        ChatVO chatVO = aiService.chat(chatDto);
        return Result.success(chatVO);
    }

    @GetMapping("/chatStream")
    @Operation(summary = "AI流式对话", description = "与AI学习助手进行流式对话")
    public Flux<String> chatStream(ChatDto chatDto) {
        return aiService.chatStream(chatDto);
    }

    @GetMapping("/sessions/{userId}")
    @Operation(summary = "获取用户所有会话", description = "获取当前用户的AI会话")
    public Result<List<ChatSessionsVo>> getUserSessions(@PathVariable Long userId) {
        return Result.success(aiChatMemoryService.getUserSessions(userId));
    }

    @GetMapping("/session/{conversationId}")
    @Operation(summary = "获取会话详情", description = "根据会话ID获取会话详情")
    public Result<List<ChatMemoryVo>> getSession(@PathVariable String conversationId) {
        return Result.success(aiChatMemoryService.getUserSessionDetail(conversationId));
    }

    @PostMapping("/session")
    @Operation(summary = "新增对话", description = "创建一个新的AI对话会话，返回sessionId")
    public Result<Long> createSession() {
        return Result.success(aiChatMemoryService.createSession());
    }

    @DeleteMapping("/session/{sessionId}")
    @Operation(summary = "删除对话", description = "删除指定会话及其所有消息记录")
    public Result<Void> deleteSession(@PathVariable Long sessionId) {
        Long userId = ThreadLocalUtil.getCurrentId();
        aiChatMemoryService.deleteSession(userId, sessionId);
        return Result.success();
    }
}