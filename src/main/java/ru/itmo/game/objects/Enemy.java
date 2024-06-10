package ru.itmo.game.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import lombok.Getter;
import ru.itmo.game.drawable.DrawableInterface;
import ru.itmo.game.drawable.Symbols;
import ru.itmo.game.objects.enemies.EnemyBehavior;
import ru.itmo.game.util.Enviroment;
import ru.itmo.game.util.Point;

import java.io.Serializable;
import java.util.List;

import static ru.itmo.game.util.HadlerEnemies.*;

public class Enemy extends BasePerson implements DrawableInterface, Serializable, MovableInterface {

    public EnemyType enemyType;

    public EnemyBehavior behavior;
    private boolean isAlive = true;
    private List<Point> pathIdle;
    private List<Point> pathAttack;
    private List<Point> pathReturn;
    private final int steps = 50;
    private int posIdle = 0;
    private int posAttack = 0;
    private int posReturn = 0;

    public Enemy(@JsonProperty("EnemyType") EnemyType enemyType,
                 @JsonProperty("EnemyBehavior") EnemyBehavior behavior,
                 @JsonProperty("health") int health,
                 @JsonProperty("damage") int damage,
                 @JsonProperty("level") int level,
                 @JsonProperty("position") Point position,
                 Enviroment enviroment
    ) {
        super(health, damage, level, position);
        this.enemyType = enemyType;
        this.behavior = behavior;
        isAlive = true;
        generatePathIdle(position, enviroment);
    }

    private void generatePathIdle(Point src, Enviroment env) {
        boolean[][] cellGrid = env.getLevel().getCellGrid();
        pathIdle = randomWalk(cellGrid, src, steps);
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
        Point possitionPlayer = enviroment.getPlayer().getPosition();
        boolean[][] ceilGrid = enviroment.getLevel().getCellGrid();
        boolean useReturnPath = false;
        if (!isAlive) {
            return;
        }
        if (pathIdle == null || pathIdle.isEmpty()) {
            return;
        }
        if (heuristic(possitionPlayer, position) < 10){
             pathAttack = behavior.generatePathAttack(enviroment, position);
             Point nextPoint = pathAttack.get(posAttack);
             if (enviroment.isTileEmpty(nextPoint)) {
                position = nextPoint;
             }
             posAttack = (posAttack + 1) % pathAttack.size();
        } else {
            if (!pathIdle.contains(position)){
                useReturnPath = true;
                pathReturn = findPathToClosestIdlePoint(position, pathIdle, ceilGrid);
                Point nextPoint = pathReturn.get(posReturn);
                if (enviroment.isTileEmpty(nextPoint)) {
                    position = nextPoint;
                }
                posReturn = (posReturn + 1) % pathReturn.size();
            } else {
                if (useReturnPath){
                    posIdle = pathIdle.indexOf(position);
                }
                Point nextPoint = pathIdle.get(posIdle);
                if (enviroment.isTileEmpty(nextPoint)) {
                    position = nextPoint;
                }
                posIdle = (posIdle + 1) % pathIdle.size();
//                System.out.println(posIdle);
            }
        }
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
}
