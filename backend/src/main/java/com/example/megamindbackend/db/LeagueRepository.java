package com.example.megamindbackend.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeagueRepository extends JpaRepository<LeagueEntity, Integer> {
    List<LeagueEntity> findByActiveTrue();

    Optional<LeagueEntity> findByInvitationCodeAndActiveTrue(String invitationCode);

    boolean existsByInvitationCode(String invitationCode);
}