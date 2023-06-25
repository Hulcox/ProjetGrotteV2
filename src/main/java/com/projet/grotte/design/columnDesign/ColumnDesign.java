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

    public ColumnDesign(double posX, double diameter, StalactiteDesign stalactiteDesign, StalagmiteDesign stalagmiteDesign) {
        super(posX, diameter);

        this.StalagmiteInColumnTriangle = stalagmiteDesign.getStalagmiteTriangle();
        this.StalagmiteInColumnTriangle.getPoints().set(4,posX);
        this.StalagmiteInColumnTriangle.getPoints().set(5,FLOOR_Y/2-50);
        this.StalagmiteInColumnTriangle.setFill(Color.BROWN);

        this.StalactiteInColumnTriangle = stalactiteDesign.getStalactiteTriangle();
        this.StalactiteInColumnTriangle.getPoints().set(4,posX);
        this.StalactiteInColumnTriangle.getPoints().set(5,FLOOR_Y/2+50);
        this.StalactiteInColumnTriangle.setFill(Color.BROWN);
    }

    public static Optional<ColumnDesign> shouldColumnsBeCreated(List<StalactiteDesign> stalactites, List<StalagmiteDesign> stalagmites, Pane root) {
        Iterator<StalactiteDesign> stalactiteIterator = stalactites.iterator();
        while (stalactiteIterator.hasNext()) {
            StalactiteDesign stalactite = stalactiteIterator.next();
            Iterator<StalagmiteDesign> stalagmiteIterator = stalagmites.iterator();
            while (stalagmiteIterator.hasNext()) {
                StalagmiteDesign stalagmite = stalagmiteIterator.next();
                if (isCollisionHappenedBetweenStalactitesAndStalagmites(stalactite, stalagmite)) {
                    root.getChildren().remove(stalagmite.getStalagmiteTriangle());
                    root.getChildren().remove(stalactite.getStalactiteTriangle());
                    stalactiteIterator.remove();
                    stalagmiteIterator.remove();
                    double posXColumn = (stalactite.getPosX() + stalagmite.getPosX()) / 2;
                    double diameterColumn = stalactite.getDiameter() + stalagmite.getDiameter();
                    ColumnDesign column = new ColumnDesign(posXColumn, diameterColumn, stalactite, stalagmite);
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
}
