package com.zhang.ai.openaicodesdk.openai.dto.model;

public enum Model {


    GLM_4_FLASH("glm-4-flash","适用简单任务，速度最快，价格最实惠的版本，具有128k上下文"),
    DEEP_SEEK_AI("deepseek-ai/DeepSeek-R1-0528-Qwen3-8B","硅基跳动deepseek免费版"),
    ;
    private final String code;
    private final String info;

    Model(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

}
