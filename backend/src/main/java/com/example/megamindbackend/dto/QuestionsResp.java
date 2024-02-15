package com.example.megamindbackend.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionsResp {

    private List<Question> questions = new ArrayList<>();

    @Data
    public static class Question {
        private Integer questionId;
        private String categoryName;
        private String questionText;
        private String correctAnswer;
        private String wrongAnswer1;
        private String wrongAnswer2;
        private String wrongAnswer3;
        private String explanation;
    }
}