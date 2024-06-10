package ru.itmo.game.util;

import com.googlecode.lanterna.graphics.TextGraphics;
import lombok.Setter;
import ru.itmo.game.drawable.DrawableInterface;
import ru.itmo.game.generation.EnemyBuilder;
import ru.itmo.game.generation.LevelBuilder;
import ru.itmo.game.objects.Enemy;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class WorldState implements DrawableInterface {

    public static final Logger log = Logger.getLogger(MethodHandles
            .lookup()
            .lookupClass()
            .getName());
    public final Enviroment enviroment;
    private final Random random = new Random();
    private int currentLevel;
    @Setter
    private List<Enemy> enemyList;

    public WorldState(Enviroment enviroment) {
        this.currentLevel = 1;
        this.enviroment = enviroment;
        initializeEnemyList(random.nextInt(1, 4 * currentLevel));
    }

    @Override
    public void draw(TextGraphics textGraphics) {
        enviroment.draw(textGraphics);
        enemyList.forEach(enemy -> enemy.draw(textGraphics));
    }

    public boolean isLevelDone() {
        return enviroment.isLevelDone();
    }

    public void newLevel() {
        LevelBuilder levelBuilder = new LevelBuilder(
                enviroment.getWidth(),
                enviroment.getHeight()
        );
        enviroment.setLevel(levelBuilder.build(5, 0.4));
        enviroment.setPlayerPosition(enviroment.generateRandomEmptyPoint());
        initializeEnemyList(random.nextInt(1, 4 * currentLevel));
        // Player stats increase
        currentLevel++;
        //
    }

    public void initializeEnemyList(int enemyCount) {
        log.info("Initializing enemy list with " + enemyCount + " enemies");
        List<Enemy> enemyList = new ArrayList<>();
        EnemyBuilder enemyBuilder = new EnemyBuilder();

        for (int i = 0; i < enemyCount; i++) {
            enemyList.add(
                    enemyBuilder.buildRandomEnemy(
                            enviroment,
                            currentLevel + 1
                    )
            );
        }
        this.enemyList = enemyList;
    }

    public void update() {
        log.info("Updating world state");
        moveAllEnemies();
    }

    public void moveAllEnemies() {
        enemyList.forEach(enemy -> enemy.move(enviroment));
    }
}
