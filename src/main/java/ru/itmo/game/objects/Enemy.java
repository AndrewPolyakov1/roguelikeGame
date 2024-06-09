package ru.itmo.game.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.lanterna.graphics.TextGraphics;
import lombok.Getter;
import ru.itmo.game.drawable.DrawableInterface;
import ru.itmo.game.util.Environment;
import ru.itmo.game.util.Point;

import java.io.Serializable;

public class Enemy extends BasePerson implements DrawableInterface, Serializable {

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
