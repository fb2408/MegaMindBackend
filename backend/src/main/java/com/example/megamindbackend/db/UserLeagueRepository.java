package com.example.megamindbackend.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserLeagueRepository extends JpaRepository<UserLeagueEntity, Integer> {

    @Modifying
    @Query("delete from UserLeagueEntity where user.id = :userId and league.id = :leagueId")
    void deleteUserFromLeague(Integer userId, Integer leagueId);


    List<UserLeagueEntity> findByDateAndLeague_ActiveTrue(LocalDate date);


    List<UserLeagueEntity> findByUser_IdAndLeague_Id(Integer userId, Integer leagueId);


    int countByLeague_IdAndDateAndScoreNot(Integer id, LocalDate date, Integer score);


    String QUERY = """
            update user_league
            SET rank = sub.rank
            from (select ul1.user_league_id, RANK() OVER ( order by ul1.score DESC ) as rank
                  from user_league ul1
                  where ul1.date_ul = CURRENT_DATE  and ul1.league_id = :leagueId) as sub
            where user_league.user_league_id = sub.user_league_id;
                        
            update user_league
            SET up_or_down = sub.up_or_down
            from (select ul1.user_league_id,
                         case when ul1.rank < ul2.rank then 1 when ul1.rank > ul2.rank then -1 else 0 END up_or_down
                  from user_league ul1
                           join user_league ul2
                                on ul1.user_id = ul2.user_id and ul1.league_id = ul2.league_id and ul1.date_ul = ul2.date_ul + 1
                  where ul1.date_ul = CURRENT_DATE  and ul1.league_id = :leagueId) as sub
            where user_league.user_league_id = sub.user_league_id;
            """;

    @Modifying
    @Query(value = QUERY, nativeQuery = true)
    void updateRanks(Integer leagueId);

    List<UserLeagueEntity> findByUser_IdAndDateAndLeague_ActiveTrue(Integer id, LocalDate date);

    List<UserLeagueEntity> findByUser_IdAndDateAndLeague_StartDateGreaterThan(Integer id, LocalDate date, LocalDate startDate);


    Optional<UserLeagueEntity> findByUser_IdAndLeague_IdAndDate(Integer id, Integer id1, LocalDate date);

    int countByLeague_IdAndDate(Integer id, LocalDate date);

    List<UserLeagueEntity> findByLeague_IdAndDate(Integer id, LocalDate date);


}