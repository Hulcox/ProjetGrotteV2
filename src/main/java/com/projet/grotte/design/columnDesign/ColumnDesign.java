package com.projet.grotte.design.columnDesign;

import com.projet.grotte.algorithm.CaveSimulation;
import com.projet.grotte.algorithm.Concretion;
import com.projet.grotte.algorithm.column.Column;
import com.projet.grotte.algorithm.stalactite.Stalactite;
import com.projet.grotte.algorithm.stalagmite.Stalagmite;
import com.projet.grotte.design.stalactiteDesign.StalactiteDesign;
import com.projet.grotte.design.stalagmiteDesign.StalagmiteDesign;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.projet.grotte.CaveSimulation.FLOOR_Y;
import static com.projet.grotte.CaveSimulation.SIZE_CAVE_Y;
import static com.projet.grotte.algorithm.CaveSimulation.CEILING_Y;

public class ColumnDesign extends Concretion {

    private Polygon StalactiteInColumnTriangle;

    private Polygon StalagmiteInColumnTriangle;

    public ColumnDesign(double posX, double diameter) {
        super(posX, diameter);

        double x1Stalagmite = posX-diameter; // Point inférieur gauche (coordonnée x)
        double y1Stalagmite = FLOOR_Y; // Point inférieur gauche (coordonnée y)
        double x2Stalagmite = posX+diameter; // Point inférieur droit (coordonnée x)
        double y2Stalagmite = FLOOR_Y; // Point inférieur droit (coordonnée y)

        double x1Stalactite = posX-diameter; // Point inférieur gauche (coordonnée x)
        double y1Stalactite = SIZE_CAVE_Y; // Point inférieur gauche (coordonnée y)
        double x2Stalactite = posX+diameter; // Point inférieur droit (coordonnée x)
        double y2Stalactite = SIZE_CAVE_Y; // Point inférieur droit (coordonnée y)

        double x3 = posX; // Point supérieur (coordonnée x)
        double y3 = FLOOR_Y/2; // Point supérieur (coordonnée y)

        this.StalagmiteInColumnTriangle = new Polygon(x1Stalagmite, y1Stalagmite, x2Stalagmite, y2Stalagmite, x3, y3);
        this.StalagmiteInColumnTriangle.setFill(Color.BROWN);

        this.StalactiteInColumnTriangle = new Polygon(x1Stalactite, y1Stalactite, x2Stalactite, y2Stalactite, x3, y3);
        this.StalactiteInColumnTriangle.setFill(Color.BROWN);
    }

    public static Optional<ColumnDesign> shouldColumnsBeCreated(List<StalactiteDesign> stalactites, List<StalagmiteDesign> stalagmites, Pane root) {
        Iterator<StalactiteDesign> stalactiteIterator = stalactites.iterator();
        while (stalactiteIterator.hasNext()) {
            StalactiteDesign stalactite = stalactiteIterator.next();
            Iterator<StalagmiteDesign> stalagmiteIterator = stalagmites.iterator();
            while (stalagmiteIterator.hasNext()) {
                StalagmiteDesign stalagmite = stalagmiteIterator.next();
                System.out.println("Taille column : " + stalactite.getSize() + stalagmite.getSize() );
                if (isCollisionHappenedBetweenStalactitesAndStalagmites(stalactite, stalagmite)) {
                    root.getChildren().remove(stalagmite.getStalagmiteTriangle());
                    root.getChildren().remove(stalactite.getStalactiteTriangle());
                    stalactiteIterator.remove();
                    stalagmiteIterator.remove();
                    double posXColumn = (stalactite.getPosX() + stalagmite.getPosX()) / 2;
                    double diameterColumn = stalactite.getDiameter() + stalagmite.getDiameter();
                    ColumnDesign column = new ColumnDesign(posXColumn, diameterColumn);
                    root.getChildren().add(column.StalactiteInColumnTriangle);
                    root.getChildren().add(column.StalagmiteInColumnTriangle);
                    return Optional.of(column);
                }
            }
        }
        return Optional.empty();
    }

    public static boolean isCollisionHappenedBetweenStalactitesAndStalagmites(StalactiteDesign stalactite, StalagmiteDesign stalagmite) {
        double[] stalactitePos = StalactiteDesign.getSurfaceCoveredByStalactite(stalactite);
        double[] stalagmitePos = StalagmiteDesign.getSurfaceCoveredByStalagmite(stalagmite);
        boolean stalagmiteAndStalactiteTouch = com.projet.grotte.CaveSimulation.checkValuesAreInRange(stalactitePos, stalagmitePos);
        if (stalagmiteAndStalactiteTouch) {
            return stalactite.getSize() + stalagmite.getSize() >= FLOOR_Y;
        }
        return false;
    }

    @Override
    public void evolve(double newWeight, double newLimestone, double newDiameter) {
        // TODO
    }
}
