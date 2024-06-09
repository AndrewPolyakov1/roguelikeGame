package ru.itmo.game.generation;

import ru.itmo.game.objects.Level;
import ru.itmo.game.serialization.Serializer;
import ru.itmo.game.util.CellularAutomata;

import java.io.IOException;

public class LevelBuilder {
    private final int width;
    private final int height;

    public LevelBuilder(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public static boolean[][] generateCellGrid(int width, int height, int numberOfIterations, double chanceOfWallParameter) {
        CellularAutomata cellularAutomaton = new CellularAutomata(height, width);
        cellularAutomaton.fillCellGridWithNoise(chanceOfWallParameter);

        for (int i = 0; i < numberOfIterations; i++) {
            cellularAutomaton.applyRules();
        }

        cellularAutomaton.cullSmallerCaverns();
        return cellularAutomaton.getCellGrid();
    }

    public Level build(int algorithmIterations, double parameter) {
        return new Level(
                width,
                height,
                generateCellGrid(
                        width,
                        height,
                        4,
                        0.4)
        );
    }

    public Level build(Serializer serializer, String path) throws IOException {
        return serializer.deserialize(path, Level.class);
    }
}
