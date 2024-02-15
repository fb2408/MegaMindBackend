package com.example.megamindbackend.service.impl;

import com.example.megamindbackend.db.*;
import com.example.megamindbackend.dto.*;
import com.example.megamindbackend.mapper.LeagueMapper;
import com.example.megamindbackend.mapper.UserMapper;
import com.example.megamindbackend.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final LeagueMapper leagueMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final UserLeagueQuestionRepository userLeagueQuestionRepository;
    private final UserLeagueRepository userLeagueRepository;
    private final LeagueRepository leagueRepository;

    @Override
    public UserResp addUser(UserReq req) {
        final UserEntity userEntity = new UserEntity();
        userMapper.toUserEntity(userEntity, req);
        userRepository.save(userEntity);
        categoryRepository.findAll().forEach(e -> {
            final UserCategoryEntity userCategory = new UserCategoryEntity();
            userCategory.setUser(userEntity);
            userCategory.setCategory(e);
            userCategory.setWeight(0.0);
            if (req.getFavouriteCategoryIds().get().contains(e.getId())) {
                userCategory.setFavouriteCategory(true);
            }
            userCategoryRepository.save(userCategory);
        });

        final LeagueEntity league = leagueRepository.findById(1).orElseThrow();
        final UserLeagueEntity userLeague = new UserLeagueEntity();
        userLeague.setLeague(league);
        userLeague.setUser(userRepository.findById(userEntity.getId()).orElseThrow());
        userLeague.setDate(LocalDate.now().minusDays(1));
        userLeague.setRank(userLeagueRepository.countByLeague_IdAndDateAndScoreNot(1, LocalDate.now().minusDays(1), 0) + 1);
        league.addUser(userLeague);

        final UserLeagueEntity userLeagueToday = new UserLeagueEntity();
        userLeagueToday.setLeague(league);
        userLeagueToday.setUser(userRepository.findById(userEntity.getId()).orElseThrow());
        userLeagueToday.setDate(LocalDate.now());
        userLeagueToday.setRank(userLeagueRepository.countByLeague_IdAndDateAndScoreNot(1, LocalDate.now().minusDays(1), 0) + 1);
        league.addUser(userLeagueToday);

        leagueRepository.save(league);
        userLeagueRepository.saveAndFlush(userLeague);
        userLeagueRepository.save(userLeagueToday);
        return userMapper.toUserResp(userEntity);
    }

    @Override
    public UserResp updateUser(Integer id, UserReq req) {
        final UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User doesn't exist!"));
        userMapper.toUserEntity(userEntity, req);
        if (req.getFavouriteCategoryIds().isPresent()) {
            userCategoryRepository.findByUser_Id(id).forEach(userCategoryEntity -> {
                userCategoryEntity.setFavouriteCategory(req.getFavouriteCategoryIds().get().contains(userCategoryEntity.getCategory().getId()));
                userCategoryRepository.save(userCategoryEntity);
            });
        }
        userRepository.save(userEntity);
        return userMapper.toUserResp(userEntity);
    }

    @Override
    public MainPageResp getMainPage(Integer userId) {
        final UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User doesn't exist!"));
        final MainPageResp mainPageResp = new MainPageResp();
        mainPageResp.setUserId(userId);
        mainPageResp.setUsername(userEntity.getUserName());
        userCategoryRepository.findByUser_IdAndFavouriteCategoryTrue(userId).stream().forEach(c -> {
            final MainPageResp.Category category = new MainPageResp.Category();
            category.setCategoryId(c.getCategory().getId());
            category.setCategoryName(c.getCategory().getCategoryName());
            category.setDailyDone(userLeagueQuestionRepository.existsByUser_IdAndQuestion_CategoryEntity_IdAndDateAndLeagueNull(userId, c.getCategory().getId(), LocalDate.now()));
            mainPageResp.getFavouriteCategories().add(category);
        });
        mainPageResp.setGlobalDone(userLeagueQuestionRepository.existsByLeague_IdAndUser_IdAndDate(1, userId, LocalDate.now()));
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
            mainPageResp.setDaysInARow(20);
        } else {
            mainPageResp.setDaysInARow(count % 20);
        }
        return mainPageResp;
    }

    @Override
    @Transactional
    @Modifying
    public LeaguesResp getLeagues(Integer userId) {
        return userMapper.toLeaguesResp(userLeagueRepository.findByUser_IdAndDateAndLeague_ActiveTrue(userId, LocalDate.now().minusDays(1)), userLeagueRepository.findByUser_IdAndDateAndLeague_StartDateGreaterThan(userId, LocalDate.now().minusDays(1), LocalDate.now()));
    }

    @Override
    public LeagueResp getLeague(Integer userId, Integer leagueId) {
        final LeagueResp leagueResp = leagueMapper.toLeagueResp(leagueRepository.findById(leagueId).orElseThrow());
        leagueResp.setQuizDone(userLeagueQuestionRepository.existsByLeague_IdAndUser_IdAndDate(leagueId, userId, LocalDate.now()));
        return leagueResp;
    }

    @Override
    public ProfileResp getProfile(Integer userId) {
        final UserEntity userEntity = userRepository.findById(userId).orElseThrow();
        final List<UserCategoryEntity> userCategories = userCategoryRepository.findByUser_Id(userId);
        final ProfileResp profileResp = userMapper.toProfileResp(userEntity, userCategories);
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
        profileResp.setDaysInARow(count);
        final UserLeagueEntity userLeague = userLeagueRepository.findByUser_IdAndLeague_IdAndDate(userId, 1, LocalDate.now().minusDays(1)).orElseThrow();
        profileResp.setGlobalLeagueRank(userLeague.getRank());
        profileResp.setGlobalLeagueMembers(userLeagueRepository.countByLeague_IdAndDate(1, LocalDate.now().minusDays(1)));
        return profileResp;
    }

    @Override
    public MainPageResp login(LoginReq loginReq) {
        return getMainPage(userRepository.findByUserNameAndPassword(loginReq.getUsername(), loginReq.getPassword()).orElseThrow(() -> new RuntimeException("Username or password is incorrect")).getId());
    }


}
