package com.projet.grotte.design.fistulousDesign;

import com.projet.grotte.algorithm.Concretion;
import com.projet.grotte.algorithm.stalactite.Stalactite;
import com.projet.grotte.design.stalactiteDesign.StalactiteDesign;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;
import java.util.List;

import static com.projet.grotte.CaveSimulation.SIZE_CAVE_Y;

@Getter
@Setter
public class FistulousDesign extends Concretion {
    private double size;

    private boolean isHollow = true;

    private Polygon fistulousTriangle;

    public FistulousDesign(double posX, double diameter) {
        super(posX, diameter);
        this.size = 20;

        double x1 = posX-diameter; // Point inférieur gauche (coordonnée x)
        double y1 = SIZE_CAVE_Y; // Point inférieur gauche (coordonnée y)
        double x2 = posX+diameter; // Point inférieur droit (coordonnée x)
        double y2 = SIZE_CAVE_Y; // Point inférieur droit (coordonnée y)
        double x3 = posX; // Point supérieur (coordonnée x)
        double y3 = SIZE_CAVE_Y+this.size; // Point supérieur (coordonnée y)

        this.fistulousTriangle = new Polygon(x1, y1, x2, y2, x3, y3);
        this.fistulousTriangle.setFill(Color.DARKGRAY);
    }

    public Polygon getFistulousTriangle() {
        return fistulousTriangle;
    }

    public void setFistulousTriangle(Polygon fistulousTriangle) {
        this.fistulousTriangle = fistulousTriangle;
    }

    public boolean isHollow() {
        return isHollow;
    }

    public void setHollow(boolean hollow) {
        isHollow = hollow;
    }

    public static void fistulousIsBecomeStalactite(List<FistulousDesign> fistulouses, List<StalactiteDesign> stalactites, Pane root) {
        Iterator<FistulousDesign> iterator = fistulouses.iterator();
        int counter = 0;
        while (iterator.hasNext()) {
            FistulousDesign fistulous = iterator.next();
            counter++;
            if (fistulous.getSize() > 30) {
                if (!fistulous.isHollow()) {
                    StalactiteDesign stalactite = new StalactiteDesign(fistulous.getPosX(), fistulous.getDiameter(), fistulous.getSize(), stalactites.size() + 1);
                    stalactites.add(stalactite);
                    root.getChildren().add(stalactite.getStalactiteTriangle());
                    root.getChildren().remove(fistulous.getFistulousTriangle());
                    iterator.remove();
                } else {
                    root.getChildren().remove(fistulous.getFistulousTriangle());
                    iterator.remove();
                }
            }
        }
    }

    public static String fistulousesToString(List<FistulousDesign> fistulouses) {
        StringBuilder fistulousesStringified = new StringBuilder();
        final int[] index = {1};
        fistulousesStringified.append("\n\n---------- FISTULEUSES ----------");
        fistulouses.forEach(fistulous -> {
                    fistulousesStringified.append("\nFistuleuse N°").append(index[0])
                            .append("\n\tPosition : ")
                            .append(fistulous.getPosX())
                            .append("\n\tDiamètre : ")
                            .append(fistulous.getDiameter())
                            .append("\n\tTaille : ")
                            .append(fistulous.getSize());
                    if(fistulous.isHollow()){
                        fistulousesStringified.append("\n\tCreuse");
                    } else {
                        fistulousesStringified.append("\n\tPleine");
                    }
                    index[0]++;
                }
        );
        return fistulousesStringified.toString();
    }
}
