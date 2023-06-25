package com.projet.grotte;

import com.projet.grotte.algorithm.column.Column;
import com.projet.grotte.algorithm.drapery.Drapery;
import com.projet.grotte.algorithm.drop.Drop;
import com.projet.grotte.algorithm.fistulous.Fistulous;
import com.projet.grotte.algorithm.stalactite.Stalactite;
import com.projet.grotte.algorithm.stalagmite.Stalagmite;
import com.projet.grotte.design.columnDesign.ColumnDesign;
import com.projet.grotte.design.dropDesign.DropDesign;
import com.projet.grotte.design.fistulousDesign.FistulousDesign;
import com.projet.grotte.design.stalactiteDesign.StalactiteDesign;
import com.projet.grotte.design.stalagmiteDesign.StalagmiteDesign;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CaveSimulation extends Application {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    //public static final double FLOOR_Y = 550;
    public static final double FLOOR_Y = 250;
    private static final double CEILING_Y = 50;
    private static final int INTERVAL_MS = 200;

    public static final int SIZE_CAVE_X = 200;
    public static final int SIZE_CAVE_Y = 50;

    private Line floorLine;
    private Line ceilingLine;
    private List<DropDesign> drops = new ArrayList<>();
    private List<FistulousDesign> fistulouses = new ArrayList<>();
    private List<StalactiteDesign> stalactites = new ArrayList<>();
    private List<StalagmiteDesign> stalagmites = new ArrayList<>();
    private List<ColumnDesign> columns = new ArrayList<>();
    private List<Drapery> draperies = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        floorLine = new Line(0, FLOOR_Y, WINDOW_WIDTH, FLOOR_Y);
        ceilingLine = new Line(0, CEILING_Y, WINDOW_WIDTH, CEILING_Y);
        root.getChildren().addAll(floorLine, ceilingLine);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= INTERVAL_MS * 1_000_000) {
                    Random random = new Random();
                    if (random.nextBoolean()) {
                        DropDesign dropDesign = DropDesign.dropBuilder();

                        dropDesign.isDropOnConcretion(fistulouses, stalactites);
                        dropDesign.isDropOnAnotherDrop(drops, root);
                    }

                    DropDesign.tooHeavyDropsFall(drops, fistulouses, stalactites, stalagmites, root);
                    DropDesign.fallenDropIsDestroyed(drops, stalagmites, root);

                    FistulousDesign.fistulousIsBecomeStalactite(fistulouses, stalactites, root);

                    Optional<ColumnDesign> optionalColumn = ColumnDesign.shouldColumnsBeCreated(stalactites, stalagmites, root);
                    optionalColumn.ifPresent(column -> columns.add(column));

                    System.out.println(columns.size());
                    lastUpdate = now;
                }
            }
        };

        timer.start();
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

    public static void main(String[] args) {
        launch(args);
    }
}
