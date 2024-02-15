package com.example.megamindbackend.controller;

import com.example.megamindbackend.dto.AnswersReq;
import com.example.megamindbackend.dto.AnswersResp;
import com.example.megamindbackend.dto.QuestionsResp;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:5174/"})
@RequestMapping(value = "/questions", produces = MediaType.APPLICATION_JSON_VALUE)
public interface QuestionController {

    @GetMapping("/{leagueId}/user/{userId}")
    QuestionsResp getQuestions(@PathVariable Integer leagueId, @PathVariable Integer userId);


    @PostMapping("/category/{categoryId}/user/{userId}")
    AnswersResp checkAnswersFromCategory( @PathVariable Integer categoryId,@PathVariable Integer userId, @RequestBody AnswersReq answersReq);

    @GetMapping("/user/{userId}/category/{categoryId}")
    QuestionsResp getQuestionsFromCategory( @PathVariable Integer userId, @PathVariable Integer categoryId);

    @PostMapping("/{leagueId}/user/{userId}")
    AnswersResp checkAnswers(@PathVariable Integer leagueId, @PathVariable Integer userId, @RequestBody AnswersReq answersReq);
}
