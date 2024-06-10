package ru.itmo.game.objects.enemies;

import ru.itmo.game.util.Enviroment;
import ru.itmo.game.util.Point;

import java.util.List;

public class ScaredBehavior implements EnemyBehavior{
    @Override
    public List<Point> generatePathAttack(Enviroment env, Point possitionEnemy) {
        return List.of();
    }
}
