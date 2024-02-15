package com.example.megamindbackend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class LeagueResp {

    private Integer leagueId;
    private String name;
    private Integer seasonLength;
    private String invitationCode;
    private LocalDate startDate;
    private Integer dailyCategories;
    private Integer questionsPerCategory;
    private Boolean quizDone;
    private List<User> users = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();

    @Data
    public static class User {
        private String username;
        private String firstname;
        private String lastname;
        private Integer score;
        private Integer position;
        private Integer up;
    }

    @Data
    public static class Category {
        private String name;
    }
}
