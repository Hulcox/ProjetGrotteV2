package com.projet.grotte.algorithm.drop;

import com.projet.grotte.algorithm.fistulous.Fistulous;
import com.projet.grotte.algorithm.stalactite.Stalactite;
import com.projet.grotte.algorithm.stalagmite.Stalagmite;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.projet.grotte.algorithm.CaveSimulation.CEILING_Y;
import static org.junit.jupiter.api.Assertions.*;

class DropTest {

    @Test
    void dropsShouldEvolve() {
        Drop drop = new Drop(0, 0);
        double initialWeight = drop.getWeight();
        double initialLimestone = drop.getLimestone();
        double initialDiameter = drop.getDiameter();

        double newWeight = 2.0;
        double newLimestone = 5.0;
        double newDiameter = 1.0;

        drop.evolve(newWeight, newLimestone, newDiameter);

        assertEquals(initialWeight + newWeight, drop.getWeight());
        assertEquals(initialLimestone + newLimestone, drop.getLimestone());
        assertEquals(initialDiameter + newDiameter, drop.getDiameter());
    }

    @Test
    void dropShouldBeOnOneConcretion() {
        Drop drop = new Drop(15.0, CEILING_Y);
        double initialPosY = drop.getPosY();
        double sizeTouchedStalactite = 8.0;

        Fistulous fistulous1 = new Fistulous(3.0, 2.0);
        Fistulous fistulous2 = new Fistulous(7.0, 2.0);
        Stalactite stalactite1 = new Stalactite(5.0, 4.0, 7.0, 1);
        //ici la stalactite que la goutte touche
        Stalactite stalactite2 = new Stalactite(14.0, 4.0, sizeTouchedStalactite, 2);

        List<Fistulous> fistulouses = new ArrayList<>();
        fistulouses.add(fistulous1);
        fistulouses.add(fistulous2);

        List<Stalactite> stalactites = new ArrayList<>();
        stalactites.add(stalactite1);
        stalactites.add(stalactite2);

        drop.isDropOnConcretion(fistulouses, stalactites);

        assertEquals(initialPosY - sizeTouchedStalactite, drop.getPosY());
    }

    @Test
    void dropShouldntBeOnAnotherDrop() {
        Drop drop = new Drop(10.0, CEILING_Y);
        double initialPosX = drop.getPosX();

        Drop dropTest1 = new Drop(3.0, CEILING_Y);
        Drop dropTest2 = new Drop(20.0, CEILING_Y);

        List<Drop> drops = new ArrayList<>();
        drops.add(dropTest1);
        drops.add(dropTest2);

        drop.isDropOnAnotherDrop(drops);

        assertTrue(drops.contains(drop));
        assertEquals(initialPosX, drop.getPosX());
    }

    @Test
    void dropShouldBeOnAnotherDrop() {
        Drop drop = new Drop(5.0, CEILING_Y);
        double initialDiameter = drop.getDiameter();

        Drop dropTest1 = new Drop(2.0, CEILING_Y);
        Drop dropTest2 = new Drop(6.0, CEILING_Y);

        List<Drop> drops = new ArrayList<>();
        drops.add(dropTest1);
        drops.add(dropTest2);

        drop.isDropOnAnotherDrop(drops);

        //System.out.println(initialDiameter + "  " + dropTest2.getDiameter()+ "  " + drop.getDiameter());
        assertEquals(initialDiameter, dropTest1.getDiameter());
        assertEquals(initialDiameter + 2, dropTest2.getDiameter());
    }

    @Test
    void falling() {
        Drop drop = new Drop(5.0, CEILING_Y);
        double initialPosY = drop.getPosY();
        drop.falling();
        assertNotEquals(initialPosY, drop.getPosX());
        assertTrue(drop.isFalling());
    }

