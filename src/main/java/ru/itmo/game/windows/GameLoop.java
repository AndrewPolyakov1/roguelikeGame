package ru.itmo.game.windows;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import lombok.Getter;
import lombok.Setter;
import ru.itmo.game.drawable.Colours;
import ru.itmo.game.util.Enviroment;
import ru.itmo.game.util.WorldState;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

@Setter
@Getter
public class GameLoop {
    public static final Logger log = Logger.getLogger(MethodHandles
            .lookup()
            .lookupClass()
            .getName());

    private int FPS = 5;

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

        int WIDTH = 150;
        int HEIGHT = 60;
        Screen screen;
        TextGraphics textGraphics;
        TerminalSize terminalSize;
        try {
            terminalSize = new TerminalSize(WIDTH, HEIGHT);
            // Create a terminal with the specified size
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

        //  Create a new WorldState
        WorldState world = new WorldState(WIDTH, HEIGHT);
        //

        boolean interrupted = false;
        long lastUpdateTime = System.currentTimeMillis();
        boolean paused = false;
        boolean gameOver = false;


        /*
        Main game loop
         */
        while (!interrupted) {

            textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
            textGraphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(terminalSize.getColumns(), terminalSize.getRows()), ' ');

            // Render

            textGraphics.setForegroundColor(Colours.DEFAULT);

            world.draw(textGraphics);
            ////////////////////////////

            screen.refresh();

            //////////////////////////////////////////


            // Keyboard events

            KeyStroke keyStroke = screen.pollInput();

            Signal signal = handleInput(keyStroke, world);
            if (signal == Signal.QUIT) {
                interrupted = true;
            }
            if (signal == Signal.PAUSE) {
                paused = true;
            }
            if (signal == Signal.RESTART) {
                world.restart();
            }
            if (signal == Signal.ATTACK) {
                world.playerAttack();
            }

            if (paused) {

                pauseScreen(WIDTH, HEIGHT, textGraphics);
                screen.refresh();
                screen.readInput();
                paused = false;
                continue;
            }

            // Player events
            if (world.isLevelDone()) {
                log.info("Starting new level");
                world.newLevel();
            }

            if (gameOver) {
                screen.clear();
                gameOver(WIDTH, HEIGHT, textGraphics);
                screen.refresh();
                // Blocking
                screen.readInput();

                world.restart();
            }
            //

            long currentTime = System.currentTimeMillis();

            if (currentTime - lastUpdateTime >= 1000 / FPS) {
                // Game events
                gameOver = world.update();
                //
                lastUpdateTime = currentTime;
            }
        }

        screen.stopScreen();
    }

    private Signal handleInput(KeyStroke keyStroke, WorldState worldState) {
        Enviroment enviroment = worldState.environment;
        if (keyStroke != null) {
            if (keyStroke.getKeyType() == KeyType.ArrowDown) {
                log.info("ArrowDown");
                if (enviroment.tryMovePlayerDown()) {
                    return Signal.MOVED_DOWN;
                } else {
                    return Signal.NONE;
                }
            } else if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
                log.info("ArrowLeft");
                if (enviroment.tryMovePlayerLeft()) {
                    return Signal.MOVED_LEFT;
                } else {
                    return Signal.NONE;
                }
            } else if (keyStroke.getKeyType() == KeyType.ArrowRight) {
                log.info("ArrowRight");
                if (enviroment.tryMovePlayerRight()) {
                    return Signal.MOVED_RIGHT;
                } else {
                    return Signal.NONE;
                }
            } else if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                log.info("ArrowUp");
                if (enviroment.tryMovePlayerUp()) {
                    return Signal.MOVED_UP;
                } else {
                    return Signal.NONE;
                }
            } else if (keyStroke.getKeyType() == KeyType.EOF ||
                    keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() == 'q'
            ) {
                return Signal.QUIT;
            } else if (keyStroke.getKeyType() == KeyType.Escape
            ) {
                log.info("Game Paused");
                return Signal.PAUSE;
            } else if (keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() == 'r'
            ) {
                return Signal.RESTART;
            } else if (keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() == 'e'
            ) {
                log.info("Attack key pressed");
                return Signal.ATTACK;
            }


        }
        return Signal.NONE;

    }

    public void pauseScreen(int width, int height, TextGraphics graphics) {
        String text = "Game Paused";
        graphics.setBackgroundColor(TextColor.ANSI.BLACK);
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        graphics.setForegroundColor(Colours.TEXT_COLOR);
        graphics.putString(width / 2 - (text.length() / 2), height / 2 - 1, text);
    }

    public void gameOver(int width, int height, TextGraphics graphics) {
        String text = "Game Over";
        graphics.setBackgroundColor(TextColor.ANSI.BLACK);
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        graphics.setForegroundColor(Colours.TEXT_COLOR);
        graphics.putString(width / 2 - (text.length() / 2), height / 2 - 1, text);

    }

    enum Signal {
        MOVED_DOWN,
        MOVED_UP,
        MOVED_LEFT,
        MOVED_RIGHT,
        QUIT,
        NONE,
        PAUSE,
        RESTART,
        ATTACK
    }
}
