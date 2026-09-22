package com.example.minesweeper.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

/**
 * Owns all Minesweeper rules and board state.
 */
public final class GameModel {

    public enum CellState {
        COVERED,
        FLAGGED,
        REVEALED,
        MINE,
        EXPLODED_MINE
    }

    public enum RevealResult {
        IGNORED,
        REVEALED,
        WON,
        LOST
    }

    private final int width;
    private final int height;
    private final int mineCount;
    private final Cell[][] cells;
    private final Random random;

    private boolean minesPlaced;
    private boolean lost;
    private int remainingSafeCells;
    private int moveCount;
    private int flagCount;
    private int explodedX;
    private int explodedY;

    public GameModel(int width, int height, int mineCount) {
        this(width, height, mineCount, new Random());
    }

    GameModel(int width, int height, int mineCount, Random random) {
        long cellCount = (long) width * height;
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Board dimensions must be positive.");
        }
        if (mineCount < 1 || mineCount >= cellCount) {
            throw new IllegalArgumentException("Mine count must be between 1 and the number of cells minus one.");
        }
        if (random == null) {
            throw new IllegalArgumentException("Random source must not be null.");
        }

        this.width = width;
        this.height = height;
        this.mineCount = mineCount;
        this.random = random;
        this.cells = new Cell[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[y][x] = new Cell();
            }
        }
        reset();
    }

    public void reset() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.reset();
            }
        }
        minesPlaced = false;
        lost = false;
        remainingSafeCells = width * height - mineCount;
        moveCount = 0;
        flagCount = 0;
        explodedX = -1;
        explodedY = -1;
    }

    public RevealResult reveal(int x, int y) {
        validateCoordinates(x, y);
        Cell selected = cells[y][x];
        if (isGameOver() || selected.revealed || selected.flagged) {
            return RevealResult.IGNORED;
        }

        if (!minesPlaced) {
            placeMines(x, y);
        }

        moveCount++;
        if (selected.mined) {
            selected.revealed = true;
            lost = true;
            explodedX = x;
            explodedY = y;
            return RevealResult.LOST;
        }

        revealConnectedArea(x, y);
        return isWon() ? RevealResult.WON : RevealResult.REVEALED;
    }

    public boolean toggleFlag(int x, int y) {
        validateCoordinates(x, y);
        Cell cell = cells[y][x];
        if (isGameOver() || cell.revealed) {
            return false;
        }

        cell.flagged = !cell.flagged;
        flagCount += cell.flagged ? 1 : -1;
        return true;
    }

    public CellState getCellState(int x, int y) {
        validateCoordinates(x, y);
        Cell cell = cells[y][x];

        if (isGameOver() && cell.mined) {
            return x == explodedX && y == explodedY ? CellState.EXPLODED_MINE : CellState.MINE;
        }
        if (cell.flagged) {
            return CellState.FLAGGED;
        }
        return cell.revealed ? CellState.REVEALED : CellState.COVERED;
    }

    public int getAdjacentMines(int x, int y) {
        validateCoordinates(x, y);
        return cells[y][x].adjacentMines;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMineCount() {
        return mineCount;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public int getFlagCount() {
        return flagCount;
    }

    public boolean isWon() {
        return !lost && minesPlaced && remainingSafeCells == 0;
    }

    public boolean isGameOver() {
        return lost || isWon();
    }

    private void placeMines(int safeX, int safeY) {
        int safePosition = positionOf(safeX, safeY);
        int[] candidates = new int[width * height - 1];
        int candidateIndex = 0;
        for (int position = 0; position < width * height; position++) {
            if (position != safePosition) {
                candidates[candidateIndex++] = position;
            }
        }

        for (int i = 0; i < mineCount; i++) {
            int selectedIndex = i + random.nextInt(candidates.length - i);
            int selectedPosition = candidates[selectedIndex];
            candidates[selectedIndex] = candidates[i];
            candidates[i] = selectedPosition;
            cells[selectedPosition / width][selectedPosition % width].mined = true;
        }

        calculateAdjacentMines();
        minesPlaced = true;
    }

    private void calculateAdjacentMines() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int adjacentMines = 0;
                for (int neighbourY = Math.max(0, y - 1);
                     neighbourY <= Math.min(height - 1, y + 1);
                     neighbourY++) {
                    for (int neighbourX = Math.max(0, x - 1);
                         neighbourX <= Math.min(width - 1, x + 1);
                         neighbourX++) {
                        if ((neighbourX != x || neighbourY != y)
                                && cells[neighbourY][neighbourX].mined) {
                            adjacentMines++;
                        }
                    }
                }
                cells[y][x].adjacentMines = adjacentMines;
            }
        }
    }

    private void revealConnectedArea(int startX, int startY) {
        Deque<Integer> pending = new ArrayDeque<>();
        pending.add(positionOf(startX, startY));

        while (!pending.isEmpty()) {
            int position = pending.removeFirst();
            int x = position % width;
            int y = position / width;
            Cell cell = cells[y][x];
            if (cell.revealed || cell.flagged || cell.mined) {
                continue;
            }

            cell.revealed = true;
            remainingSafeCells--;
            if (cell.adjacentMines != 0) {
                continue;
            }

            for (int neighbourY = Math.max(0, y - 1);
                 neighbourY <= Math.min(height - 1, y + 1);
                 neighbourY++) {
                for (int neighbourX = Math.max(0, x - 1);
                     neighbourX <= Math.min(width - 1, x + 1);
                     neighbourX++) {
                    Cell neighbour = cells[neighbourY][neighbourX];
                    if (!neighbour.revealed && !neighbour.flagged && !neighbour.mined) {
                        pending.addLast(positionOf(neighbourX, neighbourY));
                    }
                }
            }
        }
    }

    private int positionOf(int x, int y) {
        return y * width + x;
    }

    private void validateCoordinates(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException("Cell is outside the board: (" + x + ", " + y + ")");
        }
    }

    private static final class Cell {
        private boolean mined;
        private boolean revealed;
        private boolean flagged;
        private int adjacentMines;

        private void reset() {
            mined = false;
            revealed = false;
            flagged = false;
            adjacentMines = 0;
        }
    }
}
