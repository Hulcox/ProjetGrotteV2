package com.projet.grotte.design.stalagmiteDesign;

import com.projet.grotte.algorithm.Concretion;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.projet.grotte.CaveSimulation.FLOOR_Y;
import static com.projet.grotte.CaveSimulation.SIZE_CAVE_Y;

@Setter
@Getter
public class StalagmiteDesign extends Concretion {

    private double size;

    private Polygon stalagmiteTriangle;

    public StalagmiteDesign(double posX, double diametre, double size) {
        super(posX, diametre);
        this.size = size;

        double x1 = posX-diametre; // Point inférieur gauche (coordonnée x)
        double y1 = FLOOR_Y; // Point inférieur gauche (coordonnée y)
        double x2 = posX+diametre; // Point inférieur droit (coordonnée x)
        double y2 = FLOOR_Y; // Point inférieur droit (coordonnée y)
        double x3 = posX; // Point supérieur (coordonnée x)
        double y3 = FLOOR_Y-size; // Point supérieur (coordonnée y)

        this.stalagmiteTriangle = new Polygon(x1, y1, x2, y2, x3, y3);
        this.stalagmiteTriangle.setFill(Color.ROSYBROWN);
    }

    public Polygon getStalagmiteTriangle() {
        return stalagmiteTriangle;
    }

    public static double[] getSurfaceCoveredByStalagmite(com.projet.grotte.design.stalagmiteDesign.StalagmiteDesign stalagmite) {
        double positionMin = stalagmite.getPosX() - stalagmite.getDiameter() / 2;
        double positionMax = stalagmite.getPosX() + stalagmite.getDiameter() / 2;
        return new double[]{positionMin, positionMax};
    }

    public static String stalagmitesToString(List<com.projet.grotte.design.stalagmiteDesign.StalagmiteDesign> stalagmites) {
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
