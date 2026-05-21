package cn.zuo.controller.user;

import cn.zuo.dto.favoritedto.FavoriteDto;
import cn.zuo.dto.favoritedto.FavoritePageQueryDto;
import cn.zuo.entity.Favorite;
import cn.zuo.result.PageResult;
import cn.zuo.result.Result;
import cn.zuo.service.FavoriteService;
import cn.zuo.vo.favoritevo.FavoriteMessageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/favorites")
@Tag(name = "收藏管理", description = "收藏相关操作")
public class FavoriteController {

    @Resource
    private FavoriteService favoriteService;

    @PostMapping
    @Operation(summary = "收藏/取消收藏", description = "对讨论进行收藏或取消收藏")
    public Result<FavoriteMessageVo> toggleFavorite(@RequestBody FavoriteDto favoriteDto) {
        log.info("收藏/取消收藏: discussionId={}", favoriteDto.getDiscussionId());
        return Result.success(favoriteService.toggleFavorite(favoriteDto));
    }

    @GetMapping("/check")
    @Operation(summary = "检查是否已收藏", description = "检查用户是否已收藏该讨论")
    public Result<Boolean> checkFavorited(
            @Parameter(description = "讨论ID") @RequestParam Long discussionId) {
        log.debug("检查是否已收藏: discussionId={}", discussionId);
        boolean isFavorited = favoriteService.isFavorited(discussionId);
        return Result.success(isFavorited);
    }

    @GetMapping("/user")
    @Operation(summary = "获取用户收藏列表", description = "获取用户收藏的讨论列表")
    public Result<PageResult<Favorite>> getUserFavorites(FavoritePageQueryDto favoritePageQueryDto) {
        log.debug("获取用户收藏列表: userId={}", favoritePageQueryDto.getUserId());
        PageResult<Favorite> pageResult = favoriteService.getUserFavorites(favoritePageQueryDto);
        return Result.success(pageResult);
    }

    @GetMapping("/count")
    @Operation(summary = "获取收藏数", description = "获取讨论的收藏数量")
    public Result<Integer> getFavoriteCount(
            @Parameter(description = "讨论ID") @RequestParam Long discussionId) {
        log.debug("获取收藏数: discussionId={}", discussionId);
        int count = favoriteService.getFavoriteCount(discussionId);
        return Result.success(count);
    }
}