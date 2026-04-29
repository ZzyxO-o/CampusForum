package cn.zuo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("system_prompt")
@Data
public class SystemPrompt {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("prompt_type")
    private String promptType;
    @TableField("prompt")
    private String prompt;
}
