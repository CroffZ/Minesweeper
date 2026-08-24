package com.example.minesweeper.controller;

import com.example.minesweeper.model.GameModel;
import com.example.minesweeper.model.GameModel.RevealResult;
import com.example.minesweeper.view.DotButton;
import com.example.minesweeper.view.GameView;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Translates user input into model operations and view updates.
 */
public final class GameController implements ActionListener, MouseListener {

    private final GameModel model;
    private final GameView view;

    public GameController(int width, int height, int mineCount) {
        model = new GameModel(width, height, mineCount);
        view = new GameView(model, this, this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source instanceof DotButton) {
            DotButton button = (DotButton) source;
            RevealResult result = model.reveal(button.getColumn(), button.getRow());
            view.refresh();
            if (result == RevealResult.WON || result == RevealResult.LOST) {
                showCompletionDialog(result);
            }
            return;
        }

        if (GameView.RESET_COMMAND.equals(event.getActionCommand())) {
            model.reset();
            view.refresh();
        } else if (GameView.QUIT_COMMAND.equals(event.getActionCommand())) {
            view.dispose();
        }
    }

    private void showCompletionDialog(RevealResult result) {
        String title = result == RevealResult.WON ? "You won!" : "Game over";
        String message = result == RevealResult.WON
                ? "You cleared the board in " + model.getMoveCount() + " moves."
                : "A mine ended the game after " + model.getMoveCount() + " moves.";
        String[] options = {"Play Again", "Quit"};
        int selected = JOptionPane.showOptionDialog(
                view,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (selected == JOptionPane.YES_OPTION) {
            model.reset();
            view.refresh();
        } else {
            view.dispose();
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (!SwingUtilities.isRightMouseButton(event) || !(event.getSource() instanceof DotButton)) {
            return;
        }

        DotButton button = (DotButton) event.getSource();
        if (model.toggleFlag(button.getColumn(), button.getRow())) {
            view.refresh();
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
    }

    @Override
    public void mouseReleased(MouseEvent event) {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }
}
