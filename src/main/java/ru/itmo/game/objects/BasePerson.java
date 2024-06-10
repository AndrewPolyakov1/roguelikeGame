package ru.itmo.game.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.itmo.game.util.Point;

import java.io.Serializable;

@Setter
@Getter
public abstract class BasePerson implements Serializable {
    public int health;
    public int damage;
    public int level;
    public Point position;
    private int attackRadius = 4;
    private long lastAttack = System.currentTimeMillis();

    public BasePerson(@JsonProperty("health") int health, @JsonProperty("damage") int damage, @JsonProperty("level") int level, @JsonProperty("position") Point position) {
        this.health = health;
        this.damage = damage;
        this.level = level;
        this.position = position;
    }
}
