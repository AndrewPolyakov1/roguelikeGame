package ru.itmo.game.objects;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.game.drawable.Colours;
import ru.itmo.game.drawable.DrawableInterface;
import ru.itmo.game.drawable.Symbols;
import ru.itmo.game.util.HadlerEnemies;
import ru.itmo.game.util.Point;

import java.util.List;

public class Player extends BasePerson implements DrawableInterface {
    private static final Logger log = LoggerFactory.getLogger(Player.class);
    @Setter
    @Getter
    private int cooldown = 1000;
    @Setter
    @Getter
    private int experience = 0;

    public Player(int health, int damage, int level, Point position) {
        super(health, damage, level, position);
    }

    @Override
    public void draw(TextGraphics textGraphics) {
        textGraphics.setForegroundColor(Colours.PLAYER);
        if (System.currentTimeMillis() - getLastAttack() < 100) {
            textGraphics.setBackgroundColor(TextColor.ANSI.BLUE);
        }
        textGraphics.putString(position.x, position.y, Symbols.PLAYER);
        textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
        textGraphics.setBackgroundColor(Colours.BACKGROUND_DEFAULT);

    }

    public void attack(List<Enemy> enemyList) {
        int counter = 0;
        for (Enemy enemy : enemyList) {
            if (System.currentTimeMillis() - getLastAttack() > cooldown &&
                    HadlerEnemies.calculateDistance(this.position, enemy.getPosition()) < this.getAttackRadius()) {
                counter++;
                enemy.setHealth(enemy.getHealth() - this.getDamage());
                if (enemy.getHealth() <= 0) {
                    enemy.die();
                    updateExperience(enemy);
                }
            }
        }
        log.info("Attacked {} enemies", counter);
        setLastAttack(System.currentTimeMillis());
    }

    public void updateExperience(int increment) {
        experience += increment;
    }

    public void updateExperience(Enemy enemy) {
        log.info("Updated experience");
        experience += enemy.getLevel() * 100;
        if (experience / level > 100) {
            log.info("Level up");
            experience -= level * 100;
            level += 1;
        }
    }

}
