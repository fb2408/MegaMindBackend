package com.example.megamindbackend.controller.impl;

import com.example.megamindbackend.controller.QuestionController;
import com.example.megamindbackend.dto.AnswersReq;
import com.example.megamindbackend.dto.AnswersResp;
import com.example.megamindbackend.dto.QuestionsResp;
import com.example.megamindbackend.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuestionControllerImpl implements QuestionController {
    private final QuestionService questionService;

    @Override
    public QuestionsResp getQuestions(Integer leagueId, Integer userId) {
        return questionService.getQuestions(leagueId, userId);
    }

    @Override
    public AnswersResp checkAnswersFromCategory(Integer categoryId, Integer userId, AnswersReq answersReq) {
        return questionService.checkAnswersFromCategory(categoryId, userId, answersReq);
    }

    @Override
    public QuestionsResp getQuestionsFromCategory( Integer userId, Integer categoryId) {
        return questionService.getQuestionsFromCategory( userId, categoryId);
    }

    @Override
    public AnswersResp checkAnswers(Integer leagueId, Integer userId, AnswersReq answersReq) {
        return questionService.checkAnswers(leagueId, userId, answersReq);
    }
}
