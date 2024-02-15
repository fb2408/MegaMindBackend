package com.example.megamindbackend.controller;

import com.example.megamindbackend.dto.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:5174/"})
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public interface UserController {

    @GetMapping("/{userId}/mainPage")
    MainPageResp getMainPage(@PathVariable Integer userId);

    @GetMapping("/{userId}/profile")
    ProfileResp getProfile(@PathVariable Integer userId);

    @GetMapping("/{userId}/league")
    LeaguesResp getLeagues(@PathVariable Integer userId);

    @GetMapping("/{userId}/league/{leagueId}")
    LeagueResp getLeague(@PathVariable Integer userId, @PathVariable Integer leagueId);
    @PostMapping
    UserResp addUser(@RequestBody UserReq userReq);

    @PatchMapping("/{id}")
    UserResp updateUser(@PathVariable Integer id, @RequestBody UserReq userReq);

    @PostMapping("/login")
    MainPageResp login(@RequestBody LoginReq loginReq);



}
