package ru.itmo.game.objects.enemies;

import ru.itmo.game.util.Enviroment;
import ru.itmo.game.util.Point;

import java.util.List;

import static ru.itmo.game.util.HandlerEnemies.pathEscapeFinding;

public class ScaredBehavior implements EnemyBehavior{
    @Override
    public List<Point> generatePathAttack(Enviroment env, Point positionEnemy) {
        Point possPlayer = env.getPlayer().getPosition();
        boolean[][] ceilGrid = env.getLevel().getCellGrid();
        return pathEscapeFinding(ceilGrid, positionEnemy, possPlayer);
    }
}
