package cn.zuo.controller.user;

import cn.zuo.dto.discussionsdto.DiscussionDto;
import cn.zuo.dto.discussionsdto.DiscussionPageQueryByUserDto;
import cn.zuo.dto.discussionsdto.DiscussionPageQueryDto;
import cn.zuo.dto.discussionsdto.DiscussionUpdateDto;
import cn.zuo.result.PageResult;
import cn.zuo.result.Result;
import cn.zuo.service.DiscussionService;
import cn.zuo.vo.discussionvo.DiscussionDetailVo;
import cn.zuo.vo.discussionvo.DiscussionMessageVo;
import cn.zuo.vo.discussionvo.HotTitleVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/discussions")
@Tag(name = "讨论管理", description = "讨论帖相关操作")
public class DiscussionController {

    @Resource
    private DiscussionService discussionService;

    @PostMapping
    @Operation(summary = "发布讨论", description = "创建新的讨论帖")
    public Result<DiscussionMessageVo> createDiscussion(@RequestBody DiscussionDto discussionDto) {
        log.info("发布讨论: {}", discussionDto);
        return Result.success(discussionService.createDiscussion(discussionDto));
    }

    @DeleteMapping("/{discussionId}")
    @Operation(summary = "删除讨论", description = "根据ID删除讨论帖")
    public Result<String> deleteDiscussion(@Parameter(description = "讨论ID") @PathVariable Long discussionId) {
        log.info("删除讨论: {}", discussionId);
        discussionService.userDeleteDiscussion(discussionId);
        return Result.success("删除成功");
    }

    @PutMapping
    @Operation(summary = "更新讨论", description = "修改讨论帖内容")
    public Result<String> updateDiscussion(@RequestBody DiscussionUpdateDto discussionUpdateDto) {
        log.info("更新讨论: {}", discussionUpdateDto);
        discussionService.updateDiscussion(discussionUpdateDto);
        return Result.success("更新成功");
    }

    @GetMapping
    @Operation(summary = "获取最新讨论列表", description = "分页获取最新讨论帖列表，支持搜索和筛选")
    public Result<PageResult> getDiscussions(DiscussionPageQueryDto queryDto) {
        log.info("获取最新讨论列表: {}", queryDto);
        return Result.success(discussionService.pageQueryDiscussions(queryDto));
    }

    @GetMapping("/hot")
    @Operation(summary = "获取热门讨论列表", description = "分页获取热门讨论帖列表（按浏览量和回复数排序）")
    public Result<PageResult> getHotDiscussions(DiscussionPageQueryDto discussionPageQueryDto) {
        log.info("获取热门讨论列表: {}", discussionPageQueryDto);
        PageResult pageResult = discussionService.getHotDiscussions(discussionPageQueryDto);
        return Result.success(pageResult);
    }


    @GetMapping("/{discussionId}")
    @Operation(summary = "获取讨论详情", description = "根据ID获取讨论帖详情，包含回复列表")
    public Result<DiscussionDetailVo> getDiscussionDetail(@Parameter(description = "讨论ID") @PathVariable Long discussionId) {
        log.info("获取讨论详情: {}", discussionId);
        return Result.success(discussionService.getDiscussionDetailById(discussionId));
    }

    @GetMapping("/hotTitles")
    @Operation(summary = "获取热门标题", description = "获取热门讨论标题列表")
    public Result<PageResult<HotTitleVo>> getHotTitles(){
        log.info("获取热门标题");
        PageResult<HotTitleVo> hotTitleVos = discussionService.getHotTitles();
        return Result.success(hotTitleVos);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户讨论", description = "获取指定用户发布的所有讨论")
    public Result<PageResult> getUserDiscussions(DiscussionPageQueryByUserDto discussionPageQueryByUserDto) {
        log.info("获取用户讨论列表: {}", discussionPageQueryByUserDto);
        return Result.success(discussionService.getUserDiscussions(discussionPageQueryByUserDto));
    }
}