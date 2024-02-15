package com.example.megamindbackend.service;

import com.example.megamindbackend.dto.CategoriesResp;
import com.example.megamindbackend.dto.DeleteUserReq;
import com.example.megamindbackend.dto.LeagueReq;
import com.example.megamindbackend.dto.LeagueResp;

public interface LeagueService {

    LeagueResp addLeague(LeagueReq leagueReq);

    LeagueResp updateLeague(Integer id, LeagueReq leagueReq);

    LeagueResp addUser(Integer leagueId, Integer userId);

    void removeUser(Integer leagueId, DeleteUserReq deleteUserReq);

    LeagueResp joinLeague(Integer userId, String leagueCode);

    CategoriesResp getCategories();
}
