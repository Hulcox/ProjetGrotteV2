package com.projet.grotte.algorithm;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Concretion {
    private double posX;
    private double diameter;

    public Concretion(double posX, double diameter) {
        this.posX = posX;
        this.diameter = diameter;
    }
}
