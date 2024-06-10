package ru.itmo.game.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.lanterna.graphics.TextGraphics;
import lombok.Getter;
import lombok.Setter;
import ru.itmo.game.drawable.Colours;
import ru.itmo.game.drawable.DrawableInterface;
import ru.itmo.game.drawable.Symbols;
import ru.itmo.game.objects.enemies.EnemyBehavior;
import ru.itmo.game.util.Enviroment;
import ru.itmo.game.util.HandlerEnemies;
import ru.itmo.game.util.Point;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.logging.Logger;

import static ru.itmo.game.util.HandlerEnemies.findPathToClosestIdlePoint;
import static ru.itmo.game.util.HandlerEnemies.heuristic;
import static ru.itmo.game.util.HandlerEnemies.randomWalk;

public class Enemy extends BasePerson implements DrawableInterface, Serializable, AttackingInterface, MovableInterface {
    public static final Logger log = Logger.getLogger(MethodHandles
            .lookup()
            .lookupClass()
            .getName());
    private static final int steps = 50;
    //    private long lastAttack = System.currentTimeMillis();
    private static final long cooldown = 1000;
    public EnemyType enemyType;
    public EnemyBehavior behavior;
    @Setter
    @Getter
    private boolean isAlive;
    private List<Point> pathIdle;
    private int posIdle = 0;
    private int posAttack = 0;
    private int posReturn = 0;
    boolean useReturnPath = false;

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
        textGraphics.setForegroundColor(Colours.ENEMY);
        if (System.currentTimeMillis() - getLastAttack() < 100) {
            textGraphics.setBackgroundColor(Colours.ENEMY_AOE);
            List<Point> aoe = Point.getOffsetsInRadius(position, getAttackRadius());
            for (Point point : aoe) {
                textGraphics.putString(point.x, point.y, " ");
            }
        }
        textGraphics.putString(position.x, position.y, Symbols.ENEMY);
        textGraphics.setForegroundColor(Colours.DEFAULT);
    }

    @Override
    public void move(Enviroment enviroment) {
        Point possitionPlayer = enviroment.getPlayer().getPosition();
        boolean[][] ceilGrid = enviroment.getLevel().getCellGrid();
        if (!isAlive) {
            return;
        }
        if (pathIdle == null || pathIdle.isEmpty()) {
            return;
        }
        if (heuristic(possitionPlayer, position) < 10) {
            List<Point> pathAttack = behavior.generatePathAttack(enviroment, position);
            Point nextPoint = pathAttack.get(posAttack);
            if (enviroment.isTileEmpty(nextPoint)) {
                position = nextPoint;
            }
            posAttack = (posAttack + 1) % pathAttack.size();
        } else {
            if (!pathIdle.contains(position)) {
                useReturnPath = true;
                List<Point> pathReturn = findPathToClosestIdlePoint(position, pathIdle, ceilGrid);
                Point nextPoint = pathReturn.get(posReturn);
                if (enviroment.isTileEmpty(nextPoint)) {
                    position = nextPoint;
                }
                posReturn = (posReturn + 1) % pathReturn.size();
            } else {
                if (useReturnPath) {
                    posIdle = pathIdle.indexOf(position);
                }
                Point nextPoint = pathIdle.get(posIdle);
                if (enviroment.isTileEmpty(nextPoint)) {
                    position = nextPoint;
                }
                posIdle = (posIdle + 1) % pathIdle.size();
                useReturnPath = false;
            }
        }
    }

    @Override
    public boolean canAttack(Enviroment enviroment) {
        return isAlive && (System.currentTimeMillis() - getLastAttack()) > cooldown &&
                HandlerEnemies.calculateDistance(this.position, enviroment.getPlayer().getPosition()) < this.getAttackRadius();
    }

    public void die() {
        isAlive = false;
    }

    @Override
    public void attack(Enviroment enviroment) {
        if (!canAttack(enviroment)) {
            return;
        }
        log.info(String.format("Enemy %s attacked player %s", this, enviroment.getPlayer().toString()));
        enviroment.updatePlayerHealth(-this.damage);
        this.setLastAttack(System.currentTimeMillis());
    }

    @Getter
    public enum EnemyType {
        VILLAGER(1),
        HEAVY(3),
        ;
        private final int baseValue;

        EnemyType(int baseValue) {
            this.baseValue = baseValue;
        }

    }
}