    @Test
    void shouldRemoveDropOfListWhenIsDestroyed() {
        List<Drop> drops = new ArrayList<>();
        List<Stalagmite> stalagmites = new ArrayList<>();

        Drop drop1 = new Drop(5.0, 10.0);
        Drop drop2 = new Drop(7.0, 5.0);
        Drop drop3 = new Drop(3.0, 0.0);

        drops.add(drop1);
        drops.add(drop2);
        drops.add(drop3);

        Drop.fallenDropIsDestroyed(drops, stalagmites);

        assertFalse(drops.contains(drop3));
    }

    @Test
    void shouldCreateStalagmiteWhenDropReachGround() {
        List<Drop> drops = new ArrayList<>();
        List<Stalagmite> stalagmites = new ArrayList<>();

        Drop drop1 = new Drop(5.0, 10.0);
        Drop drop2 = new Drop(7.0, 5.0);
        Drop drop3 = new Drop(3.0, 0.0);

        drops.add(drop1);
        drops.add(drop2);
        drops.add(drop3);

        Drop.fallenDropIsDestroyed(drops, stalagmites);

        assertEquals(2, drops.size());
        assertEquals(1, stalagmites.size());
    }

    @Test
    void shouldCreateStalagmiteAtTheSameDropPosition() {
        List<Drop> drops = new ArrayList<>();
        List<Stalagmite> stalagmites = new ArrayList<>();

        Drop drop1 = new Drop(5.0, 10.0);
        Drop drop2 = new Drop(7.0, 5.0);
        Drop drop3 = new Drop(3.0, 0.0);

        drops.add(drop1);
        drops.add(drop2);
        drops.add(drop3);

        Drop.fallenDropIsDestroyed(drops, stalagmites);

        Stalagmite stalagmite = stalagmites.get(0);
        assertEquals(drop3.getPosX(), stalagmite.getPosX());
    }

    @Test
    void shouldCreateFistulousIfDropFall() {
        //Créer une fistuleuse quand le poids de la goutte est supérieur à 10 est qu'elle touche le plafond
        List<Drop> drops = new ArrayList<>();
        List<Fistulous> fistulouses = new ArrayList<>();
        List<Stalactite> stalactites = new ArrayList<>();
        List<Stalagmite> stalagmites = new ArrayList<>();

        Drop drop1 = new Drop(5.0, CEILING_Y);
        Drop drop2 = new Drop(7.0, 60.0);
        Drop drop3 = new Drop(3.0, 0.0);

        drop1.setWeight(10);

        drops.add(drop1);
        drops.add(drop2);
        drops.add(drop3);

        Drop.tooHeavyDropsFall(drops, fistulouses, stalactites, stalagmites);

        assertEquals(1, fistulouses.size());
        Fistulous fistulous = fistulouses.get(0);
        assertEquals(drop1.getPosX(), fistulous.getPosX());
        assertEquals(drop1.getDiameter(), fistulous.getDiameter());
        assertTrue(fistulous.isHollow());
    }

    @Test
    void shouldIncrementStalagmiteSizeIfDropFallOnIt() {
        //TODO
    }

    @Test
    void shouldIncrementStalactiteSizeIfDropFallOnIt() {
        //TODO
    }

    @Test
    void tooHeavyDropsShouldFall() {
        List<Drop> drops = new ArrayList<>();
        List<Fistulous> fistulouses = new ArrayList<>();
        List<Stalactite> stalactites = new ArrayList<>();
        List<Stalagmite> stalagmites = new ArrayList<>();

        Drop drop1 = new Drop(5.0, CEILING_Y);
        Drop drop2 = new Drop(7.0, 25.0);
        Drop drop3 = new Drop(3.0, 0.0);

        drop2.setWeight(10);
        drop3.setWeight(10);

        drops.add(drop1);
        drops.add(drop2);
        drops.add(drop3);

        Drop.tooHeavyDropsFall(drops, fistulouses, stalactites, stalagmites);

        assertFalse(drop1.isFalling());
        assertTrue(drop2.isFalling());
        assertTrue(drop3.isFalling());
    }
}