package com.example.megamindbackend.dto;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class AnswersResp {

    private Integer score;
    private Boolean allCorrect;
    private List<CategoryPoints> categories = new ArrayList<>();

    @Data
    public static class CategoryPoints {
        private String categoryName;
        private Integer points = 0;
        private Integer numberOfQuestions = 0;
        private Integer numberOfCorrect = 0;
    }


}
