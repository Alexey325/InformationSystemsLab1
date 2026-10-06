package com.example.informationsystemslab1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Float x;

    @NotNull
    @Column(nullable = false)
    private Long y;

    @NotNull
    @Column(nullable = false)
    private Integer z;

    @Pattern(regexp = ".*\\S.*", message = "name must not be blank if present")
    @Column
    private String name;

    @OneToMany(mappedBy = "location")
    @JsonBackReference("location-people")
    private List<Person> people;


}
