package com.example.megamindbackend.controller;

import com.example.megamindbackend.dto.CategoriesResp;
import com.example.megamindbackend.dto.DeleteUserReq;
import com.example.megamindbackend.dto.LeagueReq;
import com.example.megamindbackend.dto.LeagueResp;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:5174/"})
@RequestMapping(value = "/league", produces = MediaType.APPLICATION_JSON_VALUE)
public interface LeagueController {

    @PostMapping
    LeagueResp addLeague(@RequestBody LeagueReq leagueReq);

    @PatchMapping("/{id}")
    LeagueResp updateLeague(@PathVariable Integer id, @RequestBody LeagueReq leagueReq);

    @GetMapping("/{leagueId}/user/{userId}")
    LeagueResp addUser(@PathVariable Integer leagueId, @PathVariable Integer userId);

    @DeleteMapping("/{leagueId}")
    void removeUser(@PathVariable Integer leagueId, @RequestBody DeleteUserReq userToDeleteId);

    @PostMapping("/{userId}/{leagueCode}")
    LeagueResp joinLeague(@PathVariable Integer userId, @PathVariable String leagueCode);

    @GetMapping("/categories")
    CategoriesResp getCategories();
}
