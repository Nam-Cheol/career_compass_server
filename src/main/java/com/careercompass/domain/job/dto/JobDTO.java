package com.careercompass.domain.job.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobDTO {

    private long id;

    private int numberOfQuestions;

    private String question1;
    private String question2;
    private String question3;
    private String question4;
    private String question5;
    private String question6;
    private String question7;
    private String question8;
    private String question9;
    private String question10;

    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String answer5;
    private String answer6;
    private String answer7;
    private String answer8;
    private String answer9;
    private String answer10;

    private String summation1;
    private String summation2;
    private String summation3;
    private String summation4;
    private String summation5;
    private String summation6;
    private String summation7;
    private String summation8;
    private String summation9;

    private String finalSummation;

    private String resultJob;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserSendConversationDTO {
        private long id;
        private long user;
        private int numberOfQuestions;
        private String question;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserReceiveConversationDTO {
        private long id;
        private long user;
        private int numberOfQuestions;
        private String answer;
        private String summation;
        private String finalSummation;
        private String resultJob;
    }

}
