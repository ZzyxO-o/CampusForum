package cn.zuo.service;

import cn.zuo.dto.aidto.ChatDto;
import cn.zuo.dto.aidto.ChatMemoryDto;
import cn.zuo.vo.chat.ChatMemoryVo;
import cn.zuo.vo.chat.ChatVO;
import reactor.core.publisher.Flux;

public interface AIService {
    /**
     * ai对话接口
     * @param chatDto
     * @return
     */
    ChatVO chat(ChatDto chatDto);

    /**
     * ai对话流接口
     * @param chatDto
     * @return
     */
    Flux<String> chatStream(ChatDto chatDto);

//    /**
//     * ai图片生成接口
//     * @param description
//     * @return
//     */
//    String image(String description);

    /**
     * @param chatMemoryDto
     * @return
     */
    ChatMemoryVo getChatMemory(ChatMemoryDto chatMemoryDto);

}
