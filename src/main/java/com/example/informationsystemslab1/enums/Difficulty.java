package com.example.informationsystemslab1.enums;

import lombok.Getter;

@Getter
public enum Difficulty {
    NORMAL(10),
    HARD(20),
    VERY_HARD(30),
    INSANE(40),
    HOPELESS(50);

    private final int weight;

    Difficulty(int weight) {
        this.weight = weight;
    }

}
