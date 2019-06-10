package main;

import controller.GameController;

/**
 * The class <b>Minesweeper</b> launches the game.
 */
public class Minesweeper {

    private static final int DEFAULT_WIDTH = 20;
    private static final int DEFAULT_HEIGHT = 12;
    private static final int DEFAULT_MINES = 36;

    /**
     * Main entrance of the application. Creates the instance of GameController and starts the game.
     * If three parameters width, height, number of mines are passed, they are used. Otherwise, a default value is used.
     * Defaults values are also used if the parameters are too small (minimum 10 for width, 5 for heigth and 1 for number of mines).
     * Additionally, the maximum number of mines is capped at width*height-1.
     *
     * @param args command line parameters
     */
    public static void main(String[] args) {
        int width = DEFAULT_WIDTH;
        int heigth = DEFAULT_HEIGHT;
        int numberOfMines = DEFAULT_MINES;

        if (args.length == 3) {
            try {
                width = Integer.parseInt(args[0]);
                if (width < 10) {
                    System.out.println("Invalid argument, using default...");
                    width = DEFAULT_WIDTH;
                }
                heigth = Integer.parseInt(args[1]);
                if (heigth < 5) {
                    System.out.println("Invalid argument, using default...");
                    heigth = DEFAULT_HEIGHT;
                }
                numberOfMines = Integer.parseInt(args[2]);
                if (numberOfMines < 1) {
                    System.out.println("Invalid argument, using default...");
                    numberOfMines = DEFAULT_MINES;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid argument, using default...");
                width = DEFAULT_WIDTH;
                heigth = DEFAULT_HEIGHT;
                numberOfMines = DEFAULT_MINES;
            }
        }

        if (numberOfMines >= width * heigth) {
            System.out.println("Too many mines: " + numberOfMines
                    + " mines on " + (width * heigth) + " spots. Using "
                    + (width * heigth - 1) + " instead. Good luck!");
            numberOfMines = (width * heigth - 1);
        }

        new GameController(width, heigth, numberOfMines);
    }
}
