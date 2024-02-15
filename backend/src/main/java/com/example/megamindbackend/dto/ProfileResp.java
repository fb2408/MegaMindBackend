package com.example.megamindbackend.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProfileResp {
    private String userName;
    private String firstname;
    private String lastname;
    private Integer level;
    private Integer userXp;
    private Integer credits;
    private Integer answeredQuestions;
    private Integer answeredCorrect;
    private Double answerPercentage;
    private Integer globalLeagueRank;
    private Integer globalLeagueMembers;
    private Integer daysInARow;
    private List<Category> categories = new ArrayList<>();

    @Data
    public static class Category {
        private Integer id;
        private String categoryName;
        private Integer answeredQuestions;
        private Integer answeredCorrect;
        private Double answerPercentage;
    }
}
