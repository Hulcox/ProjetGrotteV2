package com.projet.grotte.algorithm.stalagmite;

import com.projet.grotte.algorithm.Concretion;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Stalagmite extends Concretion {

    private double size;

    public Stalagmite(double posX, double diametre, double size) {
        super(posX, diametre);
        this.size = size;
    }

    @Override
    public void evolve(double newWeight, double newLimestone, double newDiameter) {
        //TODO
    }

    public static double[] getSurfaceCoveredByStalagmite(Stalagmite stalagmite) {
        double positionMin = stalagmite.getPosX() - stalagmite.getDiameter() / 2;
        double positionMax = stalagmite.getPosX() + stalagmite.getDiameter() / 2;
        return new double[]{positionMin, positionMax};
    }

    public static String stalagmitesToString(List<Stalagmite> stalagmites) {
        StringBuilder stalagmitesStringified = new StringBuilder();
        final int[] index = {1};
        stalagmitesStringified.append("\n\n---------- STALAGMITES ----------");
        stalagmites.forEach(stalagmite -> {
                    stalagmitesStringified.append("\nStalagmite N°").append(index[0])
                            .append("\n\tPosition : ")
                            .append(stalagmite.getPosX())
                            .append("\n\tDiamètre : ")
                            .append(stalagmite.getDiameter())
                            .append("\n\tTaille : ")
                            .append(stalagmite.getSize());
                    index[0]++;
                }
        );
        return stalagmitesStringified.toString();
    }
}
