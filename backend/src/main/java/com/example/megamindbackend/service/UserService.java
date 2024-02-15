package com.example.megamindbackend.service;

import com.example.megamindbackend.dto.*;

public interface UserService {
    UserResp addUser(UserReq req);

    UserResp updateUser(Integer id, UserReq req);

    MainPageResp getMainPage(Integer userId);

    LeaguesResp getLeagues(Integer userId);

    LeagueResp getLeague(Integer userId, Integer leagueId);

    ProfileResp getProfile(Integer userId);

    MainPageResp login(LoginReq loginReq);

}
