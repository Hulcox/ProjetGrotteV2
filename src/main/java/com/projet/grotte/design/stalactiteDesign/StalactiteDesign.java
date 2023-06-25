package com.projet.grotte.design.stalactiteDesign;
import com.projet.grotte.algorithm.CaveSimulation;
import com.projet.grotte.algorithm.Concretion;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.projet.grotte.CaveSimulation.SIZE_CAVE_Y;

@Getter
@Setter
public class StalactiteDesign extends Concretion {

    private double size;
    private int index;
    private boolean isOnDrapery = false;

    private Polygon stalactiteTriangle;

    public StalactiteDesign(double posX, double diametre, double size, int index) {
        super(posX, diametre);
        this.index = index;
        this.size = size;

        double x1 = posX-diametre; // Point inférieur gauche (coordonnée x)
        double y1 = SIZE_CAVE_Y; // Point inférieur gauche (coordonnée y)
        double x2 = posX+diametre; // Point inférieur droit (coordonnée x)
        double y2 = SIZE_CAVE_Y; // Point inférieur droit (coordonnée y)
        double x3 = posX; // Point supérieur (coordonnée x)
        double y3 = SIZE_CAVE_Y+this.size; // Point supérieur (coordonnée y)

        this.stalactiteTriangle = new Polygon(x1, y1, x2, y2, x3, y3);
        this.stalactiteTriangle.setFill(Color.ROSYBROWN);
    }

    public Polygon getStalactiteTriangle() {
        return stalactiteTriangle;
    }

    @Override
    public void evolve(double newWeight, double newLimestone, double newDiameter) {
        //TODO
    }

    public static boolean isTwoStalactitesAreTouching(com.projet.grotte.design.stalactiteDesign.StalactiteDesign stalactite1, com.projet.grotte.design.stalactiteDesign.StalactiteDesign stalactite2) {
        double[] stalactiteFirstPosition = getSurfaceCoveredByStalactite(stalactite1);
        double[] stalactiteSecondPosition = getSurfaceCoveredByStalactite(stalactite2);
        //System.out.println(Arrays.toString(stalactiteFirstPosition) + " - " + Arrays.toString(stalactiteSecondPosition));
        return CaveSimulation.checkValuesAreInRange(stalactiteFirstPosition, stalactiteSecondPosition);
    }

    public static String stalactitesToString(List<com.projet.grotte.design.stalactiteDesign.StalactiteDesign> stalactites) {
        StringBuilder stalactitesStringified = new StringBuilder();
        stalactitesStringified.append("\n\n---------- STALACTITES ----------");
        stalactites.forEach(stalactite -> {
                    stalactitesStringified.append("\nStalactite N°").append(stalactite.getIndex())
                            .append("\n\tPosition : ")
                            .append(stalactite.getPosX())
                            .append("\n\tDiamètre : ")
                            .append(stalactite.getDiameter())
                            .append("\n\tTaille : ")
                            .append(stalactite.getSize());
                    if (stalactite.isOnDrapery()) {
                        stalactitesStringified.append("\n\tEst dans une draperie");
                    }
                }
        );
        return stalactitesStringified.toString();
    }

    public static double[] getSurfaceCoveredByStalactite(com.projet.grotte.design.stalactiteDesign.StalactiteDesign stalactite) {
        double positionMin = stalactite.getPosX() - stalactite.getDiameter() / 2;
        double positionMax = stalactite.getPosX() + stalactite.getDiameter() / 2;
        return new double[]{positionMin, positionMax};
    }

}
