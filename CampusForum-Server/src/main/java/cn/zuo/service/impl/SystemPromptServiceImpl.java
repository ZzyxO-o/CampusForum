package cn.zuo.service.impl;

import cn.zuo.entity.SystemPrompt;
import cn.zuo.mapper.SystemPromptMapper;
import cn.zuo.service.SystemPromptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SystemPromptServiceImpl extends ServiceImpl<SystemPromptMapper, SystemPrompt> implements SystemPromptService {
}
