package cn.zuo.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class ApiKeyProperties {
    @Value("${spring.ai.dashscope.api-key}")
    private String DashScopeApiKey;
}
