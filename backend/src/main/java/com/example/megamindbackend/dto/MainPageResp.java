package com.example.megamindbackend.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MainPageResp {
    private Integer userId;
    private String username;
    private Integer daysInARow;
    private Boolean globalDone;
    private List<Category> favouriteCategories = new ArrayList<>();

    @Data
    public static class Category {
        private Integer categoryId;
        private String categoryName;
        private Boolean dailyDone;
    }
}
