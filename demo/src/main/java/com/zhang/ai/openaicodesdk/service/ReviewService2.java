package com.zhang.ai.openaicodesdk.service;

import com.zhang.ai.openaicodesdk.git.GitDemand;
import com.zhang.ai.openaicodesdk.message.weixin.service.WeiXin;
import com.zhang.ai.openaicodesdk.openai.service.aiexe.impl.OpenAiCodeReviewServiceImpl;
import com.zhang.ai.openaicodesdk.openai.service.airequest.AiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReviewService2 {


    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    public void checkCode() throws Exception {


        GitDemand gitCommand = new GitDemand(
                getEnv("COMMIT_PROJECT"),
                getEnv("COMMIT_BRANCH"),
                getEnv("COMMIT_AUTHOR"),
                getEnv("GIT_URL"),
                getEnv("GIT_USER"),
                getEnv("GIT_PASSWORD"),
                getEnv("GIT_FILE_ADDRESS")
        );

        /**
         * 项目：{{repo_name.DATA}} 分支：{{branch_name.DATA}} 作者：{{commit_author.DATA}} 说明：{{commit_message.DATA}}
         */
        WeiXin weiXin = new WeiXin(
                getEnv("WEIXIN_APPID"),
                getEnv("WEIXIN_SECRET"),
                getEnv("WEIXIN_TOUSER"),
                getEnv("WEIXIN_TEMPLATE_ID")
        );


        AiService openAI = new AiService(
                getEnv("MODEL_KEY"), getEnv("MODEL_URL"), getEnv("MODEL"));


        OpenAiCodeReviewServiceImpl openAiCodeReviewService = new OpenAiCodeReviewServiceImpl(gitCommand, openAI, weiXin);
        openAiCodeReviewService.apply();

        logger.info("openai-code-review done!");

        // 清理资源
        cleanupResources();
    }

    private static void cleanupResources() {
        try {
            // 关闭OkHttp客户端
            AiService.shutdownClient();

            // 关闭JGit资源
            GitDemand.shutdownJGit();

            // 触发垃圾回收
            System.gc();

            // 给线程一些时间来响应中断
            Thread.sleep(500);
        } catch (Exception e) {
            // 忽略清理错误
        }
    }

    private static String getEnv(String key) {
        String value = System.getenv(key);
        if (null == value || value.isEmpty()) {
            throw new RuntimeException("value is null");
        }
        return value;
    }
}
