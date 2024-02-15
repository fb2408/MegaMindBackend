package com.example.megamindbackend.service.impl;

import com.example.megamindbackend.QuestionUtil;
import com.example.megamindbackend.db.*;
import com.example.megamindbackend.dto.AnswersReq;
import com.example.megamindbackend.dto.AnswersResp;
import com.example.megamindbackend.dto.QuestionsResp;
import com.example.megamindbackend.mapper.QuestionMapper;
import com.example.megamindbackend.service.QuestionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final UserLeagueQuestionRepository userLeagueQuestionRepository;
    private final CategoryLeagueRepository categoryLeagueRepository;
    private final LeagueRepository leagueRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final QuestionMapper questionMapper;
    private final QuestionUtil questionUtil;
    private final UserCategoryRepository userCategoryRepository;
    private final UserLeagueRepository userLeagueRepository;
    private final RestTemplate restTemplate;

    @Value("${openai.api.url}")
    private String apiUrl;
    @Value("${openai.model}")
    private String model;

    @Override
    public QuestionsResp getQuestions(Integer leagueId, Integer userId) {
        final List<QuestionEntity> todayAnsweredQuestions = new ArrayList<>(userLeagueQuestionRepository.findByLeague_IdAndDateAndUser_IdNull(leagueId, LocalDate.now()).stream().map(UserLeagueQuestionEntity::getQuestion).toList());
        final LeagueEntity leagueEntity = leagueRepository.findById(leagueId).orElseThrow();
        final UserEntity userEntity = userRepository.findById(userId).orElseThrow();
        final List<UserLeagueQuestionEntity> userLeagueQuestionEntities = new ArrayList<>();
        todayAnsweredQuestions.forEach(question -> {
            final UserLeagueQuestionEntity userQuestion = userLeagueQuestionRepository.findByLeague_IdAndUser_IdAndQuestion_Id(leagueId, userId, question.getId()).orElse(new UserLeagueQuestionEntity());
            userQuestion.setUser(userEntity);
            userQuestion.setQuestion(question);
            userQuestion.setLeague(leagueEntity);
            userQuestion.setDate(LocalDate.now());
            userLeagueQuestionEntities.add(userQuestion);
        });
        userLeagueQuestionRepository.saveAll(userLeagueQuestionEntities);
        return questionMapper.toQuestionsResp(todayAnsweredQuestions);
    }

    @Scheduled(cron = " 15 0 0 * * *")
    public void selectQuestions() {
        final List<UserLeagueQuestionEntity> userLeagueQuestionEntities = new ArrayList<>();
        leagueRepository.findByActiveTrue().forEach(questionUtil::addQuestionsToLeague);
        userLeagueQuestionRepository.saveAll(userLeagueQuestionEntities);
    }

    @Override
    @Transactional
    public AnswersResp checkAnswers(Integer leagueId, Integer userId, AnswersReq answersReq) {
        final AnswersResp answersResp = new AnswersResp();
        System.out.println(answersReq.toString());
        final List<UserLeagueQuestionEntity> questions = userLeagueQuestionRepository.findByLeague_IdAndUser_IdAndDate(leagueId, userId, LocalDate.now());
        answersReq.getAnswers().forEach(answer -> questions.stream().filter(e -> e.getQuestion().getId().equals(answer.getQuestionId()))
                .findFirst().orElseThrow(() -> new RuntimeException("Ode sam falio")).setIsCorrect(answer.getCorrect()));
        final List<CategoryEntity> categories = questions.stream().map(e -> e.getQuestion().getCategoryEntity()).distinct().toList();
        AtomicInteger score = new AtomicInteger();
        final AtomicBoolean allCorrect = new AtomicBoolean(true);
        categories.forEach(category -> {
            final AnswersResp.CategoryPoints categoryPoints = new AnswersResp.CategoryPoints();
            categoryPoints.setCategoryName(category.getCategoryName());
            final UserCategoryEntity userCategory = userCategoryRepository.findByUser_IdAndCategory_Id(userId, category.getId()).get();
            final AtomicBoolean categoryCorrect = new AtomicBoolean(true);
            questions.stream().filter(e -> e.getQuestion().getCategoryEntity().getId().equals(category.getId())).forEach(e -> {
                userCategory.setAnsweredTotal(userCategory.getAnsweredTotal() + 1);
                categoryPoints.setNumberOfQuestions(categoryPoints.getNumberOfQuestions() + 1);
                if (Boolean.TRUE.equals(e.getIsCorrect())) {
                    userCategory.setAnsweredCorrect(userCategory.getAnsweredCorrect() + 1);
                    categoryPoints.setNumberOfCorrect(categoryPoints.getNumberOfCorrect() + 1);
                    categoryPoints.setPoints(categoryPoints.getPoints() + 5);
                    score.addAndGet(5);
                } else {
                    categoryCorrect.set(false);
                    allCorrect.set(false);
                }
                userCategoryRepository.save(userCategory);
            });
            answersResp.getCategories().add(categoryPoints);
            if (categoryCorrect.get()) {
                score.addAndGet(10);
                categoryPoints.setPoints(categoryPoints.getPoints() + 10);
            }
        });
        if (allCorrect.get()) {
            score.addAndGet(20);
        }
        answersResp.setScore(score.get());
        answersResp.setAllCorrect(allCorrect.get());
        if (leagueId == 1) {
            final List<LocalDate> dates = userLeagueQuestionRepository.findByLeague_IdAndUser_IdOrderByDateDesc(1, userId).stream().map(UserLeagueQuestionEntity::getDate).distinct().toList();
            int count = 0;
            if (!dates.isEmpty() && !dates.get(0).isBefore(LocalDate.now().minusDays(1))) {
                count++;
                for (int i = 1; i < dates.size(); i++) {
                    if (dates.get(i).plusDays(1).isEqual(dates.get(i - 1))) {
                        count++;
                    } else {
                        break;
                    }
                }
            }
            if (count > 0 && count % 20 == 0) {
                final UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Nema usera!"));
                user.setCredits(user.getUserXp() + 100);
                userRepository.save(user);
            }
        }
        final UserLeagueEntity userLeague = userLeagueRepository.findByUser_IdAndLeague_IdAndDate(userId, leagueId, LocalDate.now()).orElseThrow(() -> new RuntimeException("User league!"));
        userLeague.setScore(userLeague.getScore() + score.get());
        userLeague.getUser().setUserXp(userLeague.getUser().getUserXp() + score.get());
        userLeagueRepository.save(userLeague);
        userLeagueQuestionRepository.saveAll(questions);
        return answersResp;
    }

    @Override
    public QuestionsResp getQuestionsFromCategory(Integer userId, Integer categoryId) {
        final List<UserLeagueQuestionEntity> questions = userLeagueQuestionRepository.findByLeague_IdAndUser_IdAndDateAndQuestion_CategoryEntity_Id(null, userId, LocalDate.now(), categoryId);
        if (questions.isEmpty()) {
            getRandomElements(questionRepository.findByCategoryEntity_Id(categoryId), 3)
                    .forEach(question -> {
                        final UserLeagueQuestionEntity userQuestion = userLeagueQuestionRepository.findByLeague_IdAndUser_IdAndQuestion_Id(null, userId, question.getId()).orElse(new UserLeagueQuestionEntity());
                        userQuestion.setUser(userRepository.findById(userId).orElseThrow());
                        userQuestion.setQuestion(question);
                        userQuestion.setLeague(null);
                        userQuestion.setDate(LocalDate.now());
                        questions.add(userQuestion);
                    });
            userLeagueQuestionRepository.saveAll(questions);
        }
        return questionMapper.toQuestionsResp(questions.stream().map(UserLeagueQuestionEntity::getQuestion).toList());
    }

    @Override
    public AnswersResp checkAnswersFromCategory(Integer categoryId, Integer userId, AnswersReq answersReq) {
        final AnswersResp answersResp = new AnswersResp();
        final List<UserLeagueQuestionEntity> questions = userLeagueQuestionRepository.findByLeague_IdAndUser_IdAndDateAndQuestion_CategoryEntity_Id(null, userId, LocalDate.now(), categoryId);
        AtomicInteger score = new AtomicInteger();
        answersReq.getAnswers().forEach(answer -> questions.stream().filter(e -> e.getQuestion().getId().equals(answer.getQuestionId())).findFirst().orElseThrow().setIsCorrect(answer.getCorrect()));
        final UserCategoryEntity userCategory = userCategoryRepository.findByUser_IdAndCategory_Id(userId, categoryId).get();
        final AnswersResp.CategoryPoints categoryPoints = new AnswersResp.CategoryPoints();
        categoryPoints.setCategoryName(userCategory.getCategory().getCategoryName());
        questions.forEach(q -> {
            categoryPoints.setNumberOfQuestions(categoryPoints.getNumberOfQuestions() + 1);
            userCategory.setAnsweredTotal(userCategory.getAnsweredTotal() + 1);
            if (Boolean.TRUE.equals(q.getIsCorrect())) {
                userCategory.setAnsweredCorrect(userCategory.getAnsweredCorrect() + 1);
                categoryPoints.setNumberOfCorrect(categoryPoints.getNumberOfCorrect() + 1);
                categoryPoints.setPoints(categoryPoints.getPoints() + 5);
                score.addAndGet(5);
            }
        });
        if (score.get() == 15) {
            score.addAndGet(10);
            answersResp.setAllCorrect(true);
        }else {
            answersResp.setAllCorrect(false);
        }
        answersResp.setScore(score.get());
        answersResp.getCategories().add(categoryPoints);
        final UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User with this id doesen't exist!"));
        user.setUserXp(user.getUserXp() + score.get());
        userRepository.save(user);
        userLeagueQuestionRepository.saveAll(questions);
        return answersResp;
    }

    @Scheduled(cron = "10 0 0 * * *")
    public void createUserLeague() {
        userLeagueRepository.findByDateAndLeague_ActiveTrue(LocalDate.now().minusDays(1))
                .forEach(pastUserLeague -> {
                    final UserLeagueEntity userLeague = new UserLeagueEntity();
                    userLeague.setIsAdmin(pastUserLeague.getIsAdmin());
                    userLeague.setDate(LocalDate.now());
                    userLeague.setUser(pastUserLeague.getUser());
                    userLeague.setLeague(pastUserLeague.getLeague());
                    userLeague.setScore(pastUserLeague.getScore());
                    userLeagueRepository.save(userLeague);
                });
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
