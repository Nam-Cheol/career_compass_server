package com.careercompass.domain.job.controller;

import com.careercompass.common.utils.ApiUtil;
import com.careercompass.domain.job.dto.JobDTO;
import com.careercompass.domain.job.service.JobAiService;
import com.careercompass.domain.job.service.JobService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/jobs")
@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final JobAiService jobAiService;
    private final Gson gson;

    @PostMapping("/conversations")
    public ApiUtil<?> getGptResponse(@RequestBody JobDTO.UserSendConversationDTO dto) {
        return new ApiUtil<>(jobAiService.getConversationToJsonByGpt(dto));
    }

    @PostMapping("/test")
    public ApiUtil<?> test(@RequestBody JobDTO.UserSendConversationDTO dto) {
        System.out.println(dto.toString());
        String json = gson.toJson(dto);
        JsonObject processingData = gson.fromJson(json, JsonObject.class);
        if(processingData.has("question")) {
            String value = processingData.get("question").getAsString();
            int numberOfQuestions = processingData.get("numberOfQuestions").getAsInt();
            String questionKey = "question" + numberOfQuestions;
            processingData.remove("question");
            processingData.addProperty(questionKey, value);
        }
        System.out.println(processingData.toString());
        return new ApiUtil<>(
                Map.of(
                        "id", dto.getId(),
                        "user", dto.getUser(),
                        "numberOfQuestions", dto.getNumberOfQuestions(),
                        "answer", "확인하는 테스트 중입니다.",
                        "summation", "",
                        "finalSummation", "",
                        "resultJob", ""
                )
        );
    }

}
