package com.zhang.ai.openaicodesdk.openai.service.airequest;

import com.alibaba.fastjson2.JSON;
import com.zhang.ai.openaicodesdk.openai.dto.ChatCompletionRequestDTO;
import com.zhang.ai.openaicodesdk.openai.dto.ChatCompletionSyncResponseDTO;
import okhttp3.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class AiService {

    Logger logger = Logger.getLogger(AiService.class.getName());

    private static volatile OkHttpClient sharedClient;
    
    private final String apiKey;

    private final String url;

    private final String model;

    private final OkHttpClient client;

    public AiService(String apiKey, String url, String model) {
        this.apiKey = apiKey;
        this.url = url;
        this.model = model;
        // 使用共享的客户端实例
        this.client = getSharedClient();
    }
    
    private static OkHttpClient getSharedClient() {
        if (sharedClient == null) {
            synchronized (AiService.class) {
                if (sharedClient == null) {
                    sharedClient = new OkHttpClient.Builder()
                            .connectTimeout(180, TimeUnit.SECONDS)
                            .readTimeout(180, TimeUnit.SECONDS)
                            .writeTimeout(180, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return sharedClient;
    }
    
    // 添加静态方法用于关闭共享客户端
    public static void shutdownClient() {
        if (sharedClient != null) {
            synchronized (AiService.class) {
                if (sharedClient != null) {
                    // 关闭调度器
                    sharedClient.dispatcher().executorService().shutdown();
                    try {
                        if (!sharedClient.dispatcher().executorService().awaitTermination(5, TimeUnit.SECONDS)) {
                            sharedClient.dispatcher().executorService().shutdownNow();
                        }
                    } catch (InterruptedException e) {
                        sharedClient.dispatcher().executorService().shutdownNow();
                        Thread.currentThread().interrupt();
                    }
                    
                    // 清空连接池
                    sharedClient.connectionPool().evictAll();
                    
                    // 关闭缓存
                    if (sharedClient.cache() != null) {
                        try {
                            sharedClient.cache().close();
                        } catch (Exception e) {
                            // 忽略缓存关闭错误
                        }
                    }
                    
                    sharedClient = null;
                }
            }
        }
    }


    public ChatCompletionSyncResponseDTO getCodeReview(ChatCompletionRequestDTO chatCompletionRequestDTO) {
        String result = null;
        try {
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(chatCompletionRequestDTO));

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    result = response.body().string();
                    logger.info("API调用成功: " + result);
                } else {
                    logger.info("请求失败: " + response.code() + ", " + response.message());
                    String errorBody = response.body() != null ? response.body().string() : "无错误详情";
                    logger.info("错误详情: " + errorBody);
                    throw new RuntimeException("API请求失败: " + response.code() + ", " + errorBody);
                }
            }
        } catch (Exception e) {
            logger.info("网络请求异常: " + e.getMessage());
            throw new RuntimeException("网络请求异常", e);
        }

        return JSON.parseObject(result, ChatCompletionSyncResponseDTO.class);
    }

    public String getModel() {
        return model;
    }
}