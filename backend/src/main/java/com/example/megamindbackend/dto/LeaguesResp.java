package com.example.megamindbackend.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LeaguesResp {
    private List<League> leagues = new ArrayList<>();
    private List<League> nonActiveLeagues = new ArrayList<>();

    @Data
    public static class League {
        private Boolean active;
        private Integer leagueId;
        private String leagueName;
        private Integer rank;
        private Integer up;
    }
}
