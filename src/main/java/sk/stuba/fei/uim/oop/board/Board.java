package sk.stuba.fei.uim.oop.board;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.List;
import java.util.*;

public class Board extends JPanel {
    private Tile[][] board;
    private int[][] boardVisited;
    private ArrayList<ArrayList<Integer>> test;
    private int boardSize;
    private Tile start;
    private Tile end;
    private boolean doSearch = true;
    public Board(int size) {
        boardSize = size;
        boardVisited = new int[size][size];
        test = new ArrayList<>();

        initBoard(boardSize);
        initStart(7, 7);
        initEnd(0, 0);
        initPath();
        updateBoard();
    }

    private void updateBoard() {
        for (int i=0; i<test.size(); i++) {
            int xValue = test.get(i).get(0);
            int yValue = test.get(i).get(1);

            int xValueNext = test.get(i).get(0);
            int yValueNext = test.get(i).get(1);


            if ((xValue == 0 && yValue == 0) || (xValue == 7 && yValue == 7)) {
                continue;
            }
            else if ((i > 0 && i < test.size() - 1) && (!Objects.equals(test.get(i - 1).get(0), test.get(i + 1).get(0)) && !Objects.equals(test.get(i - 1).get(1), test.get(i + 1).get(1)))) {
                board[xValue][yValue].setType(Type.CURVED_PIPE);
            } else {
                board[xValue][yValue].setType(Type.STRAIGHT_PIPE);
            }

        }
    }

    private void initPath() {
        for (int i = 0; i < boardSize; i++) {
            Arrays.fill(boardVisited[i], 1);
        }

        dfs(7, 7);

        // Set start and end points
        boardVisited[7][7] = 0;
        boardVisited[0][0] = 0;



        for (int i = 0; i < boardSize; i++) {
            Arrays.fill(boardVisited[i], 0);
        }

        for (ArrayList<Integer> a : test) {
            int x = a.get(0);
            int y = a.get(1);
            boardVisited[x][y] = 1;
        }

        for (int i=0; i < 8; i++) {
            for (int j=0; j<8;j ++) {
                System.out.print(boardVisited[i][j]);
            }
            System.out.print("\n");
        }
    }

    private void dfs(int r, int c) {
        // Mark cell as visited
        boardVisited[r][c] = 0;



        if (!doSearch) {
            return;
        }

        ArrayList<Integer> tmp = new ArrayList<>();
        tmp.add(r);
        tmp.add(c);
        test.add(tmp);

        if (r == 0 && c == 0) {

            doSearch = false;
            return;
        }




        // Define possible moves
        int[] rows = {-1, 0, 1, 0};
        int[] cols = {0, 1, 0, -1};

        // Shuffle moves randomly
        ArrayList<Integer> directions = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(directions);

        int counter = 0;
        // Visit all neighbors
        for (int direction : directions) {
            int rr = r + rows[direction];
            int cc = c + cols[direction];

            if (rr < 0 || cc < 0 || rr >= boardSize || cc >= boardSize || boardVisited[rr][cc] == 0) {
                continue;
            }

            counter++;
            // Mark path between current cell and neighbor
            if (direction == 0) {
                boardVisited[r-1][c] = 0;
            } else if (direction == 1) {
                boardVisited[r][c+1] = 0;
            } else if (direction == 2) {
                boardVisited[r+1][c] = 0;
            } else {
                boardVisited[r][c-1] = 0;
            }

            dfs(rr, cc);
        }
        if (counter == 0) {
            test.remove(test.size() - 1);
        }
    }

    private void initBoard(int size) {
        board = new Tile[size][size];

        setBackground(Color.WHITE);
        setBorder(new LineBorder(Color.BLACK, 2));

        setLayout(new GridLayout(size, size));
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                board[row][column] = new Tile();
                board[row][column].setType(Type.EMPTY);
                add(board[row][column]);
            }
        }
    }

    private void initStart(int x, int y) {
        board[x][y].setType(Type.START);
    }

    private void initEnd(int x, int y) {
        board[x][y].setType(Type.END);
    }

    public void clearHighlight() {
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                board[row][column].setHighlight(false);
            }
        }
    }
}

