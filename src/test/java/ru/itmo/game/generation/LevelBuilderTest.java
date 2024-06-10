package ru.itmo.game.generation;

import org.junit.jupiter.api.Test;

class LevelBuilderTest {

    @Test
    void build() {
        LevelBuilder levelBuilder = new LevelBuilder(50, 100);
        System.out.println(levelBuilder.build(4, 0.5));
        System.out.println("*".repeat(20));
        System.out.println(levelBuilder.build(4, 0.5));

    }
}