package com.example.megamindbackend.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AnswersReq {

    private List<Answer> answers = new ArrayList<>();

    @Data
    public static class Answer {
        private Integer questionId;
        private Boolean correct;
    }
}
