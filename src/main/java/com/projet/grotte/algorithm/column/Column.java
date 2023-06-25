package com.projet.grotte.algorithm.column;

import com.projet.grotte.algorithm.CaveSimulation;
import com.projet.grotte.algorithm.Concretion;
import com.projet.grotte.algorithm.stalactite.Stalactite;
import com.projet.grotte.algorithm.stalagmite.Stalagmite;
import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.projet.grotte.algorithm.CaveSimulation.CEILING_Y;

@Getter
@Setter
public class Column extends Concretion {

    public Column(double posX, double diameter) {
        super(posX, diameter);
    }

    public static Optional<Column> shouldColumnsBeCreated(List<Stalactite> stalactites, List<Stalagmite> stalagmites) {
        Iterator<Stalactite> stalactiteIterator = stalactites.iterator();
        while (stalactiteIterator.hasNext()) {
            Stalactite stalactite = stalactiteIterator.next();
            Iterator<Stalagmite> stalagmiteIterator = stalagmites.iterator();
            while (stalagmiteIterator.hasNext()) {
                Stalagmite stalagmite = stalagmiteIterator.next();
                if (isCollisionHappenedBetweenStalactitesAndStalagmites(stalactite, stalagmite)) {
                    stalactiteIterator.remove();
                    stalagmiteIterator.remove();
                    double posXColumn = (stalactite.getPosX() + stalagmite.getPosX()) / 2;
                    double diameterColumn = stalactite.getDiameter() + stalagmite.getDiameter();
                    return Optional.of(new Column(posXColumn, diameterColumn));
                }
            }
        }
        return Optional.empty();
    }

    public static boolean isCollisionHappenedBetweenStalactitesAndStalagmites(Stalactite stalactite, Stalagmite stalagmite) {
        double[] stalactiteSurface = CaveSimulation.getSurfaceCovered(stalactite.getPosX(), stalactite.getDiameter());
        double[] stalagmiteSurface = CaveSimulation.getSurfaceCovered(stalagmite.getPosX(), stalagmite.getDiameter());
        boolean stalagmiteAndStalactiteTouch = CaveSimulation.checkValuesAreInRange(stalactiteSurface, stalagmiteSurface);
        if (stalagmiteAndStalactiteTouch) {
            return stalactite.getSize() + stalagmite.getSize() >= CEILING_Y;
        }
        return false;
    }

    public static String columnsToString(List<Column> columns) {
        if (!columns.isEmpty()) {
            StringBuilder columnsStringified = new StringBuilder();
            final int[] index = {1};
            columnsStringified.append("\n\n---------- COLONNES ----------");
            columns.forEach(column -> {
                        columnsStringified.append("\nFistuleuse N°").append(index[0])
                                .append("\n\tPosition : ")
                                .append(column.getPosX())
                                .append("\n\tDiamètre : ")
                                .append(column.getDiameter());
                        index[0]++;
                    }
            );
            return columnsStringified.toString();
        }
        return "";
    }
}