package sk.stuba.fei.uim.oop.board;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class Board extends JPanel {
    private Tile[][] board;
    private int boardSize;
    public Board(int size) {
        boardSize = size;
        initBoard(boardSize);
    }

    private void initBoard(int size) {
        board = new Tile[size][size];

        setBackground(Color.WHITE);
        setBorder(new LineBorder(Color.BLACK, 2));

        setLayout(new GridLayout(size, size));
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                board[row][column] = new Tile();
                add(board[row][column]);
            }
        }
    }

    public void clearHighlight() {
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                board[row][column].setHighlight(false);
            }
        }
    }
}

