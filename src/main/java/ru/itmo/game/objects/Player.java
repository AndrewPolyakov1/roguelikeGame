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
import ru.itmo.game.util.HandlerEnemies;
import ru.itmo.game.util.Inventory;
import ru.itmo.game.util.Item;
import ru.itmo.game.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Setter
@Getter
public class Player extends BasePerson implements DrawableInterface {
    private static final Logger log = LoggerFactory.getLogger(Player.class);
    private int cooldown = 1000;
    private int experience = 0;
    private Inventory inventory;
    private final Random random = new Random();

    public Player(int health, int damage, int level, Point position) {
        super(health, damage, level, position);
        List<Item> items = new ArrayList<>(List.of(Item.createRandomItem(1), Item.createRandomItem(1), Item.createRandomItem(1)));
        inventory = new Inventory(items);
    }

    @Override
    public void draw(TextGraphics textGraphics) {
        textGraphics.setForegroundColor(Colours.PLAYER);
        if (System.currentTimeMillis() - getLastAttack() < 100) {
            textGraphics.setBackgroundColor(Colours.PLAYER_AOE);
            List<Point> aoe = Point.getOffsetsInRadius(position, getAttackRadius());
            for (Point point : aoe) {
                textGraphics.putString(point.x, point.y, " ");
            }
        }
        textGraphics.putString(position.x, position.y, Symbols.PLAYER);
        textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
        textGraphics.setBackgroundColor(Colours.BACKGROUND_DEFAULT);

    }

    public void attack(List<Enemy> enemyList) {
        int counter = 0;
        if (System.currentTimeMillis() - getLastAttack() < cooldown) {
            return;
        }
        for (Enemy enemy : enemyList) {
            if (HandlerEnemies.calculateDistance(this.position, enemy.getPosition()) < this.getAttackRadius()) {
                counter++;
                enemy.setHealth(enemy.getHealth() - this.getDamage());
                if (enemy.getHealth() <= 0) {
                    enemy.die();
                    updateExperience(enemy);
                    if (random.nextFloat(0, 1) <= 0.3){
                        addItem(Item.createRandomItem(enemy.getLevel()));
                    }
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

    public void addItem(Item item) {
        inventory.addItem(item);
    }

    public void tryUseItem(int index) {
        Item item = inventory.useItem(index);
        if (item == null){
            log.info("Not available");
            return;
        }
        switch (item.type()) {
            case HEALTH -> setHealth(getHealth() + item.level());
            case DAMAGE -> setDamage(getDamage() + item.level());
            case COOLDOWN -> setCooldown(getCooldown() - 10 * item.level());
        }
        log.info("Item {} used", index);


    }

}
