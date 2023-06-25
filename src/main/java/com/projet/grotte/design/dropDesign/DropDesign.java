package com.projet.grotte.design.dropDesign;

import com.projet.grotte.algorithm.CaveSimulation;
import com.projet.grotte.algorithm.Concretion;
import com.projet.grotte.design.columnDesign.ColumnDesign;
import com.projet.grotte.design.fistulousDesign.FistulousDesign;
import com.projet.grotte.design.stalactiteDesign.StalactiteDesign;
import com.projet.grotte.design.stalagmiteDesign.StalagmiteDesign;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.projet.grotte.CaveSimulation.*;

@Getter
@Setter
public class DropDesign extends Concretion {

    private static final int DIAMETER = 6;
    private static final double WEIGTH = 4;

    private double posY;
    private double weight;
    private double limestone;
    private boolean isFalling = false;
    private boolean toDestroy = false;

    private Circle dropCircle;

    public static DropDesign dropBuilder() {
        double posX = Math.round(new Random().nextDouble() * SIZE_CAVE_X * 100.0 / 100.0);
        return new DropDesign(posX, SIZE_CAVE_Y);
    }

    public DropDesign(double posX, double posY) {
        super(posX, DIAMETER);
        this.posY = posY;
        this.weight = WEIGTH;
        Random random = new Random();
        int randomLIMESTONE = random.nextInt(9) + 2;
        this.limestone = randomLIMESTONE;
        this.dropCircle = new Circle(DIAMETER/2, Color.AQUA);
        dropCircle.setCenterX(posX);
        dropCircle.setCenterY(posY+DIAMETER/2);
    }

    public Circle getDropCircle() {
        return dropCircle;
    }

    public void setDropCircle(Circle dropCircle) {
        this.dropCircle = dropCircle;
    }

    public void evolve(double newWeight, double newLimestone, double newDiameter) {
        setWeight(this.getWeight() + newWeight);
        setLimestone(this.getLimestone() + newLimestone);
        this.dropCircle.setRadius(newDiameter/ 2 +0.5);
        super.setDiameter(newDiameter+0.5);
    }

    public void isDropOnConcretion(List<FistulousDesign> fistulouses, List<StalactiteDesign> stalactites) {
        double posXMinCurrentDrop = this.getPosX() - this.getDiameter() / 2;
        double posXMaxCurrentDrop = this.getPosX() + this.getDiameter() / 2;
        //Verifie si la goutte est sur une fistuleuse
        for (FistulousDesign fistulous : fistulouses) {
            checkDropOnConcretions(posXMinCurrentDrop, posXMaxCurrentDrop, fistulous.getPosX(), fistulous.getDiameter(), fistulous.getSize());
        }
        //Verifie si la goutte est sur une stalactite
        for (StalactiteDesign stalactite : stalactites) {
            checkDropOnConcretions(posXMinCurrentDrop, posXMaxCurrentDrop, stalactite.getPosX(), stalactite.getDiameter(), stalactite.getSize());
        }
    }

    private void checkDropOnConcretions(double posXMinCurrentDrop, double posXMaxCurrentDrop, double posX, double diameter, double size) {
        double posXMin = posX - diameter / 2;
        double posXMax = posX + diameter / 2;

        if ((posXMinCurrentDrop >= posXMin && posXMinCurrentDrop <= posXMax) || (posXMaxCurrentDrop >= posXMin && posXMaxCurrentDrop <= posXMax)) {
            //System.out.println("Goutte sur fistuleuse");
            this.dropCircle.setCenterY(SIZE_CAVE_Y+ size);
            this.setPosY(SIZE_CAVE_Y + size);
            this.dropCircle.setCenterX(posX);
        }
    }

    public void isDropOnAnotherDrop(List<DropDesign> drops, Pane root) {


        boolean noDrops = drops.isEmpty();

        if (noDrops) {
            drops.add(this);
            root.getChildren().add(this.getDropCircle());
        } else {
            boolean isNotOnAnotherDrop = true;
            for (DropDesign secondDrop : drops) {
                double[] posXCurrentDrop = getSurfaceCoveredByDrop(this);
                double[] posXSecondDrop = getSurfaceCoveredByDrop(secondDrop);
                boolean secondAndCurrentDropAreStuck = CaveSimulation.checkValuesAreInRange(posXCurrentDrop, posXSecondDrop);
                if (secondAndCurrentDropAreStuck && !secondDrop.isFalling()) {
                    secondDrop.evolve(secondDrop.getWeight(), secondDrop.getLimestone(), secondDrop.getDiameter());
                    isNotOnAnotherDrop = false;
                }
            }
            if (isNotOnAnotherDrop) {
                root.getChildren().add(this.getDropCircle());
                drops.add(this);
            }
        }
    }

