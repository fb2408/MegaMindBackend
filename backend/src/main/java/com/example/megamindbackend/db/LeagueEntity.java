package com.example.megamindbackend.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "league")
public class LeagueEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "league_id_seq"
    )
    @SequenceGenerator(
            name = "league_id_seq",
            sequenceName = "league_id_seq",
            allocationSize = 1
    )
    @Column(name = "league_id", nullable = false)
    private Integer id;

    @Column(name = "league_name", nullable = false, length = Integer.MAX_VALUE)
    private String leagueName;

    @Column(name = "season_length", nullable = false)
    private Integer seasonLength;

    @Column(name = "invitation_code", nullable = false, length = Integer.MAX_VALUE)
    private String invitationCode;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "daily_categories", nullable = false)
    private Integer dailyCategories;

    @Column(name = "questions_per_category", nullable = false)
    private Integer questionsPerCategory;

    @OneToMany(mappedBy = "league")
    private List<CategoryLeagueEntity> categoryLeagues = new ArrayList<>();

    @OneToMany(mappedBy = "league")
    private List<UserLeagueEntity> userLeagues = new ArrayList<>();

    @Column(name = "active")
    private Boolean active = true;

    public void addCategory(CategoryLeagueEntity category) {
        category.setLeague(this);
        categoryLeagues.add(category);
    }

    public void addUser(UserLeagueEntity user) {
        user.setLeague(this);
        userLeagues.add(user);
    }
}