package ru.itmo.game.util;

import java.util.Map;

import static ru.itmo.game.util.RandomUtil.randomEnum;

public record Item(ItemType type, int level) {

    public static Map<ItemType, String> itemsIcons = Map.of(
            ItemType.COOLDOWN, "CD",
            ItemType.DAMAGE, "DM",
            ItemType.HEALTH, "HL"
    );

    public static Item createRandomItem(int level) {
        ItemType type = randomEnum(ItemType.class);
        return new Item(type, level);
    }
    @SuppressWarnings("unused")
    public static Item createItem(ItemType type, int level) {
        return new Item(type, level);
    }

    public enum ItemType {
        COOLDOWN,
        DAMAGE,
        HEALTH,
    }
}
