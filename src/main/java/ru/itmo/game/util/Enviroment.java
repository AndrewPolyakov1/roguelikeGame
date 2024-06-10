package ru.itmo.game.util;

import com.googlecode.lanterna.graphics.TextGraphics;
import lombok.Getter;
import lombok.Setter;
import ru.itmo.game.drawable.DrawableInterface;
import ru.itmo.game.objects.Level;
import ru.itmo.game.objects.Player;

import java.util.Random;

/**
 * <h2>Container to store Game environment</h2>
 */
public class Enviroment implements DrawableInterface {
    private final Random random = new Random();
    @Setter
    @Getter
    private Player player;
    @Getter
    @Setter
    private Level level;
    @Getter
    private int width;
    @Getter
    private int height;
    public Enviroment(int width, int height, Level level) {
        this.width = width;
        this.height = height;
        this.level = level;
    }

    @Override
    public void draw(TextGraphics textGraphics) {
        level.draw(textGraphics);
        player.draw(textGraphics);
    }

    public void setPlayerPosition(Point position) {
        player.setPosition(position);
    }

    public void updatePlayerHealth(int increment) {
        player.setHealth(player.getHealth() + increment);
    }

    public void updatePlayerLevel(int increment) {
        player.setLevel(player.getLevel() + increment);
    }

    public void updatePlayerDamage(int increment) {
        player.setDamage(player.getDamage() + increment);
    }

    public boolean tryMovePlayerDown() {
        Point position = player.getPosition();
        if (position.y + 1 < height && isTileEmpty(Point.of(position.x, position.y + 1))) {
            player.setPosition(Point.of(position.x, position.y + 1));
            return true;
        }
        return false;
    }

    public boolean tryMovePlayerUp() {
        Point position = player.getPosition();
        if (position.y - 1 >= 0 && isTileEmpty(Point.of(position.x, position.y - 1))) {
            player.setPosition(Point.of(position.x, position.y - 1));
            return true;
        }
        return false;
    }

    public boolean tryMovePlayerLeft() {
        Point position = player.getPosition();
        if (position.x - 1 >= 0 && isTileEmpty(Point.of(position.x - 1, position.y))) {
            player.setPosition(Point.of(position.x - 1, position.y));
            return true;
        }
        return false;
    }


    public boolean tryMovePlayerRight() {
        Point position = player.getPosition();
        if (position.x + 1 < width && isTileEmpty(Point.of(position.x + 1, position.y))) {
            player.setPosition(Point.of(position.x + 1, position.y));
            return true;
        }
        return false;
    }

    public boolean isTileEmpty(Point point) {
        return level.getCellGrid()[point.x][point.y] == Level.PASSAGE;
    }

    public boolean isLevelDone() {
        return player.getPosition().equals(level.getExitPoint());
    }


    public Point generateRandomEmptyPoint() {
        int x, y;
        do {
            x = random.nextInt(width - 1);
            y = random.nextInt(height - 1);
        } while (level.getCellGrid()[x][y]);

        return Point.of(x, y);
    }
}
