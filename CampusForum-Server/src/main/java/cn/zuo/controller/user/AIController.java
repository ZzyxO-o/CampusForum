package cn.zuo.controller.user;

import cn.zuo.dto.aidto.ChatDto;
import cn.zuo.result.Result;
import cn.zuo.service.AIService;
import cn.zuo.service.AiChatMemoryService;
import cn.zuo.vo.chat.ChatMemoryVo;
import cn.zuo.vo.chat.ChatSessionsVo;
import cn.zuo.vo.chat.ChatVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public Result<List<ChatSessionsVo>> getUserSessions(@PathVariable Long userId) {return Result.success(aiChatMemoryService.getUserSessions(userId));}

    @GetMapping("/session/{conversationId}")
    @Operation(summary = "获取会话详情", description = "根据会话ID获取会话详情")
    public Result<List<ChatMemoryVo>> getSession(@PathVariable String conversationId) {
        return Result.success(aiChatMemoryService.getUserSessionDetail(conversationId));
    }
}