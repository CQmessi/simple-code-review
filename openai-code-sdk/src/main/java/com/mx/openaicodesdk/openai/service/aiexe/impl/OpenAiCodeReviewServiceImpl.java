package com.mx.openaicodesdk.openai.service.aiexe.impl;


import com.mx.openaicodesdk.git.GitDemand;
import com.mx.openaicodesdk.message.weixin.dto.TemplateMessageDTO;
import com.mx.openaicodesdk.message.weixin.service.WeiXin;
import com.mx.openaicodesdk.openai.dto.ChatCompletionRequestDTO;
import com.mx.openaicodesdk.openai.dto.ChatCompletionSyncResponseDTO;
import com.mx.openaicodesdk.openai.service.airequest.AiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OpenAiCodeReviewServiceImpl extends AbstractOpenAiCodeReviewServiceImpl{
    public OpenAiCodeReviewServiceImpl(GitDemand gitDemand, AiService aiService, WeiXin weiXin) {
        super(gitDemand, aiService, weiXin);
    }
    @Override
    protected String getDiffCode() throws Exception {
        return gitDemand.getDiffCode();
    }

    @Override
    protected String codeReview(String diffCode) {
        ChatCompletionRequestDTO chatCompletionRequest = new ChatCompletionRequestDTO();
        chatCompletionRequest.setModel(aiService.getModel());
        chatCompletionRequest.setMessages(new ArrayList<ChatCompletionRequestDTO.Prompt>() {
            private static final long serialVersionUID = -7988151926241837899L;

            {
                add(new ChatCompletionRequestDTO.Prompt("user", "你是一位资深编程专家，拥有深厚的编程基础和广泛的技术栈知识。给下面的代码提点意见吧"));
                add(new ChatCompletionRequestDTO.Prompt("user", diffCode));
            }
        });

        ChatCompletionSyncResponseDTO completions = aiService.getCodeReview(chatCompletionRequest);
        ChatCompletionSyncResponseDTO.Message message = completions.getChoices().get(0).getMessage();
        return message.getContent();
    }

    @Override
    protected String pushCodeToGit(String recommend) throws Exception {
        return gitDemand.pushCode(recommend);
    }

    @Override
    protected void pushMessage(String logUrl) throws Exception {
        Map<String, Map<String, String>> data = new HashMap<>();
        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.REPO_NAME, gitDemand.getProject());
        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.BRANCH_NAME, gitDemand.getBranch());
        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.COMMIT_AUTHOR, gitDemand.getAuthor());
        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.COMMIT_MESSAGE, logUrl);
        weiXin.sendTemplateMessage(logUrl, data);
    }
}
