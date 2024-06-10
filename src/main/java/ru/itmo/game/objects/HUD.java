package ru.itmo.game.objects;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import ru.itmo.game.drawable.Colours;
import ru.itmo.game.drawable.DrawableInterface;
import ru.itmo.game.drawable.Symbols;
import ru.itmo.game.util.Enviroment;
import ru.itmo.game.util.Point;

public class HUD implements DrawableInterface {
    private final int screenHeight;
    private final int screenWidth;
    public static final int hudHeight = 3;
    private static final int heightOffset = 1;
    private static final int widthOffsetLeft = 2;
    private static final int widthOffsetRight = 30;
    private final Enviroment enviroment;

    public HUD(int width, int height, Enviroment enviroment) {
        screenHeight = height;
        screenWidth = width;
        this.enviroment = enviroment;
    }

    @Override
    public void draw(TextGraphics textGraphics) {
        textGraphics.setForegroundColor(Colours.HUD);

        int startPosition = 1;
        int vPosition = screenHeight - hudHeight + heightOffset;
        int playerHealth = enviroment.getPlayer().getHealth();

        for (int i = 0; i < playerHealth; i++) {
            int position = startPosition + i * widthOffsetLeft;
            textGraphics.putString(position, vPosition, Symbols.HEART);
        }

        textGraphics.putString(screenWidth - widthOffsetRight,
                vPosition,
                "LVL: %d DMG: %d EXP %d".formatted(
                        enviroment.getPlayer().getLevel(),
                        enviroment.getPlayer().getDamage(),
                        enviroment.getPlayer().getExperience()
                )
        );
    }
}
