package com.projet.grotte.algorithm;

import com.projet.grotte.algorithm.column.Column;
import com.projet.grotte.algorithm.drapery.Drapery;
import com.projet.grotte.algorithm.drop.Drop;
import com.projet.grotte.algorithm.fistulous.Fistulous;
import com.projet.grotte.algorithm.stalactite.Stalactite;
import com.projet.grotte.algorithm.stalagmite.Stalagmite;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@NoArgsConstructor
@Setter
@Getter
public class CaveSimulation {

    public static final int SIZE_CAVE = 800;
    public static final int CEILING_Y = 500;
    private List<Drop> drops = new ArrayList<>();
    private List<Fistulous> fistulouses = new ArrayList<>();
    private List<Stalactite> stalactites = new ArrayList<>();
    private List<Stalagmite> stalagmites = new ArrayList<>();
    private List<Column> columns = new ArrayList<>();
    private List<Drapery> draperies = new ArrayList<>();

    private int counter = 1;

    public static void main(String[] args) {
        /*
            Simulation simple de la formation de concrétion dans une grotte.
            la simulation se basse sur l'apparition de goutte d'eau ainsi que le chute.
            Pour simplifier le processus quand une goutte d'eau est trop lourde elle tombe est génère directement une fistuleuse.
            Si une goutte se retroube a centre de la fistuleuse alors la fistuleuse continue de grandire (en longeur mais pas en épaisseur pour la simulation non graphique)
            Quand la fistuleuse est bouché a partir d'une certaine taille définie elle devient une stalactite.
            Le meme procéssus sans les fistuleuse est appliqué au stalagmite.
            Si une stalagmite et une stalactite se touche alors on crée une colonne.
        */
        CaveSimulation simulation = new CaveSimulation();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(simulation.task, 0, 200);
    }

    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            //POUR DEBUG, A SUPPRIMER
            if (counter == 1) {
                stalactites.addAll(List.of(new Stalactite(3,  2, 6, 1), new Stalactite(5, 2, 6, 2)));
            }
            Random random = new Random();
            if (random.nextBoolean()) {
                //Créer une goutte
                Drop drop = Drop.dropBuilder();
                //Verifie si une concretion est sur la position de la goutte
                drop.isDropOnConcretion(fistulouses, stalactites);
                //Verifie si une goutte existe deja, les deux fusionnent
                drop.isDropOnAnotherDrop(drops);
            }
            //Verifie si une goutte tombe a cause de son poids
            Drop.tooHeavyDropsFall(drops, fistulouses, stalactites, stalagmites);
            //Verifie si la goutte se detruit lorsqu'elle tombe
            Drop.fallenDropIsDestroyed(drops, stalagmites);
            //Verifie si une fistuleuse devient une stalagmite
            Fistulous.fistulousIsBecomeStalactite(fistulouses, stalactites);
            //check for create Column
            Optional<Column> optionalColumn = Column.shouldColumnsBeCreated(stalactites, stalagmites);
            optionalColumn.ifPresent(column -> columns.add(column));
            //check for create Drapery
            List<Drapery> newDraperies = Drapery.shouldDraperyBeCreated(stalactites);
            draperies.addAll(newDraperies);
            showConcretions();
        }
    };

    public void showConcretions() {
        String results =
                Drop.dropsToString(drops) +
                        Fistulous.fistulousesToString(fistulouses) +
                        Stalactite.stalactitesToString(stalactites) +
                        Drapery.draperiesToString(draperies);
        //Stalagmite.stalagmitesToString(stalagmites);
        System.out.println("\nTOUR " + counter + results);
        this.setCounter(this.getCounter() + 1);
    }

    public static boolean checkValuesAreInRange(double[] array1, double[] array2) {
        for (double value : array1) {
            double minValue = Math.min(array2[0], array2[1]);
            double maxValue = Math.max(array2[0], array2[1]);
            if (minValue <= value && value <= maxValue) {
                return true;
            }
        }
        return false;
    }
}
