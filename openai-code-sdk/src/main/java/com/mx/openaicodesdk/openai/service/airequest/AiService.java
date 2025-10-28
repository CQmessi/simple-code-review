package com.mx.openaicodesdk.openai.service.airequest;

import com.alibaba.fastjson2.JSON;
import com.mx.openaicodesdk.openai.dto.ChatCompletionRequestDTO;
import com.mx.openaicodesdk.openai.dto.ChatCompletionSyncResponseDTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class AiService {

    Logger logger = Logger.getLogger(AiService.class.getName());

    
    private final String apiKey;

    private final String url;

    private final String model;


    public AiService(String apiKey, String url, String model) {
        this.apiKey = apiKey;
        this.url = url;
        this.model = model;
    }



    public ChatCompletionSyncResponseDTO getCodeReview(ChatCompletionRequestDTO chatCompletionRequestDTO) {
        try {

            URL httpUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = JSON.toJSONString(chatCompletionRequestDTO).getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            connection.disconnect();

            return JSON.parseObject(content.toString(), ChatCompletionSyncResponseDTO.class);
        } catch (Exception e) {
            logger.info("网络请求异常: " + e.getMessage());
            throw new RuntimeException("网络请求异常", e);
        }
    }

    public String getModel() {
        return model;
    }
}