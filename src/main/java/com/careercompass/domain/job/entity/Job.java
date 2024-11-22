package com.careercompass.domain.job.entity;

import com.careercompass.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer numberOfQuestions;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @PrePersist
    protected void PrePersist() {
        if(numberOfQuestions < 1 || numberOfQuestions == null) {
            numberOfQuestions = 0;
        }
    }

}
