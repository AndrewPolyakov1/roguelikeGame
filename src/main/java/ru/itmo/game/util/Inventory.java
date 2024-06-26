package ru.itmo.game.util;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Inventory {
    private final List<Item> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public Inventory(List<Item> items) {
        this.items = items;
    }


    public void addItem(Item item) {
        items.add(item);
    }

    public Item useItem(int index) {
        if (index > items.size()){
            return null;
        }
        Item item = items.get(index - 1);
        items.remove(index - 1);
        return item;
    }

    public String toString() {
        List<String> result = new ArrayList<>();
        for (Item item : items) {
            result.add(Item.itemsIcons.get(item.type()) + "-" + item.level());
        }
        return String.join(" | ", result);
    }

}
