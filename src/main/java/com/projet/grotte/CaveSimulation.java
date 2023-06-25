package com.projet.grotte;

import com.projet.grotte.design.columnDesign.ColumnDesign;
import com.projet.grotte.design.draperyDesign.DraperyDesign;
import com.projet.grotte.design.dropDesign.DropDesign;
import com.projet.grotte.design.fistulousDesign.FistulousDesign;
import com.projet.grotte.design.stalactiteDesign.StalactiteDesign;
import com.projet.grotte.design.stalagmiteDesign.StalagmiteDesign;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CaveSimulation extends Application {

    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 400;
    //public static final double FLOOR_Y = 550;
    public static final double FLOOR_Y = 350;
    private static final double CEILING_Y = 50;
    public static final int SIZE_CAVE_X = 400;
    public static final int SIZE_CAVE_Y = 50;

    private static int INTERVAL_MS = 50;
    private Line floorLine;
    private Line ceilingLine;
    private List<DropDesign> drops = new ArrayList<>();
    private List<FistulousDesign> fistulouses = new ArrayList<>();
    private List<StalactiteDesign> stalactites = new ArrayList<>();
    private List<StalagmiteDesign> stalagmites = new ArrayList<>();
    private List<ColumnDesign> columns = new ArrayList<>();
    private List<DraperyDesign> draperies = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        floorLine = new Line(0, FLOOR_Y, WINDOW_WIDTH, FLOOR_Y);
        ceilingLine = new Line(0, CEILING_Y, WINDOW_WIDTH, CEILING_Y);

        Button increaseButton = new Button("Simulation x2");
        Button decreaseButton = new Button("Simulation /2");
        Button relaunchButton = new Button("Relancer la simulation");

        increaseButton.setOnAction(event -> {
            if (INTERVAL_MS > 100) {
                INTERVAL_MS -= 50;
            } else if (INTERVAL_MS > 20 && INTERVAL_MS < 100) {
                INTERVAL_MS -= 10;
            } else if (INTERVAL_MS > 2 && INTERVAL_MS < 21) {
                INTERVAL_MS -= 1;
            } else {
                INTERVAL_MS -= 100;
            }
        });

        decreaseButton.setOnAction(event -> {
            INTERVAL_MS += 50;
        });

        relaunchButton.setOnAction(event -> {
            root.getChildren().clear();
            root.getChildren().addAll(floorLine, ceilingLine,increaseButton, decreaseButton, relaunchButton);
             drops.clear();
             fistulouses.clear();
             stalactites.clear();
             stalagmites.clear();
             columns.clear();
             draperies.clear();
        });

        root.getChildren().addAll(floorLine, ceilingLine,increaseButton, decreaseButton, relaunchButton);

        increaseButton.setLayoutX(10);
        increaseButton.setLayoutY(10);
        decreaseButton.setLayoutX(300);
        decreaseButton.setLayoutY(10);
        relaunchButton.setLayoutX(130);
        relaunchButton.setLayoutY(10);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("Simulation de concrétion dans une grotte");
        primaryStage.setResizable(false);
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
                    DropDesign.dropOnForbiddenConcretion(drops, columns, root);

                    DropDesign.tooHeavyDropsFall(drops, fistulouses, stalactites, stalagmites, root);
                    DropDesign.fallenDropIsDestroyed(drops, stalagmites, root);

                    FistulousDesign.fistulousIsBecomeStalactite(fistulouses, stalactites, root);

                    Optional<ColumnDesign> optionalColumn = ColumnDesign.shouldColumnsBeCreated(stalactites, stalagmites, root);
                    optionalColumn.ifPresent(column -> columns.add(column));


                    List<DraperyDesign> draperiesTemp = DraperyDesign.shouldDraperyBeCreated(stalactites);
                    if (!draperiesTemp.isEmpty()) {
                        DraperyDesign.isDraperyTouchAnotherDrapery(draperiesTemp, draperies);
                    }

                    //DraperyDesign.generateDrapery(stalactites);

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
}
