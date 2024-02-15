package com.example.megamindbackend.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserLeagueQuestionRepository extends JpaRepository<UserLeagueQuestionEntity, Integer> {
    List<UserLeagueQuestionEntity> findByLeague_IdAndDate(Integer id, LocalDate date);


    boolean existsByUser_IdAndQuestion_CategoryEntity_IdAndDateAndLeagueNull(Integer id, Integer id1, LocalDate date);


    List<UserLeagueQuestionEntity> findByLeague_IdAndDateAndUser_IdNull(Integer id, LocalDate date);


    List<UserLeagueQuestionEntity> findByLeague_IdAndUser_IdAndDate(Integer leagueId, Integer userId, LocalDate date);

    boolean existsByLeague_IdAndQuestion_Id(Integer id, Integer id1);

    boolean existsByLeague_IdAndUser_IdAndDate(Integer id, Integer id1, LocalDate date);

    List<UserLeagueQuestionEntity> findByLeague_IdAndUser_IdOrderByDateDesc(Integer id, Integer id1);

    Optional<UserLeagueQuestionEntity> findByLeague_IdAndUser_IdAndQuestion_Id(Integer id, Integer id1, Integer id2);

    List<UserLeagueQuestionEntity> findByLeague_IdAndUser_IdAndDateAndQuestion_CategoryEntity_Id(Object o, Integer userId, LocalDate now, Integer categoryId);
}