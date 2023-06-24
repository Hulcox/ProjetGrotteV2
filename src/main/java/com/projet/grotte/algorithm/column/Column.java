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
        double[] stalactitePos = Stalactite.getSurfaceCoveredByStalactite(stalactite);
        double[] stalagmitePos = Stalagmite.getSurfaceCoveredByStalagmite(stalagmite);
        boolean stalagmiteAndStalactiteTouch = CaveSimulation.checkValuesAreInRange(stalactitePos, stalagmitePos);
        if (stalagmiteAndStalactiteTouch) {
            return stalactite.getSize() + stalagmite.getSize() >= CEILING_Y;
        }
        return false;
    }

    @Override
    public void evolve(double newWeight, double newLimestone, double newDiameter) {
        // TODO
    }
}