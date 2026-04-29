package cn.zuo.controller.user;

import cn.zuo.dto.replydto.ReplyDto;
import cn.zuo.dto.replydto.ReplyPageQueryByDiscussionDto;
import cn.zuo.dto.replydto.ReplyUpdateDto;
import cn.zuo.entity.Reply;
import cn.zuo.result.PageResult;
import cn.zuo.result.Result;
import cn.zuo.service.ReplyService;
import cn.zuo.vo.replyvo.ReplyMessageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/replies")
@Tag(name = "回复管理", description = "回复相关操作")
public class ReplyController {

    @Resource
    private ReplyService replyService;

    @PostMapping
    @Operation(summary = "添加回复", description = "添加新的回复")
    public Result<ReplyMessageVo> createReply(@RequestBody ReplyDto replyDto) {
        log.info("添加回复: {}", replyDto);
        return Result.success(replyService.createReply(replyDto));
    }

    @DeleteMapping("/{replyId}")
    @Operation(summary = "删除回复", description = "删除回复（软删除）")
    public Result<String> deleteReply(@Parameter(description = "回复ID") @PathVariable Long replyId) {
        log.info("删除回复: {}", replyId);
        replyService.deleteReply(replyId);
        return Result.success("删除成功");
    }

    @PutMapping
    @Operation(summary = "更新回复", description = "修改回复内容")
    public Result<String> updateReply(@RequestBody ReplyUpdateDto replyUpdateDto) {
        log.info("更新回复: {}", replyUpdateDto);
        replyService.updateReply(replyUpdateDto);
        return Result.success("更新成功");
    }

    @PostMapping("/{replyId}/hide")
    @Operation(summary = "隐藏回复", description = "隐藏回复内容")
    public Result<String> hideReply(@Parameter(description = "回复ID") @PathVariable Long replyId) {
        log.info("隐藏回复: {}", replyId);
        replyService.hideReply(replyId);
        return Result.success("回复已隐藏");
    }

    @PostMapping("/{replyId}/show")
    @Operation(summary = "显示回复", description = "重新显示已隐藏的回复")
    public Result<String> showReply(@Parameter(description = "回复ID") @PathVariable Long replyId) {
        log.info("显示回复: {}", replyId);
        replyService.showReply(replyId);
        return Result.success("回复已显示");
    }

    @GetMapping("/discussion")
    @Operation(summary = "获取讨论回复", description = "获取指定讨论的所有回复")
    public Result<PageResult> getDiscussionReplies(ReplyPageQueryByDiscussionDto replyPageQueryByDiscussionDto) {
        log.info("获取讨论回复: {}", replyPageQueryByDiscussionDto);
        return Result.success(replyService.getDiscussionReplies(replyPageQueryByDiscussionDto));
    }

    @GetMapping("/{replyId}/children")
    @Operation(summary = "获取子回复", description = "获取指定回复的所有子回复")
    public Result<List<Reply>> getChildReplies(@Parameter(description = "回复ID") @PathVariable Long replyId) {
        log.info("获取子回复: {}", replyId);
        return Result.success(replyService.getChildReplies(replyId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户回复", description = "获取指定用户的所有回复")
    public Result<PageResult> getUserReplies(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            ReplyPageQueryByDiscussionDto replyPageQueryByDiscussionDto) {
        replyPageQueryByDiscussionDto.setUserId(userId);
        log.info("获取用户回复: {}", replyPageQueryByDiscussionDto);
        return Result.success(replyService.getUserReplies(replyPageQueryByDiscussionDto));
    }

    @GetMapping("/top")
    @Operation(summary = "热门回复", description = "获取热门回复（按点赞数或回复数）")
    public Result<List<Reply>> getTopReplies(
            @Parameter(description = "数量") @RequestParam(defaultValue = "10") int limit) {
        log.info("获取热门回复: {}", limit);
        return Result.success(replyService.getTopReplies(limit));
    }
}