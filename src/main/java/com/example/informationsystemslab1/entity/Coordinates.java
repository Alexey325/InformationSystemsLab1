package com.example.informationsystemslab1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Coordinates {

    @Column(nullable = false)
    private float x;

    @NotNull
    @Column(nullable = false)
    private Integer y;
}
