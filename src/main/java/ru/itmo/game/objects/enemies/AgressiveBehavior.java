package ru.itmo.game.objects.enemies;

import ru.itmo.game.util.Enviroment;
import ru.itmo.game.util.Point;

import java.util.*;

import static ru.itmo.game.util.HadlerEnemies.pathAttackFinding;

public class AgressiveBehavior implements EnemyBehavior {
    @Override
    public List<Point> generatePathAttack(Enviroment env, Point possitionEnemy) {
        Point possPlayer = env.getPlayer().getPosition();
        boolean[][] ceilGrid = env.getLevel().getCellGrid();

        return pathAttackFinding(ceilGrid, possitionEnemy, possPlayer);
    }
}
