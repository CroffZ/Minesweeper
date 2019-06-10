package model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * The class GameModel holds the model, the state of the systems.
 * It stores the following information:
 * - the state of all the dots on the board (mined or not, clicked or not, number of neighbouring mines...)
 * - the size of the board
 * - the number of steps since the last reset
 * The model provides all of this information to the other classes trough appropriate getters.
 * The controller can also update the model through setters.
 * Finally, the model is also in charge of initializing the game.
 */
public class GameModel {

    private int widthOfGame;
    private int heightOfGame;
    private DotInfo[][] model;
    private int numberOfSteps;
    private int numberUncovered;
    private int numberOfMines;
    private int numberOfFlags;

    /**
     * Constructor to initialize the model to a given size of board.
     *
     * @param width         the width of the board
     * @param height        the height of the board
     * @param numberOfMines the number of mines to hide in the board
     */
    public GameModel(int width, int height, int numberOfMines) {
        widthOfGame = width;
        heightOfGame = height;
        model = new DotInfo[heightOfGame][widthOfGame];
        this.numberOfMines = numberOfMines;
        reset();
    }

    /**
     * Resets the model to (re)start a game. The previous game (if there is one) is cleared up.
     */
    public void reset() {
        numberOfSteps = 0;
        numberUncovered = heightOfGame * widthOfGame;
        numberOfFlags = 0;
        Random generator = new Random();

        Set<Integer> minedSet = new HashSet<>();
        while (minedSet.size() < numberOfMines) {
            minedSet.add(generator.nextInt(numberUncovered));
        }

        for (int i = 0; i < heightOfGame; i++) {
            for (int j = 0; j < widthOfGame; j++) {
                model[i][j] = new DotInfo(j, i);
                if (minedSet.contains(i * widthOfGame + j)) {
                    minedSet.remove(i * widthOfGame + j);
                    model[i][j].setMined();
                }
            }
        }

        for (int i = 0; i < heightOfGame; i++) {
            for (int j = 0; j < widthOfGame; j++) {
                int neighbouringMines = 0;

                if (i > 0) {
                    if (model[i - 1][j].isMined()) {
                        neighbouringMines++;
                    }
                    if (j > 0 && model[i - 1][j - 1].isMined()) {
                        neighbouringMines++;
                    }
                    if (j < widthOfGame - 1 && model[i - 1][j + 1].isMined()) {
                        neighbouringMines++;
                    }
                }

                if (j > 0 && model[i][j - 1].isMined()) {
                    neighbouringMines++;
                }
                if (j < widthOfGame - 1 && model[i][j + 1].isMined()) {
                    neighbouringMines++;
                }

                if (i < heightOfGame - 1) {
                    if (model[i + 1][j].isMined()) {
                        neighbouringMines++;
                    }
                    if (j > 0 && model[i + 1][j - 1].isMined()) {
                        neighbouringMines++;
                    }
                    if (j < widthOfGame - 1 && model[i + 1][j + 1].isMined()) {
                        neighbouringMines++;
                    }
                }

                model[i][j].setNeighbouringMines(neighbouringMines);
            }
        }
    }

    /**
     * Getter method for the height of the game.
     *
     * @return the value of the attribute heightOfGame
     */
    public int getHeight() {
        return heightOfGame;
    }

    /**
     * Getter method for the width of the game.
     *
     * @return the value of the attribute widthOfGame
     */
    public int getWidth() {
        return widthOfGame;
    }

    /**
     * Returns true if the dot at location (i,j) is mined, false otherwise.
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */
    public boolean isMined(int i, int j) {
        return model[j][i].isMined();
    }

    /**
     * Returns true if the dot  at location (i,j) has been clicked, false otherwise.
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */
    public boolean hasBeenClicked(int i, int j) {
        return model[j][i].hasBeenClicked();
    }

    /**
     * Returns true if the dot  at location (i,j) has no mined neighbours, false otherwise.
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */
    public boolean isBlank(int i, int j) {
        return model[j][i].getNeighbouringMines() == 0;
    }

    /**
     * Returns true if the dot is covered, false otherwise.
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */
    public boolean isCovered(int i, int j) {
        return model[j][i].isCovered();
    }

    /**
     * Returns true if the dot is flag, false otherwise.
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */
    public boolean isFlag(int i, int j) {
        return model[j][i].isFlag();
    }

    /**
     * Returns the number of neighbouring mines os the dot at location (i,j).
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     * @return the number of neighbouring mines at location (i,j)
     */
    public int getNeighbouringMines(int i, int j) {
        return model[j][i].getNeighbouringMines();
    }

    /**
     * Sets the status of the dot at location (i,j) to uncovered.
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     */
    public void uncover(int i, int j) {
        numberUncovered--;
        model[j][i].uncover();
    }

    /**
     * Sets the status of the dot at location (i,j) to clicked.
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     */
    public void click(int i, int j) {
        model[j][i].click();
    }

    /**
     * Sets the flag of the dot at location (i,j).
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     */
    public void setFlag(int i, int j, boolean flag) {
        if (!model[j][i].isFlag() && flag) {
            numberOfFlags++;
        } else if (model[j][i].isFlag() && !flag) {
            numberOfFlags--;
        }
        model[j][i].setFlag(flag);
    }

    /**
     * Uncover all remaining covered dots.
     */
    public void uncoverAll() {
        for (int i = 0; i < heightOfGame; i++) {
            for (int j = 0; j < widthOfGame; j++) {
                uncover(j, i);
            }
        }
    }

    /**
     * Getter method for the current number of steps.
     *
     * @return the current number of steps
     */
    public int getNumberOfSteps() {
        return numberOfSteps;
    }

    /**
     * Getter method for the number of mines.
     *
     * @return the number of mines
     */
    public int getNumberOfMines() {
        return numberOfMines;
    }

    /**
     * Getter method for the current number of flags.
     *
     * @return the current number of flags
     */
    public int getNumberOfFlags() {
        return numberOfFlags;
    }

    /**
     * Getter method for the DotInfo reference at location (i,j).
     *
     * @param i the x coordinate of the dot
     * @param j the y coordinate of the dot
     * @return model[i][j]
     */
    public DotInfo get(int i, int j) {
        return model[j][i];
    }

    /**
     * The method <b>step</b> updates the number of steps.
     * It must be called once the model has been updated after the payer selected a new square.
     */
    public void step() {
        numberOfSteps++;
    }

    /**
     * The method <b>isFinished</b> returns true iff the game is finished,
     * that is, all the non-mined dots are uncovered.
     *
     * @return true if the game is finished, false otherwise
     */
    public boolean isFinished() {
        return numberOfMines == numberUncovered;
    }

    /**
     * Builds a String representation of the model.
     *
     * @return String representation of the model
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Covered Board: \n");
        for (int i = 0; i < heightOfGame; i++) {
            for (int j = 0; j < widthOfGame; j++) {
                if (isCovered(j, i)) {
                    stringBuilder.append("  ");
                } else {
                    if (isMined(j, i)) {
                        stringBuilder.append("B ");
                    } else {
                        stringBuilder.append(getNeighbouringMines(j, i)).append(" ");
                    }
                }
            }
            stringBuilder.append("\n");
        }

        stringBuilder.append("Uncovered Board: \n");
        for (int i = 0; i < heightOfGame; i++) {
            for (int j = 0; j < widthOfGame; j++) {
                if (isMined(j, i)) {
                    stringBuilder.append("B ");
                } else {
                    stringBuilder.append(getNeighbouringMines(j, i)).append(" ");
                }
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
