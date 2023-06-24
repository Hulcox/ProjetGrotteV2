package com.projet.grotte.algorithm.fistulous;

import com.projet.grotte.algorithm.stalactite.Stalactite;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FistulousTest {

    @Test
    void evolve() {
    }

    @Test
    void fistulousShouldBecomeStalactite() {
        List<Fistulous> fistulouses = new ArrayList<>();

        Fistulous fistulous1 = new Fistulous(1, 3);
        Fistulous fistulous2 = new Fistulous(1, 3);
        Fistulous fistulous3 = new Fistulous(1, 3);

        fistulous2.setHollow(false);
        fistulous2.setSize(11);

        fistulouses.add(fistulous1);
        fistulouses.add(fistulous2);
        fistulouses.add(fistulous3);

        List<Stalactite> stalactites = new ArrayList<>();

        Fistulous.fistulousIsBecomeStalactite(fistulouses, stalactites);

        assertEquals(2, fistulouses.size());
        assertEquals(1, stalactites.size());
    }

    @Test
    void fistulousShouldBeDestroyed() {
        List<Fistulous> fistulouses = new ArrayList<>();

        Fistulous fistulous1 = new Fistulous(1, 3);
        Fistulous fistulous2 = new Fistulous(30, 3);

        fistulous1.setHollow(true);
        fistulous1.setSize(11);
        fistulous2.setHollow(true);
        fistulous2.setSize(11);

        fistulouses.add(fistulous1);
        fistulouses.add(fistulous2);

        List<Stalactite> stalactites = new ArrayList<>();

        Fistulous.fistulousIsBecomeStalactite(fistulouses, stalactites);

        assertEquals(0, fistulouses.size());
        assertEquals(0, stalactites.size());
    }

    @Test
    void fistulousShouldBecomeStalactiteWithGoodProperties() {
        List<Fistulous> fistulouses = new ArrayList<>();

        Fistulous fistulous = new Fistulous(1, 3);

        fistulous.setHollow(false);
        fistulous.setSize(11);

        fistulouses.add(fistulous);

        List<Stalactite> stalactites = new ArrayList<>();

        Fistulous.fistulousIsBecomeStalactite(fistulouses, stalactites);

        assertEquals(1, stalactites.get(0).getPosX());
        assertEquals(3, stalactites.get(0).getDiameter());
        assertEquals(11, stalactites.get(0).getSize());
    }

}