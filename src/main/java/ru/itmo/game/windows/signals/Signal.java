package ru.itmo.game.windows.signals;

public enum Signal {
    MOVED_DOWN,
    MOVED_UP,
    MOVED_LEFT,
    MOVED_RIGHT,
    QUIT,
    NONE,
    PAUSE,
    RESTART,
    ATTACK,
    ITEM_1(1),
    ITEM_2(2),
    ITEM_3(3),
    ITEM_4(4),
    ITEM_5(5)
    ;
    private int number;

    Signal(int number) {
        this.number = number;
    }

    Signal() { }

    public int getNumber() {
        return number;
    }
}