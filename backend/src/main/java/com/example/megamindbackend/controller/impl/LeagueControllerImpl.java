package com.example.megamindbackend.controller.impl;

import com.example.megamindbackend.controller.LeagueController;
import com.example.megamindbackend.dto.CategoriesResp;
import com.example.megamindbackend.dto.DeleteUserReq;
import com.example.megamindbackend.dto.LeagueReq;
import com.example.megamindbackend.dto.LeagueResp;
import com.example.megamindbackend.service.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LeagueControllerImpl implements LeagueController {

    private final LeagueService leagueService;

    @Override
    public LeagueResp addLeague(LeagueReq leagueReq) {
        return leagueService.addLeague(leagueReq);
    }

    @Override
    public LeagueResp updateLeague(Integer id, LeagueReq leagueReq) {
        return leagueService.updateLeague(id, leagueReq);
    }

    @Override
    public LeagueResp addUser(Integer leagueId, Integer userId) {
        return leagueService.addUser(leagueId, userId);
    }

    @Override
    public void removeUser(Integer leagueId, DeleteUserReq deleteUserReq) {
        leagueService.removeUser(leagueId, deleteUserReq);
    }

    @Override
    public LeagueResp joinLeague(Integer userId, String leagueCode) {
        return leagueService.joinLeague(userId, leagueCode);
    }

    @Override
    public CategoriesResp getCategories() {
        return leagueService.getCategories();
    }
}
