package com.sixth.soul_trail.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 情感分析算法服务客户端（Flask 服务，默认端口 8081）
 * 在写日记时调用，把文本转成情感分数 + 标签，存进 diary 表。
 * 设计原则：算法服务不在线/超时 -> 返回 null，绝不影响日记保存。
 */
@Component
public class SentimentClient {

    private static final String ANALYZE_URL = "http://localhost:8081/analyze";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2))
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 调用算法服务分析文本
     *
     * @param text 日记正文
     * @return {score=Double, label=String}；失败/服务不在线返回 null
     */
    public Map<String, Object> analyze(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        try {
            String body = objectMapper.writeValueAsString(Map.of("text", text));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ANALYZE_URL))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(3))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode json = objectMapper.readTree(response.body());
            Map<String, Object> result = new HashMap<>();
            result.put("score", json.get("sentiment").asDouble());
            result.put("label", json.get("label").asText());
            return result;
        } catch (Exception e) {
            // 算法服务不在线/超时/解析失败：日记照存，分数留空
            return null;
        }
    }
}
