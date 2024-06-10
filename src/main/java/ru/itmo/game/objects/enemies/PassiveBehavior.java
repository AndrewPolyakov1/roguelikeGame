package ru.itmo.game.objects.enemies;

import ru.itmo.game.util.Enviroment;
import ru.itmo.game.util.Point;

import java.util.ArrayList;
import java.util.List;

public class PassiveBehavior implements EnemyBehavior {

    @Override
    public List<Point> generatePathAttack(Enviroment env, Point positionEnemy) {
        List<Point> path = new ArrayList<>();
        path.add(positionEnemy);
        return path;
    }
}
