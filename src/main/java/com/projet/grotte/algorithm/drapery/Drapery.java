package com.projet.grotte.algorithm.drapery;

import com.projet.grotte.algorithm.stalactite.Stalactite;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Drapery {

    private List<Stalactite> stalactites;

    public static List<Drapery> shouldDraperyBeCreated(List<Stalactite> stalactites) {
        List<Drapery> draperies = new ArrayList<>();
        for (Stalactite stalactite : stalactites) {
            for (Stalactite value : stalactites) {
                if (stalactite.hashCode() != value.hashCode()) {
                    if (!value.isOnDrapery()) {
                        boolean isTwoStalactitesAreTouching = Stalactite.isTwoStalactitesAreTouching(stalactite, value);
                        if (isTwoStalactitesAreTouching) {
                            stalactite.setOnDrapery(true);
                            value.setOnDrapery(true);
                            draperies.add(new Drapery(List.of(stalactite, value)));
                        }
                    }
                }
            }
        }
        return draperies;
    }

    public static String draperiesToString(List<Drapery> draperies) {
        StringBuilder draperiesStringified = new StringBuilder();
        final int[] index = {1};
        draperiesStringified.append("\n\n---------- DRAPERIES ----------");
        draperies.forEach(drapery -> {
                    draperiesStringified.append("\nDraperie N°").append(index[0])
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
}
