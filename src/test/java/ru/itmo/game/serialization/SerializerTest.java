package ru.itmo.game.serialization;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.itmo.game.objects.Enemy;
import ru.itmo.game.objects.Level;
import ru.itmo.game.objects.enemies.AgressiveBehavior;
import ru.itmo.game.util.Enviroment;
import ru.itmo.game.util.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class SerializerTest {

    @Test
    void serialize() throws IOException {
        Serializer serializer = new Serializer(Path.of("src/test/resources"));

        boolean[][] cellGrid = new boolean[][]{
                {true, true, true},
                {true, false, true},
                {true, true, true}
        };
        List<Enemy> enemies = List.of(new Enemy(
                Enemy.EnemyType.VILLAGER,
                new AgressiveBehavior(),
                10,
                20,
                1,
                Point.of(1, 2),
                new Enviroment(10, 10, new Level(3, 3, cellGrid)
                )
        ));
        Level level = new Level(3, 3, cellGrid);
        String serialized = serializer.serialize(level);
        System.out.println(serialized);
    }

    @Test
    void deserialize() {
        String objectJson = "{\"width\":3,\"height\":3,\"cellGrid\":[[true,true,true],[true,false,true],[true,true,true]]}";
        Serializer serializer = new Serializer(Path.of("test/resources"));
        try {
            Level level = serializer.deserialize(objectJson, Level.class);
            System.out.println(level);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void serializeToFile() throws IOException {
        Serializer serializer = new Serializer(Path.of("src/test/resources"));
        boolean[][] cellGrid = new boolean[][]{
                {true, true, true},
                {true, false, true},
                {true, true, true}
        };
        Level level = new Level(3, 3, cellGrid);
        serializer.serializeToFile(level, "testLevel.json");

        Assertions.assertTrue(Files.exists(Path.of("src/test/resources/testLevel.json")));
    }

//    @Test
//    void deserializeFromFile() throws IOException {
//        Serializer serializer = new Serializer(Path.of("src/test/resources"));
//
//        Level level = serializer.deserializeFromFile("testLevel.json", Level.class);
//        System.out.println(level);
//    }


}