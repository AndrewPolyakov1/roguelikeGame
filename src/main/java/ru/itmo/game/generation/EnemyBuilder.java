package ru.itmo.game.generation;

import ru.itmo.game.objects.Enemy;
import ru.itmo.game.objects.enemies.AgressiveBehavior;
import ru.itmo.game.objects.enemies.EnemyBehavior;
import ru.itmo.game.objects.enemies.PassiveBehavior;
import ru.itmo.game.util.Enviroment;
import ru.itmo.game.util.Point;
import ru.itmo.game.util.RandomUtil;

import java.util.Random;

public class EnemyBuilder {
    int BASE_HEALTH = 2;
    int BASE_DAMAGE = 1;
    private static final Random random = new Random();

    public EnemyBuilder() {
    }

    public Enemy buildRandomEnemy(Enviroment enviroment, int levelLimit) {
        Point position = enviroment.generateRandomEmptyPoint();
        //??????? ?????? ?? ???????
        EnemyBehavior behavior = new PassiveBehavior();
        Enemy.EnemyType type = RandomUtil.randomEnum(Enemy.EnemyType.class);
        int level = random.nextInt(1, levelLimit);
        Enemy enemy = new Enemy(
                type,
                behavior,
                getHealthFromParameters(level, type),
                getDamageFromParameters(level, type),
                level,
                position,
                enviroment
        );

        return enemy;
    }

    private int getHealthFromParameters(int level, Enemy.EnemyType type) {
        return type.getBaseValue() * level * BASE_HEALTH;
    }

    private int getDamageFromParameters(int level, Enemy.EnemyType type) {
        return type.getBaseValue() * level * BASE_DAMAGE;
    }


}
