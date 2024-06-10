package ru.itmo.game.objects.enemies;

import ru.itmo.game.util.Enviroment;
import ru.itmo.game.util.Point;
import java.util.List;

public interface EnemyBehavior {
    List<Point> generatePathAttack(Enviroment env, Point possitionEnemy);
}
