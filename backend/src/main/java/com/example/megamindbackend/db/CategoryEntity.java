package com.example.megamindbackend.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "category")
public class CategoryEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_id_seq"
    )
    @SequenceGenerator(
            name = "category_id_seq",
            sequenceName = "category_id_seq",
            allocationSize = 1
    )
    @Column(name = "category_id", nullable = false)
    private Integer id;

    @Column(name = "category_name", nullable = false, length = Integer.MAX_VALUE)
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<CategoryLeagueEntity> categoryLeagues = new ArrayList<>();

    public void addLeague(CategoryLeagueEntity league) {
        league.setCategory(this);
        categoryLeagues.add(league);
    }

}