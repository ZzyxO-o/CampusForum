package cn.zuo.controller.admin;

import cn.zuo.result.Result;
import cn.zuo.service.VectorService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/admin/vector")
public class AdminVectorController {

    @Resource
    private VectorService vectorService;

    @PostMapping("/uploadDocument")
    public Result<String> uploadDocument(@RequestParam MultipartFile file) throws Exception {
        vectorService.uploadDocument(file);
        return Result.success("文件嵌入完成");
    }
}
