package ru.itmo.game.windows;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Separator;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.logging.Logger;

public class MainMenu {
    public static final Logger log = Logger.getLogger(MethodHandles
            .lookup()
            .lookupClass()
            .getName());

    private final Screen screen;
    private final DefaultTerminalFactory terminalFactory;

    public MainMenu(DefaultTerminalFactory terminalFactory, Screen screen) {
        this.screen = screen;
        this.terminalFactory = terminalFactory;
    }

    public void draw(WindowBasedTextGUI textGUI) {
        drawMenu(textGUI);
    }

    private void drawMenu(WindowBasedTextGUI textGUI) {
        Window window = new BasicWindow("Main menu");
        window.setHints(List.of(Window.Hint.FULL_SCREEN));

        Panel contentPanel = new Panel(new GridLayout(2));
        contentPanel.setFillColorOverride(TextColor.ANSI.BLACK_BRIGHT);
        GridLayout gridLayout = (GridLayout) contentPanel.getLayoutManager();
        gridLayout.setHorizontalSpacing(5);
        gridLayout.setVerticalSpacing(2);

        Label title = new Label("ROGUELIKE");
        title.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.CENTER, // Horizontal alignment in the grid cell if the cell is larger than the component's preferred size
                GridLayout.Alignment.BEGINNING, // Vertical alignment in the grid cell if the cell is larger than the component's preferred size
                true,       // Give the component extra horizontal space if available
                false,        // Give the component extra vertical space if available
                2,                  // Horizontal span
                1));                  // Vertical span
        contentPanel.addComponent(title);

        Runnable wip = () -> {
            MessageDialog.showMessageDialog(textGUI,
                    "Work in progress",
                    "This feature is still in development",
                    MessageDialogButton.OK);

            log.info("Work in progress");
        };

        addLabelAndButton(contentPanel, "Start game",
                () -> {
                    textGUI.removeWindow(window);
                    try {
                        runGame();
                    } catch (IOException | InterruptedException e) {
                        log.warning(e.getMessage());
                    }
                }
        );
        addLabelAndButton(contentPanel, "Settings", wip);
        addLabelAndButton(contentPanel, "Title Screen", wip);

        /*
            Close off with an empty row and a separator, then a button to close the window
        */
        contentPanel.addComponent(
                new EmptySpace()
                        .setLayoutData(
                                GridLayout.createHorizontallyFilledLayoutData(2)));
        contentPanel.addComponent(
                new Separator(Direction.HORIZONTAL)
                        .setLayoutData(
                                GridLayout.createHorizontallyFilledLayoutData(2)));
        contentPanel.addComponent(
                new Button("Close", window::close).setLayoutData(
                        GridLayout.createHorizontallyEndAlignedLayoutData(2)));

            /*
            We now have the content panel fully populated with components. A common mistake is to forget to attach it to
            the window, so let's make sure to do that.
             */
        window.setComponent(contentPanel);
        textGUI.addWindowAndWait(window);
    }

    private void addLabelAndButton(Panel contentPanel, String labelText, Runnable onClick) {
        log.info("Adding label and button" + labelText);
        contentPanel.addComponent(new Label(labelText));
        contentPanel.addComponent(new Button("Select",
                onClick)
                .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER)));
    }

    private void runGame() throws IOException, InterruptedException {
        log.info("Starting game");
        GameLoop gameLoop = new GameLoop();
        gameLoop.start();
    }
}
