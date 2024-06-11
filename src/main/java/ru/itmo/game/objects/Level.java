package ru.itmo.game.objects;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import lombok.Getter;
import lombok.Setter;
import ru.itmo.game.drawable.Colours;
import ru.itmo.game.drawable.DrawableInterface;
import ru.itmo.game.drawable.Symbols;
import ru.itmo.game.util.Point;

import java.io.Serializable;
import java.util.Random;

@Getter
@Setter
public class Level implements Serializable, DrawableInterface {

    public static final boolean WALL = true;
    public static final boolean PASSAGE = false;
    private final Random random = new Random();

    private final Point exitPoint;

    private final int width;
    private final int height;
    private final boolean[][] cellGrid;

    //    private final List<Enemy> enemies;
    public Level(int width, int height, boolean[][] grid) {
        this.width = width;
        this.height = height;
        this.cellGrid = grid;
        exitPoint = getRandomEmptyPoint();
    }

    public Level() {
        this.width = 0;
        this.height = 0;

        cellGrid = new boolean[10][10];
        exitPoint = getRandomEmptyPoint();
    }

    private Point getRandomEmptyPoint() {
        int x, y;
        do {
            x = random.nextInt(width - 1);
            y = random.nextInt(height - 1);
        } while (cellGrid[x][y]);
        return Point.of(x, y);

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Level:\n");
        sb.append(width).append(" ").append(height).append("\n");

        for (boolean[] row : cellGrid) {
            for (boolean cell : row) {
                sb.append(cell ? Symbols.WALL : Symbols.PASSAGE);
            }
            sb.append("\n");
        }
        sb.append("Level end\n");
        return sb.toString();
    }

    @Override
    public void draw(TextGraphics textGraphics) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                {
                    String tile = cellGrid[i][j] ? Symbols.WALL : Symbols.PASSAGE;
                    textGraphics.putString(i, j, tile);
                }
            }
        }
        textGraphics.setForegroundColor(Colours.EXIT);
        textGraphics.putString(exitPoint.x, exitPoint.y, Symbols.EXIT);
        textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
    }
}
