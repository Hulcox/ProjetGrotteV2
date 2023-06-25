package com.projet.grotte.design.draperyDesign;

import com.projet.grotte.algorithm.CaveSimulation;
import com.projet.grotte.algorithm.Concretion;
import com.projet.grotte.algorithm.drapery.Drapery;
import com.projet.grotte.algorithm.stalactite.Stalactite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.projet.grotte.design.stalactiteDesign.StalactiteDesign;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class DraperyDesign extends Concretion {

    private List<StalactiteDesign> stalactites;

    public DraperyDesign(double posX, double diameter, List<StalactiteDesign> stalactites) {
        super(posX, diameter);
        this.stalactites = stalactites;
    }

    public void evolve(List<StalactiteDesign> stalactites) {
        double posXTotal = 0;
        double diameterTotal = 0;
        for (StalactiteDesign stalactite : stalactites) {
            posXTotal += stalactite.getPosX();
            diameterTotal += stalactite.getDiameter();
        }

        double newPosX = Math.round(posXTotal / stalactites.size());
        double newDiameter = Math.round(diameterTotal);
        Set<StalactiteDesign> newDraperyStalactites = new HashSet<>();
        newDraperyStalactites.addAll(this.stalactites);
        newDraperyStalactites.addAll(stalactites);

        super.setDiameter(newDiameter);
        super.setPosX(newPosX);
        setStalactites(newDraperyStalactites.stream().toList());
    }

    public static List<DraperyDesign> shouldDraperyBeCreated(List<StalactiteDesign> stalactites) {
        List<DraperyDesign> draperies = new ArrayList<>();
        for (StalactiteDesign stalactite1 : stalactites) {
            for (StalactiteDesign stalactite2 : stalactites) {
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
                                stalactite1.getStalactiteTriangle().setFill(Color.DARKRED);
                                stalactite2.getStalactiteTriangle().setFill(Color.DARKRED);
                                draperies.add(new DraperyDesign(posX, diameter, List.of(stalactite1, stalactite2)));
                            }
                        }
                    }
                }
            }
        }
        return draperies;
    }

    public static void isDraperyTouchAnotherDrapery(List<DraperyDesign> draperiesTemp, List<DraperyDesign> draperies) {
        for (DraperyDesign drapery1 : draperiesTemp) {
            boolean isAlreadyAdd = false;
            for (DraperyDesign drapery2 : draperiesTemp) {
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
        System.out.println("88888888888 " + DraperyDesign.draperiesToString(draperies));
    }

    private static boolean draperyListContainsDraperyWithTheseStalactites(List<DraperyDesign> draperies, List<StalactiteDesign> stalactites) {
        for (DraperyDesign drapery : draperies) {
            if (drapery.getStalactites().equals(stalactites)) {
                return false;
            }
        }
        return true;
    }

    public static void generateDrapery(List<StalactiteDesign> stalactites) {
        for (StalactiteDesign stalactite1 : stalactites) {
            double posMinStal1 = stalactite1.getPosX() - stalactite1.getDiameter() / 2;
            double posMaxStal1 = stalactite1.getPosX() + stalactite1.getDiameter() / 2;

            for (StalactiteDesign stalactite2 : stalactites) {

                double posMinStal2 = stalactite2.getPosX() - stalactite2.getDiameter() / 2;
                double posMaxStal2 = stalactite2.getPosX() + stalactite2.getDiameter() / 2;

                System.out.println(stalactite1.hashCode() != stalactite2.hashCode());

                if ((posMaxStal1 >= posMinStal2 && posMaxStal1 <= posMaxStal2)
                        || (posMinStal1 >= posMinStal2 && posMinStal1 <= posMaxStal2) && stalactite1.hashCode() != stalactite2.hashCode()) {
                    stalactite1.getStalactiteTriangle().setFill(Color.DARKRED);
                    stalactite2.getStalactiteTriangle().setFill(Color.DARKRED);
                }
            }
        }
    }

    public static String draperiesToString(List<DraperyDesign> draperies) {
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
                        for (StalactiteDesign stalactite : drapery.stalactites) {
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
