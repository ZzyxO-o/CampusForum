package cn.zuo.dto.aidto;

import lombok.Data;

@Data
public class SystemPromptDTO {

    private Integer id;

    private String promptType;

    private String prompt;
}
