package com.example.megamindbackend.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoriesResp {
    private List<Category> categories = new ArrayList<>();

    @Data
    public static class Category {
        private Integer id;
        private String name;
    }
}