    public static void dropOnForbiddenConcretion(List<DropDesign> drops, List<ColumnDesign> columns, Pane root) {
        Iterator<DropDesign> iteratorDrop = drops.iterator();

        while (iteratorDrop.hasNext()) {
            DropDesign drop = iteratorDrop.next();

            double dropMinPosition = drop.getPosX() - drop.getDiameter()/2;
            double dropMaxPosition = drop.getPosX() + drop.getDiameter()/2;

            for (ColumnDesign column : columns) {
                double columnMinPosition = column.getPosX() - column.getDiameter()/2;
                double columnMaxPosition = column.getPosX() + column.getDiameter()/2;

                if (dropMinPosition >= columnMinPosition && dropMaxPosition <= columnMaxPosition) {
                    root.getChildren().remove(drop.getDropCircle());
                    iteratorDrop.remove();
                    break; // Sortir de la boucle for dès qu'un match est trouvé (optimisation facultative)
                }
            }
        }
    }

    private static double[] getSurfaceCoveredByDrop(DropDesign drop) {
        double positionMin = drop.getPosX() - drop.getDiameter() / 2;
        double positionMax = drop.getPosX() + drop.getDiameter() / 2;
        return new double[]{positionMin, positionMax};
    }

    public void falling() {
        double gravity = 0.98;
        double posY = getPosY() + (weight * gravity);
        setPosY(posY);
        this.dropCircle.setCenterY(posY);
        this.isFalling = true;
    }

    public static void fallenDropIsDestroyed(List<DropDesign> drops, List<StalagmiteDesign> stalagmites, Pane root) {
        Iterator<DropDesign> iterator = drops.iterator();
        while (iterator.hasNext()) {
            DropDesign drop = iterator.next();
            if (drop.isToDestroy()) {
                root.getChildren().remove(drop.getDropCircle());
                iterator.remove();
            } else {
                if (drop.getPosY() > FLOOR_Y) {
                    StalagmiteDesign stalagmite = new StalagmiteDesign(drop.getPosX(), drop.getDiameter(), 20);
                    stalagmites.add(stalagmite);
                    root.getChildren().add(stalagmite.getStalagmiteTriangle());
                    root.getChildren().remove(drop.getDropCircle());
                    iterator.remove();
                }
            }
        }
    }

    //TODO
    /*public void fuse(Drop otherDrop) {
        double newSize = this.size + otherDrop.getSize();
        double newLimeStone = this.limestone + otherDrop.getLimestone();

        this.size = newSize;
        this.limestone = newLimeStone;
    }*/

