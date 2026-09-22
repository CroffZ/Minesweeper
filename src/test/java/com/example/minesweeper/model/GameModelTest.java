package com.example.minesweeper.model;

import com.example.minesweeper.model.GameModel.CellState;
import com.example.minesweeper.model.GameModel.RevealResult;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameModelTest {

    @Test
    public void firstRevealIsSafeAndPlacesTheConfiguredNumberOfMines() {
        GameModel model = new GameModel(4, 3, 3, new HighestIndexRandom());

        assertEquals(RevealResult.REVEALED, model.reveal(0, 0));
        assertEquals(CellState.REVEALED, model.getCellState(0, 0));
        assertEquals(0, countMines(model));
        assertEquals(RevealResult.LOST, model.reveal(3, 2));
        assertEquals(3, countMines(model));
    }

    @Test
    public void emptyAreaFloodFillRevealsAllSafeCellsAndWins() {
        GameModel model = new GameModel(3, 3, 1, new HighestIndexRandom());

        assertEquals(RevealResult.WON, model.reveal(0, 0));
        assertTrue(model.isWon());
        assertTrue(model.isGameOver());
        assertEquals(1, model.getMoveCount());
        assertEquals(CellState.MINE, model.getCellState(2, 2));
        assertEquals(8, countRevealed(model));
    }

    @Test
    public void flagsCanBeToggledButDoNotChangeMoveCountOrReveal() {
        GameModel model = new GameModel(3, 3, 1, new HighestIndexRandom());

        assertTrue(model.toggleFlag(1, 1));
        assertEquals(CellState.FLAGGED, model.getCellState(1, 1));
        assertEquals(1, model.getFlagCount());
        assertEquals(RevealResult.IGNORED, model.reveal(1, 1));
        assertEquals(0, model.getMoveCount());

        assertTrue(model.toggleFlag(1, 1));
        assertEquals(CellState.COVERED, model.getCellState(1, 1));
        assertEquals(0, model.getFlagCount());
    }

    @Test
    public void revealingMineLosesAndRevealsAllMines() {
        GameModel model = new GameModel(3, 3, 1, new LowestIndexRandom());

        model.reveal(0, 0);
        assertEquals(RevealResult.LOST, model.reveal(1, 0));
        assertFalse(model.isWon());
        assertTrue(model.isGameOver());
        assertEquals(CellState.EXPLODED_MINE, model.getCellState(1, 0));
        assertEquals(2, model.getMoveCount());
    }

    @Test
    public void resetClearsBoardStateAndStartsWithNoMinesPlaced() {
        GameModel model = new GameModel(3, 3, 1, new HighestIndexRandom());

        model.toggleFlag(1, 1);
        model.reveal(0, 0);
        model.reset();

        assertEquals(0, model.getMoveCount());
        assertEquals(0, model.getFlagCount());
        assertEquals(CellState.COVERED, model.getCellState(0, 0));
        assertEquals(CellState.COVERED, model.getCellState(2, 2));
        assertFalse(model.isGameOver());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsMineCountThatLeavesNoSafeCell() {
        new GameModel(2, 2, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNonPositiveWidth() {
        new GameModel(0, 5, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNonPositiveHeight() {
        new GameModel(5, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void rejectsCoordinatesOutsideBoard() {
        new GameModel(2, 2, 1).reveal(2, 0);
    }

    @Test
    public void adjacentMineCountReflectsSurroundingMines() {
        GameModel model = new GameModel(3, 3, 1, new HighestIndexRandom());

        model.reveal(0, 0);
        // With HighestIndexRandom, the single mine lands at the last candidate position, (2, 2).
        assertEquals(0, model.getAdjacentMines(0, 0));
        assertEquals(1, model.getAdjacentMines(1, 1));
        assertEquals(1, model.getAdjacentMines(2, 1));
        assertEquals(1, model.getAdjacentMines(1, 2));
    }

    @Test
    public void gameOverIgnoresFurtherRevealsAndFlagToggles() {
        GameModel model = new GameModel(3, 3, 1, new LowestIndexRandom());

        model.reveal(0, 0);
        assertEquals(RevealResult.LOST, model.reveal(1, 0));
        assertTrue(model.isGameOver());

        assertEquals(RevealResult.IGNORED, model.reveal(0, 1));
        assertFalse(model.toggleFlag(0, 1));
        assertEquals(CellState.COVERED, model.getCellState(0, 1));
        assertEquals(0, model.getFlagCount());
    }

    @Test
    public void winningGameIgnoresFurtherRevealsAndFlagToggles() {
        GameModel model = new GameModel(3, 3, 1, new HighestIndexRandom());

        assertEquals(RevealResult.WON, model.reveal(0, 0));
        assertTrue(model.isGameOver());

        assertFalse(model.toggleFlag(2, 2));
        assertEquals(RevealResult.IGNORED, model.reveal(2, 2));
    }

    private static int countRevealed(GameModel model) {
        int count = 0;
        for (int y = 0; y < model.getHeight(); y++) {
            for (int x = 0; x < model.getWidth(); x++) {
                if (model.getCellState(x, y) == CellState.REVEALED) {
                    count++;
                }
            }
        }
        return count;
    }

    private static int countMines(GameModel model) {
        int count = 0;
        for (int y = 0; y < model.getHeight(); y++) {
            for (int x = 0; x < model.getWidth(); x++) {
                if (model.getCellState(x, y) == CellState.MINE
                        || model.getCellState(x, y) == CellState.EXPLODED_MINE) {
                    count++;
                }
            }
        }
        return count;
    }

    private static final class HighestIndexRandom extends Random {
        private static final long serialVersionUID = 1L;

        @Override
        public int nextInt(int bound) {
            return bound - 1;
        }
    }

    private static final class LowestIndexRandom extends Random {
        private static final long serialVersionUID = 1L;

        @Override
        public int nextInt(int bound) {
            return 0;
        }
    }
}
