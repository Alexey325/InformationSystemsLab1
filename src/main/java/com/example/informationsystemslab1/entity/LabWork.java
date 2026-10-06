package com.example.informationsystemslab1.entity;

import com.example.informationsystemslab1.entity.enums.Difficulty;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    // LAZY не работает без EclipseLink weaving — фактически всегда EAGER (см. STUDY_NOTES.md)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discipline_id", nullable = false)
    @JsonManagedReference("discipline-labworks")
    private Discipline discipline;

    @Positive
    @Column(nullable = false)
    private int minimalPoint;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Long personalQualitiesMaximum;

    @Column(nullable = false)
    private int tunedInWork;

    @ManyToMany
    @JoinTable(
            name = "labWork_person",
            joinColumns = @JoinColumn(name = "labWork_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    @JsonManagedReference("labwork-authors")
    private Set<Person> authors = new HashSet<>();

}