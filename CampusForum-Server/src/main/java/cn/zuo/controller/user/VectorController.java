package cn.zuo.controller.user;

import cn.zuo.service.VectorService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user/vector")
public class VectorController {

    @Resource
    private VectorService vectorService;
}
