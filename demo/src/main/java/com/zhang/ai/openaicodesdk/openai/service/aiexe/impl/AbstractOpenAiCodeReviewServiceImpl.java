package com.zhang.ai.openaicodesdk.openai.service.aiexe.impl;


import com.zhang.ai.openaicodesdk.git.GitDemand;
import com.zhang.ai.openaicodesdk.message.weixin.service.WeiXin;
import com.zhang.ai.openaicodesdk.openai.service.aiexe.OpenAiCodeReviewService;
import com.zhang.ai.openaicodesdk.openai.service.airequest.AiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractOpenAiCodeReviewServiceImpl implements OpenAiCodeReviewService {


    protected final GitDemand gitDemand;
    protected final AiService aiService;
    protected final WeiXin weiXin;

    private final Logger logger = LoggerFactory.getLogger(AbstractOpenAiCodeReviewServiceImpl.class);

    public AbstractOpenAiCodeReviewServiceImpl(GitDemand gitCommand, AiService openAI, WeiXin weiXin) {
        this.gitDemand = gitCommand;
        this.aiService = openAI;
        this.weiXin = weiXin;
    }

    @Override
    public void apply() throws Exception {
        try {
            // 1. 获取提交代码
            String diffCode = getDiffCode();
            // 2. 开始评审代码
            String recommend = codeReview(diffCode);
            // 3. 记录评审结果；返回日志地址
            String logUrl = pushCodeToGit(recommend);
            // 4. 发送消息通知；日志地址、通知的内容
            pushMessage(logUrl);
        } catch (Exception e) {
            logger.error("openai-code-review error", e);
        }


    }


    protected abstract String getDiffCode() throws Exception;

    protected abstract String codeReview(String diffCode) throws Exception;

    protected abstract String pushCodeToGit(String recommend) throws Exception;

    protected abstract void pushMessage(String logUrl) throws Exception;
}
