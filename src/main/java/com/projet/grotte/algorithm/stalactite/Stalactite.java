package com.projet.grotte.algorithm.stalactite;

import com.projet.grotte.algorithm.CaveSimulation;
import com.projet.grotte.algorithm.Concretion;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Stalactite extends Concretion {

    private double size;
    private int index;
    private boolean isOnDrapery = false;

    public Stalactite(double posX, double diametre, double size, int index) {
        super(posX, diametre);
        this.index = index;
        this.size = size;
    }

    @Override
    public void evolve(double newWeight, double newLimestone, double newDiameter) {
        //TODO
    }

    public static boolean isTwoStalactitesAreTouching(Stalactite stalactite1, Stalactite stalactite2) {
        double[] stalactiteFirstPosition = getSurfaceCoveredByStalactite(stalactite1);
        double[] stalactiteSecondPosition = getSurfaceCoveredByStalactite(stalactite2);
        //System.out.println(Arrays.toString(stalactiteFirstPosition) + " - " + Arrays.toString(stalactiteSecondPosition));
        return CaveSimulation.checkValuesAreInRange(stalactiteFirstPosition, stalactiteSecondPosition);
    }

    public static String stalactitesToString(List<Stalactite> stalactites) {
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

    public static double[] getSurfaceCoveredByStalactite(Stalactite stalactite) {
        double positionMin = stalactite.getPosX() - stalactite.getDiameter() / 2;
        double positionMax = stalactite.getPosX() + stalactite.getDiameter() / 2;
        return new double[]{positionMin, positionMax};
    }

}
