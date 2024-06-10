package ru.itmo.game.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.itmo.game.util.Point;

import java.io.Serializable;

public abstract class BasePerson implements Serializable {
    @Getter
    @Setter
    public int health;
    @Getter
    @Setter
    public int damage;
    @Getter
    @Setter
    public int level;
    @Getter
    @Setter
    public Point position;

    public BasePerson(@JsonProperty("health") int health, @JsonProperty("damage") int damage, @JsonProperty("level") int level, @JsonProperty("position") Point position) {
        this.health = health;
        this.damage = damage;
        this.level = level;
        this.position = position;
    }
}
