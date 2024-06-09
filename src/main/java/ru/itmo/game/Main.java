package ru.itmo.game;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import ru.itmo.game.windows.MainMenu;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

public class Main {
    public static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    public static void main(String[] args) {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Screen screen = null;

        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();

            log.info("Screen created");

            final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);

            MainMenu mainMenu = new MainMenu(terminalFactory, screen);
            mainMenu.draw(textGUI);


        } catch (IOException e) {
            log.warning(e.getMessage());
        } finally {
            if (screen != null) {
                try {
                    /*
                    The close() call here will restore the terminal by exiting from private mode which was done in
                    the call to startScreen(), and also restore things like echo mode and intr
                     */
                    screen.close();
                } catch (IOException e) {
                    log.warning(e.getMessage());
                }
            }
        }
    }
}