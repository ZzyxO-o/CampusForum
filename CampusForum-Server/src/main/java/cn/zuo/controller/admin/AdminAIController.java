package cn.zuo.controller.admin;

import cn.zuo.dto.aidto.SystemPromptDTO;
import cn.zuo.entity.AiChatMemory;
import cn.zuo.entity.SystemPrompt;
import cn.zuo.mapper.AiChatMemoryMapper;
import cn.zuo.result.Result;
import cn.zuo.service.SystemPromptService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ai")
@Slf4j
@Tag(name = "管理员AI管理", description = "管理员对AI功能的管理操作")
public class AdminAIController {

    @Resource
    private SystemPromptService systemPromptService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private AiChatMemoryMapper aiChatMemoryMapper;

    private static final String SYSTEM_PROMPT_CACHE_KEY = "ChatClient_system_prompt";

    @GetMapping("/stats")
    @Operation(summary = "AI使用统计", description = "获取AI功能的使用统计数据")
    public Result<Long> getAIUsageStats() {
        LambdaQueryWrapper<AiChatMemory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AiChatMemory::getType, "USER");
        Long totalCalls = aiChatMemoryMapper.selectCount(queryWrapper);
        return Result.success(totalCalls);
    }

    @GetMapping("/prompts")
    @Operation(summary = "获取系统提示词列表", description = "管理员查看所有系统提示词")
    public Result<List<SystemPrompt>> getSystemPrompts() {
        List<SystemPrompt> prompts = systemPromptService.list();
        return Result.success(prompts);
    }

    @PostMapping("/prompts")
    @Operation(summary = "管理系统提示词", description = "管理员新增或更新系统提示词")
    public Result<Void> manageSystemPrompt(@RequestBody SystemPromptDTO dto) {
        SystemPrompt prompt = new SystemPrompt();
        prompt.setPromptType(dto.getPromptType());
        prompt.setPrompt(dto.getPrompt());
        if (dto.getId() != null) {
            // 更新
            prompt.setId(dto.getId());
            systemPromptService.updateById(prompt);
            log.info("更新系统提示词，id: {}", dto.getId());
        } else {
            // 新增
            systemPromptService.save(prompt);
            log.info("新增系统提示词，type: {}", dto.getPromptType());
        }
        // 清除缓存，下次聊天时会重新加载
        redisTemplate.delete(SYSTEM_PROMPT_CACHE_KEY);
        return Result.success();
    }

    @DeleteMapping("/prompts/{id}")
    @Operation(summary = "删除系统提示词", description = "管理员删除指定系统提示词")
    public Result<Void> deleteSystemPrompt(@Parameter(description = "提示词ID") @PathVariable Integer id) {
        boolean removed = systemPromptService.removeById(id);
        if (!removed) {
            return Result.error("提示词不存在");
        }
        // 清除缓存
        redisTemplate.delete(SYSTEM_PROMPT_CACHE_KEY);
        log.info("删除系统提示词，id: {}", id);
        return Result.success();
    }
}
