package ru.itmo.game.windows.signals;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

public interface SignalEventProcessing {

    static boolean isMoveDown(KeyStroke keyStroke) {
        return keyStroke.getKeyType() == KeyType.ArrowDown;
    }

    static boolean isMoveLeft(KeyStroke keyStroke) {
        return keyStroke.getKeyType() == KeyType.ArrowLeft;
    }

    static boolean isMoveRight(KeyStroke keyStroke) {
        return keyStroke.getKeyType() == KeyType.ArrowRight;
    }

    static boolean isMoveUp(KeyStroke keyStroke) {
        return keyStroke.getKeyType() == KeyType.ArrowUp;
    }

    static boolean isQuitPressed(KeyStroke keyStroke) {
        return keyStroke.getKeyType() == KeyType.EOF ||
                keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() == 'q';
    }

    static boolean isSomeItemUsed(KeyStroke keyStroke) {
        return keyStroke.getKeyType() == KeyType.Character && Character.isDigit(keyStroke.getCharacter());
    }

    static boolean isAttackPressed(KeyStroke keyStroke) {
        return keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() == 'e';
    }

    static boolean isRestartPressed(KeyStroke keyStroke) {
        return keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() == 'r';
    }

    static boolean isPausePressed(KeyStroke keyStroke) {
        return keyStroke.getKeyType() == KeyType.Escape;
    }

    static Signal itemUseProcess(KeyStroke keyStroke) {
        switch (keyStroke.getCharacter()){
            case '1' -> {
                return Signal.ITEM_1;
            }
            case '2' -> {
                return Signal.ITEM_2;
            }
            case '3' -> {
                return Signal.ITEM_3;
            }
            case '4' -> {
                return Signal.ITEM_4;
            }
            case '5' -> {
                return Signal.ITEM_5;
            }
            default -> {
                return Signal.NONE;
            }
        }
    }
}
