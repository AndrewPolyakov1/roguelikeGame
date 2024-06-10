package ru.itmo.game.objects.enemies;

import ch.qos.logback.core.joran.util.AggregationAssessor;
import ru.itmo.game.util.Enviroment;
import ru.itmo.game.util.Point;

import java.util.*;

import static ru.itmo.game.util.HadlerEnemies.pathFinding;

public class AgressiveBehavior implements EnemyBehavior {
    @Override
    public List<Point> generatePathAttack(Enviroment env, Point possitionEnemy) {
        Point possPlayer = env.getPlayer().getPosition();
        boolean[][] ceilGrid = env.getLevel().getCellGrid();

        return pathFinding(ceilGrid, possitionEnemy, possPlayer);
    }
}
