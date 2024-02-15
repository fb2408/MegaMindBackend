package com.example.megamindbackend.service;

import com.example.megamindbackend.dto.AnswersReq;
import com.example.megamindbackend.dto.AnswersResp;
import com.example.megamindbackend.dto.QuestionsResp;

public interface QuestionService {
    QuestionsResp getQuestions(Integer leagueId, Integer userId);

    AnswersResp checkAnswers(Integer leagueId, Integer userId, AnswersReq answersReq);

    QuestionsResp getQuestionsFromCategory(Integer userId, Integer categoryId);

    AnswersResp checkAnswersFromCategory(Integer categoryId, Integer userId, AnswersReq answersReq);
}
