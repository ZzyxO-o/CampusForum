package cn.zuo.controller.admin;

import cn.zuo.result.Result;
import cn.zuo.service.DiscussionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/discussions")
@Slf4j
public class AdminDiscussionController {
    @Resource
    private DiscussionService discussionService;

    @DeleteMapping("/{discussionId}")
    @Operation(summary = "删除讨论", description = "删除讨论帖（软删除）")
    public Result<String> deleteDiscussion(@Parameter(description = "讨论ID") @PathVariable Long discussionId) {
        log.info("删除讨论: {}", discussionId);
        discussionService.adminDeleteDiscussion(discussionId);
        return Result.success("删除成功");
    }

    @PostMapping("/close/{discussionId}")
    @Operation(summary = "关闭讨论", description = "关闭讨论帖，禁止新回复")
    public Result<String> closeDiscussion(@Parameter(description = "讨论ID") @PathVariable Long discussionId) {
        log.info("关闭讨论: {}", discussionId);
        discussionService.closeDiscussion(discussionId);
        return Result.success("讨论已关闭");
    }
}
