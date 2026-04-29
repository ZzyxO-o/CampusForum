package cn.zuo.controller.user;

import cn.zuo.result.Result;
import cn.zuo.utils.AliOssUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@RequestMapping("/api/common")
@RestController
@Tag(name = "通用接口", description = "通用功能接口")
public class CommonController {

    @Resource
    private AliOssUtil aliOssUtil;
    /**
     * 文件上传接口
     * 支持上传图片、文档等各种类型的文件到阿里云OSS
     *
     * @param file 上传的文件（MultipartFile对象）
     * @return 文件访问URL
     */
    @PostMapping("upload")
    @Operation(
        summary = "文件上传",
        description = "上传文件到阿里云OSS存储，支持图片、文档等各种文件类型。文件名会自动生成UUID避免重复。"
    )
    public Result fileUpload(@Parameter(description = "上传的文件，支持图片、文档等各种类型", required = true, example = "avatar.jpg") MultipartFile file) throws Exception {
        //完成文件上传
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        log.info("原始文件名:{}", originalFilename);
        //获取文件后缀名
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = UUID.randomUUID() + substring;
        String url = null;
        url = aliOssUtil.upload(file.getBytes(), objectName);
        return Result.success(url);
    }
}