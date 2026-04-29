package cn.zuo.controller.user;

import cn.zuo.dto.likedto.LikeDto;
import cn.zuo.dto.likedto.LikePageQueryDto;
import cn.zuo.entity.Like;
import cn.zuo.result.PageResult;
import cn.zuo.result.Result;
import cn.zuo.service.LikeService;
import cn.zuo.vo.likevo.LikeMessageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/likes")
@Tag(name = "点赞管理", description = "点赞相关操作")
public class LikeController {

    @Resource
    private LikeService likeService;

    @PostMapping
    @Operation(summary = "点赞/取消点赞", description = "对讨论或回复进行点赞或取消点赞")
    public Result<LikeMessageVo> toggleLike(@RequestBody LikeDto likeDto) {
        log.info("点赞/取消点赞: {}", likeDto);
        return Result.success(likeService.toggleLike(likeDto));
    }

    @GetMapping("/check")
    @Operation(summary = "检查是否已点赞", description = "检查用户是否已对目标点赞")
    public Result<Boolean> checkLiked(LikeDto likeDto) {
        log.info("检查是否已点赞: {}", likeDto);
        boolean isLiked = likeService.isLiked(likeDto);
        return Result.success(isLiked);
    }

    @GetMapping("/count")
    @Operation(summary = "获取点赞数", description = "获取目标的点赞数量")
    public Result<Integer> getLikeCount(
            @Parameter(description = "目标类型") @RequestParam String targetType,
            @Parameter(description = "目标ID") @RequestParam Long targetId) {
        log.info("获取点赞数: targetType={}, targetId={}", targetType, targetId);
        return Result.success(likeService.getLikeCount(targetType, targetId));
    }

    @GetMapping("/user/discussion")
    @Operation(summary = "获取用户点赞列表", description = "获取用户点赞的讨论目标ID列表")
    public Result<PageResult<Like>> getUserLikes(LikePageQueryDto queryDto) {
        log.info("获取用户点赞讨论列表: {}", queryDto);
        PageResult<Like> pageResult = likeService.getUserLikes(queryDto);
        return Result.success(pageResult);
    }
}