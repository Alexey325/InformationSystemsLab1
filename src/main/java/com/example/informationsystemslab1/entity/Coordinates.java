package com.example.informationsystemslab1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class Coordinates {

    @Column(nullable = false)
    private float x;

    @NotNull
    @Column(nullable = false)
    private Integer y;

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public Integer getY() { return y; }
    public void setY(Integer y) { this.y = y; }
}
