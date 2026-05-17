package cn.zuo.controller.user;

import cn.zuo.dto.feedbackdto.FeedbackDto;
import cn.zuo.dto.feedbackdto.FeedbackPageQueryDto;
import cn.zuo.entity.Feedback;
import cn.zuo.result.PageResult;
import cn.zuo.result.Result;
import cn.zuo.service.FeedbackService;
import cn.zuo.vo.feedbackvo.FeedbackMessageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户端-反馈接口
 *
 * 所有接口需要登录（JWT Bearer Token 在 Authorization 请求头）
 * 用户只能操作自己的反馈，操作他人反馈会返回 500 错误
 *
 * 统一响应格式:
 * 成功: { "code": 200, "message": "成功", "data": ... }
 * 失败: { "code": 500, "message": "错误描述", "data": null }
 */
@Slf4j
@RestController
@RequestMapping("/api/feedbacks")
@Tag(name = "用户反馈", description = "用户反馈提交与管理")
public class FeedbackController {

    @Resource
    private FeedbackService feedbackService;

    /**
     * 提交新反馈
     *
     * 请求头: Authorization: Bearer <token>
     * 请求体: { "type": "Bug报告|体验反馈|功能建议", "title": "...", "content": "..." }
     * 成功响应 data: { "feedbackId": 42, "message": "反馈提交成功，感谢您的反馈！" }
     */
    @PostMapping
    @Operation(summary = "提交反馈", description = "提交体验反馈、功能建议或Bug报告")
    public Result<FeedbackMessageVo> submitFeedback(@RequestBody FeedbackDto feedbackDto) {
        log.info("提交反馈: type={}", feedbackDto.getType());
        return Result.success(feedbackService.submitFeedback(feedbackDto));
    }

    /**
     * 获取当前用户的反馈列表（分页）
     *
     * 请求示例: GET /api/feedbacks?page=1&size=10&status=待处理
     * status 参数可选，不传或传空则查询所有状态
     * 成功响应 data: { "total": 50, "records": [Feedback...] }
     */
    @GetMapping
    @Operation(summary = "获取我的反馈列表", description = "分页获取当前用户的反馈列表，可按状态筛选")
    public Result<PageResult<Feedback>> getMyFeedbacks(FeedbackPageQueryDto queryDto) {
        log.info("获取我的反馈列表: page={}, size={}, status={}", queryDto.getPage(), queryDto.getSize(), queryDto.getStatus());
        return Result.success(feedbackService.getUserFeedbacks(queryDto));
    }

    /**
     * 获取单条反馈详情
     *
     * 仅能查看自己提交的反馈，查看他人反馈返回 500
     * 成功响应 data: Feedback 对象（包含所有字段）
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取反馈详情", description = "根据ID获取反馈详情，仅能查看自己的反馈")
    public Result<Feedback> getFeedbackDetail(
            @Parameter(description = "反馈ID") @PathVariable Long id) {
        log.info("获取反馈详情: id={}", id);
        return Result.success(feedbackService.getFeedbackById(id));
    }

    /**
     * 删除自己的反馈（物理删除）
     *
     * 仅能删除自己提交的反馈，删除他人反馈返回 500
     * 成功响应 data: "删除成功"
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除反馈", description = "删除自己的反馈")
    public Result<String> deleteFeedback(
            @Parameter(description = "反馈ID") @PathVariable Long id) {
        log.info("删除反馈: id={}", id);
        feedbackService.deleteFeedback(id);
        return Result.success("删除成功");
    }
}
