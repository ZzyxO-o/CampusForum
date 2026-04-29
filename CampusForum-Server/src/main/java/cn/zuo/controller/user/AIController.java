package cn.zuo.controller.user;

import cn.zuo.dto.aidto.ChatDto;
import cn.zuo.dto.aidto.ChatMemoryDto;
import cn.zuo.result.Result;
import cn.zuo.service.AIService;
import cn.zuo.vo.chat.ChatMemoryVo;
import cn.zuo.vo.chat.ChatVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI聊天接口", description = "AI聊天相关接口")
public class AIController {

    @Resource
    private AIService aiService;

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

    @GetMapping("/chatMemory")
    @Operation(summary = "获取AI对话记录", description = "获取AI对话助手的对话记录")
    public Result getChatMemory(ChatMemoryDto chatMemoryDto) {
        ChatMemoryVo chatMemoryVo = aiService.getChatMemory(chatMemoryDto);
        return Result.success();
    }

    @GetMapping("/image/{description}")
    @Operation(summary = "AI图片生成", description = "与AI学习助手进行图片生成")
    public Result<String> image(@Parameter(description = "图片生成描述") @PathVariable String description) {
        log.info("图片生成描述：{}", description);
        String imageUrl = aiService.image(description);
        return Result.success(imageUrl);
    }

}