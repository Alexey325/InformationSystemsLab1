package com.example.informationsystemslab1.entity;

import com.example.informationsystemslab1.entity.enums.Difficulty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

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

    public Long getId() { return id; }
    public String getName() { return name; }
    public Coordinates getCoordinates() { return coordinates; }
    public LocalDate getCreationDate() { return creationDate; }
    public String getDescription() { return description; }
    public Difficulty getDifficulty() { return difficulty; }
    public Long getDiscipline_id() { return discipline_id; }
    public int getMinimalPoint() { return minimalPoint; }
    public Long getPersonalQualitiesMaximum() { return personalQualitiesMaximum; }
    public int getTunedInWork() { return tunedInWork; }
    public Long getPerson_id() { return person_id; }

    public void setName(String name) { this.name = name; }
    public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }
    public void setCreationDate(LocalDate creationDate) { this.creationDate = creationDate; }
    public void setDescription(String description) { this.description = description; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public void setDiscipline_id(Long discipline_id) { this.discipline_id = discipline_id; }
    public void setMinimalPoint(int minimalPoint) { this.minimalPoint = minimalPoint; }
    public void setPersonalQualitiesMaximum(Long personalQualitiesMaximum) { this.personalQualitiesMaximum = personalQualitiesMaximum; }
    public void setTunedInWork(int tunedInWork) { this.tunedInWork = tunedInWork; }
    public void setPerson_id(Long person_id) { this.person_id = person_id; }

}