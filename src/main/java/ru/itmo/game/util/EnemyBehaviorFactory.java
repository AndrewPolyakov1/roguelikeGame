package ru.itmo.game.util;
import ru.itmo.game.objects.enemies.AgressiveBehavior;
import ru.itmo.game.objects.enemies.EnemyBehavior;
import ru.itmo.game.objects.enemies.PassiveBehavior;
import ru.itmo.game.objects.enemies.ScaredBehavior;

import java.util.Random;

public class EnemyBehaviorFactory {
    public static EnemyBehavior getRandomBehavior() {
        Random random = new Random();
        int behaviorType = random.nextInt(3);

        return switch (behaviorType) {
            case 0 -> new ScaredBehavior();
            case 1 -> new PassiveBehavior();
            case 2 -> new AgressiveBehavior();
            default -> throw new IllegalStateException("Unexpected value: " + behaviorType);
        };
    }
}
