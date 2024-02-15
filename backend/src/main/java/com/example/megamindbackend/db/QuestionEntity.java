package com.example.megamindbackend.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "question")
public class QuestionEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "question_id_seq"
    )
    @SequenceGenerator(
            name = "question_id_seq",
            sequenceName = "question_id_seq",
            allocationSize = 1
    )
    @Column(name = "question_id", nullable = false)
    private Integer id;

    @Column(name = "question_text", nullable = false, length = Integer.MAX_VALUE)
    private String questionText;

    @Column(name = "question_type", nullable = false, length = Integer.MAX_VALUE)
    private String questionType = "4 answers";

    @Column(name = "global_weight", nullable = false)
    private Double globalWeight =0.0;

    @Column(name = "image", length = Integer.MAX_VALUE)
    private String image;

    @Column(name = "explanation", length = Integer.MAX_VALUE)
    private String explanation;

    @Column(name = "correct_answer", nullable = false, length = Integer.MAX_VALUE)
    private String correctAnswer;

    @Column(name = "wrong_answer1", length = Integer.MAX_VALUE)
    private String wrongAnswer1;

    @Column(name = "wrong_answer2", length = Integer.MAX_VALUE)
    private String wrongAnswer2;

    @Column(name = "wrong_answer3", length = Integer.MAX_VALUE)
    private String wrongAnswer3;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity categoryEntity;

}