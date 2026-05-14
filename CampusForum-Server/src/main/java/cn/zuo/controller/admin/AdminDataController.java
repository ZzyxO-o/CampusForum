package cn.zuo.controller.admin;

import cn.zuo.result.Result;
import cn.zuo.service.DiscussionService;
import cn.zuo.service.UserService;
import cn.zuo.vo.admin.CategoryStatVo;
import cn.zuo.vo.admin.DailyStatVo;
import cn.zuo.vo.admin.NewStatsVo;
import cn.zuo.vo.uservo.UserOverviewDataVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/data")
@Slf4j
@Tag(name = "管理员数据管理", description = "管理员对系统数据的统计分析和管理操作")
public class AdminDataController {

    @Resource
    private DiscussionService discussionService;

    @Resource
    private UserService userService;


    @GetMapping("/overview")
    @Operation(summary = "系统总览", description = "获取系统核心数据总览（总用户数、总帖子数）")
    public Result<UserOverviewDataVo> getSystemOverview() {
        return Result.success(userService.getSystemOverview());
    }

    @GetMapping("/new")
    @Operation(summary = "新增数据统计", description = "获取最近N天新增用户、帖子、回复的汇总统计")
    public Result<NewStatsVo> getNewStats(
            @Parameter(description = "统计天数") @RequestParam(defaultValue = "7") Integer days) {
        return Result.success(userService.getNewStats(days));
    }

    @GetMapping("/categories")
    @Operation(summary = "分类统计", description = "获取各分类的帖子数量统计")
    public Result<List<CategoryStatVo>> getCategoryStats() {
        List<CategoryStatVo> stats = discussionService.getCategoryStats();
        return Result.success(stats);
    }

    @GetMapping("/trend")
    @Operation(summary = "每日趋势", description = "获取最近N天每天的新增用户、帖子、回复明细，用于折线图")
    public Result<List<DailyStatVo>> getTrend(
            @Parameter(description = "统计天数") @RequestParam(defaultValue = "7") Integer days) {
        return Result.success(userService.getTrend(days));
    }
}
