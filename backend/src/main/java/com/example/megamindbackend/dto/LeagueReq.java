package com.example.megamindbackend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Data
public class LeagueReq {

    Optional<String> leagueName;
    Optional<Integer> leagueAdmin;
    Optional<Integer> seasonLength;
    Optional<LocalDate> startDate;
    Optional<Integer> dailyCategories;
    Optional<Integer> questionsPerCategory;
    Optional<List<Integer>> leagueCategories;

}
