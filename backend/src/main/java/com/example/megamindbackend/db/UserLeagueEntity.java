package com.example.megamindbackend.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "user_league")
public class UserLeagueEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_league_id_seq"
    )
    @SequenceGenerator(
            name = "user_league_id_seq",
            sequenceName = "user_league_id_seq",
            allocationSize = 1
    )
    @Column(name = "user_league_id", nullable = false)
    private Integer id;

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin = false;

    @Column(name = "score", nullable = false)
    private Integer score = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "league_id", nullable = false)
    private LeagueEntity league;

    @Column(name = "date_ul", nullable = false)
    private LocalDate date;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "up_or_down")
    private Integer upOrDown = 0;

}