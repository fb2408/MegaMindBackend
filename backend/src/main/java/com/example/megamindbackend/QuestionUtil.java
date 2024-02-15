package com.example.megamindbackend;

import com.example.megamindbackend.db.*;
import com.example.megamindbackend.dto.OpenAiReq;
import com.example.megamindbackend.dto.OpenAiResp;
import com.example.megamindbackend.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Component
public class QuestionUtil {

    private final CategoryLeagueRepository categoryLeagueRepository;
    private final QuestionRepository questionRepository;
    private final UserLeagueQuestionRepository userLeagueQuestionRepository;
    private final QuestionMapper questionMapper;
    private final RestTemplate restTemplate;
    @Value("${openai.api.url}")
    private String apiUrl;
    @Value("${openai.model}")
    private String model;

    public void addQuestionsToLeague(final LeagueEntity league) {
        final List<UserLeagueQuestionEntity> userLeagueQuestionEntities = new ArrayList<>();

        final List<CategoryLeagueEntity> categories = getRandomElements(categoryLeagueRepository.findByLeague_Id(league.getId()), league.getDailyCategories());
        categories.forEach(category -> {
            if (questionRepository.findByCategoryEntity_Id(category.getCategory().getId()).stream().filter(question -> !userLeagueQuestionRepository.existsByLeague_IdAndQuestion_Id(league.getId(), question.getId())).toList().size() < league.getQuestionsPerCategory()) {
                getQuestionsFromOpenAi(category.getCategory(), questionRepository.findByCategoryEntity_Id(category.getCategory().getId()));
            }
            getRandomElements((questionRepository.findByCategoryEntity_Id(category.getCategory().getId()).stream()
                    .filter(question -> !userLeagueQuestionRepository.existsByLeague_IdAndQuestion_Id(league.getId(), question.getId())).toList()), league.getQuestionsPerCategory()).forEach(question -> {
                final UserLeagueQuestionEntity userQuestion = new UserLeagueQuestionEntity();
                userQuestion.setUser(null);
                userQuestion.setQuestion(question);
                userQuestion.setLeague(league);
                userQuestion.setDate(LocalDate.now());
                userLeagueQuestionEntities.add(userQuestion);
            });
        });
        userLeagueQuestionRepository.saveAll(userLeagueQuestionEntities);
    }

    public void getQuestionsFromOpenAi(final CategoryEntity category, List<QuestionEntity> pastQuestions) {
        System.out.println("Dohvacam pitanja");
        final String common =
                """
                        give me 15 pub quiz question from %s category that are 7 out of 10 on a difficulty scale.
                        In this format  without new line { "questions" : [{"questionText": question_text,
                        "correctAnswer" : correct_answer,
                        "wrongAnswer1": wrong_answer1,
                        "wrongAnswer2": wrong_answer2,
                        "wrongAnswer3": wrong_answer3,
                        "explanation": fun fact about question
                        }]}.
                        I already have these questions:
                        """;

        final StringBuilder sb = new StringBuilder(String.format(common, category.getCategoryName()));
        pastQuestions.forEach(q -> sb.append(q.getQuestionText()).append(" ,\n"));
        final OpenAiReq request = new OpenAiReq(model, sb.toString());
        final OpenAiResp openAiResp = restTemplate.postForObject(apiUrl, request, OpenAiResp.class);
        if (openAiResp == null || openAiResp.getChoices() == null || openAiResp.getChoices().isEmpty()) {
            throw new RuntimeException("Unable to fetch new questions from chat-gpt");
        }
        questionMapper.toQuestionEntity(openAiResp.getChoices().get(0).getMessage().getContent(), category);
        System.out.println("Pitanja dohvacena");
    }

    public <T> List<T> getRandomElements(List<T> list, Integer numberOfElements) {
        Random rand = new Random();
        List<T> randomList = new ArrayList<>();
        Set<Integer> indexes = new HashSet<>();
        while (indexes.size() < numberOfElements) {
            indexes.add(rand.nextInt(list.size()));
        }
        indexes.forEach(e -> randomList.add(list.get(e)));
        return randomList;
    }
}
