package cn.zuo.controller.admin;

import cn.zuo.constant.businessConstant.FeedbackConstants;
import cn.zuo.dto.feedbackdto.FeedbackPageQueryDto;
import cn.zuo.dto.feedbackdto.FeedbackUpdateDto;
import cn.zuo.entity.Feedback;
import cn.zuo.result.PageResult;
import cn.zuo.result.Result;
import cn.zuo.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员端-反馈管理接口
 *
 * 所有接口需要管理员权限的JWT Token
 * 管理员可以查看所有用户的反馈并更新处理状态
 *
 * 统一响应格式:
 * 成功: { "code": 200, "message": "成功", "data": ... }
 * 失败: { "code": 500, "message": "错误描述", "data": null }
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/feedbacks")
public class AdminFeedbackController {

    @Resource
    private FeedbackService feedbackService;

    /**
     * 获取所有用户的反馈列表（分页，可按状态筛选）
     *
     * 请求示例: GET /api/admin/feedbacks?page=1&size=20&status=待处理
     * status 可选，不传则返回所有状态的反馈
     * 成功响应 data: { "total": 100, "records": [Feedback...] }
     * 注意: Feedback.userId 可用于关联到具体用户
     */
    @GetMapping
    @Operation(summary = "获取所有反馈列表", description = "管理员分页查看所有用户反馈，可按状态筛选")
    public Result<PageResult<Feedback>> getAllFeedbacks(FeedbackPageQueryDto queryDto) {
        log.info("管理员获取反馈列表: page={}, size={}, status={}", queryDto.getPage(), queryDto.getSize(), queryDto.getStatus());
        return Result.success(feedbackService.adminGetFeedbacks(queryDto));
    }

    /**
     * 查看任意反馈的详情（无权限限制）
     *
     * 成功响应 data: Feedback 对象
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取反馈详情", description = "管理员根据ID查看任意反馈详情")
    public Result<Feedback> getFeedbackDetail(
            @Parameter(description = "反馈ID") @PathVariable Long id) {
        log.info("管理员获取反馈详情: id={}", id);
        Feedback feedback = feedbackService.getById(id);
        return Result.success(feedback);
    }

    /**
     * 更新反馈的处理状态
     *
     * 请求: PUT /api/admin/feedbacks/5  body: { "status": "处理中" }
     * 状态流转建议: 待处理 → 处理中 → 已解决 → 已关闭
     * 成功响应 data: "反馈状态更新成功"
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新反馈状态", description = "管理员更新反馈的处理状态")
    public Result<String> updateFeedbackStatus(
            @Parameter(description = "反馈ID") @PathVariable Long id,
            @RequestBody FeedbackUpdateDto updateDto) {
        log.info("管理员更新反馈状态: id={}, status={}", id, updateDto.getStatus());
        updateDto.setId(id);
        feedbackService.updateFeedbackStatus(updateDto);
        return Result.success(FeedbackConstants.FEEDBACK_UPDATE_SUCCESS);
    }
}
