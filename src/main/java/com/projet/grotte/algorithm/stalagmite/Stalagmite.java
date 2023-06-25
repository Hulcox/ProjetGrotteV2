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

    public static String stalagmitesToString(List<Stalagmite> stalagmites) {
        if (!stalagmites.isEmpty()) {
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
        return "";
    }
}
