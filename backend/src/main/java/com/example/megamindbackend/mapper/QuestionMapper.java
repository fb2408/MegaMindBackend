package com.example.megamindbackend.mapper;

import com.example.megamindbackend.db.CategoryEntity;
import com.example.megamindbackend.db.QuestionEntity;
import com.example.megamindbackend.db.QuestionRepository;
import com.example.megamindbackend.dto.QuestionsResp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionMapper {

    private final QuestionRepository questionRepository;


    public QuestionsResp toQuestionsResp(final List<QuestionEntity> questionEntities) {
        final QuestionsResp questionsResp = new QuestionsResp();
        questionsResp.setQuestions(
                questionEntities.stream()
                        .map(questionEntity -> {
                            final QuestionsResp.Question question = new QuestionsResp.Question();
                            question.setQuestionId(questionEntity.getId());
                            question.setCategoryName(questionEntity.getCategoryEntity().getCategoryName());
                            question.setQuestionText(questionEntity.getQuestionText());
                            question.setCorrectAnswer(questionEntity.getCorrectAnswer());
                            question.setWrongAnswer1(questionEntity.getWrongAnswer1());
                            question.setWrongAnswer3(questionEntity.getWrongAnswer2());
                            question.setWrongAnswer2(questionEntity.getWrongAnswer3());
                            question.setExplanation(questionEntity.getExplanation());
                            return question;
                        }).toList());
        return questionsResp;
    }

    public void toQuestionEntity(final String content, final CategoryEntity category) {

        Gson gson = new Gson();
        final QuestionsResp entities = gson.fromJson(content, QuestionsResp.class);
        entities.getQuestions().forEach(e -> {
            final QuestionEntity question = new QuestionEntity();
            question.setCategoryEntity(category);
            question.setQuestionText(e.getQuestionText());
            question.setCorrectAnswer(e.getCorrectAnswer());
            question.setWrongAnswer1(e.getWrongAnswer1());
            question.setWrongAnswer2(e.getWrongAnswer2());
            question.setWrongAnswer3(e.getWrongAnswer3());
            question.setExplanation(e.getExplanation());
            questionRepository.save(question);
        });
    }

}
