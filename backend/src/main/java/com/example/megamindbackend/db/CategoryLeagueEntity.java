package com.example.megamindbackend.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "category_league")
public class CategoryLeagueEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_league_id_seq"
    )
    @SequenceGenerator(
            name = "category_league_id_seq",
            sequenceName = "category_league_id_seq",
            allocationSize = 1
    )
    @Column(name = "category_league_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "league_id", nullable = false)
    private LeagueEntity league;

    @Column(name = "date", nullable = false)
    private LocalDate date;

}