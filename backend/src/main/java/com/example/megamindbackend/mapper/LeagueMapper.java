package com.example.megamindbackend.mapper;

import com.example.megamindbackend.db.CategoryEntity;
import com.example.megamindbackend.db.LeagueEntity;
import com.example.megamindbackend.db.UserLeagueRepository;
import com.example.megamindbackend.dto.CategoriesResp;
import com.example.megamindbackend.dto.LeagueReq;
import com.example.megamindbackend.dto.LeagueResp;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static com.example.megamindbackend.mapper.MapUtil.map;


@Component
public class LeagueMapper {

    private final UserLeagueRepository userLeagueRepository;

    public LeagueMapper(UserLeagueRepository userLeagueRepository) {
        this.userLeagueRepository = userLeagueRepository;
    }

    public void toLeagueEntity(final LeagueEntity leagueEntity, final LeagueReq leagueReq) {
        map(leagueReq.getLeagueName(), leagueEntity::setLeagueName);
        map(leagueReq.getSeasonLength(), leagueEntity::setSeasonLength);
        map(leagueReq.getStartDate(), leagueEntity::setStartDate);
        map(leagueReq.getDailyCategories(), leagueEntity::setDailyCategories);
        map(leagueReq.getQuestionsPerCategory(), leagueEntity::setQuestionsPerCategory);
        leagueEntity.setActive(leagueEntity.getStartDate().equals(LocalDate.now()));
    }
    @Transactional
    @Modifying
    public LeagueResp toLeagueResp(LeagueEntity leagueEntity) {
        final LeagueResp leagueResp = new LeagueResp();
        leagueResp.setLeagueId(leagueEntity.getId());
        leagueResp.setName(leagueEntity.getLeagueName());
        leagueResp.setSeasonLength(leagueEntity.getSeasonLength());
        leagueResp.setInvitationCode(leagueEntity.getInvitationCode());
        leagueResp.setStartDate(leagueEntity.getStartDate());
        leagueResp.setDailyCategories(leagueEntity.getDailyCategories());
        leagueResp.setQuestionsPerCategory(leagueEntity.getQuestionsPerCategory());
        leagueEntity.getCategoryLeagues().forEach(category -> {
            final LeagueResp.Category categoryResp = new LeagueResp.Category();
            categoryResp.setName(category.getCategory().getCategoryName());
            leagueResp.getCategories().add(categoryResp);
        });
        userLeagueRepository.updateRanks(leagueResp.getLeagueId());
        leagueEntity.getUserLeagues().stream().filter(e -> e.getDate().equals(LocalDate.now())).forEach(user -> {
            final LeagueResp.User userResp = new LeagueResp.User();
            userResp.setUsername(user.getUser().getUserName());
            userResp.setFirstname(user.getUser().getFirstname());
            userResp.setLastname(user.getUser().getLastname());
            userResp.setScore(user.getScore());
            userResp.setPosition(user.getRank());
            userResp.setUp(user.getUpOrDown());
            leagueResp.getUsers().add(userResp);
        });
        return leagueResp;
    }

    public CategoriesResp toCategoriesResp(List<CategoryEntity> categoryEntities) {
        final CategoriesResp categoriesResp = new CategoriesResp();
        categoryEntities.forEach(c -> {
            final CategoriesResp.Category category = new CategoriesResp.Category();
            category.setId(c.getId());
            category.setName(c.getCategoryName());
            categoriesResp.getCategories().add(category);
        });
        return categoriesResp;
    }
}
