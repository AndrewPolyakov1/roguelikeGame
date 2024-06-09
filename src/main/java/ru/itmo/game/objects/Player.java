package ru.itmo.game.objects;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import ru.itmo.game.drawable.DrawableInterface;
import ru.itmo.game.drawable.Symbols;
import ru.itmo.game.util.Point;

public class Player extends BasePerson implements DrawableInterface {
    public Player(int health, int damage, int level, Point position) {
        super(health, damage, level, position);
    }

    @Override
    public void draw(TextGraphics textGraphics) {
        textGraphics.setForegroundColor(TextColor.ANSI.RED);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLUE);
        textGraphics.putString(position.x, position.y, Symbols.PLAYER);
        textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
        textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);

    }
}
