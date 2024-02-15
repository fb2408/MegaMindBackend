package com.example.megamindbackend.controller.impl;

import com.example.megamindbackend.controller.UserController;
import com.example.megamindbackend.dto.*;
import com.example.megamindbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public MainPageResp getMainPage(Integer userId) {
        return userService.getMainPage(userId);
    }

    @Override
    public ProfileResp getProfile(Integer userId) {
        return userService.getProfile(userId);
    }

    @Override
    public LeaguesResp getLeagues(Integer userId) {
        return userService.getLeagues(userId);
    }

    @Override
    public LeagueResp getLeague(Integer userId, Integer leagueId) {
        return userService.getLeague(userId, leagueId);
    }

    @Override
    public UserResp addUser(UserReq userReq) {
        return userService.addUser(userReq);
    }

    @Override
    public UserResp updateUser(Integer id, UserReq userReq) {
        return userService.updateUser(id, userReq);
    }

    @Override
    public MainPageResp login(LoginReq loginReq) {
        return userService.login(loginReq);
    }


}
