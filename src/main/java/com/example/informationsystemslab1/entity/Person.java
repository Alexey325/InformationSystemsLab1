package com.example.informationsystemslab1.entity;

import com.example.informationsystemslab1.entity.enums.Color;
import com.example.informationsystemslab1.entity.enums.Country;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Color eyeColor;

    @Enumerated(EnumType.STRING)
    private Color hairColor;

    // LAZY не работает без EclipseLink weaving — фактически всегда EAGER (см. STUDY_NOTES.md)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    @JsonManagedReference("location-people")
    private Location location;

    @NotBlank
    @Size(max = 24)
    @Column(nullable = false, length = 24)
    private String passportID;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Country nationality;

    @ManyToMany(mappedBy = "authors")
    @JsonBackReference("labwork-authors")
    private Set<LabWork> labWorks = new HashSet<>();
}
