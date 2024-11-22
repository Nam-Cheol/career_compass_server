package com.careercompass.domain.job.service;

import com.careercompass.domain.job.dto.JobDTO;
import com.careercompass.domain.job.entity.Job;
import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JobAiService {

    @Value("${spring.ai.openai.api-key}")
    private String apikey;

    @Value("${spring.ai.openai.chat.options.model}")
    private String model;

    @Value("${openAiApi.url}")
    private String url;

    private final Gson gson;

    private String summation = "";

    @Autowired
    private RestTemplate restTemplate;

    public String callGptModel(String prompt) {

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apikey);

        // 요청 본문 설정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model); // 파인튜닝된 GPT-4 모델이 있는 경우 해당 모델 ID 사용
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 300); // 필요에 따라 토큰 설정

        // 요청 엔터티 생성
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // API 호출 및 응답 처리
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            // OpenAI의 응답을 처리
            return response.getBody();
        } else {
            throw new RuntimeException("OpenAI API 호출 실패: " + response.getStatusCode());
        }
    }

    public JobDTO.UserReceiveConversationDTO getConversationToJsonByGpt(JobDTO.UserSendConversationDTO userSendConversationDTO) {
        if (userSendConversationDTO != null) {
            String json = gson.toJson(userSendConversationDTO);
            JsonObject promptJson = gson.fromJson(json, JsonObject.class);
            int numberOfQuestions = promptJson.get("numberOfQuestions").getAsInt();
            if (promptJson.has("question")) {
                String value = promptJson.get("question").getAsString();
                String questionKey = "question" + numberOfQuestions;
                promptJson.remove("question");
                promptJson.addProperty(questionKey, value);

                if (numberOfQuestions <= 10) {
                    promptJson.addProperty("summation" + (numberOfQuestions - 1), summation);
                }

            }


            String promptStr = gson.toJson(promptJson);
            String gptAnswerStr = callGptModel(promptStr);
            JsonObject jsonObject = JsonParser.parseString(gptAnswerStr).getAsJsonObject();
            // "choices" 배열에서 첫 번째 요소 가져오기
            JsonArray choicesArray = jsonObject.getAsJsonArray("choices");
            JsonObject firstChoice = choicesArray.get(0).getAsJsonObject();

            // "message" 객체에서 "content" 필드 가져오기
            JsonObject messageObject = firstChoice.getAsJsonObject("message");
            String content = messageObject.get("content").getAsString();
            JsonObject contentJson = gson.fromJson(content, JsonObject.class);
            for (String key : contentJson.keySet()) {
                if (key.startsWith("answer")) {
                    // 키가 "answer"로 시작하는 경우 처리
                    String answer = contentJson.get(key).getAsString();
                    contentJson.remove(key); // 기존 키 삭제
                    contentJson.addProperty("answer", answer); // 새 키 추가
                    break; // 원하는 키를 찾으면 종료
                }
            }

            for (String key : contentJson.keySet()) {
                if (key.startsWith("summation")) {
                    summation = contentJson.get(key).getAsString();
                    contentJson.remove(key);
                    contentJson.addProperty("summation", summation);
                    break;
                }
            }

            return JobDTO.UserReceiveConversationDTO.builder()
                    .id(contentJson.has("id") ? contentJson.get("id").getAsLong() : null)
                    .user(contentJson.has("user") ? contentJson.get("user").getAsLong() : null)
                    .numberOfQuestions(contentJson.has("numberOfQuestions") ? contentJson.get("numberOfQuestions").getAsInt() : 0) // 기본값 설정
                    .answer(contentJson.has("answer") ? contentJson.get("answer").getAsString() : "")
                    .summation(contentJson.has("summation") ? contentJson.get("summation").getAsString() : "")
                    .finalSummation(contentJson.has("finalSummation") ? contentJson.get("finalSummation").getAsString() : "")
                    .resultJob(contentJson.has("resultJob") ? contentJson.get("resultJob").getAsString() : "")
                    .build();
        } else {
            return null;
        }

    }

}
