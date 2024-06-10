package ru.itmo.game.util;

import com.googlecode.lanterna.graphics.TextGraphics;
import lombok.Setter;
import ru.itmo.game.drawable.DrawableInterface;
import ru.itmo.game.generation.EnemyBuilder;
import ru.itmo.game.generation.LevelBuilder;
import ru.itmo.game.objects.Enemy;
import ru.itmo.game.objects.HUD;
import ru.itmo.game.objects.Level;
import ru.itmo.game.objects.Player;

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
    public Enviroment environment;
    private final Random random = new Random();
    private int currentLevel;
    @Setter
    private List<Enemy> enemyList;

    private HUD hud;

    public WorldState(int WIDTH, int HEIGHT) {
        this.currentLevel = 1;
        this.environment = initializeEnvironment(WIDTH, HEIGHT - HUD.hudHeight);
        this.hud = new HUD(WIDTH, HEIGHT, this.environment);
        initializeEnemyList(random.nextInt(1, 4 * currentLevel));
    }

    public void restart(){
        this.currentLevel = 1;
        this.environment = initializeEnvironment(environment.getWidth(), environment.getHeight());
        this.hud = new HUD(environment.getWidth(), environment.getHeight() + HUD.hudHeight, this.environment);
        initializeEnemyList(random.nextInt(1, 4 * currentLevel));
    }

    public Enviroment initializeEnvironment(int WIDTH, int HEIGHT) {
        LevelBuilder levelBuilder = new LevelBuilder(
                WIDTH,
                HEIGHT
        );
        Level level = levelBuilder.build(5, 0.4);

        Enviroment enviroment = new Enviroment(WIDTH, HEIGHT, level);
        Player player = new Player(
                10,
                10,
                1,
                enviroment.generateRandomEmptyPoint()
        );
        enviroment.setPlayer(player);

        return enviroment;
    }

    @Override
    public void draw(TextGraphics textGraphics) {
        environment.draw(textGraphics);
        enemyList.forEach(enemy -> enemy.draw(textGraphics));
        hud.draw(textGraphics);
    }

    public boolean isLevelDone() {
        return environment.isLevelDone();
    }

    public void newLevel() {
        LevelBuilder levelBuilder = new LevelBuilder(
                environment.getWidth(),
                environment.getHeight()
        );
        environment.setLevel(levelBuilder.build(5, 0.4));
        environment.setPlayerPosition(environment.generateRandomEmptyPoint());
        initializeEnemyList(random.nextInt(1, 4 * currentLevel));
        // Player stats increase
        currentLevel++;
        environment.updatePlayerHealth(2);
        environment.updatePlayerLevel(1);
        environment.updatePlayerDamage(1);

        //
    }

    public void initializeEnemyList(int enemyCount) {
        log.info("Initializing enemy list with " + enemyCount + " enemies");
        List<Enemy> enemyList = new ArrayList<>();
        EnemyBuilder enemyBuilder = new EnemyBuilder();

        for (int i = 0; i < enemyCount; i++) {
            enemyList.add(
                    enemyBuilder.buildRandomEnemy(
                            environment,
                            currentLevel + 1
                    )
            );
        }
        this.enemyList = enemyList;
    }

    public void update() {
//        log.info("Updating world state");
        moveAllEnemies();
    }

    public void moveAllEnemies() {
        enemyList.forEach(enemy -> enemy.move(environment));
    }
}
