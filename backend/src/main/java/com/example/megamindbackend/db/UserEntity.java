package com.example.megamindbackend.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_seq"
    )
    @SequenceGenerator(
            name = "user_id_seq",
            sequenceName = "user_id_seq",
            allocationSize = 1
    )
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "user_name", nullable = false, length = Integer.MAX_VALUE)
    private String userName;

    @Column(name = "password", nullable = false, length = Integer.MAX_VALUE)
    private String password;

    @Column(name = "level", nullable = false)
    private Integer level = 1;

    @Column(name = "user_xp", nullable = false)
    private Integer userXp = 0;


    @Column(name = "credits", nullable = false)
    private Integer credits = 0;

    @OneToMany(mappedBy = "user")
    private List<UserLeagueEntity> userLeagues = new ArrayList<>();

    public void addLeague(UserLeagueEntity league) {
        league.setUser(this);
        userLeagues.add(league);
    }

}
