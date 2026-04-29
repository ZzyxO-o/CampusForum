package cn.zuo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 聊天消息实体类
 * 对应数据库 chats 表
 */
@Data
@TableName("ai_chat_memory")
public class AiChatMemory {
    @TableId(type = IdType.AUTO)
    private Long id; // 主键id
    @TableField("conversation_id")
    private String conversationId; //对话id
    @TableField("content")
    private String content; //对话内容
    @TableField("type")
    private String type;
    @TableField("timestamp")
    private LocalDateTime timestamp; // 时间戳

}
