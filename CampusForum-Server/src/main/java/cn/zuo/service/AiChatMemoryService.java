package cn.zuo.service;

import cn.zuo.entity.AiChatMemory;
import cn.zuo.vo.chat.ChatMemoryVo;
import cn.zuo.vo.chat.ChatSessionsVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface AiChatMemoryService extends IService<AiChatMemory> {
    List<ChatSessionsVo> getUserSessions(Long userId);

    List<ChatMemoryVo> getUserSessionDetail(String conversationId);
}
