package com.example.minesweeper.view;

import com.example.minesweeper.model.GameModel.CellState;

import javax.swing.Icon;
import javax.swing.JButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

/**
 * A board button with programmatically rendered tile artwork.
 */
public final class DotButton extends JButton {

    private static final long serialVersionUID = 1L;
    private static final int TILE_SIZE = 28;
    private static final Icon COVERED_ICON = new TileIcon(CellState.COVERED, 0);
    private static final Icon FLAGGED_ICON = new TileIcon(CellState.FLAGGED, 0);
    private static final Icon MINE_ICON = new TileIcon(CellState.MINE, 0);
    private static final Icon EXPLODED_MINE_ICON = new TileIcon(CellState.EXPLODED_MINE, 0);
    private static final Icon[] NUMBER_ICONS = createNumberIcons();

    private final int column;
    private final int row;

    public DotButton(int column, int row) {
        this.column = column;
        this.row = row;
        setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
        setMargin(new Insets(0, 0, 0, 0));
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public void display(CellState state, int adjacentMines) {
        String description;
        switch (state) {
            case COVERED:
                setIcon(COVERED_ICON);
                description = "Covered cell";
                break;
            case FLAGGED:
                setIcon(FLAGGED_ICON);
                description = "Flagged cell";
                break;
            case MINE:
                setIcon(MINE_ICON);
                description = "Mine";
                break;
            case EXPLODED_MINE:
                setIcon(EXPLODED_MINE_ICON);
                description = "Exploded mine";
                break;
            case REVEALED:
                if (adjacentMines < 0 || adjacentMines >= NUMBER_ICONS.length) {
                    throw new IllegalArgumentException("Adjacent mine count must be between 0 and 8.");
                }
                setIcon(NUMBER_ICONS[adjacentMines]);
                description = adjacentMines == 0
                        ? "Empty revealed cell"
                        : "Revealed cell with " + adjacentMines + " adjacent mines";
                break;
            default:
                throw new IllegalArgumentException("Unsupported cell state: " + state);
        }
        setToolTipText(description);
        getAccessibleContext().setAccessibleName(description);
    }

    private static Icon[] createNumberIcons() {
        Icon[] icons = new Icon[9];
        for (int number = 0; number < icons.length; number++) {
            icons[number] = new TileIcon(CellState.REVEALED, number);
        }
        return icons;
    }

    private static final class TileIcon implements Icon {

        private static final Color TILE = new Color(192, 192, 192);
        private static final Color LIGHT_EDGE = Color.WHITE;
        private static final Color DARK_EDGE = new Color(112, 112, 112);
        private static final Color[] NUMBER_COLORS = {
                Color.BLACK,
                new Color(25, 76, 170),
                new Color(35, 125, 55),
                new Color(190, 45, 45),
                new Color(70, 45, 145),
                new Color(130, 35, 35),
                new Color(30, 125, 125),
                Color.BLACK,
                new Color(90, 90, 90)
        };

        private final CellState state;
        private final int number;

        private TileIcon(CellState state, int number) {
            this.state = state;
            this.number = number;
        }

        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics2D canvas = (Graphics2D) graphics.create();
            try {
                canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                paintTile(canvas, x, y);
                if (state == CellState.FLAGGED) {
                    paintFlag(canvas, x, y);
                } else if (state == CellState.MINE || state == CellState.EXPLODED_MINE) {
                    paintMine(canvas, x, y);
                } else if (state == CellState.REVEALED && number > 0) {
                    paintNumber(canvas, x, y);
                }
            } finally {
                canvas.dispose();
            }
        }

        private void paintTile(Graphics2D canvas, int x, int y) {
            canvas.setColor(state == CellState.EXPLODED_MINE ? new Color(220, 70, 70) : TILE);
            canvas.fillRect(x, y, TILE_SIZE, TILE_SIZE);

            if (state == CellState.COVERED || state == CellState.FLAGGED) {
                canvas.setStroke(new BasicStroke(3f));
                canvas.setColor(LIGHT_EDGE);
                canvas.drawLine(x + 1, y + TILE_SIZE - 2, x + 1, y + 1);
                canvas.drawLine(x + 1, y + 1, x + TILE_SIZE - 2, y + 1);
                canvas.setColor(DARK_EDGE);
                canvas.drawLine(x + TILE_SIZE - 2, y + 1, x + TILE_SIZE - 2, y + TILE_SIZE - 2);
                canvas.drawLine(x + TILE_SIZE - 2, y + TILE_SIZE - 2, x + 1, y + TILE_SIZE - 2);
            } else {
                canvas.setColor(DARK_EDGE);
                canvas.drawRect(x, y, TILE_SIZE - 1, TILE_SIZE - 1);
            }
        }

        private void paintFlag(Graphics2D canvas, int x, int y) {
            canvas.setColor(new Color(45, 45, 45));
            canvas.fillRect(x + 13, y + 7, 2, 14);
            canvas.fillRect(x + 9, y + 20, 10, 2);
            canvas.setColor(new Color(210, 40, 40));
            int[] xPoints = {x + 14, x + 14, x + 6};
            int[] yPoints = {y + 7, y + 15, y + 11};
            canvas.fillPolygon(xPoints, yPoints, 3);
        }

        private void paintMine(Graphics2D canvas, int x, int y) {
            canvas.setColor(new Color(35, 35, 35));
            canvas.setStroke(new BasicStroke(2f));
            canvas.drawLine(x + 7, y + 7, x + 21, y + 21);
            canvas.drawLine(x + 21, y + 7, x + 7, y + 21);
            canvas.drawLine(x + 14, y + 4, x + 14, y + 24);
            canvas.drawLine(x + 4, y + 14, x + 24, y + 14);
            canvas.fillOval(x + 8, y + 8, 12, 12);
            canvas.setColor(Color.WHITE);
            canvas.fillOval(x + 10, y + 10, 3, 3);
        }

        private void paintNumber(Graphics2D canvas, int x, int y) {
            Font font = new Font(Font.SANS_SERIF, Font.BOLD, 17);
            canvas.setFont(font);
            canvas.setColor(NUMBER_COLORS[number]);
            String text = Integer.toString(number);
            FontMetrics metrics = canvas.getFontMetrics();
            int textX = x + (TILE_SIZE - metrics.stringWidth(text)) / 2;
            int textY = y + (TILE_SIZE - metrics.getHeight()) / 2 + metrics.getAscent();
            canvas.drawString(text, textX, textY);
        }

        @Override
        public int getIconWidth() {
            return TILE_SIZE;
        }

        @Override
        public int getIconHeight() {
            return TILE_SIZE;
        }
    }
}
