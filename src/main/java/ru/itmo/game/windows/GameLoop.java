package ru.itmo.game.windows;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import lombok.Getter;
import lombok.Setter;
import ru.itmo.game.drawable.Colours;
import ru.itmo.game.util.Enviroment;
import ru.itmo.game.util.WorldState;
import ru.itmo.game.windows.signals.Signal;
import ru.itmo.game.windows.signals.SignalEventProcessing;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

@Setter
@Getter
public class GameLoop {
    public static final Logger log = Logger.getLogger(
            MethodHandles
                    .lookup()
                    .lookupClass()
                    .getName()
    );
    private static final int FPS = 5;
    private static final int WIDTH = 150;
    private static final int HEIGHT = 60;


    public static void main(String[] args) {
        try {
            GameLoop gameLoop = new GameLoop();
            gameLoop.start();
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }

    public void start() throws IOException {
        log.info("GameLoop started");

        Screen screen;
        TextGraphics textGraphics;
        TerminalSize terminalSize;
        try {
            terminalSize = new TerminalSize(WIDTH, HEIGHT);
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
            terminalFactory.setInitialTerminalSize(terminalSize);
            Terminal terminal = terminalFactory.createTerminal();
            screen = new TerminalScreen(terminal);
            textGraphics = screen.newTextGraphics();
            screen.startScreen();
        } catch (IOException e) {
            log.warning(e.getMessage());
            return;
        }

        mainGameLoop(
                textGraphics,
                terminalSize,
                screen
        );

        screen.stopScreen();
    }

    private void mainGameLoop(TextGraphics textGraphics, TerminalSize terminalSize, Screen screen) throws IOException {
        boolean isInterrupted = false;
        boolean isPaused = false;
        boolean isGameOver = false;

        WorldState world = new WorldState(WIDTH, HEIGHT);
        long lastUpdateTime = System.currentTimeMillis();

        while (!isInterrupted) {
            textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
            textGraphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(terminalSize.getColumns(), terminalSize.getRows()), ' ');

            // Render
            textGraphics.setForegroundColor(Colours.DEFAULT);
            world.draw(textGraphics);
            screen.refresh();

            // Keyboard events
            KeyStroke keyStroke = screen.pollInput();

            Signal signal = handleInput(keyStroke, world);
            if (signal == Signal.QUIT) {
                isInterrupted = true;
            }
            if (signal == Signal.PAUSE) {
                isPaused = true;
            }
            if (signal == Signal.RESTART) {
                world.restart();
            }
            if (signal == Signal.ATTACK) {
                world.playerAttack();
            }
            if (
                    signal == Signal.ITEM_1 ||
                            signal == Signal.ITEM_2 ||
                            signal == Signal.ITEM_3 ||
                            signal == Signal.ITEM_4 ||
                            signal == Signal.ITEM_5
            ) {
                world.useItem(signal.getNumber());
            }

            if (isPaused) {
                pauseScreen(WIDTH, HEIGHT, textGraphics);
                screen.refresh();
                screen.readInput();
                isPaused = false;
                continue;
            }

            // Player events
            if (world.isLevelDone()) {
                log.info("Starting new level");
                newLevel(WIDTH, HEIGHT, textGraphics, world);
                screen.refresh();
                screen.readInput();
                world.newLevel();
            }

            if (isGameOver) {
                screen.clear();
                gameOver(WIDTH, HEIGHT, textGraphics);
                screen.refresh();
                // Blocking
                screen.readInput();
                world.restart();
            }

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdateTime >= 1000 / FPS) {
                isGameOver = world.update();
                lastUpdateTime = currentTime;
            }
        }
    }

    private Signal handleInput(KeyStroke keyStroke, WorldState worldState) {
        Enviroment enviroment = worldState.getEnvironment();
        if (keyStroke != null) {
            if (SignalEventProcessing.isMoveDown(keyStroke)) {
                log.info("ArrowDown");
                if (enviroment.tryMovePlayerDown()) {
                    return Signal.MOVED_DOWN;
                } else {
                    return Signal.NONE;
                }
            } else if (SignalEventProcessing.isMoveLeft(keyStroke)) {
                log.info("ArrowLeft");
                if (enviroment.tryMovePlayerLeft()) {
                    return Signal.MOVED_LEFT;
                } else {
                    return Signal.NONE;
                }
            } else if (SignalEventProcessing.isMoveRight(keyStroke)) {
                log.info("ArrowRight");
                if (enviroment.tryMovePlayerRight()) {
                    return Signal.MOVED_RIGHT;
                } else {
                    return Signal.NONE;
                }
            } else if (SignalEventProcessing.isMoveUp(keyStroke)) {
                log.info("ArrowUp");
                if (enviroment.tryMovePlayerUp()) {
                    return Signal.MOVED_UP;
                } else {
                    return Signal.NONE;
                }
            } else if (SignalEventProcessing.isQuitPressed(keyStroke)) {
                return Signal.QUIT;
            } else if (SignalEventProcessing.isPausePressed(keyStroke)) {
                log.info("Game Paused");
                return Signal.PAUSE;
            } else if (SignalEventProcessing.isRestartPressed(keyStroke)) {
                return Signal.RESTART;
            } else if (SignalEventProcessing.isAttackPressed(keyStroke)) {
                log.info("Attack key pressed");
                return Signal.ATTACK;
            } else if (SignalEventProcessing.isSomeItemUsed(keyStroke)
            ) {
                log.info("Item use key pressed");
                return SignalEventProcessing.itemUseProcess(keyStroke);
            }
        }
        return Signal.NONE;
    }

    public void pauseScreen(int width, int height, TextGraphics graphics) {
        String text = "Game Paused";
        String text2 = "Press 'escape' to resume";
        graphics.setBackgroundColor(TextColor.ANSI.BLACK);
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        graphics.setForegroundColor(Colours.TEXT_COLOR);
        graphics.putString(width / 2 - (text.length() / 2), height / 2 - 1, text);
        graphics.putString(width / 2 - (text2.length() / 2), height / 2, text2);
    }

    public void gameOver(int width, int height, TextGraphics graphics) {
        String text = "Game Over";
        String text2 = "Press 'r' to restart";
        graphics.setBackgroundColor(TextColor.ANSI.BLACK);
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        graphics.setForegroundColor(Colours.TEXT_COLOR);
        graphics.putString(width / 2 - (text.length() / 2), height / 2 - 1, text);
        graphics.putString(width / 2 - (text2.length() / 2), height / 2, text2);
    }

    public void newLevel(int width, int height, TextGraphics graphics, WorldState world) {
        String text = "Level " + (world.getCurrentLevel() + 1);
        String text2 = "Press 'enter' to start";
        graphics.setBackgroundColor(TextColor.ANSI.BLACK);
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        graphics.setForegroundColor(Colours.TEXT_COLOR);
        graphics.putString(width / 2 - (text.length() / 2), height / 2 - 1, text);
        graphics.putString(width / 2 - (text2.length() / 2), height / 2, text2);
    }
}
