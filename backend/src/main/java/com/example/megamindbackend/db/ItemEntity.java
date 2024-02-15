package com.example.megamindbackend.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "item")
public class ItemEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "item_id_seq"
    )
    @SequenceGenerator(
            name = "item_id_seq",
            sequenceName = "item_id_seq",
            allocationSize = 1
    )
    @Column(name = "item_id", nullable = false)
    private Integer id;

    @Column(name = "item_name", nullable = false, length = Integer.MAX_VALUE)
    private String itemName;

    @Column(name = "item_cost", nullable = false)
    private Double itemCost;

}