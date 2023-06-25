package com.projet.grotte.algorithm.column;

import com.projet.grotte.algorithm.stalactite.Stalactite;
import com.projet.grotte.algorithm.stalagmite.Stalagmite;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ColumnTest {
    @Test
    void shouldColumnBeCreatedAndRemoveConcretions() {
        List<Stalactite> stalactites = new ArrayList<>();
        stalactites.add(new Stalactite(1,  3, 5, 1));
        stalactites.add(new Stalactite(10, 6, 40, 2));

        List<Stalagmite> stalagmites = new ArrayList<>();
        stalagmites.add(new Stalagmite(7,  9, 1));
        stalagmites.add(new Stalagmite(10,  12, 64));

        Optional<Column> result = Column.shouldColumnsBeCreated(stalactites, stalagmites);

        assertTrue(result.isPresent());
        assertEquals(1, stalactites.size());
        assertEquals(1, stalagmites.size());
    }

    @Test
    void shouldColumnBeCreatedWithGoodProperties() {
        List<Stalactite> stalactites = new ArrayList<>();
        stalactites.add(new Stalactite(1, 3, 5, 1));
        stalactites.add(new Stalactite(10, 6, 40, 2));

        List<Stalagmite> stalagmites = new ArrayList<>();
        stalagmites.add(new Stalagmite(7, 9, 1));
        stalagmites.add(new Stalagmite(10, 12, 64));

        Optional<Column> result = Column.shouldColumnsBeCreated(stalactites, stalagmites);

        assertTrue(result.isPresent());
        Column column = result.get();
        assertEquals(10, column.getPosX());
        assertEquals(18, column.getDiameter());
    }

    //TODO
    //revoir le fonctionnement de la colonne
}