    public static void tooHeavyDropsFall(List<DropDesign> drops, List<FistulousDesign> fistulouses, List<StalactiteDesign> stalactites, List<StalagmiteDesign> stalagmites, Pane root) {
        for (DropDesign drop : drops) {
            if (drop.getWeight() >= 10 && !drop.isFalling()) {
                if (drop.getPosY() != SIZE_CAVE_Y) {
                    double posXMin = drop.getPosX() - drop.getDiameter() / 2;
                    double posXMax = drop.getPosX() + drop.getDiameter() / 2;
                    for (FistulousDesign fistulous : fistulouses) {
                        if (fistulous.getPosX() > posXMin && fistulous.getPosX() < posXMax) {
                            if (Math.round(drop.getPosX()) == Math.round(fistulous.getPosX()) && fistulous.isHollow()) {
                                fistulous.setHollow(false);
                            }
                            fistulous.setSize(fistulous.getSize()+ drop.limestone / 5);
                            fistulous.setDiameter(fistulous.getDiameter() +  drop.limestone / 20);
                            fistulous.getFistulousTriangle().getPoints().set(0,fistulous.getPosX() - fistulous.getDiameter());
                            fistulous.getFistulousTriangle().getPoints().set(1, (double) SIZE_CAVE_Y);
                            fistulous.getFistulousTriangle().getPoints().set(2,fistulous.getPosX() + fistulous.getDiameter());
                            fistulous.getFistulousTriangle().getPoints().set(3, (double) SIZE_CAVE_Y);
                            fistulous.getFistulousTriangle().getPoints().set(4,fistulous.getPosX());
                            fistulous.getFistulousTriangle().getPoints().set(5, SIZE_CAVE_Y+fistulous.getSize());

                        }
                    }
                    for (StalactiteDesign stalactite : stalactites) {
                        if (stalactite.getPosX() > posXMin && stalactite.getPosX() < posXMax) {
                            stalactite.setSize(stalactite.getSize()+ drop.limestone / 5);
                            stalactite.setDiameter(stalactite.getDiameter() +  drop.limestone / 20);
                            stalactite.getStalactiteTriangle().getPoints().set(0,stalactite.getPosX()-stalactite.getDiameter());
                            stalactite.getStalactiteTriangle().getPoints().set(1, (double) SIZE_CAVE_Y);
                            stalactite.getStalactiteTriangle().getPoints().set(2,stalactite.getPosX()+stalactite.getDiameter());
                            stalactite.getStalactiteTriangle().getPoints().set(3, (double) SIZE_CAVE_Y);
                            stalactite.getStalactiteTriangle().getPoints().set(4,stalactite.getPosX());
                            stalactite.getStalactiteTriangle().getPoints().set(5, SIZE_CAVE_Y+stalactite.getSize());
                        }
                    }
                } else {
                    FistulousDesign fistulous = new FistulousDesign(drop.getPosX(), drop.getDiameter());
                    root.getChildren().add(fistulous.getFistulousTriangle());
                    fistulouses.add(fistulous);
                }
                drop.falling();
                //System.out.println("Un goutte tombe");
            }
            if (drop.isFalling()) {
                drop.falling();
                double posXMin = drop.getPosX() - drop.getDiameter() / 2;
                double posXMax = drop.getPosX() + drop.getDiameter() / 2;
                for (StalagmiteDesign stalagmite : stalagmites) {

                    double stalagmitePosXMin = stalagmite.getPosX() - stalagmite.getDiameter() / 2;
                    double stalagmitePosXMax = stalagmite.getPosX() + stalagmite.getDiameter() / 2;

                    if ((stalagmitePosXMin >= posXMin && stalagmitePosXMin <= posXMax) || (stalagmitePosXMax >= posXMin && stalagmitePosXMax <= posXMax)) {

                        if (drop.getPosY() >= FLOOR_Y-stalagmite.getSize()) {
                            stalagmite.setSize(stalagmite.getSize()+ drop.limestone / 5);
                            stalagmite.setDiameter(stalagmite.getDiameter() +  drop.limestone / 20);

                            stalagmite.getStalagmiteTriangle().getPoints().set(0,stalagmite.getPosX()-stalagmite.getDiameter());
                            stalagmite.getStalagmiteTriangle().getPoints().set(1, (double) FLOOR_Y);
                            stalagmite.getStalagmiteTriangle().getPoints().set(2,stalagmite.getPosX()+stalagmite.getDiameter());
                            stalagmite.getStalagmiteTriangle().getPoints().set(3, (double) FLOOR_Y);
                            stalagmite.getStalagmiteTriangle().getPoints().set(4,stalagmite.getPosX());
                            stalagmite.getStalagmiteTriangle().getPoints().set(5, FLOOR_Y-stalagmite.getSize());

                            drop.setToDestroy(true);
                        }
                    }
                }
            }
        }
    }

    public static String dropsToString(List<DropDesign> drops) {
        StringBuilder dropsStringified = new StringBuilder();
        final int[] index = {1};
        dropsStringified.append("\n\n---------- GOUTTES ---------- ");
        drops.forEach(drop -> {
            dropsStringified.append("\nGoutte N°").append(index[0])
                    .append("\n\tPosition : (")
                    .append(drop.getPosX()).append(",").append(drop.getPosY())
                    .append(")\n\tPoids : ")
                    .append(drop.getWeight())
                    .append("\n\tDiamètre : ")
                    .append(drop.getDiameter())
                    .append("\n\tCalcaire : ")
                    .append(drop.getLimestone());
            index[0]++;
        });
        return dropsStringified.toString();
    }
}
