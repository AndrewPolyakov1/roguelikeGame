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
import ru.itmo.game.generation.LevelBuilder;
import ru.itmo.game.objects.Level;
import ru.itmo.game.objects.Player;
import ru.itmo.game.util.Environment;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Random;
import java.util.logging.Logger;

public class GameLoop {
    public static final Logger log = Logger.getLogger(MethodHandles
            .lookup()
            .lookupClass()
            .getName());

    private Environment environment;

    @Getter
    @Setter
    private int FPS = 5;

    public static void main(String[] args) {
        try {
            GameLoop gameLoop = new GameLoop();
            gameLoop.start();
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }

    public void start() throws InterruptedException, IOException {
        log.info("GameLoop started");

        int WIDTH = 100;
        int HEIGHT = 50;
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

        //  Create a new Environment
        LevelBuilder levelBuilder = new LevelBuilder(
                WIDTH,
                HEIGHT
        );
        Level level = levelBuilder.build(5, 0.4);

        Environment environment = new Environment(WIDTH, HEIGHT, level);
        Player player = new Player(
                10,
                10,
                1,
                environment.generateRandomEmptyPoint()
        );
        environment.setPlayer(player);

        //

        boolean interrupted = false;
        long lastUpdateTime = System.currentTimeMillis();

        while (!interrupted) {

            textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
            textGraphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(terminalSize.getColumns(), terminalSize.getRows()), ' ');

            // Render

            textGraphics.setForegroundColor(TextColor.ANSI.GREEN);

            environment.drawLevel(textGraphics);
            environment.drawPlayer(textGraphics);
            ////////////////////////////

            screen.refresh();

            //////////////////////////////////////////


            // Keyboard events

            KeyStroke keyStroke = screen.pollInput();

            Signal signal = handleInput(keyStroke, environment);
            if (signal == Signal.QUIT){
                interrupted = true;
            }

            // Player events
                if (environment.isLevelDone()){
                    log.info("Starting new level");
                    environment.newLevel();
                }
            //

            long currentTime = System.currentTimeMillis();

            if (currentTime - lastUpdateTime >= 1000 / FPS) {
                // Game events

                //
                lastUpdateTime = currentTime;
            }
        }

        screen.stopScreen();
    }

    private Signal handleInput(KeyStroke keyStroke, Environment environment) {
        if (keyStroke != null) {
            if (keyStroke.getKeyType() == KeyType.ArrowDown) {
                log.info("ArrowDown");
                if (environment.tryMovePlayerDown()) {
                    return Signal.MOVED_DOWN;
                } else {
                    return Signal.NONE;
                }
            } else if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
                log.info("ArrowLeft");
                if (environment.tryMovePlayerLeft()) {
                    return Signal.MOVED_DOWN;
                } else {
                    return Signal.NONE;
                }
            } else if (keyStroke.getKeyType() == KeyType.ArrowRight) {
                log.info("ArrowRight");
                if (environment.tryMovePlayerRight()) {
                    return Signal.MOVED_DOWN;
                } else {
                    return Signal.NONE;
                }
            } else if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                log.info("ArrowUp");
                if (environment.tryMovePlayerUp()) {
                    return Signal.MOVED_DOWN;
                } else {
                    return Signal.NONE;
                }
            } else if (
                    keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() == 'q'
            ) {
                return Signal.QUIT;
            }

        }
        return Signal.NONE;

    }

    enum Signal {
        MOVED_DOWN,
        MOVED_UP,
        MOVED_LEFT,
        MOVED_RIGHT,
        QUIT,
        NONE
    }
}
