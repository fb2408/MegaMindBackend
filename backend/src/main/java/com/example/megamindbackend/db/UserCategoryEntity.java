package com.example.megamindbackend.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_category")
public class UserCategoryEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_category_id_seq"
    )
    @SequenceGenerator(
            name = "user_category_id_seq",
            sequenceName = "user_category_id_seq",
            allocationSize = 1
    )
    @Column(name = "user_category_id", nullable = false)
    private Integer id;

    @Column(name = "weight", nullable = false)
    private Double weight = 0.0;

    @Column(name = "answered_total", nullable = false)
    private Integer answeredTotal = 0;

    @Column(name = "answered_correct", nullable = false)
    private Integer answeredCorrect = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(name = "favourite_category")
    private Boolean favouriteCategory;

}