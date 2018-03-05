package cn.croff.view;

import javax.swing.*;
import java.awt.*;

/**
 * In the application <b>cn.croff.main.Minesweeper</b>, a <b>cn.croff.view.DotButton</b> is a specialized type of
 * <b>JButton</b> that represents a square in the game.
 * It can have a number of possible icons, which are found in the
 * "icons" directory. The icon expresses the state of the dot:
 * covered, number of neighbooring mines, exploded..
 * <p>
 * <p>
 * <p>
 * The icons have been found on <a href=
 * "https://en.wikipedia.org/wiki/Open_content">wikimedia</a>. The author of these
 * icons seems to be someone called
 * <a href="https://commons.wikimedia.org/wiki/User:Cryosta">Kazukiokumura</a>.
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */
public class DotButton extends JButton {

    /**
     * predefined values to capture icons of a cn.croff.model.DotInfo
     */
    public static final int NUMBER_OF_ICONS = 13;
    public static final int ZERO_NEIGHBOURS = 0;
    public static final int ONE_NEIGHBOURS = 1;
    public static final int TWO_NEIGHBOURS = 2;
    public static final int THREE_NEIGHBOURS = 3;
    public static final int FOUR_NEIGHBOURS = 4;
    public static final int FIVE_NEIGHBOURS = 5;
    public static final int SIX_NEIGHBOURS = 6;
    public static final int SEVEN_NEIGHBOURS = 7;
    public static final int EIGHT_NEIGHBOURS = 8;
    public static final int MINED = 9;
    public static final int CLICKED_MINE = 10;
    public static final int COVERED = 11;
    public static final int FLAGGED = 12;

    /**
     * An array is used to cache all the images. Since the images are not
     * modified, all the cells that display the same image reuse the same
     * <b>ImageIcon</b> object. Notice the use of the keyword <b>static</b>.
     */
    private static final ImageIcon[] icons = new ImageIcon[NUMBER_OF_ICONS];

    private int iconNumber; // 用哪个图标
    private int row; // 纵坐标
    private int column; // 横坐标

    /**
     * Constructor used for initializing a cn.croff.view.DotButton at a specific
     * Board location, with a specific icon.
     *
     * @param column     the column of this Cell
     * @param row        the row of this Cell
     * @param iconNumber specifies which iconNumber to use for this cell
     */
    public DotButton(int column, int row, int iconNumber) {
        // 初始化坐标、图标、按钮样式
        super();
        this.column = column;
        this.row = row;
        setIconNumber(iconNumber);
        setMargin(new Insets(0, 0, 0, 0));
        setBorderPainted(false);
    }

    /**
     * Sets the current value of the instance variable iconNumber, and update
     * the iconNumber used by the instance, using the method getImageIcon()
     * to get the new icon.
     *
     * @param iconNumber the iconNumber to use, based on the predifined constant values
     *                   defined in this class
     */
    public void setIconNumber(int iconNumber) {
        // 设置图标
        this.iconNumber = iconNumber;
        setIcon(getImageIcon());
    }

    /**
     * Getter method for the attribute row.
     *
     * @return the value of the attribute row
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter method for the attribute column.
     *
     * @return the value of the attribute column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns the <b>ImageIcon</b> reference to use based on
     * the current value of the variable iconNumber.
     *
     * @return the image to be displayed by the button
     */
    private ImageIcon getImageIcon() {
        if (icons[iconNumber] == null) {
            icons[iconNumber] = new ImageIcon(getClass().getResource("/icons/" + getIconFileName()));
        }
        return icons[iconNumber];
    }

    /**
     * This method returns the name of the file containing the image
     * corresponding to the current value of the variable iconNumber.
     *
     * @return the name of the icon file to be used
     */
    private String getIconFileName() {
        switch (iconNumber) {
            case 0:
                return "Minesweeper_0.png";
            case 1:
                return "Minesweeper_1.png";
            case 2:
                return "Minesweeper_2.png";
            case 3:
                return "Minesweeper_3.png";
            case 4:
                return "Minesweeper_4.png";
            case 5:
                return "Minesweeper_5.png";
            case 6:
                return "Minesweeper_6.png";
            case 7:
                return "Minesweeper_7.png";
            case 8:
                return "Minesweeper_8.png";
            case 9:
                return "Minesweeper_mine.png";
            case 10:
                return "Minesweeper_mineSelected.png";
            case 11:
                return "Minesweeper_unopened_square.png";
            case 12:
                return "Minesweeper_flag.png";
            default:
                System.out.println("Invalid icon number: " + iconNumber);
                return "";
        }
    }
}
