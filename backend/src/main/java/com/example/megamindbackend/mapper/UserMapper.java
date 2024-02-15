package com.example.megamindbackend.mapper;

import com.example.megamindbackend.db.UserCategoryEntity;
import com.example.megamindbackend.db.UserEntity;
import com.example.megamindbackend.db.UserLeagueEntity;
import com.example.megamindbackend.dto.LeaguesResp;
import com.example.megamindbackend.dto.ProfileResp;
import com.example.megamindbackend.dto.UserReq;
import com.example.megamindbackend.dto.UserResp;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.megamindbackend.mapper.MapUtil.map;


@Component
public class UserMapper {

    public void toUserEntity(final UserEntity userEntity, final UserReq userReq) {
        map(userReq.getUserName(), userEntity::setUserName);
        map(userReq.getFirstname(), userEntity::setFirstname);
        map(userReq.getLastname(), userEntity::setLastname);
        map(userReq.getPassword(), userEntity::setPassword);
    }

    public UserResp toUserResp(final UserEntity userEntity) {
        final UserResp userResp = new UserResp();
        userResp.setId(userEntity.getId());
        userResp.setUserName(userEntity.getUserName());
        userResp.setLevel(userEntity.getLevel());
        userResp.setUserXp(userEntity.getUserXp());
        userResp.setCredits(userEntity.getCredits());
        return userResp;
    }

    public LeaguesResp toLeaguesResp(final List<UserLeagueEntity> userLeagues, final List<UserLeagueEntity> nonStarted) {
        final LeaguesResp leagueResp = new LeaguesResp();
        userLeagues.forEach(e -> {
            final LeaguesResp.League league = new LeaguesResp.League();
            league.setActive(true);
            league.setLeagueId(e.getLeague().getId());
            league.setLeagueName(e.getLeague().getLeagueName());
            league.setRank(e.getRank());
            league.setUp(e.getUpOrDown());
            leagueResp.getLeagues().add(league);
        });
        nonStarted.forEach(e -> {
            final LeaguesResp.League league = new LeaguesResp.League();
            league.setActive(false);
            league.setLeagueId(e.getLeague().getId());
            league.setLeagueName(e.getLeague().getLeagueName());
            league.setRank(e.getRank());
            league.setUp(e.getUpOrDown());
            leagueResp.getNonActiveLeagues().add(league);
        });
        return leagueResp;
    }

    public ProfileResp toProfileResp(final UserEntity userEntity, final List<UserCategoryEntity> userCategories) {
        final ProfileResp resp = new ProfileResp();
        resp.setUserName(userEntity.getUserName());
        resp.setFirstname(userEntity.getFirstname());
        resp.setLastname(userEntity.getLastname());
        resp.setLevel((int) Math.sqrt((double) userEntity.getUserXp() / 30) + 1);
        resp.setUserXp(userEntity.getUserXp());
        resp.setCredits(userEntity.getCredits());
        AtomicInteger answeredQuestions = new AtomicInteger();
        AtomicInteger answeredCorrect = new AtomicInteger();
        userCategories.forEach(e -> {
            final ProfileResp.Category category = new ProfileResp.Category();
            category.setId(e.getCategory().getId());
            category.setCategoryName(e.getCategory().getCategoryName());
            category.setAnsweredQuestions(e.getAnsweredTotal());
            answeredQuestions.addAndGet(e.getAnsweredTotal());
            category.setAnsweredCorrect(e.getAnsweredCorrect());
            answeredCorrect.addAndGet(e.getAnsweredCorrect());
            if (e.getAnsweredTotal() == 0) {

                category.setAnswerPercentage(0.0);
            } else {
                category.setAnswerPercentage((double) (e.getAnsweredCorrect()) / (double) e.getAnsweredTotal());
            }
            resp.getCategories().add(category);
        });
        resp.setAnsweredQuestions(answeredQuestions.get());
        resp.setAnsweredCorrect(answeredCorrect.get());
        resp.setAnswerPercentage((double) answeredCorrect.get() / (double) answeredQuestions.get());
        return resp;
    }
}
