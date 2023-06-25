package com.projet.grotte.algorithm.drapery;

import com.projet.grotte.algorithm.CaveSimulation;
import com.projet.grotte.algorithm.Concretion;
import com.projet.grotte.algorithm.stalactite.Stalactite;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Drapery extends Concretion {

    private List<Stalactite> stalactites;

    public Drapery(double posX, double diameter, List<Stalactite> stalactites) {
        super(posX, diameter);
        this.stalactites = stalactites;
    }

    public void evolve(List<Stalactite> stalactites) {
        double posXTotal = 0;
        double diameterTotal = 0;
        for (Stalactite stalactite : stalactites) {
            posXTotal += stalactite.getPosX();
            diameterTotal += stalactite.getDiameter();
        }

        double newPosX = Math.round(posXTotal / stalactites.size());
        double newDiameter = Math.round(diameterTotal);
        Set<Stalactite> newDraperyStalactites = new HashSet<>();
        newDraperyStalactites.addAll(this.stalactites);
        newDraperyStalactites.addAll(stalactites);

        super.setDiameter(newDiameter);
        super.setPosX(newPosX);
        setStalactites(newDraperyStalactites.stream().toList());
    }

    public static List<Drapery> shouldDraperyBeCreated(List<Stalactite> stalactites) {
        List<Drapery> draperies = new ArrayList<>();
        for (Stalactite stalactite1 : stalactites) {
            for (Stalactite stalactite2 : stalactites) {
                if (stalactite1.hashCode() != stalactite2.hashCode()) {
                    if (/*!stalactite1.isOnDrapery() &&*/ !stalactite2.isOnDrapery()) {
                        double[] stalactite1Surface = CaveSimulation.getSurfaceCovered(stalactite1.getPosX(), stalactite1.getDiameter());
                        double[] stalactite2Surface = CaveSimulation.getSurfaceCovered(stalactite2.getPosX(), stalactite2.getDiameter());
                        boolean isTwoStalactitesAreTouching = CaveSimulation.checkValuesAreInRange(stalactite1Surface, stalactite2Surface);
                        if (isTwoStalactitesAreTouching) {
                            stalactite1.setOnDrapery(true);
                            stalactite2.setOnDrapery(true);
                            double posX = (stalactite2.getPosX() + stalactite1.getPosX()) / 2;
                            double diameter = stalactite2.getDiameter() + stalactite1.getDiameter();
                            boolean canAddInList = draperyListContainsDraperyWithTheseStalactites(draperies, List.of(stalactite1, stalactite2));
                            if (canAddInList) {
                                draperies.add(new Drapery(posX, diameter, List.of(stalactite1, stalactite2), draperies.size() + 1));
                            }
                        }
                    }
                }
            }
        }
        return draperies;
    }

    public static void isDraperyTouchAnotherDrapery(List<Drapery> draperiesTemp, List<Drapery> draperies) {
        for (Drapery drapery1 : draperiesTemp) {
            boolean isAlreadyAdd = false;
            for (Drapery drapery2 : draperiesTemp) {
                if (drapery1.hashCode() != drapery2.hashCode()) {
                    double[] surfaceDrapery1 = CaveSimulation.getSurfaceCovered(drapery1.getPosX(), drapery1.getDiameter());
                    double[] surfaceDrapery2 = CaveSimulation.getSurfaceCovered(drapery2.getPosX(), drapery2.getDiameter());
                    boolean isTwoDraperyAreTouching = CaveSimulation.checkValuesAreInRange(surfaceDrapery1, surfaceDrapery2);
                    if (isTwoDraperyAreTouching) {
                        System.out.println("Draperie fusionne.");
                        drapery1.evolve(drapery2.getStalactites());
                        boolean canAddInList = draperyListContainsDraperyWithTheseStalactites(draperies, drapery1.getStalactites());
                        if (canAddInList) {
                            draperies.add(drapery1);
                        }
                        isAlreadyAdd = true;
                        //Faire en sorte que la draperie qui se colle ne s'ajoute qu'une unique fois
                    }
                }
            }
            //Pour ajouter la draperie à la liste si elle ne touche aucune autre
            if (!isAlreadyAdd) {
                draperies.add(drapery1);
            }
        }
        System.out.println("88888888888 " + Drapery.draperiesToString(draperies));
    }

    private static boolean draperyListContainsDraperyWithTheseStalactites(List<Drapery> draperies, List<Stalactite> stalactites) {
        for (Drapery drapery : draperies) {
            if (drapery.getStalactites().equals(stalactites)) {
                return false;
            }
        }
        return true;
    }

    public static String draperiesToString(List<Drapery> draperies) {
        if (!draperies.isEmpty()) {
            StringBuilder draperiesStringified = new StringBuilder();
            final int[] index = {1};
            draperiesStringified.append("\n\n---------- DRAPERIES ----------");
            draperies.forEach(drapery -> {
                        draperiesStringified.append("\nDraperie N°").append(index[0])
                                .append("\n\tPosition : ")
                                .append(drapery.getPosX())
                                .append("\n\tDiamètre : ")
                                .append(drapery.getDiameter())
                                .append("\n\tContient les stalactites :");
                        for (Stalactite stalactite : drapery.stalactites) {
                            draperiesStringified.append(" ")
                                    .append(stalactite.getIndex())
                                    .append(" ;");
                        }
                        index[0]++;
                    }
            );
            return draperiesStringified.toString();
        }
        return "";
    }
}
