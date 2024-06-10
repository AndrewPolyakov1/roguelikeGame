package ru.itmo.game.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import lombok.Getter;
import ru.itmo.game.drawable.DrawableInterface;
import ru.itmo.game.drawable.Symbols;
import ru.itmo.game.util.Enviroment;
import ru.itmo.game.util.Point;

import java.io.Serializable;
import java.util.Random;

public class Enemy extends BasePerson implements DrawableInterface, Serializable, MovableInterface {

    public EnemyType enemyType;

    public EnemyBehavior behavior;
    private boolean isAlive;

    public Enemy(@JsonProperty("EnemyType") EnemyType enemyType,
                 @JsonProperty("EnemyBehavior") EnemyBehavior behavior,
                 @JsonProperty("health") int health,
                 @JsonProperty("damage") int damage,
                 @JsonProperty("level") int level,
                 @JsonProperty("position") Point position
    ) {
        super(health, damage, level, position);
        this.enemyType = enemyType;
        this.behavior = behavior;
        isAlive = true;
    }

    @Override
    public String toString() {
        return String.format("Enemy: {health: %d," +
                        " damage: %d," +
                        "level: %d," +
                        "position: %s," +
                        "behavior: %s," +
                        "type: %s}",
                this.health,
                this.damage,
                this.level,
                this.position,
                this.behavior,
                this.enemyType);
    }

    @Override
    public void draw(TextGraphics textGraphics) {
        if (!isAlive) {
            return;
        }
        textGraphics.setForegroundColor(TextColor.ANSI.RED);
        textGraphics.putString(position.x, position.y, Symbols.ENEMY);
        textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
    }

    @Override
    public void move(Enviroment enviroment) {
        if (!isAlive) {
            return;
        }
        Random random = new Random();
        int moveX;
        int moveY;

        do {
            moveX = random.nextInt(-1, 1);
            moveY = random.nextInt(-1, 1);
        } while (!enviroment.isTileEmpty(Point.of(position.x + moveX, position.y + moveY)));

        position = Point.of(position.x + moveX, position.y + moveY);
    }

    public enum EnemyType {
        VILLAGER(1),
        HEAVY(3),
        ;

        @Getter
        private int baseValue;

        EnemyType(int baseValue) {
            this.baseValue = baseValue;
        }

    }

    public enum EnemyBehavior {
        AGGRESSIVE,
        SCARED,
        PASSIVE;
    }
}
