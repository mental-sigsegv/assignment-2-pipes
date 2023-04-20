package sk.stuba.fei.uim.oop.board;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.*;

public class Board extends JPanel {
    private Tile[][] board;
    private int[][] boardVisited;
    private ArrayList<ArrayList<Integer>> test;
    private ArrayList<Integer> startPos;
    private ArrayList<Integer> endPos;
    private int boardSize;
    private boolean doSearch = true;
    public Board(int size) {
        boardSize = size;
        boardVisited = new int[size][size];
        test = new ArrayList<>();

        startPos = new ArrayList<>();
        endPos = new ArrayList<>();

        initBoard(boardSize);

        int randomStart = (int) (Math.random() * boardSize);
        int randomEnd = (int) (Math.random() * boardSize);
        startPos.add(randomStart);
        startPos.add(0);

        endPos.add(randomEnd);
        endPos.add(boardSize-1);

        System.out.println("S: " + randomStart + " | E:" + randomEnd);

        initStart(randomStart, 0);
        initEnd(randomEnd, boardSize-1);

        initPath();
        updateBoard();
    }

    private void updateBoard() {
        for (int i=0; i<test.size(); i++) {
            int xValue = test.get(i).get(0);
            int yValue = test.get(i).get(1);

            int[] angleArray = {0, 90, 180, 270};
            int randomRotation = angleArray[(int) (Math.random() * angleArray.length)];

            if ((xValue == startPos.get(0) && yValue == startPos.get(1)) || (xValue == endPos.get(0) && yValue == endPos.get(1))) {
                board[xValue][yValue].setRotation((board[xValue][yValue].getRotation() + randomRotation)%360);
                continue;
            }
            else if ((i > 0 && i < test.size() - 1) && (!Objects.equals(test.get(i - 1).get(0), test.get(i + 1).get(0)) && !Objects.equals(test.get(i - 1).get(1), test.get(i + 1).get(1)))) {
                board[xValue][yValue].setType(Type.CURVED_PIPE);
            } else {
                board[xValue][yValue].setType(Type.STRAIGHT_PIPE);
            }
            board[xValue][yValue].setRotation((board[xValue][yValue].getRotation() + randomRotation)%360);
        }
    }

    private void initPath() {
        for (int i = 0; i < boardSize; i++) {
            Arrays.fill(boardVisited[i], 1);
        }

        dfs(startPos.get(0), startPos.get(1));

        // Set start and end points
        boardVisited[startPos.get(0)][startPos.get(1)] = 0;
        boardVisited[endPos.get(0)][endPos.get(1)] = 0;

        for (ArrayList<Integer> a : test) {
            int x = a.get(0);
            int y = a.get(1);
            boardVisited[x][y] = 0;
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

        if (r == endPos.get(0) && c == endPos.get(1)) {
            doSearch = false;
            return;
        }

        // Define possible moves
        int[] rows = {-1, 0, 1, 0};
        int[] cols = {0, 1, 0, -1};

        // Shuffle moves randomly
        ArrayList<Integer> directions = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(directions);

        // Visit all neighbors
        for (int direction : directions) {
            int rr = r + rows[direction];
            int cc = c + cols[direction];

            if (rr < 0 || cc < 0 || rr >= boardSize || cc >= boardSize || boardVisited[rr][cc] == 0) {
                continue;
            }

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
        if (test.get(test.size() - 1).get(0) == r && test.get(test.size() - 1).get(1) == c) {
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

