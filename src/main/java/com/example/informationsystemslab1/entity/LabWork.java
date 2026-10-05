package com.example.informationsystemslab1.entity;

import com.example.informationsystemslab1.entity.enums.Difficulty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table()
public class LabWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Embedded
    private Coordinates coordinates;

    @Column(nullable = false)
    private LocalDate creationDate;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column
    private Long discipline_id;

    @Positive
    @Column(nullable = false)
    private int minimalPoint;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Long personalQualitiesMaximum;

    @Column(nullable = false)
    private int tunedInWork;

    @Column
    private Long person_id;

}