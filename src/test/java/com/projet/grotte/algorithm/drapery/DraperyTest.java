package com.projet.grotte.algorithm.drapery;

import com.projet.grotte.algorithm.stalactite.Stalactite;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DraperyTest {

    @Test
    void shouldDraperyBeCreatedWithGoodProperties() {
        List<Stalactite> stalactites = new ArrayList<>();

        Stalactite stalactite1 = new Stalactite(0,  3, 10, 1);
        Stalactite stalactite2 = new Stalactite(40,  6, 20, 2);
        Stalactite stalactite3 = new Stalactite(45,  9, 30, 3);

        stalactites.add(stalactite1);
        stalactites.add(stalactite2);
        stalactites.add(stalactite3);

        List<Drapery> draperies = Drapery.shouldDraperyBeCreated(stalactites);

        assertEquals(1, draperies.size());
        Drapery drapery = draperies.get(0);
        assertEquals(2, drapery.getStalactites().size());
        assertFalse(drapery.getStalactites().contains(stalactite1));
        assertTrue(stalactite2.isOnDrapery());
        assertTrue(stalactite3.isOnDrapery());
    }
}