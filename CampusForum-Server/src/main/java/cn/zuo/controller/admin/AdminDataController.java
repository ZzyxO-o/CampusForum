package cn.zuo.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/data")
@Slf4j
@Tag(name = "管理员数据管理", description = "管理员对系统数据的统计分析和管理操作")
public class AdminDataController {
}
