package ru.itmo.game.objects;

import ru.itmo.game.util.Enviroment;

public interface AttackingInterface {
    boolean canAttack(Enviroment enviroment);
    void attack(Enviroment enviroment);
}
