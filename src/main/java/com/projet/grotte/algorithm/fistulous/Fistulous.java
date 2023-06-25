package com.projet.grotte.algorithm.fistulous;

import com.projet.grotte.algorithm.Concretion;
import com.projet.grotte.algorithm.stalactite.Stalactite;
import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;
import java.util.List;

@Getter
@Setter
public class Fistulous extends Concretion {
    private double size;

    private boolean isHollow = true;

    public Fistulous(double posX, double diameter) {
        super(posX, diameter);
        this.size = 1;
    }

    public boolean isHollow() {
        return isHollow;
    }

    public void setHollow(boolean hollow) {
        isHollow = hollow;
    }

    public static void fistulousIsBecomeStalactite(List<Fistulous> fistulouses, List<Stalactite> stalactites) {
        Iterator<Fistulous> iterator = fistulouses.iterator();
        int counter = 0;
        while (iterator.hasNext()) {
            Fistulous fistulous = iterator.next();
            counter++;
            if (fistulous.getSize() > 10) {
                if (!fistulous.isHollow()) {
                    Stalactite stalactite = new Stalactite(fistulous.getPosX(), fistulous.getDiameter(), fistulous.getSize(), stalactites.size() + 1);
                    stalactites.add(stalactite);
                    iterator.remove();
                } else {
                    //System.out.println("\nFistuleuse " + counter + " détruite\n");
                    iterator.remove();
                }
            }
        }
    }

    public static String fistulousesToString(List<Fistulous> fistulouses) {
        if (!fistulouses.isEmpty()) {
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
                        if (fistulous.isHollow()) {
                            fistulousesStringified.append("\n\tCreuse");
                        } else {
                            fistulousesStringified.append("\n\tPleine");
                        }
                        index[0]++;
                    }
            );
            return fistulousesStringified.toString();
        }
        return "";
    }
}
