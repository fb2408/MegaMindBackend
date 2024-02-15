package com.example.megamindbackend.service.impl;

import com.example.megamindbackend.QuestionUtil;
import com.example.megamindbackend.db.*;
import com.example.megamindbackend.dto.CategoriesResp;
import com.example.megamindbackend.dto.DeleteUserReq;
import com.example.megamindbackend.dto.LeagueReq;
import com.example.megamindbackend.dto.LeagueResp;
import com.example.megamindbackend.mapper.LeagueMapper;
import com.example.megamindbackend.service.LeagueService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class LeagueServiceImpl implements LeagueService {

    private final LeagueMapper leagueMapper;
    private final LeagueRepository leagueRepository;
    private final UserLeagueRepository userLeagueRepository;
    private final CategoryLeagueRepository categoryLeagueRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionUtil questionUtil;
    private final UserRepository userRepository;


    @Override
    public LeagueResp addLeague(LeagueReq leagueReq) {
        final LeagueEntity league = new LeagueEntity();
        leagueMapper.toLeagueEntity(league, leagueReq);
        final UserEntity user = userRepository.findById(leagueReq.getLeagueAdmin().orElseThrow()).orElseThrow();

        final UserLeagueEntity leagueAdmin = new UserLeagueEntity();
        leagueAdmin.setIsAdmin(true);
        leagueAdmin.setDate(LocalDate.now().minusDays(1));
        leagueAdmin.setLeague(league);
        leagueAdmin.setRank(1);
        leagueAdmin.setUser(user);
        user.addLeague(leagueAdmin);

        final UserLeagueEntity leagueAdminToday = new UserLeagueEntity();
        leagueAdminToday.setIsAdmin(true);
        leagueAdminToday.setDate(LocalDate.now());
        leagueAdminToday.setLeague(league);
        leagueAdminToday.setRank(1);
        leagueAdminToday.setUser(user);
        user.addLeague(leagueAdminToday);


        league.addUser(leagueAdmin);
        league.addUser(leagueAdminToday);

        league.setInvitationCode(getSaltString());
        if (leagueReq.getLeagueCategories().get().size() < league.getDailyCategories()) {
            throw new RuntimeException("Number of categories must be equal or bigger then number of daily categories!");
        }
        leagueRepository.save(league);
        userLeagueRepository.save(leagueAdmin);
        userLeagueRepository.save(leagueAdminToday);
        leagueReq.getLeagueCategories().get().forEach(category -> {
            final CategoryEntity categoryEntity = categoryRepository.findById(category).orElseThrow();
            final CategoryLeagueEntity categoryLeague = new CategoryLeagueEntity();
            categoryLeague.setDate(LocalDate.now());
            categoryEntity.addLeague(categoryLeague);
            league.addCategory(categoryLeague);
            categoryRepository.save(categoryEntity);
            categoryLeagueRepository.save(categoryLeague);
        });
        leagueRepository.save(league);
        questionUtil.addQuestionsToLeague(league);
        return leagueMapper.toLeagueResp(league);
    }

    @Override
    public LeagueResp updateLeague(Integer id, LeagueReq leagueReq) {
        final LeagueEntity leagueEntity = leagueRepository.findById(id).orElseThrow(() -> new RuntimeException("League doesn't exist!"));
        leagueMapper.toLeagueEntity(leagueEntity, leagueReq);
        if (leagueReq.getLeagueCategories().isPresent()) {
            if (leagueReq.getLeagueCategories().get().size() < leagueEntity.getDailyCategories()) {
                throw new RuntimeException("Number of categories must be equal or bigger then number of daily categories!");
            }
            categoryLeagueRepository.deleteAll(leagueEntity.getCategoryLeagues());
            leagueReq.getLeagueCategories().orElseThrow().forEach(category -> {
                final CategoryEntity categoryEntity = categoryRepository.findById(category).orElseThrow();
                final CategoryLeagueEntity categoryLeague = new CategoryLeagueEntity();
                categoryLeague.setDate(LocalDate.now());
                categoryEntity.addLeague(categoryLeague);
                leagueEntity.addCategory(categoryLeague);
                categoryRepository.save(categoryEntity);
                categoryLeagueRepository.save(categoryLeague);
            });
        }
        leagueRepository.save(leagueEntity);
        return leagueMapper.toLeagueResp(leagueEntity);
    }

    @Override
    public LeagueResp addUser(Integer leagueId, Integer userId) {
        final UserLeagueEntity userLeague = userLeagueRepository.findByUser_IdAndLeague_IdAndDate(userId, leagueId, LocalDate.now().minusDays(1)).orElse(new UserLeagueEntity());
        final LeagueEntity league = leagueRepository.findById(leagueId).orElseThrow();
        userLeague.setLeague(league);
        userLeague.setUser(userRepository.findById(userId).orElseThrow());
        userLeague.setDate(LocalDate.now().minusDays(1));
        userLeague.setRank(userLeagueRepository.countByLeague_IdAndDateAndScoreNot(leagueId, LocalDate.now().minusDays(1), 0) + 1);
        league.addUser(userLeague);

        final UserLeagueEntity userLeagueToday = userLeagueRepository.findByUser_IdAndLeague_IdAndDate(userId, leagueId, LocalDate.now()).orElse(new UserLeagueEntity());
        userLeagueToday.setLeague(league);
        userLeagueToday.setUser(userRepository.findById(userId).orElseThrow());
        userLeagueToday.setDate(LocalDate.now());
        userLeagueToday.setRank(userLeagueRepository.countByLeague_IdAndDateAndScoreNot(leagueId, LocalDate.now().minusDays(1), 0) + 1);
        league.addUser(userLeagueToday);

        leagueRepository.save(league);
        userLeagueRepository.saveAndFlush(userLeague);
        userLeagueRepository.save(userLeagueToday);
        return leagueMapper.toLeagueResp(league);
    }

    @Override
    public LeagueResp joinLeague(Integer userId, String leagueCode) {
        final LeagueEntity leagueEntity = leagueRepository.findByInvitationCodeAndActiveTrue(leagueCode).orElseThrow(() -> new RuntimeException("Active league with that invitation code doesen't exist!"));
        return addUser(leagueEntity.getId(), userId);
    }

    @Override
    public CategoriesResp getCategories() {
        return leagueMapper.toCategoriesResp(categoryRepository.findAll());
    }

    @Override
    public void removeUser(Integer leagueId, DeleteUserReq deleteUserReq) {
        if (!Objects.equals(deleteUserReq.getUserId(), deleteUserReq.getUserToDeleteId())) {
            final UserLeagueEntity userLeague = userLeagueRepository.findByUser_IdAndLeague_Id(deleteUserReq.getUserId(), leagueId).stream().findFirst().orElseThrow();
            if (Boolean.FALSE.equals(userLeague.getIsAdmin())) {
                return;
            }
        }
        userLeagueRepository.deleteUserFromLeague(deleteUserReq.getUserToDeleteId(), leagueId);
    }

    @Scheduled(cron = "5 0 0 * * *")
    @Transactional
    public void checkLeagueEnding() {
        leagueRepository.findAll().forEach(league -> {
            if (league.getStartDate().plusDays(league.getSeasonLength()).isEqual(LocalDate.now())) {
                league.setActive(false);
                final List<UserLeagueEntity> users = userLeagueRepository.findByLeague_IdAndDate(league.getId(), LocalDate.now().minusDays(1));
                users.stream().sorted(Comparator.comparing(UserLeagueEntity::getRank)).forEach(e -> e.getUser().setCredits(e.getUser().getCredits() + (users.size()) * 10 / e.getRank()));
            } else if (league.getStartDate().isEqual(LocalDate.now())) {
                league.setActive(true);
            }
        });
    }

    private String getSaltString() {
        final String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        final StringBuilder salt = new StringBuilder();
        final Random rnd = new Random();
        while (salt.length() < 5) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        if (leagueRepository.existsByInvitationCode(salt.toString())) {
            return getSaltString();
        }
        return salt.toString();
    }


}
