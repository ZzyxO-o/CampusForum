package cn.zuo.service.impl;

import cn.zuo.entity.AiChatMemory;
import cn.zuo.mapper.AiChatMemoryMapper;
import cn.zuo.service.AiChatMemoryService;
import cn.zuo.vo.chat.ChatMemoryVo;
import cn.zuo.vo.chat.ChatSessionsVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AiChatMemoryServiceImpl extends ServiceImpl<AiChatMemoryMapper, AiChatMemory> implements AiChatMemoryService {

    @Resource
    private AiChatMemoryMapper aiChatMemoryMapper;

    @Override
    public List<ChatSessionsVo> getUserSessions(Long userId) {
        LambdaQueryWrapper<AiChatMemory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(AiChatMemory::getConversationId, userId + "_")
                .eq(AiChatMemory::getType, "USER")
                .orderByAsc(AiChatMemory::getTimestamp);

        List<AiChatMemory> memories = aiChatMemoryMapper.selectList(queryWrapper);

        Map<String, List<AiChatMemory>> grouped = memories.stream()
                .collect(Collectors.groupingBy(AiChatMemory::getConversationId));

        return grouped.entrySet().stream()
                .map(entry -> {
                    String conversationId = entry.getKey();
                    String sessionId = conversationId.substring(conversationId.indexOf("_") + 1);
                    AiChatMemory first = entry.getValue().get(0);
                    return new ChatSessionsVo(sessionId, first.getContent(), first.getTimestamp());
                })
                .sorted(Comparator.comparing(ChatSessionsVo::getSessionTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatMemoryVo> getUserSessionDetail(String conversationId) {
        LambdaQueryWrapper<AiChatMemory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AiChatMemory::getConversationId, conversationId)
                .orderByAsc(AiChatMemory::getTimestamp);

        List<AiChatMemory> memories = aiChatMemoryMapper.selectList(queryWrapper);

        return memories.stream().map(memory -> {
            ChatMemoryVo vo = new ChatMemoryVo();
            BeanUtils.copyProperties(memory, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
