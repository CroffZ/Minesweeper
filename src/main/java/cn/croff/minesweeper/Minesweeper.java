package cn.croff.minesweeper;

import cn.croff.minesweeper.controller.GameController;

import javax.swing.SwingUtilities;

/**
 * Application entry point.
 */
public final class Minesweeper {

    private static final int DEFAULT_WIDTH = 20;
    private static final int DEFAULT_HEIGHT = 12;
    private static final int DEFAULT_MINES = 36;
    private static final int MINIMUM_WIDTH = 10;
    private static final int MINIMUM_HEIGHT = 5;

    private Minesweeper() {
    }

    public static void main(String[] args) {
        final GameSettings settings = parseSettings(args);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameController(settings.width, settings.height, settings.mines);
            }
        });
    }

    private static GameSettings parseSettings(String[] args) {
        if (args.length == 0) {
            return defaultSettings();
        }
        if (args.length != 3) {
            return useDefaults("Expected either no arguments or: <width> <height> <mines>.");
        }

        try {
            int width = Integer.parseInt(args[0]);
            int height = Integer.parseInt(args[1]);
            int mines = Integer.parseInt(args[2]);
            long cells = (long) width * height;

            if (width < MINIMUM_WIDTH || height < MINIMUM_HEIGHT) {
                return useDefaults("The board must be at least "
                        + MINIMUM_WIDTH + "x" + MINIMUM_HEIGHT + ".");
            }
            if (mines < 1 || mines >= cells) {
                return useDefaults("The mine count must be between 1 and the number of cells minus one.");
            }
            return new GameSettings(width, height, mines);
        } catch (NumberFormatException exception) {
            return useDefaults("Width, height, and mines must be whole numbers.");
        }
    }

    private static GameSettings useDefaults(String reason) {
        System.err.println(reason);
        System.err.println("Starting the default 20x12 board with 36 mines.");
        return defaultSettings();
    }

    private static GameSettings defaultSettings() {
        return new GameSettings(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_MINES);
    }

    private static final class GameSettings {
        private final int width;
        private final int height;
        private final int mines;

        private GameSettings(int width, int height, int mines) {
            this.width = width;
            this.height = height;
            this.mines = mines;
        }
    }
}
